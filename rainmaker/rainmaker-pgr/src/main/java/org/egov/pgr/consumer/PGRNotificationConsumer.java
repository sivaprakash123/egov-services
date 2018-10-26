package org.egov.pgr.consumer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.contract.SMSRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.model.ActionInfo;
import org.egov.pgr.model.Service;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.service.GrievanceService;
import org.egov.pgr.service.NotificationService;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.WorkFlowConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class PGRNotificationConsumer {

	@Autowired
	private PGRProducer pGRProducer;

	@Value("${egov.hr.employee.host}")
	private String hrEmployeeHost;
	
	@Value("${egov.hr.employee.v2.host}")
	private String hrEmployeeV2Host;

	@Value("${egov.hr.employee.v2.search.endpoint}")
	private String hrEmployeeV2SearchEndpoint;

	@Value("${kafka.topics.notification.sms}")
	private String smsNotifTopic;

	@Value("${kafka.topics.notification.email}")
	private String emailNotifTopic;

	@Value("${notification.sms.enabled}")
	private Boolean isSMSNotificationEnabled;

	@Value("${notification.email.enabled}")
	private Boolean isEmailNotificationEnabled;

	@Value("${reassign.complaint.enabled}")
	private Boolean isReassignNotifEnaled;

	@Value("${reopen.complaint.enabled}")
	private Boolean isReopenNotifEnaled;

	@Value("${comment.by.employee.notif.enabled}")
	private Boolean isCommentByEmpNotifEnaled;

	@Value("${email.template.path}")
	private String emailTemplatePath;

	@Value("${date.format.notification}")
	private String notificationDateFormat;
	
	@Value("${egov.ui.app.host}")
	private String uiAppHost;
	
	@Value("${egov.ui.feedback.url}")
	private String uiFeedbackUrl;
		
	@Value("${notification.allowed.on.status}")
	private String notificationEnabledStatuses;
	
	@Value("${egov.pgr.app.playstore.link}")
	private String appDownloadLink;

	@Autowired
	private PGRUtils pGRUtils;

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private GrievanceService requestService;

	@KafkaListener(topics = {"${kafka.topics.save.service}","${kafka.topics.update.service}"})

	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		ServiceRequest serviceReqRequest = new ServiceRequest();
		try {
			log.info("Consuming record: " + record);
			serviceReqRequest = mapper.convertValue(record, ServiceRequest.class);
		} catch (final Exception e) {
			log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
		}
		process(serviceReqRequest);
	}

	public void process(ServiceRequest serviceReqRequest) {
		if (!CollectionUtils.isEmpty(serviceReqRequest.getActionInfo())) {
			for (ActionInfo actionInfo : serviceReqRequest.getActionInfo()) {
				if (null != actionInfo && (!StringUtils.isEmpty(actionInfo.getStatus()) || !StringUtils.isEmpty(actionInfo.getComment()))) {
					Service service = serviceReqRequest.getServices()
							.get(serviceReqRequest.getActionInfo().indexOf(actionInfo));
					if (isNotificationEnabled(actionInfo.getStatus(), serviceReqRequest.getRequestInfo().getUserInfo().getType(), actionInfo.getComment(), actionInfo.getAction())) {
						if (isSMSNotificationEnabled) {
							List<SMSRequest> smsRequests = prepareSMSRequest(service, actionInfo, serviceReqRequest.getRequestInfo());
							if (CollectionUtils.isEmpty(smsRequests)) {
								log.info("Messages from localization couldn't be fetched!");
								continue;
							}
							for(SMSRequest smsRequest: smsRequests) {
								pGRProducer.push(smsNotifTopic, smsRequest);
							}
						}
						// Not enabled for now - email notifications to be part of next version of PGR.
						if (isEmailNotificationEnabled
								&& (null != service.getEmail() && !service.getEmail().isEmpty())) {
							//get code from git using any check-in before 24/04/2018.
						}
					} else {
						log.info("Notification disabled for this case!");
						continue;
					}
				} else {
					continue;
				}
			}
		}
	}

	public List<SMSRequest> prepareSMSRequest(Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo) {
		List<SMSRequest> smsRequestsTobeSent = new ArrayList<>();
		if(StringUtils.isEmpty(actionInfo.getAssignee())) {
			actionInfo.setAssignee(notificationService.getCurrentAssigneeForTheServiceRequest(serviceReq, requestInfo));
		}
		for(String role: pGRUtils.getReceptorsOfNotification(actionInfo.getStatus(), actionInfo.getAction())) {
			String phoneNumberRetrived = notificationService.getPhoneNumberForNotificationService(requestInfo, serviceReq.getAccountId(), serviceReq.getTenantId(), actionInfo.getAssignee(), role);
			String phone = StringUtils.isEmpty(phoneNumberRetrived) ? serviceReq.getPhone() : phoneNumberRetrived;
			String message = getMessageForSMS(serviceReq, actionInfo, requestInfo, role);
			if (StringUtils.isEmpty(message))
				continue;
			smsRequestsTobeSent.add(SMSRequest.builder().mobileNumber(phone).message(message).build());
		}
		return smsRequestsTobeSent;
	}

	public String getMessageForSMS(Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo, String role) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(notificationDateFormat);
		String date = dateFormat.format(new Date());
		String tenantId = serviceReq.getTenantId().split("[.]")[0]; // localization values are for now state-level.
		String locale = null;
		try {
			locale = requestInfo.getMsgId().split(",")[1]; // Conventionally locale is sent in the first index of msgid.
			if (StringUtils.isEmpty(locale)) 
				locale = "en_IN";
		} catch (Exception e) {
			locale = "en_IN";
		}
		if (null == NotificationService.localizedMessageMap.get(locale + "|" + tenantId)) // static map that saves code-message pair against locale | tenantId.
			notificationService.getLocalisedMessages(requestInfo, tenantId, locale, PGRConstants.LOCALIZATION_MODULE_NAME);
		Map<String, String> messageMap = NotificationService.localizedMessageMap.get(locale + "|" + tenantId);
		if (null == messageMap)
			return null;
		String serviceType = notificationService.getServiceType(serviceReq, requestInfo, locale);
		return getMessage(serviceType, date, serviceReq, actionInfo, requestInfo, messageMap, role);

	}
	
	public String getMessage(String serviceType, String date, Service serviceReq, ActionInfo actionInfo, RequestInfo requestInfo, Map<String, String> messageMap, String role) {
		if (null == serviceType) {
			return getDefaultMessage(messageMap, actionInfo.getStatus(), actionInfo.getAction(), actionInfo.getComment());
		}
		String text = null;
		String[] reasonForRejection = new String[2];
		Map<String, String> employeeDetails = null;
		String department = null;
		String designation = null;
		if(StringUtils.isEmpty(actionInfo.getStatus()) && !StringUtils.isEmpty(actionInfo.getComment())) {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_COMMENT);
			text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_COMMENT_KEY, actionInfo.getComment())
				.replaceAll(PGRConstants.SMS_NOTIFICATION_USER_NAME_KEY, requestInfo.getUserInfo().getName());
		}else {
			text = messageMap.get(PGRConstants.getStatusRoleLocalizationKeyMap().get(actionInfo.getStatus() + "|" + role));
			if(actionInfo.getStatus().equals(WorkFlowConfigs.STATUS_OPENED)) {
				if (null != actionInfo.getAction() && actionInfo.getAction().equals(WorkFlowConfigs.ACTION_REOPEN)) {
					text = messageMap.get(PGRConstants.getActionRoleLocalizationKeyMap().get(WorkFlowConfigs.ACTION_REOPEN + "|" + role));
					employeeDetails = notificationService.getEmployeeDetails(serviceReq.getTenantId(), actionInfo.getAssignee(), requestInfo);
					text = 	text.replaceAll(PGRConstants.SMS_NOTIFICATION_EMP_NAME_KEY, employeeDetails.get("name"));
				}
			}else if(actionInfo.getStatus().equals(WorkFlowConfigs.STATUS_ASSIGNED)) {
				employeeDetails = notificationService.getEmployeeDetails(serviceReq.getTenantId(), actionInfo.getAssignee(), requestInfo);
				if(null != employeeDetails) {
					department = notificationService.getDepartment(serviceReq, employeeDetails.get("department"), requestInfo);
					designation = notificationService.getDesignation(serviceReq, employeeDetails.get("designation"), requestInfo);
				}else {
					return getDefaultMessage(messageMap, actionInfo.getStatus(), actionInfo.getAction(), actionInfo.getComment());	
				}
				if(null == department || null == designation || null == employeeDetails.get("name"))
					return getDefaultMessage(messageMap, actionInfo.getStatus(), actionInfo.getAction(), actionInfo.getComment());
					
				text = 	text.replaceAll(PGRConstants.SMS_NOTIFICATION_EMP_NAME_KEY, employeeDetails.get("name"))
							.replaceAll(PGRConstants.SMS_NOTIFICATION_EMP_DESIGNATION_KEY, designation)
							.replaceAll(PGRConstants.SMS_NOTIFICATION_EMP_DEPT_KEY, department);	
			}else if(actionInfo.getStatus().equals(WorkFlowConfigs.STATUS_REJECTED)) {
				if(StringUtils.isEmpty(actionInfo.getComment()))
					return getDefaultMessage(messageMap, actionInfo.getStatus(), actionInfo.getAction(), actionInfo.getComment());
				reasonForRejection = actionInfo.getComment().split(";");
				if(reasonForRejection.length < 2)
					return getDefaultMessage(messageMap, actionInfo.getStatus(), actionInfo.getAction(), actionInfo.getComment());	
				log.info("text before: "+text);
				text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_REASON_FOR_REOPEN_KEY, reasonForRejection[0])
						.replaceAll(PGRConstants.SMS_NOTIFICATION_ADDITIONAL_COMMENT_KEY, reasonForRejection[1]);
				log.info("text after: "+text);
			}else if(actionInfo.getStatus().equals(WorkFlowConfigs.STATUS_RESOLVED)) {
				String assignee = notificationService.getCurrentAssigneeForTheServiceRequest(serviceReq, requestInfo);
				employeeDetails = notificationService.getEmployeeDetails(serviceReq.getTenantId(), assignee, requestInfo);
				
				text = 	text.replaceAll(PGRConstants.SMS_NOTIFICATION_EMP_NAME_KEY, employeeDetails.get("name"));
			}if(actionInfo.getStatus().equals(WorkFlowConfigs.STATUS_CLOSED)) {
				ServiceReqSearchCriteria serviceReqSearchCriteria = ServiceReqSearchCriteria.builder().tenantId(serviceReq.getTenantId())
						.serviceRequestId(Arrays.asList(serviceReq.getServiceRequestId())).build();
				ServiceResponse response = (ServiceResponse) requestService.getServiceRequestDetails(requestInfo, serviceReqSearchCriteria);
				List<ActionInfo> actions = response.getActionHistory().get(0).getActions().parallelStream()
						.filter(obj -> !StringUtils.isEmpty(obj.getAssignee())).collect(Collectors.toList());
				employeeDetails = notificationService.getEmployeeDetails(serviceReq.getTenantId(), actions.get(0).getAssignee(), requestInfo);
				text = 	text.replaceAll(PGRConstants.SMS_NOTIFICATION_RATING_KEY, 
						StringUtils.isEmpty(response.getServices().get(0).getRating()) ? "5" : response.getServices().get(0).getRating())
				       .replaceAll(PGRConstants.SMS_NOTIFICATION_EMP_NAME_KEY, employeeDetails.get("name"));
			}
		}
		if(null != text) {
			String ulb = null;
			if(StringUtils.isEmpty(serviceReq.getTenantId().split("[.]")[1]))
				ulb = "Punjab";
			else {
				ulb = StringUtils.capitalize(serviceReq.getTenantId().split("[.]")[1]);
			}
			return text.replaceAll(PGRConstants.SMS_NOTIFICATION_COMPLAINT_TYPE_KEY, serviceType)
				.replaceAll(PGRConstants.SMS_NOTIFICATION_ID_KEY, serviceReq.getServiceRequestId())
				.replaceAll(PGRConstants.SMS_NOTIFICATION_DATE_KEY, date)
				.replaceAll(PGRConstants.SMS_NOTIFICATION_APP_LINK_KEY, uiAppHost + uiFeedbackUrl + serviceReq.getServiceRequestId())
				.replaceAll(PGRConstants.SMS_NOTIFICATION_APP_DOWNLOAD_LINK_KEY, appDownloadLink)
				.replaceAll(PGRConstants.SMS_NOTIFICATION_AO_DESIGNATION, PGRConstants.ROLE_NAME_GRO)
				.replaceAll(PGRConstants.SMS_NOTIFICATION_ULB_NAME, ulb);
			}
			return text;
	}
	
	public String getDefaultMessage(Map<String, String> messageMap, String status, String action, String comment) {
		String text = null;
		if(StringUtils.isEmpty(status) && !StringUtils.isEmpty(comment)) {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_COMMENT_DEFAULT);
		}else {
			text = messageMap.get(PGRConstants.LOCALIZATION_CODE_DEFAULT);
			text = text.replaceAll(PGRConstants.SMS_NOTIFICATION_STATUS_KEY, PGRConstants.getStatusNotifKeyMap().get(status));
			if(status.equals(WorkFlowConfigs.STATUS_OPENED)) {
				if (null != action && action.equals(WorkFlowConfigs.ACTION_REOPEN))
					text = text.replaceAll(PGRConstants.getStatusNotifKeyMap().get(status), PGRConstants.getActionNotifKeyMap().get(WorkFlowConfigs.ACTION_REOPEN));
			}else if(status.equals(WorkFlowConfigs.STATUS_ASSIGNED)) {
				if (null != action && action.equals(WorkFlowConfigs.ACTION_REASSIGN))
					text = text.replaceAll(PGRConstants.getStatusNotifKeyMap().get(status), PGRConstants.getActionNotifKeyMap().get(WorkFlowConfigs.ACTION_REASSIGN));
			}
		}

		return text;
	}
	
	public boolean isNotificationEnabled(String status, String userType, String comment, String action) {
		boolean isNotifEnabled = false;
		List<String> notificationEnabledStatusList = Arrays.asList(notificationEnabledStatuses.split(","));
		if(notificationEnabledStatusList.contains(status)) {
			if(status.equalsIgnoreCase(WorkFlowConfigs.STATUS_OPENED) && action.equals(WorkFlowConfigs.ACTION_REOPEN) && isReopenNotifEnaled) {
						isNotifEnabled = true;	
			}
			if(status.equalsIgnoreCase(WorkFlowConfigs.STATUS_ASSIGNED) && action.equals(WorkFlowConfigs.ACTION_REASSIGN) && isReassignNotifEnaled) {
						isNotifEnabled = true;
			}
			isNotifEnabled= true;
		}
		if ((null != comment && !comment.isEmpty()) && isCommentByEmpNotifEnaled && userType.equalsIgnoreCase("EMPLOYEE")) {
			isNotifEnabled = true;
		}
		return isNotifEnabled;
	}
}

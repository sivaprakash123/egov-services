package org.egov.workflow.web.controller;

import org.egov.workflow.domain.service.PgrWorkflowImpl;
import org.egov.workflow.domain.service.WorkflowMatrixImpl;
import org.egov.workflow.web.contract.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class WorkFlowController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowController.class);

	@Autowired
	private PgrWorkflowImpl pgrWorkflowImpl;

	@Autowired
	private WorkflowMatrixImpl matrixWorkflow;

	@PostMapping(value = "/process/_start")
	public ProcessInstanceResponse startWorkflow(@RequestBody final ProcessInstanceRequest processInstanceRequest) {

		User user = processInstanceRequest.getRequestInfo().getUserInfo();
        LOGGER.info("the request info ::: "+processInstanceRequest.getRequestInfo());
		 

		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();
		processInstance.setSenderName(user.getName());
		
		ProcessInstanceResponse response = ProcessInstanceResponse.builder().build();

		if (processInstance.getBusinessKey().equals("Complaint")) {

			response = pgrWorkflowImpl.start(processInstanceRequest);
			
		}

		else {
			response= matrixWorkflow.start(processInstanceRequest);
			 
		}
		response.setResponseInfo(getResponseInfo(processInstanceRequest.getRequestInfo()));
		return response;
	}

	@PostMapping(value = "/process/_end")
	public ProcessInstanceResponse endWorkflow(@RequestBody final ProcessInstanceRequest processInstanceRequest) {

		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();
		ProcessInstanceResponse response = ProcessInstanceResponse.builder().build();
		if (processInstance.getBusinessKey().equals("Complaint")) {

            pgrWorkflowImpl.end(processInstanceRequest);
		}

		else {
			 matrixWorkflow.end(processInstanceRequest);
		}

		response.setResponseInfo(getResponseInfo(processInstanceRequest.getRequestInfo()));
		return response;
	}

	@PostMapping(value = "/history")
	public TaskResponse getHistory(@RequestBody RequestInfoWrapper requestInfoWrapper ,@RequestParam final String tenantId, @RequestParam final String workflowId) {
		
		List<Task> historyDetail = pgrWorkflowImpl.getHistoryDetail(tenantId, workflowId);
		TaskResponse response=new TaskResponse();
		response.setResponseInfo(getResponseInfo(requestInfoWrapper.getRequestInfo()));
		response.setTasks(historyDetail);
		return response;
	}

	@PostMapping(value = "/tasks/{id}/_update")
	public TaskResponse updateTask(@RequestBody final TaskRequest taskRequest,@PathVariable String id) {
	        LOGGER.info("Update Task request:::::" + taskRequest);
		TaskResponse response=new TaskResponse();
		Task task = taskRequest.getTask();
		task.setId(id);
		if (task.getBusinessKey().equals("Complaint")) {
			response= pgrWorkflowImpl.update(taskRequest);
		}

		else {
			response= matrixWorkflow.update(taskRequest);
		}
		 
		response.setResponseInfo(getResponseInfo(taskRequest.getRequestInfo()));
		
		return response;
	}

	@PostMapping(value = "/tasks/_search")
	public TaskResponse getTasks(@RequestBody final RequestInfoWrapper requestInfoWrapper, @ModelAttribute Task task) {
		TaskRequest taskRequest=new TaskRequest();
		taskRequest.setRequestInfo(requestInfoWrapper.getRequestInfo());
		taskRequest.setTask(task);		
		TaskResponse	response=	matrixWorkflow.getTasks(taskRequest);
		response.setResponseInfo(getResponseInfo(requestInfoWrapper.getRequestInfo()));
		return response;
	}

	@PostMapping(value = "/process/_search")
	public ProcessInstanceResponse getProcess(@RequestBody RequestInfoWrapper requestInfoWrapper, @RequestParam String tenantId,
			@RequestParam String id) {

		ProcessInstance p = new ProcessInstance();
		ProcessInstanceResponse pres = ProcessInstanceResponse.builder().build();

		p = p.builder().id(id).build();

		p = matrixWorkflow.getProcess(tenantId, p, requestInfoWrapper.getRequestInfo());
		pres.setProcessInstance(p);
		pres.setResponseInfo(getResponseInfo(requestInfoWrapper.getRequestInfo()));
		LOGGER.info("The response  owner value before sending :::"+pres.getProcessInstance().getOwner());
		return pres;

	}

	@PostMapping(value = "/designations/_search")
	public List<Designation> getDesignationsByObjectType(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam final String departmentRule,
			@RequestParam final String currentStatus,
			@RequestParam final String businessKey,
			@RequestParam final String amountRule,
			@RequestParam final String additionalRule,
			@RequestParam final String pendingAction,
			@RequestParam final String approvalDepartmentName,
			@RequestParam String designation
						) {

		Task t = new Task();
		t = t.builder().action(pendingAction).businessKey(businessKey).attributes(new HashMap<>()).status(currentStatus)
				.build();

		Attribute amountRuleAtt = new Attribute();
		amountRuleAtt.setCode(amountRule);
		t.getAttributes().put("amountRule", amountRuleAtt);

		Attribute additionalRuleAtt = new Attribute();
		amountRuleAtt.setCode(additionalRule);
		t.getAttributes().put("additionalRule", additionalRuleAtt);

		Attribute designationAtt = new Attribute();
		amountRuleAtt.setCode(designation);
		t.getAttributes().put("designation", designationAtt);
		
		t.setTenantId(requestInfoWrapper.getRequestInfo().getTenantId());
        t.setStatus(currentStatus);
		return matrixWorkflow.getDesignations(t, approvalDepartmentName);

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder")
                .status("placeholder").build();
	}

}

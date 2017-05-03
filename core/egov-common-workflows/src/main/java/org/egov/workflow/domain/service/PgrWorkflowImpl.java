package org.egov.workflow.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.persistence.service.ComplaintRouterService;
import org.egov.workflow.persistence.service.StateService;
import org.egov.workflow.web.contract.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgrWorkflowImpl implements Workflow {

	private static Logger LOG = LoggerFactory.getLogger(WorkflowMatrixImpl.class);

	public static final String STATE_ID = "stateId";
	public static final String DEPARTMENT = "department";
	public static final String IN_PROGRESS = "IN PROGRESS";
	private final ComplaintRouterService complaintRouterService;
	private final StateService stateService;
	private final EmployeeRepository employeeRepository;
	private final UserRepository userRepository;
	public static final String STATE_DETAILS = "stateDetails";

	@Autowired
	public PgrWorkflowImpl(final ComplaintRouterService complaintRouterService, final StateService stateService,
					   final EmployeeRepository employeeRepository, final UserRepository userRepository) {
		this.complaintRouterService = complaintRouterService;
		this.stateService = stateService;
		this.employeeRepository = employeeRepository;
		this.userRepository = userRepository;
	}


    @Override
	public ProcessInstanceResponse start(final ProcessInstanceRequest processInstanceRequest) {
		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();
		RequestInfo requestInfo = processInstanceRequest.getRequestInfo();
		final State state = new State();
		state.setType(processInstance.getType());
		state.setSenderName(processInstance.getSenderName());
		state.setStatus(State.StateStatus.INPROGRESS);
		state.setValue(processInstance.getStatus());
		state.setComments("Complaint is created with CRN : " + processInstance.getValueForKey("crn"));
		state.setNatureOfTask("Grievance");
		Position position = resolveAssignee(processInstance,requestInfo);
        position = Position.builder().id(1L).build();
		state.setOwnerPosition(position.getId());
		state.setExtraInfo(processInstance.getValueForKey("statusDetails"));
		state.setDateInfo(processInstance.getCreatedDate());
		state.setTenantId(processInstance.getTenantId());
		setAuditableFields(state, Long.valueOf(processInstanceRequest.getRequestInfo().getUserInfo().getId()));
		stateService.create(state);
		final Value value = new Value(STATE_ID, String.valueOf(state.getId()));
		final List<Value> values = Collections.singletonList(value);
		final Attribute attribute = new Attribute(true, STATE_ID, "String", true, "This is the id of state", values);
		processInstance.getAttributes().put(STATE_ID, attribute);
		processInstance.setAssignee(position);

		ProcessInstanceResponse response = ProcessInstanceResponse.builder().processInstance(processInstance).build();
		return response;
	}

	private void setAuditableFields(State state, Long requesterId) {
		if (state.getCreatedBy() == null)
			state.setCreatedBy(requesterId);
		if (state.getCreatedDate() == null)
			state.setCreatedDate(new Date());
		state.setLastModifiedDate(new Date());
		state.setLastModifiedBy(requesterId);
	}

	public ProcessInstanceResponse end(final ProcessInstanceRequest processInstanceRequest) {
		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();
		final Long stateId = Long.valueOf(processInstance.getValueForKey(STATE_ID));
		final State state = stateService.getStateByIdAndTenantId(stateId,processInstance.getTenantId());
		if (Objects.nonNull(state)) {
			state.addStateHistory(new StateHistory(state));
			state.setStatus(State.StateStatus.ENDED);
			state.setValue(processInstance.getStatus());
			state.setComments(processInstance.getComments());
			state.setSenderName(processInstance.getSenderName());
			state.setDateInfo(processInstance.getCreatedDate());
			state.setTenantId(processInstance.getTenantId());
			// TODO OWNER POSITION condition to be checked
			UserResponse user = userRepository
					.findUserByIdAndTenantId(Long.valueOf(processInstanceRequest.getRequestInfo().getUserInfo().getId()),processInstance.getTenantId());
			if (user.isGrievanceOfficer()) {
				state.setOwnerPosition(state.getOwnerPosition());
			}
			setAuditableFields(state, Long.valueOf(processInstanceRequest.getRequestInfo().getUserInfo().getId()));
			stateService.update(state);
			processInstance.setStateId(state.getId());
			processInstance.setAssignee(Position.builder().id(state.getOwnerPosition()).build());
		}
        ProcessInstanceResponse response = ProcessInstanceResponse.builder().processInstance(processInstance).build();
		return response;
	}

	private Position resolveAssignee(final ProcessInstance processInstance,final RequestInfo requestInfo) {
		final String complaintTypeCode = processInstance.getValueForKey("complaintTypeCode");
		final Long boundaryId = Long.valueOf(processInstance.getValueForKey("boundaryId"));
		final Long employeeId = Long.valueOf(processInstance.getValueForKey("userId"));
		final Long firstTimeAssignee = null;
		final Position position = complaintRouterService.getAssignee(boundaryId, complaintTypeCode,
				firstTimeAssignee,employeeId,processInstance.getTenantId(),requestInfo);
		return position;
	}

	/*@Override
	public Position getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId,final String tenantId,null) {
		return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId,tenantId,null);
	}*/

	@Override
	public List<Task> getHistoryDetail(final String tenantId, final String workflowId) {
		LOG.debug("Starting getHistoryDetail for " + workflowId + " for tenant " + tenantId);
		final List<Task> tasks = new ArrayList<Task>();
		Task t;
		final State state = stateService.getStateByIdAndTenantId(Long.valueOf(workflowId),tenantId);
		final Set<StateHistory> history = state.getHistory();
		for (final StateHistory stateHistory : history) {
			t = stateHistory.map();
			tasks.add(t);
		}
		t = state.map();
		tasks.add(t);
		LOG.debug("getHistoryDetail for " + workflowId + " for tenant " + tenantId + "completed.");
		if (LOG.isTraceEnabled()) {
			LOG.trace(tasks.toString());
		}
		return tasks;
	}

	private Attribute putDepartmentValues(final String departmentName) {
		final Value value = new Value(DEPARTMENT, departmentName);
		final List<Value> values = Collections.singletonList(value);

		return Attribute.builder().values(values).build();
	}

	/*@Override
	public TaskResponse update(final TaskRequest taskRequest) {
		Task task = taskRequest.getTask();
		final Long stateId = Long.valueOf(task.getValueForKey(STATE_ID));
		final Long employeeId = Long.valueOf(task.getValueForKey("userId"));
		final State state = stateService.getStateByIdAndTenantId(stateId,tenantId);
		if (Objects.nonNull(state)) {
			state.addStateHistory(new StateHistory(state));
			state.setValue(task.getStatus());
			state.setComments(task.getValueForKey("approvalComments"));
			state.setSenderName(task.getSender());
			//Logic to handle escalation
			if(null == task.getAssignee()) {
				state.setOwnerPosition(getAssignee(null, task.getComplaintTypeCode(), task.getPreviousAssignee(),tenantId,employeeId).getId());
				state.setPreviousOwner(task.getPreviousAssignee());
				state.setValue(IN_PROGRESS);
			}
			else
				state.setOwnerPosition(Long.valueOf(task.getAssignee()));
			state.setExtraInfo(task.getValueForKey(STATE_DETAILS));
			state.setDateInfo(task.getCreatedDate());
			state.setTenantId(tenantId);
			setAuditableFields(state, Long.valueOf(task.getRequestInfo().getUserInfo().getId()));
			stateService.update(state);
			if (state.getId() != null) {
				task.setId(state.getId().toString());
				task.setAssignee(state.getOwnerPosition().toString());
			}
		}
		TaskResponse response = new TaskResponse();
		response.setTask(task);
		return response;
	}
*/
	@Override
	public ProcessInstance getProcess(String jurisdiction, ProcessInstance processInstance, final RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public ProcessInstance update(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskResponse update(TaskRequest taskRequest) {
		return null;
	}

	/*@Override
	public List<Task> getHistoryDetail(final String tenantId, final String workflowId) {
		final List<Task> tasks = new ArrayList<Task>();
		Task t;
		final State state = stateService.findOne(Long.valueOf(workflowId));
		final Set<StateHistory> history = state.getHistory();
		for (final StateHistory stateHistory : history) {
			t = stateHistory.map();
			tasks.add(t);
		}
		t = state.map();
		tasks.add(t);
		return tasks;
	}*/

	@Override
	public List<Designation> getDesignations(Task t, String departmentName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAssignee(Long locationId, String complaintTypeId, Long assigneeId, RequestInfo requestInfo) {
		return null;
	}

	@Override
	public TaskResponse getTasks(TaskRequest taskRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}

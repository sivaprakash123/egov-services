package org.egov.workflow.web.controller;

import org.egov.workflow.Resources;
import org.egov.workflow.domain.service.PgrWorkflowImpl;
import org.egov.workflow.domain.service.Workflow;
import org.egov.workflow.domain.service.WorkflowMatrixImpl;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.persistence.service.ComplaintRouterService;
import org.egov.workflow.persistence.service.StateService;
import org.egov.workflow.web.contract.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkFlowController.class)
public class WorkFlowControllerTest {

    private static final String TENANT_ID = "tenantId";
    private static final String STATE_ID = "stateId";
    private static final String APPROVAL_COMMENTS = "approvalComments";
    private Resources resources = new Resources();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PgrWorkflowImpl pgrWorkflowImpl;

    @MockBean
    private WorkflowMatrixImpl matrixWorkflow;

    @Test
    public void test_should_create_workflow() throws Exception {

        ProcessInstanceRequest processInstanceRequest = ProcessInstanceRequest.builder().processInstance(getProcessInstance()).build();
        ProcessInstanceResponse processInstanceResponse = getProcessInstanceResponse();
        when(pgrWorkflowImpl.start(argThat(new ProcessInstanceMatcher(processInstanceRequest))))
                .thenReturn(processInstanceResponse);

        mockMvc.perform(post("/process/_start")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("startWorkflowRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    private ProcessInstanceResponse getProcessInstanceResponse() {
      return ProcessInstanceResponse.builder().processInstance(getProcessInstance()).responseInfo(getResponseInfo()).build();
    }

    private ResponseInfo getResponseInfo() {
        return ResponseInfo.builder().build();
    }

   // @Test
    public void test_should_close_workflow() throws Exception {
        final ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest();

        when(pgrWorkflowImpl.end(argThat(new processInstanceMatcherForCloseWorkflow(processInstanceRequest))))
            .thenReturn(getProcessInstanceResponse());

        mockMvc.perform(post("/close")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("closeWorkflowRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    private ProcessInstanceRequest getProcessInstanceRequest() {
        return ProcessInstanceRequest.builder().processInstance(getProcessInstanceToStart()).requestInfo(getRequestInfo()).build();
    }

    private ProcessInstance getProcessInstanceToStart()  {
       return ProcessInstance.builder()
                .assignee(Position.builder().id(2L).build())
                .businessKey("765")
                .type("Complaint")
                .details("Grievance registered successfully")
                .senderName("harry")
                .tenantId("tenantId")
                .build();
    }
    private RequestInfo getRequestInfo() {
        return RequestInfo.builder().userInfo(User.builder().id(1L).name("Narasappa").build()).build();
    }

    private ProcessInstance getProcessInstance() {
        return ProcessInstance.builder()
                .assignee(Position.builder().id(2L).build())
                .businessKey("Complaint")
                .type("Complaint")
                .details("Workflow Terminated")
                .senderName("garry")
                .tenantId("tenantId")
                .status("REGISTERED")
                .build();
    }

    class ProcessInstanceMatcher extends ArgumentMatcher<ProcessInstanceRequest> {

        public static final String BOUNDARY_ID = "boundaryId";
        public static final String COMPLAINT_TYPE_CODE = "complaintTypeCode";

        private ProcessInstanceRequest expectedProcessInstance;

        public ProcessInstanceMatcher(ProcessInstanceRequest expectedProcessInstance) {

            this.expectedProcessInstance = expectedProcessInstance;
        }

        @Override
        public boolean matches(Object o) {
            final ProcessInstanceRequest actualProcessInstanceRequest = (ProcessInstanceRequest) o;
            ProcessInstance actualProcessInstance = actualProcessInstanceRequest.getProcessInstance();
            return expectedProcessInstance.getProcessInstance().getAssignee().equals(actualProcessInstance.getAssignee()) &&
                expectedProcessInstance.getProcessInstance().getBusinessKey().equals(actualProcessInstance.getBusinessKey()) &&
                expectedProcessInstance.getProcessInstance().getType().equals(actualProcessInstance.getType()) &&
                expectedProcessInstance.getProcessInstance().getDetails().equals(actualProcessInstance.getDetails()) &&
                expectedProcessInstance.getProcessInstance().getSenderName().equals(actualProcessInstance.getSenderName()) &&
                    isValuesValid(actualProcessInstance);
        }

        private boolean isValuesValid(ProcessInstance processInstance) {
            Map<String, Attribute> attributesMap = processInstance.getAttributes();
            if (attributesMap.get(COMPLAINT_TYPE_CODE).getValues().size() != 1)
                return false;
            Value complaintType = attributesMap.get(COMPLAINT_TYPE_CODE).getValues().get(0);
            if (attributesMap.get(BOUNDARY_ID).getValues().size() != 1)
                return false;
            Value boundary = attributesMap.get(BOUNDARY_ID).getValues().get(0);
            return (complaintType.getName().equals("PHDMG") && boundary.getName().equals("173"));
        }
    }

    class processInstanceMatcherForCloseWorkflow extends ArgumentMatcher<ProcessInstanceRequest> {

        public static final String APPROVAL_COMMENTS = "approvalComments";
        public static final String STATE_ID = "stateId";
        private ProcessInstanceRequest expectedProcessInstance;

        public processInstanceMatcherForCloseWorkflow(ProcessInstanceRequest expectedProcessInstance) {

            this.expectedProcessInstance = expectedProcessInstance;
        }

        @Override
        public boolean matches(Object o) {
            final ProcessInstanceRequest actualProcessInstanceRequest = (ProcessInstanceRequest) o;
            ProcessInstance actualProcessInstance = actualProcessInstanceRequest.getProcessInstance();
            return expectedProcessInstance.getProcessInstance().getAssignee().equals(actualProcessInstance.getAssignee()) &&
                expectedProcessInstance.getProcessInstance().getBusinessKey().equals(actualProcessInstance.getBusinessKey()) &&
                expectedProcessInstance.getProcessInstance().getType().equals(actualProcessInstance.getType()) &&
                expectedProcessInstance.getProcessInstance().getDetails().equals(actualProcessInstance.getDetails()) &&
                expectedProcessInstance.getProcessInstance().getSenderName().equals(actualProcessInstance.getSenderName()) &&
                    isValuesValid(actualProcessInstance);
        }

        private boolean isValuesValid(ProcessInstance processInstance) {
            Map<String, Attribute> attributesMap = processInstance.getAttributes();
            if (attributesMap.get(APPROVAL_COMMENTS).getValues().size() != 1)
                return false;
            Value comments = attributesMap.get(APPROVAL_COMMENTS).getValues().get(0);
            if (attributesMap.get(STATE_ID).getValues().size() != 1)
                return false;
            Value state = attributesMap.get(STATE_ID).getValues().get(0);
            return (comments.getName().equals("complaint closed") && state.getName().equals("119"));
        }
    }

   // @Test
    public void testGetWorkFlowHistoryFailsWithoutJurisdictionId() throws Exception {
        mockMvc.perform(get("/history")).andExpect(status().isBadRequest());
    }

    /*@Test
    public void testGetWorkFlowHistoryById() throws Exception {
        final List<Task> history = getWorkFlowHistory();
        when(workflow.getHistoryDetail(TENANT_ID, "2"))
            .thenReturn(history);

        mockMvc.perform(get("/history")
            .param("tenantId", TENANT_ID)
            .param("workflowId", "2"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().json(resources.getFileContents("historyResponse.json")));
    }

    private List<Task> getWorkFlowHistory() {
        final Task history1 = Task.builder()
            .requestInfo(getRequestInfo())
            .owner("Owner1")
            .sender("sender1")
            .status("Created")
            .tenantId("ap.public")
            .comments("Got workflow history 1")
            // .createdDate(new Date("2016-08-31T10:46:22.083"))
            .build();
        final Task history2 = Task.builder()
            .requestInfo(getRequestInfo())
            .owner("Owner2")
            .sender("sender2")
            .status("Closed")
            .tenantId("ap.public")
            .comments("Got workflow history 2")
            // .createdDate(new Date("2016-08-31T10:46:22.083"))
            .build();
        return Arrays.asList(history1, history2);
    }

    private RequestInfo getRequestInfo() {
        return RequestInfo.builder().ver("1").apiId("").msgId("").build();
    }

    @Test
    public void testShouldUpdateWorkflow() throws Exception {
        Task task = getTask();
        when(workflow.update(eq(TENANT_ID), argThat(new TaskMatcherForUpdateWorkflow(task))))
            .thenReturn(task);
        mockMvc.perform(post("/task")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateWorkflowRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    class TaskMatcherForUpdateWorkflow extends ArgumentMatcher<Task> {

        private Task expectedTaskInstance;

        public TaskMatcherForUpdateWorkflow(Task expectedTaskInstance) {

            this.expectedTaskInstance = expectedTaskInstance;
        }

        @Override
        public boolean matches(Object o) {
            final Task actualTaskInstance = (Task) o;
            return expectedTaskInstance.getStatus().equals(actualTaskInstance.getStatus()) &&
                expectedTaskInstance.getAssignee().equals(actualTaskInstance.getAssignee()) &&
                expectedTaskInstance.getSender().equals(actualTaskInstance.getSender()) &&
                isValuesValid(actualTaskInstance);
        }

        private boolean isValuesValid(Task task) {
            Map<String, Attribute> attributesMap = task.getAttributes();
            if (attributesMap.get(APPROVAL_COMMENTS).getValues().size() != 1)
                return false;
            Value comments = attributesMap.get(APPROVAL_COMMENTS).getValues().get(0);
            if (attributesMap.get(STATE_ID).getValues().size() != 1)
                return false;
            Value state = attributesMap.get(STATE_ID).getValues().get(0);
            return (comments.getName().equals("complaint is updated") && state.getName().equals("2"));
        }

    }

    private Task getTask() {
        final RequestInfo requestInfo = RequestInfo.builder()
            .ver("1")
            .apiId("apiId")
            .msgId("")
            .build();

        return Task.builder()
            .assignee("2")
            .sender("narasappa")
            .status("PROCESSING")
            .tenantId("tenantId")
            .requestInfo(requestInfo)
            .build();
    }*/
}
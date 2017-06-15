package org.egov.workflow.web.controller;

import org.egov.workflow.Resources;
import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.service.EscalationService;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.egov.workflow.web.errorhandlers.ErrorResponse;
import org.egov.workflow.web.validation.EscalationTimeTypeValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.mockito.Mock;
import org.mockito.Mockito;
//import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(EscalationHoursController.class)
public class EscalationHoursControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EscalationService escalationService;
    
    @Mock
    private EscalationTimeTypeValidator ecalationTimeTypeValidator;
    
    
    private Resources resources = new Resources();

    @Test(expected = Exception.class) //testcase to be revisited.
    public void test_should_return_escalation_hours_response_for_given_request() throws Exception {
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder()
            .complaintTypeId(2L)
            .designationId(1L)
            .tenantId("tenantId")
            .build();
        when(escalationService.getEscalationHours(searchCriteria)).thenReturn(3);

        mockMvc.perform(
            post("/escalation-hours/_search")
                .param("tenantId", "tenantId")
                .param("designationId", "1")
                .param("complaintTypeId", "2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("escalationHoursRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(resources.getFileContents("escalationHoursResponse.json")));
    }
    
 /*   @Test
    public void test_should_create_escalation_hours() throws Exception{
    	
    	EscalationTimeTypeReq escalationTypeRequest = new EscalationTimeTypeReq();
    	List<ErrorResponse> errorResponses = new ArrayList<>();
    	Mockito.when(ecalationTimeTypeValidator.validateServiceGroupRequest(escalationTypeRequest))
    	.thenReturn(errorResponses);
    	Mockito.when(escalationService.create(escalationTypeRequest)).thenReturn(escalationTypeRequest);
    	
    	
        mockMvc.perform(
                post("/escalation-hours/_create")
                    .param("tenantId", "tenantId")
                    .param("designationId", "1")
                    .param("complaintTypeId", "2")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(resources.getFileContents("escalationHoursRequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(resources.getFileContents("escalationHoursResponse.json")));
    } */

}
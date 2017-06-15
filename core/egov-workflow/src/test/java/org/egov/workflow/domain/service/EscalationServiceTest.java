package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.persistence.repository.EscalationRepository;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EscalationServiceTest {

    @Mock
    private EscalationRepository escalationRepository;

    @InjectMocks
    private EscalationService escalationService;

    @Test
    public void test_should_return_escalation_hours_for_given_criteria() {
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder().build();
        final int expectedEscalationHours = 3;
        when(escalationRepository.getEscalationHours(searchCriteria)).thenReturn(expectedEscalationHours);

        final int actualEscalationHours = escalationService.getEscalationHours(searchCriteria);

        assertEquals(expectedEscalationHours, actualEscalationHours);
    }
    
    @Test
    public void test_should_persist_create_escalationtime(){
		EscalationTimeTypeReq response = new EscalationTimeTypeReq();
		Mockito.when(escalationRepository.persistCreateEscalationTimeType(response)).thenReturn(response);
		
		EscalationTimeTypeReq result = escalationService.create(response);
		
		assertEquals(response, result);
    }
    
    
    @Test
    public void test_should_persist_update_escalationtime(){
		EscalationTimeTypeReq response = new EscalationTimeTypeReq();
		Mockito.when(escalationRepository.persistUpdateEscalationTimeType(response)).thenReturn(response);
		
		EscalationTimeTypeReq result = escalationService.update(response);
		
		assertEquals(response, result);
    }
}
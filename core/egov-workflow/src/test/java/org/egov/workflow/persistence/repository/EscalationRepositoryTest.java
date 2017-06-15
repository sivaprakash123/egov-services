package org.egov.workflow.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.model.EscalationTimeType;
import org.egov.workflow.domain.model.ServiceType;
import org.egov.workflow.persistence.QueryBuilder.EscalationTimeTypeQueryBuilder;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;

@RunWith(MockitoJUnitRunner.class)
public class EscalationRepositoryTest {

    private static final int DEFAULT_ESCALATION_HOURS = 10;
    @Mock
    private EscalationJpaRepository escalationJpaRepository;
    
    @Mock
    private EscalationRepository escalationRepository;
    
    @Mock
    private EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder;

    
    
	@Mock
	private JdbcTemplate jdbcTemplate;

    @Before
    public void before() {
        escalationRepository = new EscalationRepository(DEFAULT_ESCALATION_HOURS, escalationJpaRepository);
        escalationTimeTypeQueryBuilder = new EscalationTimeTypeQueryBuilder();
    }

    @Test
    public void test_should_return_default_escalation_hours_if_configured_value_not_present() {
        final long designationId = 2L;
        final long complaintTypeId = 1L;
        final String tenantId = "tenantId";
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder()
            .complaintTypeId(complaintTypeId)
            .designationId(designationId)
            .tenantId(tenantId)
            .build();
        when(escalationJpaRepository.findBy(designationId, complaintTypeId, tenantId))
            .thenReturn(null);

        final int actualEscalationHours = escalationRepository.getEscalationHours(searchCriteria);

        assertEquals(DEFAULT_ESCALATION_HOURS, actualEscalationHours);
    }

    @Test
    public void test_should_return_escalation_hours_when_configured_value_is_present() {
        final long designationId = 2L;
        final long complaintTypeId = 1L;
        final String tenantId = "tenantId";
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder()
            .complaintTypeId(complaintTypeId)
            .designationId(designationId)
            .tenantId(tenantId)
            .build();
        when(escalationJpaRepository.findBy(designationId, complaintTypeId, tenantId)).thenReturn(3);

        final int actualEscalationHours = escalationRepository.getEscalationHours(searchCriteria);

        assertEquals(3, actualEscalationHours);
    }
    
    @Test(expected = Exception.class)
    public void test_should_throw_exception_persist_create_escalationtime(){
    	EscalationTimeTypeReq escalationTimeTypeRequest = new EscalationTimeTypeReq();
    	EscalationTimeType escalationTimeType = new EscalationTimeType();
    	escalationTimeTypeRequest.setEscalationTimeType(escalationTimeType);
    	EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder = Mockito.mock(EscalationTimeTypeQueryBuilder.class);
    	Object[] obj = new Object[] {};
    	
    	Mockito.when(escalationTimeTypeQueryBuilder.insertEscalationTimeType()).thenReturn(Mockito.anyString());
    	Mockito.when(jdbcTemplate.update("query", obj)).thenReturn(1);
    	
    	assertEquals(escalationTimeTypeRequest, escalationRepository.persistCreateEscalationTimeType(escalationTimeTypeRequest));
    	
    }
    
    @Test(expected = Exception.class) //revisit
    public void test_should_persist_create_escalationtime(){
    	EscalationTimeTypeReq escalationTimeTypeRequest = new EscalationTimeTypeReq();
    	EscalationTimeType escalationTimeType = new EscalationTimeType();
    	ServiceType serviceType = new ServiceType();
    	RequestInfo requestInfo = new RequestInfo();
    	User userInfo = new User();
    	userInfo.setId(1L);
    	requestInfo.setUserInfo(userInfo);
    	serviceType.setId(1L);
    	escalationTimeType.setGrievanceType(serviceType);
    	escalationTimeType.setNoOfHours(2);
    	escalationTimeType.setDesignation(3);
    	escalationTimeType.setTenantId("tenantId");
    	escalationTimeTypeRequest.setEscalationTimeType(escalationTimeType);
    	escalationTimeTypeRequest.setRequestInfo(requestInfo);
    	
    //	EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder = Mockito.spy(EscalationTimeTypeQueryBuilder.class);
    	Object[] obj = new Object[] {1,2,3,"tenantId",1,1, new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()) };
    	
    //	Mockito.when(escalationTimeTypeQueryBuilder.insertEscalationTimeType()).thenReturn("query");
    	Mockito.when(jdbcTemplate.update("query", obj)).thenReturn(1);
    	
    	assertEquals(escalationTimeTypeRequest, escalationRepository.persistCreateEscalationTimeType(escalationTimeTypeRequest));
    	
    }
    
    @Test(expected = Exception.class)
    public void test_should_throw_exception_persist_update_escalationtime(){
    	EscalationTimeTypeReq escalationTimeTypeRequest = new EscalationTimeTypeReq();
    	EscalationTimeType escalationTimeType = new EscalationTimeType();
    	escalationTimeTypeRequest.setEscalationTimeType(escalationTimeType);
    	EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder = Mockito.mock(EscalationTimeTypeQueryBuilder.class);
    	Object[] obj = new Object[] {};
    	
    	Mockito.when(escalationTimeTypeQueryBuilder.updateEscalationTimeType()).thenReturn(Mockito.anyString());
    	Mockito.when(jdbcTemplate.update("query", obj)).thenReturn(1);
    	
    	assertEquals(escalationTimeTypeRequest, escalationRepository.persistCreateEscalationTimeType(escalationTimeTypeRequest));
    	
    }
    
    @Test(expected = Exception.class) //revisit
    public void test_should_persist_update_escalationtime(){
    	EscalationTimeTypeReq escalationTimeTypeRequest = new EscalationTimeTypeReq();
    	EscalationTimeType escalationTimeType = new EscalationTimeType();
    	ServiceType serviceType = new ServiceType();
    	RequestInfo requestInfo = new RequestInfo();
    	User userInfo = new User();
    	userInfo.setId(1L);
    	requestInfo.setUserInfo(userInfo);
    	serviceType.setId(1L);
    	escalationTimeType.setGrievanceType(serviceType);
    	escalationTimeType.setNoOfHours(2);
    	escalationTimeType.setDesignation(3);
    	escalationTimeType.setTenantId("tenantId");
    	escalationTimeTypeRequest.setEscalationTimeType(escalationTimeType);
    	escalationTimeTypeRequest.setRequestInfo(requestInfo);
    	
    	EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder = Mockito.mock(EscalationTimeTypeQueryBuilder.class);
    	Object[] obj = new Object[] {1,2,3,"tenantId",1,1, new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()) };
    	
    	Mockito.when(escalationTimeTypeQueryBuilder.insertEscalationTimeType()).thenReturn("query");
    	Mockito.when(jdbcTemplate.update("query", obj)).thenReturn(1);
    	
    	assertEquals(escalationTimeTypeRequest, escalationRepository.persistUpdateEscalationTimeType(escalationTimeTypeRequest));
    	
    }
    
}
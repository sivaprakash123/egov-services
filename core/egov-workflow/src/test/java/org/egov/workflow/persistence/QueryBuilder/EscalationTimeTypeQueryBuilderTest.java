package org.egov.workflow.persistence.QueryBuilder;

import static org.junit.Assert.*;

import org.egov.workflow.persistence.repository.EscalationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EscalationTimeTypeQueryBuilderTest {

    @Mock
    private EscalationRepository escalationRepository;
    
    @Mock
    private EscalationTimeTypeQueryBuilder escalationQueryBuilder;
    
	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void test_should_return_insert_query(){
		
		String query = "INSERT INTO egpgr_escalation(complaint_type_id, no_of_hrs, designation_id, tenantid, createdby"
				+ ", lastmodifiedby, createddate, lastmodifieddate) VALUES(?,?,?,?,?,?,?,?)";
		
		EscalationTimeTypeQueryBuilder escalationQueryBuilder = new EscalationTimeTypeQueryBuilder();
		String result = escalationQueryBuilder.insertEscalationTimeType();
		
		assertEquals(query, result);
		
	}
	
	@Test
	public void test_should_return_update_query(){
		
		String query = "UPDATE egpgr_escalation set complaint_type_id = ?, no_of_hrs = ?, designation_id = ?, tenantid = ?, "
				+ "createdby = ?, lastmodifiedby = ?, createddate = ?, lastmodifieddate = ? where id = ?";
		
		EscalationTimeTypeQueryBuilder escalationQueryBuilder = new EscalationTimeTypeQueryBuilder();
		String result = escalationQueryBuilder.updateEscalationTimeType();
		
		assertEquals(query, result);
		
	}
}

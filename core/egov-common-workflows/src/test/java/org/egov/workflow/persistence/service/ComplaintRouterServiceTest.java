package org.egov.workflow.persistence.service;

import org.egov.workflow.persistence.entity.ComplaintRouter;
import org.egov.workflow.persistence.repository.*;
import org.egov.workflow.web.contract.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintRouterServiceTest {

    private ComplaintRouterService complaintRouterService;

    @Mock
    private ComplaintRouterRepository complaintRouterRepository;

    @Mock
    private ComplaintTypeRepository complaintTypeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private BoundaryRepository boundaryRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionHierarchyRepository positionHierarchyRepository;

    @Before
    public void before() {
        complaintRouterService = new ComplaintRouterService(complaintRouterRepository, boundaryRepository, positionRepository,
                complaintTypeRepository, employeeRepository, positionHierarchyRepository);
        final ComplaintTypeResponse complaintType = getComplaintType();
        final ComplaintRouter router = getComplaintRouter();
        when(complaintTypeRepository.fetchComplaintTypeByCode(eq("C001"),eq("tenantId"),any())).thenReturn(complaintType);
        when(complaintRouterRepository.findByComplaintTypeAndBoundary(complaintType.getId(), 1L)).thenReturn(router);
        when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(router);
        when(complaintRouterRepository.findCityAdminGrievanceOfficer("ADMINISTRATION")).thenReturn(router);
        when(positionHierarchyRepository.getByObjectTypeObjectSubTypeAndFromPosition("Complaint", "C001", 2L))
                .thenReturn(getPositionHierarchies());
        when(positionRepository.getByEmployeeCode(eq("101010"),any())).thenReturn(getPositions());
        when(positionRepository.getById(eq(10L),any())).thenReturn(getPositions().get(0));
    }

    @Test
    public void test_should_return_assignee_for_complaint_from_router() {
        when(boundaryRepository.fetchBoundaryById(1L,"tenantId")).thenReturn(getBoundary());
        final Position expectedPosition = Position.builder().id(10L).name("Grievence_Officer_1").build();
        when(positionRepository.getById(10L,new RequestInfo())).thenReturn(expectedPosition);
        final Position actualPosition = complaintRouterService.getAssignee(1L, "C001", null, 1L,"tenantId",new RequestInfo());
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_for_complaint_from_positionhierarcy() {
        final Position expectedPosition = Position.builder().id(3L).name("Grievence_Officer_1").build();
        when(positionRepository.getById(3L, new RequestInfo())).thenReturn(expectedPosition);
        final Position actualPosition = complaintRouterService.getAssignee(1L, "C001", 2L, 1L,"tenantId",new RequestInfo());
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_for_complaint_without_positionhierarchy() {
        when(employeeRepository.getByRoleName("Grievance Routing Officer")).thenReturn(getEmplyees());
        final Position expectedPosition = Position.builder().id(10L).name("Grievence_Officer_1").build();
        final Position actualPosition = complaintRouterService.getAssignee(1L, "C001", 30L, 1L,"tenantId",new RequestInfo());
        assertEquals(expectedPosition, actualPosition);
    }

     @Test
     public void test_should_return_assignee_only_with_complainttype_from_router() {
         when(boundaryRepository.fetchBoundaryById(1L, "tenantId")).thenReturn(new BoundaryResponse());
         final Position expectedPosition = Position.builder().id(10L).name("Grievence_Officer_1").build();
         final Position actualPosition = complaintRouterService.getAssignee(10L, "C001", null, 1L,"tenantId",new RequestInfo());
         assertEquals(expectedPosition, actualPosition);
     }

     @Test
     public void test_should_return_assignee_without_complainttype() {
         final Position expectedPosition = Position.builder().id(10L).name("Grievence_Officer_1").build();
         final Position actualPosition = complaintRouterService.getAssignee(null, "C001", null, 1L,"tenantId",new RequestInfo());
         assertEquals(expectedPosition, actualPosition);
     }

     @Test
     public void test_should_return_assignee_with_city_administrator() {
         when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(null);
         final Position expectedPosition = Position.builder().id(10L).name("Grievence_Officer_1").build();
         final Position actualPosition = complaintRouterService.getAssignee(null, "C001", null, 1L,"tenantId",new RequestInfo());
         assertEquals(expectedPosition, actualPosition);
     }

     @Test
     public void test_should_return_assignee_only_with_boundary() {
         final BoundaryResponse boundary = new BoundaryResponse();
         boundary.setName("Srinivas Nagar");
         boundary.setId(10L);
         when(boundaryRepository.fetchBoundaryById(10L, "tenantId")).thenReturn(boundary);
         final ComplaintRouter complaintRouter = new ComplaintRouter();
         complaintRouter.setBoundary(10L);
         complaintRouter.setPosition(10L);
         complaintRouter.setComplaintType(1L);
         when(complaintRouterRepository.findByOnlyBoundary(10L)).thenReturn(complaintRouter);
         when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(null);
         final Position expectedPosition = Position.builder().id(10L).name("Grievence_Officer_1").build();
         final Position actualPosition = complaintRouterService.getAssignee(10L, "C001", null, 1L, "tenantId", new RequestInfo());
         assertEquals(expectedPosition, actualPosition);

     }

    private ComplaintRouter getComplaintRouter() {
        final ComplaintRouter complaintRouter = new ComplaintRouter();
        complaintRouter.setBoundary(1L);
        complaintRouter.setPosition(10L);
        complaintRouter.setComplaintType(1L);
        return complaintRouter;
    }

    private ComplaintTypeResponse getComplaintType() {
        final ComplaintTypeResponse complaintType = new ComplaintTypeResponse();
        complaintType.setId(1L);
        complaintType.setServiceName("Water Problem");
        return complaintType;
    }

    private BoundaryResponse getBoundary() {
        final BoundaryResponse boundary = new BoundaryResponse();
        boundary.setName("Srinivas Nagar");
        boundary.setId(1L);
        final BoundaryResponse parent = new BoundaryResponse();
        parent.setId(30L);
        parent.setName("Kurnool Municipality");
        boundary.setParent(parent);
        return boundary;
    }

    private List<PositionHierarchyResponse> getPositionHierarchies() {
        final Position fromPosition = Position.builder().id(2L).name("Accounts_Officer_1").build();
        final Position toPosition = Position.builder().id(3L).name("Grievence_Officer_1").build();
        return Collections.singletonList(PositionHierarchyResponse.builder().fromPosition(fromPosition).toPosition(toPosition).build());
    }

    private List<Employee> getEmplyees() {
        final List<Employee> employees = new ArrayList<Employee>();
        final Employee employee = new Employee();
        employee.setCode("101010");
        employee.setId(1L);
        employee.setName("narasappa");
        employee.setUsername("egovernments");
        employees.add(employee);
        return employees;
    }

    private List<Position> getPositions() {
        return Collections.singletonList(Position.builder().id(10L).name("Grievence_Officer_1").build());
    }

}

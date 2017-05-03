package org.egov.workflow.persistence.repository;


import org.egov.workflow.persistence.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;


@Repository 
public interface StateRepository extends JpaRepository<State,java.lang.Long>,JpaSpecificationExecutor<State>  {

    Long countByOwnerPositionAndCreatedDateGreaterThanEqual(Long id, Date givenDate);

    @Query("select distinct s.type from State s where s.ownerPosition.id in (:ownerIds)  and s.status <> 2 and type in (:types) ")
    List<String> findAllTypeByOwnerAndStatus(@Param("ownerIds") List<Long> ownerIds, @Param("types") List<String> enabledTypes);

    public State findByIdAndTenantId(Long id, String tenantId);

}
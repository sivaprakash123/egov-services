package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.ValueDefinition;
import org.egov.pgrrest.common.entity.ValueDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueDefinitionJpaRepository extends JpaRepository<ValueDefinition, ValueDefinitionKey> {

}

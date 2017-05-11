package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.AttributeDefinition;
import org.egov.pgrrest.common.entity.AttributeDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeDefinitionJpaRepository extends JpaRepository<AttributeDefinition, AttributeDefinitionKey> {

}

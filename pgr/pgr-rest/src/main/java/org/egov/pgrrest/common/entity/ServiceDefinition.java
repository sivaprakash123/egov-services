package org.egov.pgrrest.common.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "service_definition")
public class ServiceDefinition extends AbstractPersistable<ServiceDefinitionKey> {
    @EmbeddedId
    private ServiceDefinitionKey id;
}


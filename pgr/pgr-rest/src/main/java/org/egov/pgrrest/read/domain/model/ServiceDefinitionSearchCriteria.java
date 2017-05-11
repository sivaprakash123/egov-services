package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceDefinitionSearchCriteria {
    private String serviceCode;
    private String tenantId;
}

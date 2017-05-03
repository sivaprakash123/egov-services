package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EscalationHoursRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    @JsonProperty("tenantId")
    private String tenantId;
}


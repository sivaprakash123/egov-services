package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ComplaintStatusRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private String tenantId;
}

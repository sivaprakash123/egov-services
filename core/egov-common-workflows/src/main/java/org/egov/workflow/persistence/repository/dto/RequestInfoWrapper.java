package org.egov.workflow.persistence.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.workflow.web.contract.RequestInfo;

@Getter
@Builder
@AllArgsConstructor
public class RequestInfoWrapper {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
}

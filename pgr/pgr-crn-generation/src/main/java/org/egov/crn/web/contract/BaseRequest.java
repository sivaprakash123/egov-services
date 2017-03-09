package org.egov.crn.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BaseRequest {

    @JsonProperty("RequestInfo")
    protected RequestInfo requestInfo;
}
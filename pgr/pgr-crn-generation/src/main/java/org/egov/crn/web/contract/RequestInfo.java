package org.egov.crn.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RequestInfo {

    @JsonProperty("api_id")
    private String apiId;

    @JsonProperty("ver")
    private String ver;

    @JsonProperty("ts")
    private String ts;

    @JsonProperty("action")
    private String action;

    @JsonProperty("did")
    private String did;

    @JsonProperty("key")
    private String key;

    @JsonProperty("msg_id")
    private String msgId;

    @JsonProperty("requester_id")
    private String requesterId;

    @JsonProperty("auth_token")
    private String authToken;
}

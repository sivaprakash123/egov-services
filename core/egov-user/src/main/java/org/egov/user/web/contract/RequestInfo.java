package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RequestInfo {
    @JsonProperty("api_id")
    private String apiId = null;

    @JsonProperty("ver")
    private String ver = null;

    @JsonProperty("ts")
    private String ts = null;

    @JsonProperty("action")
    private String action = null;

    @JsonProperty("did")
    private String did = null;

    @JsonProperty("key")
    private String key = null;

    @JsonProperty("msg_id")
    private String msgId = null;

    @JsonProperty("requester_id")
    private String requesterId = null;

    @JsonProperty("auth_token")
    private String authToken = null;
}

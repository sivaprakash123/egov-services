package org.egov.demand.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserSearchResponse {
    @JsonProperty("responseInfo")
	ResponseInfo responseInfo;

    @JsonProperty("user")
    List<User> users;
}

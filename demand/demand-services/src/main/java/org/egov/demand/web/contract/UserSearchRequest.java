package org.egov.demand.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSearchRequest {

    private RequestInfo requestInfo;
    private String emailId;
    private String mobileNumber;
    private String tenantId;
}

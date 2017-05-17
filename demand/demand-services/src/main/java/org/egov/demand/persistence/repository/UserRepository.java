package org.egov.demand.persistence.repository;

import org.egov.demand.web.contract.RequestInfo;
import org.egov.demand.web.contract.User;
import org.egov.demand.web.contract.UserSearchRequest;
import org.egov.demand.web.contract.UserSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserRepository {

    private RestTemplate restTemplate;
    private String url;

    public UserRepository(final RestTemplate restTemplate,
                          @Value("${egov.services.user.host}") final String eisServiceHostname,
                          @Value("${egov.services.user.usersearchpath}") final String userSearchPath) {
        this.restTemplate = restTemplate;
        this.url = eisServiceHostname + userSearchPath;

    }

    public User searchUserByEmailAndMobileNumber(String emailId,String mobileNumber,String tenantId,RequestInfo requestInfo) {

        UserSearchRequest userSearchRequest = UserSearchRequest.builder().tenantId(tenantId).emailId(emailId).mobileNumber(mobileNumber).requestInfo(requestInfo).build();
        List<User> user = restTemplate.postForObject(url,userSearchRequest, UserSearchResponse.class).getUsers();
        System.out.println(user);
        return !user.isEmpty() ? user.get(0) : new User();

    }


}

package org.egov.pgr.persistence.repository;

import org.egov.pgr.domain.model.ComplaintRegistrationNumber;
import org.egov.pgr.domain.model.RequestContext;
import org.egov.pgr.persistence.queue.contract.BaseRequest;
import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrnRepository {

    private RestTemplate restTemplate;
    private String crnServiceUrl;

    @Autowired
    public CrnRepository(RestTemplate restTemplate,
                         @Value("${crn.service.url}") String crnServiceUrl) {

        this.restTemplate = restTemplate;
        this.crnServiceUrl = crnServiceUrl;
    }

    public ComplaintRegistrationNumber getCrn() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setMsgId(RequestContext.getId());
        BaseRequest baseRequest = new BaseRequest(requestInfo);

        return restTemplate.postForObject(crnServiceUrl, baseRequest, ComplaintRegistrationNumber.class);
    }
}

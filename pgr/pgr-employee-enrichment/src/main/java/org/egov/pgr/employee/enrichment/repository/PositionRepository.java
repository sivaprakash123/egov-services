package org.egov.pgr.employee.enrichment.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.employee.enrichment.model.Position;
import org.egov.pgr.employee.enrichment.repository.contract.PositionResponse;
import org.egov.pgr.employee.enrichment.repository.contract.PositionsResponse;
import org.egov.pgr.employee.enrichment.repository.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PositionRepository {



    private final String url;
    private RestTemplate restTemplate;


    public PositionRepository(RestTemplate restTemplate,
                              @Value("${egov.services.position.host}") String host,
                              @Value("${egov.services.position.designation}") String endpointUrl) {
        this.url = host + endpointUrl;
        this.restTemplate = restTemplate;
    }

    public Position getDesignationIdForAssignee(String tenantId, long assigneeId,long employeeId) {
      /*  PositionsResponse response = restTemplate
            .getForObject(this.url, PositionsResponse.class, tenantId, assigneeId);*/
        PositionsResponse response = null;
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(RequestInfo.builder().apiId("apiId").ver("ver").build()).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
       // String url = this.url + "?id=" + assigneeId + "&tenantId=" +tenantId;

        List<PositionResponse> positions = restTemplate.postForObject(url, request,PositionsResponse.class,employeeId,assigneeId,tenantId).getPositions();
        return response.toDomain();
    }

}



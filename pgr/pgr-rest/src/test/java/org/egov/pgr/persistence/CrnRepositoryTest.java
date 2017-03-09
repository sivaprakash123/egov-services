package org.egov.pgr.persistence;

import org.egov.pgr.domain.model.ComplaintRegistrationNumber;
import org.egov.pgr.domain.model.RequestContext;
import org.egov.pgr.persistence.queue.contract.BaseRequest;
import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.egov.pgr.persistence.repository.CrnRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrnRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldGetCrn() throws Exception {
        final String CRN_SERVICE_URL = "http://localhost:8088/";
        final String CRN = "crn_number";
        RequestContext.setId("message_id");
        final ComplaintRegistrationNumber expected = new ComplaintRegistrationNumber(CRN);
        RequestInfo requestInfo = new RequestInfo("", "", null, "", "", "", "message_id", "", "");
        BaseRequest baseRequest = new BaseRequest(requestInfo);

        BaseRequestMatcher baseRequestMatcher = new BaseRequestMatcher(baseRequest);

        when(restTemplate.postForObject(
                eq(CRN_SERVICE_URL),
                argThat(baseRequestMatcher),
                eq(ComplaintRegistrationNumber.class))).thenReturn(expected);

        CrnRepository crnRepository = new CrnRepository(restTemplate, CRN_SERVICE_URL);

        ComplaintRegistrationNumber actual = crnRepository.getCrn();

        assertEquals(expected, actual);

        verify(restTemplate).postForObject(
                eq(CRN_SERVICE_URL),
                argThat(baseRequestMatcher),
                eq(ComplaintRegistrationNumber.class));
    }

    private class BaseRequestMatcher extends ArgumentMatcher<BaseRequest> {

        private BaseRequest expectedBaseRequest;

        public BaseRequestMatcher(BaseRequest expectedBaseRequest) {

            this.expectedBaseRequest = expectedBaseRequest;
        }

        @Override
        public boolean matches(Object o) {
            BaseRequest actualBaseRequest = (BaseRequest) o;

            return actualBaseRequest.getRequestInfo().getMsgId().equals(
                    expectedBaseRequest.getRequestInfo().getMsgId());
        }
    }
}
package org.egov.crn.web.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.egov.crn.domain.model.RequestContext;
import org.egov.crn.web.contract.BaseRequest;
import org.egov.crn.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Aspect
@Component
public class MessageIdGeneratorApsect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MappingJackson2HttpMessageConverter converter;

    public MessageIdGeneratorApsect(MappingJackson2HttpMessageConverter converter) {
        this.converter = converter;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void beanAnnotatedWithRestController() {
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void beanAnnotatedWithController() {
    }

    @Pointcut("beanAnnotatedWithRestController() || beanAnnotatedWithController()")
    public void controllerClasses() {
    }

    @Pointcut("execution(public * *(org.egov.crn.web.contract.BaseRequest+, ..))")
    public void publicMethodsWithFirstParameterExtendingFromBaseRequest() {
    }

    @Before("publicMethodsWithFirstParameterExtendingFromBaseRequest() && controllerClasses() && args(request, ..)")
    public void populateMessageId(BaseRequest request) throws JsonProcessingException {
        String correlationId = getCorrelationId(request.getRequestInfo());
        MDC.put(RequestContext.MESSAGE_ID, correlationId);
        RequestContext.setId(correlationId);
        logger.info("Incoming Request with request body: " + converter.getObjectMapper().writeValueAsString(request));
    }

    private String getCorrelationId(RequestInfo requestInfo) {
        if (requestInfo != null && isNotBlank(requestInfo.getMsgId())) {
            return requestInfo.getMsgId();
        } else {
            return UUID.randomUUID().toString();
        }
    }
}

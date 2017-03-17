package org.egov.web.notification.sms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class KafkaListenerLoggingAspect {

    @Pointcut(value=" within(org.egov..*) && @annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void anyKafkaConsumer() {
    }

    @Around("anyKafkaConsumer() ")
    public Object logAction(
            ProceedingJoinPoint pjp)
            throws Throwable {

        final Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        KafkaListener myAnnotation = method.getAnnotation(KafkaListener.class);
        System.out.println(new ObjectMapper().writeValueAsString(args[0]));
        System.out.println(String.join(", ", myAnnotation.topics()));
        return pjp.proceed();
    }
}

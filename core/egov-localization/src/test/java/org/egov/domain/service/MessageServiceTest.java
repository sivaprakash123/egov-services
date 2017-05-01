package org.egov.domain.service;


import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {
    private static final String ENGLISH_INDIA = "en_IN";
    private static final String TENANT_ID = "tenant_123";
    private static final String MR_IN = "mr_IN";

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void test_should_augment_messages_for_given_tenant_with_non_overridden_default_messages() {
        String tenantId = "a";

        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        Message defaultMessage1 = Message.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .message("default message1")
            .tenant(defaultTenant)
            .build();
        List<Message> defaultEnglishMessages = Collections.singletonList(defaultMessage1);
        Message tenantMessage1 = Message.builder()
            .code("code2")
            .message("marathi message for tenant a")
            .locale(MR_IN)
            .tenant(new Tenant("a"))
            .build();
        List<Message> marathiMessagesForGivenTenant = Collections.singletonList(tenantMessage1);
        when(messageRepository.findByTenantIdAndLocale("default", ENGLISH_INDIA))
            .thenReturn(defaultEnglishMessages);
        when(messageRepository.findByTenantIdAndLocale("a", MR_IN))
            .thenReturn(marathiMessagesForGivenTenant);

        List<Message> actualMessages = messageService.getMessages(MR_IN, new Tenant(tenantId));

        assertEquals(2, actualMessages.size());
        assertEquals("code1", actualMessages.get(0).getCode());
        assertEquals("code2", actualMessages.get(1).getCode());
    }

    @Test
    public void test_should_get_messages_with_precedence_based_on_tenant_hierarchy() {
        String tenantId = "a.b.c";

        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        Message defaultMessage1 = Message.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .message("default message1")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage2 = Message.builder()
            .code("code2")
            .locale(ENGLISH_INDIA)
            .message("default message2")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage3 = Message.builder()
            .code("code3")
            .locale(ENGLISH_INDIA)
            .message("default message3")
            .tenant(defaultTenant)
            .build();
        List<Message> defaultEnglishMessages = Arrays.asList(defaultMessage1, defaultMessage2, defaultMessage3);
        Message tenantMessage1 = Message.builder()
            .code("code1")
            .message("marathi message for tenant a.b.c")
            .locale(MR_IN)
            .tenant(new Tenant("a.b.c"))
            .build();
        Message tenantMessage2 = Message.builder()
            .code("code5")
            .message("marathi message for tenant a.b.c")
            .locale(MR_IN)
            .tenant(new Tenant("a.b.c"))
            .build();
        List<Message> marathiMessagesForGivenTenant = Arrays.asList(tenantMessage1, tenantMessage2);
        Message tenantParentMessage1 = Message.builder()
            .code("code2")
            .message("marathi message for tenant a.b")
            .locale(MR_IN)
            .tenant(new Tenant("a.b"))
            .build();
        Message tenantParentMessage2 = Message.builder()
            .code("code4")
            .message("marathi message for tenant a.b")
            .locale(MR_IN)
            .tenant(new Tenant("a.b"))
            .build();
        List<Message> marathiMessagesForTenantParent = Arrays.asList(tenantParentMessage1, tenantParentMessage2);

        when(messageRepository.findByTenantIdAndLocale("default", ENGLISH_INDIA))
            .thenReturn(defaultEnglishMessages);
        when(messageRepository.findByTenantIdAndLocale("a.b.c", MR_IN))
            .thenReturn(marathiMessagesForGivenTenant);
        when(messageRepository.findByTenantIdAndLocale("a.b", MR_IN))
            .thenReturn(marathiMessagesForTenantParent);
        when(messageRepository.findByTenantIdAndLocale("a", MR_IN))
            .thenReturn(Collections.emptyList());

        List<Message> actualMessages = messageService.getMessages(MR_IN, new Tenant(tenantId));

        assertEquals(5, actualMessages.size());
        assertEquals("code1", actualMessages.get(0).getCode());
        assertEquals("marathi message for tenant a.b.c", actualMessages.get(0).getMessage());
        assertEquals("code2", actualMessages.get(1).getCode());
        assertEquals("marathi message for tenant a.b", actualMessages.get(1).getMessage());
        assertEquals("code3", actualMessages.get(2).getCode());
        assertEquals("default message3", actualMessages.get(2).getMessage());
        assertEquals("code4", actualMessages.get(3).getCode());
        assertEquals("marathi message for tenant a.b", actualMessages.get(3).getMessage());
        assertEquals("code5", actualMessages.get(4).getCode());
        assertEquals("marathi message for tenant a.b.c", actualMessages.get(4).getMessage());
    }

    @Test
    public void test_should_save_messages() {
        List<Message> modelMessages = getMessages();
        when(messageRepository.saveAllEntities(getEntityMessages())).thenReturn(modelMessages);

        List<Message> messages = messageService.saveAllEntityMessages(getEntityMessages());

        assertThat(messages.size()).isEqualTo(4);
        List<Message> modelMessagesWhereLocalIsEnglish = messages.stream().filter(message -> message.getLocale()
            .equals(ENGLISH_INDIA)).collect(Collectors.toList());
        List<Message> modelMessagesWhereLocalIsElseThanEnglish = messages.stream().filter(message -> message
            .getLocale().equals(MR_IN)).collect(Collectors.toList());

        assertThat(modelMessagesWhereLocalIsEnglish.size()).isEqualTo(2);
        assertThat(modelMessagesWhereLocalIsElseThanEnglish.size()).isEqualTo(2);
    }

    private List<org.egov.persistence.entity.Message> getEntityMessages() {

        org.egov.persistence.entity.Message message1 = org.egov.persistence.entity.Message.builder().message("OTP " +
            "यशस्वीपणे प्रमाणित").code("core.msg.OTPvalidated").locale(MR_IN).tenantId(TENANT_ID).build();
        org.egov.persistence.entity.Message message2 = org.egov.persistence.entity.Message.builder().message("प्रतिमा" +
            " यशस्वीरित्या अपलोड").code("core.lbl.imageupload").locale(MR_IN).tenantId(TENANT_ID).build();
        org.egov.persistence.entity.Message message3 = org.egov.persistence.entity.Message.builder().message
            ("EnterMobileNumber").code("core.msg.entermobileno").locale(ENGLISH_INDIA).tenantId(TENANT_ID).build();
        org.egov.persistence.entity.Message message4 = org.egov.persistence.entity.Message.builder().message("Enter " +
            "fullname").code("core.msg.enterfullname").locale(ENGLISH_INDIA).tenantId(TENANT_ID).build();
        return (Arrays.asList(message1, message2, message3, message4));
    }

    private List<Message> getMessages() {
        Message message1 = Message.builder()
            .code("core.msg.OTPvalidated")
            .message("OTP यशस्वीपणे प्रमाणित")
            .locale(MR_IN)
            .tenant(new Tenant(TENANT_ID))
            .build();
        Message message2 = Message.builder()
            .code("core.lbl.imageupload")
            .message("प्रतिमा यशस्वीरित्या अपलोड")
            .locale(MR_IN)
            .tenant(new Tenant(TENANT_ID))
            .build();
        Message message3 = Message.builder()
            .code("core.msg.entermobileno")
            .locale(ENGLISH_INDIA)
            .message("EnterMobileNumber")
            .tenant(new Tenant(TENANT_ID))
            .build();
        Message message4 = Message.builder()
            .code("core.msg.enterfullname")
            .locale(ENGLISH_INDIA)
            .message("Enter fullname")
            .tenant(new Tenant(TENANT_ID))
            .build();

        return (Arrays.asList(message1, message2, message3, message4));
    }


}
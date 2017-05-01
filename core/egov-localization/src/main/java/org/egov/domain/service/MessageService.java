package org.egov.domain.service;

import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {
    private static final String ENGLISH_INDIA = "en_IN";
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> saveAllEntityMessages(List<org.egov.persistence.entity.Message> entityMessages) {
        return messageRepository.saveAllEntities(entityMessages);
    }

    public List<Message> getMessages(String locale, Tenant tenant) {
        final Collection<Message> messagesForGivenLocale = getMessagesForGivenLocale(locale, tenant);
        List<Message> missingMessages = getDefaultMessagesForMissingCodes(messagesForGivenLocale);
        return Stream.concat(messagesForGivenLocale.stream(), missingMessages.stream())
            .sorted(Comparator.comparing(Message::getCode))
            .collect(Collectors.toList());
    }

    private List<Message> getDefaultMessagesForMissingCodes(Collection<Message> messagesForGivenLocale) {
        final List<Message> messagesInEnglishForDefaultTenant = getMessages(ENGLISH_INDIA, Tenant.DEFAULT_TENANT);

        Set<String> messageCodesInGivenLanguage = messagesForGivenLocale.stream()
            .map(Message::getCode)
            .collect(Collectors.toSet());

        return getEnglishMessagesForCodesNotPresentInLocalLanguage(messageCodesInGivenLanguage,
			messagesInEnglishForDefaultTenant);
    }

    private Collection<Message> getMessagesForGivenLocale(String locale, Tenant tenant) {
        final Map<String, Message> codeToMessageMap = new HashMap<>();
        final List<Message> messages = tenant.getTenantHierarchy().stream()
            .map(tenantItem -> getMessages(locale, tenantItem))
            .flatMap(List::stream)
            .collect(Collectors.toList());

        messages.forEach(message -> {
            final Message matchingMessage = codeToMessageMap.get(message.getCode());
            if (matchingMessage == null) {
                codeToMessageMap.put(message.getCode(), message);
            } else {
                if (message.isMoreSpecificComparedTo(matchingMessage)) {
                    codeToMessageMap.put(message.getCode(), message);
                }
            }
        });

        return codeToMessageMap.values();
    }

    private List<Message> getEnglishMessagesForCodesNotPresentInLocalLanguage(Set<String> messageCodesForGivenLocale,
                                                                              List<Message> messagesInEnglish) {
        return messagesInEnglish.stream()
            .filter(message -> !messageCodesForGivenLocale.contains(message.getCode()))
            .collect(Collectors.toList());
    }

    private List<Message> getMessages(String locale, String tenantId) {
        return messageRepository.findByTenantIdAndLocale(tenantId, locale);
    }
}

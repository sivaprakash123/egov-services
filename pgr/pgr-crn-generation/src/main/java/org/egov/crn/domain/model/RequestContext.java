package org.egov.crn.domain.model;

public class RequestContext {

    public static String MESSAGE_ID = "MESSAGE-ID";

    private static final ThreadLocal<String> id = new ThreadLocal<>();

    public static String getId() { return id.get(); }

    public static void setId(String correlationId) { id.set(correlationId); }
}

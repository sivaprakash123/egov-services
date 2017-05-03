package org.egov.workflow.web.error;


import org.egov.workflow.web.contract.ErrorResponse;

/**
 * This is to transform the model errors to web channel specific errors
 *
 * @param <T>
 */
public interface ErrorAdapter<T> {
    org.egov.common.contract.response.ErrorResponse adapt(T model);
}

package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@JsonPropertyOrder({"code","message","description","filelds"})
@Builder
public @Data class Error {

	private String code = null;

	private String message = null;

	private String description = null;

	private List<FieldError> fields = new ArrayList<FieldError>();  

	

}

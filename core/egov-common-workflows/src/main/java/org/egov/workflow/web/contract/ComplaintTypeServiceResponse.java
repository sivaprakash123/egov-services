package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import java.util.List;

@Getter
public class ComplaintTypeServiceResponse {

	@JsonProperty("ComplaintType")
	private List<ComplaintTypeResponse> complaintType;



}

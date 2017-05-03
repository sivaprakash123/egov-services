package org.egov.workflow.web.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	 
	private ResponseInfo responseInfo;

	 
	private Error error;

	
}

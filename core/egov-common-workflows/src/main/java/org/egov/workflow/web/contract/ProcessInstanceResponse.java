package org.egov.workflow.web.contract;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(value = Include.NON_NULL)
@Getter
@Builder
@Setter
public class ProcessInstanceResponse {
	private ResponseInfo responseInfo;
	private List<ProcessInstance> processInstances;
	private ProcessInstance processInstance;
	private Pagination page;
}
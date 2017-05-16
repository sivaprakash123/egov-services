package org.egov.demand.domain.service.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DemandSearchCriteria {
	private Long demandId;
    private String consumerCode;
    private String businessDetails;
    private String mobileNumber;
    private String emailId;
    private String tenantId;

}

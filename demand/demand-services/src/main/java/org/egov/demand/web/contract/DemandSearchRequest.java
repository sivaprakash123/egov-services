package org.egov.demand.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.demand.domain.service.model.DemandSearchCriteria;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DemandSearchRequest {
	private Long demandId;
    private String consumerCode;
    private String businessDetails;
    private String mobileNumber;
    private String emailId;
    private String tenantId;

    public DemandSearchCriteria toDomain() {
        return DemandSearchCriteria.builder().demandId(demandId).consumerCode(consumerCode)
                .businessDetails(businessDetails).mobileNumber(mobileNumber).emailId(emailId).tenantId(tenantId).build();
    }

}

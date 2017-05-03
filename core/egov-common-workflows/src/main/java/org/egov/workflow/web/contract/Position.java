package org.egov.workflow.web.contract;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Position {

	private Long id;

	private String name;

	private DepartmentDesignation deptdesig;

	private Boolean isPostOutsourced;

	private Boolean active;

}

package org.egov.workflow.web.contract;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionHierarchyResponse {

    private Long id;
    private Position  fromPosition;
    private Position  toPosition;

}

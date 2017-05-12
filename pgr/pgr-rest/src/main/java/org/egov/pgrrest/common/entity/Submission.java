package org.egov.pgrrest.common.entity;

import lombok.*;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.read.domain.model.ComplaintLocation;
import org.egov.pgrrest.read.domain.model.Coordinates;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submission")
public class Submission extends AbstractAuditable<String> {

    @Id
    @Column(name = "crn", unique = true)
    private String id;

    @JoinColumn(name = "servicecode")
    private String serviceCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requester", nullable = false)
    private Complainant requester = new Complainant();

    private Long assignee;

    @Column(name = "location")
    private Long location;

//    @Column(name = "childlocation")
//    private Long childLocation;

    @Column(name = "status")
    private String status;

    private String details;

    @Column(name = "landmarkdetails")
    private String landmarkDetails;

//	@ManyToOne
//	@JoinColumn(name = "receivingmode")
//	private ReceivingMode receivingMode;

//    @ManyToOne
//    @JoinColumn(name = "receivingcenter")
//    private ReceivingCenter receivingCenter;

    @Column(name = "latitude")
    private double longitude;

    @Column(name = "longitude")
    private double latitude;

    @Column(name = "escalationdate", nullable = false)
    private Date escalationDate;

    private Long department;

//    @Column(name = "citizenfeedback")
//    @Enumerated(EnumType.ORDINAL)
//    private org.egov.pgrrest.common.entity.enums.CitizenFeedback citizenFeedback;

    @Column(name = "address")
    private String address;

//    @Transient
//    private Long crossHierarchyId;

//    @Column(name = "state_id")
//    private Long stateId;
//

	@Column(name = "tenantid")
	private String tenantId;

    public boolean isCompleted() {
        return org.egov.pgrrest.common.entity.enums.ComplaintStatus
                .valueOf(getStatus()) == org.egov.pgrrest.common.entity.enums.ComplaintStatus.COMPLETED;
    }

    public org.egov.pgrrest.read.domain.model.Complaint toDomain() {
        final Coordinates coordinates = new Coordinates(latitude, longitude, tenantId);
        return org.egov.pgrrest.read.domain.model.Complaint.builder()
                .complaintLocation(new ComplaintLocation(coordinates, null, null, tenantId))
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .address(landmarkDetails)
                .description(details)
                .crn(id)
                .createdDate(getCreatedDate())
                .lastModifiedDate(getLastModifiedDate())
                .mediaUrls(Collections.emptyList())
                .escalationDate(getEscalationDate())
                .closed(isCompleted())
                .department(getDepartment())
                .lastAccessedTime(getLastModifiedDate())
                .assignee(getAssignee())
                .tenantId(tenantId)
                .complaintStatus(getComplaintStatus())
                .build();
    }

    private String getComplaintStatus(){
    	return status;
    }

}
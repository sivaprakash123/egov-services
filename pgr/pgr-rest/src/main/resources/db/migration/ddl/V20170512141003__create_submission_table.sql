CREATE TABLE submission
(
  crn VARCHAR(32) NOT NULL,
  tenantid VARCHAR(256) NOT NULL,
  escalation_date timestamp,
  landmarkdetails VARCHAR(200),
  department bigint,
  requester bigint NOT NULL,
  assignee bigint,
  lat double precision,
  lng double precision,
  latlngAddress VARCHAR(70),
  location bigint,
  status VARCHAR(25),
  details VARCHAR(500) NOT NULL,
  servicecode VARCHAR(20) NOT NULL,
  createddate timestamp NOT NULL ,
  lastmodifieddate timestamp ,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version bigint,
  CONSTRAINT pk_submission PRIMARY KEY (crn, tenantid)
);

CREATE TABLE submission_attribute
(
  crn VARCHAR(32) NOT NULL,
  code VARCHAR(50) NOT NULL,
  key VARCHAR(50) NOT NULL
);


-- Move this to attribute table
--   receivingmode smallint,
--   receivingcenter bigint,
--   state_id bigint,
--   citizenFeedback bigint,
--   crossHierarchyId bigint,
--   childlocation bigint,
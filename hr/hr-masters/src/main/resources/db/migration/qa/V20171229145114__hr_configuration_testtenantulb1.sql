insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Weekly_holidays','Define the weekly off for the organization',1,now(),1,now(),'testtenant.ulb1');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Autogenerate_employeecode','This will define if employee code needs to be system generated or manually captured',1,now(),1,now(),'testtenant.ulb1');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Include_enclosed_holidays','This will define if system needs to consider the holidays coming between as leave or not',1,now(),1,now(),'testtenant.ulb1');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Compensatory leave validity','This will define the number of days till which an employee can apply for compensatory off',1,now(),1,now(),'testtenant.ulb1');

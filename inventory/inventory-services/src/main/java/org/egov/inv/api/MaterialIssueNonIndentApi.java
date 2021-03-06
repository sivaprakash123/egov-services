/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.egov.inv.api;

import java.math.BigDecimal;


import io.swagger.annotations.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-06T06:54:07.407Z")

@Api(value = "materialissues-ni", description = "the materialissues-ni API")
public interface MaterialIssueNonIndentApi {

    @ApiOperation(value = "Create  new  materialissues for non indent case", notes = "This API holds the common information of Non Indent Issue. Whenver Non Indent Issue is created in the system, this API will be invoked internally and the common information will be hold by this API.", response = MaterialIssueResponse.class, tags={ "Material Issue", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "MaterialIssue created Successfully", response = MaterialIssueResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class) })
    
    @RequestMapping(value = "/materialissues-ni/_create",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<MaterialIssueResponse> materialissuesNonIndentCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "Create  new"  )  @Valid @RequestBody MaterialIssueRequest materialIssueRequest);


    @ApiOperation(value = "Get the list of material issues without", notes = "This API holds the common information required by Non Indent Issue. This API will be invoked internally during search of  Non Indent Issue.", response = MaterialIssueResponse.class, tags={ "Material Issue", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "MaterialIssue retrieved Successfully", response = MaterialIssueResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class) })
    
    @RequestMapping(value = "/materialissues-ni/_search",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<MaterialIssueResponse> materialissuesNonIndentSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo, @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,@ApiParam(value = "issuing store of the MaterialIssue ") @RequestParam(value = "fromStore", required = false) String fromStore,@ApiParam(value = "receiving store of the MaterialIssue ") @RequestParam(value = "toStore", required = false) String toStore,@ApiParam(value = "issueNoteNumber  Auto generated number, read only ") @RequestParam(value = "issueNoteNumber", required = false) String issueNoteNumber,@ApiParam(value = "issue date of the MaterialIssue ") @RequestParam(value = "issueDate", required = false) Long issueDate,@ApiParam(value = "material issue status of the MaterialIssue ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "materialIssueStatus", required = false) String materialIssueStatus,@ApiParam(value = "description of the MaterialIssue ") @RequestParam(value = "description", required = false) String description,@ApiParam(value = "issue purpose status of the materialissue ") @RequestParam(value = "issuePurpose", required = false) String issuePurpose,
@ApiParam(value = "total issue value of the MaterialIssue ") @RequestParam(value = "totalIssueValue", required = false) BigDecimal totalIssueValue, @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,@ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy, @ApiParam(value = "This takes purpose of issuesearch") @RequestParam(value = "purpose", required = false) String purpose);


    @ApiOperation(value = "Update any of the material issues for non indent case.", notes = "This API is inoked to update the material issue information for non indent case.", response = MaterialIssueResponse.class, tags={ "Material Issue", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "MaterialIssue updated Successfully", response = MaterialIssueResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class) })
    
    @RequestMapping(value = "/materialissues-ni/_update",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<MaterialIssueResponse> materialissuesNonIndentUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "common Request info"  )  @Valid @RequestBody MaterialIssueRequest materialIssueRequest);

}

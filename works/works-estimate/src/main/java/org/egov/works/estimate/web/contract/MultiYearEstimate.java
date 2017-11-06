package org.egov.works.estimate.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An Object that holds the basic data of Multi Year Estimate
 */
@ApiModel(description = "An Object that holds the basic data of Multi Year Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-03T07:36:47.547Z")

public class MultiYearEstimate   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("financialYear")
  private FinancialYear financialYear = null;

  @JsonProperty("detailedEstimate")
  private DetailedEstimate detailedEstimate = null;

  @JsonProperty("percentage")
  private Double percentage = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public MultiYearEstimate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Multi Year Estimate
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Multi Year Estimate")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MultiYearEstimate tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Multi Year Estimate
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Multi Year Estimate")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MultiYearEstimate financialYear(FinancialYear financialYear) {
    this.financialYear = financialYear;
    return this;
  }

   /**
   * Financial Year of the Multi Year Estimate
   * @return financialYear
  **/
  @ApiModelProperty(required = true, value = "Financial Year of the Multi Year Estimate")
  @NotNull

  @Valid

  public FinancialYear getFinancialYear() {
    return financialYear;
  }

  public void setFinancialYear(FinancialYear financialYear) {
    this.financialYear = financialYear;
  }

  public MultiYearEstimate detailedEstimate(DetailedEstimate detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
    return this;
  }

   /**
   * Reference of the Detailed Estimate for Multi Year Estimate
   * @return detailedEstimate
  **/
  @ApiModelProperty(required = true, value = "Reference of the Detailed Estimate for Multi Year Estimate")
  //@NotNull

  @Valid

  public DetailedEstimate getDetailedEstimate() {
    return detailedEstimate;
  }

  public void setDetailedEstimate(DetailedEstimate detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
  }

  public MultiYearEstimate percentage(Double percentage) {
    this.percentage = percentage;
    return this;
  }

   /**
   * Percentage for the Multi Year Estimate
   * @return percentage
  **/
  @ApiModelProperty(required = true, value = "Percentage for the Multi Year Estimate")
  @NotNull


  public Double getPercentage() {
    return percentage;
  }

  public void setPercentage(Double percentage) {
    this.percentage = percentage;
  }

  public MultiYearEstimate auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultiYearEstimate multiYearEstimate = (MultiYearEstimate) o;
    return Objects.equals(this.id, multiYearEstimate.id) &&
        Objects.equals(this.tenantId, multiYearEstimate.tenantId) &&
        Objects.equals(this.financialYear, multiYearEstimate.financialYear) &&
        Objects.equals(this.detailedEstimate, multiYearEstimate.detailedEstimate) &&
        Objects.equals(this.percentage, multiYearEstimate.percentage) &&
        Objects.equals(this.auditDetails, multiYearEstimate.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, financialYear, detailedEstimate, percentage, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultiYearEstimate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    financialYear: ").append(toIndentedString(financialYear)).append("\n");
    sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
    sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


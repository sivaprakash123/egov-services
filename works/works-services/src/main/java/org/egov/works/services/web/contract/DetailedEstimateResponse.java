package org.egov.works.services.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class to send response. Array of Detailed Estimate items are used in
 * case of search results, also multiple Detailed Estimate item is used for
 * create and update
 */
@ApiModel(description = "Contract class to send response. Array of Detailed Estimate items are used in case of search results, also multiple  Detailed Estimate item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T07:43:05.630Z")

public class DetailedEstimateResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("detailedEstimates")
	private List<DetailedEstimate> detailedEstimates = null;

	public DetailedEstimateResponse responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	/**
	 * Get responseInfo
	 * 
	 * @return responseInfo
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public DetailedEstimateResponse detailedEstimates(List<DetailedEstimate> detailedEstimates) {
		this.detailedEstimates = detailedEstimates;
		return this;
	}

	public DetailedEstimateResponse addDetailedEstimatesItem(DetailedEstimate detailedEstimatesItem) {
		if (this.detailedEstimates == null) {
			this.detailedEstimates = new ArrayList<DetailedEstimate>();
		}
		this.detailedEstimates.add(detailedEstimatesItem);
		return this;
	}

	/**
	 * Used for search result and create only
	 * 
	 * @return detailedEstimates
	 **/
	@ApiModelProperty(value = "Used for search result and create only")

	@Valid

	public List<DetailedEstimate> getDetailedEstimates() {
		return detailedEstimates;
	}

	public void setDetailedEstimates(List<DetailedEstimate> detailedEstimates) {
		this.detailedEstimates = detailedEstimates;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DetailedEstimateResponse detailedEstimateResponse = (DetailedEstimateResponse) o;
		return Objects.equals(this.responseInfo, detailedEstimateResponse.responseInfo)
				&& Objects.equals(this.detailedEstimates, detailedEstimateResponse.detailedEstimates);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, detailedEstimates);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DetailedEstimateResponse {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    detailedEstimates: ").append(toIndentedString(detailedEstimates)).append("\n");
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

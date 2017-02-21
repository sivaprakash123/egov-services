package org.egov.workflow.web.contract;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@AllArgsConstructor
@Builder
public class ProcessInstance {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("objectId")
    @Setter
    private String id = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("description")
    private String description = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
    @JsonProperty("createdDate")
    private Date createdDate = null;

    @JsonProperty("lastupdated")
    private Date lastupdatedSince = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("action")
    private String action = null;

    @JsonProperty("businesskey")
    private String businessKey = null;

    @Setter
    @JsonProperty("assignee")
    private Long assignee = null;

    @JsonProperty("group")
    private String group = null;

    @JsonProperty("senderName")
    private String senderName = null;

    @JsonProperty("values")
    private Map<String, Attribute> values;
    
    //To be used to fetch single value attributes
    public String getValueForKey(String key){
    	if(Objects.nonNull(values.get(key)))
    		return values.get(key).getValues().get(0).getName();
    	
    	return "";
    	
    }

}

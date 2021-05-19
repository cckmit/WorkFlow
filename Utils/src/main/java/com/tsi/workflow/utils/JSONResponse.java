package com.tsi.workflow.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 *
 * @author Prabhu
 */
public class JSONResponse implements Serializable {

    private static final long serialVersionUID = -7571113449729273240L;

    private static final Logger LOG = Logger.getLogger(JSONResponse.class.getName());

    private Boolean status = false;
    private StringBuilder errorMessage;
    private Object metaData;
    private Object data;
    private Long count;
    private String userId;

    public JSONResponse() {
	errorMessage = new StringBuilder();
    }

    public Object getMetaData() {
	return metaData;
    }

    public void setMetaData(Object pMetaData) {
	this.metaData = pMetaData;
    }

    public Long getCount() {
	return count;
    }

    public String getErrorMessage() {
	return errorMessage.toString();
    }

    public void setErrorMessage(String pErrorMessage) {
	this.errorMessage.append(pErrorMessage);
    }

    public boolean havingErrorMessage() {
	return errorMessage.length() > 0;
    }

    public void clearErrorMessage() {
	this.errorMessage.setLength(0);
    }

    public void setCount(Long pCount) {
	this.count = pCount;
    }

    public void setCount(Integer pCount) {
	this.count = pCount.longValue();
    }

    public Boolean getStatus() {
	return status;
    }

    public void setStatus(Boolean status) {
	this.status = status;
    }

    public Object getData() {
	return data;
    }

    public void setData(Object data) {
	this.data = data;
    }

    @JsonIgnore
    public String getDisplayErrorMessage() {
	int lErrorIndex = errorMessage.indexOf("ERROR:");
	if (lErrorIndex >= 0) {
	    String lErrorMessage = errorMessage.substring(lErrorIndex);
	    int lNewLineIndex = errorMessage.indexOf(System.lineSeparator(), lErrorIndex);
	    if (lNewLineIndex > lErrorIndex) {
		return errorMessage.substring(lErrorIndex, lNewLineIndex);
	    }
	    return lErrorMessage;
	}
	return "No Error Message to Display";
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.models;

import com.tsi.workflow.User;
import java.util.Objects;

/**
 *
 * @author Radha.Adhimoolam
 */
public class RequestQueueModel {

    private String requestURL;
    private String authorizationCode;
    private String pathinfo;
    private String queryString;
    private String method;
    private Long startDateTime;
    private Long endDateTime;
    private String impId;
    private String planId;
    private User user;
    private String asyncParentUrl;
    private Boolean asyncActionEnd;

    public RequestQueueModel() {
    }

    public String getAuthorizationCode() {
	return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
	this.authorizationCode = authorizationCode;
    }

    public String getPathinfo() {
	return pathinfo;
    }

    public void setPathinfo(String pathinfo) {
	this.pathinfo = pathinfo;
    }

    public String getQueryString() {
	return queryString;
    }

    public void setQueryString(String queryString) {
	this.queryString = queryString;
    }

    public Long getStartDateTime() {
	return startDateTime;
    }

    public void setStartDateTime(Long startDateTime) {
	this.startDateTime = startDateTime;
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public Long getEndDateTime() {
	return endDateTime;
    }

    public void setEndDateTime(Long endDateTime) {
	this.endDateTime = endDateTime;
    }

    public String getRequestURL() {
	return requestURL;
    }

    public void setRequestURL(String requestURL) {
	this.requestURL = requestURL;
    }

    public String getImpId() {
	return impId;
    }

    public void setImpId(String impId) {
	this.impId = impId;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getAsyncParentUrl() {
	return asyncParentUrl;
    }

    public void setAsyncParentUrl(String asyncParentUrl) {
	this.asyncParentUrl = asyncParentUrl;
    }

    public Boolean getAsyncActionEnd() {
	return asyncActionEnd;
    }

    public void setAsyncActionEnd(Boolean asyncActionEnd) {
	this.asyncActionEnd = asyncActionEnd;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 29 * hash + Objects.hashCode(this.method);
	hash = 29 * hash + Objects.hashCode(this.startDateTime);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final RequestQueueModel other = (RequestQueueModel) obj;
	if (!Objects.equals(this.pathinfo, other.pathinfo)) {
	    return false;
	}
	if (!Objects.equals(this.method, other.method)) {
	    return false;
	}
	if (!Objects.equals(this.startDateTime, other.startDateTime)) {
	    return false;
	}
	return true;
    }

}

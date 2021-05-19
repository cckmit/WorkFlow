/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author prabhu.prabhakaran
 */
public class UserModel implements Serializable {

    private static final long serialVersionUID = 4916549457539660906L;

    private String username;
    private String displayName;
    private String emailAddress;
    private boolean canAdmin;
    private Set<String> repositories = new HashSet<String>();
    private Map<String, AccessPermission> permissions = new LinkedHashMap<String, AccessPermission>();

    public Set<String> getRepositories() {
	return repositories;
    }

    public void setRepositories(Set<String> repositories) {
	this.repositories = repositories;
    }

    public Map<String, AccessPermission> getPermissions() {
	return permissions;
    }

    public void setPermissions(Map<String, AccessPermission> permissions) {
	this.permissions = permissions;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getEmailAddress() {
	return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
    }

    public boolean isCanAdmin() {
	return canAdmin;
    }

    public void setCanAdmin(boolean canAdmin) {
	this.canAdmin = canAdmin;
    }

}

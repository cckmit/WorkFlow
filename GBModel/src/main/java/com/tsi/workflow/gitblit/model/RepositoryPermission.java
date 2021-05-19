/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

/**
 *
 * @author User
 */
public class RepositoryPermission {

    private String registrant;
    private String permission;
    private String registrantType;
    private String permissionType;
    private boolean mutable;

    public RepositoryPermission(String userId, String permission) {
	this.mutable = true;
	this.permission = permission;
	this.permissionType = "EXPLICIT";
	this.registrantType = "USER";
	this.registrant = userId;
    }

    public String getRegistrant() {
	return registrant;
    }

    public void setRegistrant(String registrant) {
	this.registrant = registrant;
    }

    public String getPermission() {
	return permission;
    }

    public void setPermission(String permission) {
	this.permission = permission;
    }

    public String getRegistrantType() {
	return registrantType;
    }

    public void setRegistrantType(String registrantType) {
	this.registrantType = registrantType;
    }

    public String getPermissionType() {
	return permissionType;
    }

    public void setPermissionType(String permissionType) {
	this.permissionType = permissionType;
    }

    public boolean isMutable() {
	return mutable;
    }

    public void setMutable(boolean mutable) {
	this.mutable = mutable;
    }

}

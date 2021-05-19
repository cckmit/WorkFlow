package com.tsi.workflow.beans.ui;

public class UserDetails {

    private Integer id;
    private String username;
    private String permission;
    private Integer repoid;
    private String repoName;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPermission() {
	return permission;
    }

    public void setPermission(String permission) {
	this.permission = permission;
    }

    public Integer getRepoid() {
	return repoid;
    }

    public void setRepoid(Integer repoid) {
	this.repoid = repoid;
    }

    public String getRepoName() {
	return repoName;
    }

    public void setRepoName(String repoName) {
	this.repoName = repoName;
    }
}

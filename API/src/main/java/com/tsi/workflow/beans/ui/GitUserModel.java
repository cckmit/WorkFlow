package com.tsi.workflow.beans.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GitUserModel {

    private String subsourcerepo;
    private String subderivedrepo;
    private String username;
    private String displayname;
    private Integer repoid;
    private String permission;
    private String sourcerepo;
    private String derivedrepo;
    private String defultpermission;
    private String description;
    private String targetsystems;

    public String getSubsourcerepo() {
	return subsourcerepo;
    }

    public void setSubsourcerepo(String subsourcerepo) {
	this.subsourcerepo = subsourcerepo;
    }

    public String getSubderivedrepo() {
	return subderivedrepo;
    }

    public void setSubderivedrepo(String subderivedrepo) {
	this.subderivedrepo = subderivedrepo;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getDisplayname() {
	return displayname;
    }

    public void setDisplayname(String displayname) {
	this.displayname = displayname;
    }

    public Integer getRepoid() {
	return repoid;
    }

    public void setRepoid(Integer repoid) {
	this.repoid = repoid;
    }

    public String getPermission() {
	return permission;
    }

    public void setPermission(String permission) {
	this.permission = permission;
    }

    public String getSourcerepo() {
	return sourcerepo;
    }

    public void setSourcerepo(String sourcerepo) {
	this.sourcerepo = sourcerepo;
    }

    public String getDerivedrepo() {
	return derivedrepo;
    }

    public void setDerivedrepo(String derivedrepo) {
	this.derivedrepo = derivedrepo;
    }

    public String getDefultpermission() {
	return defultpermission;
    }

    public void setDefultpermission(String defultpermission) {
	this.defultpermission = defultpermission;
    }

    public boolean havingSplPermission() {
	return (permission.equalsIgnoreCase(defultpermission)) ? false : true;

    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public List<String> getTargetsystems() {
	return (targetsystems != null && !targetsystems.trim().isEmpty()) ? Arrays.asList(targetsystems.split(",")).stream().collect(Collectors.toList()) : new ArrayList<String>();
    }

    public void setTargetsystems(String targetsystems) {
	this.targetsystems = targetsystems;
    }
}

package com.tsi.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class LdapConfig {

    @Value("${ldap.url}")
    private String url = "";

    @Value("${ldap.user.class}")
    private String userClass = "";

    @Value("${ldap.user.param}")
    private String userParam = "";

    @Value("${ldap.user.searchBase}")
    private String userSearchBase = "";

    @Value("${ldap.group.searchBase}")
    private String groupSearchBase = "";

    @Value("${ldap.group.class}")
    private String groupClass = "";

    @Value("${ldap.group.param}")
    private String groupParam = "";

    @Value("${ldap.group.attr}")
    private String groupAttribute = "";

    private List<String> lUserAttributes = new ArrayList<>();

    @Value("${ldap.user.attr}")
    public void setUserAttributes(String pAttributes) {
	lUserAttributes.clear();
	lUserAttributes.addAll(Arrays.asList(pAttributes.split(",")));
    }

    public List<String> getUserAttributes() {
	return lUserAttributes;
    }

    public void setGroupClass(String groupClass) {
	this.groupClass = groupClass;
    }

    public void setGroupParam(String groupParam) {
	this.groupParam = groupParam;
    }

    public void setGroupAttribute(String groupAttribute) {
	this.groupAttribute = groupAttribute;
    }

    public void setGroupSearchBase(String groupSearchBase) {
	this.groupSearchBase = groupSearchBase;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public void setUserClass(String userClass) {
	this.userClass = userClass;
    }

    public void setUserSearchBase(String userSearchBase) {
	this.userSearchBase = userSearchBase;
    }

    public void setUserParam(String userParam) {
	this.userParam = userParam;
    }

    public String getUrl() {
	return url;
    }

    public String getUserSearchBase() {
	return userSearchBase;
    }

    public String getGroupSearchBase() {
	return groupSearchBase;
    }

    public String getUserClass() {
	return userClass;
    }

    public String getGroupClass() {
	return groupClass;
    }

    public String getUserParam() {
	return userParam;
    }

    public String getGroupParam() {
	return groupParam;
    }

    public String getGroupAttribute() {
	return groupAttribute;
    }

}

/*
 *  LDAPGroup.java
 *  This file is part of XrefRA project.
 *  Copyright (c) 2017 TPF Software Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining 
 *  a copy of this software and associated documentation files (the "Software"), 
 *  to deal in the Software without restriction, including without limitation 
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 *  and/or sell copies of the Software, and to permit persons to whom the 
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be 
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 *  DEALINGS IN THE SOFTWARE.
 *
 */
package com.tsi.workflow.ldap.config;

import com.tsi.workflow.utils.Constants;

/**
 *
 * @author USER
 */
public class LDAPGroup {

    private String ldapParam;
    private String ldapGroupName;
    private String ldapGroupBase;
    private Constants.UserGroup group;

    public LDAPGroup(String ldapParam, String ldapGroupName, String ldapGroupBase, Constants.UserGroup pGroup) {
	this.ldapParam = ldapParam;
	this.ldapGroupName = ldapGroupName;
	this.ldapGroupBase = ldapGroupBase;
	this.group = pGroup;
    }

    public Constants.UserGroup getGroup() {
	return group;
    }

    public void setGroup(Constants.UserGroup group) {
	this.group = group;
    }

    public String getLdapGroupBase() {
	return ldapGroupBase;
    }

    public void setLdapGroupBase(String ldapGroupBase) {
	this.ldapGroupBase = ldapGroupBase;
    }

    public String getLdapParam() {
	return ldapParam;
    }

    public void setLdapParam(String ldapParam) {
	this.ldapParam = ldapParam;
    }

    public String getLdapGroupName() {
	return ldapGroupName;
    }

    public void setLdapGroupName(String ldapGroupName) {
	this.ldapGroupName = ldapGroupName;
    }

}

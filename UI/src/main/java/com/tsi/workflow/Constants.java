/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import java.util.jar.Attributes;

/**
 *
 * @author USER
 */
public class Constants {

    public static final Attributes.Name APPLICATION_NAME = new Attributes.Name("Implementation-Title");
    public static final Attributes.Name ORGANISATION_NAME = new Attributes.Name("Organisation");
    public static final Attributes.Name APPLICATION_VERSION = new Attributes.Name("Implementation-Version");
    public static final Attributes.Name APPLICATION_BUILD = new Attributes.Name("Build-Number");
    public static final Attributes.Name APPLICATION_BUILD_TIME = new Attributes.Name("Build-Time");
    public static Boolean isSSOApp = Boolean.FALSE;

    public enum SSOHeaders {

	AUTHORIZATION("authorization"), // Basic Token
	DELLTAEMPLOYEE("delltaemployee"),
	GECOS("gecos"), // full name
	HOMEDIRECTORY("homedirectory"),
	HOST("host"), // DEV/QA
	LISTOFROLES("listofroles"), // list of roles
	MAIL("mail"),
	SM_AUTHDIRNAME("sm_authdirname"), // domain
	SM_AUTHDIROID("sm_authdiroid"),
	SM_REALM("sm_realm"),
	SM_REALMOID("sm_realmoid"),
	SM_SERVERSESSIONID("sm_serversessionid"),
	SM_SERVERSESSIONSPEC("sm_serversessionspec"),
	SM_TIMETOEXPIRE("sm_timetoexpire"),
	SM_TRANSACTIONID("sm_transactionid"),
	SM_UNIVERSALID("sm_universalid"), // list of uids
	SM_USER("sm_user"), // uid
	SM_USERDN("sm_userdn"), // cn
	USER_AGENT("user-agent"),
	X_FORWARDED_FOR("x-forwarded-for");

	String key;

	private SSOHeaders(String pKey) {
	    key = pKey;
	}

	public String getKey() {
	    return key;
	}

    }
}

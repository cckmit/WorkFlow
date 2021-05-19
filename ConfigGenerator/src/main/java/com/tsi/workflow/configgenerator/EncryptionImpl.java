/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.configgenerator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author USER
 */
public class EncryptionImpl {

    static ArrayList<String> lExceptions = new ArrayList<>();

    static {
	lExceptions.add("wf.api.url");
	lExceptions.add("wf.deploy.server.url");
    }
    public static String lAllowedKeys = "^(bpm|db|wf|ldap|git|gitblit|jenkins|jgit|service|tos|ssh|mail|profileName|ra|adhoc|bgjob|cdn).*";
    public static String lAllowedKeys4Enc = "^(.*\\.url|.*\\.dir|.*\\.password|.*\\.path\\..*|.*\\.host|.*\\.path)$";

    public static void encryptProperties(Properties lProps, String salt) {
	for (Map.Entry<Object, Object> entrySet : lProps.entrySet()) {
	    String key = (String) entrySet.getKey();
	    String orginalValue = (String) entrySet.getValue();
	    String encryptedValue = "";
	    if (key.matches(lAllowedKeys)) {
		if (key.matches(lAllowedKeys4Enc) && !lExceptions.contains(key)) {
		    if (orginalValue.startsWith("ENC(") || orginalValue.isEmpty()) {
			encryptedValue = orginalValue;
		    } else {
			encryptedValue = "ENC(" + EncryptUtil.encrypt(orginalValue, salt) + ")";
		    }
		} else {
		    encryptedValue = orginalValue;
		}
		lProps.setProperty(key, encryptedValue);
	    }
	}
    }

    public static void decryptProperties(Properties lProps, String salt) {
	for (Map.Entry<Object, Object> entrySet : lProps.entrySet()) {
	    String key = (String) entrySet.getKey();
	    String orginalValue = (String) entrySet.getValue();
	    String decryptedValue = "";
	    if (key.matches(lAllowedKeys)) {
		if (orginalValue.startsWith("ENC(")) {
		    String lTrimValue = orginalValue.substring(4, orginalValue.length() - 1);
		    decryptedValue = EncryptUtil.decrypt(lTrimValue, salt);
		} else {
		    decryptedValue = orginalValue;
		}
		lProps.setProperty(key, decryptedValue);
	    }
	}
    }

}

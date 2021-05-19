/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

/**
 *
 * @author USER
 */
public class YodaResult {

    private String lab;
    private int rc;
    private String ip;
    private String message;

    public String getLab() {
	return lab;
    }

    public void setLab(String lab) {
	this.lab = lab;
    }

    public int getRc() {
	return rc;
    }

    public void setRc(int rc) {
	this.rc = rc;
    }

    public String getIp() {
	return ip;
    }

    public void setIp(String ip) {
	this.ip = ip;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getErrorMessage() {
	if (rc > 0) {
	    if (message == null && ip != null) {
		return ip;
	    } else if (message == null && ip == null) {
		return "";
	    } else {
		return message;
	    }
	} else {
	    return "";
	}
    }

    public String getLogMessage() {
	if (rc > 0) {
	    if (message == null && ip != null) {
		return ip;
	    } else if (message == null && ip == null) {
		return "";
	    } else {
		return message;
	    }
	} else {
	    if (message == null) {
		return "";
	    } else {
		return message;
	    }
	}
    }

}

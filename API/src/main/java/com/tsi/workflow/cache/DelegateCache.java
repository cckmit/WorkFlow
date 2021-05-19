/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.cache;

import java.io.Serializable;

/**
 *
 * @author USER
 */
public class DelegateCache implements Serializable {

    private static final long serialVersionUID = 1692830940979099615L;

    String profile;
    String action;
    String from;
    String to;

    public DelegateCache(String profile, String action, String from, String to) {
	this.profile = profile;
	this.action = action;
	this.from = from;
	this.to = to;
    }

    public String getProfile() {
	return profile;
    }

    public void setProfile(String profile) {
	this.profile = profile;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getFrom() {
	return from;
    }

    public void setFrom(String from) {
	this.from = from;
    }

    public String getTo() {
	return to;
    }

    public void setTo(String to) {
	this.to = to;
    }

}

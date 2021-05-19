/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

/**
 *
 * @author prabhu.prabhakaran
 */
public enum AuthorizationControl {
    AUTHENTICATED,
    NAMED;

    public static AuthorizationControl fromName(String name) {
	for (AuthorizationControl type : values()) {
	    if (type.name().equalsIgnoreCase(name)) {
		return type;
	    }
	}
	return NAMED;
    }

    @Override
    public String toString() {
	return name();
    }
}

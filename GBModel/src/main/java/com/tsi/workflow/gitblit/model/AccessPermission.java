/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

import java.io.Serializable;

/**
 *
 * @author prabhu.prabhakaran
 */
public enum AccessPermission implements Serializable {
    NONE("N"),
    EXCLUDE("X"),
    VIEW("V"),
    CLONE("R"),
    PUSH("RW"),
    CREATE("RWC"),
    DELETE("RWD"),
    REWIND("RW+"),
    OWNER("RW+");

    public static final AccessPermission[] NEWPERMISSIONS = { EXCLUDE, VIEW, CLONE, PUSH, CREATE, DELETE, REWIND };

    public static final AccessPermission[] SSHPERMISSIONS = { VIEW, CLONE, PUSH };

    public static AccessPermission LEGACY = REWIND;

    public final String code;

    private AccessPermission(String code) {
	this.code = code;
    }

    public boolean atMost(AccessPermission perm) {
	return ordinal() <= perm.ordinal();
    }

    public boolean atLeast(AccessPermission perm) {
	return ordinal() >= perm.ordinal();
    }

    public boolean exceeds(AccessPermission perm) {
	return ordinal() > perm.ordinal();
    }

    public String asRole(String repository) {
	return code + ":" + repository;
    }

    @Override
    public String toString() {
	return code;
    }

    public static AccessPermission permissionFromRole(String role) {
	String[] fields = role.split(":", 2);
	if (fields.length == 1) {
	    // legacy/undefined assume full permissions
	    return AccessPermission.LEGACY;
	} else {
	    // code:repository
	    return AccessPermission.fromCode(fields[0]);
	}
    }

    public static String repositoryFromRole(String role) {
	String[] fields = role.split(":", 2);
	if (fields.length == 1) {
	    // legacy/undefined assume full permissions
	    return role;
	} else {
	    // code:repository
	    return fields[1];
	}
    }

    public static AccessPermission fromCode(String code) {
	for (AccessPermission perm : values()) {
	    if (perm.code.equalsIgnoreCase(code)) {
		return perm;
	    }
	}
	return AccessPermission.NONE;
    }
}

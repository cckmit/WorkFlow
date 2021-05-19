/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author prabhu.prabhakaran
 */
public enum AccessRestrictionType {
    NONE,
    PUSH,
    CLONE,
    VIEW;

    private static final AccessRestrictionType[] AUTH_TYPES = { PUSH, CLONE, VIEW };

    public static AccessRestrictionType fromName(String name) {
	for (AccessRestrictionType type : values()) {
	    if (type.name().equalsIgnoreCase(name)) {
		return type;
	    }
	}
	return NONE;
    }

    public static List<AccessRestrictionType> choices(boolean allowAnonymousPush) {
	if (allowAnonymousPush) {
	    return Arrays.asList(values());
	}
	return Arrays.asList(AUTH_TYPES);
    }

    public boolean exceeds(AccessRestrictionType type) {
	return this.ordinal() > type.ordinal();
    }

    public boolean atLeast(AccessRestrictionType type) {
	return this.ordinal() >= type.ordinal();
    }

    @Override
    public String toString() {
	return name();
    }

    public boolean isValidPermission(AccessPermission permission) {
	switch (this) {
	case VIEW:
	    // VIEW restriction
	    // all access permissions are valid
	    return true;
	case CLONE:
	    // CLONE restriction
	    // only CLONE or greater access permissions are valid
	    return permission.atLeast(AccessPermission.CLONE);
	case PUSH:
	    // PUSH restriction
	    // only PUSH or greater access permissions are valid
	    return permission.atLeast(AccessPermission.PUSH);
	case NONE:
	    // NO access restriction
	    // all access permissions are invalid
	    return false;
	}
	return false;
    }
}

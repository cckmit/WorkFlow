/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author USER
 */
public class RepositoryPermissionTest {

    public RepositoryPermissionTest() {
    }

    @Test
    public void testGetRegistrantType() {
	RepositoryPermission instance = new RepositoryPermission(null, null);
	String expResult = "";
	String registrantType = "";
	instance.setRegistrantType(registrantType);
	String result = instance.getRegistrantType();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetPermissionType() {
	RepositoryPermission instance = new RepositoryPermission(null, null);
	String expResult = "";
	String permissionType = "";
	instance.setPermissionType(permissionType);
	String result = instance.getPermissionType();
	assertEquals(expResult, result);
    }

    @Test
    public void testIsMutable() {
	RepositoryPermission instance = new RepositoryPermission(null, null);
	boolean expResult = false;
	boolean mutable = false;
	instance.setMutable(mutable);
	boolean result = instance.isMutable();
	assertEquals(expResult, result);
    }

}

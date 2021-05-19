/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.ldap.config;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.utils.Constants;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LDAPGroupTest {

    public LDAPGroupTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getGroup method, of class LDAPGroup.
     */
    @Test
    public void testGetGroup() {

	LDAPGroup instance = new LDAPGroup("1", "2", "3", Constants.UserGroup.DevManager);
	Constants.UserGroup expResult = Constants.UserGroup.DevManager;
	instance.setGroup(expResult);
	Constants.UserGroup result = instance.getGroup();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLdapGroupBase method, of class LDAPGroup.
     */
    @Test
    public void testGetLdapGroupBase() {

	LDAPGroup instance = new LDAPGroup("1", "2", "3", Constants.UserGroup.DevManager);
	String expResult = "1";
	instance.setLdapGroupBase(expResult);
	String result = instance.getLdapGroupBase();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLdapParam method, of class LDAPGroup.
     */
    @Test
    public void testGetLdapParam() {

	LDAPGroup instance = new LDAPGroup("1", "2", "3", Constants.UserGroup.DevManager);
	String expResult = "1";
	instance.setLdapParam(expResult);
	String result = instance.getLdapParam();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLdapGroupName method, of class LDAPGroup.
     */
    @Test
    public void testGetLdapGroupName() {

	LDAPGroup instance = new LDAPGroup("1", "2", "3", Constants.UserGroup.DevManager);
	String expResult = "1";
	instance.setLdapGroupName(expResult);
	String result = instance.getLdapGroupName();
	assertEquals(expResult, result);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LdapConfigTest {

    public LdapConfigTest() {
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
     * Test of getUserAttributes method, of class LdapConfig.
     */
    @Test
    public void testGetUserAttributes() {

	LdapConfig instance = new LdapConfig();
	List<String> expResult = Arrays.asList("1");
	instance.setUserAttributes("1");
	List<String> result = instance.getUserAttributes();
	assertEquals(expResult, result);
    }

    /**
     * Test of getUrl method, of class LdapConfig.
     */
    @Test
    public void testGetUrl() {

	LdapConfig instance = new LdapConfig();
	String expResult = "1";
	instance.setUrl(expResult);
	String result = instance.getUrl();
	assertEquals(expResult, result);
    }

    /**
     * Test of getUserSearchBase method, of class LdapConfig.
     */
    @Test
    public void testGetUserSearchBase() {

	LdapConfig instance = new LdapConfig();
	String expResult = "1";
	instance.setUserSearchBase(expResult);
	String result = instance.getUserSearchBase();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGroupSearchBase method, of class LdapConfig.
     */
    @Test
    public void testGetGroupSearchBase() {

	LdapConfig instance = new LdapConfig();
	String expResult = "1";
	instance.setGroupSearchBase(expResult);
	String result = instance.getGroupSearchBase();
	assertEquals(expResult, result);
    }

    /**
     * Test of getUserClass method, of class LdapConfig.
     */
    @Test
    public void testGetUserClass() {

	LdapConfig instance = new LdapConfig();
	String expResult = "1";
	instance.setUserClass(expResult);
	String result = instance.getUserClass();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGroupClass method, of class LdapConfig.
     */
    @Test
    public void testGetGroupClass() {

	LdapConfig instance = new LdapConfig();
	String expResult = "";
	instance.setGroupClass(expResult);
	String result = instance.getGroupClass();
	assertEquals(expResult, result);
    }

    /**
     * Test of getUserParam method, of class LdapConfig.
     */
    @Test
    public void testGetUserParam() {

	LdapConfig instance = new LdapConfig();
	String expResult = "1";
	instance.setUserParam(expResult);
	String result = instance.getUserParam();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGroupParam method, of class LdapConfig.
     */
    @Test
    public void testGetGroupParam() {

	LdapConfig instance = new LdapConfig();
	String expResult = "1";
	instance.setGroupParam(expResult);
	String result = instance.getGroupParam();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGroupAttribute method, of class LdapConfig.
     */
    @Test
    public void testGetGroupAttribute() {

	LdapConfig instance = new LdapConfig();
	String expResult = "1";
	instance.setGroupAttribute(expResult);
	String result = instance.getGroupAttribute();
	assertEquals(expResult, result);
    }

}

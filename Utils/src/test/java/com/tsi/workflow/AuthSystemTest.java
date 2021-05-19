/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class AuthSystemTest {

    public AuthSystemTest() {
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
     * Test of getIpaddress method, of class AuthSystem.
     */
    @Test
    public void testGetIpaddress() {
	AuthSystem instance = new AuthSystem();
	String expResult = "";
	instance.setIpaddress(expResult);
	String result = instance.getIpaddress();
	assertEquals(expResult, result);
    }

    /**
     * Test of getPortno method, of class AuthSystem.
     */
    @Test
    public void testGetPortno() {
	AuthSystem instance = new AuthSystem();
	Integer expResult = null;
	instance.setPortno(expResult);
	Integer result = instance.getPortno();
	assertEquals(expResult, result);
	AuthSystem instance1 = new AuthSystem("", 1, "");
    }

    /**
     * Test of getName method, of class AuthSystem.
     */
    @Test
    public void testGetName() {
	AuthSystem instance = new AuthSystem();
	String expResult = "";
	instance.setName(expResult);
	String result = instance.getName();
	assertEquals(expResult, result);
    }

}

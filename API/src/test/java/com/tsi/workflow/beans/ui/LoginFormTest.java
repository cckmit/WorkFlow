/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LoginFormTest {

    public LoginFormTest() {
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
     * Test of getUsername method, of class LoginForm.
     */
    @Test
    public void testGetUsername() {

	LoginForm instance = new LoginForm();
	String expResult = "1";
	instance.setUsername(expResult);
	String result = instance.getUsername();
	assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method, of class LoginForm.
     */
    @Test
    public void testGetPassword() {

	LoginForm instance = new LoginForm();
	String expResult = "1";
	instance.setPassword(expResult);
	String result = instance.getPassword();
	assertEquals(expResult, result);
    }

}

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
public class ExecModelTest {

    public ExecModelTest() {
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
     * Test of getUser method, of class ExecModel.
     */
    @Test
    public void testGetUser() {
	ExecModel instance = new ExecModel();
	User expResult = null;
	instance.setUser(expResult);
	User result = instance.getUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSystem method, of class ExecModel.
     */
    @Test
    public void testGetSystem() {
	ExecModel instance = new ExecModel();
	AuthSystem expResult = null;
	instance.setSystem(expResult);
	AuthSystem result = instance.getSystem();
	assertEquals(expResult, result);
	ExecModel instance1 = new ExecModel(null, null, null);
    }

    /**
     * Test of getCommand method, of class ExecModel.
     */
    @Test
    public void testGetCommand() {
	ExecModel instance = new ExecModel();
	String expResult = "";
	instance.setCommand(expResult);
	String result = instance.getCommand();
	assertEquals(expResult, result);
    }

}

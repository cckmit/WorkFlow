/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.*;

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
public class AuthModelTest {

    public AuthModelTest() {
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
     * Test of getUser method, of class AuthModel.
     */
    @Test
    public void testGetUser() {
	AuthModel instance = new AuthModel();
	User expResult = null;
	instance.setUser(expResult);
	User result = instance.getUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSystems method, of class AuthModel.
     */
    @Test
    public void testGetSystems() {
	AuthModel instance = new AuthModel();
	List<AuthSystem> expResult = null;
	instance.setSystems(expResult);
	List<AuthSystem> result = instance.getSystems();
	assertEquals(expResult, result);
    }

    /**
     * Test of addSystem method, of class AuthModel.
     */
    @Test
    public void testAddSystem() {
	AuthSystem pSystem = new AuthSystem("", 1, "");
	AuthModel instance = new AuthModel();
	instance.addSystem(pSystem);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.beans.dao.LoadFreeze;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LoadFreezeFormTest {

    public LoadFreezeFormTest() {
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
     * Test of getLoadFreeze method, of class LoadFreezeForm.
     */
    @Test
    public void testGetLoadFreeze() {

	LoadFreezeForm instance = new LoadFreezeForm();
	LoadFreeze expResult = new LoadFreeze(1);
	instance.setLoadFreeze(expResult);
	LoadFreeze result = instance.getLoadFreeze();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSystem method, of class LoadFreezeForm.
     */
    @Test
    public void testGetSystem() {

	LoadFreezeForm instance = new LoadFreezeForm();
	com.tsi.workflow.beans.dao.System expResult = new com.tsi.workflow.beans.dao.System(1);
	instance.setSystem(expResult);
	com.tsi.workflow.beans.dao.System result = instance.getSystem();
	assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class LoadFreezeForm.
     */
    @Test
    public void testEquals() {

	LoadFreeze obj = new LoadFreeze();
	LoadFreezeForm instance = new LoadFreezeForm();
	boolean expResult = false;
	boolean result = instance.equals(obj);
	assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class LoadFreezeForm.
     */
    // @Test
    // public void testHashCode() {
    //
    // LoadFreezeForm instance = new LoadFreezeForm();
    // int expResult = instance.hashCode();
    // int result = instance.hashCode();
    // assertEquals(expResult, result);
    // }

}

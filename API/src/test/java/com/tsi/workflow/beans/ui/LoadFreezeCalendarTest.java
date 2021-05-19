/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.DataWareHouse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LoadFreezeCalendarTest {

    public LoadFreezeCalendarTest() {
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
     * Test of getDate method, of class LoadFreezeCalendar.
     */
    @Test
    public void testGetDate() {

	LoadFreezeCalendar instance = new LoadFreezeCalendar(DataWareHouse.lInputString);
	String expResult = DataWareHouse.lInputString;
	instance.setDate(expResult);
	String result = instance.getDate();
	assertEquals(expResult, result);
    }

}

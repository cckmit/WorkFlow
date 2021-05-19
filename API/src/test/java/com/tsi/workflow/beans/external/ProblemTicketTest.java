/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.external;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ProblemTicketTest {

    public ProblemTicketTest() {
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
     * Test of getRefNum method, of class ProblemTicket.
     */
    @Test
    public void testGetRefNum() {

	ProblemTicket instance = new ProblemTicket();
	String expResult = DataWareHouse.lInputString;
	instance.setRefNum(expResult);
	String result = instance.getRefNum();
	assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class ProblemTicket.
     */
    @Test
    public void testGetStatus() {

	ProblemTicket instance = new ProblemTicket();
	String expResult = DataWareHouse.lInputString;
	instance.setStatus(expResult);
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class ProblemTicket.
     */
    @Test
    public void testHashCode() {

	ProblemTicket instance = new ProblemTicket();
	int expResult = 0;
	int result = instance.hashCode();
	assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class ProblemTicket.
     */
    @Test
    public void testEquals() {

	ProblemTicket object = new ProblemTicket();
	ProblemTicket instance = new ProblemTicket();
	boolean expResult = true;
	boolean result = instance.equals(object);
	assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class ProblemTicket.
     */
    @Test
    public void testEquals2() {
	ImpPlan object = new ImpPlan();
	ProblemTicket instance = new ProblemTicket();
	boolean expResult = false;
	boolean result = instance.equals(object);
	assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class ProblemTicket.
     */
    @Test
    public void testEquals3() {
	ProblemTicket object = new ProblemTicket();
	object.setRefNum("123");
	ProblemTicket instance = new ProblemTicket();
	instance.setRefNum("456");
	boolean expResult = false;
	boolean result = instance.equals(object);
	assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class ProblemTicket.
     */
    @Test
    public void testToString() {

	ProblemTicket instance = new ProblemTicket();
	String refNum = null;
	String expResult = "com.tsi.workflow.beans.ProblemTicket_1[ refNum=" + refNum + " ]";
	String result = instance.toString();
	assertEquals(expResult, result);
    }

}

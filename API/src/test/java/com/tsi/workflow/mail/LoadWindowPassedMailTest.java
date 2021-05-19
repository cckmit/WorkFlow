package com.tsi.workflow.mail;

import com.tsi.workflow.DataWareHouse;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class LoadWindowPassedMailTest {

    public LoadWindowPassedMailTest() {
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
     * Test of Imp Plan method, of class LoadWindowPassedMail.
     */
    @Test
    public void testSetImpPlan() {
	String impPlan = DataWareHouse.getPlan().getId();
	LoadWindowPassedMail instance = new LoadWindowPassedMail();
	instance.setImpPlan(impPlan);
    }

    @Test
    public void testSetTargetSystem() {
	Set<String> targetSystem = new HashSet<>();
	targetSystem.add("WSP");
	LoadWindowPassedMail instance = new LoadWindowPassedMail();
	instance.setTargetSystem(targetSystem);
    }

    @Test
    public void testSystemLoadDate() {
	Date now = new Date();
	LoadWindowPassedMail instance = new LoadWindowPassedMail();
	instance.setLoadDateTime(now);
    }

    /**
     * Test of processMessage method, of class LoadWindowPassedMail.
     */
    @Test
    public void testProcessMessage() {

	LoadWindowPassedMail instance = new LoadWindowPassedMail();
	instance.processMessage();
    }

}

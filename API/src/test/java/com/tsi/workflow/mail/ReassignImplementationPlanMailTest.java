/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author gn.ebinezardharmaraj
 */
public class ReassignImplementationPlanMailTest {

    @Test
    public void test00() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setRole("Developer");
	assertEquals("Developer", reassignImplementationPlanMail0.getRole());
    }

    @Test
    public void test01() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setRole("");
	assertNotNull(reassignImplementationPlanMail0.getRole());
    }

    @Test
    public void test02() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setOldLead("siva");
	assertEquals("siva", reassignImplementationPlanMail0.getOldLead());
    }

    @Test
    public void test03() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setOldLead("");
	assertNotNull(reassignImplementationPlanMail0.getOldLead());
    }

    @Test
    public void test04() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setNewLead("{0} Application Developer Lead reassignment");
	assertEquals("{0} Application Developer Lead reassignment", reassignImplementationPlanMail0.getNewLead());
    }

    @Test
    public void test05() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setNewLead("");
	assertNotNull(reassignImplementationPlanMail0.getNewLead());
    }

    @Test
    public void test06() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setImpPlanId("T1000180");
	assertEquals("T1000180", reassignImplementationPlanMail0.getImpPlanId());
    }

    @Test
    public void test07() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setImpPlanId("");
	assertNotNull(reassignImplementationPlanMail0.getImpPlanId());
    }

    @Test
    public void test08() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setDevManager("suresh");
	assertEquals("suresh", reassignImplementationPlanMail0.getDevManager());
    }

    @Test
    public void test09() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setCurrentUser("Tim");
	assertEquals("Tim", reassignImplementationPlanMail0.getCurrentUser());
    }

    @Test
    public void test10() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setCurrentUser("");
	assertNotNull(reassignImplementationPlanMail0.getCurrentUser());
    }

    @Test
    public void test11() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	// Undeclared exception!
	try {
	    reassignImplementationPlanMail0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test
    public void test12() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setRole("Lead");
	reassignImplementationPlanMail0.processMessage();
    }

    @Test
    public void test13() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setRole("DevManager");
	reassignImplementationPlanMail0.processMessage();
    }

    @Test
    public void test15() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setRole("DevManager");
	assertEquals("DevManager", reassignImplementationPlanMail0.getRole());
    }

    @Test
    public void test16() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setNewLead("Arul");
	assertEquals("Arul", reassignImplementationPlanMail0.getNewLead());
    }

    @Test
    public void test18() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setOldLead("Deepa");
	assertEquals("Deepa", reassignImplementationPlanMail0.getOldLead());
    }

    @Test
    public void test19() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setDevManager("");
	assertNotNull(reassignImplementationPlanMail0.getDevManager());
    }

    @Test
    public void test20() throws Throwable {
	ReassignImplementationPlanMail reassignImplementationPlanMail0 = new ReassignImplementationPlanMail();
	reassignImplementationPlanMail0.setCurrentUser("Thilak");
	assertEquals("Thilak", reassignImplementationPlanMail0.getCurrentUser());
    }

}

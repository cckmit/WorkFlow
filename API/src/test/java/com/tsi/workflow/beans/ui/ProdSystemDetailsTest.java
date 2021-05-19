package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProdSystemDetailsTest {

    private ProdSystemDetails prodSystemDetails;

    @Before
    public void setUp() throws Exception {
	prodSystemDetails = new ProdSystemDetails();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProdSystemDetails() {
	prodSystemDetails.setLoaddatetime("2019/8/19");
	assertEquals("2019/8/19", prodSystemDetails.getLoaddatetime());
	prodSystemDetails.setPlanid("P1234");
	assertEquals("P1234", prodSystemDetails.getPlanid());
	prodSystemDetails.setProdstatus("ONLINE");
	assertEquals("ONLINE", prodSystemDetails.getProdstatus());
	prodSystemDetails.setProgramname("Program Name");
	assertEquals("Program Name", prodSystemDetails.getProgramname());
	prodSystemDetails.setTargetsystem("XYZ");
	assertEquals("XYZ", prodSystemDetails.getTargetsystem());
	assertNotNull(prodSystemDetails.toString());

	ProdSystemDetails prodSystemDetails1 = new ProdSystemDetails(prodSystemDetails.getTargetsystem(), prodSystemDetails.getProdstatus(), prodSystemDetails.getLoaddatetime(), prodSystemDetails.getProgramname());
    }

}

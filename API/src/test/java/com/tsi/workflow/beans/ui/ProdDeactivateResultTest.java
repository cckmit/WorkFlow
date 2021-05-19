package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.sql.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProdDeactivateResultTest {

    ProdDeactivateResult prodDeactivateResult;

    @Before
    public void setUp() throws Exception {
	prodDeactivateResult = new ProdDeactivateResult();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProdDeactivateResult() {
	prodDeactivateResult.setDeveloperid("D123");
	assertEquals("D123", prodDeactivateResult.getDeveloperid());
	prodDeactivateResult.setDevmanager("Devmanager");
	assertEquals("Devmanager", prodDeactivateResult.getDevmanager());
	prodDeactivateResult.setLeadid("L1234");
	assertEquals("L1234", prodDeactivateResult.getLeadid());
	Date date = new Date(2019, 8, 23);
	prodDeactivateResult.setLoaddate(date);
	assertEquals(date, prodDeactivateResult.getLoaddate());
	prodDeactivateResult.setPlanid("P1234");
	assertEquals("P1234", prodDeactivateResult.getPlanid());
	prodDeactivateResult.setPlanstatus("ONLINE");
	assertEquals("ONLINE", prodDeactivateResult.getPlanstatus());
	prodDeactivateResult.setReviewers("RevivewerName");
	assertEquals("RevivewerName", prodDeactivateResult.getReviewers());
	prodDeactivateResult.setSysname("XYZ");
	assertEquals("XYZ", prodDeactivateResult.getSysname());
    }

}

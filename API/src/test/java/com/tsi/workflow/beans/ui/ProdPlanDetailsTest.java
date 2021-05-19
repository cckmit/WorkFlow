package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProdPlanDetailsTest {

    private ProdPlanDetails prodPlanDetails;

    @Before
    public void setUp() throws Exception {
	prodPlanDetails = new ProdPlanDetails();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProdPlanDetails() {
	prodPlanDetails.setActivateddatetime(new Date(2019, 8, 19));
	assertEquals(new Date(2019, 8, 19), prodPlanDetails.getActivateddatetime());
	prodPlanDetails.setDeveloperid("D1234");
	assertEquals("D1234", prodPlanDetails.getDeveloperid());
	prodPlanDetails.setDevelopername("DeveloperName");
	assertEquals("DeveloperName", prodPlanDetails.getDevelopername());
	prodPlanDetails.setDevmamangername("DevManagerName");
	assertEquals("DevManagerName", prodPlanDetails.getDevmamangername());
	prodPlanDetails.setDevmanagerid("DM1234");
	assertEquals("DM1234", prodPlanDetails.getDevmanagerid());
	prodPlanDetails.setLoaddatetime(new Date(2019, 9, 19));
	assertEquals(new Date(2019, 9, 19), prodPlanDetails.getLoaddatetime());
	prodPlanDetails.setLoadtype("A");
	assertEquals("A", prodPlanDetails.getLoadtype());
	prodPlanDetails.setPlandescription("Plan description");
	assertEquals("Plan description", prodPlanDetails.getPlandescription());
	prodPlanDetails.setPlanid("P1234");
	assertEquals("P1234", prodPlanDetails.getPlanid());
	prodPlanDetails.setPlanstatus("ONLINE");
	assertEquals("ONLINE", prodPlanDetails.getPlanstatus());
	List<ProdSystemDetails> prodSystemDetailsList = new ArrayList<>();
	prodPlanDetails.setProdSystemDetails(prodSystemDetailsList);
	assertEquals(prodSystemDetailsList, prodPlanDetails.getProdSystemDetails());
	assertNotNull(prodPlanDetails.toString());
	ProdPlanDetails prodPlanDetails1 = new ProdPlanDetails(prodPlanDetails.getPlanid(), prodPlanDetails.getPlandescription(), prodPlanDetails.getDeveloperid(), prodPlanDetails.getDevelopername(), prodPlanDetails.getDevmanagerid(), prodPlanDetails.getDevmamangername(), prodPlanDetails.getLoadtype(), prodPlanDetails.getPlanstatus(), prodPlanDetails.getActivateddatetime(), prodPlanDetails.getLoaddatetime(), prodPlanDetails.getProdSystemDetails());

    }

}

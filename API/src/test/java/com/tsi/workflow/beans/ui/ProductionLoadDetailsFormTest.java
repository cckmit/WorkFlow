package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.sql.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProductionLoadDetailsFormTest {

    private ProductionLoadDetailsForm productionLoadDetailsForm;

    @Before
    public void setUp() throws Exception {
	productionLoadDetailsForm = new ProductionLoadDetailsForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProductionLoadDetailsForm() {
	productionLoadDetailsForm.setActivateddatetime(new Date(2019, 8, 19));
	assertEquals(new Date(2019, 8, 19), productionLoadDetailsForm.getActivateddatetime());
	productionLoadDetailsForm.setDeveloperid("D1234");
	assertEquals("D1234", productionLoadDetailsForm.getDeveloperid());
	productionLoadDetailsForm.setDevelopername("DeveloperName");
	assertEquals("DeveloperName", productionLoadDetailsForm.getDevelopername());
	productionLoadDetailsForm.setDevmamangername("DevManagerName");
	assertEquals("DevManagerName", productionLoadDetailsForm.getDevmamangername());
	productionLoadDetailsForm.setDevmanagerid("DM1234");
	assertEquals("DM1234", productionLoadDetailsForm.getDevmanagerid());
	productionLoadDetailsForm.setLoaddatetime(new Date(2019, 9, 19));
	assertEquals(new Date(2019, 9, 19), productionLoadDetailsForm.getLoaddatetime());
	productionLoadDetailsForm.setLoadtype("A");
	assertEquals("A", productionLoadDetailsForm.getLoadtype());
	productionLoadDetailsForm.setPlandescription("Plan description");
	assertEquals("Plan description", productionLoadDetailsForm.getPlandescription());
	productionLoadDetailsForm.setPlanid("P1234");
	assertEquals("P1234", productionLoadDetailsForm.getPlanid());
	productionLoadDetailsForm.setPlanstatus("ONLINE");
	assertEquals("ONLINE", productionLoadDetailsForm.getPlanstatus());
	productionLoadDetailsForm.setProdstatus("ACTIVE");
	assertEquals("ACTIVE", productionLoadDetailsForm.getProdstatus());
	productionLoadDetailsForm.setProgramname("Program Name");
	assertEquals("Program Name", productionLoadDetailsForm.getProgramname());
	productionLoadDetailsForm.setTargetsystem("XYZ");
	assertEquals("XYZ", productionLoadDetailsForm.getTargetsystem());

	assertNotNull(productionLoadDetailsForm.toString());

    }

}

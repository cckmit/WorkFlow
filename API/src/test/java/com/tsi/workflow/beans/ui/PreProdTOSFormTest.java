package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import com.tsi.workflow.beans.dao.PreProductionLoads;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PreProdTOSFormTest {

    private PreProdTOSForm preProdTOSForm;

    @Before
    public void setUp() throws Exception {
	preProdTOSForm = new PreProdTOSForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testpreProdTOSForm() {
	preProdTOSForm.setAction("Test");
	assertEquals("Test", preProdTOSForm.getAction());
	preProdTOSForm.setPreProdLoads(new ArrayList<PreProductionLoads>());
	assertNotNull(preProdTOSForm.getPreProdLoads());

    }

}

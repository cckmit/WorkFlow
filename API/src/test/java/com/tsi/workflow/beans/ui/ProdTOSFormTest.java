package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import com.tsi.workflow.beans.dao.ProductionLoads;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProdTOSFormTest {

    private ProdTOSForm prodTOSForm;

    @Before
    public void setUp() throws Exception {
	prodTOSForm = new ProdTOSForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProdTOSForm() {
	prodTOSForm.setAction("ASBCD");
	assertEquals("ASBCD", prodTOSForm.getAction());
	prodTOSForm.setProdLoads(new ArrayList<ProductionLoads>());
	assertNotNull(prodTOSForm.getProdLoads());

    }

}

package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSystemMaintenanceMailFormTest {

    private TestSystemMaintenanceMailForm testSystemMaintenanceMailForm;

    @Before
    public void setUp() throws Exception {
	testSystemMaintenanceMailForm = new TestSystemMaintenanceMailForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTestSystemMaintenanceMailForm() {
	testSystemMaintenanceMailForm.setInfo("Info");
	assertEquals("Info", testSystemMaintenanceMailForm.getInfo());
	testSystemMaintenanceMailForm.setKey("Key");
	assertEquals("Key", testSystemMaintenanceMailForm.getKey());
	testSystemMaintenanceMailForm.setSystemName("XYZ");
	assertEquals("XYZ", testSystemMaintenanceMailForm.getSystemName());
    }

}

package com.tsi.workflow;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExcelTest {

    @Before
    public void setUp() throws Exception {
	ExecModel excelModel = new ExecModel(DataWareHouse.getUser(), getExcelModel().getSystem(), getExcelModel().getCommand());
	Assert.assertTrue(excelModel.equals(excelModel));
    }

    public ExecModel getExcelModel() {
	ExecModel instance = new ExecModel();
	instance.setCommand("Export Excel");
	instance.setSystem(new AuthSystem("http://vhldvztdt001.tvlport.net", 9000, "tp"));
	instance.setUser(DataWareHouse.getUser());
	return instance;

    }

    @Test
    public void getExcelTest() {
	assertEquals("Export Excel", getExcelModel().getCommand());
	assertEquals(9000, getExcelModel().getSystem().getPortno().intValue());
	assertEquals(DataWareHouse.getUser(), getExcelModel().getUser());
	assertEquals("http://vhldvztdt001.tvlport.net", getAuthSystem().getIpaddress());
	assertEquals("tp", getAuthSystem().getName());
	assertEquals(9000, getAuthSystem().getPortno().intValue());
    }

    public AuthSystem getAuthSystem() {
	AuthSystem authSystem = new AuthSystem();
	authSystem.setIpaddress("http://vhldvztdt001.tvlport.net");
	authSystem.setName("tp");
	authSystem.setPortno(9000);
	return authSystem;

    }

}

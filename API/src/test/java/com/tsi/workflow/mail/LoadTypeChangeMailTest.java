package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;

public class LoadTypeChangeMailTest {

    @Test
    public void test00() throws Throwable {
	LoadTypeChangeMail loadTypeChangeMail = new LoadTypeChangeMail();
	loadTypeChangeMail.setLoadType("AUX");
	assertEquals("AUX", loadTypeChangeMail.getLoadType());
	loadTypeChangeMail.setLoadTypeComment("loadTypeComment");
	assertEquals("loadTypeComment", loadTypeChangeMail.getLoadTypeComment());
	loadTypeChangeMail.setPlanDescrption("planDescrption");
	assertEquals("planDescrption", loadTypeChangeMail.getPlanDescrption());
	loadTypeChangeMail.setPlanId("T1900111");
	assertEquals("T1900111", loadTypeChangeMail.getPlanId());
	Set<String> prNumber = new TreeSet<String>();
	prNumber.add("A");
	prNumber.add("B");
	loadTypeChangeMail.setPrNumberList(prNumber);
	assertNotNull(prNumber);
	loadTypeChangeMail.setUserDetails(DataWareHouse.getUser());
	assertEquals(DataWareHouse.getUser(), loadTypeChangeMail.getUserDetails());
	Map<String, Date> newLoadDateTargetSys = new HashMap<String, Date>();
	newLoadDateTargetSys.put("WSP", new Date());
	newLoadDateTargetSys.put("APO", new Date());
	loadTypeChangeMail.setNewLoadDateTargetSys(newLoadDateTargetSys);
	assertNotNull(loadTypeChangeMail.getNewLoadDateTargetSys());
	loadTypeChangeMail.setOldLoadDateTargetSys(newLoadDateTargetSys);
	assertNotNull(loadTypeChangeMail.getOldLoadDateTargetSys());
	loadTypeChangeMail.processMessage();

    }

}

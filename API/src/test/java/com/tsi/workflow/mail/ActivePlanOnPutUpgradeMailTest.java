package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import org.junit.Test;

public class ActivePlanOnPutUpgradeMailTest {

    @Test
    public void test00() throws Throwable {
	ActivePlanOnPutUpgradeMail activePlanOnPutUpgradeMail = new ActivePlanOnPutUpgradeMail();
	activePlanOnPutUpgradeMail.setBackUpPut(new PutLevel());
	assertNotNull(activePlanOnPutUpgradeMail.getBackUpPut());
	activePlanOnPutUpgradeMail.setPlan(DataWareHouse.getPlan());
	assertNotNull(activePlanOnPutUpgradeMail.getPlan());
	activePlanOnPutUpgradeMail.setProdPutLevel(new PutLevel());
	assertNotNull(activePlanOnPutUpgradeMail.getProdPutLevel());
	// activePlanOnPutUpgradeMail.processMessage();
    }

    @Test
    public void test01() throws Throwable {
	ActivePlanOnPutUpgradeMail activePlanOnPutUpgradeMail = new ActivePlanOnPutUpgradeMail();
	PutLevel putlevel = new PutLevel();
	putlevel.setPutLevel("PUT14A");
	System sys = new System();
	sys.setId(5);
	putlevel.setSystemId(sys);
	activePlanOnPutUpgradeMail.setBackUpPut(putlevel);
	;
	activePlanOnPutUpgradeMail.setPlan(DataWareHouse.getPlan());
	activePlanOnPutUpgradeMail.setProdPutLevel(putlevel);
	activePlanOnPutUpgradeMail.processMessage();
    }

}

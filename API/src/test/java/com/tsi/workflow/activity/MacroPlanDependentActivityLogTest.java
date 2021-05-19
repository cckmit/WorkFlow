package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.Test;
import org.mockito.Mock;

public class MacroPlanDependentActivityLogTest {

    @Mock
    Implementation implementation;

    @Test
    public void test() throws Throwable {
	MacroPlanDependentActivityLog macroPlanDependentActivityLog = new MacroPlanDependentActivityLog(DataWareHouse.getPlan(), implementation);
	macroPlanDependentActivityLog.setUser(DataWareHouse.getUser());
	macroPlanDependentActivityLog.setActionType("actionType");
	macroPlanDependentActivityLog.setDependentPlans("T1900111");
	macroPlanDependentActivityLog.setlImpPlan(DataWareHouse.getPlan());
	macroPlanDependentActivityLog.setStatus(Boolean.TRUE);
	assertEquals("actionType", macroPlanDependentActivityLog.getActionType());
	assertEquals("T1900111", macroPlanDependentActivityLog.getDependentPlans());
	assertEquals(Boolean.TRUE, macroPlanDependentActivityLog.isStatus());
	assertEquals(DataWareHouse.getPlan(), macroPlanDependentActivityLog.getlImpPlan());
	assertNotNull(macroPlanDependentActivityLog.getUser());
	macroPlanDependentActivityLog.processMessage();
    }

    @Test
    public void testSetArguments() throws Throwable {
	IBeans[] beans = null;
	MacroHeaderOnlineMessage instance = new MacroHeaderOnlineMessage(null, null);
	instance.setArguments(beans);
    }

}

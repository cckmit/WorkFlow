package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.Test;
import org.mockito.Mock;

public class PreProdActionsActivityLogTest {

    @Mock
    ImpPlan impPlan;

    @Mock
    Implementation implementation;

    @Test
    public void test() {
	PreProdActionsActivityLog preProdActionsActivityLog = new PreProdActionsActivityLog(DataWareHouse.getPlan(), implementation);
	preProdActionsActivityLog.setStatus(Boolean.TRUE);
	preProdActionsActivityLog.setUser(DataWareHouse.getUser());
	preProdActionsActivityLog.setMessage("message");
	assertEquals("message", preProdActionsActivityLog.getMessage());
	assertNotNull(preProdActionsActivityLog.getUser());
	assertEquals(Boolean.TRUE, preProdActionsActivityLog.getStatus());
	preProdActionsActivityLog.processMessage();
    }

    @Test
    public void testSetArguments() throws Throwable {
	IBeans[] beans = null;
	PreProdActionsActivityLog instance = new PreProdActionsActivityLog(null, null);
	instance.setArguments(beans);

    }

}

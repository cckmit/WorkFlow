package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.Test;
import org.mockito.Mock;

public class ImpPlanApprovalActivityMessageTest {

    @Mock
    Implementation implementation;

    @Test
    public void test() throws Throwable {
	ImpPlanApprovalActivityMessage impPlanApprovalActivityMessage = new ImpPlanApprovalActivityMessage(DataWareHouse.getPlan(), implementation);
	impPlanApprovalActivityMessage.setAction("action");
	assertEquals("action", impPlanApprovalActivityMessage.getAction());
	impPlanApprovalActivityMessage.setApprovalComment("approvalComment");
	assertEquals("approvalComment", impPlanApprovalActivityMessage.getApprovalComment());
	impPlanApprovalActivityMessage.setFileName("fileName");
	assertEquals("fileName", impPlanApprovalActivityMessage.getFileName());
	impPlanApprovalActivityMessage.setOldComment("oldComment");
	assertEquals("oldComment", impPlanApprovalActivityMessage.getOldComment());
	impPlanApprovalActivityMessage.setUser(DataWareHouse.getUser());
	assertNotNull(impPlanApprovalActivityMessage.getUser());
	impPlanApprovalActivityMessage.processMessage();
    }

    @Test
    public void testSetArguments() throws Throwable {

	IBeans[] beans = null;
	ImpPlanApprovalActivityMessage instance = new ImpPlanApprovalActivityMessage(null, null);
	instance.setArguments(beans);

    }
}

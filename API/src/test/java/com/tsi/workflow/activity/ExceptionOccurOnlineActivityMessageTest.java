package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.Test;
import org.mockito.Mock;

public class ExceptionOccurOnlineActivityMessageTest {

    @Mock
    ImpPlan impPlan;

    @Mock
    Implementation implementation;

    @Test
    public void test00() throws Throwable {
	ExceptionOccurOnlineActivityMessage exceptionOccurOnlineActivityMessage = new ExceptionOccurOnlineActivityMessage(DataWareHouse.getPlan(), implementation);
	exceptionOccurOnlineActivityMessage.setUser(DataWareHouse.getUser());
	assertNotNull(exceptionOccurOnlineActivityMessage.getUser());
	exceptionOccurOnlineActivityMessage.processMessage();
    }

    @Test
    public void testSetArguments() throws Throwable {

	IBeans[] beans = null;
	ExceptionOccurOnlineActivityMessage instance = new ExceptionOccurOnlineActivityMessage(null, null);
	instance.setArguments(beans);

    }
}

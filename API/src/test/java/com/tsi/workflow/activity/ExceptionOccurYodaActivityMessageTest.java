package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mock;

public class ExceptionOccurYodaActivityMessageTest {

    @Mock
    ImpPlan impPlan;

    @Mock
    Implementation implementation;

    @Test
    public void test() throws Throwable {
	ExceptionOccurYodaActivityMessage exceptionOccurYodaActivityMessage = new ExceptionOccurYodaActivityMessage(impPlan, implementation);
	exceptionOccurYodaActivityMessage.setLoadSetName("G1000T10");
	assertEquals("G1000T10", exceptionOccurYodaActivityMessage.getLoadSetName());
	exceptionOccurYodaActivityMessage.setPlanId("T1900111");
	assertEquals("T1900111", exceptionOccurYodaActivityMessage.getPlanId());
	List<String> list = new ArrayList<String>();
	list.add("A");
	list.add("B");
	exceptionOccurYodaActivityMessage.setVparList(list);
	assertNotNull(exceptionOccurYodaActivityMessage.getVparList());
	exceptionOccurYodaActivityMessage.setUser(DataWareHouse.getUser());
	exceptionOccurYodaActivityMessage.processMessage();

    }

    @Test
    public void testSetArguments() throws Throwable {

	IBeans[] beans = null;
	ExceptionOccurOnlineActivityMessage instance = new ExceptionOccurOnlineActivityMessage(null, null);
	instance.setArguments(beans);

    }

}

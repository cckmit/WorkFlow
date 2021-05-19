package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.SystemLoad;
import org.apache.log4j.Priority;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DSLFilePopulateActivityMsgTest {

    @Mock
    ImpPlan impPlan;

    @Mock
    Implementation implementation;

    @Mock
    SystemLoad systemLoad;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

    }

    // @Test
    public void test00() throws Throwable {
	DSLFilePopulateActivityMsg dSLFilePopulateActivityMsg = new DSLFilePopulateActivityMsg(impPlan, implementation);
	dSLFilePopulateActivityMsg.setUser(DataWareHouse.getUser());
	dSLFilePopulateActivityMsg.setStatus(Boolean.FALSE);
	dSLFilePopulateActivityMsg.processMessage();
	Priority expResult = Priority.INFO;
	Priority result = dSLFilePopulateActivityMsg.getLogLevel();
	assertNotNull(result);

    }

    // @Test
    // public void testSetArguments() throws Throwable {
    //
    // CommonActivityMessage instance = new CommonActivityMessage(null, null);
    // IBeans beans =null;
    // systemLoad = (SystemLoad) beans;
    // instance.setArguments(systemLoad);
    // }

}

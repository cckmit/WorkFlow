package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommonActivityMessageTest {

    @Mock
    ImpPlan impPlan;

    @Mock
    Implementation implementation;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test() throws Throwable {
	CommonActivityMessage commonActivityMessage = new CommonActivityMessage(impPlan, implementation);
	commonActivityMessage.setMessage("message");
	assertEquals("message", commonActivityMessage.getMessage());
	commonActivityMessage.setStatus("ACTIVE");
	assertEquals("ACTIVE", commonActivityMessage.getStatus());
	commonActivityMessage.processMessage();
	Priority expResult = Priority.INFO;
	Priority result = commonActivityMessage.getLogLevel();
	assertNotNull(result);

    }

    @Test
    public void testSetArguments() throws Throwable {

	IBeans[] beans = null;
	CommonActivityMessage instance = new CommonActivityMessage(null, null);
	instance.setArguments(beans);

    }

}

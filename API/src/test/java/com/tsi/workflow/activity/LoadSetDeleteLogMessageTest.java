package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.Test;
import org.mockito.Mock;

public class LoadSetDeleteLogMessageTest {

    @Mock
    Implementation implementation;

    @Test
    public void test() throws Throwable {
	LoadSetDeleteLogMessage loadSetDeleteLogMessage = new LoadSetDeleteLogMessage(DataWareHouse.getPlan(), implementation);
	loadSetDeleteLogMessage.setUser(DataWareHouse.getUser());
	assertNotNull(loadSetDeleteLogMessage.getUser());
	loadSetDeleteLogMessage.processMessage();
    }

    @Test
    public void testSetArguments() throws Throwable {

	IBeans[] beans = null;
	LoadSetDeleteLogMessage instance = new LoadSetDeleteLogMessage(null, null);
	instance.setArguments(beans);

    }

}

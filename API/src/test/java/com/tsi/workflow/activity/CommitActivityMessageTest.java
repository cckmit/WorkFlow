package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommitActivityMessageTest {

    @Mock
    ImpPlan impPlan;

    @Mock
    Implementation implementation;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test00() throws Throwable {
	CommitActivityMessage commitActivityMessage = new CommitActivityMessage(impPlan, implementation);
	commitActivityMessage.setComments("comments");
	commitActivityMessage.setCommit(Boolean.FALSE);
	commitActivityMessage.setUser(DataWareHouse.getUser());
	commitActivityMessage.processMessage();
	Priority expResult = Priority.INFO;
	Priority result = commitActivityMessage.getLogLevel();
	assertNotNull(result);

    }

    @Test
    public void testSetArguments() throws Throwable {

	IBeans[] beans = null;
	CommitActivityMessage instance = new CommitActivityMessage(null, null);
	instance.setArguments(beans);

    }

}

package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class QAFunTesterAssignReAssignActivityMessageTest {

    private QAFunTesterAssignReAssignActivityMessage qAFunTesterAssignReAssignActivityMessage;

    @Before
    public void setUp() throws Exception {
	qAFunTesterAssignReAssignActivityMessage = new QAFunTesterAssignReAssignActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testQAFunTesterAssignReAssignActivityMessage() {
	qAFunTesterAssignReAssignActivityMessage.setAction("added");
	assertEquals("added", qAFunTesterAssignReAssignActivityMessage.getAction());
	qAFunTesterAssignReAssignActivityMessage.setQaFunTesterList(getTesterList());
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	qAFunTesterAssignReAssignActivityMessage.setUser(user);
	assertNotNull(qAFunTesterAssignReAssignActivityMessage.getQaFunTesterList());
	assertNotNull(qAFunTesterAssignReAssignActivityMessage.getLogLevel());
	qAFunTesterAssignReAssignActivityMessage.setArguments(Mockito.any(IBeans.class));
	qAFunTesterAssignReAssignActivityMessage.processMessage();

    }

    private List<String> getTesterList() {
	List<String> list = new ArrayList<>();
	list.add(DataWareHouse.getPlan().getApproverName());
	return list;
    }

}

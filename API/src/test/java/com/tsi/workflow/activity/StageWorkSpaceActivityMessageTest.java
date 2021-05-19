package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StageWorkSpaceActivityMessageTest {

    private StageWorkSpaceActivityMessage stageWorkSpaceActivityMessage;

    @Before
    public void setUp() throws Exception {
	stageWorkSpaceActivityMessage = new StageWorkSpaceActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStageWorkSpaceActivityMessage() {
	stageWorkSpaceActivityMessage.setComment("Comment");
	stageWorkSpaceActivityMessage.setErrorMessage("Error Msg");
	assertEquals("Error Msg", stageWorkSpaceActivityMessage.getErrorMessage());
	stageWorkSpaceActivityMessage.setStatus("failed");
	assertEquals("failed", stageWorkSpaceActivityMessage.getStatus());
	stageWorkSpaceActivityMessage.setUser(DataWareHouse.getUser());
	assertNotNull(stageWorkSpaceActivityMessage.getLogLevel());
	// Status=failed,System is null,Delegated User is null,Comment is not null
	stageWorkSpaceActivityMessage.processMessage();
	// Status=initiated,System is not null,Delegated User is null,Comment is not
	// null
	stageWorkSpaceActivityMessage.setStatus("initiated");
	stageWorkSpaceActivityMessage.system = DataWareHouse.getSystemList().get(0);
	stageWorkSpaceActivityMessage.processMessage();

	// Status=initiated,System is not null,Delegated User is null,Comment is null
	stageWorkSpaceActivityMessage.setComment("");
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	stageWorkSpaceActivityMessage.setUser(user);
	stageWorkSpaceActivityMessage.processMessage();

	// Status=initiated,System is not null,Delegated User is null,Comment is null
	stageWorkSpaceActivityMessage.setStatus("online");
	stageWorkSpaceActivityMessage.processMessage();

	// Status=initiated,System is null,Delegated User is null,Comment is null
	stageWorkSpaceActivityMessage.system = null;
	stageWorkSpaceActivityMessage.processMessage();

	assertNotNull(stageWorkSpaceActivityMessage.getLogLevel());

	StageWorkSpaceActivityMessage stageWorkSpaceActivityMessage1 = new StageWorkSpaceActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), Mockito.any(IBeans.class));

    }

}

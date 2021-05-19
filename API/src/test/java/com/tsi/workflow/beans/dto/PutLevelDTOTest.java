package com.tsi.workflow.beans.dto;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.PutLevel;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PutLevelDTOTest {

    private PutLevelDTO putLevelDTO;

    @Before
    public void setUp() throws Exception {

	putLevelDTO = new PutLevelDTO(Mockito.any(PutLevel.class), DataWareHouse.getUser().getId(), DataWareHouse.user.getCurrentRole());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPutLevelDTO() {
	putLevelDTO.setCanDelete(true);
	assertTrue(putLevelDTO.getCanDelete());
	putLevelDTO.setCanUpdateOthers(true);
	assertTrue(putLevelDTO.getCanUpdateOthers());
	putLevelDTO.setCanUpdateStatus(true);
	assertTrue(putLevelDTO.getCanUpdateStatus());
	putLevelDTO.setOptions(getOptionsList());
	assertNotNull(putLevelDTO.getOptions());
	PutLevel putLevel = new PutLevel();
	putLevelDTO.setPutLevel(putLevel);
	assertNotNull(putLevelDTO.getPutLevel());
	putLevelDTO.setRole("LoadsControl");
	assertEquals("LoadsControl", putLevelDTO.getRole());
	putLevelDTO.setUserId(DataWareHouse.getUser().getId());
	assertEquals(DataWareHouse.getUser().getId(), putLevelDTO.getUserId());
	// putLevelStatus = Null
	putLevelDTO.populate();

	// putLevelStatus = DEVELOPMENT
	putLevel.setStatus("DEVELOPMENT");
	putLevelDTO.populate();

	// putLevelStatus = PRE_PROD_CO_EXIST, Role =LoadsControl
	putLevel.setStatus("PRE_PROD_CO_EXIST");
	putLevelDTO.populate();

	// putLevelStatus = LOCKDOWN, Role =LoadsControl
	putLevel.setStatus("LOCKDOWN");
	putLevelDTO.populate();

	// putLevelStatus = PROD_CO_EXIST, Role =LoadsControl
	putLevel.setStatus("PROD_CO_EXIST");
	putLevelDTO.populate();

	// putLevelStatus = BACKUP, Role =LoadsControl
	putLevel.setStatus("BACKUP");
	putLevelDTO.populate();

	// putLevelStatus = PRODUCTION, Role =LoadsControl
	putLevel.setStatus("PRODUCTION");
	putLevelDTO.populate();

	// putLevelStatus = INACTIVE, Role =LoadsControl
	putLevel.setStatus("INACTIVE");
	putLevelDTO.populate();

	// putLevelStatus = INACTIVE,, Role =LoadsControl
	putLevel.setOwnerids(putLevelDTO.getUserId());
	putLevel.setStatus("INACTIVE");
	putLevelDTO.populate();

	// putLevelStatus = LOCKDOWN, Role =TSD
	putLevelDTO.setRole("TSD");
	putLevel.setStatus("LOCKDOWN");
	putLevelDTO.populate();

	// putLevelStatus = PROD_CO_EXIST, Role =TSD
	putLevel.setStatus("PROD_CO_EXIST");
	putLevelDTO.populate();
	// putLevelStatus = PRODUCTION, Role =TSD
	putLevel.setStatus("PRODUCTION");
	putLevelDTO.populate();
	// putLevelStatus = BACKUP, Role =TSD
	putLevel.setOwnerids("ABCD");
	putLevel.setStatus("BACKUP");
	putLevelDTO.populate();
    }

    private List<String> getOptionsList() {
	List<String> list = new ArrayList<>();
	list.add("Option");
	return list;
    }

}

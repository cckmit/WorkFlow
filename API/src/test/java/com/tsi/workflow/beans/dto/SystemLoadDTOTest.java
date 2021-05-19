package com.tsi.workflow.beans.dto;

import static org.junit.Assert.*;

import com.tsi.workflow.beans.dao.SystemLoad;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemLoadDTOTest {

    private SystemLoadDTO systemLoadDTO;

    @Before
    public void setUp() throws Exception {
	systemLoadDTO = new SystemLoadDTO();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSystemLoadDTO() {
	systemLoadDTO.setAllowPutLevelChange(true);
	assertTrue(systemLoadDTO.getAllowPutLevelChange());
	systemLoadDTO.setSystemLoad(new SystemLoad());
	assertNotNull(systemLoadDTO.getSystemLoad());
    }

}

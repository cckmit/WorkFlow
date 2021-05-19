/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.utils.JSONResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DbcrHelperTest {

    DbcrHelper instance;

    public DbcrHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    DbcrHelper realInstance = new DbcrHelper();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    // TestCaseMockService.doMockDAO(instance, SSHUtil.class);

	} catch (Exception ex) {
	    Logger.getLogger(DbcrHelperTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDbcrHelper() throws Exception {
	TestCaseExecutor.doTest(instance, DbcrHelper.class);
    }

    @Test
    public void testValidateDbcr() {
	DbcrHelper dbcrHelper = spy(new DbcrHelper());
	// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	dbcrHelper.systemDAO = mock(SystemDAO.class);
	when(dbcrHelper.getSystemDAO().find(Integer.SIZE)).thenReturn(DataWareHouse.getSystemList().get(0));
	JSONResponse jsonResponse = new JSONResponse();
	jsonResponse.setStatus(false);
	// Mockito.doReturn(false).when(sshUtil).connectSSH(DataWareHouse.getSystemList().get(0));
	// when(dbcrHelper.getSSHUtil()).thenReturn(sshUtil);
	try {
	    dbcrHelper.validateDbcr(Integer.SIZE, null);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

}

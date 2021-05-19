/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.SystemDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class FTPHelperTest {

    public FTPHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getSystemDAO method, of class FTPHelper.
     */
    @Test
    public void testGetSystemDAO() {

	FTPHelper instance = new FTPHelper();
	SystemDAO expResult = null;
	SystemDAO result = instance.getSystemDAO();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGITConfig method, of class FTPHelper.
     */
    @Test
    public void testGetGITConfig() {

	FTPHelper instance = new FTPHelper();
	GITConfig expResult = null;
	GITConfig result = instance.getGITConfig();
	assertEquals(expResult, result);
    }

    /**
     * Test of doFTP method, of class FTPHelper.
     */
    @Test
    public void testDoFTP() {

	User lUser = null;
	SystemLoad load = DataWareHouse.getPlan().getSystemLoadList().get(0);
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	Build lBuild1 = DataWareHouse.getPlan().getBuildList().get(1);
	String ipAddress = "";
	Boolean isfallback = false;
	FTPHelper realinstance = new FTPHelper();
	instance = spy(realinstance);
	Boolean expResult = null;
	SystemDAO systemDAO = mock(SystemDAO.class);
	ReflectionTestUtils.setField(instance, "systemDAO", systemDAO);
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	when(systemDAO.find(load.getSystemId().getId())).thenReturn(load.getSystemId());
	// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	try {
	    instance.doFTP(lUser, load, lBuild, ipAddress, isfallback);
	    // doReturn(true).when(sshUtil).connectSSH(load.getSystemId());
	    // when(instance.getSSHUtil()).thenReturn(sshUtil);
	    lBuild.setPlanId(DataWareHouse.getPlan());
	    lBuild1.setPlanId(DataWareHouse.getPlan());
	    instance.doFTP(lUser, load, lBuild, ipAddress, isfallback);
	    instance.doFTP(lUser, load, lBuild1, ipAddress, isfallback);
	    isfallback = true;
	    instance.doFTP(lUser, load, lBuild, ipAddress, isfallback);
	} catch (Exception e) {

	}

    }

    FTPHelper instance;

}

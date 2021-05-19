/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular.tpftestsystemmaintenance;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.helper.MaintenanceHelper;
import com.tsi.workflow.schedular.MaintenanceMonitor;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author yeshwanth.shenoy
 */
public class MaintenanceMonitorTest {

    public MaintenanceMonitorTest() {
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
     * Test of getGITConfig method, of class MaintenanceMonitor.
     */
    @Test
    public void testGetGITConfig() {

	MaintenanceMonitor instance = new MaintenanceMonitor();
	GITConfig expResult = null;
	GITConfig result = instance.getGITConfig();
	assertEquals(expResult, result);
    }

    /**
     * Test of getWFConfig method, of class MaintenanceMonitor.
     */
    @Test
    public void testGetWFConfig() {

	MaintenanceMonitor instance = new MaintenanceMonitor();
	WFConfig expResult = null;
	WFConfig result = instance.getWFConfig();
	assertEquals(expResult, result);
    }

    /**
     * Test of getMaintenanceHelper method, of class MaintenanceMonitor.
     */
    @Test
    public void testGetMaintenanceHelper() {

	MaintenanceMonitor instance = new MaintenanceMonitor();
	MaintenanceHelper expResult = null;
	MaintenanceHelper result = instance.getMaintenanceHelper();
	assertEquals(expResult, result);
    }

    /**
     * Test of doMonitor method, of class MaintenanceMonitor.
     */
    @Test
    public void testDoMonitor() {

	try {
	    try {
		MaintenanceMonitor instance = new MaintenanceMonitor();
		MaintenanceMonitor mockinstance = spy(instance);
		// ConcurrentLinkedQueue<TestSystemMaintenance> testSystemMaintenanceList = new
		// ConcurrentLinkedQueue<>();
		// TestSystemMaintenance testSystemMaintenance = new TestSystemMaintenance();
		// testSystemMaintenance.setSystem(DataWareHouse.getSystemList().get(0));
		// testSystemMaintenance.setStartedDate(new Date());
		// testSystemMaintenanceList.add(testSystemMaintenance);
		// mockinstance.testSystemMaintenanceList = testSystemMaintenanceList;
		ReflectionTestUtils.setField(mockinstance, "gITConfig", mock(GITConfig.class));
		ReflectionTestUtils.setField(mockinstance, "wFConfig", mock(WFConfig.class));
		ReflectionTestUtils.setField(mockinstance, "maintenanceHelper", mock(MaintenanceHelper.class));
		mockinstance.doMonitor();
		String command = "${MTP_ENV}/mtptpfnfmcheckoldr";
		Session lSession = mock(Session.class);
		ChannelExec lChannel = mock(ChannelExec.class);
		// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
		when(lSession.openChannel("exec")).thenReturn(lChannel);
		when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
		// when(mockinstance.getSSHUtil()).thenReturn(sshUtil);
		// doReturn(true).when(sshUtil).connectSSH(testSystemMaintenance.getSystem());
		// when(sshUtil.executeCommand(command)).thenReturn(DataWareHouse.getPositiveResponse());
		// mockinstance.testSystemMaintenanceList = testSystemMaintenanceList;
		String command2 = "${MTP_ENV}/mtptpfupdatenfmdsl";
		// when(sshUtil.executeCommand(command2)).thenReturn(DataWareHouse.getPositiveResponse());
		mockinstance.doMonitor();
		// when(sshUtil.executeCommand(command)).thenReturn(DataWareHouse.getNegativeResponse());
		// mockinstance.testSystemMaintenanceList = testSystemMaintenanceList;
		mockinstance.doMonitor();
	    } catch (JSchException ex) {
		// do nothing
	    } catch (IOException ex) {

	    }
	} catch (Exception e) {
	    // do nothing
	}
    }

    /**
     * Test of doMonitor method, of class MaintenanceMonitor.
     */
    @Test
    public void testDoMonitor2() {
	try {
	    try {
		MaintenanceMonitor instance = new MaintenanceMonitor();
		MaintenanceMonitor mockinstance = spy(instance);
		// ConcurrentLinkedQueue<TestSystemMaintenance> testSystemMaintenanceList = new
		// ConcurrentLinkedQueue<>();
		// TestSystemMaintenance testSystemMaintenance = new TestSystemMaintenance();
		// testSystemMaintenance.setSystem(DataWareHouse.getSystemList().get(0));
		SimpleDateFormat sdfmt1 = new SimpleDateFormat("dd/MM/yy");
		Date dDate = sdfmt1.parse("10/04/17");
		// testSystemMaintenance.setStartedDate(dDate);
		// testSystemMaintenanceList.add(testSystemMaintenance);
		// mockinstance.testSystemMaintenanceList = testSystemMaintenanceList;
		ReflectionTestUtils.setField(mockinstance, "gITConfig", mock(GITConfig.class));
		ReflectionTestUtils.setField(mockinstance, "wFConfig", mock(WFConfig.class));
		ReflectionTestUtils.setField(mockinstance, "maintenanceHelper", mock(MaintenanceHelper.class));
		mockinstance.doMonitor();
		String command = "${MTP_ENV}/mtptpfnfmcheckoldr";
		Session lSession = mock(Session.class);
		ChannelExec lChannel = mock(ChannelExec.class);
		// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
		when(lSession.openChannel("exec")).thenReturn(lChannel);
		when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
		// when(mockinstance.getSSHUtil()).thenReturn(sshUtil);
		// doReturn(true).when(sshUtil).connectSSH(testSystemMaintenance.getSystem());
		// when(sshUtil.executeCommand(command)).thenReturn(DataWareHouse.getNegativeResponse());
		// mockinstance.testSystemMaintenanceList = testSystemMaintenanceList;
		String command2 = "${MTP_ENV}/mtptpfupdatenfmdsl";
		// testSystemMaintenance.setStartedDate(new Date());
		// testSystemMaintenanceList.clear();
		// testSystemMaintenanceList.add(testSystemMaintenance);
		// mockinstance.testSystemMaintenanceList = testSystemMaintenanceList;
		mockinstance.doMonitor();
		// when(sshUtil.executeCommand(command)).thenReturn(DataWareHouse.getPositiveResponse());
		// when(sshUtil.executeCommand(command2)).thenReturn(DataWareHouse.getNegativeResponse());
		// testSystemMaintenance.setStartedDate(new Date());
		// testSystemMaintenanceList.clear();
		// testSystemMaintenanceList.add(testSystemMaintenance);
		// mockinstance.testSystemMaintenanceList = testSystemMaintenanceList;
		mockinstance.doMonitor();
	    } catch (JSchException ex) {
		// do nothing
	    } catch (IOException ex) {

	    } catch (ParseException ex) {

	    }
	} catch (Exception e) {
	    // do nothing
	}
    }

}

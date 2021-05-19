/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.mail.TesttSystemMaintFailMail;
import java.util.concurrent.BlockingQueue;
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
public class MaintenanceHelperTest {

    public MaintenanceHelperTest() {
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
     * Test of getGITConfig method, of class MaintenanceHelper.
     */
    // @Test
    // public void testGetGITConfig() {
    // MaintenanceHelper instance = new MaintenanceHelper();
    // GITConfig expResult = null;
    // GITConfig result = instance.getGITConfig();
    // assertEquals(expResult, result);
    // }
    //
    // /**
    // * Test of getMailMessageFactory method, of class MaintenanceHelper.
    // */
    // @Test
    // public void testGetMailMessageFactory() {
    // MaintenanceHelper instance = new MaintenanceHelper();
    // MailMessageFactory expResult = null;
    // MailMessageFactory result = instance.getMailMessageFactory();
    // assertEquals(expResult, result);
    // }
    //
    // /**
    // * Test of isMaintenanceActive method, of class MaintenanceHelper.
    // */
    // @Test
    // public void testIsMaintenanceActive() {
    // try {
    // System system = DataWareHouse.getSystemList().get(0);
    // MaintenanceHelper instance = spy(new MaintenanceHelper());
    // // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
    // // doReturn(true).when(sshUtil).connectSSH(system);
    // // when(instance.getSSHUtil()).thenReturn(sshUtil);
    // Mockito.when(instance.isMaintenanceActive(system)).thenReturn(true);
    // // doReturn(false).when(sshUtil).connectSSH(system);
    // Mockito.when(instance.isMaintenanceActive(system)).thenReturn(false);
    // } catch (Exception e) {
    // // do nothing
    // }
    // }
    //
    // @Test
    // public void testIsMaintenanceActive1() {
    // try {
    // System system = DataWareHouse.getSystemList().get(0);
    // boolean expResult = true;
    // JSONResponse lResponse = new JSONResponse();
    // lResponse.setStatus(true);
    // MaintenanceHelper instance = spy(new MaintenanceHelper());
    // // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
    // // when(instance.getSSHUtil()).thenReturn(sshUtil);
    // // doReturn(true).when(sshUtil).connectSSH(system);
    // //
    // doReturn(lResponse).when(sshUtil).executeCommand(Constants.SystemScripts.TPF_MAINTENANCE_CHECK.getScript());
    // boolean result = instance.isMaintenanceActive(system);
    // assertEquals(expResult, result);
    // } catch (Exception e) {
    // // do nothing
    // }
    // }

    /**
     * Test of notifyToolAdminOnProcessFails method, of class MaintenanceHelper.
     */
    @Test
    public void testNotifyToolAdminOnProcessFails() {
	String systemName = "";
	MaintenanceHelper instance = new MaintenanceHelper();
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(TesttSystemMaintFailMail.class)).thenReturn(new TesttSystemMaintFailMail());
	instance.notifyToolAdminOnProcessFails(systemName);
    }

}

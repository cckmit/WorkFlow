/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.User;
import com.tsi.workflow.bpm.model.TaskReponse;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.interfaces.IBPMConfig;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BPMClientUtilsTest {

    public BPMClientUtilsTest() {
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
     * Test of createADLProcess method, of class BPMClientUtils.
     */
    @Test
    public void testCreateADLProcess() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("Feb2018*");
	    BPMClientUtils ins = new BPMClientUtils(mock(IBPMConfig.class));
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    ReflectionTestUtils.setField(instance, "bPMConfig", mock(IBPMConfig.class));
	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");

	    String result = instance.createADLProcess(pUser);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of createDeveloperProcess method, of class BPMClientUtils.
     */
    @Test
    public void testCreateDeveloperProcess() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("as");
	    String implementationId = "";
	    String implementationDescription = "";
	    String developerName = "";
	    String devMailId = "";
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");
	    String expResult = "";

	    String result = instance.createDeveloperProcess(pUser, implementationId);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of removeUserFromTask method, of class BPMClientUtils.
     */
    @Test
    public void testRemoveUserFromTask() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("as");

	    String processId = "";
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    Boolean expResult = null;

	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");

	    Boolean result = instance.removeUserFromTask(pUser, processId);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of assignTask method, of class BPMClientUtils.
     */
    @Test
    public void testAssignTask() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("as");
	    String pProcessId = "";
	    String pAssigneeId = "";
	    List<TaskVariable> lVars = null;
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");
	    Boolean expResult = null;
	    Boolean result = instance.assignTask(pUser, pProcessId, pAssigneeId, lVars);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    //

    /**
     * Test of getTaskList method, of class BPMClientUtils.
     */
    @Test
    public void testGetTaskList() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("as");
	    String pTaskForUserId = "";
	    String pTaskType = "";
	    Integer offset = null;
	    Integer limit = null;
	    String sortKey = "";
	    String sortType = "";
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");
	    TaskReponse expResult = null;
	    TaskReponse result = instance.getTaskList(pUser, pTaskForUserId, pTaskType, offset, limit, sortKey, sortType);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of setTaskAsCompleted method, of class BPMClientUtils.
     */
    @Test
    public void testSetTaskAsCompleted() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("as");
	    String processId = "";
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");
	    Boolean expResult = null;
	    Boolean result = instance.setTaskAsCompleted(pUser, processId);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of setTaskAsCompletedWithVariables method, of class BPMClientUtils.
     */
    @Test
    public void testSetTaskAsCompletedWithVariables() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("as");
	    String processId = "";
	    List<TaskVariable> taskVars = null;
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");
	    Boolean expResult = null;
	    Boolean result = instance.setTaskAsCompletedWithVariables(pUser, processId, taskVars);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of assignTaskToGroup method, of class BPMClientUtils.
     */
    @Test
    public void testAssignTaskToGroup() {
	try {
	    User pUser = new User();
	    pUser.setId("deepa.jayakumar");
	    pUser.setPassword("as");
	    String processId = "";
	    String pGroupName = "";
	    List<TaskVariable> pTaskVariables = null;
	    BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
	    when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");
	    Boolean expResult = null;
	    Boolean result = instance.assignTaskToGroup(pUser, processId, pGroupName, pTaskVariables);
	} catch (Exception ex) {
	    Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of getTaskListByGroup method, of class BPMClientUtils.
     */
    // @Test
    // public void testGetTaskListByGroup() {
    // try {
    // System.out.println("getTaskListByGroup");
    // User pUser = new User();
    // pUser.setId("prabhu.prabhakaran");
    // pUser.setEncryptedPassword("TSAmAy2MAEFL0/3mINNmpsHzIf5uy0TE");
    // String pTaskType = "";
    // String pGroupName = "";
    // BPMClientUtils instance = spy(new BPMClientUtils(mock(IBPMConfig.class)));
    // ReflectionTestUtils.setField(instance, "bPMConfig", mock(IBPMConfig.class));
    // when(instance.bPMConfig.getBpmRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/activiti-rest/");
    // List<String> expResult = null;
    // List<String> result = instance.getTaskListByGroup(pUser, pTaskType,
    // pGroupName);
    // } catch (Exception ex) {
    // Logger.getLogger(BPMClientUtilsTest.class.getName()).log(Level.SEVERE, null,
    // ex);
    // }
    // }

}

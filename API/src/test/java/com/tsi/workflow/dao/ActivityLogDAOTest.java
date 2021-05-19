/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.beans.dao.ActivityLog;
import com.tsi.workflow.beans.dao.ImpPlan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class ActivityLogDAOTest {

    ActivityLogDAO instance;
    private SessionFactory sessionFactory;
    private Session session;

    public ActivityLogDAOTest() {
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
	    ActivityLogDAO realInstance = new ActivityLogDAO();
	    instance = spy(realInstance);
	    sessionFactory = mock(SessionFactory.class);
	    session = mock(Session.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    TestCaseMockService.doMockBaseDAO(ActivityLogDAO.class, ActivityLog.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(ImplementationDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperManagerService() throws Exception {
	TestCaseExecutor.doTestDAO(instance, ActivityLogDAO.class);
    }

    @Test
    public void testSave() {
	ActivityLogDAO activityLogDAO = new ActivityLogDAO();
	ActivityLogMessage pActivityLogMessage = mock(ActivityLogMessage.class);
	pActivityLogMessage.setUser(DataWareHouse.getUser());
	ActivityLog activityLog = new ActivityLog();
	User pUser = new User();
	pActivityLogMessage.setUser(pUser);
	try {
	    when(pActivityLogMessage.getActivityLog()).thenReturn(activityLog);
	    activityLogDAO.save(pUser, pActivityLogMessage);
	} catch (Exception e) {
	}

    }

    @Test
    public void testFindByPlanId() {
	ActivityLogDAO activityLogDAO = new ActivityLogDAO();
	List<ImpPlan> list = new ArrayList<>();
	try {
	    activityLogDAO.findByPlanId(list);
	} catch (Exception e) {
	}
    }

    @Test
    public void testfindBySystem() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(ActivityLogDAO.class);
	Mockito.when(session.createCriteria(ActivityLogDAO.class)).thenReturn(mockedCriteria);
	instance.findByPlanId(Arrays.asList(DataWareHouse.getPlan()));
    }

    @Test
    public void testSaveLog() {
	try {
	    ActivityLogDAO activityLogDAO = new ActivityLogDAO();
	    ActivityLogMessage pActivityLogMessage = mock(ActivityLogMessage.class);
	    User pUser = mock(User.class);
	    pActivityLogMessage.setUser(DataWareHouse.getUser());
	    ActivityLog activityLog = new ActivityLog();
	    activityLog.setPlanId(DataWareHouse.getPlan());
	    activityLog.setLogLevel("INFO");
	    activityLog.setId(1);
	    pActivityLogMessage.setUser(pUser);
	    activityLogDAO.save(pUser, pActivityLogMessage);
	} catch (Exception e) {

	}

    }
}

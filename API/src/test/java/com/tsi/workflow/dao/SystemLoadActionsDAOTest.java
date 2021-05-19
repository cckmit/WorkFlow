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
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class SystemLoadActionsDAOTest {

    SystemLoadActionsDAO instance;

    public SystemLoadActionsDAOTest() {
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
	    SystemLoadActionsDAO realInstance = new SystemLoadActionsDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(SystemLoadActionsDAO.class, SystemLoadActions.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, SystemLoadActionsDAO.class);
    }

    @Test
    public void testFindByPlanId() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(SystemLoadActionsDAO.class);
	when(session.createCriteria(SystemLoadActionsDAO.class)).thenReturn(mockedCriteria);
	instance.findByPlanId(Arrays.asList(DataWareHouse.getPlan()));
    }

    @Test
    public void testFindByPlanByUser() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);

	String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction, Vpars pars" + " Where loadAction.vparId=pars.id " + " AND loadAction.active = 'Y'" + " AND pars.active='Y'" + " AND loadAction.planId.id in (:Planids)" + " AND ((pars.type ='INTEGRATION') or (pars.type='PRIVATE' AND pars.ownerId = :ownerId))";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);

	List<Constants.VPARSEnvironment> pVpars = new ArrayList();
	pVpars.add(Constants.VPARSEnvironment.INTEGRATION);
	pVpars.add(Constants.VPARSEnvironment.PRIVATE);
	List<String> vparsType = pVpars.stream().map(Constants.VPARSEnvironment::name).collect(Collectors.toList());
	when(mockedQry.setParameterList("Planids", (String[]) Arrays.asList(DataWareHouse.getPlan().getId()).toArray())).thenReturn(mockedQry);
	when(mockedQry.setParameter("ownerId", DataWareHouse.getUser().getId())).thenReturn(mockedQry);
	User lUser = DataWareHouse.getUser();
	lUser.setCurrentRole(Constants.UserGroup.Lead.name());

	instance.findByPlan((String[]) Arrays.asList(DataWareHouse.getPlan().getId()).toArray(), pVpars, lUser);
    }

    @Test
    public void testFindByPlan() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);

	String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction" + " Where loadAction.active = 'Y'" + " AND loadAction.planId.id in (:Planids)" + " AND loadAction.vparId.type in :VparsType";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);

	List<Constants.VPARSEnvironment> pVpars = new ArrayList();
	pVpars.add(Constants.VPARSEnvironment.INTEGRATION);
	List<String> vparsType = pVpars.stream().map(Constants.VPARSEnvironment::name).collect(Collectors.toList());
	when(mockedQry.setParameterList("Planids", (String[]) Arrays.asList(DataWareHouse.getPlan().getId()).toArray())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("VparsType", vparsType)).thenReturn(mockedQry);
	User lUser = DataWareHouse.getUser();
	lUser.setCurrentRole(Constants.UserGroup.QA.name());
	instance.findByPlan((String[]) Arrays.asList(DataWareHouse.getPlan().getId()).toArray(), pVpars, lUser);
    }

    @Test
    public void testfindBySystemLoadEnv() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction" + " Where loadAction.active = 'Y'" + " AND loadAction.systemLoadId = :pSystemLoad" + " AND loadAction.vparId.type = :VparsType";

	    List<Constants.VPARSEnvironment> pVpars = new ArrayList();
	    pVpars.add(Constants.VPARSEnvironment.PRE_PROD);
	    List<String> vparsType = pVpars.stream().map(Constants.VPARSEnvironment::name).collect(Collectors.toList());
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    SystemLoad lSystemLoad = new SystemLoad();
	    lSystemLoad.setId(1);
	    when(mockedQry.setParameter("pSystemLoad", lSystemLoad.getId())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("VparsType", pVpars.get(0))).thenReturn(mockedQry);
	    instance.findBySystemLoadEnv(lSystemLoad, pVpars.get(0));
	} catch (Exception e) {

	}
    }

    @Test
    public void findByPlanAndVpars() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = " SELECT action FROM systemLoadActions action, SystemLoad sysLoad " + " WHERE action.planId = :PlanId " + " AND action.vparId in (:VparsId) " + " AND action.active = 'Y' " + " AND action.systemLoadId = sysLoad.id " + " AND sysLoad.active = 'Y' " + " AND sysLoad.qaTestingStatus = 'NONE' ";
	ImpPlan lImplan = DataWareHouse.getPlan();
	Integer[] linteger = new Integer[] { 1, 2, 3 };
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", lImplan.getId())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("VparsId", linteger)).thenReturn(mockedQry);
	instance.findByPlanAndVpars(lImplan.getId(), linteger);
    }
}

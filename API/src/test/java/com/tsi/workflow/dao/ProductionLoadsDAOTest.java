/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.utils.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
public class ProductionLoadsDAOTest {

    ProductionLoadsDAO instance;

    public ProductionLoadsDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() {
	try {
	    ProductionLoadsDAO realInstance = new ProductionLoadsDAO();
	    instance = spy(realInstance);
	    sessionFactory = mock(SessionFactory.class);
	    session = mock(Session.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    TestCaseMockService.doMockBaseDAO(ProductionLoadsDAO.class, ProductionLoads.class, instance);
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
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	TestCaseExecutor.doTestDAO(instance, ProductionLoadsDAO.class);
    }

    @Test
    public void testFindTobeLoaded() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT loads FROM ProductionLoads loads, SystemLoad sysload" + " WHERE loads.planId.planStatus LIKE ?" + " AND loads.planId.acceptedDateTime IS NULL" + " AND loads.planId.fallbackDateTime IS NULL" + " AND loads.cpuId IS NULL" + " AND (" + "     (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'INPROGRESS')" + "     OR (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'FAILED')" + " )" + " AND loads.active = 'Y'" + " AND sysload.id = loads.systemLoadId.id"
		+ " AND sysload.active = 'Y'" + " AND (" + "     (sysload.prodLoadStatus LIKE ?)" + "     OR (sysload.prodLoadStatus LIKE ?)" + ")";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter(0, Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(1, Constants.LOAD_SET_STATUS.ACTIVATED.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(2, Constants.LOAD_SET_STATUS.ACCEPTED.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(3, Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(4, Constants.PROD_LOAD_STATUS.ACCEPTED.name())).thenReturn(mockedQry);
	instance.findTobeLoaded(new HashMap());
    }

    @Test
    public void testfindTobeFallbackLoaded() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT loads FROM ProductionLoads loads, SystemLoad sysload" + " WHERE loads.planId.planStatus LIKE ?" + " AND loads.planId.acceptedDateTime IS NOT NULL" + " AND loads.planId.fallbackDateTime IS NULL" + " AND loads.cpuId IS NULL" + " AND (" + "     (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'INPROGRESS')" + "     OR (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'FAILED')" + " )" + " AND loads.active = 'Y'"
		+ " AND sysload.id = loads.systemLoadId.id" + " AND sysload.active = 'Y'" + " AND (" + "     (sysload.prodLoadStatus LIKE ?)" + "     OR (sysload.prodLoadStatus LIKE ?)" + ")";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter(0, Constants.PlanStatus.ONLINE.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(1, Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(2, Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(3, Constants.PROD_LOAD_STATUS.FALLBACK_ACTIVATED.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(4, Constants.PROD_LOAD_STATUS.FALLBACK_ACCEPTED.name())).thenReturn(mockedQry);
	instance.findTobeFallbackLoaded(new HashMap());
    }

    @Test
    public void testfindTobeFallbackLoaded1() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT loads FROM ProductionLoads loads, SystemLoad sysload" + " WHERE loads.planId.planStatus LIKE ?" + " AND loads.planId.acceptedDateTime IS NOT NULL" + " AND loads.planId.fallbackDateTime IS NULL" + " AND loads.cpuId IS NULL" + " AND (" + "     (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'INPROGRESS')" + "     OR (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'FAILED')" + " )" + " AND loads.active = 'Y'"
		    + " AND sysload.id = loads.systemLoadId.id" + " AND sysload.active = 'Y'" + " AND (" + "     (sysload.prodLoadStatus LIKE ?)" + "     OR (sysload.prodLoadStatus LIKE ?)" + ")";

	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter(0, Constants.PlanStatus.ONLINE.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(1, Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(2, Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(3, Constants.PROD_LOAD_STATUS.FALLBACK_ACTIVATED.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(4, Constants.PROD_LOAD_STATUS.FALLBACK_ACCEPTED.name())).thenReturn(mockedQry);
	    Map<String, String> orderBy = new HashMap<String, String>();
	    orderBy.put("id", "asc");
	    instance.findTobeFallbackLoaded(orderBy);
	} catch (Exception e) {

	}
    }

    @Test
    public void testFindTobeLoaded1() throws Exception {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT loads FROM ProductionLoads loads, SystemLoad sysload" + " WHERE loads.planId.planStatus LIKE ?" + " AND loads.planId.acceptedDateTime IS NULL" + " AND loads.planId.fallbackDateTime IS NULL" + " AND loads.cpuId IS NULL" + " AND (" + "     (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'INPROGRESS')" + "     OR (loads.status LIKE ? AND loads.lastActionStatus NOT LIKE 'FAILED')" + " )" + " AND loads.active = 'Y'"
		    + " AND sysload.id = loads.systemLoadId.id" + " AND sysload.active = 'Y'" + " AND (" + "     (sysload.prodLoadStatus LIKE ?)" + "     OR (sysload.prodLoadStatus LIKE ?)" + ")";

	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter(0, Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(1, Constants.LOAD_SET_STATUS.ACTIVATED.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(2, Constants.LOAD_SET_STATUS.ACCEPTED.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(3, Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter(4, Constants.PROD_LOAD_STATUS.ACCEPTED.name())).thenReturn(mockedQry);
	    Map<String, String> orderBy = new HashMap<String, String>();
	    orderBy.put("id", "asc");
	    instance.findTobeLoaded(orderBy);
	} catch (Exception e) {

	}
    }

    @Test
    public void testFindByPlanId() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.systemId = :systemId" + " AND prodLoad.cpuId IS NULL";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", DataWareHouse.getPlan())).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", DataWareHouse.getSystemList().get(0))).thenReturn(mockedQry);
	instance.findByPlanId(DataWareHouse.getPlan(), DataWareHouse.getSystemList().get(0));
    }

    @Test
    public void testFindAllLastSuccessfulBuild() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId.id = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.lastActionStatus LIKE 'SUCCESS'" + " ORDER BY prodLoad.id DESC";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	instance.findAllLastSuccessfulBuild(DataWareHouse.getPlan().getId());
    }

    @Test
    public void testGetLoadsCountByLoadStatus() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT prodLoad.status, COUNT(*) FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId.id = :PlanId" + " AND prodLoad.systemId.id = :SystemId" + " AND prodLoad.cpuId is not NULL" + " AND prodLoad.active LIKE 'Y' GROUP BY prodLoad.status";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 0)).thenReturn(mockedQry);
	instance.getLoadsCountByLoadStatus(DataWareHouse.getPlan().getId(), 0);
    }

    @Test
    public void testGetAllLoadsCountByLoadStatus() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT prodLoad.status, COUNT(*) FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId.id = :PlanId" + " AND prodLoad.systemId.id = :SystemId" + " AND prodLoad.cpuId is NULL" + " AND prodLoad.active LIKE 'Y' GROUP BY prodLoad.status";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 0)).thenReturn(mockedQry);
	instance.getAllLoadsCountByLoadStatus(DataWareHouse.getPlan().getId(), 0);
    }

    @Test
    public void testUpdateAllActivated() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	String lQuery = "UPDATE production_loads SET status = ? " + " WHERE plan_id = ?" + " AND system_load_id = ?" + " AND system_id = ?" + " AND cpu_id IS NOT NULL" + " AND active = 'Y'";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter(0, Constants.LOAD_SET_STATUS.ACTIVATED.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(1, DataWareHouse.getProductionLoads().getPlanId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter(2, DataWareHouse.getProductionLoads().getSystemLoadId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter(3, DataWareHouse.getProductionLoads().getSystemId().getId())).thenReturn(mockedQry);
	instance.updateAllActivated(DataWareHouse.user, DataWareHouse.getProductionLoads());
    }

    @Test
    public void testUpdateAllDeActivated() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	String lQuery = "UPDATE production_loads SET status = ? " + " WHERE plan_id = ?" + " AND system_load_id = ?" + " AND system_id = ?" + " AND cpu_id IS NOT NULL" + " AND active = 'Y'";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter(0, Constants.LOAD_SET_STATUS.DEACTIVATED.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(1, DataWareHouse.getProductionLoads().getPlanId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter(2, DataWareHouse.getProductionLoads().getSystemLoadId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter(3, DataWareHouse.getProductionLoads().getSystemId().getId())).thenReturn(mockedQry);
	instance.updateAllDeActivated(DataWareHouse.user, DataWareHouse.getProductionLoads());
    }

    @Test
    public void testfindByLoadset() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad, SystemLoad load" + " WHERE prodLoad.systemLoadId = load.id" + " AND prodLoad.active LIKE 'Y'" + " AND load.active LIKE 'Y'" + " AND (load.loadSetName LIKE :loadset" + " OR load.fallbackLoadSetName LIKE :loadset)" + " AND prodLoad.id = :ID";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("ID", 0)).thenReturn(mockedQry);
	when(mockedQry.setParameter("loadset", "")).thenReturn(mockedQry);
	instance.findByLoadset(0, "");
    }

    @Test
    public void testgetAllAccepted() throws Exception {
	try {

	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    SQLQuery mockedQry = mock(SQLQuery.class);
	    String lQuery = "select count(plan.id) from production_loads loads, system_load sysload, imp_plan plan" + " where plan.id like ?" + " and plan.plan_status like 'READY_FOR_PRODUCTION_DEPLOYMENT'" + " and plan.accepted_date_time is null" + " and plan.fallback_date_time is null" + " and loads.cpu_id is null" + " and (" + "     (loads.status like 'ACTIVATED' and loads.last_action_status not like 'INPROGRESS')"
		    + "     or (loads.status like 'ACCEPTED' and loads.last_action_status not like 'FAILED')" + " )" + " and loads.active = 'Y'" + " and plan.id = loads.plan_id" + " and sysload.id = loads.system_load_id" + " and sysload.active = 'Y'" + " and (" + "     (sysload.prod_load_status like 'ACTIVATED_ON_ALL_CPU')" + "     or (sysload.prod_load_status like 'ACCEPTED')" + ")";
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter(0, DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	    when(instance.getAllAccepted(DataWareHouse.getPlan().getId())).thenReturn(Long.valueOf(1));
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testgetAllFallBackAccepted() throws Exception {
	try {

	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    SQLQuery mockedQry = mock(SQLQuery.class);
	    String lQuery = "select count(plan.id) from production_loads loads, system_load sysload, imp_plan plan" + " where plan.id like ?" + " and plan.plan_status like 'ONLINE'" + " and plan.accepted_date_time is not null" + " and plan.fallback_date_time is null" + " and loads.cpu_id is null" + " and (" + "     (loads.status like 'FALLBACK_ACTIVATED' and loads.last_action_status not like 'INPROGRESS')"
		    + "     or (loads.status like 'FALLBACK_ACCEPTED' and loads.last_action_status not like 'FAILED')" + " )" + " and loads.active = 'Y'" + " and plan.id = loads.plan_id" + " and sysload.id = loads.system_load_id" + " and sysload.active = 'Y'" + " and (" + "     (sysload.prod_load_status like 'FALLBACK_ACTIVATED')" + "     or (sysload.prod_load_status like 'FALLBACK_ACCEPTED')" + ")";
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter(0, DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	    when(instance.getAllFallBackAccepted(any())).thenReturn(Long.valueOf(1));
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testfindByFallbackPlanId() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(ProductionLoadsDAO.class);
	Mockito.when(session.createCriteria(ProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	String[] ids = new String[] { "1", "2" };
	instance.findByFallbackPlanId(ids, new System());
    }
}

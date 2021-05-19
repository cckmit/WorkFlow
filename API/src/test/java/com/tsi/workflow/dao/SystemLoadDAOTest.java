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
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
public class SystemLoadDAOTest {

    SystemLoadDAO instance;

    public SystemLoadDAOTest() {
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
	    SystemLoadDAO realInstance = new SystemLoadDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(SystemLoadDAO.class, SystemLoad.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, SystemLoadDAO.class);
    }

    @Test
    public void testGetCountBySystem() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT COUNT(sl) FROM SystemLoad sl, ImpPlan ip, System s" + " WHERE sl.systemId.id = :SystemId AND " + " sl.planId.id = ip.id AND " + " ip.planStatus = :PlanStatus AND " + " sl.active = 'Y' AND " + " s.id = :SystemId";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 0)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanStatus", "")).thenReturn(mockedQry);
	instance.getCountBySystem(0, "");
    }

    @Test
    public void testGetCountBySystem1() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT count(load1) FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND (select count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " AND plan1.planStatus = :PlanStatus" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'" + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id"
		+ " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 1)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanStatus", "")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("StatusList", Constants.LOAD_SET_STATUS.getAcceptAndFallbackList())).thenReturn(mockedQry);
	when(mockedQry.setParameter("MacroHeader", true)).thenReturn(mockedQry);
	when(mockedQry.setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter("LoadSetType", Constants.LoaderTypes.A.name())).thenReturn(mockedQry);
	try {
	    instance.getCountBySystem(1, null, true, "");
	} catch (Exception e) {

	}
    }

    @Test
    public void testGetStagingDepedendentPlans() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT othLoad FROM CheckoutSegments othSeg ,CheckoutSegments seg, SystemLoad othLoad, SystemLoad loadl, ImpPlan othPlan, System sys" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND sys.name = seg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId <> othSeg.planId" + " AND loadl.planId <> othLoad.planId" + " AND loadl.active = 'Y'"
		+ " AND othLoad.active = 'Y'" + " AND othPlan.approveDateTime is not null" + " AND loadl.systemId = othLoad.systemId" + " AND othLoad.planId = othSeg.planId" + " AND (" + "         OR(othLoad.loadDateTime BETWEEN loadl.loadDateTime AND  :loadDateTime)" + "         OR(othLoad.loadDateTime BETWEEN :loadDateTime AND loadl.loadDateTime)" + "     )" + " AND othPlan.id = othLoad.planId" + " AND sys.id = othLoad.systemId" + " AND loadl.planId = seg.planId"
		+ " AND seg.planId LIKE :planId" + " AND sys.id = :systemId order by othLoad.loadDateTime desc";

	Date now = new Date();
	Set<String> status = new HashSet();
	status.add(DataWareHouse.getPlan().getPlanStatus());
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", 1)).thenReturn(mockedQry);
	when(mockedQry.setParameter("planId", DataWareHouse.getPlan())).thenReturn(mockedQry);
	when(mockedQry.setParameter("loadDateTime", now)).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(null);
	try {
	    instance.getStagingDepedendentPlans(DataWareHouse.getPlan().getId(), 1, now);
	} catch (Exception e) {

	}
    }

    @Test
    public void testgetStagingDepedendentPlansWithDate() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT sysLoad FROM SystemLoad sysLoad, ImpPlan impPlan , System sys" + " WHERE sysLoad.systemId = sys.id " + " AND sysLoad.active = 'Y'" + " AND sys.id = :systemId" + " AND (sysLoad.loadDateTime " + (true ? ">=" : "<=") + " :loadDateTime ) " + " AND sysLoad.planId = impPlan.id" + " AND impPlan.id <> :planId" + " AND impPlan.approveDateTime IS NOT NULL" + " AND impPlan.planStatus IN :planStatus" + " ORDER BY sysLoad.loadDateTime desc";

	Date now = new Date();
	Set<String> status = new HashSet();
	status.add(DataWareHouse.getPlan().getPlanStatus());
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", 1)).thenReturn(mockedQry);
	when(mockedQry.setParameter("planId", DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("loadDateTime", now)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("planStatus", status)).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(null);
	try {
	    instance.getStagingDepedendentPlansWithDate(DataWareHouse.getPlan().getId(), 1, now, status, true);
	} catch (Exception e) {

	}
    }

    @Test
    public void testGetDependentPlanByApproveDate() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT sysLoad, impPlan FROM SystemLoad sysLoad, ImpPlan impPlan , System sys" + " WHERE sysLoad.systemId = sys.id " + " AND sys.id = :systemId " + " AND (sysLoad.loadDateTime is NOT null AND sysLoad.loadDateTime <= :loadDateTime ) " + " AND sysLoad.planId = impPlan.id " + " AND impPlan.id <> :planId " + " AND impPlan.planStatus IN :planStatus " + " order by sysLoad.loadDateTime desc, impPlan.approveDateTime desc";
	Date now = new Date();
	Set<String> status = new HashSet();
	status.add(DataWareHouse.getPlan().getPlanStatus());
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", 0)).thenReturn(mockedQry);
	when(mockedQry.setParameter("planId", DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("loadDateTime", now)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("planStatus", status)).thenReturn(mockedQry);
	// when(mockedQry.list()).thenReturn(null);
	instance.getDependentPlanByApproveDate(DataWareHouse.getPlan().getId(), 0, now, status);
    }

    @Test
    public void testFindByLoadCategories() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(SystemLoadDAO.class);
	when(session.createCriteria(SystemLoadDAO.class)).thenReturn(mockedCriteria);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	instance.findByLoadCategories(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId());
    }

    @Test
    public void testFindBySystem() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(SystemLoadDAO.class);
	when(session.createCriteria(SystemLoadDAO.class)).thenReturn(mockedCriteria);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	Integer[] pid = { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId() };
	instance.findBySystem(pid);
    }

    @Test
    public void testFindBySystem1() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT sl FROM SystemLoad sl, ImpPlan ip, System s" + " WHERE sl.systemId.id = :SystemId AND " + " sl.planId.id = ip.id AND " + " ip.planStatus = :PlanStatus AND " + " sl.active = 'Y' AND " + " s.id = :SystemId ORDER BY sl.loadDateTime DESC, sl.loadCategoryId.name ASC";
	Date now = new Date();
	Set<String> status = new HashSet();
	status.add(DataWareHouse.getPlan().getPlanStatus());
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanStatus", DataWareHouse.getPlan().getPlanStatus())).thenReturn(mockedQry);
	when(mockedQry.setFirstResult(5)).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(5)).thenReturn(mockedQry);
	instance.findBySystem(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), DataWareHouse.getPlan().getPlanStatus(), 1, 5);
    }

    @Test
    public void testFindBySystem2() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	String filter = "";
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT load1 FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND (select count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " AND plan1.planStatus = :PlanStatus" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'" + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id"
		+ " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType" + " ORDER BY load1.loadDateTime DESC, load1.loadCategoryId.name ASC";

	Set<String> status = new HashSet();
	status.add(DataWareHouse.getPlan().getPlanStatus());
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanStatus", DataWareHouse.getPlan().getPlanStatus())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("StatusList", Constants.LOAD_SET_STATUS.getAcceptAndFallbackList())).thenReturn(mockedQry);
	when(mockedQry.setParameter("LoadSetType", Constants.LoaderTypes.A.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter("MacroHeader", true)).thenReturn(mockedQry);
	when(mockedQry.setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name())).thenReturn(mockedQry);
	when(mockedQry.setFirstResult(5)).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(5)).thenReturn(mockedQry);
	try {
	    instance.findBySystem(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), null, true, 1, 5, filter);
	} catch (Exception e) {
	}
    }

    @Test
    public void testGetFallbackLoadSetPlanIds() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT othLoad FROM CheckoutSegments othSeg ,CheckoutSegments seg, SystemLoad othLoad, SystemLoad loadl, ImpPlan othPlan, System sys, LoadCategories cat" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND sys.name = seg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId <> othSeg.planId" + " AND loadl.planId <> othLoad.planId"
		+ " AND loadl.systemId = othLoad.systemId" + " AND othLoad.planId = othSeg.planId" + " AND othLoad.active = 'Y' " + " AND loadl.active = 'Y' " + " AND (othLoad.loadDateTime < loadl.loadDateTime)" + " AND othPlan.id = othLoad.planId" + " AND sys.id = othLoad.systemId" + " AND sys.id = :SystemId" + " AND othLoad.loadCategoryId = cat.id" + " AND cat.name NOT LIKE 'P'" + " AND loadl.planId = seg.planId" + " AND seg.planId LIKE :planid" + " AND othPlan.planStatus IN (:status)"
		+ " ORDER BY othLoad.loadDateTime desc";

	Date now = new Date();
	Set<String> status = new HashSet();
	status.add(DataWareHouse.getPlan().getPlanStatus());
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("planid", DataWareHouse.getPlan())).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 1)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", status)).thenReturn(mockedQry);

	instance.getFallbackLoadSetPlanIds(DataWareHouse.getPlan().getId(), status, 1);
    }

    @Test
    public void testGetSystemLoadsFromImp() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT DISTINCT seg.systemLoad from CheckoutSegments seg" + " WHERE seg.impId.id = :ImplId" + " AND seg.active = 'Y'";

	Date now = new Date();
	Set<String> status = new HashSet();
	status.add(DataWareHouse.getPlan().getPlanStatus());
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("ImplId", DataWareHouse.getPlan().getImplementationList().get(0).getId())).thenReturn(mockedQry);
	instance.getSystemLoadsFromImp(DataWareHouse.getPlan().getImplementationList().get(0).getId());
    }

    @Test
    public void testfindFallbackLoadsBySystem() {
	try {
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.WEEK_OF_MONTH, Constants.fallbackLoadDateGap);
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT load1 FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND plan1.acceptedDateTime BETWEEN :fromTime AND :toTime" + " AND (select count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " AND plan1.planStatus = :PlanStatus" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'"
		    + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id" + " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType" + " ORDER BY load1.loadDateTime DESC, load1.loadCategoryId.name ASC";

	    Set<String> status = new HashSet();
	    status.add(DataWareHouse.getPlan().getPlanStatus());
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("SystemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	    when(mockedQry.setParameterList("StatusList", Constants.LOAD_SET_STATUS.getFallbackFinalList())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("PlanStatus", Constants.PlanStatus.ONLINE.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("MacroHeader", false)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("LoadSetType", Constants.LoaderTypes.A.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("fromTime", c.getTime())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("toTime", new Date())).thenReturn(mockedQry);
	    when(mockedQry.setFirstResult(1 * 5)).thenReturn(mockedQry);
	    when(mockedQry.setMaxResults(5)).thenReturn(mockedQry);

	    instance.findFallbackLoadsBySystem(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), 1, 5);
	} catch (Exception e) {
	}
    }

    @Test
    public void testcountFallbackLoadsBySystem() {
	try {
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.WEEK_OF_MONTH, Constants.fallbackLoadDateGap);
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT count(load1) FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND plan1.acceptedDateTime BETWEEN :fromTime AND :toTime" + " AND (select count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " AND plan1.planStatus = :PlanStatus" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'"
		    + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id" + " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType";

	    Set<String> status = new HashSet();
	    status.add(DataWareHouse.getPlan().getPlanStatus());
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("SystemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	    when(mockedQry.setParameterList("StatusList", Constants.LOAD_SET_STATUS.getFallbackFinalList())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("PlanStatus", Constants.PlanStatus.ONLINE.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("MacroHeader", false)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("LoadSetType", Constants.LoaderTypes.A.name())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("fromTime", c.getTime())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("toTime", new Date())).thenReturn(mockedQry);
	    when(mockedQry.uniqueResult()).thenReturn(new Long(2));

	    instance.countFallbackLoadsBySystem(1);
	} catch (Exception e) {
	}
    }

    @Test
    public void testfindBySystem() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(SystemLoadDAO.class);
	Mockito.when(session.createCriteria(SystemLoadDAO.class)).thenReturn(mockedCriteria);
	List<String> intobj = new ArrayList<String>();
	intobj.add(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name());
	intobj.add(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name());
	instance.findPlanByQATestingStatus(DataWareHouse.getPlan().getId(), intobj);
    }

    @Test
    public void testfindBySystem1() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(SystemLoadDAO.class);
	Mockito.when(session.createCriteria(SystemLoadDAO.class)).thenReturn(mockedCriteria);
	LinkedHashMap<String, String> pOrderBy = new LinkedHashMap<String, String>();
	pOrderBy.put("id", "asc");
	instance.findBySystem(1, 1, 5, pOrderBy);
    }

}

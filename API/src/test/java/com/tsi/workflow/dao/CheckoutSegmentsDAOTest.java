/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.LegacyFallBackPlan;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class CheckoutSegmentsDAOTest {

    CheckoutSegmentsDAO instance;

    public CheckoutSegmentsDAOTest() {
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
	    CheckoutSegmentsDAO realInstance = new CheckoutSegmentsDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(CheckoutSegmentsDAO.class, CheckoutSegments.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, CheckoutSegmentsDAO.class);
    }

    @Test
    public void testFfindByFileName() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));

	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(CheckoutSegmentsDAO.class);
	when(session.createCriteria(CheckoutSegmentsDAO.class)).thenReturn(mockedCriteria);
	instance.findByFileName("", "", "", "");
    }

    @Test
    public void testFindByFileName() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.programName LIKE (:programName) AND " + " seg.planId.id LIKE (:nickName) AND " + " seg.planId.planStatus IN (:status) AND " + " seg.impId.checkinDateTime IS NOT NULL AND " + " seg.active LIKE 'Y' ORDER BY seg.fileName, seg.systemLoad.loadDateTime DESC";

	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("programName", "a%")).thenReturn(mockedQry);
	when(mockedQry.setParameter("nickName", "b%")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", Constants.PlanStatus.getAfterSubmitStatus().keySet())).thenReturn(mockedQry);
	try {
	    instance.findByFileName("a", "b");
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetDependentSegments() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "select imp.id from checkout_segments seg, checkout_segments seg1, system_load load1, system_load load2, implementation imp" + " where" + " seg.imp_id like ?" + " and seg.file_name = seg1.file_name" + " and seg.plan_id <> seg1.plan_id" + " and seg.target_system = seg1.target_system"
	// + " -- and seg.file_hash_code = seg1.file_hash_code"
		+ " and load1.id = seg.system_load" + " and load2.id = seg1.system_load" + " and seg.imp_id != seg1.imp_id" + " and imp.id = seg1.imp_id" + " and imp.imp_status <> ?" + " and seg.active='Y'" + " and seg1.active='Y'" + " and load1.active='Y'" + " and load2.active='Y'" + " and load2.load_date_time < load1.load_date_time";

	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter(0, "")).thenReturn(mockedQry);
	when(mockedQry.setParameter(1, "")).thenReturn(mockedQry);

	instance.getDependentSegments("", "");
    }

    @Test
    public void testFindByFiles() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT Seg FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName IN (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	Date now = new Date();
	when(mockedQry.setParameter("loadDateTime", now)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("progNameList", new ArrayList())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("planStatusList", new ArrayList())).thenReturn(mockedQry);
	when(mockedQry.setFirstResult(5)).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(5)).thenReturn(mockedQry);

	instance.findByFiles(new ArrayList(), new ArrayList(), now, DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 1, 5, null);
    }

    @Test
    public void testFindByFiles1() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT Seg FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName LIKE (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	Date now = new Date();
	when(mockedQry.setParameter("loadDateTime", now)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("progNameList", "a%")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("planStatusList", new ArrayList())).thenReturn(mockedQry);
	when(mockedQry.setFirstResult(5)).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(5)).thenReturn(mockedQry);

	instance.findByFiles(new ArrayList(), "a", now, DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 1, 5, null);
    }

    @Test
    public void testCountByFiles() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT count(Seg) FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName IN (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	Date now = new Date();
	when(mockedQry.setParameter("loadDateTime", now)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("progNameList", new ArrayList())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("planStatusList", new ArrayList())).thenReturn(mockedQry);
	when(mockedQry.setFirstResult(5)).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(5)).thenReturn(mockedQry);

	instance.countByFiles(new ArrayList(), new ArrayList(), now, DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
    }

    @Test
    public void testCountByFiles1() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT count(Seg) FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName LIKE (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	Date now = new Date();
	when(mockedQry.setParameter("loadDateTime", now)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("progNameList", "a%")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("planStatusList", new ArrayList())).thenReturn(mockedQry);

	instance.countByFiles(new ArrayList(), "a", now, DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
    }

    @Test
    public void testCommonFiles() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "select concat(b.ipaddress,',',array_to_string(array_agg(concat(lower(a.target_system),'/',a.file_name)),',')) from checkout_segments a, system b\n" + " where a.imp_id like :implId" + " and a.file_name like :file" + " and a.active = 'Y'" + " and a.common_file = true" + " and b.name = a.target_system" + " group by a.target_system, b.ipaddress";
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	Date now = new Date();
	when(mockedQry.setParameter("implId", "")).thenReturn(mockedQry);
	when(mockedQry.setParameter("file", "")).thenReturn(mockedQry);

	instance.findCommonFiles("", "");
    }

    @Test
    public void testCommonFilesStatus() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "select a.file_name from checkout_segments a\n" + " where a.imp_id like :implId" + " and a.file_name like :file" + " and a.active = 'Y'" + " and a.common_file = true" + " and a.target_system = :core";

	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	Date now = new Date();
	when(mockedQry.setParameter("implId", "")).thenReturn(mockedQry);
	when(mockedQry.setParameter("file", "")).thenReturn(mockedQry);
	when(mockedQry.setParameter("core", "")).thenReturn(mockedQry);

	instance.findCommonFilesStatus("", "", "");
    }

    @Test
    public void testGetBlockedSystemsByPlan() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT DISTINCT seg.targetSystem from CheckoutSegments seg" + " WHERE seg.planId.id = :PlanId" + " AND seg.active = 'Y'";

	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", "")).thenReturn(mockedQry);

	instance.getBlockedSystemsByPlan("");
    }

    @Test
    public void testIsMacroHeaderPlan1() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT seg.program_name from checkout_segments seg " + "WHERE seg.plan_id = :PlanId " + "AND seg.active = 'Y' " + "AND seg.program_name NOT SIMILAR TO '%.mac|%.hpp|%.h|%.cpy'";
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", "")).thenReturn(mockedQry);

	instance.isMacroHeaderPlan("");
    }

    @Test
    public void testGetSameSegmentDevelopersByImpId() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT othSeg" + " FROM CheckoutSegments othSeg ,CheckoutSegments seg, ImpPlan othPlan, Implementation imp, SystemLoad load1, SystemLoad load2" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND load1.active = 'Y'" + " AND load2.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId.id <> othSeg.planId.id" + " AND othPlan.id = othSeg.planId.id"
		+ " AND seg.impId.id = :impId" + " AND othPlan.planStatus IN (:statuses)" + " AND imp.id = othSeg.impId.id" + " AND load1.id = othSeg.systemLoad.id" + " AND load2.id = seg.systemLoad.id" + " AND load1.loadDateTime IS NOT NULL" + " AND load2.loadDateTime IS NOT NULL" + " AND load2.loadDateTime < load1.loadDateTime";
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("statuses", Arrays.asList(Constants.PlanStatus.ACTIVE.name()))).thenReturn(mockedQry);
	when(mockedQry.setParameter("impId", "")).thenReturn(mockedQry);
	instance.getSameSegmentDevelopersByImpId("", Arrays.asList(Constants.PlanStatus.ACTIVE.name()));
    }

    @Test
    public void testGetSameSegmentDevelopersByFileName() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	String lQuery = "SELECT DISTINCT othSeg" + " FROM CheckoutSegments othSeg ,CheckoutSegments seg, ImpPlan othPlan, Implementation imp, SystemLoad load1, SystemLoad load2" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.fileName = :FileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId.id <> othSeg.planId.id" + " AND othPlan.id = othSeg.planId.id"
		+ " AND seg.impId.id <> othSeg.impId.id" + " AND othPlan.planStatus IN (:statuses)" + " AND load1.id = othSeg.systemLoad.id" + " AND load2.id = seg.systemLoad.id" + " AND load1.loadDateTime IS NOT NULL" + " AND load2.loadDateTime IS NOT NULL" + " AND load2.loadDateTime < load1.loadDateTime" + " AND othSeg.impId.id <> :ImpId";
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("statuses", Arrays.asList(Constants.PlanStatus.ACTIVE.name()))).thenReturn(mockedQry);
	when(mockedQry.setParameter("ImpId", "")).thenReturn(mockedQry);
	when(mockedQry.setParameter("FileName", "")).thenReturn(mockedQry);
	instance.getSameSegmentDevelopersByFileName("", "", Arrays.asList(Constants.PlanStatus.ACTIVE.name()));
    }

    @Test
    public void testFindByImplementation() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));

	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(CheckoutSegmentsDAO.class);
	when(session.createCriteria(CheckoutSegmentsDAO.class)).thenReturn(mockedCriteria);
	String[] ids = { DataWareHouse.getPlan().getImplementationList().get(0).getId() };
	instance.findByImplementation(ids);
    }

    @Test
    public void testfindByDependentPlans() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	String lQuery = "SELECT segs.plan_id  FROM checkout_segments segs, imp_plan impPlan, system_load sysLoad " + " WHERE segs.program_name = :programName " + "AND segs.func_area = :funcArea " + "AND sysLoad.load_date_time >= to_date(:loadDate,'yyyyMMdd') " + "AND impPlan.id = sysLoad.plan_id " + " AND impPlan.id = segs.plan_id " + "AND segs.active = 'Y' ";

	LegacyFallBackPlan legacy = new LegacyFallBackPlan();
	legacy.setFuncArea("FLS");
	legacy.setId(1);
	legacy.setPlanId(DataWareHouse.getPlan().getId());
	legacy.setLoadDateTime("2018-10-10");
	legacy.setProgramName("acd1.asm");
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("programName", "acd1.asm")).thenReturn(mockedQry);
	when(mockedQry.setParameter("funcArea", "FLS")).thenReturn(mockedQry);
	when(mockedQry.setParameter("loadDate", legacy.getLoadDateTime())).thenReturn(mockedQry);
	instance.findByDependentPlans(legacy);
    }

    @Test
    public void testfindByFileName() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT DISTINCT Seg.planId.id FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.fileName LIKE (:fileName) AND" + " Seg.active = 'Y' AND" + " Seg.planId.id = IP.id AND" + " IP.planStatus NOT IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " Seg.targetSystem = :systemName AND" + " Seg.targetSystem = S.name AND" + " S.active = 'Y' AND" + " Seg.active = 'Y'";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemName", "WSP")).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileName", "acd1.asm" + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("planStatusList", new ArrayList<>(Constants.PlanStatus.getOnlineAndAbove().keySet()))).thenReturn(mockedQry);
	instance.findByFileName(new ArrayList<>(Constants.PlanStatus.getOnlineAndAbove().keySet()), "acd1.asm", "WSP");
    }

    @Test
    public void testfindCommonFilesBtwnPlan() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	String lQuery = "SELECT DISTINCT concat(seg1.file_name,'[',seg1.target_system,']') FROM checkout_segments seg1, checkout_segments seg2" + " WHERE seg1.file_name = seg2.file_name" + " AND seg1.active = 'Y'" + " AND seg2.active = 'Y'" + " AND seg1.file_name NOT LIKE '%.mak'" + " AND seg1.plan_id = :SourcePlan" + " AND seg2.plan_id = :TargetPlan";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("SourcePlan", "T1800023")).thenReturn(mockedQry);
	when(mockedQry.setParameter("TargetPlan", "T1800094")).thenReturn(mockedQry);
	instance.findCommonFilesBtwnPlan("T1800023", "T1800094");
    }

    @Test
    public void testfinfindByFileName() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.programName LIKE (:programName) AND " + " seg.planId.id LIKE (:nickName) AND " + " seg.planId.planStatus IN (:status) AND " + " seg.impId.checkinDateTime IS NOT NULL AND " + " seg.refStatus IS NOT NULL AND " + " seg.active LIKE 'Y' ORDER BY seg.fileName, seg.systemLoad.loadDateTime DESC";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("programName", "acd1.asm" + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameter("nickName", "T1800094_001".charAt(0) + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", Constants.PlanStatus.getAfterSubmitStatus().keySet())).thenReturn(mockedQry);
	instance.findByFileName("acd1.asm", "T1800094_001");
    }

    @Test
    public void testgetSegmentWithNoAccess() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.active = 'Y' " + " AND seg.impId.id = :impId " + " AND seg.sourceUrl NOT IN (:repoList)";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	Set<String> pRepoList = new HashSet<>();
	pRepoList.add("tpf/tp/nonibm/nonibm_covr.git");
	when(mockedQry.setParameter("impId", "T1800094_001")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("repoList", pRepoList)).thenReturn(mockedQry);
	instance.getSegmentWithNoAccess("T1800094_001", pRepoList);
    }

    @Test
    public void testfindByFileNameAccess() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.programName LIKE (:programName) AND " + " seg.planId.id LIKE (:nickName) AND " + " seg.planId.planStatus IN (:status) AND " + " seg.impId.checkinDateTime IS NOT NULL AND " + " seg.refStatus IS NOT NULL AND " + " seg.sourceUrl IN (:repoList) AND" + " seg.active LIKE 'Y' ORDER BY seg.fileName, seg.systemLoad.loadDateTime DESC";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	Set<String> pRepoList = new HashSet<>();
	pRepoList.add("tpf/tp/nonibm/nonibm_covr.git");
	when(mockedQry.setParameter("programName", "acd1.asm" + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameter("nickName", "T1800094_001".charAt(0) + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("repoList", pRepoList)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", Constants.PlanStatus.getAfterSubmitStatus().keySet())).thenReturn(mockedQry);
	instance.findByFileName("acd1.asm", "T1800094_001", pRepoList);
    }

    @Test
    public void testgetSgementsByPlan() {

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.active = 'Y' " + " AND seg.impId.id = :impId ";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("impId", DataWareHouse.getPlan().getImplementationList().get(0).getId())).thenReturn(mockedQry);
	instance.getSgementsByPlan(DataWareHouse.getPlan().getImplementationList().get(0).getId());

    }

}

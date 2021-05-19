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

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadFreeze;
import com.tsi.workflow.beans.dao.LoadFreezeGrouped;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.ui.LoadFreezeForm;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
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
public class LoadFreezeDAOTest {

    LoadFreezeDAO instance;

    public LoadFreezeDAOTest() {
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
	    LoadFreezeDAO realInstance = new LoadFreezeDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(LoadFreezeDAO.class, LoadFreeze.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, LoadFreezeDAO.class);
    }

    @Test
    public void testFindCommonDateBySystem() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	List<String> lDateList = new ArrayList<String>();
	lDateList.add("9999-10-27");
	String lQuery = "SELECT to_char( date, '" + Constants.LOAD_FREEZE_DATE_TIME_FORMAT + "') as date FROM load_freeze_date" + " WHERE system_id = :SystemId" + " AND date BETWEEN :fromDate AND :toDate" + " AND category_count = :Count";
	Date dt = new Date();
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	Date fromDate = DateUtils.addDays(DateUtils.truncate(dt, Calendar.MONTH), -7);
	Date toDate = DateUtils.addDays(DateUtils.ceiling(dt, Calendar.MONTH), 7);
	when(mockedQry.setParameter("Count", 1)).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 0)).thenReturn(mockedQry);
	when(mockedQry.setParameter("fromDate", fromDate)).thenReturn(mockedQry);
	when(mockedQry.setParameter("toDate", toDate)).thenReturn(mockedQry);
	LoadFreezeDAO mockLoad = mock(LoadFreezeDAO.class);
	when(session.createSQLQuery(lQuery).list()).thenReturn(lDateList);
	instance.findCommonDateBySystem(0, dt, 1);
    }

    @Test
    public void testFindCommonDateBySystem1() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	List<String> lDateList = new ArrayList<String>();
	lDateList.add("9999-10-27");
	String lQuery = "SELECT DISTINCT to_char( date, '" + Constants.LOAD_FREEZE_DATE_TIME_FORMAT + "') as date FROM load_freeze_date" + " WHERE system_id = :SystemId" + " AND date BETWEEN :fromDate AND :toDate";
	Date dt = new Date();
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	Date fromDate = DateUtils.addDays(DateUtils.truncate(dt, Calendar.MONTH), -7);
	Date toDate = DateUtils.addDays(DateUtils.ceiling(dt, Calendar.MONTH), 7);
	when(mockedQry.setParameter("SystemId", 0)).thenReturn(mockedQry);
	when(mockedQry.setParameter("fromDate", fromDate)).thenReturn(mockedQry);
	when(mockedQry.setParameter("toDate", toDate)).thenReturn(mockedQry);
	when(session.createSQLQuery(lQuery).list()).thenReturn(lDateList);
	instance.findCommonDateBySystem(0, dt);
    }

    @Test
    public void testFindCommonDateByLoadCategory() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	List<String> lDateList = new ArrayList<String>();
	lDateList.add("9999-10-27");
	String lQuery = "SELECT to_char( date, '" + Constants.LOAD_FREEZE_DATE_TIME_FORMAT + "') as date FROM load_window_date" + " WHERE date BETWEEN :fromDate AND :toDate" + " AND load_category_id = :Category";
	Date dt = new Date();
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	Date fromDate = DateUtils.addDays(DateUtils.truncate(dt, Calendar.MONTH), -7);
	Date toDate = DateUtils.addDays(DateUtils.ceiling(dt, Calendar.MONTH), 7);
	when(mockedQry.setParameter("Category", 0)).thenReturn(mockedQry);
	when(mockedQry.setParameter("fromDate", fromDate)).thenReturn(mockedQry);
	when(mockedQry.setParameter("toDate", toDate)).thenReturn(mockedQry);
	when(session.createSQLQuery(lQuery).list()).thenReturn(lDateList);
	instance.findCommonDateByLoadCategory(0, dt);
    }

    @Test
    public void testgetLoadFreezeGrouped() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    SQLQuery mockedSQry = mock(SQLQuery.class);
	    String lQuery = "SELECT * from load_freeze_group";
	    String lOrderByString = " ORDER BY id asc";
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createSQLQuery(lQuery + lOrderByString)).thenReturn(mockedSQry);
	    when(mockedSQry.setFirstResult(5)).thenReturn(mockedSQry);
	    when(mockedSQry.setMaxResults(5)).thenReturn(mockedSQry);
	    when(mockedSQry.setResultTransformer(new AliasToBeanResultTransformer(LoadFreezeGrouped.class))).thenReturn(mockedSQry);
	    LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
	    orderBy.put("id", "asc");

	    instance.getLoadFreezeGrouped(1, 5, orderBy);
	} catch (Exception e) {

	}
    }

    @Test
    public void testgetLoadFreezeGroupedCount() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    SQLQuery mockedSQry = mock(SQLQuery.class);
	    String lQuery = "SELECT COUNT(ids) from load_freeze_group";
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when((session.createSQLQuery(lQuery))).thenReturn(mockedSQry);
	    when(Long.valueOf(session.createSQLQuery(lQuery).uniqueResult().toString())).thenReturn(new Long(2));
	    instance.getLoadFreezeGroupedCount();
	} catch (Exception e) {

	}

    }

    @Test
    public void testgetLoadFreezeGroupedByDateAndSystem() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	String lQuery = "SELECT DISTINCT a.* FROM load_freeze_group a, system b" + " WHERE :Date BETWEEN to_char(a.from_date,'YYYY-MM-DD') AND to_char(a.to_date,'YYYY-MM-DD')" + " AND b.id = :SystemId" + " AND b.active = 'Y'" + " AND a.name = b.name ";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("Date", "2018-07-12")).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 1)).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(LoadFreezeGrouped.class))).thenReturn(mockedQry);
	instance.getLoadFreezeGroupedByDateAndSystem("2018-07-12", 1);
    }

    @Test
    public void testfindCommonDateBySystem() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    SQLQuery mockedQry = mock(SQLQuery.class);
	    String lQuery = "SELECT DISTINCT to_char( date, '" + Constants.LOAD_FREEZE_DATE_TIME_FORMAT + "') as date FROM load_freeze_date" + " WHERE system_id = :SystemId" + " AND date BETWEEN :fromDate AND :toDate";
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("SystemId", 1)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("fromDate", "2018-07-12")).thenReturn(mockedQry);
	    when(mockedQry.setParameter("toDate", "2018-07-10")).thenReturn(mockedQry);
	    Date date = new Date();
	    instance.findCommonDateBySystem(1, date);
	} catch (Exception e) {

	}
    }

    @Test
    public void testlLoadFreezeList() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT load FROM LoadFreeze load" + " WHERE load.active = 'Y'" + " AND load.loadCategoryId.systemId.id = :systemId " + " AND load.loadCategoryId.id = :loadCategoryId " + " AND load.toDate  = :toDate" + " AND load.fromDate = :fromDate";

	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("systemId", 1)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("loadCategoryId", 1)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("toDate", new Date())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("fromDate", new Date())).thenReturn(mockedQry);
	    System s = new System();
	    s.setId(1);
	    LoadFreeze load = new LoadFreeze();
	    load.setFromDate(new Date());
	    load.setToDate(new Date());
	    load.setId(1);
	    LoadFreezeForm loadFreeze = new LoadFreezeForm();
	    loadFreeze.setLoadFreeze(load);
	    loadFreeze.setSystem(s);
	    LoadCategories categories = new LoadCategories();
	    categories.setId(1);
	    instance.lLoadFreezeList(loadFreeze, categories);
	} catch (Exception e) {

	}
    }

    @Test
    public void testlLoadFreezeListByCategories() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT load FROM LoadFreeze load" + " WHERE load.active = 'Y' " + " AND load.loadCategoryId.systemId.id = :systemId " + " AND load.loadCategoryId.id  = :loadCategoryId" + " AND load.toDate  = :toDate" + " AND load.fromDate = :fromDate";
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("systemId", 1)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("loadCategoryId", 1)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("toDate", new Date())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("fromDate", new Date())).thenReturn(mockedQry);
	    System s = new System();
	    s.setId(1);
	    LoadCategories categories = new LoadCategories();
	    categories.setId(1);
	    categories.setSystemId(s);
	    LoadFreeze load = new LoadFreeze();
	    load.setFromDate(new Date());
	    load.setToDate(new Date());
	    load.setId(1);
	    load.setLoadCategoryId(categories);
	    instance.lLoadFreezeListByCategories(load);
	} catch (Exception e) {

	}
    }

    @Test
    public void testlLoadFreezeUpdateByDate() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT load FROM LoadFreeze load" + " WHERE load.active = 'Y' " + " AND load.loadCategoryId.systemId.id = :systemId " + " AND load.loadCategoryId.id  = :loadCategoryId" + " AND load.toDate  = :toDate" + " AND load.fromDate = :fromDate";
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("systemId", 1)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("loadCategoryId", 1)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("toDate", new Date())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("fromDate", new Date())).thenReturn(mockedQry);
	    System s = new System();
	    s.setId(1);
	    LoadCategories categories = new LoadCategories();
	    categories.setId(1);
	    categories.setSystemId(s);
	    LoadFreeze load = new LoadFreeze();
	    load.setFromDate(new Date());
	    load.setToDate(new Date());
	    load.setId(1);
	    load.setLoadCategoryId(categories);
	    instance.lLoadFreezeUpdateByDate(load, new Date(), new Date());
	} catch (Exception e) {

	}
    }
}

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
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.System;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class LoadCategoriesDAOTest {

    LoadCategoriesDAO instance;

    public LoadCategoriesDAOTest() {
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
	    LoadCategoriesDAO realInstance = new LoadCategoriesDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(LoadCategoriesDAO.class, LoadCategories.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, LoadCategoriesDAO.class);
    }

    @Test
    public void testGetDevelopersBySegmentForDelete() {
	com.tsi.workflow.beans.dao.System lSystem = DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId();
	Date lDate = new Date();
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);

	String lQuery = "SELECT DISTINCT a FROM LoadCategories a, LoadFreeze b" + " WHERE a.id != b.loadCategoryId" + " AND a.systemId = :System" + " AND :Date between b.fromDate and b.toDate" + " AND b.active = 'Y'" + " AND a.active = 'Y'";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("System", new com.tsi.workflow.beans.dao.System(lSystem.getId()))).thenReturn(mockedQry);
	when(mockedQry.setParameter("Date", lDate)).thenReturn(mockedQry);

	instance.getLoadCategoriesBy(lSystem.getId(), lDate);
    }

    @Test
    public void testfindBySystem() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(LoadCategoriesDAO.class);
	when(session.createCriteria(LoadCategoriesDAO.class)).thenReturn(mockedCriteria);
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	lFilter.put("active", "Y");
	when(instance.findAll(lFilter)).thenReturn(null);
	instance.findBySystem(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());

    }

    @Test
    public void testfindByIdAndSystem() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(LoadCategoriesDAO.class);
	Mockito.when(session.createCriteria(LoadCategoriesDAO.class)).thenReturn(mockedCriteria);
	List<String> intobj = new ArrayList<String>();
	intobj.add("1");
	instance.findByIdAndSystem(1, intobj);
    }

    @Test
    public void testgetLoadCategoryByDate() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    SQLQuery mockedQry = mock(SQLQuery.class);
	    String lQuery = "SELECT DISTINCT load_category_id FROM load_window_date WHERE date = :Date";

	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("Date", new Date())).thenReturn(mockedQry);
	    instance.getLoadCategoryByDate(new Date());
	} catch (Exception e) {

	}
    }

    @Test
    public void testgetLoadCategoriesByDay() {
	try {
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT DISTINCT cat FROM LoadCategories cat, LoadWindow win" + " WHERE cat.active = 'Y'" + " AND win.active = 'Y'" + " AND cat.systemId = :SystemId" + " AND cat.id = win.loadCategoryId" + " AND win.daysOfWeek = :Day";

	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("SystemId", new System(1))).thenReturn(mockedQry);
	    when(mockedQry.setParameter("Day", "5")).thenReturn(mockedQry);
	    instance.getLoadCategoriesByDay(1, "5");
	} catch (Exception e) {

	}
    }

    @Test
    public void testgetLoadCategoriesBy() {
	try {
	    List<LoadCategories> pLoadCategories = new ArrayList<>();
	    LoadCategories load = new LoadCategories();
	    load.setId(1);
	    pLoadCategories.add(load);
	    List<Integer> lCategoryId = new ArrayList<>();
	    lCategoryId.add(1);
	    SessionFactory sessionFactory = mock(SessionFactory.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    Session session = mock(Session.class);
	    Query mockedQry = mock(Query.class);
	    String lQuery = "SELECT cat FROM LoadCategories cat, LoadFreeze fre" + " WHERE cat.active = 'Y'" + " AND fre.active = 'Y'" + " AND cat.id = fre.loadCategoryId" + " AND :Date BETWEEN fre.fromDate AND fre.toDate" + " AND cat.id IN (:CategoryId)";

	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createQuery(lQuery)).thenReturn(mockedQry);
	    when(mockedQry.setParameterList("CategoryId", lCategoryId)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("Date", new Date())).thenReturn(mockedQry);
	    instance.getLoadCategoriesBy(pLoadCategories, new Date());
	} catch (Exception e) {

	}
    }

}

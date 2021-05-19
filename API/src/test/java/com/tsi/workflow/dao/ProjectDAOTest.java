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
import com.tsi.workflow.beans.dao.Project;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
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
public class ProjectDAOTest {

    ProjectDAO instance;

    public ProjectDAOTest() {
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
	    ProjectDAO realInstance = new ProjectDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(ProjectDAO.class, Project.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, ProjectDAO.class);
    }

    @Test
    public void testfindActiveProjectNumbers() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(ProjectDAO.class);
	when(session.createCriteria(ProjectDAO.class)).thenReturn(mockedCriteria);
	List<Criterion> criterions = new ArrayList<>();
	criterions.add(Restrictions.eq("active", "Y"));
	when(instance.findAll(criterions)).thenReturn(Arrays.asList(DataWareHouse.getPlan().getProjectId()));
	instance.findActiveProjectNumbers();

    }

    @Test
    public void testfindNonActiveProjectNumbers() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(ProjectDAO.class);
	when(session.createCriteria(ProjectDAO.class)).thenReturn(mockedCriteria);
	List<Criterion> criterions = new ArrayList<>();
	criterions.add(Restrictions.eq("active", "N"));
	when(instance.findAll(criterions)).thenReturn(Arrays.asList(DataWareHouse.getPlan().getProjectId()));
	instance.findNonActiveProjectNumbers();

    }

    @Test
    public void testfindFiltered() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(ProjectDAO.class);
	Mockito.when(session.createCriteria(ProjectDAO.class)).thenReturn(mockedCriteria);
	LinkedHashMap<String, String> pOrderBy = new LinkedHashMap<String, String>();
	pOrderBy.put("id", "asc");
	instance.findFiltered("32", 1, 5, pOrderBy, "", true);
	instance.countBy("32", true, "");
    }

}

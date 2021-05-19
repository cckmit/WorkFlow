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
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;
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
public class VparsDAOTest {

    VparsDAO instance;

    public VparsDAOTest() {
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
	    VparsDAO realInstance = new VparsDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(VparsDAO.class, Vpars.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, VparsDAO.class);
    }

    @Test
    public void testFindBySystem() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(VparsDAO.class);
	when(session.createCriteria(VparsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystem((Integer[]) Arrays.asList(DataWareHouse.getSystemList().get(0).getId()).toArray());
    }

    @Test
    public void testFindBySystem1() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(VparsDAO.class);
	when(session.createCriteria(VparsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystem(DataWareHouse.getSystemList());
    }

    @Test
    public void testfindBySystem() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(VparsDAO.class);
	Mockito.when(session.createCriteria(VparsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystem(DataWareHouse.getSystemList(), Constants.VPARSEnvironment.INTEGRATION, DataWareHouse.getUser().getId());
    }

    @Test
    public void testfindByVaprsIds() {
	Integer[] vpars = new Integer[] { 1, 2 };
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(VparsDAO.class);
	Mockito.when(session.createCriteria(VparsDAO.class)).thenReturn(mockedCriteria);
	instance.findByVpars(vpars);
    }

}

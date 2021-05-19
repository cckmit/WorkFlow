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
import com.tsi.workflow.base.BaseDAO;
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
public class SystemDAOTest {

    SystemDAO instance;

    public SystemDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private SessionFactory sessionFactory;
    private Session session;
    private Criteria criteria;

    @Before
    public void setUp() {
	try {
	    SystemDAO realInstance = new SystemDAO();
	    instance = spy(realInstance);
	    sessionFactory = mock(SessionFactory.class);
	    session = mock(Session.class);
	    criteria = mock(Criteria.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createCriteria(BaseDAO.class)).thenReturn(criteria);
	    TestCaseMockService.doMockBaseDAO(SystemDAO.class, System.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, SystemDAO.class);
    }

    @Test
    public void testfindByIds() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(SystemDAO.class);
	Mockito.when(session.createCriteria(SystemDAO.class)).thenReturn(mockedCriteria);
	Integer[] intobj = new Integer[] { 1 };
	instance.findByIds(intobj);
    }
}

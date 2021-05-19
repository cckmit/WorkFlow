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
import com.tsi.workflow.beans.dao.SystemCpu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SystemCpuDAOTest {

    SystemCpuDAO instance;

    public SystemCpuDAOTest() {
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
	    SystemCpuDAO realInstance = new SystemCpuDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(SystemCpuDAO.class, SystemCpu.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, SystemCpuDAO.class);
    }

    @Test
    public void testFindCpusOtherthan() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);

	String lQuery = "SELECT cpu FROM SystemCpu cpu" + " WHERE cpu.id NOT IN (:loads)" + " AND cpu.systemId = :system" + " AND cpu.cpuType LIKE :type" + " AND cpu.active = 'Y' ORDER BY cpu.displayName DESC ";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("loads", Arrays.asList(new Integer(0)))).thenReturn(mockedQry);
	when(mockedQry.setParameter("system", DataWareHouse.getSystemList().get(0))).thenReturn(mockedQry);
	when(mockedQry.setParameter("type", DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0).getCpuType())).thenReturn(mockedQry);
	instance.findCpusOtherthan(DataWareHouse.getSystemList().get(0), Arrays.asList(new Integer(0)), DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0).getCpuType(), Boolean.FALSE);
    }

    @Test
    public void testFindCpusOtherthan1() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);

	String lQuery = "SELECT cpu FROM SystemCpu cpu" + " WHERE cpu.systemId = :system" + " AND cpu.cpuType LIKE :type" + " AND cpu.active = 'Y' ORDER BY cpu.displayName DESC ";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("system", DataWareHouse.getSystemList().get(0))).thenReturn(mockedQry);
	when(mockedQry.setParameter("type", DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0).getCpuType())).thenReturn(mockedQry);
	instance.findCpusOtherthan(DataWareHouse.getSystemList().get(0), new ArrayList(), DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0).getCpuType(), Boolean.FALSE);
	instance.findCpusOtherthan(DataWareHouse.getSystemList().get(0), new ArrayList(), DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0).getCpuType(), Boolean.TRUE);
    }

}

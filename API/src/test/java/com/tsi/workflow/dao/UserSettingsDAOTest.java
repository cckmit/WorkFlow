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
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.utils.Constants;
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
public class UserSettingsDAOTest {

    UserSettingsDAO instance;

    public UserSettingsDAOTest() {
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
	    UserSettingsDAO realInstance = new UserSettingsDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(UserSettingsDAO.class, UserSettings.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, UserSettingsDAO.class);
    }

    @Test
    public void testFindAllActiveDelegations() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "select a from UserSettings a, UserSettings b" + " where a.name like ?" + " and b.userId = a.userId" + " and b.name like ?" + " and b.value like 'TRUE'";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter(0, Constants.UserSettings.DELEGATE_USER.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter(1, Constants.UserSettings.DELEGATION.name())).thenReturn(mockedQry);
	instance.findAllActiveDelegations();
    }

    @Test
    public void testFindAnyDelegatedUsersFor() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "select a from UserSettings a, UserSettings b" + " where a.userId in (:userList)" + " and a.name like :type1" + " and b.userId = a.userId" + " and b.name like :type2" + " and b.value like 'TRUE'";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("userList", Arrays.asList(DataWareHouse.getUser().getId()))).thenReturn(mockedQry);
	when(mockedQry.setParameter("type1", Constants.UserSettings.DELEGATE_USER.name())).thenReturn(mockedQry);
	when(mockedQry.setParameter("type2", Constants.UserSettings.DELEGATION.name())).thenReturn(mockedQry);
	instance.findAnyDelegatedUsersFor(Arrays.asList(DataWareHouse.getUser().getId()));
    }

}

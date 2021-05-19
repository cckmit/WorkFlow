/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.mail.DelegateActivationMail;
import com.tsi.workflow.utils.Constants;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class DelegateHelperTest {

    public DelegateHelperTest() {
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
	    DelegateHelper realInstance = new DelegateHelper();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, UserSettingsDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, LDAPAuthenticatorImpl.class);
	    // TestCaseMockService.doMockDAO(instance, ConcurrentHashMap.class);
	    // TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);

	} catch (Exception ex) {
	    Logger.getLogger(RejectHelperTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    DelegateHelper instance;

    @Test
    public void testDelegateHelper() {
	TestCaseExecutor.doTest(instance, DelegateHelper.class);
    }

    /**
     * Test of onActivateSuperUser method, of class DelegateHelper.
     */
    @Test
    public void testOnActivateSuperUser() {
	DelegateHelper instance = new DelegateHelper();

	User lUser = DataWareHouse.getUser();

	UserSettings pUserSetting = DataWareHouse.getUserSettings();
	LDAPAuthenticatorImpl lDAPAuthenticatorImpl = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", lDAPAuthenticatorImpl);
	ReflectionTestUtils.setField(instance, "lSuperUserMap", mock(ConcurrentHashMap.class));
	when(lDAPAuthenticatorImpl.getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	when(instance.lSuperUserMap.get(lUser.getId())).thenReturn(new TreeSet<User>());
	when(lDAPAuthenticatorImpl.getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);

	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(DelegateActivationMail.class)).thenReturn(mock(DelegateActivationMail.class));
	try {
	    instance.onActivateSuperUser(lUser, pUserSetting);
	} catch (Exception e) {

	}
    }

    /**
     * Test of onActivateDelegate method, of class DelegateHelper.
     */
    @Test
    public void testOnActivateDelegate() {

	DelegateHelper instance = new DelegateHelper();
	User lUser = DataWareHouse.getUser();
	UserSettings pUserSetting = DataWareHouse.getUserSettings();
	LDAPAuthenticatorImpl lDAPAuthenticatorImpl = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", lDAPAuthenticatorImpl);
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "lDelegationMap", mock(ConcurrentHashMap.class));

	when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	when(lDAPAuthenticatorImpl.getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	when(lDAPAuthenticatorImpl.getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(lUser.getId());
	root.add(new DefaultMutableTreeNode(lUser.getId()));
	when(instance.lDelegationMap.get(pUserSetting.getUserId())).thenReturn(root);
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(DelegateActivationMail.class)).thenReturn(mock(DelegateActivationMail.class));
	try {
	    instance.onActivateDelegate(lUser, pUserSetting);
	    when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(null);

	    instance.onActivateDelegate(lUser, pUserSetting);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of onDeActivateDelegate method, of class DelegateHelper.
     */
    @Test
    public void testOnDeActivateDelegate() {

	DelegateHelper instance = new DelegateHelper();
	User lUser = DataWareHouse.getUser();
	UserSettings pUserSetting = DataWareHouse.getUserSettings();
	LDAPAuthenticatorImpl lDAPAuthenticatorImpl = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", lDAPAuthenticatorImpl);
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "lDelegationMap", mock(ConcurrentHashMap.class));

	when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	ReflectionTestUtils.setField(instance, "lSuperUserMap", mock(ConcurrentHashMap.class));
	when(lDAPAuthenticatorImpl.getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	when(instance.lSuperUserMap.get(lUser.getId())).thenReturn(new TreeSet<User>());
	when(lDAPAuthenticatorImpl.getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);

	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(DelegateActivationMail.class)).thenReturn(mock(DelegateActivationMail.class));
	try {
	    instance.onDeActivateDelegate(lUser, pUserSetting);
	} catch (Exception e) {

	}
    }

    /**
     * Test of onAssignDelegate method, of class DelegateHelper.
     */
    @Test
    public void testOnAssignDelegate() {
	DelegateHelper instance = new DelegateHelper();
	User lUser = DataWareHouse.getUser();
	UserSettings pUserSetting = DataWareHouse.getUserSettings();
	LDAPAuthenticatorImpl lDAPAuthenticatorImpl = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", lDAPAuthenticatorImpl);
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "lDelegationMap", mock(ConcurrentHashMap.class));

	when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	when(lDAPAuthenticatorImpl.getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(DelegateActivationMail.class)).thenReturn(mock(DelegateActivationMail.class));
	instance.onAssignDelegate(lUser, pUserSetting);

    }

    /**
     * Test of populateDelegations method, of class DelegateHelper.
     */
    @Test
    public void testPopulateDelegations() {

	User lUser = DataWareHouse.getUser();
	DelegateHelper instance = new DelegateHelper();
	ReflectionTestUtils.setField(instance, "lDelegationMap", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));

	SortedSet<String> set = new TreeSet();
	set.add(lUser.getId());

	when(instance.lDAPAuthenticatorImpl.getUserDetails(lUser.getId())).thenReturn(lUser);
	// when(instance.getFromCache(lUser.getId())).thenReturn((SortedSet<String>)mutSet);
	instance.populateDelegations(lUser);
    }

    /**
     * Test of addToCache method, of class DelegateHelper.
     */
    @Test
    public void testAddToCache() {

	String from = "abc";
	String to = "123";
	DelegateHelper instance = new DelegateHelper();
	ReflectionTestUtils.setField(instance, "lDelegationMap", mock(ConcurrentHashMap.class));
	try {
	    instance.addToCache(from, to, true);
	    from = "";
	    to = "";
	    instance.addToCache(from, to, true);
	} catch (Exception e) {

	}
    }

    // /**
    // * Test of getFromCache method, of class DelegateHelper.
    @Test
    public void testGetFromCache() {
	DefaultMutableTreeNode defaultMutableTreeNode = mock(DefaultMutableTreeNode.class);

	ReflectionTestUtils.invokeMethod(instance, "getFromCache", defaultMutableTreeNode);

    }

    /*
     * Test of removeSuperUser method, of class DelegateHelper.
     */
    @Test
    public void testRemoveSuperUser() {
	DelegateHelper delegateHelper = spy(new DelegateHelper());
	delegateHelper.lSuperUserMap = mock(ConcurrentHashMap.class);
	User lUserSession = new User();
	lUserSession.setId("");
	delegateHelper.lSuperUserMap.remove(lUserSession.getId());

	delegateHelper.removeSuperUser(lUserSession);

    }

    /**
     * Test of removeFromCache method, of class DelegateHelper.
     */
    @Test
    public void testRemoveFromCache() {
	DelegateHelper delegateHelper = spy(new DelegateHelper());
	delegateHelper.lDelegationMap = mock(ConcurrentHashMap.class);
	String from = "";
	String to = "";
	DefaultMutableTreeNode lToUserNode = mock(DefaultMutableTreeNode.class);
	delegateHelper.lDelegationMap.get(to);
	int i = 1;

	try {
	    DefaultMutableTreeNode defaultMutableTreeNode = mock(DefaultMutableTreeNode.class);
	    lToUserNode.getChildAt(i);

	    delegateHelper.removeFromCache(from, to, true);

	} catch (Exception e) {
	}

    }

    /*
     * Test of populateSuperuser method, of class DelegateHelper.
     */
    @Test
    public void testPopulateSuperuser() {

	User pUser = DataWareHouse.user;
	DelegateHelper instance = new DelegateHelper();
	ReflectionTestUtils.setField(instance, "lSuperUserMap", mock(ConcurrentHashMap.class));
	TreeSet mySet = new TreeSet();
	mySet.add(pUser);
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	when(instance.lSuperUserMap.get(pUser.getId())).thenReturn(mySet);

	instance.populateSuperuser(pUser);
    }

    @Test
    public void testlogNode() {
	int X = 0;

	DefaultMutableTreeNode defaultMutableTreeNode = mock(DefaultMutableTreeNode.class);
	defaultMutableTreeNode.getChildAt(X);
	ReflectionTestUtils.invokeMethod(instance, "logNode", X, defaultMutableTreeNode);

    }

}

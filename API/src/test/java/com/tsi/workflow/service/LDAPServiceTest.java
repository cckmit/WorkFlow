/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.helper.DelegateHelper;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class LDAPServiceTest {

    LDAPService instance;

    public LDAPServiceTest() {
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
	    LDAPService realInstance = new LDAPService();
	    instance = spy(realInstance);

	} catch (Exception ex) {
	    Logger.getLogger(LDAPServiceTest.class.getName()).log(Level.SEVERE, null, ex);
	    // fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLDAPService() throws Exception {
	TestCaseExecutor.doTest(instance, LDAPService.class);
    }

    /**
     * Test of getUsersByRole method, of class LDAPService.
     */
    @Test
    public void testGetUsersByRole() {
	instance = spy(new LDAPService());
	String role = Constants.UserGroup.Lead.name();
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));

	Map<String, List<LDAPGroup>> roles = new HashMap();
	roles.put(Constants.UserGroup.Lead.name(), Arrays.asList(new LDAPGroup(role, role, role, Constants.UserGroup.Lead)));
	roles.put(Constants.UserGroup.DevManager.name(), Arrays.asList(new LDAPGroup(role, role, role, Constants.UserGroup.Lead)));
	// when(instance.getLdapGroupConfig().getLdapRolesMap()).thenReturn(roles);
	SortedSet<User> users = new TreeSet<>();
	User usr = new User("test");
	usr.setDisplayName("test");
	users.add(usr);
	// when(instance.getLDAPAuthenticatorImpl().getLinuxUsers(null)).thenReturn(users);
	// when(instance.getLDAPAuthenticatorImpl().getLinuxUsers(Arrays.asList(new
	// LDAPGroup(role, role, role,
	// Constants.UserGroup.Lead)))).thenReturn(users);

	JSONResponse result = instance.getUsersByRole(role);
	when(instance.getLdapGroupConfig().getLdapRolesMap()).thenReturn(roles);
	instance.setLdapGroupConfig(mock(LdapGroupConfig.class));
	instance.setLDAPAuthenticatorImpl(mock(LDAPAuthenticatorImpl.class));
	instance.setGITConfig(mock(GITConfig.class));
	instance.setUserSettingsDAO(mock(UserSettingsDAO.class));

	Assert.assertNotNull(result);
    }

    /**
     * Test of doLogin method, of class LDAPService.
     */
    @Test
    public void testDoLogin() {
	instance = new LDAPService();

	User pUser = DataWareHouse.user;
	pUser.setTimeZone(TimeZone.getDefault().getID());
	String pUserName = DataWareHouse.user.getId();
	String pSecret = "abcd";
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	LDAPAuthenticatorImpl lDAPAuthenticatorImpl = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", lDAPAuthenticatorImpl);
	// ReflectionTestUtils.setField(instance, "lSessionValidator",
	// mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	when(instance.getGITConfig().getServiceUserID()).thenReturn(pUser.getId());
	when(instance.userSettingsDAO.find(pUser.getId(), Constants.UserSettings.DELEGATION.name())).thenReturn(DataWareHouse.getUserSettings());
	Constants.LoginErrorCode err = Constants.LoginErrorCode.SUCCESS;
	try {
	    when(instance.lDAPAuthenticatorImpl.validate(pUser, pSecret)).thenReturn(err);
	    instance.doLogin(pUserName, pSecret);
	    err = Constants.LoginErrorCode.WRONG_USER_NAME_OR_PASSWORD;
	    when(instance.lDAPAuthenticatorImpl.validate(pUser, pSecret)).thenReturn(err);
	    instance.doLogin(pUserName, pSecret);
	} catch (Exception e) {

	}

    }

    // /**
    // * Test of doSSOAuthentication method, of class LDAPService.
    // */
    // @Test
    // public void testDoSSOAuthentication() {
    //
    // HttpServletRequest request = mock(HttpServletRequest.class);
    // LDAPService instance = new LDAPService();
    // User expResult = DataWareHouse.user;
    // User result = instance.doSSOAuthentication(request);
    // assertEquals(expResult, result);
    // }
    /**
     * Test of switchDelegate method, of class LDAPService.
     */
    @Test
    public void testSwitchDelegate() {

	User pUser = DataWareHouse.user;
	String changeToUser = "Test";
	Boolean force = true;
	LDAPService instance = new LDAPService();
	ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	JSONResponse result = instance.switchDelegate(pUser, changeToUser, force);

    }
    //
    // /**
    // * Test of removeSuperUser method, of class LDAPService.
    // */
    // @Test
    // public void testRemoveSuperUser() {
    //
    // User lUserSession = null;
    // LDAPService instance = new LDAPService();
    // instance.removeSuperUser(lUserSession);
    // // TODO review the generated test code and remove the default call to fail.
    // fail("The test case is a prototype.");
    // }
}

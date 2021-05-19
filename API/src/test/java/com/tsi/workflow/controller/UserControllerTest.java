/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.ui.LoginForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.service.LDAPService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class UserControllerTest {

    public UserControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    UserController instance;

    @Before
    public void setUp() {
	try {
	    UserController realInstance = new UserController();
	    instance = spy(realInstance);
	    // TestCaseMockService.doMockController(instance, UserController.class);
	} catch (Exception ex) {
	    Logger.getLogger(UserControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testUserController() throws Exception {
	TestCaseExecutor.doTest(instance, UserController.class);
    }

    /**
     * Test of login method, of class UserController.
     */
    @Test
    public void testLogin() {

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	LoginForm loginForm = new LoginForm();
	loginForm.setPassword("");
	loginForm.setUsername("");
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lLDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lDAPService", mock(LDAPService.class));
	JSONResponse expResult = null;
	JSONResponse result = instance.login(request, response, loginForm);
	assertEquals(expResult, result);
	;
    }

    /**
     * Test of ssoLogin method, of class UserController.
     */
    // @Test
    // public void testSsoLogin() {
    // JSONResponse lResponse = new JSONResponse();
    // HttpServletRequest request = null;
    // HttpServletResponse response = null;
    // when(instance.getCurrentUser(null,
    // null)).thenReturn(DataWareHouse.getUser());
    // when(instance.ssoLogin(request, response)).thenReturn(lResponse);
    // }
    /**
     * Test of logout method, of class UserController.
     */
    @Test(expected = NullPointerException.class)
    public void testLogout() {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	BaseController instanceBase = new BaseController() {
	};
	// Mockito.when(request.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("Authtest");
	Mockito.when(request.getHeader("Authorization")).thenReturn("Manualtest");
	try {
	    instanceBase.removeCurrentUser(request, response);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.logout(request, response)).thenReturn(lResponse);

    }

    /**
     * Test of setRole method, of class UserController.
     */
    @Test
    public void testSetRole() {

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	String role = "";
	UserController instance = new UserController();
	JSONResponse expResult = new JSONResponse();
	JSONResponse result = instance.setRole(request, response, role);
	assertNotNull(result);
    }

    /**
     * Test of setDelegate method, of class UserController.
     */
    @Test
    public void testSetDelegate() {

	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	String userId = "deepa.jayakumar";
	Boolean force = null;
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lLDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lDAPService", mock(LDAPService.class));
	JSONResponse result = instance.setDelegate(request, response, userId, force);
	assertEquals(expResult, result);
    }

    /**
     * Test of setLogLevel method, of class UserController.
     */
    @Test
    public void testSetLogLevel() {

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	String level = "";
	UserController instance = new UserController();
	instance.setLogLevel(request, response, level);
    }

    /**
     * Test of getDelegationToUsersList method, of class UserController.
     */
    @Test
    public void testGetDelegationToUsersList() {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lLDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lDAPService", mock(LDAPService.class));
	LDAPGroup grp = new LDAPGroup("", "", "", Constants.UserGroup.Lead);
	when(instance.ldapGroupConfig.getDelegateGroupsUsersList()).thenReturn(new List[] { Arrays.asList(grp) });
	JSONResponse result = instance.getDelegationToUsersList(request, response);
	assertNotNull(result);
    }

    /**
     * Test of getDelegationFromUsersList method, of class UserController.
     */
    @Test
    public void testGetDelegationFromUsersList() {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lLDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lDAPService", mock(LDAPService.class));
	LDAPGroup grp = new LDAPGroup("", "", "", Constants.UserGroup.Lead);
	when(instance.ldapGroupConfig.getDelegateGroupsRoleList()).thenReturn(new List[] { Arrays.asList(grp) });
	JSONResponse result = instance.getDelegationFromUsersList(request, response);
	assertNotNull(result);
    }

    /**
     * Test of allowLoadAnyTime method, of class UserController.
     */
    @Test
    public void testAllowLoadAnyTime() {

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	boolean loadAnyTime = false;
	UserController instance = new UserController();
	instance.allowLoadAnyTime(request, response, loadAnyTime);
    }

    @Test
    public void testGetSuperUserFormUsersList() {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lLDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lDAPService", mock(LDAPService.class));
	LDAPGroup grp = new LDAPGroup("", "", "", Constants.UserGroup.Lead);
	when(instance.ldapGroupConfig.getSuperUserGroupsRoleList()).thenReturn(new List[] { Arrays.asList(grp) });
	JSONResponse result = instance.getSuperUserFromUsersList(request, response);
	assertNotNull(result);
    }

    @Test
    public void testGetSuperUserToUsersList() {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lLDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lDAPService", mock(LDAPService.class));
	LDAPGroup grp = new LDAPGroup("", "", "", Constants.UserGroup.Lead);
	when(instance.ldapGroupConfig.getSuperUserGroupsUsersList()).thenReturn(new List[] { Arrays.asList(grp) });
	JSONResponse result = instance.getSuperUserToUsersList(request, response);
	assertNotNull(result);
    }

    @Test
    public void testallowAllUser1s() {

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	UserController instance = new UserController();
	instance.allowAllUsers(request, response, 1);
    }

}

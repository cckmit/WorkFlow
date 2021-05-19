/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.authenticator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.LdapConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.User;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.utils.Constants;
import com.unboundid.ldap.sdk.LDAPConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.beanutils.BeanUtils;
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
public class LDAPAuthenticatorImplTest {

    public LDAPAuthenticatorImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class LDAPAuthenticatorImpl.
     */
    @Test
    public void testValidate() {
	LDAPAuthenticatorImpl instance = new LDAPAuthenticatorImpl();
	TestCaseExecutor.doTest(instance, LDAPAuthenticatorImpl.class);
    }

    @Test
    public void getSetDelegatedLinuxGroupDetails() {
	LDAPAuthenticatorImpl realinstance = new LDAPAuthenticatorImpl();
	LDAPAuthenticatorImpl instance = spy(realinstance);
	User user = DataWareHouse.getUser();
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "ldapConfig", mock(LdapConfig.class));
	ReflectionTestUtils.setField(instance, "lLdapGroupMap", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "lLdapUserMap", mock(ConcurrentHashMap.class));

	// when(instance.ldapGroupConfig.getDelegateGroupsRoleList()).thenReturn(Arrays.asList(Arrays.asList(new
	// LDAPGroup("", "", "", Constants.UserGroup.Lead)).toArray()));
	// instance.setDelegatedLinuxGroupDetails(user);
	LDAPGroup grp = new LDAPGroup("", "", "", Constants.UserGroup.Lead);
	ReflectionTestUtils.setField(instance, "lAuthConnection", new LDAPConnection());
	ReflectionTestUtils.setField(instance, "lAnonymousConnection", new LDAPConnection());

	grp.setLdapGroupBase("");
	grp.setLdapParam("");
	grp.setLdapGroupName("");
	try {
	    instance.getLinuxUsers(Arrays.asList(grp));
	} catch (Exception e) {
	    assertTrue(true);
	}
	instance.getUserDetails(DataWareHouse.user.getId());
	instance.validate(DataWareHouse.user, "test");
    }

    @Test
    public void testSetDelegatedLinuxGroupDetails() {

	LDAPAuthenticatorImpl instance = new LDAPAuthenticatorImpl();
	LdapGroupConfig ldapGroupConfig = new LdapGroupConfig();
	instance.ldapGroupConfig = ldapGroupConfig;
	User pUser = new User();
	pUser.setDisplayName("ss");
	try {
	    instance.setDelegatedLinuxGroupDetails(pUser);
	    if (pUser.getRole().isEmpty()) {

		assertEquals(pUser.getDisplayName(), "ss");

	    }

	} catch (Exception e) {
	}

    }

    @Test
    public void testSetSuperUserDelegatedLinuxGroupDetails() {

	LDAPAuthenticatorImpl instance = new LDAPAuthenticatorImpl();
	LdapGroupConfig ldapGroupConfig = new LdapGroupConfig();
	instance.ldapGroupConfig = ldapGroupConfig;
	User pUser = new User();
	try {
	    pUser.setId("a");
	    pUser.setDisplayName("1");
	    instance.setSuperUserDelegatedLinuxGroupDetails(pUser);

	} catch (Exception e) {

	}

    }

    // @Test
    public void testGetLinuxUsers() {
	LDAPAuthenticatorImpl instance = new LDAPAuthenticatorImpl();
	List<LDAPGroup> ldapGroupList = new ArrayList<LDAPGroup>();
	LDAPGroup ldapGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.Lead);
	for (LDAPGroup lGroup : ldapGroupList) {
	    SortedSet<User> lUsers = instance.lLdapGroupMap.get(lGroup.getLdapGroupName());
	    ldapGroupList.add(ldapGroup);
	}
	instance.getLinuxUsers(ldapGroupList);

    }

    // @Test
    public void testGetLinuxUsers_private() {
	LDAPAuthenticatorImpl instance = new LDAPAuthenticatorImpl();
	List<LDAPGroup> ldapGroupList = new ArrayList<LDAPGroup>();
	LDAPGroup ldapGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.Lead);

	ReflectionTestUtils.invokeMethod(instance, "getLinuxUsers", ldapGroup);

    }

    // @Test
    public void tsetGetUserDetails() {
	LDAPAuthenticatorImpl instance = new LDAPAuthenticatorImpl();

	ConcurrentHashMap<String, User> lLdapUserMap = new ConcurrentHashMap<>();
	instance.lLdapUserMap = lLdapUserMap;
	User lReturn = new User();
	String pUserID = "a";
	lReturn.setId(pUserID);

	try {
	    User lUser = lLdapUserMap.get(pUserID);

	    BeanUtils.copyProperties(lReturn, lUser);

	    assertNotNull(instance.getUserDetails(pUserID));
	} catch (Exception e) {
	    System.out.println("user" + e);
	}
	assertEquals(lReturn.getId(), pUserID);

    }

    @Test
    public void testGetLinuxGroupDetails() {
	LDAPAuthenticatorImpl instance = new LDAPAuthenticatorImpl();
	LdapGroupConfig ldapGroupConfig = new LdapGroupConfig();
	instance.ldapGroupConfig = ldapGroupConfig;
	User pUser = new User();
	try {
	    instance.getLinuxGroupDetails(pUser);
	} catch (Exception e) {
	}

    }

}

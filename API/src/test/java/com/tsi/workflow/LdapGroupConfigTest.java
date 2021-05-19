/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.tsi.workflow.ldap.config.LDAPGroup;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class LdapGroupConfigTest {

    public LdapGroupConfigTest() {
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
     * Test of getLdapRolesMap method, of class LdapGroupConfig.
     */
    @Test
    public void testGetLdapRolesMap() {

	LdapGroupConfig instance = new LdapGroupConfig();
	Map<String, List<LDAPGroup>> expResult = null;
	Map<String, List<LDAPGroup>> result = instance.getLdapRolesMap();
	assertNotNull(result);

    }

    /**
     * Test of setLdapRolesMap method, of class LdapGroupConfig.
     */
    @Test
    public void testSetLdapRolesMap() throws IllegalAccessException {

	Map<String, List<LDAPGroup>> ldapRolesMap = null;
	LdapGroupConfig instance = new LdapGroupConfig();
	instance.setLdapRolesMap(ldapRolesMap);
	assertNull(instance.getLdapRolesMap());

    }

    /**
     * Test of getLdapCNMap method, of class LdapGroupConfig.
     */
    @Test
    public void testGetLdapCNMap() {

	LdapGroupConfig instance = new LdapGroupConfig();
	Map<String, List<LDAPGroup>> expResult = null;
	Map<String, List<LDAPGroup>> result = instance.getLdapCNMap();
	assertNotNull(result);

    }

    /**
     * Test of setLdapCNMap method, of class LdapGroupConfig.
     */
    @Test
    public void testSetLdapCNMap() {

	Map<String, List<LDAPGroup>> ldapCNMap = null;
	LdapGroupConfig instance = new LdapGroupConfig();
	instance.setLdapCNMap(ldapCNMap);
	assertNull(instance.getLdapCNMap());
    }

    /**
     * Test of getDeveloperGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetDeveloperGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getDeveloperGroups();
	assertNotNull(result);

    }

    /**
     * Test of getLeadGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetLeadGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getLeadGroups();
	assertNotNull(result);

    }

    /**
     * Test of getReviewerGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetReviewerGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getReviewerGroups();
	assertNotNull(result);

    }

    /**
     * Test of getDevManagerGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetDevManagerGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getDevManagerGroups();
	assertNotNull(result);

    }

    /**
     * Test of getLoadsControlGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetLoadsControlGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getLoadsControlGroups();
	assertNotNull(result);

    }

    /**
     * Test of getQAGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetQAGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getQAGroups();
	assertNotNull(result);

    }

    /**
     * Test of getSystemSupportGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetSystemSupportGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getSystemSupportGroups();
	assertNotNull(result);

    }

    /**
     * Test of getServiceDeskGroups method, of class LdapGroupConfig.
     */
    @Test
    public void testGetServiceDeskGroups() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List<LDAPGroup> expResult = null;
	List<LDAPGroup> result = instance.getServiceDeskGroups();
	assertNotNull(result);

    }

    /**
     * Test of getGroupsList method, of class LdapGroupConfig.
     */
    @Test
    public void testGetGroupsList() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List[] expResult = null;
	try {
	    List[] result = instance.getGroupsList();
	    assertNotNull(result);
	} catch (Exception e) {

	}
    }

    /**
     * Test of getDelegateGroupsUsersList method, of class LdapGroupConfig.
     */
    @Test
    public void testGetDelegateGroupsUsersList() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List[] expResult = null;
	try {
	    List[] result = instance.getDelegateGroupsUsersList();
	    assertNotNull(result);
	} catch (Exception e) {

	}
    }

    /**
     * Test of getDelegateGroupsRoleList method, of class LdapGroupConfig.
     */
    @Test
    public void testGetDelegateGroupsRoleList() {

	LdapGroupConfig instance = new LdapGroupConfig();
	List[] expResult = null;
	try {
	    List[] result = instance.getDelegateGroupsRoleList();
	    assertNotNull(result);
	} catch (Exception e) {

	}
    }

}

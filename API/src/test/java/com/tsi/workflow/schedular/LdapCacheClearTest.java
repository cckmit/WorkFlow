/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.helper.DelegateHelper;
import com.tsi.workflow.helper.FallbackHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class LdapCacheClearTest {

    public LdapCacheClearTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    LdapCacheClear instance;

    @Before
    public void setUp() {
	try {
	    LdapCacheClear realInstance = new LdapCacheClear();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, UserSettingsDAO.class);
	    TestCaseMockService.doMockDAO(instance, DelegateHelper.class);
	    // TestCaseMockService.doMockDAO(instance, ConcurrentHashMap.class);

	} catch (Exception ex) {
	    Logger.getLogger(LdapCacheClearTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAll() {
	TestCaseExecutor.doTest(instance, FallbackHelper.class);
    }

    /**
     * Test of getDelegateHelper method, of class LdapCacheClear.
     */
    @Test
    public void testGetDelegateHelper() {

	LdapCacheClear instance = new LdapCacheClear();
	DelegateHelper expResult = null;
	DelegateHelper result = instance.getDelegateHelper();

    }

    /**
     * Test of getUserSettingsDAO method, of class LdapCacheClear.
     */
    @Test
    public void testGetUserSettingsDAO() {

	LdapCacheClear instance = new LdapCacheClear();
	UserSettingsDAO expResult = null;
	UserSettingsDAO result = instance.getUserSettingsDAO();

    }

    /**
     * Test of clearCache method, of class LdapCacheClear.
     */
    @Test
    public void testClearCache() {

	// LdapCacheClear instance = new LdapCacheClear();
	ReflectionTestUtils.setField(instance, "lLdapUserMap", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "lLdapGroupMap", mock(ConcurrentHashMap.class));
	instance.clearCache();

    }

    /**
     * Test of populateDelegationCache method, of class LdapCacheClear.
     */
    @Test
    public void testPopulateDelegationCache() {
	LdapCacheClear instance = new LdapCacheClear();
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	UserSettings userSettings = new UserSettings();
	userSettings.setUserId("userid");
	userSettings.setValue("value");
	List<UserSettings> list = new ArrayList();
	list.add(userSettings);

	when(instance.userSettingsDAO.findAllActiveDelegations()).thenReturn(list);
	// when(instance.delegateHelper.addToCache("userid",
	// "value")).thenReturn(false);
	when(instance.getDelegateHelper().addToCache(userSettings.getUserId(), userSettings.getValue(), true)).thenReturn(false);
	instance.populateDelegationCache();

    }

}

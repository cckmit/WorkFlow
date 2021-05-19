package com.tsi.workflow.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.hazelcast.core.HazelcastInstance;
import com.tsi.workflow.CacheConfig;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.utils.JSONResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class ToolAdminServiceTest {

    ToolAdminService instance;

    @Mock
    private CacheClient cacheClient;

    @Mock
    private CacheConfig cacheConfig;

    @Mock
    private HazelcastInstance hazelcastInstance;

    public ToolAdminServiceTest() {
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
	    ToolAdminService realInstance = new ToolAdminService();
	    instance = spy(realInstance);
	    cacheConfig = new CacheConfig();
	    cacheClient = new CacheClient();
	} catch (Exception ex) {
	    Logger.getLogger(ToolAdminService.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testToolAdminService() throws Exception {
	TestCaseExecutor.doTest(instance, ToolAdminService.class);
    }

    @Test
    public void testgetRepoList() {
	try {

	    ToolAdminService realInstance = new ToolAdminService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, CacheClient.class);
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setData(DataWareHouse.getRepositoryDetails());
	    lResponse.setStatus(Boolean.TRUE);
	    ReflectionTestUtils.setField(instance, "cacheClient", mock(CacheClient.class));
	    Mockito.when(instance.getCacheClient().getFilteredRepositoryMap().values()).thenReturn((Collection<RepositoryView>) new RepositoryView());
	    when(instance.getRepoList(1, 1)).thenReturn(lResponse);
	} catch (Exception ex) {
	}

    }

    @Test
    public void testgetRepoList1() {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setData(DataWareHouse.getRepositoryDetails());
	    lResponse.setStatus(Boolean.TRUE);
	    ReflectionTestUtils.setField(instance, "cacheClient", mock(CacheClient.class));
	    RepositoryView sRepo = new RepositoryView();
	    sRepo.setDefaultAccess("RWC");
	    sRepo.setRepository(DataWareHouse.getRepositoryDetails());
	    Collection<RepositoryView> repo = Collections.singletonList(sRepo);
	    cacheConfig.setCacheServer(" ENC(0FA5D7BFF6664B7044E6158AC5D42DD4063200AF67A1F198FE9E2386CAA7533041619DA1A9E714A8)");
	    cacheConfig.setCachePort(8451);
	    Mockito.when(instance.getCacheClient().getCacheInstance()).thenReturn(hazelcastInstance);
	    when(instance.getRepoList(1, 1)).thenReturn(lResponse);
	} catch (Exception e) {

	}
    }

    // @Test
    public void testsetRepositoryOwners() {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setData(DataWareHouse.getRepositoryDetails());
	    lResponse.setStatus(Boolean.TRUE);
	    ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	    ReflectionTestUtils.setField(instance, "cacheClient", mock(CacheClient.class));
	    ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	    ReflectionTestUtils.setField(instance, "gitBlitClientUtils", mock(GitBlitClientUtils.class));
	    RepositoryView repo = new RepositoryView();
	    repo.setRepository(DataWareHouse.getRepositoryDetails());
	    repo.setDefaultAccess("RWC");
	    when(instance.setRepositoryOwners(DataWareHouse.getUser(), repo)).thenReturn(lResponse);
	} catch (Exception ex) {
	}

    }

}

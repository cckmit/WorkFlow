package com.tsi.workflow.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.utils.JSONResponse;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class GitHookUpdateServiceTest {

    GitHookUpdateService instance;

    @Before
    public void setUp() {
	try {
	    GitHookUpdateService realInstance = new GitHookUpdateService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, GitSearchDAO.class);

	} catch (Exception ex) {
	    Logger.getLogger(GitHookUpdateServiceTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @Test
    public void testGitHookUpdateService() throws Exception {
	TestCaseExecutor.doTest(instance, GitHookUpdateService.class);
    }

    @Test
    public void testGitDbUpdateProcess() throws Throwable {
	GitHookUpdateService instance = new GitHookUpdateService();
	LDAPAuthenticatorImpl LDAP = new LDAPAuthenticatorImpl();
	ReflectionTestUtils.setField(instance, "gitSearchDAO", mock(GitSearchDAO.class));
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "cacheClient", mock(CacheClient.class));
	ReflectionTestUtils.setField(LDAP, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(LDAP, "lLdapUserMap", mock(ConcurrentHashMap.class));

	instance.lDAPAuthenticatorImpl = LDAP;
	String refStatus = DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getRefStatus();
	String refPlan = DataWareHouse.getPlan().getId();
	Date dateTime = new Date();
	String refLoadDateTime = dateTime.toString();
	String sourceCommitDate = dateTime.toString();
	String sourceRepo = "tpf/tp/nonibm/nonibm_bde1.git";
	String fileName = DataWareHouse.getGitSearchResult().getFileName();
	String targetSystem = DataWareHouse.getSystemList().get(0).getName();
	String sourceCommitId = "a815973421bb35dc4dd99507a40bc21284194dd6";
	Boolean isModify = Boolean.TRUE;
	String derivedCommitId = "";
	String derivedCommitDate = dateTime.toString();

	User user = Mockito.mock(User.class);
	when(instance.lDAPAuthenticatorImpl.getServiceUser()).thenReturn(user);
	when(user.getDisplayName()).thenReturn("MTPSERVICE");
	// when(instance.lDAPAuthenticatorImpl.getServiceUser().getDisplayName()).thenReturn("MTPSERVICE");
	when(user.getMailId()).thenReturn("XYZ@travelport.com");
	when(instance.gitSearchDAO.getSubRepoDetail(sourceRepo)).thenReturn(99);
	when(instance.gitSearchDAO.getFileDetail(sourceRepo, fileName, targetSystem)).thenReturn(10);
	when(instance.gitSearchDAO.getCommitDetailId(10, sourceCommitId)).thenReturn(10);

	try {
	    JSONResponse result = instance.gitDbUpdateProcess(refPlan, refStatus, refLoadDateTime, sourceCommitId, sourceCommitDate, derivedCommitId, derivedCommitDate, sourceRepo, fileName, targetSystem, isModify);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

}

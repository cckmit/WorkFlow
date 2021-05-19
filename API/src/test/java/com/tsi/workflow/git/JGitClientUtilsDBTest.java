package com.tsi.workflow.git;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.GitProdSearchDb;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.PRODSearchType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class JGitClientUtilsDBTest {

    JGitClientUtilsDB instance;

    @Before
    public void setUp() {
	try {
	    JGitClientUtilsDB realInstance = new JGitClientUtilsDB();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, GitSearchDAO.class);
	    TestCaseMockService.doMockDAO(instance, PutLevelDAO.class);
	} catch (Exception ex) {
	    Logger.getLogger(JGitClientUtilsDBTest.class.getName()).log(Level.SEVERE, null, ex);
	    // fail("Fail on Exception " + ex.getMessage());
	}
    }

    @Test
    public void test() {
	// JGitClientUtilsDB instance = new JGitClientUtilsDB();
	ReflectionTestUtils.setField(instance, "gitSearchDAO", mock(GitSearchDAO.class));
	// ReflectionTestUtils.setField(instance, "gITHelper", mock(GITHelper.class));
	// ReflectionTestUtils.setField(instance, "constants", mock(Constants.class));
	// ReflectionTestUtils.setField(instance, "gitProdSearchDb",
	// mock(GitProdSearchDb.class));
	// ReflectionTestUtils.setField(instance, "gitSearchResult",
	// mock(GitSearchResult.class));
	// ReflectionTestUtils.setField(instance, "gitBranchSearchResult",
	// mock(GitBranchSearchResult.class));

	GITHelper lgITHelper = Mockito.mock(GITHelper.class);
	instance.gitHelper = lgITHelper;
	String pCompany = DataWareHouse.getSystemList().get(0).getPlatformId().getNickName();
	String pFileFilter = "abcd.asm";
	Boolean pMacroHeader = DataWareHouse.getPlan().getMacroHeader();
	PRODSearchType pPendingStatusReq = PRODSearchType.ONLINE_ONLY;
	Collection<String> pAllowedRepos = new ArrayList<String>();
	Collection<GitSearchResult> value = new ArrayList<GitSearchResult>();
	GitSearchResult gitSearchResult = DataWareHouse.getGitSearchResult();
	value.add(gitSearchResult);
	List<String> putLevelStatus = new ArrayList<String>();
	putLevelStatus.add(Constants.PUTLevelOptions.PRODUCTION.name());
	HashMap<String, String> tarSysAndPutLevel = new HashMap<String, String>();

	List<String> systems = new ArrayList<String>();
	systems.add(DataWareHouse.getSystemList().get(0).getName());
	Set<String> systemAndFuncBasedFiles = new HashSet<String>();
	systemAndFuncBasedFiles.add("");

	List<GitProdSearchDb> gitProdSearchDb = new ArrayList<GitProdSearchDb>();
	GitProdSearchDb gitlist = Mockito.mock(GitProdSearchDb.class);
	gitProdSearchDb.add(gitlist);

	try {
	    when(instance.gitHelper.getSystemBasedPutLevels(putLevelStatus)).thenReturn(tarSysAndPutLevel);
	    when(instance.SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq, pAllowedRepos)).thenReturn(value);
	    when(instance.SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq, pAllowedRepos, systems)).thenReturn(value);
	    when(instance.getGitSearchDAO().findByProgramName(pCompany, pFileFilter, pPendingStatusReq, pAllowedRepos, systems)).thenReturn(gitProdSearchDb);
	    when(instance.SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq, pAllowedRepos, systems, systemAndFuncBasedFiles)).thenReturn(value);

	} catch (IOException e) {
	    assertTrue(true);
	}

    }

}

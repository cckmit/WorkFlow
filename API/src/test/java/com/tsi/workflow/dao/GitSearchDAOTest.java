package com.tsi.workflow.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.GitProdSearchDb;
import com.tsi.workflow.beans.ui.GitUserModel;
import com.tsi.workflow.beans.ui.GitUsers;
import com.tsi.workflow.beans.ui.RepoBasedSrcArtifacts;
import com.tsi.workflow.beans.ui.SystemBasedSrcArtifacts;
import com.tsi.workflow.beans.ui.UserDetails;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.PRODSearchType;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class GitSearchDAOTest {

    GitSearchDAO instance;

    @Before

    public void setUp() throws Exception {

	GitSearchDAO GitSearchDAO = new GitSearchDAO();
	instance = Mockito.spy(GitSearchDAO);
	TestCaseMockService.doMockBaseDAO(GitSearchDAO.class, GitProdSearchDb.class, instance);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGitSearchDAO() {
	TestCaseExecutor.doTestDAO(instance, GitSearchDAO.class);
    }

    @Test
    public void testOldFindByProgramName() {

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(SQLQuery.class);

	Collection<String> pAllowedRepos = new ArrayList<>();
	pAllowedRepos.add("Response");
	PRODSearchType pPendingStatusReq = PRODSearchType.BOTH;
	String query = "SELECT a FROM GitProdSearchDb a WHERE a.programName LIKE (:filterName)" + " AND a.company = :companyName" + " AND a.refStatus in (:status)";
	if (pAllowedRepos != null && !pAllowedRepos.isEmpty()) {
	    query = query + " AND a.subSourceRepo in (:sourceRepos)";
	}

	List<String> status = new ArrayList<>();
	status.add("Online");
	status.add("Pending");
	status.add("Fallback");

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("filterName", "filterName" + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameter("companyName", "companyName")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", status)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("sourceRepos", pAllowedRepos)).thenReturn(mockedQry);
	instance.oldFindByProgramName("companyName", "filterName", PRODSearchType.BOTH, pAllowedRepos);

    }

    @Test
    public void testOldFindByProgramName1() {

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(SQLQuery.class);

	Collection<String> pAllowedRepos = new ArrayList<>();
	pAllowedRepos.add("Response");
	PRODSearchType pPendingStatusReq = PRODSearchType.BOTH;
	String query = "SELECT a FROM GitProdSearchDb a WHERE a.programName LIKE (:filterName)" + " AND a.company = :companyName" + " AND a.refStatus in (:status)";
	if (pAllowedRepos != null && !pAllowedRepos.isEmpty()) {
	    query = query + " AND a.subSourceRepo in (:sourceRepos)";
	}

	List<String> status = new ArrayList<>();
	status.add("Online");
	status.add("Fallback");

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("filterName", "filterName" + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameter("companyName", "companyName")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", status)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("sourceRepos", pAllowedRepos)).thenReturn(mockedQry);
	instance.oldFindByProgramName("companyName", "filterName", PRODSearchType.ONLINE_ONLY, pAllowedRepos);

    }

    @Test
    public void testOldFindByProgramName2() {

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(SQLQuery.class);

	Collection<String> pAllowedRepos = new ArrayList<>();
	pAllowedRepos.add("Response");
	PRODSearchType pPendingStatusReq = PRODSearchType.BOTH;
	String query = "SELECT a FROM GitProdSearchDb a WHERE a.programName LIKE (:filterName)" + " AND a.company = :companyName" + " AND a.refStatus in (:status)";
	if (pAllowedRepos != null && !pAllowedRepos.isEmpty()) {
	    query = query + " AND a.subSourceRepo in (:sourceRepos)";
	}

	List<String> status = new ArrayList<>();
	status.add("Pending");

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("filterName", "filterName" + "%")).thenReturn(mockedQry);
	when(mockedQry.setParameter("companyName", "companyName")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", status)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("sourceRepos", pAllowedRepos)).thenReturn(mockedQry);
	instance.oldFindByProgramName("companyName", "filterName", PRODSearchType.PENDING_ONLY, pAllowedRepos);

    }

    @Test
    public void testGetSubRepoDetail() {
	String query = "SELECT id FROM git.sub_repo_detail WHERE sub_source_repo =:subRepoName";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("subRepoName", "subRepoName")).thenReturn(mockedQry);
	when(mockedQry.uniqueResult()).thenReturn(new Integer(1));
	instance.getSubRepoDetail("subRepoName");

	session.close();
	sessionFactory.close();

    }

    @Test
    public void testGetFileDetaill() {
	String query = "SELECT a.id FROM git.repo_file_list a, git.sub_repo_detail b WHERE a.sub_repo_id = b.id " + " and a.file_name = :fileName" + " and a.target_system = :targetSystem" + " and	b.sub_source_repo = :subRepoName" + " and a.is_deleted='N'";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileName", "fileName")).thenReturn(mockedQry);
	when(mockedQry.setParameter("targetSystem", "targetSystem")).thenReturn(mockedQry);
	when(mockedQry.setParameter("subRepoName", "subRepoName")).thenReturn(mockedQry);
	when(mockedQry.uniqueResult()).thenReturn(new Integer(1));
	instance.getFileDetail("subRepoName", "fileName", "targetSystem");

	session.close();
	sessionFactory.close();

    }

    @Test
    public void testUpdateFileListDeleteFlag() {
	String query = "update git.repo_file_list set is_deleted=:deleteFlag, modified_dt=now() where id= :fileId";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("deleteFlag", "Y")).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileId", new Integer(1))).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.updateFileListDeleteFlag(new Integer(1), Boolean.TRUE);

	session.close();
	sessionFactory.close();

    }

    @Test
    public void testSaveNewFileList() {
	String query = "INSERT INTO GIT.REPO_FILE_LIST(SUB_REPO_ID, FILE_NAME, PROGRAM_NAME, FILE_EXT, TARGET_SYSTEM, is_deleted, created_dt) VALUES ( :subRepoId, :fileName, :progName, :fileExt, :targetSystem,'N', now())";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("subRepoId", new Integer(1))).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileName", "fileName")).thenReturn(mockedQry);
	when(mockedQry.setParameter("progName", FilenameUtils.getName("fileName"))).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileExt", FilenameUtils.getExtension("fileName"))).thenReturn(mockedQry);
	when(mockedQry.setParameter("targetSystem", "targetSystem")).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.saveNewFileList(new Integer(1), "fileName", "targetSystem");

	session.close();
	sessionFactory.close();

    }

    @Test
    public void testSaveCommitDetail() {
	String query = "INSERT INTO GIT.REPO_COMMIT(SUB_REPO_ID, FILE_ID, SOURCE_COMMIT_ID, COMMITTER_NAME, COMMITTER_MAIL_ID, COMMIT_DATE_TIME, REF_PLAN, REF_STATUS, REF_LOAD_DATE_TIME, FILE_HASHCODE, CREATED_DT) VALUES(:subRepoId,:fileId,:srcCommitId,:committerName,:committerMailId,:commitDateTime,:refPlan,:refStatus,:refLoadDateTime,:fileHashCode, now())";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("subRepoId", new Integer(1))).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileId", new Integer(1))).thenReturn(mockedQry);
	when(mockedQry.setParameter("srcCommitId", "srcCommitId")).thenReturn(mockedQry);
	when(mockedQry.setParameter("committerName", "committerName")).thenReturn(mockedQry);
	when(mockedQry.setParameter("committerMailId", "committerMailId")).thenReturn(mockedQry);
	when(mockedQry.setParameter("commitDateTime", new Date(2019, 04, 05))).thenReturn(mockedQry);
	when(mockedQry.setParameter("refPlan", "refPlan")).thenReturn(mockedQry);
	when(mockedQry.setParameter("refStatus", "refStatus")).thenReturn(mockedQry);
	when(mockedQry.setParameter("refLoadDateTime", new Date(2019, 04, 05))).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileHashCode", "fileHashCode")).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.saveCommitDetail(new Integer(1), new Integer(1), "srcCommitId", "committerName", "committerMailId", new Date(2019, 04, 05), "refPlan", "refStatus", new Date(2019, 04, 05), "fileHashCode");

	session.close();
	sessionFactory.close();

    }

    @Test
    public void testUpdateCommitDetail() {
	String query = "update git.repo_commit set ref_status=:refStatus, modified_dt=now() where id=:commitId";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("refStatus", "refStatus")).thenReturn(mockedQry);
	when(mockedQry.setParameter("commitId", new Integer(1))).thenReturn(mockedQry);
	instance.updateCommitDetail(new Integer(1), "srcCommitId", "committerName", "committerMailId", new Date(2019, 10, 10), "refPlan", "refStatus", new Date(2019, 10, 10), "fileHashCode", "derivedCommitId", new Integer(1));
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testGetCommitDetailId() {
	String query = "select id from git.repo_commit where file_id= :fileId and SOURCE_COMMIT_ID =:srcCommitId";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileId", new Integer(1))).thenReturn(mockedQry);
	when(mockedQry.setParameter("srcCommitId", "srcCommitId")).thenReturn(mockedQry);
	when(mockedQry.uniqueResult()).thenReturn(new Integer(1));
	instance.getCommitDetailId(new Integer(1), "srcCommitId");
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testGetExistingGitUsers() {

	List<String> pUserNames = new ArrayList();
	pUserNames.add("ABCD");

	String query = "SELECT id, user_name as username, active, display_name displayname  FROM GIT.USERS ";
	if (!pUserNames.isEmpty()) {
	    query = query + " where user_name in (:userNames)";
	}

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("userNames", pUserNames)).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(GitUsers.class))).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<GitUsers>());
	instance.getExistingGitUsers(pUserNames);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testUpdateInActiveFlag() {
	Set<String> activeUsers = new HashSet<>();
	String query = "UPDATE GIT.USERS SET ACTIVE= :activeFlag where user_name not in (:userNames) ";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("activeFlag", "N")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("userNames", activeUsers)).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.updateInActiveFlag(activeUsers);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testUpdateActiveFlag() {
	Set<String> activeUsers = new HashSet<>();
	String query = "UPDATE GIT.USERS SET ACTIVE= :activeFlag where user_name in (:userNames) ";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("activeFlag", "Y")).thenReturn(mockedQry);
	when(mockedQry.setParameterList("userNames", activeUsers)).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.updateActiveFlag(activeUsers);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testSaveNewUsers() {
	List<GitUsers> newUsers = new ArrayList<GitUsers>();
	GitUsers user = new GitUsers();
	newUsers.add(user);
	String query = "INSERT INTO GIT.USERS(USER_NAME, DISPLAY_NAME, ACTIVE) VALUES ( :userName,:displayName, 'Y')";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setParameter("userName", user.getUsername())).thenReturn(mockedQry);
	when(mockedQry.setParameter("displayName", user.getDisplayname())).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.saveNewUsers(newUsers);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testUpdateRepoPermissions() {
	String query = "DELETE FROM GIT.USER_DETAILS where user_name in (SELECT USER_NAME FROM GIT.USERS WHERE ACTIVE='N') ";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.updateRepoPermissions();
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testGetGitUserPermDetails() {
	String query = "select distinct dp.sub_source_repo as subsourcerepo , dp.sub_derived_repo as subderivedrepo, dp.user_name as username," + " dp.display_name as displayname, dp.repo_id as repoid, COALESCE (up.permission, dp.default_permission) as permission, " + " dp.default_permission as defultpermission, dp.source_repo as sourcerepo, dp.derived_repo as derivedrepo, dp.repo_description as description "
		+ " from (select c.sub_source_repo, c.sub_derived_repo,a.user_name, a.display_name, b.id as repo_id, b.default_permission, " + " d.source_repo, d.derived_repo, b.repo_description from git.users a, git.repo_detail b, git.sub_repo_detail c, git.repo_detail d " + " where c.repo_id=b.id and c.repo_id=d.id and a.active='Y') as dp left join (select a.user_name, a.display_name, b.repo_id, b.permission "
		+ " from git.users a, git.user_details b where a.user_name = b.user_name and a.active='Y') as up on dp.user_name = up.user_name and dp.repo_id = up.repo_id ";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(GitUserModel.class))).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<GitUserModel>());
	instance.getGitUserPermDetails();
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testUpdateDefaultPermAndDesc() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	HashMap<String, String> repoAndDefPermDetails = new HashMap<String, String>();
	repoAndDefPermDetails.put("ABCD", "ABCD");
	HashMap<String, String> repoAndDescDetails = new HashMap<String, String>();
	repoAndDescDetails.put("ABCD", "ABCD");
	repoAndDefPermDetails.forEach((key, val) -> {
	    StringBuilder query = new StringBuilder();
	    query.append("UPDATE GIT.repo_detail SET default_permission = :permission, modified_dt=now() ");
	    if (repoAndDescDetails.get(key) != null) {
		query.append(" , repo_description = :desc ");
	    }
	    query.append(" where source_repo= :repoName");
	    when(session.createSQLQuery(query.toString())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("permission", val)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("desc", repoAndDescDetails.get(key))).thenReturn(mockedQry);
	    when(mockedQry.setParameter("repoName", key)).thenReturn(mockedQry);
	});
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.updateDefaultPermAndDesc(repoAndDefPermDetails, repoAndDescDetails);
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testGetUserDetails() {
	List<String> permList = new ArrayList();
	String query = "select a.user_name, b.id, b.source_repo, b.derived_repo from git.user_details a, git.repo_detail b where a.repo_id = b.id and a.permission in(:permissions) ";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query.toString())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("permissions", permList)).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<Object[]>());
	instance.getUserDetails(permList);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testInsertOrDelUserOwnerDetails() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	HashMap<Integer, Set<String>> repoAndUserToBeAdded = new HashMap<Integer, Set<String>>();
	HashMap<Integer, Set<String>> repoAndUserToBeDeleted = new HashMap<Integer, Set<String>>();
	Set<String> set = new HashSet<>();
	set.add("ABCD");
	repoAndUserToBeDeleted.put(0, set);
	repoAndUserToBeAdded.put(0, set);
	if (repoAndUserToBeDeleted != null && !repoAndUserToBeDeleted.isEmpty()) {
	    repoAndUserToBeDeleted.forEach((key, val) -> {
		if (val != null && !val.isEmpty()) {
		    String query = "delete from git.user_details where repo_id in (:sourceRepo) and user_name in (:userNames)";
		    when(session.createSQLQuery(query)).thenReturn(mockedQry);
		    when(mockedQry.setParameter("sourceRepo", key)).thenReturn(mockedQry);
		    when(mockedQry.setParameterList("userNames", val)).thenReturn(mockedQry);
		    when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
		}
	    });
	}

	if (repoAndUserToBeAdded != null && !repoAndUserToBeAdded.isEmpty()) {
	    repoAndUserToBeAdded.forEach((key, val) -> {
		val.forEach(user -> {
		    String query = "insert into git.user_details (user_name, repo_id, permission, created_dt) values(:userName,:srcRepo, 'OWNER', now())";

		    when(session.createSQLQuery(query)).thenReturn(mockedQry);
		    when(mockedQry.setParameter("userName", user)).thenReturn(mockedQry);
		    when(mockedQry.setParameter("srcRepo", key)).thenReturn(mockedQry);
		    when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
		});
	    });
	}

	instance.insertOrDelUserOwnerDetails(repoAndUserToBeDeleted, repoAndUserToBeAdded);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testGetRepoIds() {
	List<String> repoNames = new ArrayList<>();
	repoNames.add("XYZ");
	StringBuffer query = new StringBuffer();
	query.append("select id,source_repo, derived_repo, default_permission from git.repo_detail ");
	if (repoNames != null && !repoNames.isEmpty()) {
	    query.append(" where source_repo in (:sourceRepos)");
	}
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query.toString())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("sourceRepos", repoNames)).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<Object[]>());
	instance.getRepoIds(repoNames);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testGetGitUseDetails() {
	String query = "select id, user_name as username, repo_id as repoid, permission from git.user_details";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(UserDetails.class))).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<UserDetails>());
	instance.getGitUseDetails();
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testUpdateUserDetails() {
	List<UserDetails> gitUserModel = new ArrayList();
	UserDetails userDetail1 = new UserDetails();
	gitUserModel.add(userDetail1);

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);

	gitUserModel.forEach(userDetail -> {
	    String query = "UPDATE git.user_details set permission=:permission, modified_dt=now() where user_name =:userName and repo_id=:repoId";
	    when(session.createSQLQuery(query)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("permission", userDetail.getPermission())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("userName", userDetail.getUsername())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("repoId", userDetail.getRepoid())).thenReturn(mockedQry);
	    when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	});

	instance.updateUserDetails(gitUserModel);
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testInsertUserDetails() {
	List<UserDetails> gitUserModel = new ArrayList();
	UserDetails userDetail1 = new UserDetails();
	gitUserModel.add(userDetail1);
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	gitUserModel.forEach(userDetail -> {
	    String query = "insert into git.user_details (user_name, repo_id, permission, created_dt) values(:userName,:repoId, :permission, now())";
	    when(session.createSQLQuery(query)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("permission", userDetail.getPermission())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("userName", userDetail.getUsername())).thenReturn(mockedQry);
	    when(mockedQry.setParameter("repoId", userDetail.getRepoid())).thenReturn(mockedQry);
	    when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	});

	instance.insertUserDetails(gitUserModel);
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testGetAvailablerefs() {
	String query = "select  concat('refs/heads/master_',LOWER(fin.target_system)), fin.source_repo  from (select distinct target_system, b.source_repo from git.repo_file_list a, git.repo_detail b, git.sub_repo_detail c " + "where c.id = a.sub_repo_id and b.id = c.repo_id) as fin";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<Object[]>());
	instance.getAvailablerefs();
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testdeleteOldDelegations() {
	String sql1 = "update user_settings a set active = 'N', modified_dt=now() from git.users b where a.name in ('DELEGATE_USER', 'DELEGATION') and a.active = 'Y' and  a.user_id = b.user_name and b.active = 'N'";
	String sql2 = "update user_settings a set value = null, modified_dt=now() from git.users b where a.name = 'DELEGATE_USER' and a.active = 'Y' and  a.value = b.user_name and b.active = 'N'";
	String sql3 = "update user_settings a set value = FALSE, modified_dt=now() from git.users b where a.name = 'DELEGATION' and a.active = 'Y' and  a.user_id = b.user_name and b.active = 'N'";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(sql1)).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	when(session.createSQLQuery(sql2)).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	when(session.createSQLQuery(sql3)).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.deleteOldDelegations();
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testsaveRepoDetail() {
	String repoDetails = "INSERT INTO GIT.REPO_DETAIL (SOURCE_REPO, DERIVED_REPO, FUNC_AREA, FILE_TYPE, COMPANY, DEFAULT_PERMISSION, CREATED_DT) VALUES (:sourceRepo,:derivedRepo,:funcArea,:fileType,:company, 'READ_WRITE', now())";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(repoDetails)).thenReturn(mockedQry);
	String srcRepo = "";
	when(mockedQry.setParameter("sourceRepo", srcRepo)).thenReturn(mockedQry);
	when(mockedQry.setParameter("derivedRepo", srcRepo.replace("nonibm_", "derived_"))).thenReturn(mockedQry);
	when(mockedQry.setParameter("funcArea", "funcArea")).thenReturn(mockedQry);
	when(mockedQry.setParameter("fileType", "fileType")).thenReturn(mockedQry);
	when(mockedQry.setParameter("company", "company")).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.saveRepoDetail(srcRepo, "funcArea", "fileType", "company");
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testsaveSubRepoDetail() {
	String repoDetails = "INSERT INTO GIT.SUB_REPO_DETAIL(REPO_ID, SUB_SOURCE_REPO, SUB_DERIVED_REPO, SOURCE_URL, DERIVED_URL, CREATED_DT) VALUES (:repoId,:subSrcRepo,:derSrcRepo,:srcUrl,:derUrl, now())";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(repoDetails)).thenReturn(mockedQry);
	when(mockedQry.setParameter("repoId", new Integer(1))).thenReturn(mockedQry);
	String subSrcRepo = "";
	when(mockedQry.setParameter("subSrcRepo", subSrcRepo)).thenReturn(mockedQry);
	when(mockedQry.setParameter("derSrcRepo", subSrcRepo.replace("nonibm_", "derived_"))).thenReturn(mockedQry);
	String srcUrl = "";
	when(mockedQry.setParameter("srcUrl", srcUrl)).thenReturn(mockedQry);
	when(mockedQry.setParameter("derUrl", srcUrl.replace("nonibm_", "derived_"))).thenReturn(mockedQry);
	when(mockedQry.executeUpdate()).thenReturn(new Integer(1));
	instance.saveSubRepoDetail(subSrcRepo, new Integer(1), srcUrl);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testgetTarSysFromSubRepoDetails() {
	String query = "select target_systems, sub_source_repo from git.sub_repo_detail";
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<Object[]>());

	instance.getTarSysFromSubRepoDetails();
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testupdateTargetSysBasedOnSubRepo() {
	Map<String, Set<String>> repoBasedTargetSytems = new HashMap<>();
	Set<String> set = new HashSet<>();
	set.add("ABCD");
	repoBasedTargetSytems.put("ABCD", set);
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	repoBasedTargetSytems.forEach((key, value) -> {
	    String query = "UPDATE git.sub_repo_detail set target_systems=:targetSystem, modified_dt=now() where sub_source_repo =:srcRepo ";
	    when(session.createSQLQuery(query)).thenReturn(mockedQry);
	    when(mockedQry.setParameter("targetSystem", value.stream().filter(x -> x.contains("refs/heads/master_")).collect(Collectors.joining(",")))).thenReturn(mockedQry);
	    when(mockedQry.setParameter("srcRepo", key)).thenReturn(mockedQry);
	});

	instance.updateTargetSysBasedOnSubRepo(repoBasedTargetSytems);
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testgetRepoBasedSrcArtifcatsCount() {
	String progName = "progName";

	StringBuilder sb = new StringBuilder();
	sb.append(" select count(rd.source_commit_id) from (");
	sb.append(" select fin.sub_repo_id, fin.file_id, fin.source_commit_id, fin.sub_source_repo, fin.source_repo,fin.ref_load_date_time,");
	sb.append(" rank() over(partition by fin.sub_repo_id, fin.file_id order by fin.ref_load_date_time desc) as load_rank,");
	sb.append(" fin.file_name, fin.program_name, fin.file_ext from (select aa.id,aa.sub_repo_id, aa.file_id, aa.source_commit_id,");
	sb.append(" aa.sub_source_repo, aa.source_repo, aa.ref_load_date_time,CASE WHEN ( bb.max_rc_id > 0 ) THEN bb.max_rc_id ELSE 0 END AS DELETE_ID,");
	sb.append(" aa.file_name, aa.program_name, aa.file_ext from (select a.id, a.sub_repo_id, a.file_id, a.source_commit_id, ");
	sb.append(" c.sub_source_repo, d.source_repo, a.ref_load_date_time, b.file_name, b.program_name, b.file_ext from git.repo_commit a,");
	sb.append(" git.repo_file_list b, git.sub_repo_detail c, git.repo_detail d where c.id = a.sub_repo_id and b.id = a.file_id and d.id = c.repo_id and b.sub_repo_id = c.id");
	sb.append(" and b.is_deleted='N' and d.source_repo=:srcRepo and a.ref_status=:refStats");
	if (progName != null && !progName.trim().isEmpty()) {
	    sb.append(" and b.program_name ilike :programName");
	}
	sb.append(" ) as aa left join (select a.file_id, max(a.id) as max_rc_id from git.repo_commit a, git.repo_file_list b, git.sub_repo_detail c, git.repo_detail d");
	sb.append(" where c.id = a.sub_repo_id and b.id = a.file_id and d.id = c.repo_id and b.sub_repo_id = c.id and b.is_deleted='N' and ");
	sb.append(" d.source_repo=:srcRepo and a.ref_status=:deleteStatus ");
	if (progName != null && !progName.trim().isEmpty()) {
	    sb.append(" and b.program_name ilike :programName");
	}
	sb.append(" GROUP BY a.file_id) as bb on aa.file_id = bb.file_id order by delete_id desc) as fin where fin.id>fin.delete_id) as rd where rd.load_rank=1");

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(sb.toString())).thenReturn(mockedQry);
	when(mockedQry.setParameter("srcRepo", "srcRepo")).thenReturn(mockedQry);
	when(mockedQry.setParameter("refStats", Constants.PlanStatus.ONLINE.getDisplayName())).thenReturn(mockedQry);
	when(mockedQry.setParameter("deleteStatus", Constants.PlanStatus.DELETED.getDisplayName())).thenReturn(mockedQry);
	when(mockedQry.setParameter("programName", "%" + progName + "%")).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<BigInteger>());

	instance.getRepoBasedSrcArtifcatsCount("srcRepo", progName);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testgetRepoBasedSrcArtifcats() {
	String progName = "progName";
	LinkedHashMap<String, String> pOrderBy = new LinkedHashMap<>();
	pOrderBy.put("String", "String");

	StringBuilder sb = new StringBuilder();
	sb.append("select rd.source_commit_id as sorucecommitid, rd.sub_source_repo as subsourcerepo,");
	sb.append(" rd.source_repo as sourcerepo, rd.file_name as filename,");
	sb.append(" rd.program_name as progname, rd.file_ext as fileext ,rd.target_system as targetsystem from (");
	sb.append(" select fin.sub_repo_id, fin.file_id, fin.source_commit_id, fin.sub_source_repo, fin.source_repo,fin.ref_load_date_time,");
	sb.append(" rank() over(partition by fin.sub_repo_id, fin.file_id order by fin.ref_load_date_time desc) as load_rank,");
	sb.append(" fin.file_name, fin.program_name, fin.file_ext , fin.target_system from (select aa.id,aa.sub_repo_id, aa.file_id, aa.source_commit_id,");
	sb.append(" aa.sub_source_repo, aa.source_repo, aa.ref_load_date_time,CASE WHEN ( bb.max_rc_id > 0 ) THEN bb.max_rc_id ELSE 0 END AS DELETE_ID,");
	sb.append(" aa.file_name, aa.program_name, aa.file_ext ,aa.target_system from (select a.id, a.sub_repo_id, a.file_id, a.source_commit_id, ");
	sb.append(" c.sub_source_repo, d.source_repo, a.ref_load_date_time, b.file_name, b.program_name, b.file_ext , b.target_system from git.repo_commit a,");
	sb.append(" git.repo_file_list b, git.sub_repo_detail c, git.repo_detail d where c.id = a.sub_repo_id and b.id = a.file_id and d.id = c.repo_id and b.sub_repo_id = c.id");
	sb.append(" and b.is_deleted='N' and d.source_repo=:srcRepo and a.ref_status=:refStats");
	if (progName != null && !progName.trim().isEmpty()) {
	    sb.append(" and b.program_name ilike :programName");
	}
	sb.append(" ) as aa left join (select a.file_id, max(a.id) as max_rc_id from git.repo_commit a, git.repo_file_list b, git.sub_repo_detail c, git.repo_detail d");
	sb.append(" where c.id = a.sub_repo_id and b.id = a.file_id and d.id = c.repo_id and b.sub_repo_id = c.id and b.is_deleted='N' and ");
	sb.append(" d.source_repo=:srcRepo and a.ref_status=:deleteStatus ");
	if (progName != null && !progName.trim().isEmpty()) {
	    sb.append(" and b.program_name ilike :programName");
	}
	sb.append(" GROUP BY a.file_id) as bb on aa.file_id = bb.file_id order by delete_id desc) as fin where fin.id>fin.delete_id) as rd where rd.load_rank=1");

	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		sb.append(" ORDER BY " + key + " " + value);
	    }
	} else {
	    sb.append(" ORDER BY filename asc, progname asc, fileext asc");
	}

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(sb.toString())).thenReturn(mockedQry);
	when(mockedQry.setParameter("srcRepo", "srcRepo")).thenReturn(mockedQry);
	when(mockedQry.setParameter("refStats", Constants.PlanStatus.ONLINE.getDisplayName())).thenReturn(mockedQry);
	when(mockedQry.setParameter("deleteStatus", Constants.PlanStatus.DELETED.getDisplayName())).thenReturn(mockedQry);
	when(mockedQry.setParameter("programName", "%" + progName + "%")).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(RepoBasedSrcArtifacts.class))).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<BigInteger>());

	instance.getRepoBasedSrcArtifcats("srcRepo", progName, 1, 1, pOrderBy);
	session.close();
	sessionFactory.close();
    }

    @Test
    public void testgetSystemBasedSegments() {

	String progName = "progName";
	LinkedHashMap<String, String> pOrderBy = new LinkedHashMap<>();
	pOrderBy.put("String", "String");

	StringBuilder sb = new StringBuilder();
	sb.append("select rd.source_commit_id as sorucecommitid, rd.sub_source_repo as subsourcerepo,");
	sb.append(" rd.source_repo as sourcerepo, rd.file_name as filename,");
	sb.append(" rd.program_name as progname, rd.file_ext as fileext ,rd.target_system as targetsystem, rd.ref_load_date_time as loaddatetime from (");
	sb.append(" select fin.sub_repo_id, fin.file_id, fin.source_commit_id, fin.sub_source_repo, fin.source_repo,fin.ref_load_date_time,");
	sb.append(" rank() over(partition by fin.sub_repo_id, fin.file_id order by fin.ref_load_date_time desc) as load_rank,");
	sb.append(" fin.file_name, fin.program_name, fin.file_ext , fin.target_system from (select aa.id,aa.sub_repo_id, aa.file_id, aa.source_commit_id,");
	sb.append(" aa.sub_source_repo, aa.source_repo, aa.ref_load_date_time,CASE WHEN ( bb.max_rc_id > 0 ) THEN bb.max_rc_id ELSE 0 END AS DELETE_ID,");
	sb.append(" aa.file_name, aa.program_name, aa.file_ext ,aa.target_system from (select a.id, a.sub_repo_id, a.file_id, a.source_commit_id, ");
	sb.append(" c.sub_source_repo, d.source_repo, a.ref_load_date_time, b.file_name, b.program_name, b.file_ext , b.target_system from git.repo_commit a,");
	sb.append(" git.repo_file_list b, git.sub_repo_detail c, git.repo_detail d where c.id = a.sub_repo_id and b.id = a.file_id and d.id = c.repo_id and b.sub_repo_id = c.id");
	sb.append(" and b.is_deleted='N' and b.target_system in (:TargetSystems) and a.ref_status=:refStats");
	if (progName != null && !progName.trim().isEmpty()) {
	    sb.append(" and b.program_name ilike :programName");
	}
	sb.append(" ) as aa left join (select a.file_id, max(a.id) as max_rc_id from git.repo_commit a, git.repo_file_list b, git.sub_repo_detail c, git.repo_detail d");
	sb.append(" where c.id = a.sub_repo_id and b.id = a.file_id and d.id = c.repo_id and b.sub_repo_id = c.id and b.is_deleted='N' and ");
	sb.append(" b.target_system in (:TargetSystems) and a.ref_status=:deleteStatus ");
	if (progName != null && !progName.trim().isEmpty()) {
	    sb.append(" and b.program_name ilike :programName");
	}
	sb.append(" GROUP BY a.file_id) as bb on aa.file_id = bb.file_id order by delete_id desc) as fin where fin.id>fin.delete_id) as rd where rd.load_rank=1");

	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		sb.append(" ORDER BY ").append(key).append(" ").append(value);
	    }
	} else {
	    sb.append(" ORDER BY filename asc, progname asc, fileext asc");
	}

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(sb.toString())).thenReturn(mockedQry);
	List<String> system = new ArrayList<String>();
	system.add(DataWareHouse.getSystemList().get(0).getAliasName());
	when(mockedQry.setParameterList("TargetSystems", system)).thenReturn(mockedQry);
	when(mockedQry.setParameter("refStats", Constants.PlanStatus.ONLINE.getDisplayName())).thenReturn(mockedQry);
	when(mockedQry.setParameter("deleteStatus", Constants.PlanStatus.DELETED.getDisplayName())).thenReturn(mockedQry);
	when(mockedQry.setParameter("programName", "%" + progName + "%")).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(SystemBasedSrcArtifacts.class))).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<SystemBasedSrcArtifacts>());

	instance.getSystemBasedSegments(system, progName, 1, 1, pOrderBy);
	session.close();
	sessionFactory.close();

    }

    @Test
    public void testfindByProgramName() {

	Collection<String> pAllowedRepos = new ArrayList<>();
	List<String> systems = new ArrayList<>();
	PRODSearchType pPendingStatusReq = PRODSearchType.BOTH;
	List<String> pRankStatus = null;
	String query = "SELECT a.id AS repoCommitId, d.func_area as funcArea, rank() OVER(partition BY a.file_id, a.ref_status ORDER BY a.ref_load_date_time DESC) as version," + " a.sub_repo_id as subRepoId, a.file_id as fileId, b.file_name as fileName, b.program_name as programName, a.source_commit_id as sourceCommitId, a.derived_commit_id as derivedCommitId," + " a.committer_name as committerName, a.committer_mail_id as committerMailId,"
		+ " a.commit_date_time as commitDateTime, a.ref_plan as refPlan, a.ref_status as refStatus, a.ref_load_date_time as refLoadDateTime, a.file_hashcode as fileHashcode," + " LOWER('master_'||b.target_system) as targetSystem, c.repo_id as repoId," + " c.sub_source_repo as subSourceRepo, c.sub_derived_repo as subDerivedRepo, c.source_url as sourceUrl, c.derived_url as derivedUrl, d.source_repo as sourceRepo,"
		+ " d.derived_repo as derivedRepo, d.company as company, d.file_type as fileType, b.file_ext as fileExt," + " case when c.repo_id>0 then 'Y' else 'N' end as active FROM   git.repo_commit a, git.repo_file_list b, git.sub_repo_detail c, git.repo_detail d" + " WHERE  a.file_id = b.id AND b.sub_repo_id = c.id AND c.repo_id = d.id AND a.ref_status in(:status) " + " AND a.id IN (select distinct id from (SELECT c.id, C.delete_id, c.file_id, c.ref_status, Rank() OVER ("
		+ " partition BY c.file_id,c.ref_status ORDER BY c.ref_load_date_time DESC) AS load_date_rank FROM (SELECT a.id, a.file_id, a.source_commit_id, a.ref_load_date_time, a.ref_status," + " CASE WHEN ( b.id > 0 ) THEN B.id ELSE 0 END AS DELETE_ID FROM   (SELECT rc.id, rc.file_id, rc.source_commit_id," + " rc.ref_load_date_time, rc.ref_status FROM   git.repo_commit rc, git.repo_file_list fl"
		+ " WHERE  rc.file_id = fl.id and fl.program_Name NOT ILIKE 'README.md'and fl.program_Name ILIKE :progName and rc.ref_status in(:status)) a LEFT JOIN (SELECT rc.file_id," + " Max(rc.id) AS id FROM   git.repo_commit rc, git.repo_file_list fl" + " WHERE  rc.file_id = fl.id and fl.program_Name ILIKE :progName and rc.ref_status = 'Deleted' GROUP  BY file_id) b ON a.file_id = b.file_id) c"
		+ " WHERE  c.id > C.delete_id) d where case when d.ref_status in(:lRankStatus) then d.load_date_rank = 1" + " else d.load_date_rank > 0 end) AND d.company = :companyName";
	if (pAllowedRepos != null && !pAllowedRepos.isEmpty()) {
	    query = query + " AND c.sub_source_repo in (:sourceRepos)";
	}

	if (systems != null && !systems.isEmpty()) {
	    query = query + " AND b.target_system in (:systems)";
	}

	List<String> status = new ArrayList<>();

	if (pPendingStatusReq != null && Constants.PRODSearchType.ONLINE_ONLY.name().equals(pPendingStatusReq.name())) {
	    status.add("Online");
	    status.add("Fallback");
	} else if (pPendingStatusReq != null && Constants.PRODSearchType.PENDING_ONLY.name().equals(pPendingStatusReq.name())) {
	    status.add("Pending");
	} else {
	    status.add("Online");
	    status.add("Pending");
	    status.add("Fallback");
	    status.add("New File");
	}

	// If we mention rankStatus then it will returns only 0 version, if we want more
	// than 0 version, not include the status in Array List
	if (pRankStatus == null || pRankStatus.isEmpty()) {
	    pRankStatus = new ArrayList();
	    pRankStatus.add("Online");
	    pRankStatus.add("Fallback");
	}

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(query)).thenReturn(mockedQry);
	String pFileFilter = "pFileFilter";
	when(mockedQry.setParameter("progName", pFileFilter + "%")).thenReturn(mockedQry);
	String companyName = "companyName";
	when(mockedQry.setParameter("companyName", companyName)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("status", status)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("lRankStatus", pRankStatus)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("sourceRepos", pAllowedRepos)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("systems", systems)).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(GitProdSearchDb.class))).thenReturn(mockedQry);
	when(mockedQry.list()).thenReturn(new ArrayList<GitProdSearchDb>());

	instance.findByProgramName(companyName, pFileFilter, pPendingStatusReq, pAllowedRepos, systems);
	session.close();
	sessionFactory.close();
    }

}

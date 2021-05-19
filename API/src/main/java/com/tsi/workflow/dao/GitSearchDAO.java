package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.GitProdSearchDb;
import com.tsi.workflow.beans.ui.GitUserModel;
import com.tsi.workflow.beans.ui.GitUsers;
import com.tsi.workflow.beans.ui.RepoBasedSrcArtifacts;
import com.tsi.workflow.beans.ui.SystemBasedSrcArtifacts;
import com.tsi.workflow.beans.ui.UserDetails;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.PRODSearchType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GitSearchDAO extends BaseDAO<GitProdSearchDb> {

    private static final Logger LOG = Logger.getLogger(GitSearchDAO.class.getName());

    @Transactional
    public List<GitProdSearchDb> oldFindByProgramName(String pCompany, String pFileFilter, PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos) {
	String query = "SELECT a FROM GitProdSearchDb a WHERE a.programName LIKE (:filterName)" + " AND a.company = :companyName" + " AND a.refStatus in (:status)";
	if (pAllowedRepos != null && !pAllowedRepos.isEmpty()) {
	    query = query + " AND a.subSourceRepo in (:sourceRepos)";
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
	}

	Query lQuery = getCurrentSession().createQuery(query).setParameter("filterName", pFileFilter + "%").setParameter("companyName", pCompany).setParameterList("status", status);
	if (pAllowedRepos != null && !pAllowedRepos.isEmpty()) {
	    lQuery.setParameterList("sourceRepos", pAllowedRepos);
	}
	return lQuery.list();
    }

    @Transactional
    public List<GitProdSearchDb> findByProgramName(String pCompany, String pFileFilter, PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems) {
	return findByProgramName(pCompany, pFileFilter, pPendingStatusReq, pAllowedRepos, systems, null);
    }

    @Transactional
    public List<GitProdSearchDb> findByProgramName(String pCompany, String pFileFilter, PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems, List<String> pRankStatus) {
	String query = "SELECT a.id AS repoCommitId, d.func_area as funcArea, d.repo_description as repoDesc,  rank() OVER(partition BY a.file_id, a.ref_status ORDER BY a.ref_load_date_time DESC) as version," + " a.sub_repo_id as subRepoId, a.file_id as fileId, b.file_name as fileName, b.program_name as programName, a.source_commit_id as sourceCommitId, a.derived_commit_id as derivedCommitId," + " a.committer_name as committerName, a.committer_mail_id as committerMailId,"
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
	LOG.info(query.toString());
	Query lQuery = getCurrentSession().createSQLQuery(query).setParameter("progName", pFileFilter + "%").setParameter("companyName", pCompany).setParameterList("status", status).setParameterList("lRankStatus", pRankStatus);
	if (pAllowedRepos != null && !pAllowedRepos.isEmpty()) {
	    lQuery.setParameterList("sourceRepos", pAllowedRepos);
	}

	if (systems != null && !systems.isEmpty()) {
	    lQuery.setParameterList("systems", systems);
	}

	lQuery.setResultTransformer(new AliasToBeanResultTransformer(GitProdSearchDb.class));
	return lQuery.list();
    }

    @Transactional
    public Integer getSubRepoDetail(String subRepoName) {

	String query = "SELECT id FROM git.sub_repo_detail WHERE sub_source_repo =:subRepoName";

	Integer subRepoId = (Integer) getCurrentSession().createSQLQuery(query).setParameter("subRepoName", subRepoName).uniqueResult();

	return subRepoId != null ? subRepoId : 0;

    }

    @Transactional
    public Integer getFileDetail(String subRepoName, String fileName, String targetSystem) {

	String query = "SELECT a.id FROM git.repo_file_list a, git.sub_repo_detail b WHERE a.sub_repo_id = b.id " + " and a.file_name = :fileName" + " and a.target_system = :targetSystem" + " and	b.sub_source_repo = :subRepoName" + " and a.is_deleted='N'";

	Integer fileDetails = (Integer) getCurrentSession().createSQLQuery(query).setParameter("fileName", fileName).setParameter("targetSystem", targetSystem).setParameter("subRepoName", subRepoName).uniqueResult();

	return fileDetails != null ? fileDetails : 0;

    }

    @Transactional
    public void updateFileListDeleteFlag(Integer fileId, boolean isDeleted) {

	String query = "update git.repo_file_list set is_deleted=:deleteFlag, modified_dt=now() where id= :fileId";

	getCurrentSession().createSQLQuery(query).setParameter("deleteFlag", isDeleted ? "Y" : "N").setParameter("fileId", fileId).executeUpdate();
    }

    @Transactional
    public void saveNewFileList(Integer subRepoId, String fileName, String targetSystem) {

	String query = "INSERT INTO GIT.REPO_FILE_LIST(SUB_REPO_ID, FILE_NAME, PROGRAM_NAME, FILE_EXT, TARGET_SYSTEM, is_deleted, created_dt) VALUES ( :subRepoId, :fileName, :progName, :fileExt, :targetSystem,'N', now())";

	int count = getCurrentSession().createSQLQuery(query).setParameter("subRepoId", subRepoId).setParameter("fileName", fileName).setParameter("progName", FilenameUtils.getName(fileName)).setParameter("fileExt", FilenameUtils.getExtension(fileName)).setParameter("targetSystem", targetSystem).executeUpdate();
	LOG.info("# of rec updated: " + count);
    }

    @Transactional
    public void saveCommitDetail(Integer subRepoId, Integer fileId, String srcCommitId, String committerName, String committerMailId, Date commitDateTime, String refPlan, String refStatus, Date refLoadDateTime, String fileHashCode) {

	String query = "INSERT INTO GIT.REPO_COMMIT(SUB_REPO_ID, FILE_ID, SOURCE_COMMIT_ID, COMMITTER_NAME, COMMITTER_MAIL_ID, COMMIT_DATE_TIME, REF_PLAN, REF_STATUS, REF_LOAD_DATE_TIME, FILE_HASHCODE, CREATED_DT) VALUES(:subRepoId,:fileId,:srcCommitId,:committerName,:committerMailId,:commitDateTime,:refPlan,:refStatus,:refLoadDateTime,:fileHashCode, now())";

	getCurrentSession().createSQLQuery(query).setParameter("subRepoId", subRepoId).setParameter("fileId", fileId).setParameter("srcCommitId", srcCommitId).setParameter("committerName", committerName).setParameter("committerMailId", committerMailId).setParameter("commitDateTime", commitDateTime).setParameter("refPlan", refPlan).setParameter("refStatus", refStatus).setParameter("refLoadDateTime", refLoadDateTime).setParameter("fileHashCode", fileHashCode).executeUpdate();
    }

    @Transactional
    public void updateCommitDetail(Integer fileId, String srcCommitId, String committerName, String committerMailId, Date commitDateTime, String refPlan, String refStatus, Date refLoadDateTime, String fileHashCode, String derivedCommitId, Integer commitId) {

	// String query = "update git.repo_commit set
	// ref_status=:refStatus,COMMITTER_NAME =:committerName,
	// COMMITTER_MAIL_ID =:committerMailId, COMMIT_DATE_TIME =:commitDateTime, "
	// + " REF_PLAN=:refPlan, REF_LOAD_DATE_TIME=:refLoadDateTime,
	// FILE_HASHCODE=:fileHashCode, derived_commit_id
	// =:derivedCommitId where id=:commitId";
	String query = "update git.repo_commit set ref_status=:refStatus, modified_dt=now() where id=:commitId";

	int count = getCurrentSession().createSQLQuery(query).setParameter("refStatus", refStatus).setParameter("commitId", commitId).executeUpdate();
	LOG.info("# of rec updated in updateCommitDetail(): " + count);
    }

    @Transactional
    public int getCommitDetailId(Integer fileId, String srcCommitId) {

	String query = "select id from git.repo_commit where file_id= :fileId and SOURCE_COMMIT_ID =:srcCommitId";

	Integer repoCommitId = (Integer) getCurrentSession().createSQLQuery(query).setParameter("fileId", fileId).setParameter("srcCommitId", srcCommitId).uniqueResult();

	return repoCommitId != null ? repoCommitId : 0;
    }

    @Transactional
    public List<GitUsers> getExistingGitUsers(List<String> pUserNames) {
	String query = "SELECT id, user_name as username, active, display_name displayname  FROM GIT.USERS ";
	if (!pUserNames.isEmpty()) {
	    query = query + " where user_name in (:userNames)";
	}

	Query lQuery = getCurrentSession().createSQLQuery(query);
	if (!pUserNames.isEmpty()) {
	    lQuery.setParameterList("userNames", pUserNames);
	}
	lQuery.setResultTransformer(new AliasToBeanResultTransformer(GitUsers.class));
	return lQuery.list();
    }

    @Transactional
    public void updateInActiveFlag(Set<String> activeUsers) {
	String query = "UPDATE GIT.USERS SET ACTIVE= :activeFlag where user_name not in (:userNames) ";

	getCurrentSession().createSQLQuery(query).setParameter("activeFlag", "N").setParameterList("userNames", activeUsers).executeUpdate();
    }

    @Transactional
    public void updateActiveFlag(Set<String> activeUsers) {
	String query = "UPDATE GIT.USERS SET ACTIVE= :activeFlag where user_name in (:userNames) ";

	getCurrentSession().createSQLQuery(query).setParameter("activeFlag", "Y").setParameterList("userNames", activeUsers).executeUpdate();
    }

    @Transactional
    public void saveNewUsers(List<GitUsers> newUsers) {
	newUsers.forEach(user -> {
	    String query = "INSERT INTO GIT.USERS(USER_NAME, DISPLAY_NAME, ACTIVE) VALUES ( :userName,:displayName, 'Y')";
	    int count = getCurrentSession().createSQLQuery(query).setParameter("userName", user.getUsername()).setParameter("displayName", user.getDisplayname()).executeUpdate();
	});
    }

    @Transactional
    public void updateRepoPermissions() {
	String query = "DELETE FROM GIT.USER_DETAILS where user_name in (SELECT USER_NAME FROM GIT.USERS WHERE ACTIVE='N') ";

	getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    // @Transactional
    // public void updateDisplayName(List<GitUsers> users) {
    // users.forEach(user -> {
    // String query = "UPDATE GIT.USERS SET display_name = :displayName where
    // user_name= :userName";
    // int count = getCurrentSession().createSQLQuery(query)
    // .setParameter("displayName", user.getDisplayname())
    // .setParameter("userName", user.getUsername())
    // .executeUpdate();
    // });
    // }
    @Transactional
    public List<GitUserModel> getGitUserPermDetails() {
	String query = "select distinct dp.sub_source_repo as subsourcerepo , dp.sub_derived_repo as subderivedrepo, dp.user_name as username," + " dp.display_name as displayname, dp.repo_id as repoid, COALESCE (up.permission, dp.default_permission) as permission, " + " dp.default_permission as defultpermission, dp.source_repo as sourcerepo, dp.derived_repo as derivedrepo, dp.repo_description as description "
		+ " from (select c.sub_source_repo, c.sub_derived_repo,a.user_name, a.display_name, b.id as repo_id, b.default_permission, " + " d.source_repo, d.derived_repo, b.repo_description from git.users a, git.repo_detail b, git.sub_repo_detail c, git.repo_detail d " + " where c.repo_id=b.id and c.repo_id=d.id and a.active='Y') as dp left join (select a.user_name, a.display_name, b.repo_id, b.permission "
		+ " from git.users a, git.user_details b where a.user_name = b.user_name and a.active='Y') as up on dp.user_name = up.user_name and dp.repo_id = up.repo_id ";

	Query lQuery = getCurrentSession().createSQLQuery(query);
	lQuery.setResultTransformer(new AliasToBeanResultTransformer(GitUserModel.class));
	return lQuery.list();
    }

    @Transactional
    public void updateDefaultPermAndDesc(HashMap<String, String> repoAndDefPermDetails, HashMap<String, String> repoAndDescDetails) {
	repoAndDefPermDetails.forEach((key, val) -> {
	    StringBuilder query = new StringBuilder();
	    query.append("UPDATE GIT.repo_detail SET default_permission = :permission, modified_dt=now() ");
	    if (repoAndDescDetails.get(key) != null) {
		query.append(" , repo_description = :desc ");
	    }
	    query.append(" where source_repo= :repoName");

	    Query lQuery = getCurrentSession().createSQLQuery(query.toString()).setParameter("permission", val);
	    if (repoAndDescDetails.get(key) != null) {
		lQuery.setParameter("desc", repoAndDescDetails.get(key));
	    }
	    lQuery.setParameter("repoName", key).executeUpdate();
	});
    }

    @Transactional
    public List<Object[]> getUserDetails(List<String> permList) {
	String query = "select a.user_name, b.id, b.source_repo, b.derived_repo from git.user_details a, git.repo_detail b where a.repo_id = b.id and a.permission in(:permissions) ";

	Query lQuery = getCurrentSession().createSQLQuery(query).setParameterList("permissions", permList);
	return lQuery.list();
    }

    @Transactional
    public void insertOrDelUserOwnerDetails(HashMap<Integer, Set<String>> repoAndUserToBeAdded, HashMap<Integer, Set<String>> repoAndUserToBeDeleted) {

	if (repoAndUserToBeDeleted != null && !repoAndUserToBeDeleted.isEmpty()) {
	    repoAndUserToBeDeleted.forEach((key, val) -> {
		if (val != null && !val.isEmpty()) {
		    String query = "delete from git.user_details where repo_id in (:sourceRepo) and user_name in (:userNames)";
		    int count = getCurrentSession().createSQLQuery(query).setParameter("sourceRepo", key).setParameterList("userNames", val).executeUpdate();
		}
	    });
	}

	if (repoAndUserToBeAdded != null && !repoAndUserToBeAdded.isEmpty()) {
	    repoAndUserToBeAdded.forEach((key, val) -> {
		val.forEach(user -> {
		    String query = "insert into git.user_details (user_name, repo_id, permission, created_dt) values(:userName,:srcRepo, 'OWNER', now())";
		    int count = getCurrentSession().createSQLQuery(query).setParameter("userName", user).setParameter("srcRepo", key).executeUpdate();
		});
	    });
	}
    }

    @Transactional
    public List<Object[]> getRepoIds(List<String> repoNames) {
	StringBuffer query = new StringBuffer();
	query.append("select id,source_repo, derived_repo, default_permission from git.repo_detail ");
	if (repoNames != null && !repoNames.isEmpty()) {
	    query.append(" where source_repo in (:sourceRepos)");
	}
	Query lQuery = getCurrentSession().createSQLQuery(query.toString());
	if (repoNames != null && !repoNames.isEmpty()) {
	    lQuery.setParameterList("sourceRepos", repoNames);
	}
	return lQuery.list();
    }

    @Transactional
    public List<UserDetails> getGitUseDetails() {

	String query = "select id, user_name as username, repo_id as repoid, permission from git.user_details";

	Query lQuery = getCurrentSession().createSQLQuery(query);
	lQuery.setResultTransformer(new AliasToBeanResultTransformer(UserDetails.class));
	return lQuery.list();
    }

    @Transactional
    public void updateUserDetails(List<UserDetails> gitUserModel) {
	gitUserModel.forEach(userDetail -> {
	    String query = "UPDATE git.user_details set permission=:permission, modified_dt=now() where user_name =:userName and repo_id=:repoId";
	    LOG.info("permission" + userDetail.getPermission() + " userName" + userDetail.getUsername() + " repoId" + userDetail.getRepoid());
	    int count = getCurrentSession().createSQLQuery(query).setParameter("permission", userDetail.getPermission()).setParameter("userName", userDetail.getUsername()).setParameter("repoId", userDetail.getRepoid()).executeUpdate();

	    LOG.info("UD update count: " + count);
	});
    }

    @Transactional
    public void insertUserDetails(List<UserDetails> gitUserModel) {
	gitUserModel.forEach(userDetail -> {
	    String query = "insert into git.user_details (user_name, repo_id, permission, created_dt) values(:userName,:repoId, :permission, now())";
	    LOG.info("permission" + userDetail.getPermission() + " userName" + userDetail.getUsername() + " repoId" + userDetail.getRepoid());
	    int count = getCurrentSession().createSQLQuery(query).setParameter("permission", userDetail.getPermission()).setParameter("userName", userDetail.getUsername()).setParameter("repoId", userDetail.getRepoid()).executeUpdate();

	    LOG.info("UD add count: " + count);
	});
    }

    @Transactional
    public List<Object[]> getAvailablerefs() {
	String query = "select  concat('refs/heads/master_',LOWER(fin.target_system)), fin.source_repo  from (select distinct target_system, b.source_repo from git.repo_file_list a, git.repo_detail b, git.sub_repo_detail c " + "where c.id = a.sub_repo_id and b.id = c.repo_id) as fin";

	Query lQuery = getCurrentSession().createSQLQuery(query);
	return lQuery.list();
    }

    public void deleteOldDelegations() {
	String sql1 = "update user_settings a set active = 'N', modified_dt=now() from git.users b where a.name in ('DELEGATE_USER', 'DELEGATION') and a.active = 'Y' and  a.user_id = b.user_name and b.active = 'N'";
	String sql2 = "update user_settings a set value = null, modified_dt=now() from git.users b where a.name = 'DELEGATE_USER' and a.active = 'Y' and  a.value = b.user_name and b.active = 'N'";
	String sql3 = "update user_settings a set value = FALSE, modified_dt=now() from git.users b where a.name = 'DELEGATION' and a.active = 'Y' and  a.user_id = b.user_name and b.active = 'N'";
	getCurrentSession().createSQLQuery(sql1).executeUpdate();
	getCurrentSession().createSQLQuery(sql2).executeUpdate();
	getCurrentSession().createSQLQuery(sql3).executeUpdate();
    }

    public void saveRepoDetail(String srcRepo, String funcArea, String fileType, String company) {
	String repoDetails = "INSERT INTO GIT.REPO_DETAIL (SOURCE_REPO, DERIVED_REPO, FUNC_AREA, FILE_TYPE, COMPANY, DEFAULT_PERMISSION, CREATED_DT) VALUES (:sourceRepo,:derivedRepo,:funcArea,:fileType,:company, 'READ_WRITE', now())";

	getCurrentSession().createSQLQuery(repoDetails).setParameter("sourceRepo", srcRepo).setParameter("derivedRepo", srcRepo.replace("nonibm_", "derived_")).setParameter("funcArea", funcArea).setParameter("fileType", fileType).setParameter("company", company).executeUpdate();

    }

    public void saveSubRepoDetail(String subSrcRepo, Integer repoId, String srcUrl) {
	String repoDetails = "INSERT INTO GIT.SUB_REPO_DETAIL(REPO_ID, SUB_SOURCE_REPO, SUB_DERIVED_REPO, SOURCE_URL, DERIVED_URL, CREATED_DT) VALUES (:repoId,:subSrcRepo,:derSrcRepo,:srcUrl,:derUrl, now())";

	getCurrentSession().createSQLQuery(repoDetails).setParameter("repoId", repoId).setParameter("subSrcRepo", subSrcRepo).setParameter("derSrcRepo", subSrcRepo.replace("nonibm_", "derived_")).setParameter("srcUrl", srcUrl).setParameter("derUrl", srcUrl.replace("nonibm_", "derived_")).executeUpdate();

    }

    @Transactional
    public List<Object[]> getTarSysFromSubRepoDetails() {
	String query = "select target_systems, sub_source_repo from git.sub_repo_detail";

	Query lQuery = getCurrentSession().createSQLQuery(query);
	return lQuery.list();
    }

    @Transactional
    public void updateTargetSysBasedOnSubRepo(Map<String, Set<String>> repoBasedTargetSytems) {
	repoBasedTargetSytems.forEach((key, value) -> {
	    String query = "UPDATE git.sub_repo_detail set target_systems=:targetSystem, modified_dt=now() where sub_source_repo =:srcRepo ";
	    int count = getCurrentSession().createSQLQuery(query).setParameter("targetSystem", value.stream().filter(x -> x.contains("refs/heads/master_")).collect(Collectors.joining(","))).setParameter("srcRepo", key).executeUpdate();
	});
    }

    @Transactional
    public List<RepoBasedSrcArtifacts> getRepoBasedSrcArtifcats(String repoName, String progName, Integer limit, Integer offset, LinkedHashMap<String, String> pOrderBy) {

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
	// LOG.info("Query: " + sb.toString() + " Prog Name: " + progName + " Repo name:
	// " + repoName);
	Query lQuery = getCurrentSession().createSQLQuery(sb.toString());
	lQuery.setParameter("srcRepo", repoName).setParameter("refStats", Constants.PlanStatus.ONLINE.getDisplayName()).setParameter("deleteStatus", Constants.PlanStatus.DELETED.getDisplayName());

	if (progName != null && !progName.trim().isEmpty()) {
	    lQuery.setParameter("programName", "%" + progName + "%");
	}
	if (limit > 0) {
	    lQuery.setFirstResult(offset * limit);
	    lQuery.setMaxResults(limit);
	}

	lQuery.setResultTransformer(new AliasToBeanResultTransformer(RepoBasedSrcArtifacts.class));
	return lQuery.list();

    }

    @Transactional
    public Long getRepoBasedSrcArtifcatsCount(String repoName, String progName) {

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
	Query lQuery = getCurrentSession().createSQLQuery(sb.toString());
	lQuery.setParameter("srcRepo", repoName).setParameter("refStats", Constants.PlanStatus.ONLINE.getDisplayName()).setParameter("deleteStatus", Constants.PlanStatus.DELETED.getDisplayName());

	if (progName != null && !progName.trim().isEmpty()) {
	    lQuery.setParameter("programName", "%" + progName + "%");
	}
	Long lCount = 0L;
	final List<BigInteger> obj = lQuery.list();
	for (BigInteger l : obj) {
	    if (l != null) {
		lCount = l.longValue();
	    }
	}
	return lCount;

    }

    @Transactional
    public List<SystemBasedSrcArtifacts> getSystemBasedSegments(List<String> systems, String progName, Integer limit, Integer offset, LinkedHashMap<String, String> pOrderBy) {

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
	Query lQuery = getCurrentSession().createSQLQuery(sb.toString());
	lQuery.setParameterList("TargetSystems", systems).setParameter("refStats", Constants.PlanStatus.ONLINE.getDisplayName()).setParameter("deleteStatus", Constants.PlanStatus.DELETED.getDisplayName());

	if (progName != null && !progName.trim().isEmpty()) {
	    lQuery.setParameter("programName", "%" + progName + "%");
	}
	if (limit != null && limit > 0) {
	    lQuery.setFirstResult(offset * limit);
	    lQuery.setMaxResults(limit);
	}

	lQuery.setResultTransformer(new AliasToBeanResultTransformer(SystemBasedSrcArtifacts.class));
	return lQuery.list();

    }

}

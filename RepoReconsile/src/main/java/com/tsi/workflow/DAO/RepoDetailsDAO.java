/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.DAO;

import com.gitblit.models.RepositoryModel;
import com.tsi.workflow.Main;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.config.AppConfig;
import com.tsi.workflow.database.Postgres;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author DINESH.RAMANATHAN
 */
public class RepoDetailsDAO {

    private static final Logger LOG = Logger.getLogger(RepoDetailsDAO.class.getName());

    public void saveRepoDetails(List<RepositoryModel> pRepositoryModels) throws SQLException {
	String REPO_FILE_LIST = "INSERT INTO " + Main.lDBSchema + ".REPO_DETAIL (SOURCE_REPO, DERIVED_REPO, FUNC_AREA, FILE_TYPE, COMPANY, CREATED_DT) VALUES (?,?,?,?,?, now())";

	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement statement = conn.prepareStatement(REPO_FILE_LIST)) {
	    int itemNoInBatch = 0;
	    for (RepositoryModel lRepositoryModel : pRepositoryModels) {
		int counter = 0;
		String lCommonRepoName = lRepositoryModel.name.replaceAll("\\d*\\.git$", "\\.git");
		if (!CacheClient.getInstance().getAllRepositoryNames().contains(lCommonRepoName)) {
		    LOG.info("Inserting for Repo " + lCommonRepoName);
		    CacheClient.getInstance().getAllRepositoryNames().add(lCommonRepoName);
		    statement.setString(++counter, lCommonRepoName);
		    statement.setString(++counter, lCommonRepoName.replaceAll("nonibm_", "derived_").replaceAll("ibm_", "derived_"));
		    statement.setString(++counter, getFuncArea(lCommonRepoName));
		    statement.setString(++counter, lCommonRepoName.contains(File.separator + "ibm" + File.separator) ? Constants.FILE_TYPE.IBM.name() : Constants.FILE_TYPE.NON_IBM.name());
		    statement.setString(++counter, lCommonRepoName.contains(File.separator + "tp" + File.separator) ? "tp" : "dl");
		    statement.addBatch();
		    itemNoInBatch++;
		    if (itemNoInBatch % 10000 == 0) {
			statement.executeBatch();
			statement.clearBatch();
			itemNoInBatch = 0;
		    }
		}
	    }
	    // Exit Check
	    if (itemNoInBatch > 0) {
		statement.executeBatch();
		statement.clearBatch();
	    }
	    // conn.commit();
	}
    }

    private String getFuncArea(String funcArea) {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(funcArea)).toUpperCase();
	String lReturn = FilenameUtils.getName(funcArea).toLowerCase().replace("nonibm_", "").replace("ibm_", "").replace(".git", "").toUpperCase();

	if (lRepoName.startsWith("NONIBM_") && lRepoName.length() > 10) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
	    return lReturn;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
	    return lReturn;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
	    return lReturn;
	} else {
	    return lReturn.replaceAll("\\d+$", "");
	}
    }

    public void saveSubRepoDetails(List<RepositoryModel> pRepositoryModels) throws WorkflowException, SQLException {
	String SUB_REPO_DETAIL = "INSERT INTO " + Main.lDBSchema + ".SUB_REPO_DETAIL(REPO_ID, SUB_SOURCE_REPO, SUB_DERIVED_REPO, SOURCE_URL, DERIVED_URL, CREATED_DT) VALUES (?,?,?,?,?, now())";
	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement statement = conn.prepareStatement(SUB_REPO_DETAIL)) {
	    int itemNoInBatch = 0;
	    for (RepositoryModel lRepositoryModel : pRepositoryModels) {
		int counter = 0;
		if (!CacheClient.getInstance().getAllSubRepositoryNames().contains(lRepositoryModel.name)) {

		    String lCommonRepoName = lRepositoryModel.name.replaceAll("\\d*\\.git$", "\\.git");
		    Integer lId = CacheClient.getInstance().getAllRepositoryDetails().get(lCommonRepoName);
		    LOG.info("Inserting for Sub Repo " + lRepositoryModel.name + " with parent " + lId);
		    CacheClient.getInstance().getAllSubRepositoryNames().add(lRepositoryModel.name);

		    statement.setInt(++counter, lId);
		    String lDerivedName = lRepositoryModel.name.replaceAll("nonibm_", "derived_").replaceAll("ibm_", "derived_");
		    statement.setString(++counter, lRepositoryModel.name);
		    statement.setString(++counter, lDerivedName);
		    String lSourceURL = "ssh://" + AppConfig.getInstance().getWfLoadBalancerHost() + ":" + AppConfig.getInstance().getGitDataPort() + "/" + lRepositoryModel.name;
		    String lDerivedURL = "ssh://" + AppConfig.getInstance().getWfLoadBalancerHost() + ":" + AppConfig.getInstance().getGitDataPort() + "/" + lDerivedName;
		    statement.setString(++counter, lSourceURL);
		    statement.setString(++counter, lDerivedURL);
		    statement.addBatch();
		    itemNoInBatch++;
		    if (itemNoInBatch % 10000 == 0) {
			statement.executeBatch();
			statement.clearBatch();
			itemNoInBatch = 0;
		    }
		}
	    }
	    if (itemNoInBatch > 0) {
		statement.executeBatch();
		statement.clearBatch();
	    }
	    // conn.commit();
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing saveSubRepoDetails", e);
	}
    }

    public List<String> getRepoDetails() throws WorkflowException, SQLException {
	String REPO_DETAILS = "SELECT SOURCE_REPO FROM " + Main.lDBSchema + ".REPO_DETAIL";
	List<String> lRepoNames = new ArrayList<>();
	ResultSet rs = null;
	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(REPO_DETAILS)) {
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		lRepoNames.add(rs.getString("SOURCE_REPO"));
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing getRepoDetails", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return lRepoNames;
    }

    public List<String> getSubRepoDetails() throws WorkflowException, SQLException {
	String REPO_DETAILS = "SELECT SUB_SOURCE_REPO FROM " + Main.lDBSchema + ".SUB_REPO_DETAIL";
	List<String> lRepoNames = new ArrayList<>();
	ResultSet rs = null;
	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(REPO_DETAILS)) {
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		lRepoNames.add(rs.getString("SUB_SOURCE_REPO"));
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing getSubRepoDetails", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return lRepoNames;
    }

    public Map<String, Integer> getRepoDetailsMap() throws WorkflowException, SQLException {
	String REPO_DETAILS = "SELECT SOURCE_REPO, ID FROM " + Main.lDBSchema + ".REPO_DETAIL";
	Map<String, Integer> lRepoNames = new HashMap<>();
	ResultSet rs = null;
	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(REPO_DETAILS)) {
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		lRepoNames.put(rs.getString("SOURCE_REPO"), rs.getInt("ID"));
	    }
	} catch (Exception e) {
	    throw new WorkflowException("Error while executing getRepoDetailsMap", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return lRepoNames;
    }

    public Map<String, Integer> getSubRepoDetailsMap() throws WorkflowException, SQLException {
	String REPO_DETAILS = "SELECT SUB_SOURCE_REPO, ID FROM " + Main.lDBSchema + ".SUB_REPO_DETAIL";
	Map<String, Integer> lRepoNames = new HashMap<>();

	ResultSet rs = null;
	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(REPO_DETAILS)) {
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		lRepoNames.put(rs.getString("SUB_SOURCE_REPO"), rs.getInt("ID"));
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing getRepoDetailsMap", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}

	return lRepoNames;
    }

    public Map<Integer, String> getSubRepoIDWiseDetailsMap() throws WorkflowException, SQLException {
	String REPO_DETAILS = "SELECT SUB_SOURCE_REPO, ID FROM " + Main.lDBSchema + ".SUB_REPO_DETAIL";
	Map<Integer, String> lRepoNames = new HashMap<>();

	ResultSet rs = null;
	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(REPO_DETAILS)) {
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		lRepoNames.put(rs.getInt("ID"), rs.getString("SUB_SOURCE_REPO"));
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing getRepoDetailsMap", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}

	return lRepoNames;
    }

    public boolean exist(String pDBSchema) throws WorkflowException, SQLException {
	// String sql = "SELECT schema_name FROM information_schema.schemata WHERE
	// schema_name = '" + pDBSchema + "'";
	String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA LIKE '" + pDBSchema + "'  AND TABLE_NAME ILIKE 'repo_detail'";
	ResultSet rs = null;
	try (Connection conn = Postgres.getInstance().getConnection(); Statement lStatement = conn.createStatement()) {
	    rs = lStatement.executeQuery(sql);
	    if (rs.next()) {
		return rs.getString(1).equals("repo_detail");
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing schema exist", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return false;
    }

    public void createSchema(List<String> DBQueries) throws WorkflowException, SQLException {
	try (Connection conn = Postgres.getInstance().getConnection(); Statement lStatement = conn.createStatement()) {
	    for (String DBQuery : DBQueries) {
		if (!DBQuery.isEmpty()) {
		    LOG.info("Executing : " + DBQuery);
		    lStatement.executeUpdate(DBQuery);
		}
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing create schema", e);
	}
    }

    public void updateRepoPermissions() throws WorkflowException, SQLException {
	try (Connection conn = Postgres.getInstance().getConnection(); Statement lStatement = conn.createStatement()) {
	    lStatement.executeUpdate("UPDATE " + Main.lDBSchema + ".repo_detail SET default_permission = 'READ_WRITE' where default_permission is null");
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error on update permission", e);
	}
    }

    public List<String> getOldPermissions() throws WorkflowException, SQLException {
	List<String> lReturn = new ArrayList<>();
	String lQuery = "select concat('INSERT INTO git.user_details (user_name, repo_id, permission) VALUES (', '''', a.user_name, '''', ',', " + "'(SELECT id FROM git.repo_detail WHERE source_repo = ', '''', b.source_repo, '''', ')', ',', '''', a.permission, '''', ');')" + " from git.user_details a, git.repo_detail b" + " where b.id = a.repo_id" + " order by a.user_name";
	ResultSet rs = null;
	LOG.info("Logging Permissions");
	try (Connection conn = Postgres.getInstance().getConnection(); Statement lStatement = conn.createStatement()) {
	    rs = lStatement.executeQuery(lQuery);
	    while (rs.next()) {
		lReturn.add(rs.getString(1));
		LOG.info(rs.getString(1));
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while getting permission from git", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return lReturn;
    }

    public void populatePermissions(List<String> oldPermissions) throws WorkflowException, SQLException {
	try (Connection conn = Postgres.getInstance().getConnection(); Statement lStatement = conn.createStatement()) {
	    for (String oldPermission : oldPermissions) {
		lStatement.execute(oldPermission);
	    }
	    lStatement.execute("SELECT setval('git.user_details_id_seq', max(id)) FROM git.user_details");
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error on storing permission in git", e);
	}
    }

    public void switchData() throws WorkflowException, SQLException {
	try (Connection conn = Postgres.getInstance().getConnection(); Statement lStatement = conn.createStatement()) {
	    lStatement.execute("truncate table git.repo_detail cascade");

	    lStatement.execute("INSERT INTO git.repo_detail SELECT * FROM " + Main.lDBSchema + ".repo_detail");
	    lStatement.execute("SELECT setval('git.repo_detail_id_seq', max(id)) FROM git.repo_detail");
	    lStatement.execute("INSERT INTO git.sub_repo_detail SELECT * FROM " + Main.lDBSchema + ".sub_repo_detail");
	    lStatement.execute("SELECT setval('git.sub_repo_detail_id_seq', max(id)) FROM git.sub_repo_detail");
	    lStatement.execute("INSERT INTO git.repo_file_list SELECT * FROM " + Main.lDBSchema + ".repo_file_list");
	    lStatement.execute("SELECT setval('git.repo_file_list_id_seq', max(id)) FROM git.repo_file_list");
	    lStatement.execute("INSERT INTO git.repo_commit SELECT * FROM " + Main.lDBSchema + ".repo_commit");
	    lStatement.execute("SELECT setval('git.repo_commit_id_seq', max(id)) FROM git.repo_commit");
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error on storing permission in git", e);
	}
    }
}

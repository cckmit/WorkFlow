/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.DAO;

import com.tsi.workflow.Main;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.database.Postgres;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.recon.git.GitBranchSearchResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author DINESH.RAMANATHAN
 */
public class RepoCommitDAO {

    public void saveRepoCommitList(List<GitBranchSearchResult> repoCommitList) throws WorkflowException, SQLException {
	String REPO_FILE_LIST = "INSERT INTO " + Main.lDBSchema + ".REPO_COMMIT(SUB_REPO_ID, FILE_ID, SOURCE_COMMIT_ID, COMMITTER_NAME, COMMITTER_MAIL_ID, COMMIT_DATE_TIME, REF_PLAN, REF_STATUS, REF_LOAD_DATE_TIME, FILE_HASHCODE, CREATED_DT) VALUES(?,?,?,?,?,?,?,?,?,?,now())";
	Connection conn = Postgres.getInstance().getConnection();
	try (PreparedStatement statement = conn.prepareStatement(REPO_FILE_LIST)) {
	    int itemNoInBatch = 0;
	    Set<String> subRepoAndCommitId = new HashSet<>();
	    for (GitBranchSearchResult repoCommitDTO : repoCommitList) {
		int counter = 0;
		statement.setInt(++counter, repoCommitDTO.getSubRepoId());
		statement.setInt(++counter, repoCommitDTO.getFileId());
		statement.setString(++counter, repoCommitDTO.getCommitId());
		statement.setString(++counter, repoCommitDTO.getCommitterName());
		statement.setString(++counter, repoCommitDTO.getCommitterMailId());
		statement.setTimestamp(++counter, new java.sql.Timestamp(repoCommitDTO.getCommitDateTime().getTime()));
		statement.setString(++counter, repoCommitDTO.getRefPlan());
		statement.setString(++counter, repoCommitDTO.getRefStatus());
		statement.setTimestamp(++counter, new java.sql.Timestamp(repoCommitDTO.getRefLoadDate().getTime()));
		statement.setString(++counter, repoCommitDTO.getFileHashCode());

		if (!CacheClient.getInstance().getAllCommitNames().stream().filter(x -> (repoCommitDTO.getSubRepoId() + "|" + repoCommitDTO.getCommitId()).equalsIgnoreCase(x)).findAny().isPresent()) {
		    statement.addBatch();
		    itemNoInBatch++;
		}
		subRepoAndCommitId.add(repoCommitDTO.getSubRepoId() + "|" + repoCommitDTO.getCommitId());
		if (itemNoInBatch % 10000 == 0) {
		    statement.executeBatch();
		    statement.clearBatch();
		    itemNoInBatch = 0;
		}
	    }
	    CacheClient.getInstance().getAllCommitNames().addAll(subRepoAndCommitId);
	    if (itemNoInBatch > 0) {
		statement.executeBatch();
		statement.clearBatch();
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing saveRepoCommitList", e);
	} finally {
	    conn.close();
	}
    }

    public List<String> getFilesCommitList() throws SQLException {
	String REPO_SUB_ID_AND_COMMIT = "SELECT DISTINCT (CONCAT(SUB_REPO_ID,'|',SOURCE_COMMIT_ID)) AS SUB_ID_AND_COMMIT FROM " + Main.lDBSchema + ".REPO_COMMIT";
	ResultSet rs = null;
	List<String> subIdAndCommitMap = new ArrayList<>();
	Connection conn = Postgres.getInstance().getConnection();
	try (PreparedStatement stmt = conn.prepareStatement(REPO_SUB_ID_AND_COMMIT)) {
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		subIdAndCommitMap.add(rs.getString("SUB_ID_AND_COMMIT"));
	    }
	} catch (SQLException e) {
	    throw new SQLException("Error in getRepoFileList() - ", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	    conn.close();
	}
	return subIdAndCommitMap;
    }

    public void updateRepoCommitList(List<GitBranchSearchResult> repoCommitList) throws WorkflowException, SQLException {
	String REPO_FILE_LIST = "update git.repo_commit set REF_STATUS=?, modified_dt=now() where SUB_REPO_ID=? and FILE_ID=? and SOURCE_COMMIT_ID=?";
	Connection conn = Postgres.getInstance().getConnection();
	try (PreparedStatement statement = conn.prepareStatement(REPO_FILE_LIST)) {
	    int itemNoInBatch = 0;

	    for (GitBranchSearchResult repoCommitDTO : repoCommitList) {
		int counter = 0;
		statement.setString(++counter, repoCommitDTO.getRefStatus());
		statement.setInt(++counter, repoCommitDTO.getSubRepoId());
		statement.setInt(++counter, repoCommitDTO.getFileId());
		statement.setString(++counter, repoCommitDTO.getCommitId());
		statement.addBatch();
		itemNoInBatch++;
		if (itemNoInBatch % 10000 == 0) {
		    statement.executeBatch();
		    statement.clearBatch();
		    itemNoInBatch = 0;
		}
	    }
	    if (itemNoInBatch > 0) {
		statement.executeBatch();
		statement.clearBatch();
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing updateRepoCommitList", e);
	} finally {
	    conn.close();
	}
    }
}

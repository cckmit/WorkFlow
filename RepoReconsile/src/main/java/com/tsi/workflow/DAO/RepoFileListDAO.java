/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.DAO;

import com.tsi.workflow.Main;
import com.tsi.workflow.database.Postgres;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.recon.git.GitMetaResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author DINESH.RAMANATHAN
 */
public class RepoFileListDAO {

    public HashMap<String, Integer> getFilesRepoMap() throws WorkflowException, SQLException {
	String REPO_DETAILS = "SELECT CONCAT(a.FILE_NAME,'|',a.TARGET_SYSTEM) as FILE_NAME, a.SUB_REPO_ID, b.sub_source_repo FROM " + Main.lDBSchema + ".REPO_FILE_LIST a, " + Main.lDBSchema + ".SUB_REPO_DETAIL b where a.SUB_REPO_ID = b.id and a.is_deleted = 'N'";
	HashMap<String, Integer> lFileNamesMap = new HashMap<>();
	ResultSet rs = null;
	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(REPO_DETAILS)) {
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		String key = rs.getString("FILE_NAME");
		if (rs.getString("sub_source_repo").contains("/ibm")) {
		    key = key + "|" + rs.getString("sub_source_repo");
		}
		lFileNamesMap.put(key, rs.getInt("SUB_REPO_ID"));
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error while executing getFilesList", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return lFileNamesMap;
    }

    public void saveRepoFileList(List<GitMetaResult> repoFileList) throws WorkflowException, SQLException {
	String REPO_FILE_LIST = "INSERT INTO " + Main.lDBSchema + ".REPO_FILE_LIST(SUB_REPO_ID, FILE_NAME, PROGRAM_NAME, FILE_EXT, TARGET_SYSTEM, CREATED_DT) VALUES (?,?,?,?,?, now())";

	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement statement = conn.prepareStatement(REPO_FILE_LIST)) {
	    int itemNoInBatch = 0;
	    for (GitMetaResult repoFile : repoFileList) {
		int counter = 0;
		statement.setInt(++counter, repoFile.getSubRepoId());
		statement.setString(++counter, repoFile.getFileName());
		statement.setString(++counter, repoFile.getProgramName());
		statement.setString(++counter, FilenameUtils.getExtension(repoFile.getProgramName()));
		statement.setString(++counter, repoFile.getTargetSystem());

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
	    throw new WorkflowException("Error while executing saveSubRepoDetails", e);
	}
    }

    public List<GitMetaResult> getRepoFileList(int pRepoId) throws WorkflowException, SQLException {
	String REPO_FILE_LIST = "SELECT ID, SUB_REPO_ID, FILE_NAME, TARGET_SYSTEM FROM " + Main.lDBSchema + ".REPO_FILE_LIST WHERE SUB_REPO_ID = ? and is_deleted = 'N'";
	ResultSet rs = null;
	List<GitMetaResult> repoFileList = new ArrayList<>();

	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(REPO_FILE_LIST)) {
	    stmt.setInt(1, pRepoId);
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		// new GitMetaResult from DB
		GitMetaResult repoFileListDTO = new GitMetaResult(rs.getInt("ID"), rs.getInt("SUB_REPO_ID"), rs.getString("TARGET_SYSTEM"), rs.getString("FILE_NAME"));
		repoFileList.add(repoFileListDTO);
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error in getRepoFileList() - ", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return repoFileList;
    }

    public HashMap<String, String> getCommitIdBasedOnFileAndSubRepo(String fileName, Integer subRepoId) throws WorkflowException, SQLException {
	StringBuilder sb = new StringBuilder();
	HashMap<String, String> fileAndSubRepoBasedCommitId = new HashMap<>();
	sb.append("select c.target_system,c.file_name, a.sub_repo_id, a.source_commit_id,a.commit_date_time, a.commit_rank from ( ");
	sb.append(" select fl.target_system, rc.file_id, rc.sub_repo_id, rc.source_commit_id, rc.commit_date_time, rank() over (partition by rc.file_id, rc.sub_repo_id, fl.target_system order by");
	sb.append(" rc.commit_date_time desc)as  commit_rank from ").append(Main.lDBSchema).append(".repo_commit rc, ").append(Main.lDBSchema).append(".repo_file_list fl where fl.id = rc.file_id) as a ,");
	sb.append("( select aa.target_system, aa.file_id, aa.sub_repo_id, max(aa.commit_rank) as max_rank from (select");
	sb.append(" fl.target_system, rc.file_id, rc.sub_repo_id, rank() over (partition by rc.file_id, rc.sub_repo_id, fl.target_system");
	sb.append(" order by rc.commit_date_time desc)as  commit_rank from ").append(Main.lDBSchema).append(".repo_commit rc, ").append(Main.lDBSchema).append(".repo_file_list fl where fl.id = rc.file_id) as aa");
	sb.append(" group by aa.file_id, aa.sub_repo_id, aa.target_system) as b, ").append(Main.lDBSchema).append(".repo_file_list c ");
	sb.append(" where a.file_id = b.file_id and a.sub_repo_id = b.sub_repo_id and a.commit_rank = b.max_rank and a.target_system = b.target_system and c.id = a.file_id and c.file_name = ? ");
	sb.append(" and a.sub_repo_id =? and a.commit_rank <=3 order by a.commit_rank desc ");

	ResultSet rs = null;

	try (Connection conn = Postgres.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sb.toString())) {
	    stmt.setString(1, fileName);
	    stmt.setInt(2, subRepoId);
	    stmt.setFetchSize(100000);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		fileAndSubRepoBasedCommitId.put(rs.getString("file_name") + "-" + rs.getInt("sub_repo_id") + "-" + rs.getString("target_system"), rs.getString("source_commit_id"));
	    }
	} catch (WorkflowException e) {
	    throw new WorkflowException("Error in getRepoFileList() - ", e);
	} finally {
	    if (rs != null) {
		rs.close();
	    }
	}
	return fileAndSubRepoBasedCommitId;
    }
}

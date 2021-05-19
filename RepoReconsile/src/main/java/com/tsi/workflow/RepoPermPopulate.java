/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.gitblit.Constants.AccessPermission;
import com.gitblit.models.RepositoryModel;
import com.gitblit.models.UserModel;
import com.gitblit.utils.RpcUtils;
import com.tsi.workflow.config.AppConfig;
import com.tsi.workflow.database.Postgres;
import com.tsi.workflow.utils.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author prabhu.prabhakaran
 */
public class RepoPermPopulate {

    public static void main(String[] args) throws Exception {
	BasicConfigurator.configure();
	System.out.println(AppConfig.getInstance().getGitblitURL());
	System.out.println(AppConfig.getInstance().getServiceUserName());
	Map<String, RepositoryModel> gitRepositoryList = RpcUtils.getRepositories(AppConfig.getInstance().getGitblitURL(), AppConfig.getInstance().getServiceUserName(), AppConfig.getInstance().getServicePassword().toCharArray());
	List<UserModel> usersList = RpcUtils.getUsers(AppConfig.getInstance().getGitblitURL(), AppConfig.getInstance().getServiceUserName(), AppConfig.getInstance().getServicePassword().toCharArray());

	String trunc = "DELETE FROM " + Main.lDBSchema + ".USER_DETAILS";
	Connection conn = Postgres.getInstance().getConnection();
	PreparedStatement stmt = conn.prepareStatement(trunc);
	stmt.executeUpdate();
	stmt.close();

	for (Map.Entry<String, RepositoryModel> entry : gitRepositoryList.entrySet()) {
	    RepositoryModel lRepository = entry.getValue();
	    String lUpdate = "UPDATE " + Main.lDBSchema + ".REPO_DETAIL SET default_permission = ? WHERE source_repo = ?";
	    stmt = conn.prepareStatement(lUpdate);
	    stmt.setString(1, getCurrentAccess(lRepository));
	    stmt.setString(2, getTrimmedName(lRepository.name).toLowerCase() + ".git");
	    int executeUpdate = stmt.executeUpdate();
	    if (executeUpdate > 0) {
		System.out.println("Updated for the repo" + getTrimmedName(lRepository.name).toLowerCase() + ".git" + " with access " + getCurrentAccess(lRepository));
	    }
	    stmt.close();

	    String lQuery = "SELECT id FROM " + Main.lDBSchema + ".REPO_DETAIL WHERE source_repo = ?";
	    stmt = conn.prepareStatement(lQuery);
	    stmt.setString(1, getTrimmedName(lRepository.name).toLowerCase() + ".git");
	    ResultSet lRs = stmt.executeQuery();
	    int lid = 0;
	    if (lRs.next()) {
		lid = lRs.getInt(1);
	    }
	    stmt.close();

	    String insert = "INSERT INTO " + Main.lDBSchema + ".USER_DETAILS (user_name, repo_id, permission) VALUES (?, ?, ?)";
	    if (lid > 0) {
		System.out.println("Repo id : " + lid);

		List<String> lowners = lRepository.owners;
		if (!lowners.isEmpty()) {
		    for (String lowner : lowners) {
			String lQuery1 = "SELECT count(id) FROM " + Main.lDBSchema + ".USER_DETAILS WHERE repo_id = ? and user_name = ?";
			stmt = conn.prepareStatement(lQuery1);
			stmt.setInt(1, lid);
			stmt.setString(2, lowner);
			ResultSet lRs1 = stmt.executeQuery();
			int lcount = 0;
			if (lRs1.next()) {
			    lcount = lRs1.getInt(1);
			}
			stmt.close();

			if (lcount == 0) {
			    stmt = conn.prepareStatement(insert);
			    System.out.println("Adding User " + lowner + " as  OWNER");
			    stmt.setString(1, lowner);
			    stmt.setInt(2, lid);
			    stmt.setString(3, "OWNER");
			    stmt.executeUpdate();
			    stmt.close();
			}
		    }
		}

		for (UserModel userModel : usersList) {
		    String lQuery1 = "SELECT count(id) FROM " + Main.lDBSchema + ".USER_DETAILS WHERE repo_id = ? and user_name = ?";
		    stmt = conn.prepareStatement(lQuery1);
		    stmt.setInt(1, lid);
		    stmt.setString(2, userModel.username);
		    ResultSet lRs1 = stmt.executeQuery();
		    int lcount = 0;
		    if (lRs1.next()) {
			lcount = lRs1.getInt(1);
		    }
		    stmt.close();

		    if (lcount == 0) {
			stmt = conn.prepareStatement(insert);
			Map<String, AccessPermission> lPermissions = userModel.permissions;
			AccessPermission lpermission = lPermissions.get(lRepository.name);
			if (lpermission != null) {
			    System.out.println("Adding User " + userModel.username + " as  " + Constants.RepoPermission.getKey(lpermission.code));
			    stmt.setString(1, userModel.username);
			    stmt.setInt(2, lid);
			    stmt.setString(3, Constants.RepoPermission.getKey(lpermission.code));
			    stmt.executeUpdate();
			}
			stmt.close();
		    }
		}
	    }
	}
	conn.close();
    }

    public static String getTrimmedName(String pRepoName) {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(pRepoName)).toUpperCase();
	String lReturn1 = FilenameUtils.removeExtension(pRepoName).toUpperCase().replaceAll("\\d+$", "");
	String lReturn2 = FilenameUtils.removeExtension(pRepoName).toUpperCase();
	if (lRepoName.startsWith("NONIBM_") && lRepoName.length() > 10) {
	    return lReturn1;
	} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
	    return lReturn2;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
	    return lReturn1;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
	    return lReturn2;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
	    return lReturn1;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
	    return lReturn2;
	} else {
	    return lReturn1;
	}
    }

    public static String getCurrentAccess(RepositoryModel pModel) {
	String defaultAccess = Constants.RepoPermission.RESTRICTED.name();
	if (pModel.authorizationControl == com.gitblit.Constants.AuthorizationControl.AUTHENTICATED) {
	    defaultAccess = Constants.RepoPermission.READ_WRITE.name();
	} else if (pModel.accessRestriction == com.gitblit.Constants.AccessRestrictionType.PUSH && pModel.authorizationControl == com.gitblit.Constants.AuthorizationControl.NAMED) {
	    defaultAccess = Constants.RepoPermission.READ.name();
	}
	return defaultAccess;
    }
}

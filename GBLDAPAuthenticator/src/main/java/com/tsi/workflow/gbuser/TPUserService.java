/*
 * Copyright 2011 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tsi.workflow.gbuser;

import com.gitblit.ConfigUserService;
import com.gitblit.Constants;
import com.gitblit.IUserService;
import com.gitblit.manager.IRuntimeManager;
import com.gitblit.models.TeamModel;
import com.gitblit.models.UserModel;
import com.google.gson.Gson;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TPUserService implements IUserService {

    HazelcastInstance lClient;
    String key;
    String host;
    String port;
    Gson lGson;
    List<String> admins;
    ConcurrentHashMap<String, String> lCookies;
    protected final Logger logger = LoggerFactory.getLogger(ConfigUserService.class);

    public TPUserService() {
	lGson = new Gson();
	lCookies = new ConcurrentHashMap<>();
	logger.info("TPUserService Initialized");
    }

    @Override
    public void setup(IRuntimeManager runtimeManager) {
	// clientConfig.setConnectionAttemptLimit(10);
	// clientConfig.setConnectionAttemptPeriod(24 * 60);
	// clientConfig.setConnectionTimeout(5000);

	host = runtimeManager.getSettings().getString("wf.cache.server.host", "localhost");
	port = runtimeManager.getSettings().getString("wf.cache.server.port", "8451");
	key = runtimeManager.getSettings().getString("wf.cache.session.key", "");
	admins = runtimeManager.getSettings().getStrings("realm.ldap.admins", " ");

	logger.info("Connecting to " + host + ":" + port + " with key: " + key);

	ClientConfig clientConfig = new ClientConfig();
	clientConfig.addAddress(host + ":" + port);
	lClient = HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Override
    public String getCookie(UserModel model) {
	logger.info("in getCookie " + model.username);
	if (model.cookie.isEmpty()) {
	    logger.error("Empty Cookie for the user " + model.username);
	}
	return model.cookie;
    }

    @Override
    public UserModel getUserModel(char[] cookie) {
	logger.info("in getCookie " + new String(cookie));
	String lCookie = new String(cookie);
	if (!lCookie.isEmpty()) {
	    try {
		String userName = lCookie.split("-")[1];
		return getUserModel(userName);
	    } catch (Exception e) {
		logger.error("Invalid Cookie " + lCookie);
	    }
	} else {
	    logger.error("Empty Cookie");
	}
	return null;
    }

    @Override
    public UserModel getUserModel(String username) {
	// logger.info("in getUserModel " + username);
	UserModel lGBUserModel = null;
	com.tsi.workflow.gitblit.model.UserModel lWFUserModel = (com.tsi.workflow.gitblit.model.UserModel) lClient.getMap("ALL_USER" + key).get(username);
	if (lWFUserModel != null) {
	    lGBUserModel = lGson.fromJson(lGson.toJson(lWFUserModel), UserModel.class);
	    lGBUserModel.accountType = Constants.AccountType.LDAP;
	} else if (admins.contains(username)) {
	    lGBUserModel = new UserModel(username);
	    lGBUserModel.accountType = Constants.AccountType.PAM;
	    lGBUserModel.displayName = username.toUpperCase();
	}
	if (lGBUserModel != null) {
	    lGBUserModel.cookie = lCookies.getOrDefault(username, RandomStringUtils.randomAlphanumeric(32) + "-" + username);
	    lGBUserModel.password = "#externalAccount";
	    lCookies.put(username, lGBUserModel.cookie);

	    if (admins.contains(username)) {
		lGBUserModel.canAdmin = true;
		lGBUserModel.canCreate = true;
		lGBUserModel.disabled = false;
		lGBUserModel.isAuthenticated = true;
	    }
	}
	return lGBUserModel;
    }

    @Override
    public boolean updateUserModel(UserModel model) {
	logger.info("in updateUserModel " + model.username);
	UserModel lWFUserModel = getUserModel(model.username);
	String lOldModel = lGson.toJson(lWFUserModel);
	String lNewModel = lGson.toJson(model);
	logger.debug(StringUtils.difference(lNewModel, lOldModel));
	logger.debug(StringUtils.difference(lOldModel, lNewModel));
	return false;
    }

    @Override
    public boolean updateUserModels(Collection<UserModel> models) {
	logger.info("in updateUserModels " + models.size());
	for (UserModel model : models) {
	    UserModel lWFUserModel = getUserModel(model.username);
	    String lOldModel = lGson.toJson(lWFUserModel);
	    String lNewModel = lGson.toJson(model);
	    logger.debug(StringUtils.difference(lNewModel, lOldModel));
	    logger.debug(StringUtils.difference(lOldModel, lNewModel));
	}
	return false;
    }

    @Override
    public boolean updateUserModel(String username, UserModel model) {
	logger.info("in updateUserModel " + username + " " + model.username);
	return false;
    }

    @Override
    public boolean deleteUserModel(UserModel model) {
	logger.info("in deleteUserModel " + model.username);
	return false;
    }

    @Override
    public boolean deleteUser(String username) {
	logger.info("in deleteUser " + username);
	return false;
    }

    @Override
    public List<String> getAllUsernames() {
	logger.info("in getAllUsernames");
	IMap<String, Object> lUserMap = lClient.getMap("ALL_USER" + key);
	return new ArrayList<>(lUserMap.keySet());
    }

    @Override
    public List<UserModel> getAllUsers() {
	logger.info("in getAllUsers");
	ArrayList<UserModel> lReturn = new ArrayList<>();
	IMap<String, com.tsi.workflow.gitblit.model.UserModel> lUserMap = lClient.getMap("ALL_USER" + key);
	for (com.tsi.workflow.gitblit.model.UserModel lWFUserModel : lUserMap.values()) {
	    UserModel lGBUserModel = lGson.fromJson(lGson.toJson(lWFUserModel), UserModel.class);
	    lReturn.add(lGBUserModel);
	}
	return lReturn;
    }

    @Override
    public List<String> getAllTeamNames() {
	logger.info("in getAllTeamNames");
	return new ArrayList<>();
    }

    @Override
    public List<TeamModel> getAllTeams() {
	logger.info("in getAllTeams");
	return new ArrayList<>();
    }

    @Override
    public List<String> getTeamNamesForRepositoryRole(String role) {
	logger.info("in getTeamNamesForRepositoryRole " + role);
	return new ArrayList<>();
    }

    @Override
    public TeamModel getTeamModel(String teamname) {
	logger.info("in getTeamModel " + teamname);
	return null;
    }

    @Override
    public boolean updateTeamModel(TeamModel model) {
	logger.info("in updateTeamModel " + model.name);
	return false;
    }

    @Override
    public boolean updateTeamModels(Collection<TeamModel> models) {
	logger.info("in updateTeamModels " + models.size());
	return false;
    }

    @Override
    public boolean updateTeamModel(String teamname, TeamModel model) {
	logger.info("in updateTeamModel " + teamname + " " + model.name);
	return false;
    }

    @Override
    public boolean deleteTeamModel(TeamModel model) {
	logger.info("in deleteTeamModel " + model.name);
	return false;
    }

    @Override
    public boolean deleteTeam(String teamname) {
	logger.info("in deleteTeam " + teamname);
	return false;
    }

    @Override
    public List<String> getUsernamesForRepositoryRole(String role) {
	logger.info("in getUsernamesForRepositoryRole " + role);
	return new ArrayList<>();

    }

    @Override
    public boolean renameRepositoryRole(String oldRole, String newRole) {
	logger.info("in renameRepositoryRole " + oldRole + " " + newRole);
	return false;
    }

    @Override
    public boolean deleteRepositoryRole(String role) {
	logger.info("in deleteRepositoryRole " + role);
	return false;
    }

}

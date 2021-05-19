/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.tsi.workflow.AppConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.ui.LoginForm;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.schedular.LdapCacheClear;
import com.tsi.workflow.service.LDAPService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Deepa
 */
@Controller
public class UserController extends BaseController {

    @Autowired
    LDAPAuthenticatorImpl lLDAPAuthenticatorImpl;

    @Autowired
    LdapGroupConfig ldapGroupConfig;

    @Autowired
    private LDAPService lDAPService;

    @Autowired
    LdapCacheClear ldapCacheClear;

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JSONResponse login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm loginForm) {
	return lDAPService.doLogin(loginForm.getUsername(), loginForm.getPassword());
    }

    // @ResponseBody
    // @RequestMapping(value = "/ssoLogin", method = RequestMethod.POST)
    // public JSONResponse ssoLogin(HttpServletRequest request, HttpServletResponse
    // response) {
    // JSONResponse lResponse = new JSONResponse();
    // User currentUser = getCurrentUser(request, response);
    // lResponse.setData(currentUser);
    // lResponse.setStatus(Boolean.TRUE);
    // return lResponse;
    // }
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JSONResponse logout(HttpServletRequest request, HttpServletResponse response) {
	User lUserSession = this.removeCurrentUser(request, response);
	if (lUserSession != null) {
	    lDAPService.removeSuperUser(lUserSession);
	    LOG.info(StringUtils.repeat("+", 30));
	    LOG.info("User " + lUserSession.getDisplayName() + " logged Out, Role : " + String.join(", ", lUserSession.getRole()));
	    LOG.info(StringUtils.repeat("+", 30));
	}
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @RequestMapping(value = "/setRole", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse setRole(HttpServletRequest request, HttpServletResponse response, @RequestParam String role) {
	JSONResponse lResponse = new JSONResponse();
	User lCurrentUser = this.getCurrentUser(request, response);

	if (lCurrentUser == null) {
	    lResponse.setErrorMessage("No User Session Found");
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}
	User currentOrDelagateUser = lCurrentUser.getCurrentOrDelagateUser();
	if (!currentOrDelagateUser.getRole().contains(role)) {
	    lResponse.setErrorMessage(currentOrDelagateUser.getDisplayName() + " doesn't have the role " + role);
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}
	currentOrDelagateUser.setCurrentRole(role);
	LOG.info("Changing User to " + currentOrDelagateUser.getDisplayName() + " to role " + currentOrDelagateUser.getCurrentRole());

	lCurrentUser.setAllowDelegateMenu(Constants.UserGroup.getDelegatedUserList().contains(lCurrentUser.getCurrentRole()));
	this.updateCurrentUser(request, response, lCurrentUser);
	lResponse.setData(lCurrentUser);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @RequestMapping(value = "/setDelegate", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse setDelegate(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId, @RequestParam(required = false, defaultValue = "false") Boolean force) {
	// SwitchDelegate
	User lUserSession = this.getCurrentUser(request, response);
	JSONResponse lResponse = lDAPService.switchDelegate(lUserSession, userId, force);
	this.updateCurrentUser(request, response, lUserSession);
	return lResponse;
    }

    @RequestMapping(value = "/setLogLevel", method = RequestMethod.GET)
    @ResponseBody
    public void setLogLevel(HttpServletRequest request, HttpServletResponse response, @RequestParam String level) {
	Logger.getRootLogger().setLevel(Level.toLevel(level.toUpperCase(), Level.ERROR));
    }

    @RequestMapping(value = "/getDelegationToUsersList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDelegationToUsersList(HttpServletRequest request, HttpServletResponse response) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	SortedSet<User> lResult = new TreeSet<>();
	List[] lGroupList = ldapGroupConfig.getDelegateGroupsUsersList();

	for (List<LDAPGroup> lGroups : lGroupList) {
	    lResult.addAll(lLDAPAuthenticatorImpl.getLinuxUsers(lGroups));
	}
	lResponse.setData(lResult);
	return lResponse;
    }

    @RequestMapping(value = "/getDelegationFromUsersList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDelegationFromUsersList(HttpServletRequest request, HttpServletResponse response) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	List[] lGroupList = ldapGroupConfig.getDelegateGroupsRoleList();

	SortedSet<User> lMembers = new TreeSet<>();
	for (List<LDAPGroup> lGroups : lGroupList) {
	    lMembers.addAll(lLDAPAuthenticatorImpl.getLinuxUsers(lGroups));
	}

	lResponse.setData(lMembers);
	return lResponse;
    }

    @RequestMapping(value = "/getSuperUserToUsersList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSuperUserToUsersList(HttpServletRequest request, HttpServletResponse response) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	SortedSet<User> lResult = new TreeSet<>();
	List[] lGroupList = ldapGroupConfig.getSuperUserGroupsUsersList();

	for (List<LDAPGroup> lGroups : lGroupList) {
	    lResult.addAll(lLDAPAuthenticatorImpl.getLinuxUsers(lGroups));
	}
	lResponse.setData(lResult);
	return lResponse;
    }

    @RequestMapping(value = "/getSuperUserFromUsersList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSuperUserFromUsersList(HttpServletRequest request, HttpServletResponse response) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	List[] lGroupList = ldapGroupConfig.getSuperUserGroupsRoleList();

	SortedSet<User> lMembers = new TreeSet<>();
	for (List<LDAPGroup> lGroups : lGroupList) {
	    lMembers.addAll(lLDAPAuthenticatorImpl.getLinuxUsers(lGroups));
	}

	lResponse.setData(lMembers);
	return lResponse;
    }

    @RequestMapping(value = "/setAllowLoadAnyTime", method = RequestMethod.GET)
    @ResponseBody
    public void allowLoadAnyTime(HttpServletRequest request, HttpServletResponse response, @RequestParam boolean loadAnyTime) {
	Constants.isAllowLoadAnyTime = loadAnyTime;
    }

    @RequestMapping(value = "/setFallbackDate", method = RequestMethod.GET)
    @ResponseBody
    public void allowAllUsers(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer interval) {
	Constants.fallbackLoadDateGap = interval;
    }

    @RequestMapping(value = "/WFInfo", method = RequestMethod.POST)
    @ResponseBody
    public SortedMap<String, Map> bgInfo(Model model, HttpServletRequest request, HttpServletResponse response) {
	SortedMap<String, Map> lReturn = new TreeMap<>();
	{
	    Properties properties = System.getProperties();
	    HashMap<String, String> lPropertyMap = new HashMap<>();
	    for (final String name : properties.stringPropertyNames()) {
		lPropertyMap.put(name, properties.getProperty(name));
	    }
	    lReturn.put("RA_SYS_PROP", lPropertyMap);
	    Map<String, String> env = System.getenv();
	    lReturn.put("RA_SYS_ENV_PROP", env);
	}
	try {
	    Properties properties = new Properties();
	    properties.load(AppConfig.class.getResourceAsStream("/app.properties"));
	    lReturn.put("API_PROP", properties);
	    properties = new Properties();
	    properties.load(AppConfig.class.getResourceAsStream("/log4j-api.properties"));
	    lReturn.put("API_LOG", properties);
	    properties = new Properties();
	    properties.load(AppConfig.class.getResourceAsStream("/log4j-jgit.properties"));
	    lReturn.put("JGIT_LOG", properties);
	    properties = new Properties();
	    properties.load(AppConfig.class.getResourceAsStream("/log4j-tos.properties"));
	    lReturn.put("TOS_LOG", properties);
	} catch (Exception e) {

	}
	return lReturn;
    }
}

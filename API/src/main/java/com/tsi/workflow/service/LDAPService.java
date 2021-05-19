/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.DelegateHelper;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.EncryptUtil;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
public class LDAPService {

    private static final Logger LOG = Logger.getLogger(LDAPService.class.getName());

    @Autowired
    LdapGroupConfig ldapGroupConfig;
    @Autowired
    CacheClient lCacheClient;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    UserSettingsDAO userSettingsDAO;
    @Autowired
    DelegateHelper delegateHelper;
    @Autowired
    SSHClientUtils sSHClientUtils;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public LDAPService() {
    }

    public DelegateHelper getDelegateHelper() {
	return delegateHelper;
    }

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public void setLdapGroupConfig(LdapGroupConfig ldapGroupConfig) {
	this.ldapGroupConfig = ldapGroupConfig;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return lDAPAuthenticatorImpl;
    }

    public void setLDAPAuthenticatorImpl(LDAPAuthenticatorImpl lDAPAuthenticatorImpl) {
	this.lDAPAuthenticatorImpl = lDAPAuthenticatorImpl;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public void setGITConfig(GITConfig gITConfig) {
	this.gITConfig = gITConfig;
    }

    public UserSettingsDAO getUserSettingsDAO() {
	return userSettingsDAO;
    }

    public void setUserSettingsDAO(UserSettingsDAO userSettingsDAO) {
	this.userSettingsDAO = userSettingsDAO;
    }

    public JSONResponse getUsersByRole(String role) {
	JSONResponse lResponse = new JSONResponse();
	List<LDAPGroup> lGroups = getLdapGroupConfig().getLdapRolesMap().get(role);
	if (lGroups != null && !lGroups.isEmpty()) {
	    SortedSet<User> lUsers = getLDAPAuthenticatorImpl().getLinuxUsers(lGroups);
	    if (lUsers == null || lUsers.isEmpty()) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("No Users Found");
	    } else {
		lResponse.setStatus(Boolean.TRUE);
		lResponse.setData(lUsers);
	    }
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("No Users Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse doLogin(String pUserName, String pSecret) {
	JSONResponse lResponse = new JSONResponse();
	if (!Constants.MTPSERVICE_KEY_MAP) {
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Application is starting up... Please wait");
	    return lResponse;
	}

	User lUser = new User(pUserName);
	lUser.setTimeZone(TimeZone.getDefault().getID());
	Constants.LoginErrorCode lReturn = getLDAPAuthenticatorImpl().validate(lUser, pSecret);

	if (wFConfig.getMaintenance() && !getLDAPAuthenticatorImpl().isAllowedOnMaintanence(lUser)) {
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Application is in Maintenance... Please wait");
	    return lResponse;
	}

	if (lReturn != Constants.LoginErrorCode.SUCCESS) {
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage(lReturn.getDescription());
	    return lResponse;
	}
	LOG.info("LDAP validation success for the User " + lUser.getDisplayName());
	lUser.setEncryptedPassword(pSecret);
	String lEncoded = EncryptUtil.encrypt(lUser.getId(), Constants.SECRET_HASH);
	populateSettings(lUser);
	lCacheClient.getLoginMap().put(lEncoded, lUser);
	lResponse.setStatus(true);
	lResponse.setData(lUser);
	lResponse.setMetaData(lEncoded);

	LOG.info(StringUtils.repeat("*", 30));
	LOG.info("User " + lUser.getDisplayName() + " logged In, Role : " + String.join(", ", lUser.getRole()));
	LOG.info(StringUtils.repeat("*", 30));
	return lResponse;
    }

    public void setLogLevel(String plevel) {
	Logger.getRootLogger().setLevel(Level.toLevel(plevel));
	Enumeration allAppenders = Logger.getRootLogger().getAllAppenders();
	while (allAppenders.hasMoreElements()) {
	    ConsoleAppender lAppender = (ConsoleAppender) allAppenders.nextElement();
	    lAppender.setThreshold(Priority.toPriority(plevel));
	}
    }

    private void populateSettings(User pUser) {
	UserSettings lMyDelegateSetting = getUserSettingsDAO().find(pUser.getId(), Constants.UserSettings.DELEGATION.name());
	if (lMyDelegateSetting != null) {
	    if (lMyDelegateSetting.getValue().equals("TRUE")) {
		pUser.setDelegated(true);
	    }
	}
	getDelegateHelper().populateDelegations(pUser);
	removeSuperUser(pUser);
    }

    // @Transactional
    // public User doSSOAuthentication(HttpServletRequest request) {
    // if (!Constants.MTPSERVICE_KEY_MAP) {
    // return null;
    // }
    // User user = new User();
    // user.setTimeZone(TimeZone.getDefault().getID());
    // user.setDisplayName(getParam(request, Constants.SSOHeaders.GECOS));
    //
    // //TODO: need to check for SSO UID
    // user.setId(getParamArray(request, Constants.SSOHeaders.SM_UNIVERSALID)[0]);
    // user.setMailId(getParam(request, Constants.SSOHeaders.MAIL));
    // String lParam = getParam(request, Constants.SSOHeaders.AUTHORIZATION);
    // String pass = new String(Base64.getDecoder().decode(lParam.replace("Basic",
    // "").trim())).split("\\:")[1];
    // user.setEncryptedPassword(pass);
    //
    // Constants.LoginErrorCode lReturn =
    // lDAPAuthenticatorImpl.getLinuxGroupDetails(user);
    //
    // if (lReturn != Constants.LoginErrorCode.SUCCESS) {
    // return null;
    // }
    //
    // populateSettings(user);
    //
    // LOG.info(StringUtils.repeat("*", 30));
    // WFLOGGER.LOG(LDAPService.class, Level.INFO, "User " + user.getDisplayName() +
    // " logged In via SSO, Role : " +
    // String.join(", ", user.getRole()));
    // LOG.info(StringUtils.repeat("*", 30));
    // return user;
    // }
    //
    // private static String getParam(HttpServletRequest request,
    // Constants.SSOHeaders pHeader) {
    // return request.getHeader(pHeader.getKey());
    // }
    //
    // private static String[] getParamArray(HttpServletRequest request,
    // Constants.SSOHeaders pHeader) {
    // return getParam(request, pHeader).split("\\^");
    // }
    @Transactional
    public JSONResponse switchDelegate(User pUser, String changeToUser, Boolean force) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	if (force) {
	    pUser.getDelegations().clear();
	    getDelegateHelper().populateDelegations(pUser);
	    getDelegateHelper().populateSuperuser(pUser);
	}
	if (changeToUser.equals(pUser.getId())) {
	    pUser.setCurrentDelegatedUser(null);
	    LOG.info("Changing User to " + pUser.getCurrentOrDelagateUser().getDisplayName());
	} else {
	    User lUser = pUser.getDelegations().get(changeToUser);
	    if (lUser != null) {
		pUser.setCurrentDelegatedUser(lUser);
		LOG.info("Changing User to " + pUser.getCurrentOrDelagateUser().getDisplayName());
	    } else {
		pUser.setCurrentDelegatedUser(null);
		LOG.info("Changing User to " + pUser.getCurrentOrDelagateUser().getDisplayName());
	    }
	}
	pUser.setAllowDelegateMenu(Constants.UserGroup.getDelegatedUserList().contains(pUser.getCurrentRole()));
	lResponse.setData(pUser);
	return lResponse;
    }

    public void removeSuperUser(User lUserSession) {
	getDelegateHelper().removeSuperUser(lUserSession);
    }

    public JSONResponse getLoadAttendeeUsers(User pUser) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    SortedSet<User> lUsers = new TreeSet<>();
	    for (String lRole : Constants.UserGroup.getLoadAttendeeUserList()) {
		List<LDAPGroup> lGroups = getLdapGroupConfig().getLdapRolesMap().get(lRole);
		lUsers.addAll(getLDAPAuthenticatorImpl().getLinuxUsers(lGroups));
	    }
	    if (!lUsers.isEmpty()) {
		lResponse.setData(lUsers);
		lResponse.setStatus(Boolean.TRUE);
	    } else {
		lResponse.setStatus(Boolean.FALSE);
	    }

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("getLoadAttendeeUsers, Unable to get Users list", ex);
	    throw new WorkflowException("Unable to get Users Information");
	}
	return lResponse;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.google.gson.Gson;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.cache.DelegateCache;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.mail.DelegateActivationMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class DelegateHelper {

    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    UserSettingsDAO userSettingsDAO;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    CacheClient lCacheClient;
    @Autowired
    @Qualifier("lSuperUserMap")
    ConcurrentHashMap<String, SortedSet<User>> lSuperUserMap;
    @Autowired
    @Qualifier("lDelegationMap")
    ConcurrentHashMap<String, DefaultMutableTreeNode> lDelegationMap;
    @Autowired
    WSMessagePublisher wsserver;

    private static final Logger LOG = Logger.getLogger(DelegateHelper.class.getName());

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public UserSettingsDAO getUserSettingsDAO() {
	return userSettingsDAO;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return lDAPAuthenticatorImpl;
    }

    public void onActivateSuperUser(User lUser, UserSettings pUserSetting) {
	User lToUser = getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getValue()); // arul
	User lFromUser = getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getUserId()); // aravinth
	DelegateActivationMail lMail = (DelegateActivationMail) mailMessageFactory.getTemplate(DelegateActivationMail.class);
	lMail.setSuperuser(Boolean.TRUE);
	lMail.setAssignee(lToUser.getDisplayName());
	lMail.setFromUser(lFromUser.getDisplayName());
	lMail.addToAddressUserId(lToUser.getId(), Constants.MailSenderRole.NO_ROLE);
	lMail.addToAddressUserId(lFromUser.getId(), Constants.MailSenderRole.NO_ROLE);
	mailMessageFactory.push(lMail);
	if (lSuperUserMap.containsKey(lToUser.getId())) {
	    lSuperUserMap.get(lToUser.getId()).add(lFromUser);
	} else {
	    TreeSet<User> treeSet = new TreeSet<>();
	    treeSet.add(lFromUser);
	    lSuperUserMap.put(lToUser.getId(), treeSet);
	}
	LOG.info("Delegating User " + lFromUser.getDisplayName() + " to Super User " + lToUser.getDisplayName());

	wsserver.sendMessage(Constants.Channels.DELEGATION, lFromUser, lToUser);
    }

    public void onActivateDelegate(User lUser, UserSettings pUserSetting) {
	UserSettings lUserSetting = getUserSettingsDAO().find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name());
	if (lUserSetting != null) {
	    SortedSet<String> lDelegatedUsers = getFromCache(lUserSetting.getValue());
	    lDelegatedUsers.add(lUserSetting.getUserId());
	    DefaultMutableTreeNode lFromNode = lDelegationMap.get(lUserSetting.getUserId());
	    String lRoot = ((DefaultMutableTreeNode) lFromNode.getRoot()).getUserObject().toString();
	    User lRootUser = getLDAPAuthenticatorImpl().getUserDetails(lRoot);
	    getImpPlanDAO().setPlanDelegate(lRootUser, lDelegatedUsers);
	    User lAssignedUser = getLDAPAuthenticatorImpl().getUserDetails(lUserSetting.getValue());
	    User lFromUser = getLDAPAuthenticatorImpl().getUserDetails(lUserSetting.getUserId());
	    DelegateActivationMail lMail = (DelegateActivationMail) mailMessageFactory.getTemplate(DelegateActivationMail.class);
	    lMail.setActivated(true);
	    lMail.setAssignee(lAssignedUser.getDisplayName());
	    lMail.setFromUser(lFromUser.getDisplayName());
	    lMail.addToAddressUserId(lAssignedUser.getId(), Constants.MailSenderRole.NO_ROLE);
	    lMail.addToAddressUserId(lFromUser.getId(), Constants.MailSenderRole.NO_ROLE);
	    mailMessageFactory.push(lMail);
	    wsserver.sendMessage(Constants.Channels.DELEGATION, lUser, lAssignedUser);
	} else {
	    throw new WorkflowException("No User Setting found for the User " + pUserSetting.getUserId());
	}
    }

    public void onDeActivateDelegate(User lUser, UserSettings pUserSetting) {
	UserSettings UserSetting = getUserSettingsDAO().find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name());
	if (UserSetting != null) {
	    User lFromUser = getLDAPAuthenticatorImpl().getUserDetails(UserSetting.getUserId());
	    SortedSet<String> lDelegatedUsers = getFromCache(UserSetting.getUserId());
	    lDelegatedUsers.add(UserSetting.getUserId());
	    getImpPlanDAO().setPlanDelegate(lFromUser, lDelegatedUsers);
	    getImpPlanDAO().removePlanDelegate(UserSetting.getUserId());

	    User lAssignedUser = getLDAPAuthenticatorImpl().getUserDetails(UserSetting.getValue());
	    DelegateActivationMail lMail = (DelegateActivationMail) mailMessageFactory.getTemplate(DelegateActivationMail.class);
	    lMail.setActivated(false);
	    lMail.setAssignee(lAssignedUser.getDisplayName());
	    lMail.setFromUser(lFromUser.getDisplayName());
	    lMail.addToAddressUserId(lAssignedUser.getId(), Constants.MailSenderRole.NO_ROLE);
	    lMail.addToAddressUserId(lFromUser.getId(), Constants.MailSenderRole.NO_ROLE);
	    mailMessageFactory.push(lMail);
	    wsserver.sendMessage(Constants.Channels.DELEGATION, lUser, lAssignedUser);
	} else {
	    throw new WorkflowException("No User Setting found for the User " + pUserSetting.getUserId());
	}
    }

    public void onAssignDelegate(User lUser, UserSettings pUserSetting) {
	if (pUserSetting.getValue() != null && !pUserSetting.getValue().isEmpty()) {
	    User lAssignedUser = getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getValue());

	}
    }

    public void populateDelegations(User pUser) {
	SortedSet<String> lDelegateSettings = getFromCache(pUser.getId());
	for (String lDelegateSetting : lDelegateSettings) {
	    User lUser = getLDAPAuthenticatorImpl().getUserDetails(lDelegateSetting);
	    getLDAPAuthenticatorImpl().setDelegatedLinuxGroupDetails(lUser);
	    if (!lUser.getRole().isEmpty()) {
		pUser.addDelegators(lUser.getId(), lUser);
	    }
	}
    }

    public Boolean addToCache(String from, String to, boolean publish) {
	if (from.equals(to)) {
	    return false;
	}
	LOG.info("Adding Delegation from " + from + " to " + to);
	DefaultMutableTreeNode lToUserNode = null;
	if (lDelegationMap.get(to) == null) {
	    LOG.info("Creating New Node for " + to);
	    lToUserNode = new DefaultMutableTreeNode(to);
	    lDelegationMap.put(to, lToUserNode);
	} else {
	    lToUserNode = lDelegationMap.get(to);
	}

	DefaultMutableTreeNode lFromUserNode = null;
	if (lDelegationMap.get(from) == null) {
	    LOG.info("Creating New Node for " + from);
	    lFromUserNode = new DefaultMutableTreeNode(from);
	    lDelegationMap.put(from, lFromUserNode);
	} else {
	    lFromUserNode = lDelegationMap.get(from);
	}

	if (!lToUserNode.isNodeAncestor(lFromUserNode)) {
	    LOG.info("Delegate Adding " + from + "," + to);
	    lToUserNode.add(lFromUserNode);
	    if (publish) {
		LOG.info("Delegate Publishing ADD " + from + "," + to);
		String toJson = new Gson().toJson(new DelegateCache(wFConfig.getProfileName(), "ADD", from, to));
		lCacheClient.getDelegationTopic().publish(toJson);
	    }

	    return true;
	}
	return false;
    }

    public SortedSet<String> getFromCache(String user) {
	DefaultMutableTreeNode lToUserNode = lDelegationMap.get(user);
	return getChildrensFromCache(lToUserNode);
    }

    public SortedSet<String> getParentsCache(String user) {
	DefaultMutableTreeNode lToUserNode = lDelegationMap.get(user);
	return getParentsFromCache(lToUserNode);
    }

    private void logNode(int x, DefaultMutableTreeNode lNode) {
	if (lNode == null) {
	    return;
	}
	for (int i = 0; i < lNode.getChildCount(); i++) {
	    DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) lNode.getChildAt(i);
	    LOG.info("Having Users at Level " + x + " Index " + i + " " + childAt.getUserObject().toString());
	    if (!lNode.isLeaf()) {
		logNode(x + 1, childAt);
	    }
	}
    }

    private SortedSet<String> getParentsFromCache(DefaultMutableTreeNode lNode) {
	SortedSet<String> lList = new TreeSet<>();
	Boolean lDelegationExists = true;
	if (lNode == null) {
	    return lList;
	}
	while (lNode.getParent() != null && lDelegationExists) {
	    lDelegationExists = false;
	    DefaultMutableTreeNode lParentNode = (DefaultMutableTreeNode) lNode.getParent();
	    lList.add(lParentNode.getUserObject().toString());
	    if (lParentNode.getParent() != null) {
		lList.addAll(getParentsFromCache(lParentNode));
	    }
	}
	return lList;
    }

    private SortedSet<String> getChildrensFromCache(DefaultMutableTreeNode lNode) {
	SortedSet<String> lList = new TreeSet<>();
	if (lNode == null) {
	    return lList;
	}
	for (int i = 0; i < lNode.getChildCount(); i++) {
	    DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) lNode.getChildAt(i);
	    lList.add(childAt.getUserObject().toString());
	    if (!lNode.isLeaf()) {
		lList.addAll(getChildrensFromCache(childAt));
	    }
	}
	return lList;
    }

    public void removeFromCache(String from, String to, boolean publish) {
	LOG.info("Delegate Removing " + from + ", " + to);
	DefaultMutableTreeNode lToUserNode = lDelegationMap.get(to);
	for (int i = 0; i < lToUserNode.getChildCount(); i++) {
	    DefaultMutableTreeNode lFromUserNode = (DefaultMutableTreeNode) lToUserNode.getChildAt(i);
	    if (lFromUserNode.getUserObject().equals(from)) {
		lFromUserNode.removeFromParent();

		break;
	    }
	}
	if (publish) {
	    LOG.info("Delegate Remove Publish " + from + ", " + to);
	    String toJson = new Gson().toJson(new DelegateCache(wFConfig.getProfileName(), "REMOVE", from, to));
	    lCacheClient.getDelegationTopic().publish(toJson);
	}
    }

    public void removeSuperUser(User lUserSession) {
	lSuperUserMap.remove(lUserSession.getId());
    }

    public void populateSuperuser(User pUser) {
	SortedSet<User> get = lSuperUserMap.get(pUser.getId());
	if (get != null) {
	    for (User lUser : get) {
		getLDAPAuthenticatorImpl().setSuperUserDelegatedLinuxGroupDetails(lUser);
		if (!lUser.getRole().isEmpty()) {
		    lUser.setCurrentRole(lUser.getRole().iterator().next());
		    pUser.addDelegators(lUser.getId(), lUser);
		}
	    }
	}
    }

}

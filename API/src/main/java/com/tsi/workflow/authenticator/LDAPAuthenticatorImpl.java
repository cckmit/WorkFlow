package com.tsi.workflow.authenticator;

import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.LoginErrorCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.InvalidNameException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class LDAPAuthenticatorImpl extends ADSAuthenticator {

    private static final Logger LOG = Logger.getLogger(LDAPAuthenticatorImpl.class.getName());
    @Autowired
    WFConfig wFConfig;
    @Autowired
    LdapGroupConfig ldapGroupConfig;
    @Autowired
    @Qualifier("lLdapUserMap")
    ConcurrentHashMap<String, User> lLdapUserMap;
    @Autowired
    @Qualifier("lLdapGroupMap")
    ConcurrentHashMap<String, SortedSet<User>> lLdapGroupMap;

    public LDAPAuthenticatorImpl() {
	super();
    }

    @Override
    public LoginErrorCode validate(Object pUserName, String pPassword) {
	return validateLDAPLogin((User) pUserName, pPassword);
    }

    public User getServiceUser() {
	User lServiceUser = getUserDetails(wFConfig.getServiceUserID());
	if (lServiceUser.getDisplayName() == null || lServiceUser.getDisplayName().isEmpty()) {
	    lServiceUser.setDisplayName("MTPSERVICE");
	    lServiceUser.setCurrentRole("Service User");
	}
	return lServiceUser;
    }

    public void setDelegatedLinuxGroupDetails(User pUser) {
	List<String> lMembers = new ArrayList<>();
	List[] lGroupList = ldapGroupConfig.getDelegateGroupsRoleList();
	for (List<LDAPGroup> lGroups : lGroupList) {
	    lMembers.clear();
	    for (LDAPGroup lGroup : lGroups) {
		lMembers.addAll(getLinuxUsers(lGroup));
	    }
	    if (lMembers.contains(pUser.getId())) {
		pUser.addRole(lGroups.get(0).getGroup().name());
	    }
	}
	if (pUser.getRole().isEmpty()) {
	    LOG.warn("No Roles defined for the User " + pUser.getDisplayName());
	}
	pUser.setCurrentRole(pUser.getRole().iterator().next());
    }

    public void setSuperUserDelegatedLinuxGroupDetails(User pUser) {
	List<String> lMembers = new ArrayList<>();
	List[] lGroupList = ldapGroupConfig.getSuperUserGroupsRoleList();
	for (List<LDAPGroup> lGroups : lGroupList) {
	    lMembers.clear();
	    for (LDAPGroup lGroup : lGroups) {
		lMembers.addAll(getLinuxUsers(lGroup));
	    }
	    if (lMembers.contains(pUser.getId())) {
		pUser.addRole(lGroups.get(0).getGroup().name());
	    }
	}
	if (pUser.getRole().isEmpty()) {
	    LOG.warn("No Roles defined for the User " + pUser.getDisplayName());
	}
	pUser.setCurrentRole(pUser.getRole().iterator().next());
    }

    public SortedSet<User> getLinuxUsers(List<LDAPGroup> pGroups) {
	LOG.info("Checking for the group " + pGroups.get(0).getGroup().name());
	if (Constants.UserGroup.getKeyMappingGroupList().contains(pGroups.get(0).getGroup().name())) {
	    LOG.info("Filtering group " + pGroups.get(0).getGroup().name());
	    List<LDAPGroup> serverAccessGroups = ldapGroupConfig.getServerAccessGroups();
	    SortedSet<User> lUsers = getAllLinuxUsers(serverAccessGroups);
	    return getLinuxUsers(pGroups, lUsers);
	}
	return getAllLinuxUsers(pGroups);
    }

    private SortedSet<User> getLinuxUsers(List<LDAPGroup> pGroups, SortedSet<User> pUsers) {
	SortedSet<User> lMembers = new TreeSet<>();
	for (LDAPGroup lGroup : pGroups) {
	    SortedSet<User> lUsers = lLdapGroupMap.get(lGroup.getLdapGroupName());
	    if (lUsers == null) {
		lUsers = new TreeSet<>();
		List<String> linuxUsers = getLinuxUsers(lGroup);
		LOG.info("User Id Count : " + linuxUsers.size() + " for the group " + lGroup.getGroup().name());
		for (String linuxUser : linuxUsers) {
		    User lUser = getUserDetails(linuxUser);
		    if (lUser.getDisplayName() != null && !lUser.getDisplayName().isEmpty() && pUsers.contains(lUser)) {
			LOG.debug("Adding User " + lUser.getDisplayName() + " to Group : " + lGroup.getLdapGroupName());
			lUsers.add(lUser);
			// } else {
			// LOG.error("No User details found for the User " + linuxUser);
		    }
		}
		if (!lUsers.isEmpty()) {
		    lLdapGroupMap.putIfAbsent(lGroup.getLdapGroupName(), lUsers);
		}
	    }
	    lMembers.addAll(lUsers);
	}
	LOG.info("All Users Id Count : " + lMembers.size() + " for the group");
	return lMembers;
    }

    private SortedSet<User> getAllLinuxUsers(List<LDAPGroup> pGroups) {
	SortedSet<User> lMembers = new TreeSet<>();
	for (LDAPGroup lGroup : pGroups) {
	    SortedSet<User> lUsers = lLdapGroupMap.get(lGroup.getLdapGroupName());
	    if (lUsers == null) {
		lUsers = new TreeSet<>();
		List<String> linuxUsers = getLinuxUsers(lGroup);
		LOG.info("User Id Count : " + linuxUsers.size() + " for the group " + lGroup.getGroup().name());
		for (String linuxUser : linuxUsers) {
		    User lUser = getUserDetails(linuxUser);
		    if (lUser.getDisplayName() != null && !lUser.getDisplayName().isEmpty()) {
			LOG.debug("Adding User " + linuxUser + " : " + lUser.getDisplayName() + " to Group : " + lGroup.getLdapGroupName());
			lUsers.add(lUser);
		    } else {
			LOG.error("No User details found for the User " + linuxUser);
		    }
		}
		if (!lUsers.isEmpty()) {
		    lLdapGroupMap.putIfAbsent(lGroup.getLdapGroupName(), lUsers);
		}
	    }
	    lMembers.addAll(lUsers);
	}
	LOG.info("All Users Id Count : " + lMembers.size() + " for the group");
	return lMembers;
    }

    public User getUserDetails(String pUserID) {
	if (lLdapUserMap.containsKey(pUserID)) {
	    User lUser = lLdapUserMap.get(pUserID);
	    User lReturn = new User();
	    try {
		BeanUtils.copyProperties(lReturn, lUser);
	    } catch (Exception ex) {
		LOG.warn("Error in Copying Properties");
	    }
	    LOG.debug("returning User details for the User " + pUserID + " : " + lReturn.getDisplayName());
	    return lReturn;
	} else {
	    User lUser = new User();
	    lUser.setId(pUserID);
	    User lReturn = new User();
	    try {
		if (getUserDetails(lUser)) {
		    BeanUtils.copyProperties(lReturn, lUser);
		    lLdapUserMap.put(pUserID, lUser);
		} else {
		    LOG.error("No User Detail found for the ID: " + pUserID);
		}
	    } catch (Exception ex) {
		LOG.warn("Error in Copying Properties");
	    }
	    LOG.debug("returning User details for the User " + pUserID + " : " + lReturn.getDisplayName());
	    return lReturn;
	}
    }

    public LoginErrorCode getLinuxGroupDetails(User pUser) {
	List<String> lMembers = new ArrayList<>();
	List[] lGroupList = ldapGroupConfig.getGroupsList();
	SortedSet<User> allAllowedUsers = getAllLinuxUsers(ldapGroupConfig.getServerAccessGroups());
	for (List<LDAPGroup> lGroups : lGroupList) {
	    lMembers.clear();
	    for (LDAPGroup lGroup : lGroups) {
		if (lGroup.getGroup().isAccessCheck() && !allAllowedUsers.contains(pUser)) {
		    LOG.debug(pUser.getDisplayName() + " doesn't have server access for the " + lGroup.getGroup().name());
		    continue;
		}
		lMembers.addAll(getLinuxUsers(lGroup));
	    }
	    if (lMembers.contains(pUser.getId())) {
		pUser.addRole(lGroups.get(0).getGroup().name());
	    }
	}

	if (pUser.getRole().isEmpty()) {
	    LOG.info("No Roles defined for the User " + pUser.getDisplayName());
	    return LoginErrorCode.USER_DISABLED;
	}

	pUser.setCurrentRole(pUser.getRole().iterator().next());
	pUser.setAllowDelegateMenu(Constants.UserGroup.getDelegatedUserList().contains(pUser.getCurrentRole()));
	return LoginErrorCode.SUCCESS;
    }

    /**
     * Private Methods
     */
    private Boolean getUserDetails(User pUser) {
	HashMap<String, String> lAttribute = getAttributes(ldapConfig.getUserClass(), ldapConfig.getUserParam(), pUser.getId(), ldapConfig.getUserSearchBase(), ldapConfig.getUserAttributes().toArray(new String[0]));
	if (lAttribute.isEmpty()) {
	    lAttribute = getAttributes(ldapConfig.getUserClass(), ldapConfig.getUserParam(), pUser.getId(), ldapConfig.getUserSearchBase(), ldapConfig.getUserAttributes().toArray(new String[0]));
	}
	if (lAttribute.isEmpty()) {
	    return false;
	}
	pUser.setDisplayName(lAttribute.get(ldapConfig.getUserAttributes().get(0)));
	pUser.setMailId(lAttribute.get(ldapConfig.getUserAttributes().get(1)));
	pUser.setDeltaEmployee(Boolean.valueOf(lAttribute.getOrDefault(ldapConfig.getUserAttributes().get(2), "false")));
	pUser.setHomeDirectory(lAttribute.get(ldapConfig.getUserAttributes().get(3)));
	LOG.debug("returning User details for the User " + pUser.getDisplayName());
	return true;
    }

    private LoginErrorCode validateLDAPLogin(User pUser, String pPassword) {
	boolean lReturn = false;
	try {
	    lReturn = createContext(pUser.getId(), pPassword);
	    if (!lReturn) {
		LOG.info("Wrong username or password");
		return LoginErrorCode.WRONG_USER_NAME_OR_PASSWORD;
	    }
	    if (!getUserDetails(pUser)) {
		LOG.info("User is not present inside the search base");
		return LoginErrorCode.USER_DISABLED;
	    }
	    return getLinuxGroupDetails(pUser);
	} catch (Exception ex) {
	    LOG.error("Error in finding Attributes", ex);
	    return LoginErrorCode.USER_DISABLED;
	}
    }

    public List<String> getLinuxUsers(LDAPGroup pGroup) {
	List<String> lMembers = new ArrayList<>();
	// LOG.info("Getting User List for the Group " + pGroup.getLdapGroupName());
	try {
	    HashMap<String, String> lAttribute = getAttributes(ldapConfig.getGroupClass(), pGroup.getLdapParam(), pGroup.getLdapGroupName(), pGroup.getLdapGroupBase(), new String[] { ldapConfig.getGroupAttribute() });
	    if (lAttribute.isEmpty()) {
		lAttribute = getAttributes(ldapConfig.getGroupClass(), pGroup.getLdapParam(), pGroup.getLdapGroupName(), pGroup.getLdapGroupBase(), new String[] { ldapConfig.getGroupAttribute() });
	    }
	    if (!lAttribute.isEmpty()) {
		String lMemberAttributes = lAttribute.get(ldapConfig.getGroupAttribute());
		if (lMemberAttributes != null && !lMemberAttributes.isEmpty()) {
		    lMembers = getClassNames(lMemberAttributes, ldapConfig.getUserParam());
		}
	    }
	} catch (InvalidNameException ex) {
	    LOG.error("Error in finding Attributes", ex);
	}
	// LOG.info("User List size " + lMembers.size());
	return lMembers;
    }

    public Boolean isAllowedOnMaintanence(User lUser) {
	SortedSet<User> allAllowedUsers = getAllLinuxUsers(ldapGroupConfig.getMaintenanceAccessGroups());
	return allAllowedUsers.contains(lUser);
    }

}

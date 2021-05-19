package com.tsi.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.EncryptUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Prabhu
 */
public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = -3032034971814265315L;
    private String id;
    private String password;
    private String displayName;
    private String mailId;
    private String homeDirectory;
    private boolean deltaEmployee;
    private Set<String> role = new LinkedHashSet<String>();
    private String currentRole;
    private String timeZone;
    private boolean delegated;
    private boolean allowDelegateMenu;
    private User currentDelegatedUser;
    private Map<String, User> delegations = new HashMap<String, User>();

    public User() {
	delegated = false;
	allowDelegateMenu = false;
    }

    public User(String id) {
	this.id = id;
	delegated = false;
	allowDelegateMenu = false;
    }

    public boolean isDeltaEmployee() {
	return deltaEmployee;
    }

    public void setDeltaEmployee(boolean deltaEmployee) {
	this.deltaEmployee = deltaEmployee;
    }

    public String getHomeDirectory() {
	return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
	this.homeDirectory = homeDirectory;
    }

    public boolean isAllowDelegateMenu() {
	return allowDelegateMenu;
    }

    public void setAllowDelegateMenu(boolean allowDelegateMenu) {
	this.allowDelegateMenu = allowDelegateMenu;
    }

    public User getCurrentDelegatedUser() {
	return currentDelegatedUser;
    }

    public void setCurrentDelegatedUser(User currentDelegatedUser) {
	this.currentDelegatedUser = currentDelegatedUser;
    }

    public boolean isDelegated() {
	return delegated;
    }

    public void setDelegated(boolean delegated) {
	this.delegated = delegated;
    }

    public Map<String, User> getDelegations() {
	return delegations;
    }

    public void setDelegations(Map<String, User> delegations) {
	this.delegations = delegations;
    }

    public void addDelegators(String userId, User pUser) {
	this.delegations.put(userId, pUser);
    }

    public String getId() {
	return id;
    }

    @JsonIgnore
    public User getCurrentOrDelagateUser() {
	if (currentDelegatedUser == null) {
	    return this;
	}
	return currentDelegatedUser;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getCurrentRole() {
	return currentRole;
    }

    public void setCurrentRole(String currentRole) {
	this.currentRole = currentRole;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getMailId() {
	return mailId;
    }

    public void setMailId(String mailId) {
	this.mailId = mailId;
    }

    public Set<String> getRole() {
	return role;
    }

    public void setRole(LinkedHashSet<String> role) {
	this.role = role;
    }

    public void addRole(String role) {
	this.role.add(role);
    }

    public String getTimeZone() {
	return timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof User) {
	    User lValue = (User) obj;
	    return lValue.getId().equals(id);
	} else if (obj instanceof String) {
	    return obj.equals(id);
	}
	return false;
    }

    @Override
    public int hashCode() {
	return this.id.hashCode();
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    @JsonIgnore
    public String getDecryptedPassword() {
	return EncryptUtil.decrypt(this.password, Constants.SECRET_HASH);
    }

    public void setEncryptedPassword(String pPassword) {
	this.password = EncryptUtil.encrypt(pPassword, Constants.SECRET_HASH);
    }

    @Override
    public int compareTo(User lValue) {
	return id.compareTo(lValue.getId());
    }

}

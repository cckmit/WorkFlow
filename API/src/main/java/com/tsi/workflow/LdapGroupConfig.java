/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author User
 */
public class LdapGroupConfig {

    private static final Logger LOG = Logger.getLogger(LdapGroupConfig.class.getName());

    private List<LDAPGroup> lDeveloperGroups = new ArrayList<>();
    private List<LDAPGroup> lLeadGroups = new ArrayList<>();
    private List<LDAPGroup> lReviewerGroups = new ArrayList<>();
    private List<LDAPGroup> lDevManagerGroups = new ArrayList<>();
    private List<LDAPGroup> lLoadsControlGroups = new ArrayList<>();
    private List<LDAPGroup> lQAGroups = new ArrayList<>();
    private List<LDAPGroup> lQADeployedGroups = new ArrayList<>();
    private List<LDAPGroup> lDLCoreChangeTeamGroups = new ArrayList<>();
    private List<LDAPGroup> lSystemSupportGroups = new ArrayList<>();
    private List<LDAPGroup> lServiceDeskGroups = new ArrayList<>();
    private List<LDAPGroup> lToolAdminGroups = new ArrayList<>();
    private List<LDAPGroup> lServerAccessGroups = new ArrayList<>();
    private List<LDAPGroup> lMaintenanceAccessGroups = new ArrayList<>();

    @Value("${ldap.group.role.developer}")
    private String lDeveloperGroupName;
    @Value("${ldap.group.role.lead}")
    private String lLeadGroupName;
    @Value("${ldap.group.role.reviewer}")
    private String lReviewerGroupName;
    @Value("${ldap.group.role.devmanager}")
    private String lDevManagerGroupName;
    @Value("${ldap.group.role.loadscontrol}")
    private String lLoadsControlGroupName;
    @Value("${ldap.group.role.qa}")
    private String lQAGroupName;
    @Value("${ldap.group.role.qadeployed.lead}")
    private String lQADeployedLeadGroupName;
    @Value("${ldap.group.role.systemsupport}")
    private String lSystemSupportGroupName;
    @Value("${ldap.group.role.servicedesk}")
    private String lServiceDeskGroupName;
    @Value("${ldap.group.role.tooladmin}")
    private String lToolAdminGroupName;
    @Value("${ldap.group.role.zall}")
    private String lAllGroupName;
    @Value("${ldap.group.role.servergroup}")
    private String lServerAccessGroupName;
    @Value("${ldap.group.role.rfc.change.management}")
    private String lDLCoreChangeTeam;
    @Value("${wf.allowAllRoles}")
    private String isAllowAllUsers;
    private Map<String, List<LDAPGroup>> ldapRolesMap;
    private Map<String, List<LDAPGroup>> ldapCNMap;

    public LdapGroupConfig() {
	ldapRolesMap = new HashMap<>();
	ldapCNMap = new HashMap<>();
    }

    public void loadLdapConfig() throws WorkflowException {
	try {
	    LOG.info("Loading User Groups as per Ldap roles");
	    lDeveloperGroups.clear();
	    lLeadGroups.clear();
	    lReviewerGroups.clear();
	    lDevManagerGroups.clear();
	    lLoadsControlGroups.clear();
	    lQAGroups.clear();
	    lQADeployedGroups.clear();
	    lDLCoreChangeTeamGroups.clear();
	    lSystemSupportGroups.clear();
	    lServiceDeskGroups.clear();
	    lToolAdminGroups.clear();
	    lServerAccessGroups.clear();
	    lMaintenanceAccessGroups.clear();
	    populateGroup(lDeveloperGroups, lDeveloperGroupName, Constants.UserGroup.Developer);
	    populateGroup(lLeadGroups, lLeadGroupName, Constants.UserGroup.Lead);
	    populateGroup(lReviewerGroups, lReviewerGroupName, Constants.UserGroup.Reviewer);
	    populateGroup(lDevManagerGroups, lDevManagerGroupName, Constants.UserGroup.DevManager);
	    populateGroup(lLoadsControlGroups, lLoadsControlGroupName, Constants.UserGroup.LoadsControl);
	    populateGroup(lQAGroups, lQAGroupName, Constants.UserGroup.QA);
	    populateGroup(lQADeployedGroups, lQADeployedLeadGroupName, Constants.UserGroup.QADeployLead);
	    populateGroup(lDLCoreChangeTeamGroups, lDLCoreChangeTeam, Constants.UserGroup.DLCoreChangeTeam);
	    populateGroup(lSystemSupportGroups, lSystemSupportGroupName, Constants.UserGroup.SystemSupport);
	    populateGroup(lServiceDeskGroups, lServiceDeskGroupName, Constants.UserGroup.TechnicalServiceDesk);
	    populateGroup(lToolAdminGroups, lToolAdminGroupName, Constants.UserGroup.ToolAdmin);
	    populateGroup(lServerAccessGroups, lServerAccessGroupName, Constants.UserGroup.ServerAccess);
	    populateGroup(lMaintenanceAccessGroups, lAllGroupName, Constants.UserGroup.Maintenance);
	    if (isAllowAllUsers.equals("true") && !lAllGroupName.isEmpty()) {
		LOG.info("Adding DEVOPS Users to all roles");
		populateGroup(lDeveloperGroups, lAllGroupName, Constants.UserGroup.Developer);
		populateGroup(lLeadGroups, lAllGroupName, Constants.UserGroup.Lead);
		populateGroup(lReviewerGroups, lAllGroupName, Constants.UserGroup.Reviewer);
		populateGroup(lDevManagerGroups, lAllGroupName, Constants.UserGroup.DevManager);
		populateGroup(lLoadsControlGroups, lAllGroupName, Constants.UserGroup.LoadsControl);
		populateGroup(lQAGroups, lAllGroupName, Constants.UserGroup.QA);
		populateGroup(lQADeployedGroups, lAllGroupName, Constants.UserGroup.QADeployLead);
		populateGroup(lDLCoreChangeTeamGroups, lAllGroupName, Constants.UserGroup.DLCoreChangeTeam);
		populateGroup(lSystemSupportGroups, lAllGroupName, Constants.UserGroup.SystemSupport);
		populateGroup(lServiceDeskGroups, lAllGroupName, Constants.UserGroup.TechnicalServiceDesk);
		populateGroup(lToolAdminGroups, lAllGroupName, Constants.UserGroup.ToolAdmin);
	    }
	} catch (InvalidNameException e) {
	    throw new WorkflowException("Error in Ldap Config role names", e);
	}
    }

    public Map<String, List<LDAPGroup>> getLdapRolesMap() {
	return ldapRolesMap;
    }

    public void setLdapRolesMap(Map<String, List<LDAPGroup>> ldapRolesMap) {
	this.ldapRolesMap = ldapRolesMap;
    }

    public Map<String, List<LDAPGroup>> getLdapCNMap() {
	return ldapCNMap;
    }

    public void setLdapCNMap(Map<String, List<LDAPGroup>> ldapCNMap) {
	this.ldapCNMap = ldapCNMap;
    }

    public List<LDAPGroup> getDeveloperGroups() {
	return lDeveloperGroups;
    }

    public List<LDAPGroup> getLeadGroups() {
	return lLeadGroups;
    }

    public List<LDAPGroup> getReviewerGroups() {
	return lReviewerGroups;
    }

    public List<LDAPGroup> getDevManagerGroups() {
	return lDevManagerGroups;
    }

    public List<LDAPGroup> getLoadsControlGroups() {
	return lLoadsControlGroups;
    }

    public List<LDAPGroup> getQAGroups() {
	return lQAGroups;
    }

    public List<LDAPGroup> getSystemSupportGroups() {
	return lSystemSupportGroups;
    }

    public List<LDAPGroup> getServiceDeskGroups() {
	return lServiceDeskGroups;
    }

    public List<LDAPGroup> getServerAccessGroups() {
	return lServerAccessGroups;
    }

    public List<LDAPGroup> getMaintenanceAccessGroups() {
	return lMaintenanceAccessGroups;
    }

    public String getlQADeployedLeadGroupName() {
	return lQADeployedLeadGroupName;
    }

    public List<LDAPGroup> getlDLCoreChangeTeamGroups() {
	return lDLCoreChangeTeamGroups;
    }

    private void populateGroup(List<LDAPGroup> lUserGroups, String pGroupNames, Constants.UserGroup pGroup) throws InvalidNameException {
	String[] split = pGroupNames.split("\\|");
	for (String ldapGroupName : split) {
	    LdapName ln = new LdapName(ldapGroupName);
	    for (Rdn rdn : ln.getRdns()) {
		if ("CN".equalsIgnoreCase(rdn.getType())) {
		    lUserGroups.add(new LDAPGroup("CN", rdn.getValue().toString(), ln.getPrefix(ln.size() - 1).toString(), pGroup));
		}
	    }
	    ldapCNMap.put(ldapGroupName, lUserGroups);
	}
	ldapRolesMap.put(pGroup.name(), lUserGroups);
    }

    public List<LDAPGroup>[] getGroupsList() {
	if (lDevManagerGroups.isEmpty()) {
	    loadLdapConfig();
	}
	// Order Maintained Here
	return new List[] { lDevManagerGroups, lServiceDeskGroups, lLoadsControlGroups, lReviewerGroups, lLeadGroups, lDeveloperGroups, lQAGroups, lQADeployedGroups, lDLCoreChangeTeamGroups, lSystemSupportGroups, lToolAdminGroups };
    }

    public List<LDAPGroup>[] getDelegateGroupsUsersList() {
	if (lDevManagerGroups.isEmpty()) {
	    loadLdapConfig();
	}
	// Order Maintained Here
	return new List[] { lDevManagerGroups, lServiceDeskGroups, lLoadsControlGroups, lReviewerGroups, lLeadGroups };
    }

    public List<LDAPGroup>[] getDelegateGroupsRoleList() {
	if (lDevManagerGroups.isEmpty()) {
	    loadLdapConfig();
	}
	// Order Maintained Here - need to be in sync with Constants.UserGroup
	return new List[] { lDevManagerGroups };
    }

    public List[] getSuperUserGroupsUsersList() {
	if (lDevManagerGroups.isEmpty()) {
	    loadLdapConfig();
	}
	// Order Maintained Here - need to be in sync with Constants.UserGroup
	return new List[] { lToolAdminGroups };
    }

    public List[] getSuperUserGroupsRoleList() {
	if (lDevManagerGroups.isEmpty()) {
	    loadLdapConfig();
	}
	// Order Maintained Here - need to be in sync with Constants.UserGroup
	return new List[] { lDevManagerGroups, lServiceDeskGroups, lLoadsControlGroups, lReviewerGroups, lLeadGroups, lQAGroups, lQADeployedGroups, lDLCoreChangeTeamGroups, lSystemSupportGroups };
    }

}

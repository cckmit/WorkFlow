package com.tsi.workflow.authenticator;

import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.utils.Constants.LoginErrorCode;
import java.util.HashMap;
import java.util.List;
import javax.naming.InvalidNameException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public final class ADSAuthenticatorImpl extends ADSAuthenticator {// implements Condition {

    private static final Logger LOG = Logger.getLogger(ADSAuthenticatorImpl.class.getName());
    @Autowired
    LdapGroupConfig ldapGroupConfig;

    // @Override
    // public boolean matches(ConditionContext context, AnnotatedTypeMetadata
    // metadata) {
    //// return ldapConfig.getLdapType().equalsIgnoreCase("WINDOWS");
    // return false;
    // }
    public ADSAuthenticatorImpl() {
	super();
    }

    @Override
    public LoginErrorCode validate(Object pUserName, String pPassword) {
	LOG.info("Authenticating Using ADS Login Type");
	return validateADSLogin((User) pUserName, pPassword);
    }

    private LoginErrorCode validateADSLogin(User pUser, String pPassword) {
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
	    return getGroupDetails(pUser);
	} catch (Exception ex) {
	    LOG.error("Error in finding Attributes", ex);
	    return LoginErrorCode.USER_DISABLED;
	}
    }

    public boolean getUserDetails(User pUser) {
	HashMap<String, String> lAttribute = getAttributes(ldapConfig.getUserClass(), ldapConfig.getUserParam(), pUser.getId(), ldapConfig.getUserSearchBase(), ldapConfig.getUserAttributes().toArray(new String[0]));
	if (lAttribute.isEmpty()) {
	    return false;
	}
	pUser.setMailId(lAttribute.get(ldapConfig.getUserAttributes().get(1)));
	pUser.setDisplayName(lAttribute.get(ldapConfig.getUserAttributes().get(0)));
	return true;
    }

    public LoginErrorCode getGroupDetails(User pUser) throws InvalidNameException {
	HashMap<String, String> lAttribute = getAttributes(ldapConfig.getGroupClass(), ldapConfig.getGroupParam(), pUser.getId(), ldapConfig.getGroupSearchBase(), new String[] { ldapConfig.getGroupAttribute() });
	String lMemberAttributes = lAttribute.get(ldapConfig.getGroupAttribute());
	List<String> lMemberGroups = getClassNames(lMemberAttributes, "CN");
	List<LDAPGroup>[] lGroupList = ldapGroupConfig.getGroupsList();
	for (List<LDAPGroup> lGroups : lGroupList) {
	    boolean lReturn = false;
	    for (LDAPGroup lGroup : lGroups) {
		lReturn = lMemberGroups.contains(lGroup.getLdapGroupName());
		if (lReturn) {
		    break;
		}
	    }
	    if (!lReturn) {
		pUser.addRole(lGroups.get(0).getGroup().name());
	    }
	}
	if (pUser.getRole().isEmpty()) {
	    LOG.error("No Roles defined for the User " + pUser.getDisplayName());
	    return LoginErrorCode.USER_DISABLED;
	}
	LOG.info(StringUtils.repeat("*", 30));
	LOG.info("User " + pUser.getDisplayName() + " logged in, Roles : " + pUser.getRole());
	LOG.info(StringUtils.repeat("*", 30));
	return LoginErrorCode.SUCCESS;
    }

}

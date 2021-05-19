package com.tsi.workflow.authenticator;

import com.tsi.workflow.LdapConfig;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants.LoginErrorCode;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ADSAuthenticator {

    private static final Logger LOG = Logger.getLogger(ADSAuthenticator.class.getName());

    @Autowired
    LdapConfig ldapConfig;

    LDAPConnection lAnonymousConnection;

    URL lUrl;

    protected ADSAuthenticator() {

    }

    protected String getHost() {
	return lUrl.getHost();
    }

    protected int getPort() {
	return lUrl.getPort();
    }

    public abstract LoginErrorCode validate(Object pUserName, String pPassword);

    public void createAnonymsContext() {
	try {
	    lUrl = new URL(ldapConfig.getUrl().replace("ldaps:", "https:").replace("ssh:", "https:"));
	    SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
	    lAnonymousConnection = new LDAPConnection(sslUtil.createSSLSocketFactory());
	    lAnonymousConnection.connect(getHost(), getPort());
	    BindResult Anonymousbind = lAnonymousConnection.bind(new SimpleBindRequest());
	    LOG.info(Anonymousbind.getResultString());
	} catch (Exception ex) {
	    LOG.error("Error in Creating Anonymous Connection");
	}
    }

    public boolean createContext(String username, String password) {
	try {
	    lUrl = new URL(ldapConfig.getUrl().replace("ldaps:", "https:").replace("ssh:", "https:"));
	    if ("".equals(username) || "".equals(password)) {
		LOG.error("Error: Username or Password Empty");
		return false;
	    }
	    SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
	    LDAPConnection lAuthConnection = new LDAPConnection(sslUtil.createSSLSocketFactory());
	    try {
		lAuthConnection.connect(getHost(), getPort());
		SimpleBindRequest adminBindRequest = new SimpleBindRequest(ldapConfig.getUserParam() + "=" + username + "," + ldapConfig.getUserSearchBase(), password);
		BindResult bind = lAuthConnection.bind(adminBindRequest);
		if (bind.getResultCode().intValue() == 49) {
		    LOG.error("Invalid Credentials Entered!");
		}
		return bind.getResultCode().intValue() == 0;
	    } finally {
		lAuthConnection.close();
	    }
	} catch (LDAPException ex) {
	    LOG.error("Error in LDAP communication " + ex.getMessage());
	} catch (Exception ex) {
	    LOG.error("Error in LDAP communication ", ex);
	}
	return false;
    }

    public HashMap<String, String> getAttributes(String pClassType, String pSearchKey, String pSearchValue, String pSearchBase, String[] pAttributes) {
	HashMap<String, String> mBuffer = new HashMap();
	try {
	    String userSearchFilter = "(&(objectClass=" + pClassType + ")(" + pSearchKey + "=" + pSearchValue + "))";
	    if (lAnonymousConnection == null || !lAnonymousConnection.isConnected()) {
		createAnonymsContext();
	    }

	    if (lAnonymousConnection != null) {
		SearchResult search = lAnonymousConnection.search(pSearchBase, SearchScope.SUB, userSearchFilter, pAttributes);
		List<SearchResultEntry> searchEntries = search.getSearchEntries();
		for (SearchResultEntry searchEntry : searchEntries) {
		    Collection<Attribute> attributes = searchEntry.getAttributes();
		    for (Attribute attribute : attributes) {
			if (attribute.size() > 1) {
			    mBuffer.put(attribute.getName(), StringUtils.join(attribute.getValues(), ","));
			} else {
			    mBuffer.put(attribute.getName(), attribute.getValue());
			}
		    }
		}
	    } else {
		throw new WorkflowException("No LDAP Connection found");
	    }
	} catch (LDAPSearchException ex) {
	    LOG.error("Error in gettting LDAP attibutes", ex);
	}
	return mBuffer;
    }

    public List<String> getClassNames(String pLdapString, String pFilter) throws InvalidNameException {
	List<String> lClassNames = new ArrayList<>();
	LdapName ln = new LdapName(pLdapString);
	for (Rdn rdn : ln.getRdns()) {
	    if (rdn.getType().equalsIgnoreCase(pFilter)) {
		lClassNames.add(rdn.getValue().toString());
	    }
	}
	return lClassNames;
    }

    public void closeAnonymousConnection() {
	if (lAnonymousConnection != null) {
	    lAnonymousConnection.close();
	}
    }
}

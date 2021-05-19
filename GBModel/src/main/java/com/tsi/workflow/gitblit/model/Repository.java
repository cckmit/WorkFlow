/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author User
 */
public class Repository implements Serializable {

    private static final long serialVersionUID = 3117536553664626014L;

    private String name;
    private String description;
    private SortedSet<String> owners = new TreeSet<>();
    public AccessRestrictionType accessRestriction;
    public AuthorizationControl authorizationControl;
    private boolean allowAuthenticated;
    private boolean useIncrementalPushTags;
    public boolean isFrozen;
    private String platform;
    private List<String> availableRefs = new ArrayList<>();

    public Repository() {
    }

    public String getName() {
	return name;
    }

    public String getTrimmedName() {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(this.name)).toUpperCase();
	String lReturn1 = FilenameUtils.removeExtension(this.name).toUpperCase().replaceAll("\\d+$", "");
	String lReturn2 = FilenameUtils.removeExtension(this.name).toUpperCase();
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

    public String getFuncArea() {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(this.name)).toUpperCase();
	String lReturn = FilenameUtils.getName(this.name).toLowerCase().replace("nonibm_", "").replace("ibm_", "").replace(".git", "").toUpperCase();

	if (lRepoName.startsWith("NONIBM_") && lRepoName.length() > 10) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
	    return lReturn;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
	    return lReturn;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
	    return lReturn;
	} else {
	    return lReturn.replaceAll("\\d+$", "");
	}
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public List<String> getAvailableRefs() {
	return availableRefs;
    }

    public void setAvailableRefs(List<String> availableRefs) {
	this.availableRefs = availableRefs;
    }

    public SortedSet<String> getOwners() {
	return owners;
    }

    public void setOwners(SortedSet<String> owners) {
	this.owners = owners;
    }

    public AccessRestrictionType getAccessRestriction() {
	return accessRestriction;
    }

    public void setAccessRestriction(AccessRestrictionType accessRestriction) {
	this.accessRestriction = accessRestriction;
    }

    public AuthorizationControl getAuthorizationControl() {
	return authorizationControl;
    }

    public void setAuthorizationControl(AuthorizationControl authorizationControl) {
	this.authorizationControl = authorizationControl;
    }

    public boolean isAllowAuthenticated() {
	return allowAuthenticated;
    }

    public void setAllowAuthenticated(boolean allowAuthenticated) {
	this.allowAuthenticated = allowAuthenticated;
    }

    public boolean isUseIncrementalPushTags() {
	return useIncrementalPushTags;
    }

    public void setUseIncrementalPushTags(boolean useIncrementalPushTags) {
	this.useIncrementalPushTags = useIncrementalPushTags;
    }

    public boolean isIsFrozen() {
	return isFrozen;
    }

    public void setIsFrozen(boolean isFrozen) {
	this.isFrozen = isFrozen;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 17 * hash + Objects.hashCode(this.name);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Repository other = (Repository) obj;
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	return true;
    }

    public String getPlatform() {
	return platform;
    }

    public void setPlatform(String platform) {
	this.platform = platform;
    }

    public String getCurrentAccess() {
	String defaultAccess = RepoPermission.RESTRICTED.name();
	if (getAuthorizationControl() == AuthorizationControl.AUTHENTICATED) {
	    defaultAccess = RepoPermission.READ_WRITE.name();
	} else if (getAccessRestriction() == AccessRestrictionType.PUSH && getAuthorizationControl() == AuthorizationControl.NAMED) {
	    defaultAccess = RepoPermission.READ.name();
	}
	return defaultAccess;
    }

    public void setCurrentAccess(String defaultAccess) {
	if (defaultAccess != null) {
	    if (defaultAccess.equalsIgnoreCase(RepoPermission.RESTRICTED.name())) {
		setAccessRestriction(AccessRestrictionType.VIEW);
		setAuthorizationControl(AuthorizationControl.NAMED);
	    } else if (defaultAccess.equalsIgnoreCase(RepoPermission.READ.name())) {
		setAccessRestriction(AccessRestrictionType.PUSH);
		setAuthorizationControl(AuthorizationControl.NAMED);
	    } else if (defaultAccess.equalsIgnoreCase(RepoPermission.READ_WRITE.name())) {
		setAccessRestriction(AccessRestrictionType.PUSH);
		setAuthorizationControl(AuthorizationControl.AUTHENTICATED);
	    }
	}
    }

    public enum RepoPermission {

	RESTRICTED("V", true),
	READ("R", true),
	READ_WRITE("RWC", true),
	OWNER("RW+", false);

	private final String repoPermission;
	private final Boolean toShow;

	private RepoPermission(String repoPermission, Boolean toShow) {
	    this.repoPermission = repoPermission;
	    this.toShow = toShow;
	}

	public String getPermission() {
	    return repoPermission;
	}

	public Boolean getToShow() {
	    return toShow;
	}
    }
}

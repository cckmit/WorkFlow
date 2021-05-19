package com.tsi.workflow.beans.ui;

public class DevlBuildForm {

    boolean allowRebuild;
    boolean allowDevlBuild;

    public DevlBuildForm() {
	allowDevlBuild = false;
	allowRebuild = false;
    }

    public boolean isAllowRebuild() {
	return allowRebuild;
    }

    public void setAllowRebuild(boolean allowRebuild) {
	this.allowRebuild = allowRebuild;
    }

    public boolean isAllowDevlBuild() {
	return allowDevlBuild;
    }

    public void setAllowDevlBuild(boolean allowDevlBuild) {
	this.allowDevlBuild = allowDevlBuild;
    }
}

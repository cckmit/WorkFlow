package com.tsi.workflow.beans.dto;

import com.tsi.workflow.beans.dao.SystemLoad;

public class SystemLoadDTO {

    private SystemLoad systemLoad;
    private Boolean allowPutLevelChange = true;

    public SystemLoad getSystemLoad() {
	return systemLoad;
    }

    public void setSystemLoad(SystemLoad systemLoad) {
	this.systemLoad = systemLoad;
    }

    public Boolean getAllowPutLevelChange() {
	return allowPutLevelChange;
    }

    public void setAllowPutLevelChange(Boolean allowPutLevelChange) {
	this.allowPutLevelChange = allowPutLevelChange;
    }

}

package com.tsi.workflow.beans.ui;

import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;

public class ImpPlanAndUserDetail {

    private ImpPlan impPlan;
    private User user;
    private SystemLoad systemLoad;

    public ImpPlan getImpPlan() {
	return impPlan;
    }

    public void setImpPlan(ImpPlan impPlan) {
	this.impPlan = impPlan;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public SystemLoad getSystemLoad() {
	return systemLoad;
    }

    public void setSystemLoad(SystemLoad systemLoad) {
	this.systemLoad = systemLoad;
    }
}

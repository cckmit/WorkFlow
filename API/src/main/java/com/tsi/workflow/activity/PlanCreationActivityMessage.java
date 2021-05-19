/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.SystemLoad;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class PlanCreationActivityMessage extends ActivityLogMessage {

    List<SystemLoad> systemLoads;
    boolean create = false;
    boolean update = false;

    public void setSystemLoads(List<SystemLoad> systemLoads) {
	this.systemLoads = systemLoads;
    }

    public void setCreate(boolean create) {
	this.create = create;
    }

    public void setUpdate(boolean update) {
	this.update = update;
    }

    public PlanCreationActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    @Override
    public String processMessage() {
	List<String> systems = new ArrayList<>();
	for (SystemLoad systemLoad : systemLoads) {
	    systems.add(systemLoad.getSystemId().getName());
	}
	if (create) {
	    if (user.getCurrentDelegatedUser() == null) {
		return MessageFormat.format("{1} has created the implementation plan {0} for {2}", impPlan.getId(), user.getDisplayName(), String.join(",", systems));
	    } else {
		return MessageFormat.format("{1} has created the implementation plan {0} on behalf of {2} for {3}", impPlan.getId(), user.getDisplayName(), user.getCurrentDelegatedUser().getDisplayName(), String.join(",", systems));
	    }
	} else if (update) {
	    if (user.getCurrentDelegatedUser() == null) {
		return MessageFormat.format("{1} has deleted the target systems {2} from implementation plan {0} ", impPlan.getId(), user.getDisplayName(), String.join(",", systems));
	    } else {
		return MessageFormat.format("{1} has deleted the target systems {3} from implementation plan{0} on behalf of {2} ", impPlan.getId(), user.getDisplayName(), user.getCurrentDelegatedUser().getDisplayName(), String.join(",", systems));
	    }
	} else {
	    if (user.getCurrentDelegatedUser() == null) {
		return MessageFormat.format("{1} has newly added the target systems {2} for implementation plan {0}", impPlan.getId(), user.getDisplayName(), String.join(",", systems));
	    } else {
		return MessageFormat.format("{1} has newly added the target systems {3} for implementation plan{0} on behalf of {2} ", impPlan.getId(), user.getDisplayName(), user.getCurrentDelegatedUser().getDisplayName(), String.join(",", systems));
	    }
	}
    }

}

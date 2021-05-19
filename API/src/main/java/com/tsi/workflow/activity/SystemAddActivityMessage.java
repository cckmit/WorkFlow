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
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class SystemAddActivityMessage extends ActivityLogMessage {

    SystemLoad systemLoad;

    public SystemAddActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    @Override
    public String processMessage() {
	return MessageFormat.format("{3} {1} has added new System {0} while checking out for the implementation {2}", systemLoad.getSystemId().getName(), user.getDisplayName(), implementation.getId(), user.getCurrentRole());
    }

    @Override
    public Priority getLogLevel() {
	return Priority.WARN;
    }

    @Override
    public void setArguments(IBeans... beans) {
	systemLoad = (SystemLoad) beans[0];
    }

}

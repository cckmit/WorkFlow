package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class MacroHeaderPlanFallBackActivity extends ActivityLogMessage {

    public static final Logger LOG = Logger.getLogger(MacroHeaderPlanFallBackActivity.class.getName());

    SortedSet<String> lSet;
    String message;

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public SortedSet<String> getlSet() {
	return lSet;
    }

    public void setlSet(SortedSet<String> lSet) {
	this.lSet = lSet;
    }

    public MacroHeaderPlanFallBackActivity(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	return message;
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {

    }

}

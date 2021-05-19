/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import java.text.MessageFormat;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class CheckoutActivityMessage extends ActivityLogMessage {

    CheckoutSegments segments;
    boolean populate = false;
    boolean commit = false;
    boolean newFile = false;
    boolean deleteFile = false;

    public CheckoutActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    @Override
    public void setArguments(IBeans... beans) {
	segments = (CheckoutSegments) beans[0];
    }

    @Override
    public String processMessage() {
	if (populate) {
	    return MessageFormat.format("{4} {3} has populated and checkedout the source artifact {0} from {1} system into implementation {2}", segments.getFileName(), segments.getTargetSystem(), segments.getImpId().getId(), user.getDisplayName(), user.getCurrentRole());
	} else if (newFile) {
	    return MessageFormat.format("{4} {3} has created and checkedout the source artifact {0} from {1} system into implementation {2}", segments.getFileName(), segments.getTargetSystem(), segments.getImpId().getId(), user.getDisplayName(), user.getCurrentRole());
	} else if (deleteFile) {
	    return MessageFormat.format("{4} {3} has deleted the source artifact {0} from {1} system in implementation {2}", segments.getFileName(), segments.getTargetSystem(), segments.getImpId().getId(), user.getDisplayName(), user.getCurrentRole());
	} else if (commit) {
	    return MessageFormat.format("{4} {3} has done local commit for the source artifact {0} from {1} system in implementation {2}", segments.getFileName(), segments.getTargetSystem(), implementation.getId(), user.getDisplayName(), user.getCurrentRole());
	} else {
	    return MessageFormat.format("{4} {3} has checkedout the source artifact {0} from {1} system into implementation {2}", segments.getFileName(), segments.getTargetSystem(), segments.getImpId().getId(), user.getDisplayName(), user.getCurrentRole());
	}
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    public void setPopulate(boolean populate) {
	this.populate = populate;
    }

    public void setNewFile(boolean newFile) {
	this.newFile = newFile;
    }

    public void setDeleteFile(boolean deleteFile) {
	this.deleteFile = deleteFile;
    }

    public void setCommit(boolean commit) {
	this.commit = commit;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.ui.TestSystemMaintenanceMailForm;
import com.tsi.workflow.mail.MNFExecutionMail;
import com.tsi.workflow.mail.TesttSystemMaintFailMail;
import com.workflow.ssh.SSHClientUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class MaintenanceHelper {

    private static final Logger LOG = Logger.getLogger(MaintenanceHelper.class.getName());

    @Autowired
    GITConfig gITConfig;
    @Autowired
    MailMessageFactory mailMessageFactory;

    @Autowired
    SSHClientUtils sSHClientUtils;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public void notifyToolAdminOnProcessFails(String systemName) {
	TesttSystemMaintFailMail processFailMail = (TesttSystemMaintFailMail) getMailMessageFactory().getTemplate(TesttSystemMaintFailMail.class);
	processFailMail.setSystemName(systemName);
	processFailMail.addToDEVCentre();
	getMailMessageFactory().push(processFailMail);
    }

    public void mnfExecutionMail(TestSystemMaintenanceMailForm testSystemMaintenanceMailForm) {
	MNFExecutionMail processMail = (MNFExecutionMail) getMailMessageFactory().getTemplate(MNFExecutionMail.class);
	processMail.setSystemName(testSystemMaintenanceMailForm.getSystemName());
	processMail.setInfo(testSystemMaintenanceMailForm.getInfo());
	processMail.setKey(testSystemMaintenanceMailForm.getKey());
	processMail.addToDEVCentre();
	getMailMessageFactory().push(processMail);
    }

}

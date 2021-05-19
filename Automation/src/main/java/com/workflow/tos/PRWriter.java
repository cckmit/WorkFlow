/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.tos;

import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.interfaces.IPRConfig;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class PRWriter {

    IPRConfig prConfig;
    private static final Logger LOG = Logger.getLogger(PRWriter.class.getName());

    public PRWriter(IPRConfig prConfig) {
	this.prConfig = prConfig;
    }

    public IPRConfig getPrConfig() {
	return prConfig;
    }

    public void writeData(String pFileName, String pContent) {
	NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(getPrConfig().getPRUserDomain(), getPrConfig().getPRUser(), getPrConfig().getPRPassword());
	try (SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(new SmbFile(getPrConfig().getPrFilePath() + "/" + pFileName, auth))) {
	    smbFileOutputStream.write(pContent.getBytes());
	} catch (Exception e) {
	    LOG.error("Error occured in generating PR Status implementation file", e);
	    throw new WorkflowException("Error occured in generating PR Status implementation file", e);
	}
    }
}

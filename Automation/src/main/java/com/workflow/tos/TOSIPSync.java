/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.tos;

import com.tsi.workflow.interfaces.ITOSConfig;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class TOSIPSync {

    ITOSConfig tOSConfig;
    private static final Logger LOG = Logger.getLogger(TOSIPSync.class.getName());

    public TOSIPSync(ITOSConfig tOSConfig) {
	this.tOSConfig = tOSConfig;
    }

    public List<String> getData() {
	List<String> readLines = new ArrayList<>();
	SmbFileInputStream smbFileInputStream = null;
	try {
	    LOG.info("Reading from path " + tOSConfig.getTosFilePath());
	    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(tOSConfig.getTosFileDomain(), tOSConfig.getTosFileUser(), tOSConfig.getTosFilePassword());
	    smbFileInputStream = new SmbFileInputStream(new SmbFile(tOSConfig.getTosFilePath(), auth));
	    readLines = IOUtils.readLines(smbFileInputStream, StandardCharsets.UTF_8.name());
	} catch (Exception ex) {
	    LOG.error("Error in Reading TOS System IP", ex);
	} finally {
	    if (smbFileInputStream != null) {
		try {
		    smbFileInputStream.close();
		} catch (IOException ex) {
		    LOG.error("Error in Closing TOS File", ex);
		}
	    }
	}
	return readLines;
    }
}

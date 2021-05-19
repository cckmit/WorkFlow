/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.recon.git;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author USER
 */
public class GitBaseMetaResult {

    private String fileHashCode;
    private String fileName;
    private String programName;
    private String funcArea;

    public GitBaseMetaResult(String fileHashCode, String fileName) {
	this.fileHashCode = fileHashCode;
	this.fileName = fileName;
	this.programName = FilenameUtils.getName(fileName);
    }

    public void setFuncArea(String funcArea) {
	this.funcArea = funcArea;
    }

    public String getFuncArea() {
	return funcArea;
    }

    public String getFileHashCode() {
	return fileHashCode;
    }

    public String getFileName() {
	return fileName;
    }

    public String getProgramName() {
	return programName;
    }

    public String getFileNameWithHash() {
	return DigestUtils.md5Hex(fileName + "|" + funcArea + "|" + fileHashCode);
    }

    public String getFileNametoGroupSort() {
	return fileName + "|" + fileHashCode + "|" + funcArea;
    }

}

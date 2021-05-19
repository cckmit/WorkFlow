package com.tsi.workflow.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class FileIOUtils {

    public static byte[] getFileContent(File pFileName) throws Exception {
	if (pFileName.exists()) {
	    return FileUtils.readFileToByteArray(pFileName);
	}
	return new byte[0];
    }

    public static String getFileContentType(File pFileName) throws Exception {
	if (pFileName.exists()) {
	    return Files.probeContentType(Paths.get(pFileName.toURI()));
	}
	return "";
    }

    public static String getCurrentProcessID() {
	String jvmName = ManagementFactory.getRuntimeMXBean().getName();
	return jvmName.split("@")[0];
    }

}

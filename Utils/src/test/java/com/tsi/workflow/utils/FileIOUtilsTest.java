/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class FileIOUtilsTest {

    public FileIOUtilsTest() {
    }

    @Test
    public void testGetFileContent() throws Exception {
	File pFileName = new File(FileIOUtilsTest.class.getResource("/test.txt").toURI());
	byte[] result = FileIOUtils.getFileContent(pFileName);
	assertNotNull(result);
	pFileName = new File("/test1.txt");
	result = FileIOUtils.getFileContent(pFileName);
	assertNotNull(result);
    }

    @Test
    public void testGetFileContentType() throws Exception {
	FileIOUtils test = new FileIOUtils();
	File pFileName = new File(FileIOUtilsTest.class.getResource("/test.txt").toURI());
	String result = FileIOUtils.getFileContentType(pFileName);
	assertNotNull(result);
	pFileName = new File("/test1.txt");
	result = FileIOUtils.getFileContentType(pFileName);
	assertNotNull(result);
    }

}

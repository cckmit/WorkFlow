package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class FilesOpenFileTest {

    @Test
    public void test() {
	FilesOpenFile filesOpenFile = new FilesOpenFile();
	filesOpenFile.setFile(new File());
	assertNotNull(filesOpenFile.getFile());
    }

}

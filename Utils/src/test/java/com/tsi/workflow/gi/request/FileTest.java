package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileTest {

    @Test
    public void test() {
	File file = new File();
	file.setFileName("bam.asm");
	assertEquals("bam.asm", file.getFileName());
	file.setHostOrIP("IP");
	assertEquals("IP", file.getHostOrIP());
	file.setPath("path");
	assertEquals("path", file.getPath());
	file.setUserID("e738090");
	assertEquals("e738090", file.getUserID());
    }

}

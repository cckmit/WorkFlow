package com.tsi.workflow.gi.response;

import static org.junit.Assert.*;

import org.junit.Test;

public class DirectoryTest {

    @Test
    public void test() {
	Directory directory = new Directory();
	directory.setHostOrIP("IP");
	assertEquals("IP", directory.getHostOrIP());
	directory.setPath("path");
	assertEquals("path", directory.getPath());
	directory.setUserID("e738670");
	assertEquals("e738670", directory.getUserID());
    }

}

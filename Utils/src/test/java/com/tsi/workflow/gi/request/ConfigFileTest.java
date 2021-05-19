package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigFileTest {

    @Test
    public void test() {
	ConfigFile configFile = new ConfigFile();
	configFile.setFileName("value");
	assertEquals("value", configFile.getFileName());
	configFile.setHostOrIP("IP");
	assertEquals("IP", configFile.getHostOrIP());
	configFile.setPath("path");
	assertEquals("path", configFile.getPath());
	configFile.setUserID("e738090");
	assertEquals("e738090", configFile.getUserID());
    }

}

package com.tsi.workflow.gi.response;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileRuleTest {

    @Test
    public void test() {
	FileRule fileRule = new FileRule();
	fileRule.setFile(new File());
	assertNotNull(fileRule.getFile());
    }

}

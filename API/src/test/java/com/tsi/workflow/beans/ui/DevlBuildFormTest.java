package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.Test;

public class DevlBuildFormTest {

    @Test
    public void test001() throws Throwable {
	DevlBuildForm devlBuildForm = new DevlBuildForm();
	devlBuildForm.setAllowDevlBuild(Boolean.FALSE);
	assertEquals(false, devlBuildForm.isAllowDevlBuild());
    }

    @Test
    public void test002() throws Throwable {
	DevlBuildForm devlBuildForm = new DevlBuildForm();
	devlBuildForm.setAllowRebuild(Boolean.FALSE);
	assertEquals(false, devlBuildForm.isAllowRebuild());
    }

}

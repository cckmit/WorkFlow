package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import antlr.collections.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.junit.Test;

public class FileExtnReportFormTest {

    @Test
    public void test00() throws Throwable {
	FileExtnReportForm fileExtnReportForm = new FileExtnReportForm();
	fileExtnReportForm.setEndDate(new Date());
	assertEquals(new Date(), fileExtnReportForm.getEndDate());
    }

    @Test
    public void test01() throws Throwable {
	FileExtnReportForm fileExtnReportForm = new FileExtnReportForm();
	fileExtnReportForm.setStartDate(new Date());
	assertEquals(new Date(), fileExtnReportForm.getStartDate());
    }

    @Test
    public void test02() throws Throwable {
	FileExtnReportForm fileExtnReportForm = new FileExtnReportForm();
	java.util.List<String> list = new ArrayList<String>(Arrays.asList(".c", ".asm", ".cpp"));
	fileExtnReportForm.setFileExten(list);
	java.util.List<String> extensions = fileExtnReportForm.getFileExten();
	assertEquals(list, extensions);
    }

    @Test
    public void test03() throws Throwable {
	FileExtnReportForm fileExtnReportForm = new FileExtnReportForm();
	java.util.List<String> list = new ArrayList<String>(Arrays.asList("APO", "PGR", "WSP"));
	fileExtnReportForm.setSystems(list);
	java.util.List<String> systems = fileExtnReportForm.getSystems();
	assertEquals(list, systems);
    }

}

package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.junit.Test;

public class CheckoutSegmentsTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setFuncArea("OBG");
	String string0 = checkoutSegments0.getFuncArea();
	assertFalse(checkoutSegments0.getCommonFile());
	assertEquals("OBG", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setFuncArea("");
	checkoutSegments0.getFuncArea();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setTargetSystem("qP%\"65Pkkp)q)jYI%");
	checkoutSegments0.getTargetSystem();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setTargetSystem("");
	checkoutSegments0.getTargetSystem();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = new Integer((-1760));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	SystemLoad systemLoad0 = new SystemLoad();
	checkoutSegments0.setSystemLoad(systemLoad0);
	checkoutSegments0.getSystemLoad();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer((-1));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setSubject("TjOOxCR=Jwp[");
	checkoutSegments0.getSubject();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.setSubject("");
	checkoutSegments0.getSubject();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer(1);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setStatus("W4?5&0+*pd=g4*=");
	checkoutSegments0.getStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setStatus("");
	checkoutSegments0.getStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setSourceUrl("com.tsi.workflow.beans.dao.CheckoutSegments[ id=");
	checkoutSegments0.getSourceUrl();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	Boolean boolean0 = Boolean.valueOf(true);
	checkoutSegments0.setReviewStatus(boolean0);
	checkoutSegments0.getReviewStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(0);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	Boolean boolean0 = new Boolean("d");
	checkoutSegments0.setReviewStatus(boolean0);
	checkoutSegments0.getReviewStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setRepoDesc("# 4h_sLGY1L@t>");
	checkoutSegments0.getRepoDesc();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.setRefStatus("[>z");
	checkoutSegments0.getRefStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setRefStatus("");
	checkoutSegments0.getRefStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setRefPlan("V`TR2yI663*6F5aZ");
	checkoutSegments0.getRefPlan();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	Date mockDate0 = new Date(2, 2, 1);
	checkoutSegments0.setRefLoadDate(mockDate0);
	checkoutSegments0.getRefLoadDate();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.setProgramName("ibm_");
	checkoutSegments0.getProgramName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setProgramName("");
	checkoutSegments0.getProgramName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setProdFlag(".git");
	checkoutSegments0.getProdFlag();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setProdFlag("");
	checkoutSegments0.getProdFlag();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	ImpPlan impPlan0 = new ImpPlan();
	checkoutSegments0.setPlanId(impPlan0);
	checkoutSegments0.getPlanId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	Date mockDate0 = new Date(0, 1, (-1));
	checkoutSegments0.setModifiedDt(mockDate0);
	checkoutSegments0.getModifiedDt();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setModifiedBy("/!ubq@m?2g.qBCD");
	checkoutSegments0.getModifiedBy();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setModifiedBy("");
	checkoutSegments0.getModifiedBy();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	Implementation implementation0 = new Implementation();
	checkoutSegments0.setImpId(implementation0);
	checkoutSegments0.getImpId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Integer integer0 = new Integer(2);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer(0);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	Integer integer0 = new Integer((-1760));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	Integer integer0 = new Integer((-2884));
	checkoutSegments0.setId(integer0);
	checkoutSegments0.getId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setFuncArea("P )I`u VZ3U7S=");
	checkoutSegments0.getFuncArea();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setFuncArea("");
	checkoutSegments0.getFuncArea();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Integer integer0 = new Integer(2166);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setFileType("3+<Qf:+db");
	checkoutSegments0.getFileType();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	Integer integer0 = new Integer(1);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setFileType("");
	checkoutSegments0.getFileType();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	Integer integer0 = new Integer(2);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setFileName("/wp9J1^/;>r");
	checkoutSegments0.getFileName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setFileName("");
	checkoutSegments0.getFileName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setFileHashCode(" does not support them");
	checkoutSegments0.getFileHashCode();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setFileHashCode("");
	checkoutSegments0.getFileHashCode();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	Date mockDate0 = new Date();
	checkoutSegments0.setCreatedDt(mockDate0);
	checkoutSegments0.getCreatedDt();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setCreatedBy("0|,*'9");
	checkoutSegments0.getCreatedBy();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	Integer integer0 = new Integer((-1760));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setCreatedBy("");
	checkoutSegments0.getCreatedBy();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setCommonFile((Boolean) null);
	Boolean boolean0 = checkoutSegments0.getCommonFile();
	assertNull(boolean0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.setCommitterName("The list of names must not be null");
	checkoutSegments0.getCommitterName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	Integer integer0 = new Integer((-1));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setCommitterName("");
	checkoutSegments0.getCommitterName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setCommitterMailId("com.tsi.workflow.beans.dao.ActivityLog");
	checkoutSegments0.getCommitterMailId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	Integer integer0 = new Integer(556);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setCommitterMailId("");
	checkoutSegments0.getCommitterMailId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	Integer integer0 = new Integer((-1));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setCommitId("9^if'q.Y)$");
	checkoutSegments0.getCommitId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test47() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setCommitId("");
	checkoutSegments0.getCommitId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test48() throws Throwable {
	Integer integer0 = new Integer((-4748));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	Date mockDate0 = new Date((-4748), (-4748), 0, (-4748), 0, 0);
	checkoutSegments0.setCommitDateTime(mockDate0);
	checkoutSegments0.getCommitDateTime();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test49() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setAuthorName("com.tsi.workflow.beans.dao.CheckoutSegments[ id=");
	checkoutSegments0.getAuthorName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test50() throws Throwable {
	Integer integer0 = new Integer(1);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setAuthorName("");
	checkoutSegments0.getAuthorName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test51() throws Throwable {
	Integer integer0 = new Integer(0);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.setAuthorMailId(" ]");
	checkoutSegments0.getAuthorMailId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test52() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setAuthorMailId("");
	checkoutSegments0.getAuthorMailId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test53() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setActive("com.tsi.workflow.beans.dao.CheckoutSegments[ id=");
	checkoutSegments0.getActive();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test54() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setActive("");
	checkoutSegments0.getActive();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test55() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getFuncArea();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test56() throws Throwable {
	Integer integer0 = new Integer((-40));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	CheckoutSegments checkoutSegments1 = new CheckoutSegments();
	boolean boolean0 = checkoutSegments0.equals(checkoutSegments1);
	assertFalse(checkoutSegments1.getCommonFile());
	assertFalse(boolean0);
	assertFalse(checkoutSegments1.equals((Object) checkoutSegments0));
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test57() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	Integer integer0 = new Integer((-1));
	CheckoutSegments checkoutSegments1 = new CheckoutSegments(integer0);
	boolean boolean0 = checkoutSegments0.equals(checkoutSegments1);
	assertFalse(checkoutSegments0.getCommonFile());
	assertFalse(boolean0);
	assertFalse(checkoutSegments1.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test58() throws Throwable {
	Integer integer0 = new Integer(1);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	boolean boolean0 = checkoutSegments0.equals(checkoutSegments0);
	assertTrue(boolean0);
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test59() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	boolean boolean0 = checkoutSegments0.equals((Object) null);
	assertFalse(boolean0);
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test60() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	boolean boolean0 = checkoutSegments0.equals(checkoutSegments0);
	assertTrue(boolean0);
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test61() throws Throwable {
	Integer integer0 = new Integer(1);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.hashCode();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test62() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.hashCode();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test63() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.getAuthorMailId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test64() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getRefPlan();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test65() throws Throwable {
	Integer integer0 = new Integer((-1760));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getFileType();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test66() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setSourceUrl("");
	checkoutSegments0.getSourceUrl();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test67() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.getActive();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test68() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.getCreatedBy();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test69() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.getPlanId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test70() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setRepoDesc("");
	checkoutSegments0.getRepoDesc();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test71() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.getSubject();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test72() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getCreatedDt();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test73() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getCommitId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test74() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test75() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getCommitterName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test76() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getModifiedDt();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test77() throws Throwable {
	Integer integer0 = new Integer((-1760));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getSystemLoad();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test78() throws Throwable {
	Integer integer0 = new Integer((-1760));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getCommitterMailId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test79() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	String string0 = checkoutSegments0.toString();
	assertEquals("com.tsi.workflow.beans.dao.CheckoutSegments[ id=null ]", string0);
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test80() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	Boolean boolean0 = checkoutSegments0.getCommonFile();
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test81() throws Throwable {
	Integer integer0 = new Integer((-40));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getRefStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test82() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test83() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getSourceUrl();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test84() throws Throwable {
	Integer integer0 = new Integer((-40));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getProdFlag();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test85() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getImpId();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test86() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getRefLoadDate();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test87() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getProgramName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test88() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getCommitDateTime();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test89() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getAuthorName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test90() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.setRefPlan("");
	checkoutSegments0.getRefPlan();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test91() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.getFileName();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test92() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getTargetSystem();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test93() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getFileHashCode();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test94() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	Boolean boolean0 = Boolean.TRUE;
	checkoutSegments0.setCommonFile(boolean0);
	Boolean boolean1 = checkoutSegments0.getCommonFile();
	assertTrue(boolean1);
    }

    @Test(timeout = 4000)
    public void test95() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments();
	checkoutSegments0.getReviewStatus();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test97() throws Throwable {
	CheckoutSegments checkoutSegments0 = new CheckoutSegments((Integer) null);
	checkoutSegments0.getModifiedBy();
	assertFalse(checkoutSegments0.getCommonFile());
    }

    @Test(timeout = 4000)
    public void test98() throws Throwable {
	Integer integer0 = new Integer((-1760));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	checkoutSegments0.getRepoDesc();
	assertFalse(checkoutSegments0.getCommonFile());
    }
}


package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class QATestingStatusActivityMessageTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	Implementation implementation0 = new Implementation("5sMSN|ZtE<E`@Av1Ug`");
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	qATestingStatusActivityMessage0.setSystemName("");
	String string0 = qATestingStatusActivityMessage0.getSystemName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	Implementation implementation0 = new Implementation("N<4sTzc4`^aF6[.9");
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	qATestingStatusActivityMessage0.setQaPhaseName("com.tsi.workflow.activity.QATestingStatusActivityMessage");
	String string0 = qATestingStatusActivityMessage0.getQaPhaseName();
	assertEquals("com.tsi.workflow.activity.QATestingStatusActivityMessage", string0);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	Implementation implementation0 = new Implementation();
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	qATestingStatusActivityMessage0.setQaPhaseName("");
	String string0 = qATestingStatusActivityMessage0.getQaPhaseName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	Implementation implementation0 = new Implementation();
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	String string0 = qATestingStatusActivityMessage0.getSystemName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test4() throws Throwable {
	Implementation implementation0 = new Implementation();
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	String string0 = qATestingStatusActivityMessage0.getQaPhaseName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test5() throws Throwable {
	Implementation implementation0 = new Implementation();
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	String string0 = qATestingStatusActivityMessage0.processMessage();
	assertEquals("null testing has been ByPassed for System null", string0);
    }

    @Test(timeout = 4000)
    public void test6() throws Throwable {
	Implementation implementation0 = new Implementation();
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	Priority priority0 = qATestingStatusActivityMessage0.getLogLevel();
	assertEquals(30000, Priority.WARN_INT);
    }

    @Test(timeout = 4000)
    public void test7() throws Throwable {
	Implementation implementation0 = new Implementation();
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	qATestingStatusActivityMessage0.setArguments((IBeans[]) null);
	assertNull(qATestingStatusActivityMessage0.getSystemName());
    }

    @Test(timeout = 4000)
    public void test8() throws Throwable {
	Implementation implementation0 = new Implementation();
	QATestingStatusActivityMessage qATestingStatusActivityMessage0 = new QATestingStatusActivityMessage((ImpPlan) null, implementation0);
	qATestingStatusActivityMessage0.setSystemName("com.tsi.workflow.beans.dao.Implementation[ id=null ]");
	String string0 = qATestingStatusActivityMessage0.getSystemName();
	assertEquals("com.tsi.workflow.beans.dao.Implementation[ id=null ]", string0);
    }
}

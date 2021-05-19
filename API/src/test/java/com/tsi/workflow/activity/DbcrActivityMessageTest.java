/**
 * 
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import java.util.regex.Pattern;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class DbcrActivityMessageTest {

    public DbcrActivityMessageTest() {
    }

    DbcrActivityMessage instance;

    @Test
    public void dbcrActivityMessageTest() {

	DbcrActivityMessage dbcrActivityMessage = new DbcrActivityMessage(null, null);
	dbcrActivityMessage.getUser();
	assertTrue(true);

    }

    @SuppressWarnings("deprecation")
    @Test
    public void dbcrActivityMessageLogTest() {
	DbcrActivityMessage dbcrActivityMessageLog = DataWareHouse.getDbcrActivityMessage();
	assertEquals("T1800089", dbcrActivityMessageLog.getImpPlanId());
	assertEquals("J0200", dbcrActivityMessageLog.getDbcrName());
	assertEquals("e738090", dbcrActivityMessageLog.getCreatedBy());
	assertEquals("WSP", dbcrActivityMessageLog.getTargetSystem());
	assertEquals("Added", dbcrActivityMessageLog.getAction());
	assertEquals(Priority.INFO, dbcrActivityMessageLog.getLogLevel());
	assertEquals(null, dbcrActivityMessageLog.getUser());
    }

    @Test
    public void testSetArguments() {
	IBeans[] beans = null;
	DbcrActivityMessage instance = new DbcrActivityMessage(null, null);
	instance.setArguments(beans);
	assertTrue(true);
    }

    @Test
    public void testProcessMessage() {
	DbcrActivityMessage instance = new DbcrActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	String result = instance.processMessage();
	assertNotNull(Pattern.matches(".* has Added DBCR(s)   .* for target System .* ", result));
    }
}

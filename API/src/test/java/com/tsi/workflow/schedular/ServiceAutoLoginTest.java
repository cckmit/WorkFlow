
package com.tsi.workflow.schedular;

import com.tsi.workflow.utils.Constants;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ServiceAutoLoginTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	Boolean boolean0 = Boolean.TRUE;
	ServiceAutoLogin serviceAutoLogin0 = new ServiceAutoLogin();
	Constants.MTPSERVICE_KEY_MAP = boolean0;
	serviceAutoLogin0.serviceUserAutoLogin();
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	ServiceAutoLogin serviceAutoLogin0 = new ServiceAutoLogin();
	// Undeclared exception!
	try {
	    serviceAutoLogin0.serviceUserAutoLogin();
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {

	}
    }
}

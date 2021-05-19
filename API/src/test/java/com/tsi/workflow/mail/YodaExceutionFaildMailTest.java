package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class YodaExceutionFaildMailTest {

    private YodaExceutionFaildMail yodaExceutionFaildMail;

    @Before
    public void setUp() throws Exception {
	yodaExceutionFaildMail = new YodaExceutionFaildMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testYodaExceutionFaildMail() {
	yodaExceutionFaildMail.setLoadSetName(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadSetName());
	assertEquals(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadSetName(), yodaExceutionFaildMail.getLoadSetName());
	yodaExceutionFaildMail.setPlanId(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), yodaExceutionFaildMail.getPlanId());
	yodaExceutionFaildMail.setVparList(getVparsList());

	yodaExceutionFaildMail.processMessage();

    }

    private List<String> getVparsList() {
	List<String> list = new ArrayList<>();
	list.add(DataWareHouse.getSystemList().get(0).getVparsList().get(0).getName());
	return list;
    }

}

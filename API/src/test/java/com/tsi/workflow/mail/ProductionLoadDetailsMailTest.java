package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.beans.ui.ProdPlanDetails;
import com.tsi.workflow.beans.ui.ProdSystemDetails;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.velocity.app.VelocityEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ProductionLoadDetailsMailTest {

    @Mock
    ProductionLoadDetailsMail productionLoadDetailsMail;
    @Mock
    VelocityEngine velocityEngine;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProcessMessage() {

	productionLoadDetailsMail.setBeforeDayDetails(getBeforeDayDetailsMap());
	productionLoadDetailsMail.setCompanyName("TravelPort");
	Mockito.when(velocityEngine.mergeTemplate(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(false);
	productionLoadDetailsMail.processMessage();
    }

    private Map<String, ProdPlanDetails> getBeforeDayDetailsMap() {
	Map<String, ProdPlanDetails> beforeDayDetailsMap = new HashMap<>();
	ProdPlanDetails prodPlanDetails = new ProdPlanDetails();
	prodPlanDetails.setActivateddatetime(new Date(2019, 8, 7));
	prodPlanDetails.setDeveloperid("AER4786");
	prodPlanDetails.setDevelopername("DeveloperName");
	prodPlanDetails.setDevmamangername("DevMangerName");
	prodPlanDetails.setDevmanagerid("MAN786");
	prodPlanDetails.setLoaddatetime(new Date(2019, 8, 7));
	prodPlanDetails.setPlandescription("Plan Description");
	prodPlanDetails.setPlanid("P32878778");
	prodPlanDetails.setPlanstatus("Plan Status");
	prodPlanDetails.setProdSystemDetails(getProdSystemDetails());
	beforeDayDetailsMap.put("ABCD", prodPlanDetails);
	return beforeDayDetailsMap;
    }

    private List<ProdSystemDetails> getProdSystemDetails() {
	List<ProdSystemDetails> list = new ArrayList<>();
	ProdSystemDetails ProdSystemDetails = new ProdSystemDetails();
	ProdSystemDetails.setProdstatus("DEACTIVATED");
	list.add(ProdSystemDetails);
	return list;
    }

}

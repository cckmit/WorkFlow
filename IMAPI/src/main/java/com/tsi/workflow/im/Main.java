/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.im;

import static com.tsi.workflow.im.IMTicket.createIssue;
import static com.tsi.workflow.im.IMTicket.editIssue;

import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.util.APIVersion;
import com.tsi.workflow.im.IMOptions.LoadType;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
	BasicConfigurator.configure();
	IntegrationPoint lIMPoint = null;
	Session lSession = null;
	try {
	    String lUser = IMOptions.getProperty(IMOptions.PreProdKeys.REQUESTOR_NAME);
	    String lPass = IMOptions.getProperty(IMOptions.OtherKeys.REQUESTOR_PASSWORD);

	    // lIMPoint =
	    // IntegrationPointFactory.getInstance().createIntegrationPoint("hpvrdmksr01v.resource.corp.lcl",
	    // 7001, false, APIVersion.API_4_13);
	    lIMPoint = IntegrationPointFactory.getInstance().createIntegrationPoint("si.worldspan.com", 7001, false, APIVersion.API_4_13);
	    lSession = lIMPoint.createSession(lUser, lPass);

	    String lPreProdIMTicket = createIssue(lSession);
	    if (lPreProdIMTicket == null || lPreProdIMTicket.isEmpty()) {
		System.exit(1);
	    }
	    boolean lEditStaus = editIssue(lSession, LoadType.PRE_PROD, lPreProdIMTicket);
	    if (!lEditStaus) {
		System.exit(1);
	    }

	    String lProdIMTicket = createIssue(lSession, lPreProdIMTicket);
	    if (lProdIMTicket == null || lProdIMTicket.isEmpty()) {
		System.exit(1);
	    }
	    lEditStaus = editIssue(lSession, LoadType.PROD, lProdIMTicket);
	    if (!lEditStaus) {
		System.exit(1);
	    }

	    LOG.info("SUCCESS: PreProd IM Ticket Number = " + lPreProdIMTicket);
	    LOG.info("SUCCESS: Prod IM Ticket Number = " + lProdIMTicket);
	} catch (Exception e) {
	    LOG.error("Error in Creating Ticket" + e.getMessage());
	} finally {
	    try {
		if (lSession != null) {
		    lSession.release();
		}
	    } catch (Exception e) {
		LOG.error("Error in Closing Session" + e.getMessage());
	    }
	    if (lIMPoint != null) {
		lIMPoint.release();
	    }
	}
    }

}

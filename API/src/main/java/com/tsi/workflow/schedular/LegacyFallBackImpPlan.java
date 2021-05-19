package com.tsi.workflow.schedular;

import com.google.gson.Gson;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.LegacyFallBackPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.LegacyFallBackPlanDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vinoth.ponnurangan
 *
 */
@Component
public class LegacyFallBackImpPlan {

    private static final Logger LOG = Logger.getLogger(LegacyFallBackImpPlan.class.getName());

    @Autowired
    LegacyFallBackPlanDAO legacyFallBackDAO;

    @Autowired
    RejectHelper rejectHelper;

    @Autowired
    TOSConfig config;

    @Autowired
    WFConfig wFConfig;

    @Autowired
    SystemDAO systemDAO;

    @Autowired
    SSHClientUtils sSHClientUtils;

    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    @Autowired
    ImpPlanDAO impPlanDAO;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public LegacyFallBackPlanDAO getLegacyFallBackDAO() {
	return legacyFallBackDAO;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public TOSConfig getConfig() {
	return config;
    }

    public WFConfig getwFConfig() {
	return wFConfig;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public void setImpPlanDAO(ImpPlanDAO impPlanDAO) {
	this.impPlanDAO = impPlanDAO;
    }

    @Transactional
    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE)
    public void populateLegacyGitInfo() throws Exception {
	if (wFConfig.getPrimary()) {
	    populateLegacyImpPlanFromGit();
	}
    }

    private void populateLegacyImpPlanFromGit() throws Exception {
	User lUser = getLDAPAuthenticatorImpl().getServiceUser();
	List<System> lSystemList = getSystemDAO().findAll();
	JSONResponse lResponse = new JSONResponse();

	Set<String> rejectionList = new HashSet<>();
	for (System lSystem : lSystemList) {
	    if (lSystem.getPlatformId().getNickName().equals(Constants.TRAVELPORT) && !lSystem.getName().equals(Constants.getDSLLoadsetSystem().get(0))) {
		String command = Constants.SystemScripts.LEGACY_FALL_BACK_PALN.getScript() + " " + lSystem.getName().toLowerCase();
		lResponse = getsSHClientUtils().executeCommand(lSystem, command);
		if (lResponse.getStatus()) {
		    String lresponseData = (String) lResponse.getData();
		    if (lresponseData != null && !lresponseData.isEmpty()) {
			LOG.info("Legacy Plan Response form Shell Script " + lresponseData);
			LegacyFallBackPlan[] legacyPlan = new Gson().fromJson(lresponseData.trim(), LegacyFallBackPlan[].class);
			for (LegacyFallBackPlan lplan : legacyPlan) {

			    if (lplan.getDevopsPlan() != null && !lplan.getDevopsPlan().trim().isEmpty() && !lplan.getDevopsPlan().equalsIgnoreCase("NULL")) {
				rejectionList.addAll(Arrays.asList(lplan.getDevopsPlan().toUpperCase().split(",")).stream().filter(plan -> !plan.trim().isEmpty()).collect(Collectors.toSet()));
			    }

			    getLegacyFallBackDAO().save(lUser, lplan);
			}

		    }
		}
	    }
	}

	// Check the rejection plan list and reject based on the given cond`
	LOG.info("# of legacy Plans obtained: " + rejectionList.size());

	for (String plan : rejectionList) {
	    LOG.info("# Rejected  legacy Plans List: " + rejectionList);
	    getRejectHelper().rejectPlan(lUser, plan, Constants.REJECT_REASON.REJECTION.getValue(), Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
	}
    }

}

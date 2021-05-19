package com.tsi.workflow.schedular;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.ui.ProdPlanDetails;
import com.tsi.workflow.beans.ui.ProdSystemDetails;
import com.tsi.workflow.beans.ui.ProductionLoadDetailsForm;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.ProductionLoadDetailsMail;
import com.tsi.workflow.utils.DateHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ProductionDeploymentsSchedular.
 *
 * @author vinoth.ponnurangan
 */
@Component
public class ProductionDeploymentsSchedular {

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(ProductionDeploymentsSchedular.class.getName());

    /** The imp plan DAO. */
    @Autowired
    ImpPlanDAO impPlanDAO;

    /** The system load DAO. */
    @Autowired
    SystemLoadDAO systemLoadDAO;

    /** The reject helper. */
    @Autowired
    RejectHelper rejectHelper;

    /** The l DAP authenticator impl. */
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    /** The put level DAO. */
    @Autowired
    PutLevelDAO putLevelDAO;

    /** The implementation DAO. */
    @Autowired
    ImplementationDAO implementationDAO;

    /** The mail message factory. */
    @Autowired
    MailMessageFactory mailMessageFactory;

    /** The w F config. */
    @Autowired
    WFConfig wFConfig;

    /**
     * Gets the mail message factory.
     *
     * @return the mail message factory
     */
    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    /**
     * Gets the production deployment loads.
     * 
     * Story:ZTPFM-1795
     * 
     * @author vinoth.ponnurangan
     *
     */
    // @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE)
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void getProductionDeploymentLoads() {
	if (wFConfig.getPrimary()) {
	    String companyName = null;
	    LOG.info("Production Load Schdular Running " + new Date());
	    companyName = "dl";
	    List<ProductionLoadDetailsForm> productionLoadBeforeDayDetails = impPlanDAO.getProductionLoadsBeforeDayDetails(companyName);
	    List<ProductionLoadDetailsForm> productionLoadNextDayDetails = impPlanDAO.getProductionLoadsNextDayDetails(companyName);
	    Map<String, ProdPlanDetails> deployedInProd = new HashMap<>();
	    Map<String, ProdPlanDetails> deployedInProdNextDay = new HashMap<>();
	    getProdLoadStatusPreviousAndNextDayData(productionLoadBeforeDayDetails, deployedInProd);
	    getProdLoadStatusPreviousAndNextDayData(productionLoadNextDayDetails, deployedInProdNextDay);
	    Set<String> lPlanList = new HashSet<>();
	    List<String> beforeDayPlan = productionLoadBeforeDayDetails.stream().map(ProductionLoadDetailsForm::getPlanid).collect(Collectors.toList());
	    List<String> nextDayPlan = productionLoadNextDayDetails.stream().map(ProductionLoadDetailsForm::getPlanid).collect(Collectors.toList());
	    if (!beforeDayPlan.isEmpty() || !nextDayPlan.isEmpty()) {
		LOG.info("Before Day Plan List :-  " + beforeDayPlan + "  Next Day Plan List:-  " + nextDayPlan);
		lPlanList.addAll(beforeDayPlan);
		lPlanList.addAll(nextDayPlan);
	    }
	    if (!productionLoadBeforeDayDetails.isEmpty() || !productionLoadNextDayDetails.isEmpty()) {
		ProductionLoadDetailsMail productionLoadDetailsMail = (ProductionLoadDetailsMail) getMailMessageFactory().getTemplate(ProductionLoadDetailsMail.class);
		productionLoadDetailsMail.setBeforeDayDetails(deployedInProd);
		productionLoadDetailsMail.setCompanyName("Delta");
		productionLoadDetailsMail.setNextDayDetails(deployedInProdNextDay);
		if (wFConfig.getProdLoadsCentreMailId() != null) {
		    productionLoadDetailsMail.addToProdLoadCentre(true);
		}
		LOG.info("Mail Send Plan  List " + lPlanList);
		getMailMessageFactory().push(productionLoadDetailsMail);

	    }

	    companyName = "tp";
	    List<ProductionLoadDetailsForm> productionLoadTPBeforeDayDetails = impPlanDAO.getProductionLoadsBeforeDayDetails(companyName);
	    List<ProductionLoadDetailsForm> productionLoadTPNextDayDetails = impPlanDAO.getProductionLoadsNextDayDetails(companyName);
	    Map<String, ProdPlanDetails> deployedTPInProd = new HashMap<>();
	    Map<String, ProdPlanDetails> deployedTPInProdNextDay = new HashMap<>();
	    getProdLoadStatusPreviousAndNextDayData(productionLoadTPBeforeDayDetails, deployedTPInProd);
	    getProdLoadStatusPreviousAndNextDayData(productionLoadTPNextDayDetails, deployedTPInProdNextDay);
	    Set<String> lTPPlanList = new HashSet<>();
	    List<String> beforeDayTPPlan = productionLoadTPBeforeDayDetails.stream().map(ProductionLoadDetailsForm::getPlanid).collect(Collectors.toList());
	    List<String> nextDayTPPlan = productionLoadTPNextDayDetails.stream().map(ProductionLoadDetailsForm::getPlanid).collect(Collectors.toList());
	    if (!beforeDayTPPlan.isEmpty() || !nextDayTPPlan.isEmpty()) {
		LOG.info("Before Day Plan List :-  " + beforeDayTPPlan + "  Next Day Plan List:-  " + nextDayTPPlan);
		lTPPlanList.addAll(beforeDayTPPlan);
		lTPPlanList.addAll(nextDayTPPlan);
	    }
	    if (!productionLoadTPBeforeDayDetails.isEmpty() || !productionLoadTPNextDayDetails.isEmpty()) {
		ProductionLoadDetailsMail productionLoadDetailsMail = (ProductionLoadDetailsMail) getMailMessageFactory().getTemplate(ProductionLoadDetailsMail.class);
		productionLoadDetailsMail.setBeforeDayDetails(deployedTPInProd);
		productionLoadDetailsMail.setNextDayDetails(deployedTPInProdNextDay);
		productionLoadDetailsMail.setCompanyName("TravelPort");
		if (wFConfig.getProdLoadsCentreMailId() != null) {
		    productionLoadDetailsMail.addToProdLoadCentre(true);
		}
		LOG.info("Mail Send Plan  List " + lTPPlanList);
		getMailMessageFactory().push(productionLoadDetailsMail);

	    }
	}

    }

    private void getProdLoadStatusPreviousAndNextDayData(List<ProductionLoadDetailsForm> productionLoadDetails, Map<String, ProdPlanDetails> deployedStatus) {
	productionLoadDetails.forEach(prevProd -> {
	    if (!deployedStatus.containsKey(prevProd.getPlanid())) {
		ProdPlanDetails prod = new ProdPlanDetails();
		prod.setPlanid(prevProd.getPlanid());
		prod.setPlandescription(prevProd.getPlandescription());
		prod.setDeveloperid(prevProd.getDeveloperid());
		prod.setDevelopername(prevProd.getDevelopername());
		prod.setDevmanagerid(prevProd.getDevmanagerid());
		prod.setDevmamangername(prevProd.getDevmamangername());
		prod.setLoadtype(prevProd.getLoadtype());
		prod.setPlanstatus(prevProd.getPlanstatus());
		prod.setLoaddatetime(prevProd.getLoaddatetime());
		prod.setActivateddatetime(prevProd.getActivateddatetime());
		prod.setProdSystemDetails(new ArrayList<>());
		deployedStatus.put(prevProd.getPlanid(), prod);
	    }
	    ProdSystemDetails prodSystem = new ProdSystemDetails();
	    prodSystem.setTargetsystem(prevProd.getTargetsystem());
	    prodSystem.setProdstatus(prevProd.getProdstatus());
	    prodSystem.setProgramname(prevProd.getProgramname());
	    prodSystem.setLoaddatetime(DateHelper.convertGMTtoEST(prevProd.getLoaddatetime()));
	    prodSystem.setPlanid(prevProd.getPlanid());
	    deployedStatus.get(prevProd.getPlanid()).getProdSystemDetails().add(prodSystem);
	});
    }
}

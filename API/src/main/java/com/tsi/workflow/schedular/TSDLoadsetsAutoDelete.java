package com.tsi.workflow.schedular;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.service.TSDService;
import com.tsi.workflow.utils.Constants;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.management.timer.Timer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TSDLoadsetsAutoDelete {

    private static final Logger LOG = Logger.getLogger(TSDLoadsetsAutoDelete.class.getName());

    @Autowired
    ProductionLoadsDAO productionLoadsDAO;

    @Autowired
    TSDService tsdService;

    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    @Autowired
    WFConfig wFConfig;

    @Autowired
    ImpPlanDAO impPlanDAO;

    public TSDService getTSDService() {
	return tsdService;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE)
    @Transactional
    public void deleteLoadsetFromTSD() {
	if (wFConfig.getPrimary()) {
	    Date date = Date.from(LocalDateTime.now().minusDays(3).atZone(ZoneId.systemDefault()).toInstant());
	    List<ProductionLoads> prodPlans = getProductionLoadsDAO().getProdLoadDeactivatedPlans(date);

	    HashMap<String, List<ProductionLoads>> planIdBasedProdPlans = new HashMap<>();

	    // Group by Plans
	    prodPlans.forEach(prodPlan -> {
		if (planIdBasedProdPlans.containsKey(prodPlan.getPlanId().getId())) {
		    planIdBasedProdPlans.get(prodPlan.getPlanId().getId()).add(prodPlan);
		} else {
		    List<ProductionLoads> temp = new ArrayList<>();
		    temp.add(prodPlan);
		    planIdBasedProdPlans.put(prodPlan.getPlanId().getId(), temp);
		}
	    });
	    List<ProductionLoads> plansToBeDelete = new ArrayList<>();
	    planIdBasedProdPlans.forEach((key, values) -> {
		if (!values.stream().filter(prodPlan -> (prodPlan.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) || (prodPlan.getLastActionStatus().equalsIgnoreCase("INPROGRESS") && prodPlan.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()))).findAny().isPresent()) {
		    values.stream().filter(prod -> !prod.getPlanId().getPlanStatus().equals(Constants.PlanStatus.PENDING_FALLBACK.name()) && !(prod.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name()) && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS"))).forEach(prodPlan -> {
			LOG.info("Processing Prod Plan : " + prodPlan.getPlanId().getId());
			SortedSet<String> lSet = new TreeSet<>();
			List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallback(prodPlan.getPlanId().getId());
			for (Object[] plan : segmentRelatedPlans) {
			    String planStatus = plan[1].toString();
			    if (Constants.PlanStatus.getReadyForProductionandAboveMap().containsKey(planStatus)) {
				if (!Constants.PlanStatus.FALLBACK.name().equals(planStatus)) {
				    lSet.add(plan[0].toString());
				}
			    }
			}

			LOG.info("List of dep plans to delete or fallback: " + lSet.stream().collect(Collectors.joining(",")));
			if (lSet.isEmpty()) {
			    prodPlan.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
			    plansToBeDelete.add(prodPlan);
			}
		    });
		}
	    });

	    plansToBeDelete.forEach(prodPlan -> {
		getTSDService().postActivationAction(lDAPAuthenticatorImpl.getServiceUser(), prodPlan, false, StringUtils.EMPTY);
	    });
	}
    }
}

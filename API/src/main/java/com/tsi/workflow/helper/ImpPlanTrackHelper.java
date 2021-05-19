package com.tsi.workflow.helper;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.tracker.PlanTrackerData;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.TrackStatusForm;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImpPlanTrackHelper {

    private static final Logger LOG = Logger.getLogger(ImpPlanTrackHelper.class.getName());
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    WFConfig wfConfig;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;

    public WFConfig getWfConfig() {
	return wfConfig;
    }

    public CommonHelper getCommonHelper() {
	return commonHelper;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public void setCurrentStage(PlanTrackerData planTrackerData, ImpPlan lPlan) {

	boolean isStatusAssigned = false;
	for (TrackStatusForm stage : planTrackerData.getStages()) {
	    // In progress
	    if ("In Progress".equalsIgnoreCase(stage.getStatus())) {
		if (!isPlanReadyForQa(lPlan)) {
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		    setInProgMessage(stage, lPlan);
		    isStatusAssigned = true;
		} else if (isPlanReadyForQa(lPlan)) {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		}
	    }

	    // Devl Build and load sets
	    if ("DEVL Build /Loadset".equalsIgnoreCase(stage.getStatus())) {
		boolean isMessageAdded = false;
		if (!lPlan.getMacroHeader() && lPlan.getImplementationList() != null && !lPlan.getImplementationList().isEmpty() && !getCommonHelper().isDevBuildCompleted(lPlan) && !lPlan.getImplementationList().stream().filter(imp -> imp.getActive().equals("Y") && imp.getImpStatus().equals(Constants.ImplementationStatus.IN_PROGRESS.name())).findAny().isPresent()) {
		    if (isPlanReadyForQa(lPlan)) {
			stage.setCurrentStage(Boolean.TRUE);
		    }
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		    setDevBuildOrLoadMessage(stage, lPlan, true);
		    isMessageAdded = true;
		    isStatusAssigned = true;
		}
		if (!isMessageAdded) {
		    if (!lPlan.getMacroHeader() && getCommonHelper().isDevBuildCompleted(lPlan) && getCommonHelper().isDevLoadSetCompleted(lPlan)) {
			stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		    } else if (!lPlan.getMacroHeader() && lPlan.getImplementationList() != null && !lPlan.getImplementationList().isEmpty() && getCommonHelper().isDevBuildCompleted(lPlan) && !getCommonHelper().isDevLoadSetCompleted(lPlan)) {
			if (isPlanReadyForQa(lPlan)) {
			    stage.setCurrentStage(Boolean.TRUE);
			}
			stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
			setDevBuildOrLoadMessage(stage, lPlan, false);
			isStatusAssigned = true;
		    }
		}

	    }

	    // QA Bypass status
	    if (!lPlan.getMacroHeader()) {
		setQaBypassStatus(planTrackerData, lPlan);
	    }

	    // Submit Status
	    if (!isStatusAssigned && "Submit".equalsIgnoreCase(stage.getStatus())) {
		if (isPlanSubmitted(lPlan) && Constants.PlanStatus.getApprovedStatusMap().containsKey(lPlan.getPlanStatus())) {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		} else if (!isPlanSubmitted(lPlan)) {
		    isStatusAssigned = true;
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		    setSubmitMessage(stage, lPlan);
		}
	    }

	    // QA Functional Testing status
	    if (!isStatusAssigned && "QA Functional Testing".equalsIgnoreCase(stage.getStatus())) {
		if (qaFunctionalValidation(stage, lPlan)) {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		} else if (isPlanSubmitted(lPlan) && !qaFunctionalValidation(stage, lPlan)) {
		    isStatusAssigned = true;
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		}
	    }

	    // QA Regression testing status
	    if (!isStatusAssigned && "QA Regression Testing".equalsIgnoreCase(stage.getStatus())) {
		if (!qaRegressionValidation(stage, lPlan)) {
		    isStatusAssigned = true;
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		} else {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		}
	    }

	    // Pre production Status
	    if (!isStatusAssigned && "Pre-Production Deployment".equalsIgnoreCase(stage.getStatus())) {
		if (!preProdDeployValidation(stage, lPlan)) {
		    isStatusAssigned = true;
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		} else {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		}
	    }

	    // UAT status
	    if (!isStatusAssigned && "User Acceptance Testing".equalsIgnoreCase(stage.getStatus())) {
		if (!UATValidation(stage, lPlan)) {
		    isStatusAssigned = true;
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		} else {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		}
	    }

	    // Approved or Passed acceptance testing
	    if (!isStatusAssigned && "Dev Manager Approval".equalsIgnoreCase(stage.getStatus())) {
		if ((lPlan.getMacroHeader() && Constants.PlanStatus.APPROVED.name().equals(lPlan.getPlanStatus())) || Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name().equals(lPlan.getPlanStatus())) {
		    isStatusAssigned = true;
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		    stage.getMessages().add("Dev Manager " + lPlan.getDevManagerName() + " has to mark the implementation plan " + lPlan.getId() + " as Approved");
		} else {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		}
	    }

	    // Loads ctrl status
	    if (!isStatusAssigned && "Loads Control".equalsIgnoreCase(stage.getStatus())) {
		if (Constants.PlanStatus.DEV_MGR_APPROVED.name().equals(lPlan.getPlanStatus())) {
		    isStatusAssigned = true;
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		    stage.getMessages().add("Loads Control team has to mark the implementation plan " + lPlan.getId() + "as Ready for Production Deployment");
		} else {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		}
	    }

	    // TSD Status
	    if (!isStatusAssigned && "TSD".equalsIgnoreCase(stage.getStatus())) {
		if (!tsdValidation(stage, lPlan)) {
		    stage.setCurrentStage(Boolean.TRUE);
		    stage.setCurrentStatus(Constants.TrackerStatus.IN_PROGRESS.name());
		} else {
		    stage.setCurrentStatus(Constants.TrackerStatus.COMPLETED.name());
		}
	    }

	}
    }

    private boolean isPlanReadyForQa(ImpPlan lPlan) {
	boolean isPlanReadyForQa = true;

	if (lPlan.getImplementationList() == null || lPlan.getImplementationList().isEmpty() || !lPlan.getImplementationList().stream().filter(imp -> imp.getActive().equals("Y")).findAny().isPresent()) {
	    isPlanReadyForQa = false;
	}
	if (lPlan.getPlanStatus().equals(Constants.PlanStatus.ACTIVE.name()) && lPlan.getImplementationList().stream().filter(imp -> imp.getActive().equals("Y") && imp.getImpStatus().equals(Constants.ImplementationStatus.IN_PROGRESS.name())).findAny().isPresent()) {
	    isPlanReadyForQa = false;
	}
	return isPlanReadyForQa;
    }

    private boolean isPlanSubmitted(ImpPlan lPlan) {
	boolean isPlanReadyToSubmit = true;
	if (isPlanReadyForQa(lPlan) && Constants.PlanStatus.getBeforeApprovedStatus().containsKey(lPlan.getPlanStatus())) {
	    isPlanReadyToSubmit = false;
	}
	return isPlanReadyToSubmit;
    }

    private boolean qaFunctionalValidation(TrackStatusForm pTrackStatusForm, ImpPlan lPlan) {
	boolean isQAFuncCompleted = true;
	if (isPlanSubmitted(lPlan) && Constants.PlanStatus.getQAFunctionalDeploymentStatus().containsKey(lPlan.getPlanStatus())) {
	    Set<String> lSystemList = new TreeSet<>();
	    Set<String> actSystemLoadList = new TreeSet<>();
	    getCommonHelper().getActSystemLoadActionsList(lPlan, Constants.VPARSEnvironment.QA_FUCTIONAL, true, lSystemList, actSystemLoadList);
	    List<SystemLoad> lActiveSystemLoads = getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(), Arrays.asList("NONE", Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name()));
	    if (actSystemLoadList.isEmpty() || actSystemLoadList.size() < 1) {
		isQAFuncCompleted = false;
		pTrackStatusForm.getMessages().add("QA Functional Tester has to deploy the Loadsets to QA Functional Environment in target system(s) " + lSystemList.stream().collect(Collectors.joining(",")));
	    } else if (actSystemLoadList.size() != lActiveSystemLoads.size() || !lPlan.getPlanStatus().equals(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name())) {
		isQAFuncCompleted = false;
		Collection sysToBeLoaded = CollectionUtils.subtract(lSystemList, actSystemLoadList);
		if (sysToBeLoaded == null) {
		    sysToBeLoaded = lSystemList;
		}
		pTrackStatusForm.getMessages().add("QA Functional Tester has to load test systems in Target systems like " + sysToBeLoaded.stream().collect(Collectors.joining(",")));
	    } else if (lPlan.getPlanStatus().equals(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name())) {
		isQAFuncCompleted = false;
		StringBuilder sb = new StringBuilder();
		sb.append("QA Functional Tester  has to mark implementation plan ");
		sb.append(lPlan.getId() + " as Passed QA Functional Testing");
		pTrackStatusForm.getMessages().add(sb.toString());
	    }
	}
	return isQAFuncCompleted;
    }

    private boolean qaRegressionValidation(TrackStatusForm pTrackStatusForm, ImpPlan lPlan) {
	boolean isQARegCompleted = true;
	if (Constants.PlanStatus.getQARegressionDeploymentStatus().containsKey(lPlan.getPlanStatus())) {
	    Set<String> lSystemList = new TreeSet<>();
	    Set<String> actSystemLoadList = new TreeSet<>();
	    getCommonHelper().getActSystemLoadActionsList(lPlan, Constants.VPARSEnvironment.QA_REGRESSION, true, lSystemList, actSystemLoadList);
	    List<SystemLoad> lActiveSystemLoads = getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(), Arrays.asList("NONE", Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name()));
	    if (actSystemLoadList.isEmpty() || actSystemLoadList.size() < 1) {
		isQARegCompleted = false;
		pTrackStatusForm.getMessages().add("QA Regression Tester has to deploy the Loadsets to QA Regression Environment in target system(s) " + lSystemList.stream().collect(Collectors.joining(",")));
	    } else if (actSystemLoadList.size() != lActiveSystemLoads.size() || !lPlan.getPlanStatus().equals(Constants.PlanStatus.DEPLOYED_IN_QA_REGRESSION.name())) {
		isQARegCompleted = false;
		Collection sysToBeLoaded = CollectionUtils.subtract(lSystemList, actSystemLoadList);
		if (sysToBeLoaded == null) {
		    sysToBeLoaded = lSystemList;
		}
		pTrackStatusForm.getMessages().add("QA Regression Tester has to load test systems in Target systems like " + sysToBeLoaded.stream().collect(Collectors.joining(",")));
	    } else if (lPlan.getPlanStatus().equals(Constants.PlanStatus.DEPLOYED_IN_QA_REGRESSION.name())) {
		isQARegCompleted = false;
		StringBuilder sb = new StringBuilder();
		sb.append("QA Regression Tester  has to mark implementation plan ");
		sb.append(lPlan.getId() + " as Passed QA Regression Testing");
		pTrackStatusForm.getMessages().add(sb.toString());
	    }
	}
	return isQARegCompleted;

    }

    private boolean preProdDeployValidation(TrackStatusForm pTrackStatusForm, ImpPlan lPlan) {
	boolean isDeployedInPreProd = true;
	if (Constants.PlanStatus.getApprovedToPreProd().containsKey(lPlan.getPlanStatus())) {
	    Set<String> lSystemList = new TreeSet<>();
	    Set<String> actSystemLoadList = new TreeSet<>();
	    getCommonHelper().getActSystemLoadActionsList(lPlan, Constants.VPARSEnvironment.PRE_PROD, getWfConfig().getIsDeltaApp() ? true : false, lSystemList, actSystemLoadList);
	    if (actSystemLoadList.isEmpty() || actSystemLoadList.size() < 1) {
		isDeployedInPreProd = false;
		StringBuilder sb = new StringBuilder();
		sb.append("System Support user has to Load & Activate Loadsets to Pre-Production Environment in target system(s) " + lSystemList.stream().collect(Collectors.joining(",")));
		pTrackStatusForm.getMessages().add(sb.toString());
	    } else if (actSystemLoadList.size() != lSystemList.size()) {
		isDeployedInPreProd = false;
		Collection sysToBeLoaded = CollectionUtils.subtract(lSystemList, actSystemLoadList);
		pTrackStatusForm.getMessages().add("System Support user has to Load & Activate Implementation plan " + lPlan.getId() + " in Systems like " + sysToBeLoaded.stream().collect(Collectors.joining(",")));
	    }
	}
	return isDeployedInPreProd;
    }

    private boolean UATValidation(TrackStatusForm pTrackStatusForm, ImpPlan lPlan) {
	boolean isUATCompleted = true;
	Set<String> lSystemList = new TreeSet<>();
	Set<String> actSystemLoadList = new TreeSet<>();
	getCommonHelper().getActSystemLoadActionsList(lPlan, Constants.VPARSEnvironment.PRE_PROD, getWfConfig().getIsDeltaApp() ? true : false, lSystemList, actSystemLoadList);
	if (Constants.PlanStatus.DEPLOYED_IN_PRE_PRODUCTION.name().equals(lPlan.getPlanStatus()) && actSystemLoadList.size() == lSystemList.size()) {
	    isUATCompleted = false;
	    pTrackStatusForm.getMessages().add("Lead " + lPlan.getLeadName() + " has to mark the implementation plan " + lPlan.getId() + " as Passed Acceptance Testing");
	}
	return isUATCompleted;

    }

    private boolean tsdValidation(TrackStatusForm pTrackStatusForm, ImpPlan lPlan) {
	boolean isTSDCompleted = true;
	if (Constants.PlanStatus.getReadyForProductionandAboveMap().containsKey(lPlan.getPlanStatus())) {
	    List<ProductionLoads> prodLoads = getProductionLoadsDAO().findByPlanId(lPlan).stream().filter(prod -> prod.getActive().equals("Y")).collect(Collectors.toList());
	    Set<SystemLoad> systemList = lPlan.getSystemLoadList().stream().filter(sysLoad -> sysLoad.getActive().equals("Y")).collect(Collectors.toSet());
	    if (prodLoads == null || prodLoads.isEmpty()) {
		isTSDCompleted = false;
		pTrackStatusForm.getMessages().add("TSD has to load and activate the loadset(s) of implementation plan " + lPlan.getId() + " in Production");
	    } else {
		Set<String> sysNames = systemList.stream().filter(sys -> sys.getActive().equals("Y")).map(sys -> sys.getSystemId().getName()).collect(Collectors.toSet());
		Collection planToLoad = CollectionUtils.subtract(sysNames, prodLoads.stream().filter(prod -> prod.getActive().equals("Y") && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS")).map(prod -> prod.getSystemId().getName()).collect(Collectors.toSet()));
		if (planToLoad != null && !planToLoad.isEmpty()) {
		    isTSDCompleted = false;
		    pTrackStatusForm.getMessages().add("TSD has to load the loadset(s) of implementation plan " + lPlan.getId() + " for the target system(s) " + planToLoad.stream().collect(Collectors.joining(",")) + " in Production");
		}
		List<ProductionLoads> plansToActivate = prodLoads.stream().filter(prod -> prod.getActive().equals("Y") && (Constants.LOAD_SET_STATUS.LOADED.name().equals(prod.getStatus()) && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS")) || (Constants.LOAD_SET_STATUS.ACTIVATED.name().equals(prod.getStatus()) && prod.getLastActionStatus().equalsIgnoreCase("FAILED"))).collect(Collectors.toList());
		if (plansToActivate != null && !plansToActivate.isEmpty()) {
		    Set<String> systems = plansToActivate.stream().distinct().map(prod -> prod.getSystemId().getName()).collect(Collectors.toSet());
		    isTSDCompleted = false;
		    pTrackStatusForm.getMessages().add("TSD has to Activate the loadset(s) of implementation plan " + lPlan.getId() + " for the target system(s) " + String.join(",", systems) + " in Production");
		}

		List<ProductionLoads> plansToAccept = prodLoads.stream().filter(prod -> prod.getActive().equals("Y") && Constants.LOAD_SET_STATUS.ACTIVATED.name().equals(prod.getStatus()) && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS")).collect(Collectors.toList());
		if (plansToAccept != null && !plansToAccept.isEmpty()) {
		    Set<String> systems = plansToAccept.stream().distinct().map(prod -> prod.getSystemId().getName()).collect(Collectors.toSet());
		    isTSDCompleted = false;
		    pTrackStatusForm.getMessages().add("TSD has to Accept the loadset(s) of implementation plan " + lPlan.getId() + " for the target system(s) " + String.join(",", systems) + " in Production");
		}

		List<ProductionLoads> deactivatedPlans = prodLoads.stream().filter(prod -> prod.getActive().equals("Y") && Constants.LOAD_SET_STATUS.DEACTIVATED.name().equals(prod.getStatus()) && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS")).collect(Collectors.toList());
		if (deactivatedPlans != null && !deactivatedPlans.isEmpty()) {
		    Set<String> systems = deactivatedPlans.stream().distinct().map(prod -> prod.getSystemId().getName()).collect(Collectors.toSet());
		    isTSDCompleted = false;
		    pTrackStatusForm.getMessages().add("TSD has to either delete or re-activate the loadset(s) of implementation plan " + lPlan.getId() + " for the target system(s) " + String.join(",", systems) + " in Production");
		}

		List<ProductionLoads> fallbackLoaded = prodLoads.stream().filter(prod -> prod.getActive().equals("Y") && Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name().equals(prod.getStatus()) && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS")).collect(Collectors.toList());
		if (fallbackLoaded != null && !fallbackLoaded.isEmpty()) {
		    Set<String> systems = fallbackLoaded.stream().distinct().map(prod -> prod.getSystemId().getName()).collect(Collectors.toSet());
		    isTSDCompleted = false;
		    pTrackStatusForm.getMessages().add("TSD has to Activate the Fallback loadset(s) of implementation plan " + lPlan.getId() + " for the target system(s) " + String.join(",", systems) + " in Production");
		}

		List<ProductionLoads> fallbackActivated = prodLoads.stream().filter(prod -> prod.getActive().equals("Y") && Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name().equals(prod.getStatus()) && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS")).collect(Collectors.toList());
		if (fallbackActivated != null && !fallbackActivated.isEmpty()) {
		    Set<String> systems = fallbackActivated.stream().distinct().map(prod -> prod.getSystemId().getName()).collect(Collectors.toSet());
		    isTSDCompleted = false;
		    pTrackStatusForm.getMessages().add("TSD has to Accpet the Fallback loadset(s) of implementation plan " + lPlan.getId() + " for the target system(s) " + String.join(",", systems) + " in Production");
		}

		List<ProductionLoads> fallbackDeActivated = prodLoads.stream().filter(prod -> prod.getActive().equals("Y") && Constants.LOAD_SET_STATUS.FALLBACK_DEACTIVATED.name().equals(prod.getStatus()) && prod.getLastActionStatus().equalsIgnoreCase("SUCCESS")).collect(Collectors.toList());
		if (fallbackDeActivated != null && !fallbackDeActivated.isEmpty()) {
		    isTSDCompleted = false;
		    Set<String> systems = fallbackDeActivated.stream().distinct().map(prod -> prod.getSystemId().getName()).collect(Collectors.toSet());
		    pTrackStatusForm.getMessages().add("TSD has to either delete or re-activate the Fallback loadset(s) of implementation plan " + lPlan.getId() + " for the target system(s) " + String.join(",", systems) + " in Production");
		}
	    }
	}
	return isTSDCompleted;

    }

    private void setInProgMessage(TrackStatusForm pTrackStatusForm, ImpPlan lPlan) {
	StringBuilder sb = new StringBuilder();
	if (lPlan.getImplementationList() == null || lPlan.getImplementationList().isEmpty() || !lPlan.getImplementationList().stream().filter(imp -> imp.getActive().equals("Y")).findAny().isPresent()) {
	    sb.append("Lead ").append(lPlan.getLeadName());
	    sb.append(" have to create new Implementation");
	} else {
	    sb.append("Not all Implementations are marked Ready for QA");
	}

	pTrackStatusForm.getMessages().add(sb.toString());
    }

    private void setDevBuildOrLoadMessage(TrackStatusForm pTrackStatusForm, ImpPlan lPlan, boolean isDevbuild) {
	StringBuilder sb = new StringBuilder();
	String devNames = lPlan.getImplementationList().stream().map(imp -> imp.getDevName()).collect(Collectors.joining(","));
	sb.append("Developer(s) ").append(devNames);
	sb.append(" or Lead ").append(lPlan.getLeadName());
	if (isDevbuild) {
	    sb.append(" have to perform a DEVL build for implementation plan ");
	} else {
	    sb.append(" have to generate a DEVL loadset for implementation plan ");
	}
	sb.append(lPlan.getId());
	pTrackStatusForm.getMessages().add(sb.toString());
    }

    private void setSubmitMessage(TrackStatusForm pTrackStatusForm, ImpPlan lPlan) {
	StringBuilder sb = new StringBuilder();
	if (Constants.PlanStatus.SUBMITTED.name().equals(lPlan.getPlanStatus())) {
	    sb.append("Submit In progress for the Implementation plan ");
	    sb.append(lPlan.getId()).append(". Please wait for sometime");
	} else {
	    sb.append("Lead ").append(lPlan.getLeadName());
	    sb.append(" has to submit the implementation plan ");
	    sb.append(lPlan.getId());
	}

	pTrackStatusForm.getMessages().add(sb.toString());
    }

    private void setQaBypassStatus(PlanTrackerData planTrackerData, ImpPlan lPlan) {
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(lPlan.getId());
	planTrackerData.getStages().forEach(stg -> {
	    if (stg.getStatus().equals("QA Functional Testing")) {
		stg.setByPassStatus(lSystemLoads.stream().filter(sysLoad -> sysLoad.getActive().equals("Y") && (Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name().equals(sysLoad.getQaBypassStatus()) || Constants.PLAN_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name().equals(sysLoad.getQaBypassStatus()))).findAny().isPresent());
	    } else if (stg.getStatus().equals("QA Regression Testing")) {
		stg.setByPassStatus(lSystemLoads.stream().filter(sysLoad -> sysLoad.getActive().equals("Y") && (Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name().equals(sysLoad.getQaBypassStatus()) || Constants.PLAN_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name().equals(sysLoad.getQaBypassStatus()))).findAny().isPresent());
	    }
	});
    }
}

package com.tsi.workflow.service;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.ui.AdvancedMetaSearchResult;
import com.tsi.workflow.beans.ui.RFCDetailsDTO;
import com.tsi.workflow.beans.ui.RFCInboxForm;
import com.tsi.workflow.beans.ui.RFCReportSearchForm;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanApprovalsDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.dao.RfcConfigValuesDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.helper.RFCHelper;
import com.tsi.workflow.mail.RfcCalendarInviteMail;
import com.tsi.workflow.mail.RfcDevTeamNotificationMail;
import com.tsi.workflow.report.RFCReportCreator;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RFCChangeMgmtService {

    private static final Logger LOG = Logger.getLogger(RFCChangeMgmtService.class.getName());

    @Autowired
    ImpPlanApprovalsDAO impPlanApprovalsDAO;
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    RFCDetailsDAO rfcDetailsDAO;
    @Autowired
    RFCHelper rfcHelper;
    @Autowired
    RfcConfigValuesDAO lRfcConfigValuesDAO;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    WFConfig wfConfig;

    public RfcConfigValuesDAO getlRfcConfigValuesDAO() {
	return lRfcConfigValuesDAO;
    }

    public void setlRfcConfigValuesDAO(RfcConfigValuesDAO lRfcConfigValuesDAO) {
	this.lRfcConfigValuesDAO = lRfcConfigValuesDAO;
    }

    public RFCDetailsDAO getRfcDetailsDAO() {
	return rfcDetailsDAO;
    }

    public CommonHelper getCommonHelper() {
	return commonHelper;
    }

    public RFCHelper getRFCHelper() {
	return rfcHelper;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    @Transactional
    public JSONResponse getRFCDetails(String planIds, String systemName) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<RFCDetails> rfcDetails = getRfcDetailsDAO().findByPlanIdAndSystemName(planIds, systemName);
	    List<RFCDetailsDTO> rfcDetailsDTOs = new ArrayList<RFCDetailsDTO>();
	    rfcDetails.forEach(rfc -> {
		rfcDetailsDTOs.add(new RFCDetailsDTO(rfc));
	    });
	    getCommonHelper().validateRFCUpdateAllowed(rfcDetailsDTOs);
	    lResponse.setStatus(true);
	    lResponse.setData(rfcDetailsDTOs);
	} catch (Exception ex) {
	    LOG.error("Error in getRFCDetails : ", ex);
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in retriving RFC Details.");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getRFCInboxPlansIds(Integer limit, Integer offset, LinkedHashMap pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<String> rfcInboxIds = getRfcDetailsDAO().getRFCInboxPlanIds(offset, limit, pOrderBy);
	    lResponse.setStatus(true);
	    lResponse.setData(rfcInboxIds);
	} catch (Exception ex) {
	    LOG.error("Error in getRFCDetails : ", ex);
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in retriving RFC Details.");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getRFCInboxPlansDetails(Boolean isApprovedPlans, Integer limit, Integer offset, LinkedHashMap<String, String> pOrderBy, String pPlanId, boolean isReportGeneration, List<String> systems, Date pStartDate, Date pEndDate, String rfcNumber) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<AdvancedMetaSearchResult> rfcInboxIds = getRfcDetailsDAO().getRFCInboxPlansDetails(offset, limit, pOrderBy, isApprovedPlans, pPlanId, isReportGeneration, systems, pStartDate, pEndDate, rfcNumber);
	    lResponse.setStatus(true);
	    if (rfcInboxIds != null && !rfcInboxIds.isEmpty()) {
		List<RFCInboxForm> rfcForms = getRFCHelper().getRFCFormList(rfcInboxIds);
		int start = limit * offset;
		Map<String, List<RFCInboxForm>> planIdAndRfcInboxPlan = new LinkedHashMap<>();
		List<RFCInboxForm> sortedRfcForms = new ArrayList<>();

		if (pOrderBy == null || pOrderBy.isEmpty()) {
		    sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId).thenComparing(RFCInboxForm::getTargetSystem)).collect(Collectors.toList());
		} else if (pOrderBy != null && !pOrderBy.isEmpty()) {
		    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
			String key = entrySet.getKey();
			String value = entrySet.getValue();
			if ("planid".equalsIgnoreCase(key)) {
			    if (value.equalsIgnoreCase("asc")) {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getPlanId)).collect(Collectors.toList());
			    } else {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getPlanId).reversed()).collect(Collectors.toList());
			    }
			} else if ("rfcnumber".equalsIgnoreCase(key)) {
			    if (value.equalsIgnoreCase("asc")) {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getRfcNumber).thenComparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId).thenComparing(RFCInboxForm::getTargetSystem)).collect(Collectors.toList());
			    } else {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getRfcNumber).reversed().thenComparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId).thenComparing(RFCInboxForm::getTargetSystem)).collect(Collectors.toList());
			    }
			} else if ("targetsystem".equalsIgnoreCase(key)) {
			    if (value.equalsIgnoreCase("asc")) {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getTargetSystem).thenComparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId)).collect(Collectors.toList());
			    } else {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getTargetSystem).reversed().thenComparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId)).collect(Collectors.toList());
			    }
			} else if ("loaddatetime".equalsIgnoreCase(key)) {
			    if (value.equalsIgnoreCase("asc")) {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId)).collect(Collectors.toList());
			    } else {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getMaxLoadDateTime).reversed().thenComparing(RFCInboxForm::getPlanId)).collect(Collectors.toList());
			    }
			} else if ("rfcstatus".equalsIgnoreCase(key)) {
			    sortedRfcForms = rfcForms;
			    if (value.equalsIgnoreCase("asc")) {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getRfcSchedularFlag, Comparator.nullsLast(Boolean::compareTo)).thenComparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId)).collect(Collectors.toList());
			    } else {
				sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCInboxForm::getRfcSchedularFlag, Comparator.nullsFirst(Boolean::compareTo)).reversed().thenComparing(RFCInboxForm::getMinLoadDateTime).thenComparing(RFCInboxForm::getPlanId)).collect(Collectors.toList());
			    }
			}
		    }
		}

		sortedRfcForms.stream().skip(start).limit(limit).forEach(rfc -> {
		    String planId = rfc.getPlanId();
		    if (!planIdAndRfcInboxPlan.containsKey(planId)) {
			planIdAndRfcInboxPlan.put(planId, new ArrayList<RFCInboxForm>());
		    }
		    planIdAndRfcInboxPlan.get(planId).add(rfc);
		});

		int count = 0;
		Map<String, List<RFCInboxForm>> finalPlanIdAndRfcInboxPlan = new LinkedHashMap<>();
		for (Map.Entry<String, List<RFCInboxForm>> entry : planIdAndRfcInboxPlan.entrySet()) {
		    count++;
		    finalPlanIdAndRfcInboxPlan.put(String.format("%010d", count) + "_" + entry.getKey(), entry.getValue());
		}
		lResponse.setData(finalPlanIdAndRfcInboxPlan);
		lResponse.setCount(rfcForms.size());
	    }
	} catch (Exception ex) {
	    LOG.error("Error in getRFCInboxPlansDetails : ", ex);
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in getRFCInboxPlansDetails.");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse saveOrUpdateRFCDetail(User user, RFCDetails pRFCDetails) {
	JSONResponse lResponse = new JSONResponse();
	CommonActivityMessage lMessage = new CommonActivityMessage(pRFCDetails.getPlanId(), null);
	try {
	    String systemName = pRFCDetails.getSystemLoadId().getSystemId().getName();

	    StringBuilder sb = new StringBuilder();
	    // Mail to notify
	    RfcCalendarInviteMail rfcCalendarInviteMail = (RfcCalendarInviteMail) getMailMessageFactory().getTemplate(RfcCalendarInviteMail.class);
	    rfcCalendarInviteMail.setStartDate(pRFCDetails.getSystemLoadId().getLoadDateTime());
	    // rfcCalendarInviteMail.setDurationMins(30);
	    rfcCalendarInviteMail.setCalendarFileName(wfConfig.getAttachmentDirectory() + "invites/" + pRFCDetails.getPlanId().getId() + "_" + systemName + "_RFC.ics");
	    rfcCalendarInviteMail.setRfcNumber(pRFCDetails.getRfcNumber());
	    rfcCalendarInviteMail.setTargetSystem(systemName);
	    rfcCalendarInviteMail.setLoadDateTime(pRFCDetails.getSystemLoadId().getLoadDateTime());
	    rfcCalendarInviteMail.setImpPlan(pRFCDetails.getPlanId());
	    rfcCalendarInviteMail.addToDlCoreChangeTeamMail();
	    // To get Developer details
	    Set<String> developerNames = new HashSet<>();
	    for (Implementation lImp : getImplementationDAO().findByImpPlan(pRFCDetails.getPlanId().getId())) {
		developerNames.add(lImp.getDevName());
	    }
	    if (!developerNames.isEmpty()) {
		rfcCalendarInviteMail.setDeveloperName(developerNames.stream().collect(Collectors.joining(",")));
	    }

	    // Mail to notify DevTeam for RFC Number Add/Update action
	    RfcDevTeamNotificationMail rfcDevTeamNotificationMail = (RfcDevTeamNotificationMail) getMailMessageFactory().getTemplate(RfcDevTeamNotificationMail.class);
	    rfcDevTeamNotificationMail.setRfcNumber(pRFCDetails.getRfcNumber());
	    rfcDevTeamNotificationMail.setTargetSystem(systemName);
	    rfcDevTeamNotificationMail.setLoadDateTime(pRFCDetails.getSystemLoadId().getLoadDateTime());
	    rfcDevTeamNotificationMail.setImpPlan(pRFCDetails.getPlanId());
	    rfcDevTeamNotificationMail.setDevFlagMailEnable(Boolean.TRUE);
	    rfcDevTeamNotificationMail.addToAddressUserId(pRFCDetails.getPlanId().getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
	    rfcDevTeamNotificationMail.addToAddressUserId(pRFCDetails.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
	    if (!developerNames.isEmpty()) {
		rfcDevTeamNotificationMail.setDeveloperName(developerNames.stream().collect(Collectors.joining(",")));
	    }

	    boolean updateOtherSystemsRFCDetails = false;

	    if (pRFCDetails.getId() != null && pRFCDetails.getId() > 0) {
		RFCDetails lRFCDetails = getRfcDetailsDAO().findByImpPlanAndSysLoad(pRFCDetails.getPlanId().getId(), pRFCDetails.getSystemLoadId().getId());
		if (lRFCDetails != null) {
		    boolean sendCalendarInvite = true;
		    boolean updateRFCData = false;
		    boolean devTeamMailUpdate = false;
		    if (lRFCDetails.getRfcNumber() == null) {
			sendCalendarInvite = pRFCDetails.getRfcNumber() != null ? true : false;
			devTeamMailUpdate = sendCalendarInvite;
			updateRFCData = true;
		    } else if (lRFCDetails.getRfcNumber() != null && lRFCDetails.getRfcNumber().equalsIgnoreCase(pRFCDetails.getRfcNumber())) {
			updateRFCData = true;
			sendCalendarInvite = false;
		    } else if (lRFCDetails.getRfcNumber() != null && user.getCurrentRole().equals(Constants.UserGroup.DLCoreChangeTeam.name()) && !lRFCDetails.getRfcNumber().equalsIgnoreCase(pRFCDetails.getRfcNumber())) {
			updateRFCData = true;
			devTeamMailUpdate = true;
		    }
		    if (updateRFCData) {

			/**
			 * ZTPFM-2638 As a Delta Change team, I need an email notification when the Lead
			 * selects YES to 'Ready for RFC' question, so it prompts them to take immediate
			 * action
			 */
			if (lRFCDetails.getReadyToSchedule() != null && lRFCDetails.getReadyToSchedule().equals(Boolean.FALSE) && pRFCDetails.getReadyToSchedule()) {
			    mailSendForRFCSchedule(pRFCDetails, developerNames);
			}

			getRfcDetailsDAO().update(user, pRFCDetails);

			if (devTeamMailUpdate) {
			    getMailMessageFactory().push(rfcDevTeamNotificationMail);
			}

			updateOtherSystemsRFCDetails = true;

			if (sendCalendarInvite) {
			    getMailMessageFactory().push(rfcCalendarInviteMail);
			}
			sb.append("RFC fields updated for the System " + pRFCDetails.getSystemLoadId().getSystemId().getName() + " in the Implementation Plan " + pRFCDetails.getPlanId().getId());
		    } else {
			LOG.error("User try to update RFC Number without proper access.");
			throw new WorkflowException("Unable to Save or Update RFC fields");
		    }
		}
	    } else {
		getRfcDetailsDAO().save(user, pRFCDetails);

		if (pRFCDetails.getReadyToSchedule()) {
		    mailSendForRFCSchedule(pRFCDetails, developerNames);
		}

		updateOtherSystemsRFCDetails = true;
		if (pRFCDetails.getRfcNumber() != null && !pRFCDetails.getRfcNumber().isEmpty()) {
		    getMailMessageFactory().push(rfcCalendarInviteMail);
		    getMailMessageFactory().push(rfcDevTeamNotificationMail);
		}
		sb.append("RFC fields added for the System " + pRFCDetails.getSystemLoadId().getSystemId().getName() + " in the Implementation Plan " + pRFCDetails.getPlanId().getId());
	    }

	    if (updateOtherSystemsRFCDetails) {
		getRFCHelper().updateSystemwiseRFCDetails(user, pRFCDetails);
	    }
	    lMessage.setStatus("SUCCESS");
	    lMessage.setMessage(sb.toString());
	    lResponse.setStatus(true);
	} catch (Exception ex) {
	    LOG.error("Error in saveOrUpdateRFCDetail : ", ex);
	    lMessage.setStatus("FAIL");
	    lMessage.setMessage("Save/Update RFC fields failed for the implementation plan " + pRFCDetails.getPlanId().getId());
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in saveOrUpdateRFCDetail.");
	}
	getActivityLogDAO().save(user, lMessage);
	return lResponse;
    }

    private void mailSendForRFCSchedule(RFCDetails pRFCDetails, Set<String> developerNames) {
	RfcDevTeamNotificationMail rfcTeamNotificationMail = (RfcDevTeamNotificationMail) getMailMessageFactory().getTemplate(RfcDevTeamNotificationMail.class);
	rfcTeamNotificationMail.setLoadDateTime(pRFCDetails.getSystemLoadId().getLoadDateTime());
	rfcTeamNotificationMail.setImpPlan(pRFCDetails.getPlanId());
	rfcTeamNotificationMail.setTargetSystem(pRFCDetails.getSystemLoadId().getSystemId().getName());
	rfcTeamNotificationMail.setDevFlagMailEnable(Boolean.FALSE);
	if (wfConfig.getProdLoadsCentreMailId() != null) {
	    rfcTeamNotificationMail.addToProdLoadCentre(true);
	}
	if (!developerNames.isEmpty()) {
	    rfcTeamNotificationMail.setDeveloperName(developerNames.stream().collect(Collectors.joining(",")));
	}
	getMailMessageFactory().push(rfcTeamNotificationMail);
    }

    @Transactional
    public JSONResponse getRfcConfigValues() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(true);
	List<String> rfcConfigVal = lRfcConfigValuesDAO.getConfigValues().stream().map(conf -> conf.getConfigName()).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
	lResponse.setData(rfcConfigVal);
	return lResponse;
    }

    @Transactional
    public JSONResponse exportRFCReport(RFCReportSearchForm pRFCReportSearchForm, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    if (pRFCReportSearchForm.getRfcNumber() != null) {
		pOrderBy = new LinkedHashMap<>();
		pOrderBy.put("planid", "asc");
	    }
	    RFCReportCreator rfcReportCreator = new RFCReportCreator();
	    getRFCHelper().getRFCReportForm(rfcReportCreator, pRFCReportSearchForm.getSystems(), pRFCReportSearchForm.getStartDate(), pRFCReportSearchForm.getEndDate(), pRFCReportSearchForm.getRfcNumber(), false, pOrderBy);

	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    rfcReportCreator.getExcelAttachmentCreator().write(lExcelStream);
	    rfcReportCreator.getWorkBook().close();
	    lResponse.setData(lExcelStream.toByteArray());
	    lExcelStream.close();
	    lResponse.setMetaData("application/vnd.ms-excel");
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    lResponse.setErrorMessage("Error in Downloading Report");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;
    }
}

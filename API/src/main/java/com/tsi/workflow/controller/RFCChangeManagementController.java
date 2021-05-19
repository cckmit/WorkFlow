package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.ui.RFCReportSearchForm;
import com.tsi.workflow.service.RFCChangeMgmtService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("rfc")
public class RFCChangeManagementController extends BaseController {

    @Autowired
    RFCChangeMgmtService rfcChangeMgmtService;

    public RFCChangeMgmtService getRFCChangeMgmtService() {
	return rfcChangeMgmtService;
    }

    @RequestMapping(value = "/getRFCDetailsByPlanIds", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRFCDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String planIds, @RequestParam String systemName) throws Exception {
	return getRFCChangeMgmtService().getRFCDetails(planIds, systemName);
    }

    @RequestMapping(value = "/getRFCInboxPlanIds", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRFCInboxPlans(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getRFCChangeMgmtService().getRFCInboxPlansIds(limit, offset, lOrderBy);
    }

    @RequestMapping(value = "/getRFCInboxPlans", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRFCInboxPlansDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "false") Boolean isApprovedPlans, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getRFCChangeMgmtService().getRFCInboxPlansDetails(isApprovedPlans, limit, offset, lOrderBy, filter, false, null, null, null, null);
    }

    @RequestMapping(value = "/updateRFCDetail", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateRFCDetails(HttpServletRequest request, HttpServletResponse response, @RequestBody RFCDetails pRFCDetails) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getRFCChangeMgmtService().saveOrUpdateRFCDetail(lUser, pRFCDetails);
    }

    @RequestMapping(value = "/getRFCConfigValues", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRFCConfigValues(HttpServletRequest request, HttpServletResponse response) throws Exception {
	return getRFCChangeMgmtService().getRfcConfigValues();
    }

    @RequestMapping(value = "/exportRFCReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse exportRFCReport(HttpServletRequest request, HttpServletResponse response, @RequestBody RFCReportSearchForm pRFCReportSearchForm, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getRFCChangeMgmtService().exportRFCReport(pRFCReportSearchForm, lOrderBy);
    }

    @RequestMapping(value = "/getRFCReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getRFCReport(HttpServletRequest request, HttpServletResponse response, @RequestBody RFCReportSearchForm pRFCReportSearchForm, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getRFCChangeMgmtService().getRFCInboxPlansDetails(false, limit, offset, lOrderBy, filter, true, pRFCReportSearchForm.getSystems(), pRFCReportSearchForm.getStartDate(), pRFCReportSearchForm.getEndDate(), pRFCReportSearchForm.getRfcNumber());
    }
}

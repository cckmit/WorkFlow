/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.LoadFreeze;
import com.tsi.workflow.beans.dao.LoadFreezeGrouped;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.ui.LoadCategoriesForm;
import com.tsi.workflow.beans.ui.LoadFreezeForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.service.LoadsControlService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author USER
 */
@Controller
@RequestMapping("/loadsControl")
public class LoadsControlController extends BaseController {

    @Autowired
    private LoadsControlService loadsControlService;
    private static final Logger LOG = Logger.getLogger(LoadsControlController.class.getName());

    public void setLoadsControlService(LoadsControlService loadsControlService) {
	this.loadsControlService = loadsControlService;
    }

    public LoadsControlService getLoadsControlService() {
	return loadsControlService;
    }

    // <editor-fold defaultstate="collapsed" desc="Load Category">
    /**
     * Returns All the load categories including deleted/deactivated
     *
     * @param limit
     * @param offset
     * @param orderBy
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getLoadCategoryList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadCategoryList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return this.getLoadsControlService().getAllLoadCategoryList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/saveLoadCategory", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveLoadCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody LoadCategoriesForm loadCategory) {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().saveLoadCategory(lUser, loadCategory);
    }

    @RequestMapping(value = "/updateLoadCategory", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateLoadCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody LoadCategoriesForm loadCategory) {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().updateLoadCategory(lUser, loadCategory);
    }

    @RequestMapping(value = "/deleteLoadCategory", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deleteLoadCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id) {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().deleteLoadCategory(lUser, id);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Load Freeze">
    @RequestMapping(value = "/saveLoadFreeze", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveLoadFreeze(HttpServletRequest request, HttpServletResponse response, @RequestBody List<LoadFreezeForm> loadFreezeFormList) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().saveLoadFreeze(lUser, loadFreezeFormList);
    }

    @RequestMapping(value = "/updateLoadFreeze", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateLoadFreeze(HttpServletRequest request, HttpServletResponse response, @RequestBody LoadFreeze loadFreeze) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().updateLoadFreeze(lUser, loadFreeze);
    }

    @RequestMapping(value = "/groupUpdateLoadFreeze", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse groupUpdateLoadFreeze(HttpServletRequest request, HttpServletResponse response, @RequestBody List<LoadFreeze> loadFreezeList) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().groupUpdateLoadFreeze(lUser, loadFreezeList);
    }

    @RequestMapping(value = "/deleteLoadFreeze", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deleteLoadFreeze(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().deleteLoadFreeze(lUser, id);
    }

    @RequestMapping(value = "/deleteGroupLoadFreeze", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deleteGroupLoadFreeze(HttpServletRequest request, HttpServletResponse response, @RequestBody LoadFreezeGrouped lLoadFreezegrouped) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().deleteGroupLoadFreeze(lUser, lLoadFreezegrouped);
    }

    @RequestMapping(value = "/getLoadFreezeDateByMonth", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadFreezeDateByMonth(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam Integer year, @RequestParam Integer month) throws Exception {
	return this.getLoadsControlService().getLoadFreezeDateByMonth(id, year, month);
    }

    @RequestMapping(value = "/getLoadWindowDateByLoadCategory", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadWindowDateByLoadCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam Integer year, @RequestParam Integer month) throws Exception {
	return this.getLoadsControlService().getLoadWindowDateByLoadCategory(id, year, month);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Put Level">
    @RequestMapping(value = "/getPutLevelList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPutLevelList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lUser = this.getCurrentUser(request, response);
	return getLoadsControlService().getPutLevelList(lUser, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/savePutLevel", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse savePutLevel(HttpServletRequest request, HttpServletResponse response, @RequestBody PutLevel putLevel) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().updatePutLevel(lUser, putLevel);
    }

    @RequestMapping(value = "/deletePutLevel", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deletePutLevel(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().deletePutLevel(lUser, id);
    }
    // </editor-fold>

    @RequestMapping(value = "/myTasks", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse listImplementations(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().getUserTaskList(lCurrentUser, filter, offset, limit);
    }

    @RequestMapping(value = "/getSystemLoadByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadByPlan(@RequestParam String[] ids) {
	return getLoadsControlService().getSystemLoadByPlan(ids);
    }

    @RequestMapping(value = "/readyForProdDeploy", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse readyForProdDeploy(HttpServletRequest request, HttpServletResponse response, @RequestBody String[] planIds) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planIds[0]);
	return this.getLoadsControlService().readyForProdDeploy(lCurrentUser, planIds);
    }

    @RequestMapping(value = "/loadHistory", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse loadHistory(HttpServletRequest request, HttpServletResponse response) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getLoadsControlService().getLoadHistory(lCurrentUser);
    }

    @RequestMapping(value = "/getProductionLoadsHistory", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionLoads(@RequestParam String[] ids) throws WorkflowException {
	return this.getLoadsControlService().getProductionLoadsHistory(ids);
    }

    @RequestMapping(value = "/getLoadCategoriesByDate", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadCategoriesByDate(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String date) throws WorkflowException {
	User user = getCurrentUser(request, response);
	return this.getLoadsControlService().getLoadCategoriesByDate(user, id, date);
    }

    @RequestMapping(value = "/getLoadWindowByDay", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadWindowByDay(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String date, @RequestParam String day) throws WorkflowException {
	User user = getCurrentUser(request, response);
	return this.getLoadsControlService().getLoadWindowByDay(user, id, date, day);
    }

}

/**
 *
 */
package com.tsi.workflow.controller;

import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.service.ToolAdminService;
import com.tsi.workflow.utils.JSONResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author vinoth
 *
 */
@Controller
@RequestMapping("toolAdmin")
@ResponseBody
public class ToolAdminController extends BaseController {

    @Autowired
    private ToolAdminService toolAdminService;

    public ToolAdminService getToolAdminService() {
	return toolAdminService;
    }

    public void setToolAdminService(ToolAdminService toolAdminService) {
	this.toolAdminService = toolAdminService;
    }

    @ResponseBody
    @RequestMapping(value = "/getRepoList", method = RequestMethod.GET)
    public JSONResponse getRepoList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset) {
	return getToolAdminService().getRepoList(limit, offset);
    }

    @RequestMapping(value = "/updateRepository", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateRepository(HttpServletRequest request, HttpServletResponse response, @RequestBody RepositoryView pReopsitory) {
	User lUser = this.getCurrentUser(request, response);
	return getToolAdminService().setRepositoryOwners(lUser, pReopsitory);

    }

    // Call used for Debugging purpose
    @ResponseBody
    @RequestMapping(value = "/getAllRepoList", method = RequestMethod.GET)
    public JSONResponse getAllRepoList() {
	return getToolAdminService().getAllRepoList();
    }

    // Call used for Debugging purpose
    @ResponseBody
    @RequestMapping(value = "/getRepoWiseUserList", method = RequestMethod.GET)
    public JSONResponse getRepoWiseUserList() {
	return getToolAdminService().getRepoWiseUserList();
    }

    // Call used for Debugging purpose
    @ResponseBody
    @RequestMapping(value = "/getAllRepoUserList", method = RequestMethod.GET)
    public JSONResponse getAllRepoUserList() {
	return getToolAdminService().getAllRepoUserList();
    }

    // Call used for Debugging purpose
    @ResponseBody
    @RequestMapping(value = "/getFilteredRepoList", method = RequestMethod.GET)
    public JSONResponse getFilteredRepoList() {
	return getToolAdminService().getFilteredRepoList();
    }

    @ResponseBody
    @RequestMapping(value = "getRepositoryInfo", method = RequestMethod.GET)
    public JSONResponse getRepositoryInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String repoName, @RequestParam String platform) {
	User lUser = this.getCurrentUser(request, response);
	return getToolAdminService().getRepositoryInfo(lUser, repoName, platform);
    }

    @ResponseBody
    @RequestMapping(value = "createRepo", method = RequestMethod.POST)
    public JSONResponse createRepository(HttpServletRequest request, HttpServletResponse response, @RequestBody RepositoryView repository, @RequestParam String platform) {
	User lUser = this.getCurrentUser(request, response);
	return getToolAdminService().createRepository(lUser, repository, platform);
    }
}

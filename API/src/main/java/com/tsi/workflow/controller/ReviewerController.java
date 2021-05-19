/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.service.ReviewerService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.LinkedHashMap;
import java.util.List;
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
 * The Class ReviewerController.
 *
 * @author vinoth.ponnurangan
 */
@Controller
@RequestMapping("reviewer")
public class ReviewerController extends BaseController {

    /**
     * The reviewer service.
     */
    @Autowired
    ReviewerService reviewerService;

    /**
     * Gets the reviewer service.
     *
     * @return the reviewer service
     */
    public ReviewerService getReviewerService() {
	return reviewerService;
    }

    /**
     * Sets the reviewer service.
     *
     * @param reviewerService
     *            the new reviewer service
     */
    public void setReviewerService(ReviewerService reviewerService) {
	this.reviewerService = reviewerService;
    }

    /**
     * List implementations.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param limit
     *            the limit
     * @param offset
     *            the offset
     * @param orderBy
     *            the order by
     * @return the JSON response
     * @throws Exception
     *             the exception
     */
    @RequestMapping(value = "/myTasks", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse listImplementations(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getReviewerService().getUserTaskList(lCurrentUser, offset, limit, lOrderBy);
    }

    /**
     * Reviewer history.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param limit
     *            the limit
     * @param offset
     *            the offset
     * @param orderBy
     *            the order by
     * @return the JSON response
     * @throws Exception
     *             the exception
     */
    @RequestMapping(value = "/reviewerHistory", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse reviewerHistory(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getReviewerService().getUserTaskListHistory(lCurrentUser, offset, limit, lOrderBy);
    }

    /**
     * Approve review.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param implId
     *            the impl id
     * @return the JSON response
     */
    @RequestMapping(value = "/approveReview", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse approveReview(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId) {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return this.getReviewerService().approveReview(lCurrentUser, implId);
    }

    /**
     * Commit.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param pSearchResult
     *            the search result
     * @param implId
     *            the impl id
     * @return the JSON response
     * @throws Exception
     *             the exception
     */
    @RequestMapping(value = "/reviewSegments", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse commit(HttpServletRequest request, HttpServletResponse response, @RequestBody List<GitSearchResult> pSearchResult, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return this.getReviewerService().reviewSegments(lUser, implId, pSearchResult);
    }

}

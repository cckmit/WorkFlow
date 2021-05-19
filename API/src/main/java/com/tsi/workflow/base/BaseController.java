/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import com.tsi.workflow.User;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.JSONResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author USER
 */
public abstract class BaseController {

    private static final Logger LOG = Logger.getLogger(BaseController.class.getName());

    @Autowired
    CacheClient lCacheClient;

    @ExceptionHandler({ RuntimeException.class, Exception.class })
    @ResponseBody
    public JSONResponse handleException(HttpServletRequest request, Exception ex) {
	LOG.error("Error in Execution : ", ex);
	JSONResponse lJSONResponse = new JSONResponse();
	lJSONResponse.setStatus(Boolean.FALSE);
	lJSONResponse.setErrorMessage(ex.getMessage());
	return lJSONResponse;
    }

    @ExceptionHandler({ WorkflowException.class })
    @ResponseBody
    public JSONResponse handleWorkflowException(HttpServletRequest request, Exception ex) {
	LOG.error("Workflow Action Failed : " + ex.getMessage());
	JSONResponse lJSONResponse = new JSONResponse();
	lJSONResponse.setStatus(Boolean.FALSE);
	lJSONResponse.setErrorMessage(ex.getMessage());
	return lJSONResponse;
    }

    public User getCurrentUser(HttpServletRequest pRequest, HttpServletResponse pResponse) {
	Object lUser = null;
	if (pRequest != null) {
	    // String lSSOAuthorizeCode =
	    // pRequest.getHeader(Constants.SSOHeaders.SM_USER.getKey());
	    String lManualAuthorizeCode = pRequest.getHeader("Authorization");
	    // if (lSSOAuthorizeCode != null) {
	    // lUser = lCacheClient.getLoginMap().get(lSSOAuthorizeCode);
	    // if (lUser != null) {
	    // return (User) lUser;
	    // }
	    // }
	    if (lManualAuthorizeCode != null) {
		lUser = lCacheClient.getLoginMap().get(lManualAuthorizeCode);
		if (lUser != null) {
		    return (User) lUser;
		}
	    }
	}
	return null;
    }

    public void updateCurrentUser(HttpServletRequest pRequest, HttpServletResponse pResponse, User pUser) {
	if (pRequest != null) {
	    // String lSSOAuthorizeCode =
	    // pRequest.getHeader(Constants.SSOHeaders.SM_USER.getKey());
	    String lManualAuthorizeCode = pRequest.getHeader("Authorization");
	    // if (lSSOAuthorizeCode != null) {
	    // lCacheClient.getLoginMap().put(lSSOAuthorizeCode, pUser);
	    // }
	    if (lManualAuthorizeCode != null) {
		lCacheClient.getLoginMap().put(lManualAuthorizeCode, pUser);
	    }
	}
    }

    public User removeCurrentUser(HttpServletRequest pRequest, HttpServletResponse pResponse) {
	Object lUser = null;
	if (pRequest != null) {
	    // String lSSOAuthorizeCode =
	    // pRequest.getHeader(Constants.SSOHeaders.SM_USER.getKey());
	    String lManualAuthorizeCode = pRequest.getHeader("Authorization");
	    // if (lSSOAuthorizeCode != null) {
	    // lUser = lCacheClient.getLoginMap().remove(lSSOAuthorizeCode);
	    // if (lUser != null) {
	    // return (User) lUser;
	    // }
	    // }
	    if (lManualAuthorizeCode != null) {
		lUser = lCacheClient.getLoginMap().remove(lManualAuthorizeCode);
		if (lUser != null) {
		    return (User) lUser;
		}
	    }
	}
	throw new WorkflowException("User Not Found");
    }
}

package com.tsi.workflow.security;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.audit.models.RequestQueueModel;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.service.LDAPService;
import com.tsi.workflow.utils.WFLOGGER;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(RequestInterceptor.class.getName());

    @Autowired
    CacheClient lCacheClient;

    @Autowired
    LDAPService lDAPService;

    @Autowired
    WFConfig wfConfig;

    @Autowired
    ConcurrentLinkedQueue<RequestQueueModel> apiRequestQueue;

    @Autowired
    ConcurrentLinkedQueue<RequestQueueModel> apiResponseQueue;

    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	List<String> lExceptionUrls = new ArrayList<>();
	lExceptionUrls.add(request.getContextPath() + "/login");
	lExceptionUrls.add(request.getContextPath() + "/setLogLevel");
	lExceptionUrls.add(request.getContextPath() + "/getUnitTestData");
	lExceptionUrls.add(request.getContextPath() + "/setFallbackDate");
	lExceptionUrls.add(request.getContextPath() + "/workflowStatus");
	lExceptionUrls.add(request.getContextPath() + "/common/getLocalConfigDetails");
	lExceptionUrls.add(request.getContextPath() + "/common/getFileSyncInfo");
	lExceptionUrls.add(request.getContextPath() + "/common/getTAPDetails");
	lExceptionUrls.add(request.getContextPath() + "/common/getTAPDetailsByPlan");
	lExceptionUrls.add(request.getContextPath() + "/common/getFutureSecured");
	lExceptionUrls.add(request.getContextPath() + "/common/getPutDeployDate");
	lExceptionUrls.add(request.getContextPath() + "/common/FindAllRepos");
	lExceptionUrls.add(request.getContextPath() + "/common/publishMessage");
	lExceptionUrls.add(request.getContextPath() + "/common/gitDbUpdate");
	lExceptionUrls.add(request.getContextPath() + "/common/getPutLevelInfo");
	lExceptionUrls.add(request.getContextPath() + "/common/generiAPI");
	lExceptionUrls.add(request.getContextPath() + "/common/setMaintanance");
	lExceptionUrls.add(request.getContextPath() + "/common/getNewFileList");
	lExceptionUrls.add(request.getContextPath() + "/common/getSystemDSL");
	lExceptionUrls.add(request.getContextPath() + "/common/sendGenericMail");
	lExceptionUrls.add(request.getContextPath() + "/common/apiports");
	lExceptionUrls.add(request.getContextPath() + "/common/downloadFile");
	lExceptionUrls.add(request.getContextPath() + "/common/FindAllProdRepos");
	lExceptionUrls.add(request.getContextPath() + "/common/getSystemBasedSegments");
	lExceptionUrls.add(request.getContextPath() + "/common/getSegListwithFunArea");
	lExceptionUrls.add(request.getContextPath() + "/audit/common/getTransaction");
	lExceptionUrls.add(request.getContextPath() + "/audit/common/saveTransaction");
	lExceptionUrls.add(request.getContextPath() + "/audit/common/updateAuditSettings");
	lExceptionUrls.add(request.getContextPath() + "/common/updateBuildQueueInfo");

	String lUri = request.getRequestURI();

	long startTime = System.currentTimeMillis();
	request.setAttribute("startTime", startTime);

	try {
	    if (auditCommonHelper.isAuditNeeded(request)) {
		RequestQueueModel lRequestWrapper = new RequestQueueModel();
		lRequestWrapper.setStartDateTime(startTime);
		lRequestWrapper.setRequestURL(lUri);
		String lManualAuthorizeCode = request.getHeader("Authorization");
		if (lManualAuthorizeCode != null && !lManualAuthorizeCode.isEmpty()) {
		    lRequestWrapper.setAuthorizationCode(lManualAuthorizeCode);
		}
		lRequestWrapper.setQueryString(request.getQueryString());
		lRequestWrapper.setPathinfo(request.getPathInfo());
		lRequestWrapper.setMethod(request.getMethod());
		apiRequestQueue.add(lRequestWrapper);
	    }
	} catch (Exception ex) {
	    LOG.info("Exception occurs on Servlet request for transaction" + request.getPathInfo(), ex);
	}

	if (lExceptionUrls.contains(lUri)) {
	    MDC.put("sessionID", "Anonymous");
	    WFLOGGER.LOG(RequestInterceptor.class, Level.INFO, "--> " + request.getMethod() + " " + request.getRequestURL());
	    return true;
	} else {
	    String lManualAuthorizeCode = request.getHeader("Authorization");

	    if (lManualAuthorizeCode == null) {
		MDC.put("sessionID", "Anonymous");
		LOG.warn("No Authorization Code Not Found");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return false;
	    }

	    if (!lManualAuthorizeCode.isEmpty()) {
		if (lCacheClient.getLoginMap() != null && lCacheClient.getLoginMap().keySet().contains(lManualAuthorizeCode)) {
		    User user = (User) lCacheClient.getLoginMap().get(lManualAuthorizeCode);
		    MDC.put("sessionID", user.getDisplayName());
		    String hashCode = request.getHeader("HashCode");
		    lCacheClient.getLoginSessionMap().put(lManualAuthorizeCode, LocalDateTime.now());
		    if (hashCode != null) {
			if (!lCacheClient.getRequestMonitor().contains(hashCode)) {
			    lCacheClient.getRequestMonitor().add(hashCode);
			} else {
			    LOG.warn("Previous request was in progress " + user.getId() + request.getPathInfo() + " error code :" + HttpServletResponse.SC_EXPECTATION_FAILED);
			    response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			    return false;
			}
		    }
		    WFLOGGER.LOG(RequestInterceptor.class, Level.INFO, "--> " + request.getMethod() + " " + request.getRequestURL());
		    return true;
		} else {
		    MDC.put("sessionID", "Anonymous");
		    LOG.warn("Login Authorization Code doesn't Exist in Current Session");
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return false;
		}
	    } else {
		MDC.put("sessionID", "Anonymous");
		LOG.warn("Authorization Code is Null or Empty");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return false;
	    }
	}
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	// LOG.info("<-- " + request.getMethod() + " : " + request.getRequestURL() + " :
	// " + response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	long startTime = (Long) request.getAttribute("startTime");
	long endTime = System.currentTimeMillis();
	long executeTime = endTime - startTime;
	WFLOGGER.LOG(RequestInterceptor.class, Level.INFO, "<-- " + response.getStatus() + " " + request.getRequestURL() + " (" + executeTime + "ms, )");

	try {
	    if (auditCommonHelper.isAuditNeeded(request)) {
		RequestQueueModel lRequestWrapper = new RequestQueueModel();
		lRequestWrapper.setEndDateTime(endTime);
		lRequestWrapper.setStartDateTime(startTime);
		lRequestWrapper.setPathinfo(request.getPathInfo());
		lRequestWrapper.setPlanId((String) request.getAttribute("planId"));
		lRequestWrapper.setImpId((String) request.getAttribute("impId"));

		String lManualAuthorizeCode = request.getHeader("Authorization");
		if (lManualAuthorizeCode != null && !lManualAuthorizeCode.isEmpty()) {
		    lRequestWrapper.setAuthorizationCode(lManualAuthorizeCode);
		    apiResponseQueue.add(lRequestWrapper);

		}
	    }
	} catch (Exception exc) {
	    LOG.info("Expection occurs " + request.getPathInfo(), exc);
	}

    }

}

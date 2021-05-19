package com.tsi.workflow.security;

import com.tsi.workflow.utils.WFLOGGER;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(RequestInterceptor.class.getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	WFLOGGER.LOG(RequestInterceptor.class, Level.INFO, "--> " + request.getMethod() + " " + request.getRequestURL());
	long startTime = System.currentTimeMillis();
	request.setAttribute("startTime", startTime);
	return true;
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
    }

}

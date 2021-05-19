/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author USER
 */
public class SSOFilter implements Filter {

    public void init(FilterConfig fc) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) servletRequest;
	HttpSession session = request.getSession();

	String lSSOURL = getParam(request, Constants.SSOHeaders.SM_UNIVERSALID);
	if (lSSOURL != null && !lSSOURL.isEmpty()) {
	    Constants.isSSOApp = Boolean.TRUE;
	} else {
	    Constants.isSSOApp = Boolean.FALSE;
	}
	chain.doFilter(request, servletResponse);
    }

    public void destroy() {
    }

    private static String getParam(HttpServletRequest request, Constants.SSOHeaders pHeader) {
	return request.getHeader(pHeader.getKey());
    }

}

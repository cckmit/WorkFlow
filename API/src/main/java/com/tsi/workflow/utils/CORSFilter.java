/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author ThangaVigneshRaja
 */

public class CORSFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(CORSFilter.class.getName());
    SortedSet<String> lDevServers = new TreeSet<>();
    SortedSet<String> lQAServers = new TreeSet<>();
    SortedSet<String> lPREPRODServers = new TreeSet<>();
    SortedSet<String> lPRODServers = new TreeSet<>();
    SortedSet<String> lDRServers = new TreeSet<>();
    SortedSet<String> lPubPorts = new TreeSet<>();
    SortedSet<String> lPriPorts = new TreeSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

	lDevServers.add("vhldvztdt001.tvlport.net");
	lDevServers.add("vhldvztdt003.tvlport.net");
	lDevServers.add("vhldvztdt004.tvlport.net");
	lDevServers.add("ztpfdevopstoolchainint.dv.tvlport.com");

	lQAServers.add("vhlqaztdt001.tvlport.net");
	lQAServers.add("vhlqaztdt002.tvlport.net");
	lQAServers.add("ztpfdevopstoolchain.dv.tvlport.com");
	lQAServers.add("federation-qa.travelport.com");

	lPREPRODServers.add("vhlppztdt001.tvlport.net");
	lPREPRODServers.add("vhlppztdt002.tvlport.net");
	lPREPRODServers.add("ztpfdevopstoolchain.pp.tvlport.com");

	lPREPRODServers.add("vhlppztdt003.tvlport.net");
	lPREPRODServers.add("vhlppztdt004.tvlport.net");
	lPREPRODServers.add("ztpfdevopstoolchaindl.pp.tvlport.com");
	lPREPRODServers.add("federation-qa.travelport.com");

	lPRODServers.add("vhlpnztdt002.tvlport.net");
	lPRODServers.add("vhlpnztdt003.tvlport.net");
	lPRODServers.add("ztpfdevopstoolchain.prod.tvlport.com");

	lPRODServers.add("vhlpnztdt004.tvlport.net");
	lPRODServers.add("vhlpnztdt005.tvlport.net");
	lPRODServers.add("ztpfdevopstoolchaindl.prod.tvlport.com");
	lPRODServers.add("federation.travelport.com");

	lDRServers.add("valdrzttd002.tvlport.net");
	lDRServers.add("valdrzttd003.tvlport.net");
	lDRServers.add("ztpfdevopstoolchaindl.prod.tvlport.com");
	lDRServers.add("federation.travelport.com");

	lPubPorts.add("8443");
	lPubPorts.add("9443");
	lPriPorts.add("9446");
	lPriPorts.add("8446");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) servletRequest;
	HttpServletResponse resp = (HttpServletResponse) servletResponse;

	SortedSet<String> allowedOrigins = new TreeSet<>();
	SortedSet<String> lServers = new TreeSet<>();
	String lCorsProfile = System.getProperty("wf.cors.env");

	if (lCorsProfile.equals("DEV")) {
	    lServers = lDevServers;
	} else if (lCorsProfile.equals("QA")) {
	    lServers = lQAServers;
	} else if (lCorsProfile.equals("PP")) {
	    lServers = lPREPRODServers;
	} else if (lCorsProfile.equals("PROD")) {
	    lServers = lPRODServers;
	} else if (lCorsProfile.equals("DR")) {
	    lServers = lDRServers;
	} else if (lCorsProfile.equals("LOCAL")) {
	    allowedOrigins.add("http://localhost:8080");
	    allowedOrigins.add("http://localhost:8081");
	}

	for (String lserver : lServers) {
	    if (lserver.startsWith("vhl") || lserver.startsWith("val")) {
		for (String lPriPort : lPriPorts) {
		    allowedOrigins.add("https://" + lserver + ":" + lPriPort);
		}
	    } else if (lserver.startsWith("ztp")) {
		for (String lPriPort : lPubPorts) {
		    allowedOrigins.add("https://" + lserver + ":" + lPriPort);
		}
	    } else {
		allowedOrigins.add("https://" + lserver);
	    }
	}

	String originHeader = request.getHeader("Origin");
	// Just ACCEPT and REPLY OK if OPTIONS
	if (originHeader != null && !allowedOrigins.contains(originHeader)) {
	    LOG.error("Origin Header Not Matched " + originHeader);
	    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    return;
	}

	// resp.addHeader("Access-Control-Allow-Origin", "*");
	resp.setHeader("Access-Control-Max-Age", "3600");
	resp.addHeader("Access-Control-Allow-Origin", originHeader);
	resp.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	resp.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Authorization , HashCode , " + "Content-Type, Cache-Control, If-Modified-Since, " + "gecos, sm_user, listofroles, mail, sm_universalid");

	if (request.getMethod().equals("OPTIONS")) {
	    resp.setStatus(HttpServletResponse.SC_OK);
	    return;
	}

	chain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
    }
}

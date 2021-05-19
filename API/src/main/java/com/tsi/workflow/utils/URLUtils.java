package com.tsi.workflow.utils;

import javax.servlet.http.HttpServletRequest;

public class URLUtils {

    public static boolean isAjaxRequest(HttpServletRequest pRequest) {
	return "XMLHttpRequest".equals(pRequest.getHeader("X-Requested-With"));
    }

}

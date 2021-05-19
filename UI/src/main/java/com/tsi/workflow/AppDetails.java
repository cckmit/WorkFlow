package com.tsi.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author USER
 */
public class AppDetails {

    public static Map getManifestDetail(HttpServletRequest request) {
	InputStream inputStream = null;
	Map<Object, Object> mApplicationDetail = new HashMap();
	try {
	    ServletContext servletContext = request.getServletContext();
	    inputStream = servletContext.getResourceAsStream("META-INF/MANIFEST.MF");
	    try {
		Manifest mManifestFile = new Manifest(inputStream);
		mApplicationDetail.putAll(mManifestFile.getMainAttributes());
	    } catch (Exception ex) {
	    }
	    if (mApplicationDetail.isEmpty()) {
		mApplicationDetail.put(Constants.APPLICATION_NAME, "zTPF DevOps Toolchain");
		mApplicationDetail.put(Constants.ORGANISATION_NAME, "TPF Software.inc");
		mApplicationDetail.put(Constants.APPLICATION_VERSION, "Local Version");
		mApplicationDetail.put(Constants.APPLICATION_BUILD, "0.0");
		mApplicationDetail.put(Constants.APPLICATION_BUILD_TIME, "2017/01/01 00:00:00");
	    }
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (IOException ex) {
		}
	    }
	}
	return mApplicationDetail;
    }
}

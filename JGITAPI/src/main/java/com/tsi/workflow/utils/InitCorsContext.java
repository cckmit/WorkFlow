/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author prabhu.prabhakaran
 */
public class InitCorsContext implements ServletContextListener {

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
    }

    @Override
    public void contextInitialized(final ServletContextEvent event) {
	String lFileDirPath = System.getenv("MTP_ENV");
	File lConfigFile = new File(lFileDirPath, "app.properties");
	System.out.println("Loading Config : " + lConfigFile.getAbsolutePath());
	final Properties propsFromFile = new Properties();
	try {
	    propsFromFile.load(new FileReader(lConfigFile));
	} catch (final IOException e) {
	    // can't get resource
	}
	String property = propsFromFile.getProperty("wf.cors.env");
	System.setProperty("wf.cors.env", property);
	String gitSearchType = propsFromFile.getProperty("wf.git.search.type");
	System.out.println("GIT Search Type Profile : " + gitSearchType);
	System.setProperty("wf.git.search.type", gitSearchType);
    }
}

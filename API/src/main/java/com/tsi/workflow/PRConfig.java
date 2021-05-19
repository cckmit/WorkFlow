/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.IPRConfig;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author USER
 */
public class PRConfig implements IPRConfig {

    @Value("${tos.file.username}")
    String prUser;
    @Value("${tos.file.password}")
    String prPassword;
    @Value("${tos.file.domain}")
    String prDomain;
    @Value("${wf.pr.file.path}")
    String prDirPath;

    @Override
    public String getPRUserDomain() {
	return prDomain;
    }

    @Override
    public String getPRUser() {
	return prUser;
    }

    @Override
    public String getPRPassword() {
	return prPassword;
    }

    @Override
    public String getPrFilePath() {
	return prDirPath;
    }

}

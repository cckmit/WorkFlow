/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.workflow.tos.TOSIPSync;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class TOSIPResolver {

    @Autowired
    TOSConfig config;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    SystemCpuDAO systemCpuDAO;
    @Autowired
    WFConfig wFConfig;

    @Transactional
    @Scheduled(cron = "0 0/10 * * * *")

    public void refreshIps() {
	if (wFConfig.getPrimary()) {
	    TOSIPSync tOSIPSync = new TOSIPSync(config);
	    List<String> lLines = tOSIPSync.getData();
	    if (!lLines.isEmpty()) {
		List<SystemCpu> lAllCpus = systemCpuDAO.findAll();
		Map<String, List<SystemCpu>> lAllCpuMap = lAllCpus.stream().collect(Collectors.groupingBy(t -> t.getCpuName()));
		for (String lCPUString : lLines) {
		    if (lCPUString == null || lCPUString.isEmpty()) {
			continue;
		    }
		    String[] split = lCPUString.split("\\s+");
		    List<SystemCpu> lCpus = lAllCpuMap.get(split[0]);
		    if (lCpus != null && !lCpus.isEmpty()) {
			for (SystemCpu lCpu : lCpus) {
			    lCpu.setPrimaryIpAddress(split[1] + ":" + split[2]);
			    lCpu.setSecondaryIpAddress(split[4] + ":" + split[5]);
			    lCpu.setCpuCode(split[6]);
			    // lCpu.setDisplayName(lCpu.getSystemId().getName() + "-" + split[6]);
			    User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
			    systemCpuDAO.update(serviceUser, lCpu);
			}
		    }
		}
	    }
	}
    }
}

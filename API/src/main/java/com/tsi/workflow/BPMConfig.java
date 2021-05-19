package com.tsi.workflow;

import com.tsi.workflow.interfaces.IBPMConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deepa
 */
@Component
public class BPMConfig implements IBPMConfig {

    @Value("${bpm.rest.api.url}")
    private String bpmRestUrl;
    @Value("${bpm.plan.process.key}")
    private String adlProcessKey;
    @Value("${bpm.impl.process.key}")
    private String dlProcessKey;

    public String getBpmRestUrl() {
	return bpmRestUrl;
    }

    public void setBpmRestUrl(String bpmRestUrl) {
	this.bpmRestUrl = bpmRestUrl;
    }

    public String getAdlProcessKey() {
	return adlProcessKey;
    }

    public void setAdlProcessKey(String adlProcessKey) {
	this.adlProcessKey = adlProcessKey;
    }

    public String getDlProcessKey() {
	return dlProcessKey;
    }

    public void setDlProcessKey(String dlProcessKey) {
	this.dlProcessKey = dlProcessKey;
    }

}

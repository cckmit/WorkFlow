/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.interfaces;

/**
 *
 * @author Radha.Adhimoolam
 */
public interface ISnowPRConfig extends IWFProxyConfig {

    public String getRestSNOWPRUrl();

    public String getRestSNOWPRUserId();

    public String getRestSNOWPRPasscode();
}

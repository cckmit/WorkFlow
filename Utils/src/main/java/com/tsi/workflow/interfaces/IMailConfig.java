/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.interfaces;

/**
 *
 * @author USER
 */
public interface IMailConfig {

    public String getAzureURL();

    public String getFromMailId();

    public String getAzureKey();

    public String getAzureId();

    public String getSendingApplication();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.tsi.workflow.AuthModel;
import com.tsi.workflow.ExecModel;
import com.tsi.workflow.service.SSHExecService;
import com.tsi.workflow.service.TDBoxAuthService;
import com.tsi.workflow.utils.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author USER
 */
@Controller
@RequestMapping("tdbox")
public class TDBoxAuthenticator {

    @Autowired
    TDBoxAuthService tDBoxAuthService;
    @Autowired
    SSHExecService sSHExecService;

    @ResponseBody
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public JSONResponse authenticate(@RequestBody AuthModel pAuthModel) {
	Boolean checkForKeys = tDBoxAuthService.checkForKeys(pAuthModel.getUser(), pAuthModel.getSystems());
	if (checkForKeys) {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(true);
	    return lResponse;
	} else {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in Key Mapping");
	    return lResponse;
	}
    }

    @ResponseBody
    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public JSONResponse execute(@RequestBody ExecModel pModel) {
	return sSHExecService.executeCommand(pModel);
    }
}

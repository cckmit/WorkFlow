/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.controller;

import com.tsi.workflow.audit.service.GiService;
import com.tsi.workflow.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Radha.Adhimoolam
 */
@Controller
@RequestMapping("/audit/gi")
public class GiController extends BaseController {

    @Autowired
    GiService giService;

    public GiService getGiService() {
	return giService;
    }

}

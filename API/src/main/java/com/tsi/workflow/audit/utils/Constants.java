/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.utils;

import java.text.DecimalFormat;

/**
 *
 * @author Radha.Adhimoolam
 */
public class Constants {
    public static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public enum InfoLevel {
	PLAN,
	IMP;
    }

    public enum AuditConfiguration {
	SystemView,
	TransactionView;
    }
}

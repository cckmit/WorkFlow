/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author User
 */
public class DateHelper {

    public static String convertGMTtoEST(Date GMTTime) {
	DateFormat gmtFormat = new SimpleDateFormat(Constants.APP_READABLE_DATE_TIME_FORMAT_STRING);
	TimeZone estTime = TimeZone.getTimeZone("EST5EDT");
	gmtFormat.setTimeZone(estTime);
	return gmtFormat.format(GMTTime);
    }

    public static String convertGMTtoESTDate(Date GMTTime) {
	DateFormat gmtFormat = new SimpleDateFormat(Constants.APP_READABLE_DATE_FORMAT_STRING);
	TimeZone estTime = TimeZone.getTimeZone("EST5EDT");
	gmtFormat.setTimeZone(estTime);
	return gmtFormat.format(GMTTime);
    }
}

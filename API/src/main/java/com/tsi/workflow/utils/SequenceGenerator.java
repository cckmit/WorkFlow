/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import java.util.Calendar;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class SequenceGenerator {

    @Autowired
    private SessionFactory sessionFactory;

    public String getNewImplementationId(String planId) {
	String lSequenceName = "seq_" + planId;
	if (!isDBObjectExist(lSequenceName)) {
	    createSequence(lSequenceName);
	}
	SQLQuery lQuery = this.sessionFactory.getCurrentSession().createSQLQuery("select nextval('" + lSequenceName + "') AS RESULT");
	return planId + "_" + String.format("%03d", lQuery.uniqueResult());

    }

    public String getNewImplementationPlanId(String pPlatform) {
	String lYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
	String lSequenceName = "seq_" + pPlatform + "_" + lYear;
	if (!isDBObjectExist(lSequenceName)) {
	    createSequence(lSequenceName);
	}
	SQLQuery lQuery = this.sessionFactory.getCurrentSession().createSQLQuery("select nextval('" + lSequenceName + "') AS result");
	return pPlatform + lYear + String.format("%05d", lQuery.uniqueResult());
    }

    private boolean isDBObjectExist(String pTableName) {
	SQLQuery lQuery = this.sessionFactory.getCurrentSession().createSQLQuery("SELECT to_regclass('" + pTableName + "') AS result");
	lQuery.addScalar("result", StringType.INSTANCE);
	Object lResult = lQuery.uniqueResult();
	if (lResult == null) {
	    return false;
	}
	return pTableName.equalsIgnoreCase(lResult.toString());
    }

    private void createSequence(String lSequenceName) {
	System.out.println("CREATE SEQUENCE " + lSequenceName);
	SQLQuery lQuery = this.sessionFactory.getCurrentSession().createSQLQuery("CREATE SEQUENCE " + lSequenceName);
	lQuery.executeUpdate();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.dao;

import com.tsi.workflow.audit.base.AuditBaseDAO;
import com.tsi.workflow.audit.beans.dao.GiTransaction;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Radha.Adhimoolam
 */
@Repository
public class GiTransactionDAO extends AuditBaseDAO<GiTransaction> {

    public List<GiTransaction> getGiTransactionInfo(Date startDate, Date endDate, String planId, List<String> userId, List<String> actions) {
	StringBuilder lQueryStr = new StringBuilder("SELECT gi from GiTransaction gi, ApiActions ap " + " WHERE gi.actionsId.id = ap.id and gi.startedDt >= :StartDate and gi.startedDt <= :EndDate ");

	if (planId != null && !planId.isEmpty()) {
	    lQueryStr.append(" and gi.planId = :PlanId");
	}

	if (userId != null && !userId.isEmpty()) {
	    lQueryStr.append(" and gi.userId in :UserId");
	}
	if (actions != null && !actions.isEmpty()) {
	    lQueryStr.append(" and ap.actionName in :Actions");
	}
	lQueryStr.append(" order by gi.id");

	Query query = getCurrentSession().createQuery(lQueryStr.toString()).setParameter("StartDate", startDate).setParameter("EndDate", endDate);
	if (planId != null && !planId.isEmpty()) {
	    query = query.setParameter("PlanId", planId);
	}
	if (userId != null && !userId.isEmpty()) {
	    query = query.setParameterList("UserId", userId);
	}

	if (actions != null && !actions.isEmpty()) {
	    query = query.setParameterList("Actions", actions);
	}

	return query.list();
    }

}

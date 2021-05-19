/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.dao;

import com.tsi.workflow.audit.base.AuditBaseDAO;
import com.tsi.workflow.audit.beans.dao.ApiActions;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Radha.Adhimoolam
 */
@Repository
public class ApiActionsDAO extends AuditBaseDAO<ApiActions> {

    public ApiActions findByActionUrl(String actionUrl) {
	String lQuery = "SELECT aUrl from ApiActions aUrl where aUrl.actionUrl = :ActionURL";
	return (ApiActions) getCurrentSession().createQuery(lQuery).setParameter("ActionURL", actionUrl).uniqueResult();
    }

    public List<ApiActions> findAllActive() {
	String lQuery = "SELECT aUrl from ApiActions aUrl where aUrl.active = 'Y'";
	return getCurrentSession().createQuery(lQuery).list();
    }

    public List<Integer> findByActionUrl(List<String> actionNames) {
	String lQuery = "SELECT aUrl.id from ApiActions aUrl where aUrl.actionName in :Actions";
	return getCurrentSession().createQuery(lQuery).setParameterList("Actions", actionNames).list();
    }

}

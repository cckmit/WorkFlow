/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.dao;

import com.tsi.workflow.audit.base.AuditBaseDAO;
import com.tsi.workflow.audit.beans.dao.ApiTransaction;
import com.tsi.workflow.audit.uiform.TransactionViewBean;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Radha.Adhimoolam
 */
@Repository
public class ApiTransactionDAO extends AuditBaseDAO<ApiTransaction> {

    private static final Logger LOG = Logger.getLogger(ApiTransactionDAO.class.getName());

    public ApiTransaction find(Long startDt, String userId) {
	String lQuery = "SELECT aTranx FROM ApiTransaction aTranx where aTranx.startDtLong = :StartDtLong and aTranx.userId";
	ApiTransaction lTranx = (ApiTransaction) getCurrentSession().createQuery(lQuery).setParameter("StartDtLong", startDt).setParameter("UserId", userId).uniqueResult();
	return lTranx;
    }

    public List<ApiTransaction> getApiTransactionInfo(String hostProfile, List<String> userId, String planId, Date startDate, Date endDate, List<String> userAction) {

	String lQuery = "SELECT tx from ApiTransaction tx, ApiActions ax, LinuxServers lx " + " where tx.actionsId.id = ax.id" + " and tx.lnxServerId.id = lx.id " + " and ax.active = 'Y'" + " and lx.hostProfile = :HostProfile " + " and tx.startedDt >= :StartDate " + " and tx.startedDt <= :EndDate ";

	if (userId != null && !userId.isEmpty()) {
	    lQuery = lQuery + " and tx.userId in (:UserId) ";
	}

	if (planId != null && !planId.isEmpty()) {
	    lQuery = lQuery + " and tx.planId = :PlanId ";
	}

	if (userAction != null && !userAction.isEmpty()) {
	    userAction.stream().forEach(t -> LOG.info(t));
	    lQuery = lQuery + " and ax.actionName in :UserAction ";
	}
	lQuery = lQuery + " order by tx.startedDt ";

	Query query = getCurrentSession().createQuery(lQuery).setParameter("HostProfile", hostProfile).setParameter("StartDate", startDate).setParameter("EndDate", endDate);
	if (userId != null && !userId.isEmpty()) {
	    query.setParameterList("UserId", userId);
	}

	if (planId != null && !planId.isEmpty()) {
	    query.setParameter("PlanId", planId);
	}

	if (userAction != null && !userAction.isEmpty()) {
	    query.setParameterList("UserAction", userAction);
	}
	LOG.info(query.getQueryString());
	List<ApiTransaction> lTransactions = query.list();
	return lTransactions;

    }

    public List<TransactionViewBean> getTransactionViewInfo(String hostProfile, Date startDate, Date endDate, List<String> userAction) {
	LOG.info("Into Query");
	StringBuffer sb = new StringBuffer();
	sb.append("SELECT ");
	sb.append("  tx.started_dt as  startDate, ");
	sb.append("  tx.end_dt as endDate, ");
	sb.append("  tx.plan_id as planId, ");
	sb.append("  tx.impl_id as implId, ");
	sb.append("  ax.action_name as userAction, ");
	sb.append("  tx.user_role as userRole, ");
	sb.append("  tx.user_id as initiatedBy, ");
	sb.append("  sum(px.asmcount) as asmcount, ");
	sb.append("  sum(px.sbtcount) as sbtcount, ");
	sb.append("  sum(px.makcount) as makcount, ");
	sb.append("  sum(px.ccppcount) as ccppcount, ");
	sb.append("  sum(px.headercount) as headercount, ");
	sb.append("  sum(px.totalcount) as totalcount, ");
	sb.append("  sum(px.socount) as socount, ");
	sb.append("  sum(px.repocount) as repocount, ");
	sb.append("  string_agg(DISTINCT (px.reponamelist),',') as reponamelist, ");
	sb.append("  px.targetsystem as targetsystem, ");
	sb.append("  tx.response_Time as responseTime, ");
	sb.append("  lx.dns_name as hostName, ");
	sb.append("  px.tdx as tDX ");
	sb.append(" FROM audit.api_transaction tx, audit.Api_Actions ax, audit.Linux_Servers lx,audit.plan_details px ");
	sb.append(" where tx.actions_Id = ax.id ");
	sb.append(" and tx.id=px.tranx_id ");
	sb.append(" and tx.lnx_Server_Id = lx.id ");
	sb.append(" and ax.active = 'Y' ");
	sb.append(" and lx.host_Profile = :HostProfile ");
	sb.append(" and ax.Action_name in (:UserAction) ");
	sb.append(" and tx.started_dt >= :StartDate  and tx.end_dt <= :EndDate");
	sb.append(" group by tx.id, ax.action_name,px.planid,px.targetsystem, lx.dns_name,px.tdx");
	List<TransactionViewBean> lReturn = getCurrentSession().createSQLQuery(sb.toString()).setParameter("HostProfile", hostProfile).setParameterList("UserAction", userAction).setParameter("StartDate", startDate).setParameter("EndDate", endDate).setResultTransformer(new AliasToBeanResultTransformer(TransactionViewBean.class)).list();
	return lReturn;
    }

    public ApiTransaction findAsycnTranxInfo(String userId, String actionUrl, String planId) {
	String lQuery = "SELECT aTranx FROM ApiTransaction aTranx, ApiActions actions " + " where aTranx.userId = :UserId " + " and aTranx.actionsId.id = actions.id" + " and actions.actionUrl = :ActionUrl " + " and aTranx.planId = :PlanId " + " order by aTranx.id desc";
	ApiTransaction lTranx = (ApiTransaction) getCurrentSession().createQuery(lQuery).setParameter("UserId", userId).setParameter("ActionUrl", actionUrl).setParameter("PlanId", planId).setMaxResults(1).uniqueResult();
	return lTranx;
    }

}

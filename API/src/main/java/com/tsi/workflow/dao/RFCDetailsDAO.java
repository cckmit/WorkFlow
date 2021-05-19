package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.AdvancedMetaSearchResult;
import com.tsi.workflow.beans.ui.RFCReportForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

@Repository
public class RFCDetailsDAO extends BaseDAO<RFCDetails> {

    private static final Logger LOG = Logger.getLogger(RFCDetailsDAO.class.getName());

    public List<RFCDetails> findByImpPlan(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<RFCDetails> findByImpPlan(List<String> pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public RFCDetails findByImpPlanAndSysLoad(String pId, Integer systemLoadId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("systemLoadId", new SystemLoad(systemLoadId));
	lFilter.put("active", "Y");
	return find(lFilter);
    }

    public List<String> getRFCInboxPlanIds(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {

	String query = "SELECT a.id from imp_plan a, system_load b where b.plan_id = a.id and a.id not in (SELECT PLAN_ID FROM RFC_DETAILS WHERE active='Y' and RFC_NUMBER IS not NULL and rfc_desc IS not NULL and impact_level IS not NULL and config_item IS not NULL) and a.plan_status in(:planStatus) and a.active='Y' and b.active='Y' order by b.load_date_time desc ";

	Query lQuery = getCurrentSession().createSQLQuery(query);
	lQuery.setParameterList("planStatus", Constants.PlanStatus.getAllPlanStatus().keySet());
	lQuery.setFirstResult(pOffset * pLimit);
	lQuery.setMaxResults(pLimit);
	return lQuery.list();
    }

    public List<AdvancedMetaSearchResult> getRFCInboxPlansDetails(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, boolean isApprovedPlans, String planId, Boolean isReportGeneration, List<String> systems, Date startDate, Date endDate, String rfcNumber) {
	StringBuilder query = new StringBuilder();
	query.append("select a.id as planid, a.load_type as loadtype, a.plan_status as planstatus, b.load_date_time as loaddatetime, d.name as loadcategory,");
	query.append(" string_agg( distinct c.dbcr_name, ',' ) as dbcrname, e.name as targetsystem, f.rfc_number as rfcnumber from imp_plan a JOIN system_load b ON b.plan_id = a.id ");
	query.append(" JOIN load_categories d ON d.id = b.load_category_id left join dbcr c on (c.plan_id = a.id AND c.system_id = b.system_id AND ");
	query.append(" C.ACTIVE ='Y') join system e on e.id = b.system_id left join RFC_DETAILS f on (f.plan_id = a.id and f.system_load_id = b.id and f.active='Y')");
	query.append(" where A.ACTIVE ='Y' AND B.ACTIVE ='Y' AND D.ACTIVE ='Y' AND a.id in  (SELECT a.id from imp_plan a where a.rfc_flag='true' ");
	query.append(" and a.plan_status in(:planStatus) and a.rfc_flag='true' and a.active='Y')");

	if (isApprovedPlans && !isReportGeneration) {
	    query.append(" and f.rfc_number is not null");
	} else if (!isReportGeneration) {
	    query.append(" and f.rfc_number is null");
	}

	if (planId != null && !planId.isEmpty()) {
	    query.append(" and  a.id like :planId ");
	}

	if (startDate != null && endDate != null) {
	    query.append(" AND b.load_date_time between :startDate and :endDate");
	}
	if (systems != null && !systems.isEmpty()) {
	    query.append(" AND e.name in (:system)");
	}

	if (rfcNumber != null && !rfcNumber.isEmpty()) {
	    query.append(" AND f.rfc_number ilike :rfcNumber ");
	}

	query.append(" group by d.name , a.id , b.load_date_time , e.name ");

	if (isApprovedPlans && !isReportGeneration) {
	    query.append(" ,f.rfc_number ");
	} else if (!isReportGeneration) {
	    query.append(" ,f.rfc_number ");
	} else if (isReportGeneration) {
	    query.append(" ,f.rfc_number ");
	}

	Query lQuery = getCurrentSession().createSQLQuery(query.toString());
	lQuery.setParameterList("planStatus", Constants.PlanStatus.getAllPlanStatus().keySet());

	if (planId != null && !planId.isEmpty()) {
	    lQuery.setParameter("planId", "%" + planId + "%");
	}

	if (systems != null && !systems.isEmpty()) {
	    lQuery.setParameterList("system", systems);
	}
	if (startDate != null && endDate != null) {
	    lQuery.setParameter("startDate", startDate);
	    lQuery.setParameter("endDate", endDate);
	}
	if (rfcNumber != null && !rfcNumber.isEmpty()) {
	    lQuery.setParameter("rfcNumber", "%" + rfcNumber + "%");
	}

	lQuery.setResultTransformer(new AliasToBeanResultTransformer(AdvancedMetaSearchResult.class));

	return lQuery.list();
    }

    public List<RFCReportForm> getRFCReport(List<String> systems, Date startDate, Date endDate, String rfcNumber) {
	String query = "SELECT a.id AS planid ,a.load_type AS loadtype ,a.plan_status AS planstatus ,b.load_date_time AS loaddatetime ,d.name AS loadcategory ,c.dbcr_name AS dbcrname ,e.name AS targetsystem , " + "a.plan_desc AS impdesc ,f.rfc_number AS rfcNumber ,f.rfc_desc AS breakfix ,f.impact_level AS impactlevel ,f.config_item AS configvalue ,a.lead_name AS leadname , "
		+ "a.dev_manager_name AS managername ,b.load_attendee AS loadattendee ,b.load_attendee_contact AS loadattendeecontact ,f.vs_flag AS vsflag ,f.vs_desc AS vsdesc ,f.vs_area AS vsarea , " + "f.vs_test_flag AS vstestflag ,g.file_name AS approvalFileName ,h.program_name AS programname ,CASE WHEN (impl.pr_tkt_num IS NOT NULL AND impl.pr_tkt_num <>'') THEN "
		+ "CASE	WHEN (a.sdm_tkt_num = impl.pr_tkt_num) THEN a.sdm_tkt_num ELSE ARRAY_TO_STRING(ARRAY_AGG(DISTINCT CONCAT ( a.sdm_tkt_num,' ,',impl.pr_tkt_num	)), ',')END " + "ELSE  a.sdm_tkt_num END  AS incidentnumber FROM imp_plan a JOIN system_load b ON b.plan_id = a.id JOIN load_categories d ON d.id = b.load_category_id " + "LEFT JOIN dbcr c ON ( c.plan_id = a.id AND c.system_id = b.system_id AND C.ACTIVE = 'Y') JOIN system e ON e.id = b.system_id "
		+ "LEFT JOIN rfc_details f ON ( f.system_load_id = b.id AND f.plan_id = a.id AND f.active = 'Y'	)LEFT JOIN imp_plan_approvals g ON ( g.plan_id = a.id AND g.active = 'Y') " + "LEFT JOIN checkout_segments h ON ( h.plan_id = a.id AND h.target_system = e.name AND h.active = 'Y')LEFT JOIN implementation impl ON (impl.plan_id = a.id AND impl.ACTIVE = 'Y' ) "
		+ "WHERE A.ACTIVE = 'Y' AND B.ACTIVE = 'Y'AND D.ACTIVE = 'Y'AND a.id IN (SELECT id FROM imp_plan WHERE rfc_flag = 'true'AND active = 'Y') and a.plan_status in(:planStatus)";

	if (startDate != null && endDate != null) {
	    query = query + " AND b.load_date_time between :startDate and :endDate";
	}
	if (systems != null && !systems.isEmpty()) {
	    query = query + " AND e.name in (:system)";
	}

	if (rfcNumber != null && !rfcNumber.isEmpty()) {
	    query = query + " AND f.rfc_number ilike :rfcNumber ";
	}
	query = query + " group by a.id,b.load_date_time ,d.name,c.dbcr_name ,e.name ,f.rfc_number ,f.rfc_desc ,f.impact_level ,f.config_item ,b.load_attendee ,b.load_attendee_contact ,f.vs_flag ,f.vs_desc ,f.vs_area ,f.vs_test_flag ,g.file_name ,h.program_name ,impl.pr_tkt_num";
	query = query + " order by b.load_date_time,a.plan_status, a.id, e.name";
	Query lQuery = getCurrentSession().createSQLQuery(query);
	lQuery.setParameterList("planStatus", Constants.PlanStatus.getAllPlanStatus().keySet());
	if (systems != null && !systems.isEmpty()) {
	    lQuery.setParameterList("system", systems);
	}
	if (startDate != null && endDate != null) {
	    lQuery.setParameter("startDate", startDate);
	    lQuery.setParameter("endDate", endDate);
	}
	if (rfcNumber != null && !rfcNumber.isEmpty()) {
	    lQuery.setParameter("rfcNumber", "%" + rfcNumber + "%");
	}

	lQuery.setResultTransformer(new AliasToBeanResultTransformer(RFCReportForm.class));
	return lQuery.list();
    }

    public List<RFCDetails> findByPlanIdAndSystemName(String planId, String systemName) {
	String lQuery = "SELECT a FROM RFCDetails a, SystemLoad b, System c WHERE a.active = 'Y' and b.active = 'Y' and a.planId.id = b.planId.id and a.systemLoadId.id = b.id and b.systemId.id = c.id and c.name=:systemName and a.planId.id =:planId";

	List<RFCDetails> rfcDetails = getCurrentSession().createQuery(lQuery).setParameter("planId", planId).setParameter("systemName", systemName).list();
	return rfcDetails;
    }

    public List<Object[]> getRfcFieldsPendingPlanDetails(String startDate, String endDate) {
	String lQuery = "select distinct a.id, min(b.load_date_time), a.lead_id, a.dev_manager from imp_plan a join system_load b on b.plan_id = a.id left join rfc_details c on (c.plan_id= a.id and c.system_load_id = b.id and c.active='Y') where a.plan_status in (:planStatus) and a.active='Y' and a.rfc_flag='true' and (a.rfc_mail_flag='false' or a.rfc_mail_flag is null) and b.active='Y' and (c.rfc_desc is null or c.impact_level is null or c.config_item is null or case when vs_flag='true' then (vs_area is null) else false end ) and b.load_date_time is not null and to_char(b.load_date_time,'yyyyMMddHHmm') between :startDate and :endDate group by a.id, a.lead_id, a.dev_manager";
	return getCurrentSession().createSQLQuery(lQuery).setParameterList("planStatus", Constants.PlanStatus.getPlanStatus().keySet()).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
    }

    public List<RFCDetails> checkRFCSchedularFlag(String planId) {
	String lQuery = " SELECT a from RFCDetails a where  a.active = 'Y' and a.planId.id =:planId and a.readyToSchedule is not null ";
	List<RFCDetails> lReturnRFCPlans = getCurrentSession().createQuery(lQuery).setParameter("planId", planId).list();
	return lReturnRFCPlans;
    }
}

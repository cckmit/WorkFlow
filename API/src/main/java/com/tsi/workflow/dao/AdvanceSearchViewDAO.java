/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.AdvancedMetaSearchResult;
import com.tsi.workflow.beans.ui.AdvancedSearchForm;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

/**
 *
 * @author madhan.gowindhasamy
 */
@Repository
public class AdvanceSearchViewDAO extends BaseDAO<SystemLoad> {
    private static final Logger LOG = Logger.getLogger(AdvanceSearchViewDAO.class.getName());

    public List getPlanByAdvancedSearchView(AdvancedSearchForm searchForm, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	StringBuilder lBuilder = new StringBuilder();
	String loadDate = "";
	lBuilder.append("SELECT bb.programname as programname, bb.planid as planid, bb.plandescription as plandescription, MAX(bb.fallbackdatetime) as fallbackdatetime, bb.planstatus as planstatus, MAX(bb.activateddatetime) as activateddatetime, bb.loaddatetime as loaddatetime, bb.targetsystem as targetsystem, ");
	lBuilder.append("bb.qastatus as qastatus, bb.loadcategory as loadcategory, bb.dbcrname as dbcrname, bb.loadinstruction as loadinstruction, bb.problemticketnum as problemticketnum, bb.loadattendee as loadattendee, bb.csrnumber as csrnumber, bb.projectname as projectname, bb.developername as developername, bb.leadname as leadname, bb.managername as managername, bb.peerreviewer as peerreviewer, bb.functionalarea as functionalarea FROM (");
	lBuilder.append(" SELECT string_agg(DISTINCT lSystemLoads.programname, ', ')  as programname, lSystemLoads.planid as planid, lSystemLoads.plandescription as plandescription, ");
	lBuilder.append(" lSystemLoads.fallbackdatetime as fallbackdatetime, lSystemLoads.planstatus as planstatus,  ");
	lBuilder.append(" lSystemLoads.activateddatetime as activateddatetime, lSystemLoads.loaddatetime as loaddatetime, lSystemLoads.targetsystem as targetsystem, string_agg(DISTINCT lSystemLoads.developername, ', ') AS developername, ");
	lBuilder.append(" lSystemLoads.qastatus as qastatus,lSystemLoads.loadinstruction as loadinstruction, ");
	lBuilder.append(" string_agg(DISTINCT lSystemLoads.leadname, ', ') as leadname,string_agg(DISTINCT lSystemLoads.managername, ', ') as managername, lSystemLoads.loadcategory as loadcategory, ");
	lBuilder.append(" string_agg(DISTINCT lSystemLoads.peerreviewer, ', ') as peerreviewer,lSystemLoads.dbcrname as dbcrname,  ");
	lBuilder.append(" lSystemLoads.loadattendee as loadattendee,string_agg(DISTINCT lSystemLoads.functionalarea, ', ') as functionalarea, ");
	lBuilder.append(" lSystemLoads.csrnumber as csrnumber,lSystemLoads.projectname as projectname , lSystemLoads.problemticketnum as problemticketnum FROM advance_search_view lSystemLoads ");

	lBuilder.append(" WHERE lSystemLoads.active ='Y' ");

	if (searchForm.getStartDate() != null) {
	    loadDate = " AND (sl.load_date_time BETWEEN :activatedStartTime AND :activatedEndTime)";
	}
	if (searchForm.getImplPlanId() != null) {
	    lBuilder.append(" AND lSystemLoads.planid like (:planid) ");
	}
	if (searchForm.getImplId() != null) {
	    lBuilder.append(" AND lSystemLoads.implid SIMILAR TO ");
	    List<String> list = new ArrayList<String>();
	    list.add(searchForm.getImplId());
	    String csrString = parameterListWithOperator(list, Boolean.TRUE);
	    lBuilder.append(csrString);
	}
	if (searchForm.getTargetSystems() != null && searchForm.getTargetSystems().size() > 0) {
	    lBuilder.append(" AND  lSystemLoads.planid in (select distinct sl.plan_id from system_load sl, system sys, imp_plan plan, implementation impl left join checkout_segments seg on impl.id = seg.imp_id ");
	    lBuilder.append(" WHERE (seg.plan_id = sl.plan_id OR seg.plan_id is NULL) AND impl.active = 'Y' AND plan.active = 'Y' and sl.active = 'Y' and (seg.active = 'Y' OR seg.active is NULL) ");
	    lBuilder.append(" AND sys.id = sl.system_id AND (seg.target_system = sys.name or seg.target_system is NULL) AND impl.plan_id = sl.plan_id AND impl.plan_id = sl.plan_id AND sl.system_id IN (:systemId)" + loadDate + ") ");
	}
	if (searchForm.getProgramName() != null && !searchForm.getProgramName().isEmpty()) {
	    lBuilder.append(" AND LOWER(lSystemLoads.programname) SIMILAR TO ");
	    List<String> list = new ArrayList<String>();
	    list.add(searchForm.getProgramName());
	    String csrString = parameterListWithOperator(list, Boolean.TRUE);
	    lBuilder.append(csrString);
	}

	if (searchForm.getCsrNumber() != null && !searchForm.getCsrNumber().isEmpty()) {
	    lBuilder.append(" AND lSystemLoads.csrnumber SIMILAR TO ");
	    String csrString = parameterListWithOperator(searchForm.getCsrNumber(), true);
	    lBuilder.append(csrString);
	}
	if (searchForm.getImplPlanStatus() != null && !searchForm.getImplPlanStatus().isEmpty()) {
	    lBuilder.append(" AND lSystemLoads.planstatus SIMILAR TO ");
	    String implPlanString = parameterListWithOperator(searchForm.getImplPlanStatus(), false);
	    lBuilder.append(implPlanString);
	}
	if (searchForm.getFunctionalPackages() != null && !searchForm.getFunctionalPackages().isEmpty()) {
	    lBuilder.append(" AND lSystemLoads.functionalarea SIMILAR TO ");
	    String functionalPackagesString = parameterListWithOperator(searchForm.getFunctionalPackages(), true);
	    lBuilder.append(functionalPackagesString);
	}
	List<String> roleList = searchForm.getRole();
	if (roleList != null && roleList.size() > 0) {
	    boolean setFlag = false;
	    lBuilder.append("  AND ( ");
	    String logicalOperator = " OR ";

	    if (roleList.contains(Constants.UserGroup.Lead.name())) {

		lBuilder.append("  lSystemLoads.developerlead in (:developerLead) ");
		setFlag = true;
	    }
	    if (roleList.contains(Constants.UserGroup.Developer.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.developer in (:developer)");
	    }
	    if (roleList.contains(Constants.UserGroup.Reviewer.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.peerreviewer in (:reviewer)");
	    }
	    if (roleList.contains(Constants.UserGroup.LoadsControl.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.loadattendee in (:loadAttendee)");
	    }
	    if (roleList.contains(Constants.UserGroup.DevManager.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.devmanager in (:devManager)");
	    }
	    lBuilder.append(" ) ");
	}
	lBuilder.append("GROUP BY  lSystemLoads.planid,lSystemLoads.plandescription,lSystemLoads.fallbackdatetime,lSystemLoads.planstatus,lSystemLoads.loaddatetime,lSystemLoads.activateddatetime,lSystemLoads.targetsystem,lSystemLoads.qastatus,lSystemLoads.loadinstruction,lSystemLoads.loadcategory,lSystemLoads.loadattendee,lSystemLoads.csrnumber,lSystemLoads.projectname,lSystemLoads.dbcrname,lSystemLoads.problemticketnum");
	lBuilder.append(") as bb GROUP BY bb.programname,bb.planid,bb.plandescription,bb.planstatus,bb.loaddatetime,bb.targetsystem,bb.qastatus,bb.loadcategory,bb.dbcrname,bb.loadinstruction,bb.problemticketnum,bb.loadattendee,bb.csrnumber,bb.projectname,bb.developername,bb.leadname,bb.managername,bb.peerreviewer,bb.functionalarea");

	LOG.info(lBuilder.toString());

	Query lQuery = getCurrentSession().createSQLQuery(lBuilder.toString());

	if (searchForm.getTargetSystems() != null && searchForm.getTargetSystems().size() > 0) {
	    lQuery.setParameterList("systemId", searchForm.getTargetSystems());
	}

	if (searchForm.getImplPlanId() != null) {
	    String progName = (searchForm.getImplPlanId().replaceAll("\\s+", ""));
	    lQuery.setParameter("planid", "%" + progName.trim().toUpperCase() + "%");
	}

	if (searchForm.getStartDate() != null) {
	    lQuery.setParameter("activatedStartTime", searchForm.getStartDate());
	    lQuery.setParameter("activatedEndTime", searchForm.getEndDate());
	}
	if (roleList != null && roleList.size() > 0) {

	    if (roleList.contains(Constants.UserGroup.Lead.name())) {
		lQuery.setParameterList("developerLead", searchForm.getName());
	    }
	    if (roleList.contains(Constants.UserGroup.Developer.name())) {
		lQuery.setParameterList("developer", searchForm.getName());
	    }
	    if (roleList.contains(Constants.UserGroup.Reviewer.name())) {
		lQuery.setParameterList("reviewer", searchForm.getName());
	    }
	    if (roleList.contains(Constants.UserGroup.LoadsControl.name())) {
		lQuery.setParameterList("loadAttendee", searchForm.getName());
	    }
	    if (roleList.contains(Constants.UserGroup.DevManager.name())) {
		lQuery.setParameterList("devManager", searchForm.getName());
	    }

	}

	lQuery.setResultTransformer(new AliasToBeanResultTransformer(AdvancedMetaSearchResult.class));

	return lQuery.list();
    }

    private String parameterListWithOperator(List<String> listObject, boolean wildcard) {
	StringBuffer buffer = new StringBuffer("");
	if (listObject != null && listObject.size() > 0) {
	    if (wildcard) {
		buffer.append(" '%(");
	    } else {
		buffer.append(" '(");
	    }
	    for (int i = 0; i < listObject.size(); i++) {
		String get = listObject.get(i);
		buffer.append(get);
		if (i != (listObject.size() - 1)) {
		    buffer.append("|");
		}
	    }
	    if (wildcard) {
		buffer.append(")%' ");
	    } else {
		buffer.append(")' ");
	    }
	    // '%(APPROVED|ACTIVE)%'
	}
	return buffer.toString();
    }

}

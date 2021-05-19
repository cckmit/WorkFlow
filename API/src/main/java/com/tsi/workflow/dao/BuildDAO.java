package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class BuildDAO extends BaseDAO<Build> {

    public List<Build> findByImpPlan(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Build> findByImpPlan(List<ImpPlan> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Build> findByImpPlan(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Build> findAll(ImpPlan pId, Constants.BUILD_TYPE pBuildType) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("buildType", pBuildType.name());
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Build> findByImpPlan(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Build> findBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Build> findBySystem(List<System> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Build> findBySystem(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Build> findBySystem(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	Criteria lCriteria = getCriteria();

	lCriteria.add(Restrictions.in("systemId", lSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public Build findByBuild(String planId, System pSystem, Integer buildNumber, Constants.BUILD_TYPE buildType) {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(planId));
	lFilter.put("active", "Y");
	lFilter.put("systemId", pSystem);
	lFilter.put("buildNumber", buildNumber);
	lFilter.put("buildType", buildType.name());
	return find(lFilter);
    }

    public List<Build> findLastBuild(String planId, List<System> pSystem, Constants.BUILD_TYPE buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	lCriteria.add(Restrictions.eq("buildType", buildType.name()));
	lCriteria.addOrder(Order.desc("id"));
	lCriteria.setMaxResults(pSystem.size());
	return lCriteria.list();
    }

    public List<Build> findLastBuildDetails(String planId, List<System> pSystem, Constants.BUILD_TYPE buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	lCriteria.add(Restrictions.eq("buildType", buildType.name()));
	lCriteria.addOrder(Order.desc("id"));
	lCriteria.setMaxResults(pSystem.size());
	return lCriteria.list();
    }

    public Build findLastSuccessfulBuild(String pPlanId, Integer pSystemId, Constants.BUILD_TYPE pBuildType) {

	String lQuery = "SELECT build FROM Build build, ImpPlan plan, System sys " + " WHERE build.planId.id = :PlanId" + " AND build.systemId.id = :SystemId" + " AND build.active LIKE 'Y'" + " AND build.jobStatus LIKE 'S'" + " AND build.buildType = :BuildType" + " AND plan.id = :PlanId" + " AND sys.id = :SystemId ORDER BY build.id DESC";

	Build lBuild = (Build) getCurrentSession().createQuery(lQuery).setParameter("PlanId", pPlanId).setParameter("SystemId", pSystemId).setParameter("BuildType", pBuildType.name()).setMaxResults(1).uniqueResult();
	return lBuild;
    }

    public List<String> getBuildInProgressPlan(List<String> pPlanId, List<String> pBuildTypes) {
	String lQuery = "SELECT b.planId.id FROM Build b" + " WHERE b.planId.id IN (:PlanIdList)" + " AND b.jobStatus = 'P'" + " AND b.buildType IN (:BuildType)" + " AND active = 'Y'";

	List<String> lReturnList = getCurrentSession().createQuery(lQuery).setParameterList("PlanIdList", pPlanId).setParameterList("BuildType", pBuildTypes).list();
	return lReturnList;
    }

    public List<Build> findBuildWithPlanAndSystem(String planId, List<System> pSystem, Constants.BUILD_TYPE buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	lCriteria.add(Restrictions.eq("buildType", buildType.name()));
	return lCriteria.list();
    }

    // Need to fetch the last record even if it is active = 'N', used during AUX/E
    // type detection
    public List<Build> findLastBuildByPlan(String planId, List<System> pSystem, Constants.BUILD_TYPE buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	lCriteria.add(Restrictions.eq("buildType", buildType.name()));
	lCriteria.addOrder(Order.desc("id"));
	lCriteria.setMaxResults(pSystem.size());
	return lCriteria.list();
    }

    // fetch the build data inprogress details
    public List<Build> findLastBuildInProgress(String planId, List<System> pSystem, Constants.BUILD_TYPE buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	lCriteria.add(Restrictions.eq("jobStatus", "P"));
	lCriteria.add(Restrictions.eq("buildType", buildType.name()));
	lCriteria.addOrder(Order.desc("id"));
	lCriteria.setMaxResults(pSystem.size());
	return lCriteria.list();
    }

    public List<Build> findBuildInProgress() {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("jobStatus", "P"));
	lCriteria.addOrder(Order.desc("id"));
	return lCriteria.list();
    }

    public Boolean getFullBuildStatus(String pPlanId) {
	String lQuery = "select case when count(distinct b.id) > 0 and count(distinct a.id) = count(distinct b.id) then 1 else 0 end as full_build_status from system_load a " + " left join build b on b.plan_id = a.plan_id and b.active = 'Y' and b.build_type= :buildType and b.job_status = 'S' and b.build_status is not NULL" + " where a.active = 'Y' and a.plan_id = :planId group by a.plan_id";

	Long lCount = Long.parseLong(getCurrentSession().createSQLQuery(lQuery).setParameter("buildType", Constants.BUILD_TYPE.DVL_BUILD.name()).setParameter("planId", pPlanId).uniqueResult().toString());
	return lCount > 0 ? true : false;
    }

    public List<Build> findBuildWithPlanAndSystem(String planId, List<System> pSystem) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	return lCriteria.list();
    }

    public List<Build> findAllWithSuccess(ImpPlan pId, Constants.BUILD_TYPE pBuildType) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("buildType", pBuildType.name());
	lFilter.put("active", "Y");
	lFilter.put("jobStatus", "S");
	return findAll(lFilter);
    }

    public List<String> getBuildQueuePlan(List<String> pBuildTypes) {
	String lQuery = "SELECT lower(concat(b.planId.id,'_',s.name)) FROM Build b, System s" + " WHERE b.jobStatus = 'P'" + " AND s.id = b.systemId.id AND s.active = 'Y' " + " AND b.buildType IN (:BuildType)" + " AND b.active = 'Y'";

	List<String> lReturnList = getCurrentSession().createQuery(lQuery).setParameterList("BuildType", pBuildTypes).list();
	return lReturnList;
    }

    public Build findBuildWithPlanAndSystem(String planId, String systemName, List<String> buildTypes, List<String> runningStatus) {
	String lQuery = "SELECT b FROM Build b, System s" + " WHERE b.planId.id = :PlanId" + " AND s.id = b.systemId.id " + " AND s.active = 'Y' " + " AND b.buildType IN (:BuildType)" + " AND b.active = 'Y'" + " AND s.name = :SystemName" + " AND b.tdxRunningStatus IN (:TdxStatus)";

	Build lReturnList = (Build) getCurrentSession().createQuery(lQuery).setParameter("PlanId", planId).setParameter("SystemName", systemName).setParameterList("TdxStatus", runningStatus)//
		.setParameterList("BuildType", buildTypes).uniqueResult();
	return lReturnList;
    }

    public List<Build> findBuildWithPlanAndSystem(List<String> systemName, List<String> buildTypes, List<String> runningStatus) {
	String lQuery = "SELECT b FROM Build b, System s" + " WHERE s.id = b.systemId.id " + " AND s.active = 'Y' " + " AND b.buildType IN (:BuildType)" + " AND b.active = 'Y'" + " AND s.name IN (:SystemName)" + " AND b.tdxRunningStatus = :TdxStatus";

	List<Build> lReturnList = getCurrentSession().createQuery(lQuery).setParameterList("SystemName", systemName).setParameterList("TdxStatus", runningStatus).setParameterList("BuildType", buildTypes).list();
	return lReturnList;
    }

    public void updateAllInProgressBuild(List<String> systemName, List<String> buildTypes, List<String> runningStatus) {
	String lQuery = "update build set tdx_running_status = :UpdateStatus " + " where id in (" + "SELECT b.id FROM Build b, System s" + " WHERE s.id = b.system_id " + " AND s.active = 'Y' " + " AND b.build_type IN (:BuildType)" + " AND b.active = 'Y'" + " AND s.name IN (:SystemName)" + " AND b.tdx_running_status = :TdxStatus" + " )";

	getCurrentSession().createSQLQuery(lQuery).setParameter("UpdateStatus", Constants.TDXRunningStatus.COMPLETED.getTDXRunningStatus()).setParameterList("SystemName", systemName).setParameterList("TdxStatus", runningStatus).setParameterList("BuildType", buildTypes).executeUpdate();
    }

    public void updateBuildAsInprogress(String planId, String systemName, List<String> buildTypes, List<String> runningStatus) {
	String lQuery = " update build set tdx_running_status = :UpdateStatus " + "where id in ( " + "SELECT b.id FROM Build b, System s" + " WHERE b.plan_id = :PlanId" + " AND s.id = b.system_id " + " AND s.active = 'Y' " + " AND b.build_type IN (:BuildType)" + " AND b.active = 'Y'" + " AND s.name = :SystemName" + " AND b.tdx_Running_Status IN (:TdxStatus))";

	getCurrentSession().createSQLQuery(lQuery).setParameter("UpdateStatus", Constants.TDXRunningStatus.INPROGRESS.getTDXRunningStatus()).setParameter("PlanId", planId).setParameter("SystemName", systemName).setParameterList("TdxStatus", runningStatus)//
		.setParameterList("BuildType", buildTypes).executeUpdate();

    }

    public void updateBuildAsWaiting(String planId, String systemName, List<String> buildTypes, List<String> runningStatus) {
	String lQuery = " update build set tdx_running_status = :UpdateStatus where id in ( " + "SELECT b.id FROM Build b, System s" + " WHERE b.plan_id = :PlanId" + " AND s.id = b.system_id " + " AND s.active = 'Y' " + " AND b.build_type IN (:BuildType)" + " AND b.active = 'Y'" + " AND s.name = :SystemName" + " AND b.tdx_Running_Status IN (:TdxStatus))";

	getCurrentSession().createSQLQuery(lQuery).setParameter("UpdateStatus", Constants.TDXRunningStatus.PENDING.getTDXRunningStatus()).setParameter("PlanId", planId).setParameter("SystemName", systemName).setParameterList("TdxStatus", runningStatus)//
		.setParameterList("BuildType", buildTypes).executeUpdate();

    }
}

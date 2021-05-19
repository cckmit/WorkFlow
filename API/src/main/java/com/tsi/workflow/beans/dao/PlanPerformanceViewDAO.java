/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.ui.PlanPerformanceView;
import java.util.List;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Radha.Adhimoolam
 */
@Repository
public class PlanPerformanceViewDAO extends BaseDAO<SystemLoad> {

    public List<PlanPerformanceView> findByPlanId(String planId) {
	StringBuffer sb = new StringBuffer();
	sb.append("select planId,impid,targetSystem,tdx, ");
	sb.append(" CASE when sbtCount is null then 0 else sbtCount end as sbtcount, ");
	sb.append(" CASE when asmCount is null then 0 else asmCount end as asmcount, ");
	sb.append(" CASE when makCount is null then 0 else makCount end as makcount , ");
	sb.append(" CASE when ccppCount is null then 0 else ccppCount end as ccppcount, ");
	sb.append(" CASE when headerCount is null then 0 else headerCount end as headercount , ");
	sb.append(" totalCount, soCount, repoCount, repoNameList  from ");
	sb.append(" (select distinct cs.plan_id as planId, cs.imp_id as impid, cs.target_system as targetSystem,  s.alias_name as tdx, ");
	sb.append(" (Select totalcount from (select count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%.sbt' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system ) as sub where sub.tarSys=cs.target_system) as sbtCount , ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%.asm' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system )as sub where sub.tarSys=cs.target_system) as asmCount, ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%.mak' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system ) as sub where sub.tarSys=cs.target_system) as makCount , ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system  as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%(.c|.cpp)' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system )as sub where sub.tarSys=cs.target_system) as ccppCount , ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%(.h|.hpp|.mac|.inc)' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system )as sub where sub.tarSys=cs.target_system) as headerCount , ");
	sb.append(" (select  count(*)  from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id and asm.target_system= cs.target_system) as totalCount, ");
	sb.append(" (select COUNT(distinct asm.so_name) from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id and asm.so_name is not null) as soCount , ");
	sb.append(" (select COUNT(distinct asm.func_area) from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id) as repoCount , ");
	sb.append(" (select string_agg( distinct asm.func_Area,', ') ");
	sb.append(" from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id) as repoNameList ");
	sb.append(" from checkout_segments cs, imp_plan ip, system s  where s.name = cs.target_system and s.active = 'Y' and cs.active = 'Y' ");
	sb.append(" and ip.id = cs.plan_id  and ip.id = '");
	sb.append(planId);
	sb.append("' )as aa  order by planId , impid");
	List<PlanPerformanceView> lReturn = getCurrentSession().createSQLQuery(sb.toString()).setResultTransformer(new AliasToBeanResultTransformer(PlanPerformanceView.class)).list();
	return lReturn;
    }

    public List<PlanPerformanceView> findByImpId(String impId) {
	StringBuffer sb = new StringBuffer();
	sb.append("select planId,impid,targetSystem,tdx, ");
	sb.append(" CASE when sbtCount is null then 0 else sbtCount end as sbtcount, ");
	sb.append(" CASE when asmCount is null then 0 else asmCount end as asmcount, ");
	sb.append(" CASE when makCount is null then 0 else makCount end as makcount, ");
	sb.append(" CASE when ccppCount is null then 0 else ccppCount end as ccppcount, ");
	sb.append(" CASE when headerCount is null then 0 else sbtCount end as headercount, ");
	sb.append(" totalCount, soCount, repoCount, repoNameList from ");
	sb.append(" (select distinct cs.plan_id as planId, cs.imp_id as impid, cs.target_system as targetSystem, s.alias_name as tdx, ");
	sb.append(" (Select totalcount from (select count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%.sbt' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system ) as sub where sub.tarSys=cs.target_system) as sbtCount , ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%.asm' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system )as sub where sub.tarSys=cs.target_system) as asmCount, ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%.mak' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system ) as sub where sub.tarSys=cs.target_system) as makCount , ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system  as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%(.c|.cpp)' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system )as sub where sub.tarSys=cs.target_system) as ccppCount , ");
	sb.append(" (Select totalcount from (select  count(*) as totalcount,asm.target_system as tarsys from checkout_segments asm where asm.active = 'Y' and asm.program_name SIMILAR TO '%(.h|.hpp|.mac|.inc)' and asm.imp_id = cs.imp_id and asm.target_system=cs.target_system group by asm.target_system )as sub where sub.tarSys=cs.target_system) as headerCount , ");
	sb.append(" (select  count(*)  from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id and asm.target_system= cs.target_system) as totalCount, ");
	sb.append(" (select COUNT(distinct asm.so_name) from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id and asm.so_name is not null) as soCount , ");
	sb.append(" (select COUNT(distinct asm.func_area) from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id) as repoCount , ");
	sb.append(" (select string_agg( distinct asm.func_Area,', ') ");
	sb.append(" from checkout_segments asm where asm.active = 'Y' and asm.imp_id = cs.imp_id) as repoNameList ");
	sb.append(" from checkout_segments cs, imp_plan ip ,system s  where s.name = cs.target_system and cs.active = 'Y' and ip.id = cs.plan_id  and ip.id = cs.plan_id ");
	sb.append(" and cs.imp_id = :ImpId )as aa order by planId , impid desc");
	List<PlanPerformanceView> lReturn = getCurrentSession().createSQLQuery(sb.toString()).setParameter("ImpId", impId).setResultTransformer(new AliasToBeanResultTransformer(PlanPerformanceView.class)).list();
	return lReturn;
    }
}

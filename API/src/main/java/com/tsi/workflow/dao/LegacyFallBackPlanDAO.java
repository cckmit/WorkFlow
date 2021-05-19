/**
 * 
 */
package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.LegacyFallBackPlan;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author vinoth.ponnurangan
 *
 */
@Repository
public class LegacyFallBackPlanDAO extends BaseDAO<LegacyFallBackPlan> {

    public List<LegacyFallBackPlan> findByLegacyFallBackPlan(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("id", new LegacyFallBackPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<LegacyFallBackPlan> findByLegacyPlan(LegacyFallBackPlan lLegacyPlan) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", lLegacyPlan.getPlanId());
	lFilter.put("programName", lLegacyPlan.getProgramName());
	lFilter.put("funcArea", lLegacyPlan.getFuncArea());
	lFilter.put("loadDateTime", lLegacyPlan.getLoadDateTime());
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

}

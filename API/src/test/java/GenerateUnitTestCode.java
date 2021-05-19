import com.tsi.workflow.beans.dao.ActivityLog;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ImpPlanApprovals;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.utils.JSONResponse;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author USER
 */
public class GenerateUnitTestCode {

    // @ResponseBody
    // @RequestMapping(value = "/getUnitTestData", method = RequestMethod.POST)
    public JSONResponse getUnitTestData(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws Exception {
	// return lDAPService.getUnitTestData(planId);
	return null;
    }

    public JSONResponse getUnitTestData(String planId) throws Exception {
	BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
	ImpPlan lPlan = null;// getImpPlanDAO().find(planId);

	ImpPlan lReturnPlan = new ImpPlan();
	ImpPlan lCopyPlan = new ImpPlan();

	BeanUtils.copyProperties(lReturnPlan, lPlan);
	BeanUtils.copyProperties(lCopyPlan, lPlan);

	{
	    lCopyPlan.setImplementationList(null);
	    lCopyPlan.setSystemLoadList(null);
	    lCopyPlan.setActivityLogList(null);
	    lCopyPlan.setSystemLoadActionsList(null);
	    lCopyPlan.setDbcrList(null);
	    lCopyPlan.setProductionLoadsList(null);
	    lCopyPlan.setCheckoutSegmentsList(null);
	    lCopyPlan.setImpPlanApprovalsList(null);
	    lCopyPlan.setBuildList(null);
	}
	{
	    lReturnPlan.setImplementationList(null);
	    lReturnPlan.setSystemLoadList(null);
	    lReturnPlan.setActivityLogList(null);
	    lReturnPlan.setSystemLoadActionsList(null);
	    lReturnPlan.setDbcrList(null);
	    lReturnPlan.setProductionLoadsList(null);
	    lReturnPlan.setCheckoutSegmentsList(null);
	    lReturnPlan.setImpPlanApprovalsList(null);
	    lReturnPlan.setBuildList(null);
	}

	{
	    List<Build> lList = lPlan.getBuildList();
	    List<Build> lReturnList = new ArrayList<>();
	    for (Build build : lList) {
		Build lReturnBuild = new Build();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setBuildList(lReturnList);
	}
	{
	    List<Implementation> lList = lPlan.getImplementationList();
	    List<Implementation> lReturnList = new ArrayList<>();
	    for (Implementation build : lList) {
		Implementation lReturnBuild = new Implementation();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setImplementationList(lReturnList);
	}
	{
	    List<SystemLoad> lList = lPlan.getSystemLoadList();
	    List<SystemLoad> lReturnList = new ArrayList<>();
	    for (SystemLoad build : lList) {
		SystemLoad lReturnBuild = new SystemLoad();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setSystemLoadList(lReturnList);
	}
	{
	    List<ActivityLog> lList = lPlan.getActivityLogList();
	    List<ActivityLog> lReturnList = new ArrayList<>();
	    for (ActivityLog build : lList) {
		ActivityLog lReturnBuild = new ActivityLog();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		if (lReturnBuild.getImpId() != null) {
		    lReturnBuild.getImpId().setPlanId(lCopyPlan);
		}
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setActivityLogList(lReturnList);
	}
	{
	    List<SystemLoadActions> lList = lPlan.getSystemLoadActionsList();
	    List<SystemLoadActions> lReturnList = new ArrayList<>();
	    for (SystemLoadActions build : lList) {
		SystemLoadActions lReturnBuild = new SystemLoadActions();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		if (lReturnBuild.getSystemLoadId() != null) {
		    lReturnBuild.getSystemLoadId().setPlanId(lCopyPlan);
		}
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setSystemLoadActionsList(lReturnList);
	}
	{
	    List<Dbcr> lList = lPlan.getDbcrList();
	    List<Dbcr> lReturnList = new ArrayList<>();
	    for (Dbcr build : lList) {
		Dbcr lReturnBuild = new Dbcr();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setDbcrList(lReturnList);
	}
	{
	    List<ProductionLoads> lList = lPlan.getProductionLoadsList();
	    List<ProductionLoads> lReturnList = new ArrayList<>();
	    for (ProductionLoads build : lList) {
		ProductionLoads lReturnBuild = new ProductionLoads();

		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		if (lReturnBuild.getSystemLoadId() != null) {
		    lReturnBuild.getSystemLoadId().setPlanId(lCopyPlan);
		}
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setProductionLoadsList(lReturnList);
	}
	{
	    List<CheckoutSegments> lList = lPlan.getCheckoutSegmentsList();
	    List<CheckoutSegments> lReturnList = new ArrayList<>();
	    for (CheckoutSegments build : lList) {
		CheckoutSegments lReturnBuild = new CheckoutSegments();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		if (lReturnBuild.getImpId() != null) {
		    lReturnBuild.getImpId().setPlanId(lCopyPlan);
		}
		if (lReturnBuild.getSystemLoad() != null) {
		    lReturnBuild.getSystemLoad().setPlanId(lCopyPlan);
		}
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setCheckoutSegmentsList(lReturnList);
	}
	{
	    List<ImpPlanApprovals> lList = lPlan.getImpPlanApprovalsList();
	    List<ImpPlanApprovals> lReturnList = new ArrayList<>();
	    for (ImpPlanApprovals build : lList) {
		ImpPlanApprovals lReturnBuild = new ImpPlanApprovals();
		BeanUtils.copyProperties(lReturnBuild, build);
		lReturnBuild.setPlanId(lCopyPlan);
		lReturnList.add(lReturnBuild);
	    }
	    lReturnPlan.setImpPlanApprovalsList(lReturnList);
	}

	JSONResponse lResponse = new JSONResponse();
	// LOG.info(new Gson().toJson(lReturnPlan));
	lResponse.setData(lReturnPlan);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public String getQuery() {
	return "select 1 as slNo, count(id) from activity_log union " + "select 2 as slNo, count(id) from build union " + "select 3 as slNo, count(id) from checkout_segments union " + "select 4 as slNo, count(id) from dbcr union " + "select 5 as slNo, count(id) from imp_plan union " + "select 6 as slNo, count(id) from imp_plan_approvals union " + "select 7 as slNo, count(id) from implementation union " + "select 8 as slNo, count(id) from load_categories union "
		+ "select 9 as slNo, count(id) from load_freeze union " + "select 10 as slNo, count(id) from load_window union " + "select 11 as slNo, count(id) from platform union " + "select 12 as slNo, count(id) from production_loads union " + "select 13 as slNo, count(id) from project union " + "select 14 as slNo, count(id) from put_level union " + "select 15 as slNo, count(id) from system union " + "select 16 as slNo, count(id) from system_cpu union "
		+ "select 17 as slNo, count(id) from system_load union " + "select 18 as slNo, count(id) from system_load_actions union " + "select 19 as slNo, count(id) from user_settings union " + "select 20 as slNo, count(id) from vpars " + "order by 1";
    }

}

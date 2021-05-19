import com.tsi.workflow.base.controller.CommonBaseController;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.controller.CommonController;
import com.tsi.workflow.controller.DeveloperController;
import com.tsi.workflow.controller.DeveloperLeadController;
import com.tsi.workflow.controller.DeveloperManagerController;
import com.tsi.workflow.controller.LoadsControlController;
import com.tsi.workflow.controller.ProtectedController;
import com.tsi.workflow.controller.QualityAssuranceController;
import com.tsi.workflow.controller.ReviewerController;
import com.tsi.workflow.controller.TSDController;
import com.tsi.workflow.controller.TestSystemSupportController;
import com.tsi.workflow.controller.UserController;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanApprovalsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.LoadCategoriesDAO;
import com.tsi.workflow.dao.LoadFreezeDAO;
import com.tsi.workflow.dao.LoadWindowDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.service.CommonService;
import com.tsi.workflow.service.DeveloperLeadService;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.service.DeveloperService;
import com.tsi.workflow.service.LDAPService;
import com.tsi.workflow.service.LoadsControlService;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.service.QualityAssuranceService;
import com.tsi.workflow.service.ReviewerService;
import com.tsi.workflow.service.TSDService;
import com.tsi.workflow.service.TestSystemSupportService;
import com.tsi.workflow.utils.JSONResponse;
import java.lang.reflect.Method;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author USER
 */
public class GenerateData {

    public static void main(String[] args) {
	Class[] lclasses = new Class[] { ActivityLogDAO.class, BuildDAO.class, CheckoutSegmentsDAO.class, CommonBaseController.class, CommonBaseService.class, CommonController.class, CommonService.class, DbcrDAO.class, DeveloperController.class, DeveloperLeadController.class, DeveloperLeadService.class, DeveloperManagerController.class, DeveloperManagerService.class, DeveloperService.class, ImpPlanApprovalsDAO.class, ImpPlanDAO.class, ImplementationDAO.class, LDAPService.class,
		LoadCategoriesDAO.class, LoadFreezeDAO.class, LoadWindowDAO.class, LoadsControlController.class, LoadsControlService.class, PlatformDAO.class, ProductionLoadsDAO.class, ProjectDAO.class, ProtectedController.class, ProtectedService.class, PutLevelDAO.class, QualityAssuranceController.class, QualityAssuranceService.class, ReviewerController.class, ReviewerService.class, SystemCpuDAO.class, SystemDAO.class, SystemLoadActionsDAO.class, SystemLoadDAO.class, TSDController.class,
		TSDService.class, TestSystemSupportController.class, TestSystemSupportService.class, UserController.class, UserSettingsDAO.class, VparsDAO.class, };

	for (Class lclass : lclasses) {
	    Method[] methods = lclass.getMethods();
	    for (Method method : methods) {
		if (method.getName().equals("equals") | method.getName().equals("wait")) {
		    continue;
		}
		if (method.getParameterCount() > 0) {
		    System.out.println("{" + "ParamInOut paramInOut = new ParamInOut();");
		    for (int i = 0; i < method.getParameterCount(); i++) {
			System.out.println("paramInOut.addIn(null);");
		    }
		    if (method.getReturnType() == JSONResponse.class) {
			System.out.println("paramInOut.setOut(DataWareHouse.getPositiveResponse());");
		    } else {
			System.out.println("paramInOut.setOut(null);");
		    }
		    System.out.println("ParameterMap.addParameterInOut(\"" + lclass.getSimpleName() + "." + method.getName() + "\", paramInOut);" + "}");
		}
	    }
	}
    }
}

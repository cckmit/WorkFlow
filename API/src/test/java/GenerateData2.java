import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.User;
import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.BaseBeans;
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
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author USER
 */
public class GenerateData2 {

    public static void main(String[] args) throws Exception {
	Class[] lclasses = new Class[] { ActivityLogDAO.class, BuildDAO.class, CheckoutSegmentsDAO.class, CommonBaseController.class, CommonBaseService.class, CommonController.class, CommonService.class, DbcrDAO.class, DeveloperController.class, DeveloperLeadController.class, DeveloperLeadService.class, DeveloperManagerController.class, DeveloperManagerService.class, DeveloperService.class, ImpPlanApprovalsDAO.class, ImpPlanDAO.class, ImplementationDAO.class, LDAPService.class,
		LoadCategoriesDAO.class, LoadFreezeDAO.class, LoadWindowDAO.class, LoadsControlController.class, LoadsControlService.class, PlatformDAO.class, ProductionLoadsDAO.class, ProjectDAO.class, ProtectedController.class, ProtectedService.class, PutLevelDAO.class, QualityAssuranceController.class, QualityAssuranceService.class, ReviewerController.class, ReviewerService.class, SystemCpuDAO.class, SystemDAO.class, SystemLoadActionsDAO.class, SystemLoadDAO.class, TSDController.class,
		TSDService.class, TestSystemSupportController.class, TestSystemSupportService.class, UserController.class, UserSettingsDAO.class, VparsDAO.class, };
	DataWareHouse.init();
	int j = 0;
	for (Class lclass : lclasses) {
	    StringBuilder lBuilder = new StringBuilder();
	    lBuilder.append("package com.tsi.workflow.datawarehouse;\n");
	    lBuilder.append("import com.tsi.workflow.ParamInOut;\nimport com.tsi.workflow.ParameterMap;\nimport com.tsi.workflow.DataWareHouse;\n");
	    lBuilder.append("public class ").append(lclass.getSimpleName()).append("DataHub {\n");
	    lBuilder.append("public static void init() {\n");
	    Method[] methods = lclass.getMethods();
	    System.out.println("/** " + lclass.getSimpleName() + " */");
	    for (Method method : methods) {
		if (method.getName().equals("equals") || method.getName().equals("toString") || method.getName().equals("hashCode") || method.getName().equals("getClass") || method.getName().equals("notify") || method.getName().equals("notifyAll") || method.getName().equals("wait")) {
		    continue;
		}

		if (method.getParameterCount() > 0) {
		    lBuilder.append("{\n");
		    if (!ParameterMap.lMap.containsKey(lclass.getSimpleName() + "." + method.getName())) {
			lBuilder.append("//Need to Replace\n");
		    }
		    lBuilder.append("ParamInOut paramInOut = new ParamInOut();\n");
		    for (int i = 0; i < method.getParameterCount(); i++) {
			Class inClass = method.getParameters()[i].getType();
			if (inClass == List.class) {
			    if (inClass.getGenericSuperclass() != null) {
				inClass = inClass.getGenericSuperclass().getClass();
			    }
			}
			if (inClass.getSuperclass() == BaseBeans.class) {
			    lBuilder.append("paramInOut.addIn(get" + inClass.getSimpleName() + "()); // " + inClass.getSimpleName() + "\n");
			} else if (inClass == User.class) {
			    lBuilder.append("paramInOut.addIn(get" + inClass.getSimpleName() + "()); // " + inClass.getSimpleName() + "\n");
			} else if (inClass == ActivityLogMessage.class) {
			    lBuilder.append("paramInOut.addIn(get" + inClass.getSimpleName() + "()); // " + inClass.getSimpleName() + "\n");
			} else if (inClass == HttpServletRequest.class) {
			    lBuilder.append("paramInOut.addIn(get" + inClass.getSimpleName() + "()); // " + inClass.getSimpleName() + "\n");
			} else if (inClass == HttpServletResponse.class) {
			    lBuilder.append("paramInOut.addIn(get" + inClass.getSimpleName() + "()); // " + inClass.getSimpleName() + "\n");
			} else {
			    lBuilder.append("paramInOut.addIn(null); // " + inClass.getSimpleName() + "\n");
			}
		    }
		    if (method.getReturnType() == JSONResponse.class) {
			lBuilder.append("paramInOut.setOut(DataWareHouse.getPositiveResponse());\n");
		    } else {
			lBuilder.append("paramInOut.setOut(null); // " + method.getReturnType().getSimpleName() + "\n");
		    }
		    lBuilder.append("ParameterMap.addParameterInOut(\"").append(lclass.getSimpleName()).append(".").append(method.getName()).append("\", paramInOut);" + "\n}\n");
		}
	    }
	    lBuilder.append("    }\n");
	    lBuilder.append("}\n");
	    System.out.println(lBuilder.toString());
	    FileWriter fileWriter = new FileWriter("D:\\" + lclass.getSimpleName() + "DataHub.java");
	    IOUtils.write(lBuilder.toString(), fileWriter);
	    IOUtils.closeQuietly(fileWriter);
	}
    }
}

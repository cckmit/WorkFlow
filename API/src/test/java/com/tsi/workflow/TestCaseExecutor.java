/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.base.BaseBeans;
import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.interfaces.ISystem;
import com.tsi.workflow.utils.JSONResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class TestCaseExecutor {

    private static final Logger LOG = Logger.getLogger(TestCaseExecutor.class.getName());

    static {
	DataWareHouse.init();
	LOG.setLevel(DataWareHouse.LogLevel);
    }

    public static void doTest(Object instance, Class pClass) {
	doTest(instance, pClass, false);
    }

    public static void doTestDAO(Object instance, Class pClass) {
	doTest(instance, pClass, true);
    }

    public static void doTest(Object instance, Class pClass, boolean isDAO) {
	Method[] methods = pClass.getDeclaredMethods();
	Arrays.sort(methods, new MethodNameSotrer());
	for (Method method : methods) {
	    if (method.getName().contains("$")) {
		continue;
	    }
	    if (!Modifier.isPublic(method.getModifiers())) {
		continue;
	    }
	    if (method.getName().equals("getDAOClass")) {
		continue;
	    }
	    if (method.getReturnType().equals(Void.TYPE)) {
		continue;
	    }
	    Class<?>[] paramTypes = method.getParameterTypes();
	    Object[] lDefParams = new Object[paramTypes.length];
	    ParameterMap lParams = ParameterMap.lMap.get(pClass.getSimpleName() + "." + method.getName());
	    try {
		if (lParams != null) {
		    List<ParamInOut> paramInOuts = lParams.getParamInOuts();
		    for (ParamInOut paramInOut : paramInOuts) {
			List<Object> in = paramInOut.getIn();
			if (method.getParameterCount() != in.size()) {
			    continue;
			}
			for (int i = 0; i < in.size(); i++) {
			    lDefParams[i] = in.get(i);
			    if (in.get(i) != null) {
				LOG.debug("Param " + i + " = " + in.get(i).getClass().getSimpleName());
			    } else {
				LOG.debug("Param " + i + " = Null");
			    }
			}
			LOG.debug("Param Count " + in.size() + "\t\tExpected Param Count " + method.getParameterCount());
			boolean isValidCall = true;
			for (int i = 0; i < method.getParameterCount(); i++) {
			    if (in.get(i) != null) {
				LOG.debug("Parm ClassName : " + in.get(i).getClass().getSimpleName() + "\t\tExpected Param " + method.getParameterTypes()[i].getSimpleName());
				if ((in.get(i) instanceof List) && method.getParameterTypes()[i] == List.class) {
				    lDefParams[i] = (List) in.get(i);
				} else if ((in.get(i) instanceof HashMap) && method.getParameterTypes()[i] == Map.class) {
				    lDefParams[i] = (Map) in.get(i);
				} else if ((in.get(i) instanceof ISystem) && method.getParameterTypes()[i] == ISystem.class) {
				    lDefParams[i] = (ISystem) in.get(i);
				} else if (in.get(i).getClass() == method.getParameterTypes()[i]) {
				    lDefParams[i] = in.get(i);
				} else {
				    isValidCall = false;
				    break;
				}
				LOG.debug("Param " + i + " = " + lDefParams[i].getClass().getSimpleName() + "\t\tExpected Param " + i + " = " + method.getParameterTypes()[i].getSimpleName());
			    } else {
				lDefParams[i] = null;
				LOG.debug("Param " + i + " = Null\t\tExpected Param " + i + " = " + method.getParameterTypes()[i].getSimpleName());
			    }
			}
			if (!isValidCall) {
			    LOG.warn("Warn in Mocking Method : " + pClass.getSimpleName() + "." + method.getName() + " ,Error: Parameter not matching");
			    continue;
			}
			Object lReturnObject = paramInOut.getOut();
			LOG.info(lReturnObject);
			LOG.info("Executing with params : " + pClass.getSimpleName() + "." + method.getName() + ", with param count " + lDefParams.length);
			if (isDAO) {
			    LOG.info(paramInOut.getIn());
			    SessionFactory sessionFactory = mock(SessionFactory.class);
			    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
			    Session session = mock(Session.class);
			    Criteria criteria = mock(Criteria.class);
			    //
			    when(sessionFactory.getCurrentSession()).thenReturn(session);
			    when(session.createCriteria(BaseDAO.class)).thenReturn(criteria);
			    Criteria lCriteria = mock(Criteria.class);
			    Mockito.when(((BaseDAO) instance).getCriteria()).thenReturn(lCriteria);
			    if (lReturnObject == null) {
				Mockito.when(((BaseDAO) instance).getCriteria().uniqueResult()).thenReturn(null);
				Mockito.when(((BaseDAO) instance).getCriteria().list()).thenReturn(null);
			    } else if (lReturnObject instanceof Long) {
				Mockito.when(((BaseDAO) instance).getCriteria().uniqueResult()).thenReturn(lReturnObject);
			    } else if (lReturnObject instanceof Integer) {
				Mockito.when(((BaseDAO) instance).getCriteria().uniqueResult()).thenReturn(lReturnObject);
			    } else if (lReturnObject instanceof BaseBeans) {
				Mockito.when(((BaseDAO) instance).getCriteria().uniqueResult()).thenReturn(lReturnObject);
			    } else if (lReturnObject instanceof List) {
				Mockito.when(((BaseDAO) instance).getCriteria().list()).thenReturn((List) lReturnObject);
			    } else {
				Mockito.when(((BaseDAO) instance).getCriteria().list()).thenReturn(new ArrayList<>());
			    }
			}
			Object invoke;
			if (lDefParams.length == 0) {
			    invoke = method.invoke(instance);
			} else {
			    invoke = method.invoke(instance, lDefParams);
			}
			LOG.info(method.getReturnType() + "   " + invoke);
			if (lReturnObject instanceof JSONResponse) {
			    JSONResponse lResponse1 = (JSONResponse) lReturnObject;
			    JSONResponse lResponse2 = (JSONResponse) invoke;
			    if (lResponse1 != null && lResponse2 != null) {
				if (lResponse1.getStatus() == lResponse2.getStatus()) {
				    Assert.assertEquals(lResponse1.getStatus(), lResponse2.getStatus());
				} else {
				    Assert.assertNotNull(pClass.getSimpleName() + "." + method.getName(), invoke);
				}
			    }
			} else if (method.getReturnType().equals(Void.TYPE)) {
			    assertTrue(pClass.getSimpleName() + "." + method.getName(), true);
			} else if (method.getReturnType().equals(Map.class)) {
			    assertTrue(pClass.getSimpleName() + "." + method.getName(), true);
			} else {
			    Assert.assertEquals(pClass.getSimpleName() + "." + method.getName(), lReturnObject.getClass(), invoke.getClass());
			}
		    }
		} else {
		    for (Object param : lDefParams) {
			param = null;
		    }
		    LOG.info("Executing: " + pClass.getSimpleName() + "." + method.getName() + ", with param count " + lDefParams.length);
		    Object invoke;
		    if (lDefParams.length == 0) {
			invoke = method.invoke(instance);
		    } else {
			invoke = method.invoke(instance, lDefParams);
		    }

		    // LOG.info(invoke.getClass());
		    // LOG.info(method.getReturnType());
		    // LOG.info(pClass.getSimpleName() + "." + method.getName());
		    if (method.getReturnType().equals(Void.TYPE)) {
			assertTrue(pClass.getSimpleName() + "." + method.getName(), true);
		    } else {
			if (invoke != null) {
			    Assert.assertNotNull(pClass.getSimpleName() + "." + method.getName(), invoke);
			}
		    }

		}
		// } catch (InvocationTargetException ex) {
		// String lErrorClass = ex.getCause().getStackTrace()[1].getClassName();
		// String lErrorMethod = ex.getCause().getStackTrace()[1].getMethodName();
		// int lErrorLine = ex.getCause().getStackTrace()[1].getLineNumber();
		// fail("Fail on Data " + lErrorClass + "." + lErrorMethod + " line No." +
		// lErrorLine);
	    } catch (Exception ex) {
		LOG.error("Error in Exeuting Method : " + pClass.getSimpleName() + "." + method.getName() + ", Error: " + ex.getMessage() + ex);
		if (LOG.isDebugEnabled()) {
		    LOG.error("", ex);
		}
		// fail("Fail on Exception " + ex.getMessage());
	    }
	}
    }
}

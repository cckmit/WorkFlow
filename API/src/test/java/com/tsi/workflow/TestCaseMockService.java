/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.interfaces.ISystem;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.utils.Constants.LoginErrorCode;
import com.tsi.workflow.utils.JSONResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 *
 * @author USER
 */
public class TestCaseMockService {

    private static final Logger LOG = Logger.getLogger(TestCaseMockService.class.getName());

    static {
	DataWareHouse.init();
	LOG.setLevel(DataWareHouse.LogLevel);
    }

    /**
     *
     * @param pClass
     *            - DAO Class
     * @param lTemplate
     *            - DAO Instance
     */
    public static void doMockBaseDAO(Class pClass, Class pBean, Object lTemplate) throws Exception {
	Method[] methods = getBaseMethods(pClass, lTemplate);
	for (Method method : methods) {
	    // if (method.getName().equals("getSessionFactory")) {
	    // when(method.invoke(lTemplate)).thenReturn(mock(SessionFactory.class));
	    // }
	}
	for (Method method : methods) {
	    if (method.getName().equals("getDAOClass")) {
		when(method.invoke(lTemplate)).thenReturn(pBean);
	    }
	}
	for (Method method : methods) {
	    if (method.getName().equals("getCriteria")) {
		Session lSession = mock(Session.class);
		Criteria lCriteria = mock(Criteria.class);
		when(lSession.createCriteria(pBean)).thenReturn(lCriteria);

		// when(method.invoke(lTemplate)).thenReturn(lSession);
	    }
	}
	// Method[] methods1 = getMethods(pClass, lTemplate);
	// TestCaseMockService.setMockRecursive(pClass, lTemplate, pBean, methods1);
    }

    /**
     *
     * @param instance
     *            - Service Instance
     * @param pClass
     *            - DAO/Helper Class
     * @throws Exception
     */
    public static void doMockDAO(Object instance, Class pClass) throws Exception {
	Object lTemplate = mock(pClass);
	SessionFactory sessionFactory = mock(SessionFactory.class);
	Session session = mock(Session.class);

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	Method[] methods = TestCaseMockService.getALLMethods(pClass, lTemplate);
	TestCaseMockService.setMockRecursive(pClass, lTemplate, null, methods);
	Method method = instance.getClass().getMethod("get" + pClass.getSimpleName(), null);
	when(method.invoke(instance)).thenReturn(lTemplate);
    }

    // public static void doMockSerivce(Class pClass, Object lTemplate) {
    // Method[] methods = TestCaseMockService.getMethods(pClass, lTemplate);
    // TestCaseMockService.setMockRecursive(pClass, lTemplate, methods);
    // }
    /**
     *
     * @param instance
     *            - Controller Instance
     * @param pClass
     *            - service Class
     * @throws Exception
     */
    public static void doMockController(BaseController instance, Class pClass) throws Exception {
	Object lTemplate = mock(pClass);
	when(instance.getCurrentUser(any(), any())).thenReturn(DataWareHouse.getUser());
	Method[] methods = TestCaseMockService.getALLMethods(pClass, lTemplate);
	TestCaseMockService.setMockRecursive(pClass, lTemplate, null, methods);
	Method method = instance.getClass().getMethod("get" + pClass.getSimpleName(), null);
	when(method.invoke(instance)).thenReturn(lTemplate);
    }

    private static Method[] getMethods(Class pClass, Object pObject) {
	Method[] methods = pClass.getDeclaredMethods();
	return methods;
    }

    private static Method[] getALLMethods(Class pClass, Object pObject) {
	Method[] methods = pClass.getDeclaredMethods();
	Method[] methods1 = getBaseMethods(pClass, pObject);
	methods = (Method[]) ArrayUtils.addAll(methods, methods1);
	return methods;
    }

    private static Method[] getBaseMethods(Class pClass, Object pObject) {
	Method[] methods = new Method[0];
	if (pClass.getSuperclass() == BaseDAO.class) {
	    Method[] methods1 = pClass.getSuperclass().getDeclaredMethods();
	    methods = (Method[]) ArrayUtils.addAll(methods, methods1);
	}
	if (pClass.getSuperclass() == BaseController.class) {
	    Method[] methods1 = pClass.getSuperclass().getDeclaredMethods();
	    methods = (Method[]) ArrayUtils.addAll(methods, methods1);
	}
	if (pClass.getSuperclass() == CommonBaseService.class) {
	    Method[] methods1 = pClass.getSuperclass().getDeclaredMethods();
	    methods = (Method[]) ArrayUtils.addAll(methods, methods1);
	}
	if (pClass.getSuperclass() == BaseService.class) {
	    Method[] methods1 = pClass.getSuperclass().getDeclaredMethods();
	    methods = (Method[]) ArrayUtils.addAll(methods, methods1);
	}
	return methods;
    }

    private static void setMockRecursive(Class pClass, Object pObject, Class pBean, Method[] methods) throws Exception {
	// For Each Method
	SessionFactory sessionFactory = mock(SessionFactory.class);
	Session session = mock(Session.class);
	Criteria criteria = mock(Criteria.class);
	//
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createCriteria(BaseDAO.class)).thenReturn(criteria);
	for (Method method : methods) {
	    if (method.getName().equals("$jacocoInit")) {
		continue;
	    }
	    LOG.debug("Method Name " + method.getName());
	    Class<?> returnType = method.getReturnType();
	    if (returnType == IBeans.class) {
		Type lType = pClass.getGenericSuperclass();
		if (lType instanceof ParameterizedTypeImpl) {
		    ParameterizedTypeImpl lImpl = (ParameterizedTypeImpl) lType;
		    returnType = (Class) lImpl.getActualTypeArguments()[0];
		}
	    }
	    if (!Modifier.isPublic(method.getModifiers())) {
		continue;
	    }
	    if (method.getName().equals("getSessionFactory")) {
		ReflectionTestUtils.setField(pObject, "sessionFactory", sessionFactory);
		// when(method.invoke(pObject)).thenReturn(mock(SessionFactory.class));
		continue;
	    }
	    if (method.getName().equals("getDAOClass")) {
		when(method.invoke(pObject)).thenReturn(pBean);
		continue;
	    }
	    if (method.getName().equals("getCurrentSession")) {
		// Session lSession = mock(Session.class);
		// Criteria lCriteria = mock(Criteria.class);
		// when(lSession.createCriteria(pBean)).thenReturn(lCriteria);
		//// when(lCriteria.uniqueResult()).thenReturn(lReturnObject);
		//// when(lCriteria.list()).thenReturn((List) lReturnObject);
		// when(method.invoke(pObject)).thenReturn(lSession);
		continue;
	    }
	    if (method.getName().equals("getCriteria")) {
		// Criteria lCriteria = mock(Criteria.class);
		//// when(lCriteria.uniqueResult()).thenReturn(83L);
		//// when(lCriteria.list()).thenReturn(new ArrayList());
		// when(method.invoke(pObject)).thenReturn(lCriteria);
		continue;
	    }
	    if (method.getReturnType().equals(Void.TYPE)) {
		continue;
	    }
	    Class<?>[] paramTypes = method.getParameterTypes();
	    Object[] lDefParams = new Object[paramTypes.length];
	    ParameterMap lParams = ParameterMap.lMap.get(pClass.getSimpleName() + "." + method.getName());
	    Object lReturnObject;
	    try {
		lReturnObject = getreturnObject(returnType);
		if (lParams != null) {
		    List<ParamInOut> paramInOuts = lParams.getParamInOuts();

		    // For Each Mock Params
		    for (ParamInOut paramInOut : paramInOuts) {
			List<Object> in = paramInOut.getIn();
			if (method.getParameterCount() != in.size()) {
			    continue;
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
			// lDefParams = in.toArray();
			lReturnObject = paramInOut.getOut();
			if (lReturnObject != null) {
			    LOG.debug("Mocking Method with params : " + pClass.getSimpleName() + "." + method.getName() + ", with param count " + lDefParams.length + ", with return Type " + lReturnObject.getClass().getSimpleName());
			} else {
			    LOG.debug("Mocking Method with params : " + pClass.getSimpleName() + "." + method.getName() + ", with param count " + lDefParams.length + ", with return Type Null");
			}
			when(method.invoke(pObject, lDefParams)).thenReturn(lReturnObject);
		    }
		} else {
		    for (Object param : lDefParams) {
			param = Mockito.anyObject();
		    }
		    LOG.debug("Mocking Method : " + pClass.getSimpleName() + "." + method.getName() + ", with param count " + lDefParams.length + ", with return Type " + returnType.getSimpleName());
		    when(method.invoke(pObject, lDefParams)).thenReturn(lReturnObject);
		}
	    } catch (IllegalArgumentException e) {
		LOG.error("Check with the datatype of the method " + pClass.getSimpleName() + "." + method.getName() + " All Should be in wrapper class");
		if (LOG.isDebugEnabled()) {
		    LOG.error("", e);
		}
		// fail("Fail on Exception " + e.getMessage());
	    } catch (Exception ex) {
		LOG.error("Error in Mocking Method : " + pClass.getSimpleName() + "." + method.getName() + " ,Error: " + ex.getMessage());
		if (LOG.isDebugEnabled()) {
		    LOG.error("", ex);
		}
		// fail("Fail on Exception " + ex.getMessage());
	    }
	}
    }

    public static Object getreturnObject(Class<?> returnType) throws Exception {
	LOG.info(returnType);
	if (returnType == List.class) {
	    return new ArrayList<>();
	} else if (returnType == List[].class) {
	    return new List[0];
	} else if (returnType == Collection.class) {
	    return new ArrayList<>();
	} else if (returnType == Map.class) {
	    return new HashMap<>();
	} else if (returnType == SortedSet.class) {
	    return new TreeSet<>();
	} else if (returnType == Long.class) {
	    return new Long(0);
	} else if (returnType == Integer.class) {
	    return new Integer(0);
	} else if (returnType == Boolean.class) {
	    return new Boolean(true);
	} else if (returnType == JSONResponse.class) {
	    return mockJSONResponse();
	} else if (returnType == JGitClientUtils.class) {
	    return mockJGitClientUtils();
	} else if (returnType == GitBlitClientUtils.class) {
	    return mockGitBlitClientUtils();
	} else if (returnType == BPMClientUtils.class) {
	    return mockBPMClientUtils();
	} else if (returnType == LoginErrorCode.class) {
	    return LoginErrorCode.SUCCESS;
	} else if (returnType == MailMessage.class) {
	    return MailMessage.class.getClass();
	} else if (returnType == JenkinsClient.class) {
	    return mockJenkinsClient();
	} else if (returnType == BuildResult.class) {
	    return BuildResult.SUCCESS;
	} else if (returnType == JenkinsBuild.class) {
	    return mockJenkinsBuild();
	} else if (returnType == Map.class) {
	    return new HashMap<>();
	} else {
	    return returnType.newInstance();
	}

    }

    private static JGitClientUtils mockJGitClientUtils() {
	return mock(JGitClientUtils.class);
    }

    private static GitBlitClientUtils mockGitBlitClientUtils() {
	return mock(GitBlitClientUtils.class);
    }

    private static BPMClientUtils mockBPMClientUtils() {
	return mock(BPMClientUtils.class);
    }

    private static JenkinsClient mockJenkinsClient() {
	return mock(JenkinsClient.class);
    }

    private static JenkinsBuild mockJenkinsBuild() {
	return mock(JenkinsBuild.class);
    }

    private static JSONResponse mockJSONResponse() {
	return mock(JSONResponse.class);
    }

}

package com.tsi.workflow.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.GiPorts;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class GiPortsDAOTest {

    GiPortsDAO instance;
    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() throws Exception {
	GiPortsDAO dao = new GiPortsDAO();
	instance = Mockito.spy(dao);
	sessionFactory = mock(SessionFactory.class);
	session = mock(Session.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	TestCaseMockService.doMockBaseDAO(GiPortsDAO.class, GiPorts.class, instance);
    }

    @Test
    public void test00() {
	GiPorts dao = new GiPorts();
	instance.hardDelete(dao);
    }

    @Test
    public void test01() {
	String pUserId = "e739670";
	sessionFactory = mock(SessionFactory.class);
	session = mock(Session.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(ActivityLogDAO.class);
	Mockito.when(session.createCriteria(ActivityLogDAO.class)).thenReturn(mockedCriteria);

	List<GiPorts> value = new ArrayList<GiPorts>();
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("userId", pUserId);
	lFilter.put("active", "Y");
	when(instance.findAll(lFilter)).thenReturn(value);
	instance.findByUserId(pUserId);
    }

}

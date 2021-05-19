package com.tsi.workflow.schedular;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.LegacyFallBackPlanDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

public class LegacyFallBackImpPlanTest {

    public LegacyFallBackImpPlanTest() {
    }

    @Mock
    SystemDAO instance;

    @Mock
    JSONResponse lResponse;

    private SessionFactory sessionFactory;
    private Session session;
    private Criteria criteria;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    SystemDAO realInstance = new SystemDAO();
	    SystemDAO instance = new SystemDAO();
	    instance = new SystemDAO();
	    lResponse = new JSONResponse();
	    lResponse.setStatus(true);
	    instance = spy(realInstance);
	    sessionFactory = mock(SessionFactory.class);
	    session = mock(Session.class);
	    criteria = mock(Criteria.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    when(session.createCriteria(BaseDAO.class)).thenReturn(criteria);
	    TestCaseMockService.doMockBaseDAO(SystemDAO.class, System.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(LegacyFallBackImpPlanTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPopulateLegacyImpPlanFromGit() throws Exception {
	WFConfig wFConfig = new WFConfig();
	wFConfig.setServiceUserID("e738090");
	wFConfig.setAttachmentDirectory("D:/demo");
	wFConfig.setBuildLogDir("E:/Demo");
	wFConfig.setDevCentreMailID("tpf@software.com");
	wFConfig.setIbmVanillaDirectory("D:test");
	LegacyFallBackImpPlan instance = spy(new LegacyFallBackImpPlan());
	instance.getCheckoutSegmentsDAO();
	instance.getConfig();
	instance.getLegacyFallBackDAO();
	instance.getRejectHelper();
	instance.getsSHClientUtils();
	instance.getSystemDAO();
	instance.getwFConfig();
	ReflectionTestUtils.setField(instance, "legacyFallBackDAO", mock(LegacyFallBackPlanDAO.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	instance.populateLegacyGitInfo();
	when(instance.systemDAO.findAll()).thenReturn(Arrays.asList(DataWareHouse.getSystemList().get(0)));

    }
}

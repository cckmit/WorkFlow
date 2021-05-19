package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.ui.ProductionLoadDetailsForm;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.ProductionLoadDetailsMail;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductionDeploymentsSchedularTest {

    ProductionDeploymentsSchedular instance;
    List<ProductionLoadDetailsForm> productionLoadBeforeDayDetails;
    List<ProductionLoadDetailsForm> productionLoadNextDayDetails;
    ProductionLoadDetailsForm productionLoadDetailsForm;
    String companyName;
    ProductionLoadDetailsMail productionLoadDetailsMail;
    BlockingQueue<MailMessage> blockingQueue;

    @Before
    public void setUp() throws Exception {
	ProductionDeploymentsSchedular productionDeploymentsSchedular = new ProductionDeploymentsSchedular();
	instance = Mockito.spy(productionDeploymentsSchedular);

	ReflectionTestUtils.setField(instance, "wFConfig", Mockito.mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", Mockito.mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", Mockito.mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "putLevelDAO", Mockito.mock(PutLevelDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", Mockito.mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", Mockito.mock(MailMessageFactory.class));

	productionLoadBeforeDayDetails = new ArrayList<>();
	productionLoadNextDayDetails = new ArrayList<>();
	productionLoadDetailsForm = Mockito.mock(ProductionLoadDetailsForm.class);
	companyName = "dl";
	productionLoadDetailsMail = Mockito.mock(ProductionLoadDetailsMail.class);
	blockingQueue = Mockito.mock(BlockingQueue.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetProductionDeploymentLoads() {

	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(true);
	Mockito.when(instance.impPlanDAO.getProductionLoadsBeforeDayDetails(companyName)).thenReturn(productionLoadBeforeDayDetails);
	Mockito.when(instance.impPlanDAO.getProductionLoadsNextDayDetails(companyName)).thenReturn(productionLoadNextDayDetails);
	Mockito.when(instance.getMailMessageFactory().getTemplate(ProductionLoadDetailsMail.class)).thenReturn(productionLoadDetailsMail);
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	Mockito.when(instance.wFConfig.getProdLoadsCentreMailId()).thenReturn("value");
	companyName = "tp";
	Mockito.when(instance.impPlanDAO.getProductionLoadsBeforeDayDetails(companyName)).thenReturn(productionLoadBeforeDayDetails);
	Mockito.when(instance.impPlanDAO.getProductionLoadsNextDayDetails(companyName)).thenReturn(productionLoadNextDayDetails);
	Mockito.when(productionLoadDetailsForm.getLoaddatetime()).thenReturn(new Date("2019/09/09"));
	instance.getProductionDeploymentLoads();

    }

    @Test
    public void testGetProductionDeploymentLoads1() {
	productionLoadBeforeDayDetails.add(productionLoadDetailsForm);
	productionLoadNextDayDetails.add(productionLoadDetailsForm);

	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(true);
	Mockito.when(instance.impPlanDAO.getProductionLoadsBeforeDayDetails(companyName)).thenReturn(productionLoadBeforeDayDetails);
	Mockito.when(instance.impPlanDAO.getProductionLoadsNextDayDetails(companyName)).thenReturn(productionLoadNextDayDetails);
	Mockito.when(instance.getMailMessageFactory().getTemplate(ProductionLoadDetailsMail.class)).thenReturn(productionLoadDetailsMail);
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	Mockito.when(instance.wFConfig.getProdLoadsCentreMailId()).thenReturn("value");
	companyName = "tp";
	Mockito.when(instance.impPlanDAO.getProductionLoadsBeforeDayDetails(companyName)).thenReturn(productionLoadBeforeDayDetails);
	Mockito.when(instance.impPlanDAO.getProductionLoadsNextDayDetails(companyName)).thenReturn(productionLoadNextDayDetails);
	Mockito.when(productionLoadDetailsForm.getLoaddatetime()).thenReturn(new Date("2019/09/09"));
	instance.getProductionDeploymentLoads();

    }

    @Test
    public void testGetProductionDeploymentLoads2() {

	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(true);
	Mockito.when(instance.impPlanDAO.getProductionLoadsBeforeDayDetails(companyName)).thenReturn(productionLoadBeforeDayDetails);
	Mockito.when(instance.impPlanDAO.getProductionLoadsNextDayDetails(companyName)).thenReturn(productionLoadNextDayDetails);
	Mockito.when(instance.getMailMessageFactory().getTemplate(ProductionLoadDetailsMail.class)).thenReturn(productionLoadDetailsMail);
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	String string = "null";
	Mockito.when(instance.wFConfig.getProdLoadsCentreMailId()).thenReturn(string);
	companyName = "tp";
	Mockito.when(instance.impPlanDAO.getProductionLoadsBeforeDayDetails(companyName)).thenReturn(productionLoadBeforeDayDetails);
	Mockito.when(instance.impPlanDAO.getProductionLoadsNextDayDetails(companyName)).thenReturn(productionLoadNextDayDetails);
	Mockito.when(productionLoadDetailsForm.getLoaddatetime()).thenReturn(new Date("2019/09/09"));
	instance.getProductionDeploymentLoads();

    }

    @Test
    public void testGetProductionDeploymentLoads3() {

	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(false);
	instance.getProductionDeploymentLoads();

    }

}

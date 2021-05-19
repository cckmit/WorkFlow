package com.tsi.workflow.schedular;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.helper.MaintenanceHelper;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class MaintenanceMonitorTest {

    MaintenanceMonitor instance;

    @Before
    public void setUp() throws Exception {
	MaintenanceMonitor maintenanceMonitor = new MaintenanceMonitor();
	instance = Mockito.spy(maintenanceMonitor);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoMonitor() {

	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	List<System> lSystemList = new ArrayList();
	lSystemList.add(DataWareHouse.getSystemList().get(0));
	Mockito.when(instance.systemDAO.findAll()).thenReturn(lSystemList);
	JSONResponse jsonResponse = new JSONResponse();
	jsonResponse.setStatus(true);
	Mockito.when(instance.getsSHClientUtils().executeCommand(DataWareHouse.getSystemList().get(0), "${MTP_ENV}/mtptpfupdatenfmdsl apo")).thenReturn(jsonResponse);
	instance.doMonitor();

    }

    @Test
    public void testDoMonitor1() {

	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "maintenanceHelper", mock(MaintenanceHelper.class));
	List<System> lSystemList = new ArrayList();
	lSystemList.add(DataWareHouse.getSystemList().get(0));
	Mockito.when(instance.systemDAO.findAll()).thenReturn(lSystemList);
	JSONResponse jsonResponse = new JSONResponse();
	jsonResponse.setStatus(false);
	Mockito.when(instance.getsSHClientUtils().executeCommand(DataWareHouse.getSystemList().get(0), "${MTP_ENV}/mtptpfupdatenfmdsl apo")).thenReturn(jsonResponse);
	Mockito.doNothing().when(instance.maintenanceHelper).notifyToolAdminOnProcessFails(DataWareHouse.getSystemList().get(0).getName());
	instance.doMonitor();
	instance.getBuildDAO();
	instance.getGITConfig();
	instance.getWFConfig();

    }

}

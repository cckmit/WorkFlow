/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tdx.executor;

import com.tsi.workflow.tdx.executor.models.TdxShellExecutorModel;
import com.tsi.workflow.tdx.executor.service.CheckInExecutor;
import com.tsi.workflow.tdx.executor.service.CheckOutExecutor;
import com.tsi.workflow.tdx.executor.service.CommitExecutor;
import com.tsi.workflow.tdx.executor.service.DeleteExecutor;
import java.io.IOException;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class TdxShellExecutor {

    private static final Logger LOG = Logger.getLogger(TdxShellExecutor.class.getName());

    @Autowired
    CheckInExecutor checkinExecutor;

    @Autowired
    CheckOutExecutor checkouyExecutor;

    @Autowired
    CommitExecutor commitExecutor;

    @Autowired
    DeleteExecutor deleteExecutor;

    @Async
    @Transactional
    public Future<TdxShellExecutorModel> executeCheckin(TdxShellExecutorModel executorModel) {

	return checkinExecutor.executeCheckin(executorModel);
    }

    @Async
    @Transactional
    public Future<TdxShellExecutorModel> executeCheckOut(TdxShellExecutorModel executorModel) throws IOException {
	return checkouyExecutor.executeCheckout(executorModel);
    }

    @Async
    @Transactional
    public Future<TdxShellExecutorModel> executeCommit(TdxShellExecutorModel executorModel) {
	return commitExecutor.executeCommit(executorModel);
    }

    @Async
    @Transactional
    public Future<TdxShellExecutorModel> executeDelete(TdxShellExecutorModel executorModel) {
	return deleteExecutor.executeDelete(executorModel);
    }
}

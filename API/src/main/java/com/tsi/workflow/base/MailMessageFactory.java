/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import com.tsi.workflow.exception.WorkflowException;
import java.util.concurrent.BlockingQueue;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class MailMessageFactory {

    @Autowired
    ApplicationContext applicationContext;

    @Resource
    @Qualifier("mailQueue")
    BlockingQueue<MailMessage> mailQueue;

    private static final Logger LOG = Logger.getLogger(MailMessageFactory.class.getName());

    public MailMessage getTemplate(Class<? extends MailMessage> pClassName) throws WorkflowException {
	try {
	    MailMessage object = (MailMessage) Class.forName(pClassName.getName()).newInstance();
	    applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
	    return object;
	} catch (Exception ex) {
	    LOG.error("Error in getting the MailTemplate", ex);
	    throw new WorkflowException("Error in getting the MailTemplate", ex);
	}
    }

    public final void push(final MailMessage pMessage) throws WorkflowException {
	getMailQueue().add(pMessage);
    }

    public BlockingQueue<MailMessage> getMailQueue() {
	return mailQueue;
    }

    public void setMailQueue(BlockingQueue<MailMessage> mailQueue) {
	this.mailQueue = mailQueue;
    }
}

package com.tsi.workflow.schedular;

import com.tsi.workflow.base.MailMessage;
import java.util.concurrent.BlockingQueue;
import javax.annotation.Resource;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class MailListener {

    @Resource
    @Qualifier("mailQueue")
    BlockingQueue<MailMessage> mailQueue;

    private static final Logger LOG = Logger.getLogger(MailListener.class.getName());

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void sendMails() {
	MailMessage lMailItem;
	lMailItem = mailQueue.poll();
	while (lMailItem != null) {
	    try {
		lMailItem.processMessage();
		lMailItem.send();
	    } catch (Exception ex) {
		LOG.error("Error in Sending Mail for " + lMailItem.getClass().getSimpleName(), ex);
	    }
	    lMailItem = mailQueue.poll();
	}
    }
}

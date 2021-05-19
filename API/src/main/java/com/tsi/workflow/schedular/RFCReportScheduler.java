package com.tsi.workflow.schedular;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.helper.RFCHelper;
import com.tsi.workflow.mail.RFCReportMail;
import com.tsi.workflow.report.RFCReportCreator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RFCReportScheduler {

    private static final Logger LOG = Logger.getLogger(RFCReportScheduler.class.getName());
    @Autowired
    WFConfig wFConfig;
    @Autowired
    RFCDetailsDAO rfcDetailsDAO;
    @Autowired
    RFCHelper rfcHelper;
    @Autowired
    MailMessageFactory mailMessageFactory;

    public WFConfig getwFConfig() {
	return wFConfig;
    }

    public RFCDetailsDAO getRfcDetailsDAO() {
	return rfcDetailsDAO;
    }

    public RFCHelper getRFCHelper() {
	return rfcHelper;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    @Scheduled(cron = "1 1 3 * * ?")
    @Transactional
    public void doMonitor() {
	if (getwFConfig().getPrimary()) {
	    LOG.info("RFC Scheduler process started.");
	    try {
		RFCReportCreator rfcReportCreator = new RFCReportCreator();
		Instant startDate = ZonedDateTime.now().toInstant();
		Instant endDate = startDate.plus(21, ChronoUnit.DAYS);

		LocalDateTime lStartDate = LocalDateTime.now();
		LocalDateTime lEndDate = lStartDate.plusDays(21);

		getRFCHelper().getRFCReportForm(rfcReportCreator, null, Date.from(startDate), Date.from(endDate), null, true, null);

		String fileName = "RFC_Report_" + lStartDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + ".xls";
		FileOutputStream fos = new FileOutputStream("/tmp/" + fileName);
		rfcReportCreator.getWorkBook().write(fos);
		rfcReportCreator.getWorkBook().close();
		fos.close();

		RFCReportMail rfcReportMail = (RFCReportMail) getMailMessageFactory().getTemplate(RFCReportMail.class);
		rfcReportMail.setReportStartDate(lStartDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		rfcReportMail.setReportEndDate(lEndDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		rfcReportMail.addToProdLoadCentre(true);
		rfcReportMail.addAttachment("/tmp/" + fileName);
		getMailMessageFactory().push(rfcReportMail);
		LOG.info("RFC Scheduler process completed.");
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

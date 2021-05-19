package com.tsi.workflow.schedular;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.mail.RfcFieldsAlertMail;
import com.tsi.workflow.utils.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RfcFieldsAlertScheduler {

    private static final Logger LOG = Logger.getLogger(RfcFieldsAlertScheduler.class.getName());

    @Autowired
    RFCDetailsDAO rfcDetailsDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    WFConfig wFConfig;

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public RFCDetailsDAO getRfcDetailsDAO() {
	return rfcDetailsDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public WFConfig getwFConfig() {
	return wFConfig;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE * 30)
    @Transactional
    public void doMonitor() {
	if (getwFConfig().getPrimary()) {
	    LocalDateTime startDate = LocalDateTime.now();
	    LocalDateTime endDate = startDate.plusDays(18);
	    List<Object[]> rfcReports = getRfcDetailsDAO().getRfcFieldsPendingPlanDetails(startDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")), endDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
	    List<String> planIds = new ArrayList<>();

	    for (Object[] plan : rfcReports) {
		String planId = plan[0].toString();
		planIds.add(planId);
		String loadDateTime = plan[1].toString();
		RfcFieldsAlertMail rfcFieldsAlertMail = (RfcFieldsAlertMail) getMailMessageFactory().getTemplate(RfcFieldsAlertMail.class);
		rfcFieldsAlertMail.setLoadDateTime(loadDateTime);
		rfcFieldsAlertMail.setPlanId(planId);
		rfcFieldsAlertMail.addToAddressUserId(plan[2].toString(), Constants.MailSenderRole.LEAD);
		rfcFieldsAlertMail.addToAddressUserId(plan[3].toString(), Constants.MailSenderRole.DEV_MANAGER);
		getMailMessageFactory().push(rfcFieldsAlertMail);
	    }

	    if (!planIds.isEmpty()) {
		List<ImpPlan> lImpPlans = getImpPlanDAO().find(planIds);
		lImpPlans.forEach(imp -> {
		    imp.setRfcMailFlag(Boolean.TRUE);
		    getImpPlanDAO().update(lDAPAuthenticatorImpl.getServiceUser(), imp);
		});
	    }
	}

    }

    @Scheduled(cron = "1 1 3 * * ?")
    @Transactional
    public void resetRfcMailFlag() {
	if (getwFConfig().getPrimary()) {
	    getImpPlanDAO().resetRfcMailFlag();
	}

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.mail.ExceptionLoadMail;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author
 */
@Component
public class ExceptionLoadNotificationHelper {

    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;

    public void notifyDevelopers(String pPlanId, Constants.PlanStatus status) {
	List<Object[]> planList = impPlanDAO.getSameSegmentDevelopers(pPlanId, new ArrayList<>(Constants.PlanStatus.getNonProdStatusMap().keySet()));
	Map<String, List<Object[]>> collect = planList.stream().collect(Collectors.groupingBy(t -> t[1].toString()));

	for (Map.Entry<String, List<Object[]>> entrySet : collect.entrySet()) {
	    String key = entrySet.getKey();
	    List<Object[]> value = entrySet.getValue();
	    Set<String> lPlans = new HashSet<>();

	    ExceptionLoadMail exceptionLoadMail = (ExceptionLoadMail) mailMessageFactory.getTemplate(ExceptionLoadMail.class);
	    for (Object[] segment : value) {
		exceptionLoadMail.addToAddressUserId(segment[2].toString(), Constants.MailSenderRole.DEVELOPER);
		exceptionLoadMail.addToAddressUserId(segment[3].toString(), Constants.MailSenderRole.DEVELOPER);
		lPlans.add(segment[0].toString() + " [" + segment[4].toString() + "]");
	    }

	    exceptionLoadMail.setPlanId(pPlanId);
	    exceptionLoadMail.setStatus(status.name());
	    exceptionLoadMail.setPlanDetails(String.join(", ", lPlans));
	    exceptionLoadMail.setSubject(key + " : Resolve source contention with the exception Load " + pPlanId);
	    mailMessageFactory.push(exceptionLoadMail);
	}
    }
}

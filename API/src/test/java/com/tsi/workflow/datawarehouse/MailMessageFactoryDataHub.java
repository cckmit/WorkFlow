/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.mail.CheckoutDeleteMail;
import com.tsi.workflow.mail.CompilerValidationMail;
import com.tsi.workflow.mail.DelegateActivationMail;
import com.tsi.workflow.mail.DeveloperReassignmentMail;
import com.tsi.workflow.mail.ImplementationStatusCompletionMail;
import com.tsi.workflow.mail.NewTargetSystemMail;
import com.tsi.workflow.mail.PutDateChangeMail;
import com.tsi.workflow.mail.RejectMail;
import com.tsi.workflow.mail.ReviewerAssignmentMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;

/**
 *
 * @author User
 */
public class MailMessageFactoryDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(StatusChangeToDependentPlanMail.class);
	    paramInOut.setOut(new StatusChangeToDependentPlanMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(CompilerValidationMail.class.getClass());
	    paramInOut.setOut(new CompilerValidationMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(RejectMail.class.getClass());
	    paramInOut.setOut(new RejectMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(ReviewerAssignmentMail.class.getClass());
	    paramInOut.setOut(new ReviewerAssignmentMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(ImplementationStatusCompletionMail.class.getClass());
	    paramInOut.setOut(new ImplementationStatusCompletionMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DeveloperReassignmentMail.class.getClass());
	    paramInOut.setOut(new DeveloperReassignmentMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(CheckoutDeleteMail.class.getClass());
	    paramInOut.setOut(new CheckoutDeleteMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(NewTargetSystemMail.class.getClass());
	    paramInOut.setOut(new NewTargetSystemMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(PutDateChangeMail.class.getClass());
	    paramInOut.setOut(new PutDateChangeMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DelegateActivationMail.class.getClass());
	    paramInOut.setOut(new DelegateActivationMail());
	    ParameterMap.addParameterInOut("MailMessageFactory.getTemplate", paramInOut);
	}
    }
}

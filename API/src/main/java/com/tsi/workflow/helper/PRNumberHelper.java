package com.tsi.workflow.helper;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.snow.pr.SnowPRClientUtils;
import com.tsi.workflow.utils.Constants;
import com.workflow.tos.PRWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PRNumberHelper {

    private static final Logger LOG = Logger.getLogger(PRNumberHelper.class.getName());

    @Autowired
    PRWriter pRWriter;

    @Autowired
    WFConfig wfConfig;

    @Autowired
    SnowPRClientUtils snowPRClientUtils;

    @Autowired
    ImplementationDAO impDAO;

    public void writeFileInNAS(ImpPlan impPlan) {
	if (impPlan.getId().startsWith("D")) {
	    return;
	}
	if (impPlan.getSdmTktNum() != null && !impPlan.getSdmTktNum().isEmpty() && Constants.PlanStatus.getPRStatus().containsKey(impPlan.getPlanStatus())) {
	    if (wfConfig.getIsSdmDBMigrated()) {
		updatePRNumber(impPlan);
	    } else {
		Date dateTime = new Date();
		String lFileFormat = Constants.JGIT_COMMENT_DATEFORMAT_1.get().format(dateTime);
		String lWriteFormat = Constants.REX_DATEFORMAT.get().format(dateTime);
		String lFileName = impPlan.getId() + "_update_" + lFileFormat + ".txt";
		Constants.PlanStatus lStatus = Constants.PlanStatus.valueOf(impPlan.getPlanStatus());
		List<String> prTickets = Arrays.asList(impPlan.getSdmTktNum().split(","));
		StringBuilder sb = new StringBuilder();
		prTickets.forEach(prTicket -> {
		    sb.append(String.format(Constants.PR_FILE_FORMAT, prTicket, lStatus.getPrStatus(), lWriteFormat));
		});
		pRWriter.writeData(lFileName, sb.toString());
	    }
	}
    }

    public Boolean updatePRNumber(ImpPlan plan) {

	if (plan.getId().startsWith("D") || !wfConfig.getIsSdmDBMigrated()) {
	    return Boolean.TRUE;
	}
	return updatePRNumber(plan, null);
    }

    public Boolean updatePRNumber(ImpPlan plan, String prStatus) {

	if (plan.getId().startsWith("D") || !wfConfig.getIsSdmDBMigrated()) {
	    return Boolean.TRUE;
	}

	Set<String> prTickets = new HashSet(Arrays.asList(plan.getSdmTktNum().split(",")));
	if (prStatus == null || prStatus.isEmpty()) {
	    Constants.PlanStatus lStatus = Constants.PlanStatus.valueOf(plan.getPlanStatus());
	    prStatus = lStatus.getPrStatus();
	}
	List<Implementation> impList = impDAO.findByImpPlan(plan.getId());
	prTickets.addAll(impList.stream().filter(t -> t.getPrTktNum() != null && !t.getPrTktNum().isEmpty()).map(t -> t.getPrTktNum()).collect(Collectors.toSet()));
	for (String prTicket : prTickets) {
	    try {
		snowPRClientUtils.updatePRNumberAndStatus(prTicket, prStatus);
	    } catch (Exception ex) {
		LOG.info("Error Occurs on updating the problem ticket number - " + prTicket, ex);
	    }
	}
	return Boolean.TRUE;
    }
}

package com.tsi.workflow.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.AdvancedMetaSearchResult;
import com.tsi.workflow.beans.ui.AdvancedSearchForm;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.test.util.ReflectionTestUtils;

public class AdvanceSearchViewDAOTest {

    AdvanceSearchViewDAO instance;
    AdvanceSearchViewDAO mockedInstance;

    public AdvanceSearchViewDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    AdvanceSearchViewDAO realInstance = new AdvanceSearchViewDAO();
	    instance = spy(realInstance);
	    mockedInstance = mock(AdvanceSearchViewDAO.class);
	    TestCaseMockService.doMockBaseDAO(AdvanceSearchViewDAO.class, SystemLoad.class, instance);

	} catch (Exception ex) {
	    Logger.getLogger(AdvanceSearchViewDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetPlanByAdvancedSearch() {
	AdvancedSearchForm searchForm = new AdvancedSearchForm();
	StringBuilder lBuilder = new StringBuilder();
	lBuilder.append(" SELECT lSystemLoads.programname as programname, lSystemLoads.planid as planid, lSystemLoads.plandescription as plandescription, ");
	lBuilder.append(" lSystemLoads.fallbackdatetime as fallbackdatetime, lSystemLoads.planstatus as planstatus,  ");
	lBuilder.append(" lSystemLoads.activateddatetime as activateddatetime, lSystemLoads.loaddatetime as loaddatetime, lSystemLoads.targetsystem as targetsystem, lSystemLoads.developername as developername, ");
	lBuilder.append(" lSystemLoads.qastatus as qastatus,lSystemLoads.loadinstruction as loadinstruction, ");
	lBuilder.append(" lSystemLoads.leadname as leadname,lSystemLoads.managername as managername, lSystemLoads.loadcategory as loadcategory, ");
	lBuilder.append(" lSystemLoads.peerreviewer  as peerreviewer,lSystemLoads.dbcrname as dbcrname,  ");
	lBuilder.append(" lSystemLoads.loadattendee as loadattendee,lSystemLoads.functionalarea as functionalarea, ");
	lBuilder.append(" lSystemLoads.csrnumber as csrnumber, lSystemLoads.projectname as projectname FROM advance_search_view lSystemLoads ");

	lBuilder.append(" WHERE lSystemLoads.active ='Y' ");

	if (searchForm.getStartDate() != null) {
	    lBuilder.append(" AND (lSystemLoads.loaddatetime between :activatedStartTime AND :activatedEndTime OR  lSystemLoads.loaddatetime is NULL)");
	}

	if (searchForm.getTargetSystems() != null && searchForm.getTargetSystems().size() > 0) {
	    lBuilder.append(" AND lSystemLoads.systemId IN (:systemId) ");
	}
	if (searchForm.getProgramName() != null && !searchForm.getProgramName().isEmpty()) {
	    lBuilder.append(" AND lSystemLoads.programname SIMILAR TO ");
	    List<String> list = new ArrayList<String>();
	    list.add(searchForm.getProgramName());
	    String csrString = parameterListWithOperator(list);
	    lBuilder.append(csrString);
	}

	if (searchForm.getCsrNumber() != null && !searchForm.getCsrNumber().isEmpty()) {
	    lBuilder.append(" AND lSystemLoads.csrnumber SIMILAR TO ");
	    String csrString = parameterListWithOperator(searchForm.getCsrNumber());
	    lBuilder.append(csrString);
	}
	if (searchForm.getImplPlanStatus() != null && !searchForm.getImplPlanStatus().isEmpty()) {
	    lBuilder.append(" AND lSystemLoads.planstatus SIMILAR TO ");
	    String implPlanString = parameterListWithOperator(searchForm.getImplPlanStatus());
	    lBuilder.append(implPlanString);
	}
	if (searchForm.getFunctionalPackages() != null && !searchForm.getFunctionalPackages().isEmpty()) {
	    lBuilder.append(" AND lSystemLoads.functionalarea SIMILAR TO ");
	    String functionalPackagesString = parameterListWithOperator(searchForm.getFunctionalPackages());
	    lBuilder.append(functionalPackagesString);
	}
	List<String> roleList = searchForm.getRole();
	if (roleList != null && roleList.size() > 0) {
	    boolean setFlag = false;
	    lBuilder.append("  AND ( ");
	    String logicalOperator = " OR ";

	    if (roleList.contains(Constants.UserGroup.Lead.name())) {

		lBuilder.append("  lSystemLoads.developerlead = :developerLead ");
		setFlag = true;
	    }
	    if (roleList.contains(Constants.UserGroup.Developer.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.developer = :developer");
	    }
	    if (roleList.contains(Constants.UserGroup.Reviewer.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.peerreviewer = :reviewer");
	    }
	    if (roleList.contains(Constants.UserGroup.LoadsControl.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.loadattendee = :loadAttendee");
	    }
	    if (roleList.contains(Constants.UserGroup.DevManager.name())) {
		if (setFlag) {
		    lBuilder.append(logicalOperator);
		} else {
		    setFlag = true;
		}
		lBuilder.append("  lSystemLoads.devmanager = :devManager");
	    }
	    lBuilder.append(" ) ");
	}

	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lBuilder.toString())).thenReturn(mockedQry);
	when(mockedQry.setParameterList(Matchers.any(), Matchers.anyCollection())).thenReturn(mockedQry);
	when(mockedQry.setParameter(Matchers.any(), Matchers.any())).thenReturn(mockedQry);
	when(mockedQry.setResultTransformer(new AliasToBeanResultTransformer(AdvancedMetaSearchResult.class))).thenReturn(mockedQry);

	Integer offset = 0;
	Integer limit = 10;
	LinkedHashMap lOrderBy = new LinkedHashMap();

	instance.getPlanByAdvancedSearchView(searchForm, offset, limit, lOrderBy);
    }

    private String parameterListWithOperator(List<String> listObject) {
	StringBuffer buffer = new StringBuffer("");
	if (listObject != null && listObject.size() > 0) {
	    buffer.append(" '%(");
	    for (int i = 0; i < listObject.size(); i++) {
		String get = listObject.get(i);
		buffer.append(get);
		if (i != (listObject.size() - 1)) {
		    buffer.append("|");
		}
	    }
	    buffer.append(")%' ");
	}
	return buffer.toString();
    }
}

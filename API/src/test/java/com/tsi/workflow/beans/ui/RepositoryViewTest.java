package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class RepositoryViewTest {

    private RepositoryView repositoryView;

    @Before
    public void setUp() {
	repositoryView = new RepositoryView();
    }

    @Test
    public void getRepositoryViewtest() {

	repositoryView.setRepository(DataWareHouse.getRepositoryDetails());
	assertNotNull(repositoryView.getRepository());

    }

    @Test
    public void getRepositoryDefaulttest() {

	repositoryView.setDefaultAccess("READ");

	assertNotNull("READ", repositoryView.getDefaultAccess());
    }

}

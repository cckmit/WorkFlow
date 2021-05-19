/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadWindow;
import java.util.List;

/**
 *
 * @author USER
 */
public class LoadCategoriesForm {

    private LoadCategories loadCategory;
    private List<LoadWindow> loadWindows;

    public LoadCategoriesForm() {
    }

    public LoadCategoriesForm(LoadCategories loadCategory, List<LoadWindow> loadWindows) {
	this.loadCategory = loadCategory;
	this.loadWindows = loadWindows;
    }

    public LoadCategories getLoadCategory() {
	return loadCategory;
    }

    public void setLoadCategory(LoadCategories loadCategory) {
	this.loadCategory = loadCategory;
    }

    public List<LoadWindow> getLoadWindows() {
	return loadWindows;
    }

    public void setLoadWindows(List<LoadWindow> loadWindows) {
	this.loadWindows = loadWindows;
    }

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof LoadCategoriesForm)) {
	    return false;
	}
	LoadCategoriesForm lLoadCategoriesForm = (LoadCategoriesForm) obj;
	return loadCategory.equals(lLoadCategoriesForm.loadCategory);
    }

    @Override
    public int hashCode() {
	return loadCategory.hashCode();
    }

}

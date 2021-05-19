/**
 * 
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.PutLevel;

/**
 * @author vinoth.ponnurangan
 *
 */
public class PutLevelUIForm {

    private PutLevel putLevel;

    private String putNameDesc;

    public PutLevel getPutLevel() {
	return putLevel;
    }

    public void setPutLevel(PutLevel putLevel) {
	this.putLevel = putLevel;
    }

    public String getPutNameDesc() {
	return putNameDesc;
    }

    public void setPutNameDesc(String putNameDesc) {
	this.putNameDesc = putNameDesc;
    }

}

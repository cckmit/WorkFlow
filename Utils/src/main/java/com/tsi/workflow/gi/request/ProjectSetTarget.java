//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.28 at 02:50:50 PM IST 
//

package com.tsi.workflow.gi.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "projName", "target" })
@XmlRootElement(name = "Project-SetTarget")
public class ProjectSetTarget {

    @XmlElement(name = "ProjName", required = true)
    protected String projName;
    @XmlElement(name = "Target", required = true)
    protected String target;

    /**
     * Gets the value of the projName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProjName() {
	return projName;
    }

    /**
     * Sets the value of the projName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProjName(String value) {
	this.projName = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTarget() {
	return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTarget(String value) {
	this.target = value;
    }

}

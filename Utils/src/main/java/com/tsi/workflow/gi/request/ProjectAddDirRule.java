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
@XmlType(name = "", propOrder = { "projName", "directory", "mask", "recurse" })
@XmlRootElement(name = "Project-AddDirRule")
public class ProjectAddDirRule {

    @XmlElement(name = "ProjName", required = true)
    protected String projName;
    @XmlElement(name = "Directory", required = true)
    protected Directory directory;
    @XmlElement(name = "Mask", required = true)
    protected String mask;
    @XmlElement(name = "Recurse", required = true)
    protected String recurse;

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
     * Gets the value of the directory property.
     * 
     * @return possible object is {@link Directory }
     * 
     */
    public Directory getDirectory() {
	return directory;
    }

    /**
     * Sets the value of the directory property.
     * 
     * @param value
     *            allowed object is {@link Directory }
     * 
     */
    public void setDirectory(Directory value) {
	this.directory = value;
    }

    /**
     * Gets the value of the mask property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMask() {
	return mask;
    }

    /**
     * Sets the value of the mask property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMask(String value) {
	this.mask = value;
    }

    /**
     * Gets the value of the recurse property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRecurse() {
	return recurse;
    }

    /**
     * Sets the value of the recurse property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRecurse(String value) {
	this.recurse = value;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import java.sql.Types;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 *
 * @author USER
 */
public class CoalesceOrder extends Order {

    private static final Logger LOG = Logger.getLogger(CoalesceOrder.class.getName());

    private String[] properties;

    protected CoalesceOrder(boolean ascending, String... properties) {
	super(Arrays.toString(properties), ascending);
	this.properties = properties;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {

	StringBuilder fragment = new StringBuilder();
	StringBuilder exp = new StringBuilder();
	fragment.append("COALESCE(");
	SessionFactoryImplementor factory = criteriaQuery.getFactory();
	for (int j = 0; j < this.properties.length; j++) {
	    String propertyName = this.properties[j];
	    String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
	    Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
	    StringBuilder fragForField = new StringBuilder();
	    for (int i = 0; i < columns.length; i++) {
		final StringBuilder expression = new StringBuilder();
		boolean lower = false;
		if (super.isIgnoreCase()) {
		    int sqlType = type.sqlTypes(factory)[i];
		    lower = sqlType == Types.VARCHAR || sqlType == Types.CHAR || sqlType == Types.LONGVARCHAR;
		}

		if (lower) {
		    expression.append(factory.getDialect().getLowercaseFunction()).append('(');
		}
		expression.append(columns[i]);
		if (lower) {
		    expression.append(')');
		}
		fragForField.append(expression.toString());
		if (i < columns.length - 1) {
		    fragForField.append(", ");
		}
	    }
	    exp.append(fragForField.toString());
	    if (j < properties.length - 1) {
		exp.append(", ");
	    }
	}
	exp.append(")");

	fragment.append(factory.getDialect().renderOrderByElement(exp.toString(), null, super.isAscending() ? "asc" : "desc", factory.getSettings().getDefaultNullPrecedence()));
	return fragment.toString();
    }

    public static Order asc(String... properties) {
	return new CoalesceOrder(true, properties);
    }

    public static Order desc(String... properties) {
	return new CoalesceOrder(false, properties);
    }
}

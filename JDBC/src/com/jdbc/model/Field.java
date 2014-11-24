package com.jdbc.model;

import java.sql.Types;

/**
 * Table Field
 * @author Administrator 
 * 2014年11月24日
 */
public class Field<E> {

	private String fieldName; // field name
	private E fieldValue; // field value
	private int type; // field type

	public Field(String fieldName) {
		this(fieldName, null);
	}

	public Field(String fieldName, E fieldValue) {
		this(fieldName, fieldValue, Types.VARCHAR);
	}

	public Field(String fieldName, E fieldValue, int type) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.type = type;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public E getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(E fieldValue) {
		this.fieldValue = fieldValue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

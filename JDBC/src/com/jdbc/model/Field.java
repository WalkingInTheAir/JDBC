package com.jdbc.model;

import java.sql.Types;

/**
 * Table Field
 * @author Administrator
 * 2014年11月24日
 */
public class Field<E> {
	
	private String fieldName;		//field name
	private E fieldValue;			//field value
	private Types type;				//field type

	public Field(String fieldName, E fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	
}

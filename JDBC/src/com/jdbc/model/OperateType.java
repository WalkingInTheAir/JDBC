package com.jdbc.model;

/**
 * DML type: DELETE/INSERT/SELECT/UPDATE
 * @author Administrator
 * 2014年11月24日
 */
public enum OperateType {

	DELETE(" DELETE "),
	INSERT(" INSERT "),
	SELECT(" SELECT "),
	UPDATE(" UPDATE ");

	private String value;

	private OperateType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}

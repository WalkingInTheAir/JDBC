package com.db.model;

import com.db.util.StringUtil;

/**
 * Database Table
 * @author Administrator
 * 2014年11月24日
 */
public class Table {

	private String tableName;		//table name
	private String alias;			//table alias
	private String schema;			//table schema
	public Table() {
		super();
	}
	public Table(String tableName, String alias, String schema) {
		super();
		this.tableName = tableName;
		this.alias = alias;
		this.schema = schema;
	}
	public Table(String tableName, String alias) {
		this(tableName, alias, null);
	}
	public Table(String tableName) {
		this(tableName, null);
	}
	
	public boolean hasSchema(){
		return StringUtil.isEmpty(this.schema);
	}
	
	public boolean hasAlias(){
		return StringUtil.isEmpty(this.alias);
	}
	
	@Override
	public String toString(){
		return "[Table:" + this.tableName + "]";
	}
}

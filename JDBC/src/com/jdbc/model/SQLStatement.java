package com.jdbc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL statement
 * @author Administrator
 * 2014年11月24日
 */
public class SQLStatement {
	
	private final String WHERE = " WHERE ";
	private OperateType opType;					//DML type
	private String tableName;					//table name;
	private List<Table> tables;					//table
	
	private StringBuffer sqlBuff;				//SQL statement 
	
	private SQLStatement(){
		super();
		tables = new ArrayList<Table>();
		sqlBuff = new StringBuffer();
	}
	
	public SQLStatement(OperateType opType){
		this();
		this.opType = opType;
	}
	
	public void addTable(Table table){
		this.tables.add(table);
	}
	
	public void setTable(Table table){
		this.tables.clear();
		this.tables.add(table);
	}
	
	
}

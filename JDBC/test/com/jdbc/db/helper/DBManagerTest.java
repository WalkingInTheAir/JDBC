package com.jdbc.db.helper;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.jdbc.db.converter.ConverterFactory;
import com.jdbc.db.converter.IResultSetConverter;
import com.jdbc.db.converter.impl.UserResultSetConverter;
import com.jdbc.test.bean.User;

public class DBManagerTest {

	@Test
	public void testList() {
		List<User> us = DBManager.queryToList(
				"select * from user", 
				null,
				new UserResultSetConverter());
		for (User u : us) {
			System.out.println(u);
		}
	}

	@Test
	public void testBean() {
		String password = DBManager.queryToBean(
				"select password from user where id = ?",
				new Object[] { 10 }, 
				new IResultSetConverter<String>() {
					@Override
					public String conver(ResultSet rs) throws SQLException {
						String password = null;
						while (rs.next()) {
							password = rs.getString(1);
						}
						return password;
					}
				});
		System.out.println(password);
	}

	@Test
	public void testSingleColumn() {
		String password = DBManager
				.queryToBean(
						"select password from user where id = ?",
						new Object[] { 1 },
						ConverterFactory.getConverter(String.class));
		System.out.println(password);
	}

	@Test
	public void testInsert(){
		int flag = DBManager.executeUpdate(
				"INSERT INTO USER VALUES(?,?,?)", 
				new Object[]{20, "张三", "12121212"});
		System.out.println(flag);
	}
	
	@Test
	public void testDelete(){
		int flag = DBManager.executeUpdate("delete from user where id=20", null);
		System.out.println(flag);
	}
}

package com.core.jdbc.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.core.jdbc.bean.Pagination;
import com.core.jdbc.bean.User;
import com.core.jdbc.converter.ConverterFactory;
import com.core.jdbc.converter.IResultSetConverter;
import com.core.jdbc.converter.impl.UserResultSetConverter;
import com.core.jdbc.helper.DBManager;

public class DBManagerTest {

	@Test
	public void testList() throws Exception {
		List<User> us = DBManager.queryToList(
				"select * from user", 
				null,
				new UserResultSetConverter());
		for (User u : us) {
			System.out.println(u);
		}
	}

	@Test
	public void testBean() throws Exception {
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
	public void testSingleColumn() throws Exception {
		String password = DBManager
				.queryToBean(
						"select password from user where id = ?",
						new Object[] { 1 },
						ConverterFactory.getConverter(String.class));
		System.out.println(password);
	}

	@Test
	public void testInsert() throws SQLException{
		int flag = DBManager.executeUpdate(
				"INSERT INTO USER VALUES(?,?,?)", 
				new Object[]{20, "张三", "12121212"});
		System.out.println(flag);
	}
	
	@Test
	public void testDelete() throws SQLException{
		int flag = DBManager.executeUpdate("delete from user where id=20", null);
		System.out.println(flag);
	}
	
	@Test
	public void testPagination1() throws Exception{
		Pagination<User> page = new Pagination<User>(3);
		page = DBManager.queryByPagination("select * from user where id>2 order by id",
					null, new UserResultSetConverter(), page);
		
		System.out.println("current page: " + page.getCurrPage());
		System.out.println("total page: " + page.getTotalPage());
		List<User> us = page.getContent();
		for(User u : us){
			System.out.println(u);
		}
	}
	
	@Test
	public void testPagination2() throws Exception{
		Pagination<User> page = new Pagination<User>(3);
		page = DBManager.queryByPagination("select * from user where id>? order by id",
					new Object[]{2}, new UserResultSetConverter(), page);
		
		System.out.println("current page: " + page.getCurrPage());
		System.out.println("total page: " + page.getTotalPage());
		List<User> us = page.getContent();
		for(User u : us){
			System.out.println(u);
		}
	}
	
	@Test
	public void testExecuteBatch1() throws SQLException{
		String sql = "insert into user values(null, 'abab', '1212')";
		DBManager.executeBatch(sql, null);
	}
	@Test
	public void testExecuteBatch2() throws SQLException{
		String sql = "insert into user values(null, ?, ?)";
		List<Object[]> list = new ArrayList<Object[]>();
		for(int i=0; i<50; i++){
			list.add(new Object[]{"a"+i, "b"+i});
		}
		
		DBManager.executeBatch(sql, list);
		
	}
	
	@Test 
	public void testExecuteBatch3() throws SQLException{
		String sql = "UPDATE USER SET name=?, password=? WHERE ID=?";
		List<Object[]> list = new ArrayList<Object[]>();
		
		for(int i=1; i<30; i++){
			list.add(new Object[]{"c"+i, "d"+i, i});
		}
		DBManager.executeBatch(sql, list);
		
	}
	
	@Test
	public void testExecuteBatch4() throws SQLException {
		String[] sqls = { "DELETE FROM USER WHERE ID = 2",
				"UPDATE USER SET NAME = ? WHERE ID=?",
				"INSERT INTO USER VALUES(NULL,?,?)" };

		List<Object[]> list = new ArrayList<Object[]>();
		list.add(null);
		list.add(new Object[] { "aaaaaaa", 3 });
		list.add(new Object[] { "张三", "0000000" });

		DBManager.executeBatch(sqls, list);
	}
}

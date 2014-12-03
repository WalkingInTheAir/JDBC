package com.jdbc.db.helper;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.util.List;

import org.junit.Test;




import com.jdbc.db.converter.impl.UserResultSetConverter;
import com.jdbc.test.bean.User;

public class DBManagerTest {

	@Test
	public void test() {
		ResultSet rs = DBManager.executeQuery("select * from user");
		List<User> us = DBManager.toList(rs, new UserResultSetConverter());
		for(User u : us){
			System.out.println(u);
		}
	}

}

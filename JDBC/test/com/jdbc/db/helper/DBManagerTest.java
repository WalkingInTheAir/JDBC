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
	public void test() {
		List<User> us = DBManager.queryToList("select * from t_user where user_id=?",new Object[]{1}, new UserResultSetConverter());
		for(User u : us){
			System.out.println(u);
		}
		
		String password = DBManager.queryToBean("select user_password from t_user where user_id = ?",
				new Object[]{1}, 
				new IResultSetConverter<String>(){
					@Override
					public String conver(ResultSet rs) throws SQLException {
						String password = null;
						while(rs.next()){
							password = rs.getString(1);
						}
						return password;
					}
				});
		System.out.println(password);
		
		String password2 = DBManager.queryToBean("select user_password from t_user where user_id = ?",
				new Object[]{1}, 
				ConverterFactory.getConverter(String.class));
		System.out.println(password2);
	}

}

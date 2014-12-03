package com.jdbc.db.converter.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jdbc.db.converter.IResultSetConverter;
import com.jdbc.test.bean.User;

public class UserResultSetConverter implements IResultSetConverter<User> {

	@Override
	public User conver(ResultSet rs) throws SQLException {
		if (null == rs) {
			return null;
		}
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setUsername(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		return user;
	}

}

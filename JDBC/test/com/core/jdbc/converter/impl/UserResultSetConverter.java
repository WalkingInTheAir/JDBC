package com.core.jdbc.converter.impl;

import java.sql.ResultSet;

import com.core.jdbc.bean.User;
import com.core.jdbc.converter.IResultSetConverter;

public class UserResultSetConverter implements IResultSetConverter<User> {

	@Override
	public User conver(ResultSet rs) throws Exception {
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

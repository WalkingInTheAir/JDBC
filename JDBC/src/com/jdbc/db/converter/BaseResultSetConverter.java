package com.jdbc.db.converter;

import java.sql.ResultSet;

public class BaseResultSetConverter<T> implements IResultSetConverter<T>{

	@Override
	public T conver(ResultSet rs) {
		return null;
	}

}

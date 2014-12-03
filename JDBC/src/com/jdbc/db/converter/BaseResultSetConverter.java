package com.jdbc.db.converter;

import java.sql.ResultSet;

public class BaseResultSetConverter<E> implements IResultSetConverter<E>{

	@Override
	public E conver(ResultSet rs) {
		return null;
	}

}

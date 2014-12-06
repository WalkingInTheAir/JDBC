package com.jdbc.db.converter;

import java.sql.ResultSet;

public interface IResultSetConverter<T> {
	public T conver(ResultSet rs) throws Exception;
}

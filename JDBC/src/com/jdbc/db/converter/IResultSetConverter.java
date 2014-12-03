package com.jdbc.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultSetConverter<T> {
	public T conver(ResultSet rs) throws SQLException;
}

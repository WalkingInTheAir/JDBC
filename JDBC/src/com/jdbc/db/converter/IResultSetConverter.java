package com.jdbc.db.converter;

import java.sql.ResultSet;

public interface IResultSetConverter<E> {
	public E conver(ResultSet rs);
}

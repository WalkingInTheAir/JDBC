package com.jdbc.db.converter;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConverterFactory {

	public static IResultSetConverter<String> getStringConverter() {

		return new IResultSetConverter<String>() {
			@Override
			public String conver(ResultSet rs) throws SQLException {
				String str = null;
				while (rs.next()) {
					str = rs.getString(1);
					break;
				}
				return str;
			}
		};
	}

	public static IResultSetConverter<Long> getLongConverter() {
		return new IResultSetConverter<Long>() {

			@Override
			public Long conver(ResultSet rs) throws SQLException {
				Long lo = null;
				while(rs.next()){
					lo = rs.getLong(1);
					break;
				}
				return lo;
			}
		};
	}
	
	/**
	 * 
	 * @param cls
	 * @return
	 */
	public static <T> IResultSetConverter<T> getConverter(final Class<T> cls){
		return new IResultSetConverter<T>(){
			@SuppressWarnings("unchecked")
			@Override
			public T conver(ResultSet rs) throws SQLException {
				T t = null;
				try {
					Method m = rs.getClass().getMethod(getMethodName(cls),
							int.class);
					while (rs.next()) {
						t = (T) m.invoke(rs, 1);
						break;
					}
				} catch (Exception e) {
					t = null;
					e.printStackTrace();
				}
				return t;
			}
		};
	}
	
	
	/**
	 * build function name: getXXX
	 * @param cls
	 * @return
	 */
	public static <T> String getMethodName(Class<T> cls){
		return "get" + classToString(cls);
	}
	/**
	 * 将Class类型转成String
	 * @param cls
	 * @return
	 */
	public static <T> String classToString(Class<T> cls) {
		String type = null;
		if (cls == Object.class) {
			type = "Object";
		} else if (cls == String.class) {
			type = "String";
		} else if (cls == Integer.class) {
			type = "Int";
		} else if (cls == Float.class) {
			type = "Float";
		} else if (cls == Long.class) {
			type = "Long";
		}
		if (null == type) {
			type = "Object";
		}
		return type;
	}
}

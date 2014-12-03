package com.jdbc.db.helper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jdbc.db.converter.IResultSetConverter;

/**
 * 数据库操作工具类
 * @author Administrator 
 * 2014年12月3日
 */
public class DBManager {

	private static String driverClassName; // 数据库驱动全限定类名
	private static String url; // 数据库连接url
	private static String username; // 数据库用户名
	private static String password; // 数据库密码

	static {
		Properties pro = new Properties();
		try {
			// 读取并解析数据库配置文件
			pro.load(DBManager.class.getResourceAsStream("/db.properties"));
			driverClassName = pro.getProperty("jdbc.driverClassName");
			url = pro.getProperty("jdbc.url");
			username = pro.getProperty("jdbc.username");
			password = pro.getProperty("jdbc.password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			// 加载driver class
			Class.forName(driverClassName);
			// 获取数据库连接
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭资源
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param stmt
	 *            事务集对象
	 * @param rs
	 *            结果集对象
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		closeRs(rs);
		closeStmt(stmt);
		closeConn(conn);
	}

	/**
	 * 关闭数据库连接对象
	 * 
	 * @param conn
	 */
	public static void closeConn(Connection conn) {
		try {
			if (null != conn) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭事务集对象
	 * 
	 * @param stmt
	 */
	public static void closeStmt(Statement stmt) {
		try {
			if (null != stmt) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭结果集对象
	 * 
	 * @param rs
	 */
	public static void closeRs(ResultSet rs) {
		try {
			if (null != rs) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * insert/update/delete 操作通用方法
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int executeUpdate(String sql, Object[] params) {
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			if(null != params){
				int index = 1;
				for (Object p : params) {
					pstmt.setObject(index++, p);
				}
			}
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, null);
		}

		return result;
	}
	
	/**
	 * insert/update/delete 操作通用方法，无需指定参数
	 * @param sql
	 * @return
	 */
	public static int executeUpdate(String sql){
		return executeUpdate(sql, null);
	}
	
	/**
	 * 查询通用方法
	 * @param sql
	 * @param params
	 * @return
	 */
	public static ResultSet executeQuery(String sql, Object[] params) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			if(null != params){
				int index = 1;
				for (Object p : params) {
					pstmt.setObject(index++, p);
				}
			}
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//close(conn, pstmt, null);
		}

		return rs;
	}
	/**
	 * 查询通用方法，无需参数
	 * @param sql
	 * @return
	 */
	public static ResultSet executeQuery(String sql) {
		return executeQuery(sql, null);
	}
	
	/**
	 * 将结果集转成JavaBean
	 * @param rs
	 * @param converter
	 * @return
	 */
	public static <T> T toBean(ResultSet rs, IResultSetConverter<T> converter) {
		T t = null;
		try {
			t = converter.conver(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 将结果集转成集合
	 * @param rs
	 * @param converter
	 * @return
	 */
	public static <T> List<T> toList(ResultSet rs, IResultSetConverter<T> converter){
		List<T> list = new ArrayList<T>();
		try {
			while(rs.next()){
				list.add(toBean(rs, converter));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public static <T> Object queryToBean(String sql, Object[] params, IResultSetConverter<T> converter){
		return query(true, sql, params, converter);
	}
	
	private static <T> Object query(boolean isToBean, String sql, Object[] params, IResultSetConverter<T> converter){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			if(null != params){
				int index = 1;
				for (Object p : params) {
					pstmt.setObject(index++, p);
				}
			}
			rs = pstmt.executeQuery();
			if(isToBean){
				return converter.conver(rs);
			}else{
				List<T> list = new ArrayList<T>();
				while(rs.next()){
					list.add(converter.conver(rs));
				}
				return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}

		return null;
	}
	
	
	
}

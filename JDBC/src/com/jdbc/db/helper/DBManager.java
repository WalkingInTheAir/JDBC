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

import com.jdbc.db.bean.Pagination;
import com.jdbc.db.converter.ConverterFactory;
import com.jdbc.db.converter.IResultSetConverter;

/**
 * 数据库操作工具类
 * @author Administrator 
 * 2014年12月3日
 */
public class DBManager {

	private static String driverClassName; 	// 数据库驱动全限定类名
	private static String url; 				// 数据库连接url
	private static String username; 		// 数据库用户名
	private static String password; 		// 数据库密码

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
	 * @param conn 数据库连接对象
	 * @param stmt 事务集对象
	 * @param rs   结果集对象
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		closeRs(rs);
		closeStmt(stmt);
		closeConn(conn);
	}

	/**
	 * 关闭数据库连接对象
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
	 * 单记录查询
	 * @param sql
	 * @param params
	 * @param converter
	 * @return
	 */
	public static <T> T queryToBean(String sql, Object[] params, IResultSetConverter<T> converter){
		T t = null;
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
			t = converter.conver(rs);
		} catch (SQLException e) {
			t = null;
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}

		return t;
	}
	
	/**
	 * 单记录无动态参数查询
	 * @param sql
	 * @param converter
	 * @return
	 */
	public static <T> T queryToBean(String sql, IResultSetConverter<T> converter){
		return queryToBean(sql, null, converter);
	}
	
	/**
	 * 多记录查询
	 * @param sql
	 * @param params
	 * @param converter
	 * @return
	 */
	public static <T> List<T> queryToList(String sql, Object[] params, IResultSetConverter<T> converter){
		List<T> list = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			if (null != params) {
				int index = 1;
				for (Object p : params) {
					pstmt.setObject(index++, p);
				}
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(converter.conver(rs));
			}
		} catch (SQLException e) {
			list = null;
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return list;
	}
	
	/**
	 * 多记录无动态参数查询
	 * @param sql
	 * @param converter
	 * @return
	 */
	public static <T> List<T> queryToList(String sql, IResultSetConverter<T> converter){
		return queryToList(sql, null, converter);
	}
	
	/**
	 * 分页查询
	 * @param sql
	 * @param params
	 * @param converter
	 * @param page
	 * @return
	 */
	public static <T> Pagination<T> queryByPagination(String sql, Object[] params,
			IResultSetConverter<T> converter, Pagination<T> page) {
		String totalSql = "SELECT COUNT(*) FROM (" + sql + ") AS a";
		int totalRecords = queryToBean(totalSql, params, ConverterFactory.getConverter(Integer.class));
		int totalPage = (totalRecords-1)/page.getPageSize()+1;
		page.setTotalPage(totalPage);
		sql += " LIMIT ?, ?";
		Object[] newParams = null;
		int currRecord = page.getCurrPage()*page.getPageSize();
		int endRecord = currRecord + page.getPageSize();
		if(null == params){
			newParams = new Object[]{currRecord, endRecord};
		}else{
			int len = params.length;
			newParams = new Object[len+2];
			System.arraycopy(params, 0, newParams, 0, len);
			newParams[len] = currRecord;
			newParams[len+1] = endRecord;
		}
		page.setContent(queryToList(sql, newParams, converter));
		page.setCurrPage(page.getCurrPage()+1);
		return page;
	}
	
	/**
	 * 无动态参数分页查询
	 * @param sql
	 * @param converter
	 * @param page
	 * @return
	 */
	public static <T> Pagination<T> queryByPagination(String sql,
			IResultSetConverter<T> converter, Pagination<T> page) {
		return queryByPagination(sql, converter, page);
	}
}

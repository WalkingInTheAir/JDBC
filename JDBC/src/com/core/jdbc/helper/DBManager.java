package com.core.jdbc.helper;

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

import com.core.jdbc.bean.PageContent;
import com.core.jdbc.bean.PageInfo;
import com.core.jdbc.converter.ConverterFactory;
import com.core.jdbc.converter.IResultSetConverter;

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
	 * @throws SQLException 
	 */
	public static int executeUpdate(String sql, Object[] params) throws SQLException {
		if(null == sql || sql.trim().length() == 0){
			return -1;
		}
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			if(null != params){
				int index = 1;
				for (Object p : params) {
					pstmt.setObject(index++, p);
				}
			}
			result = pstmt.executeUpdate();
			conn.commit();
			System.out.println("******SQL LOG******" + sql);
		} catch (SQLException e) {
			System.out.println("******SQL LOG****** ROLLBACK");
			conn.rollback();
			throw e;
		} finally {
			close(conn, pstmt, null);
		}

		return result;
	}
	
	/**
	 * 批量insert/update/delete: sql语句一样，参数不一样
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static boolean executeBatch(String sql, List<Object[]> params) throws SQLException{
		if(null == sql || sql.trim().length()==0){
			return  false;
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			if(null != params){
				for(int i=0, len=params.size(); i<len; i++){
					int index = 1;
					Object[] os = params.get(i);
					for (Object o : os) {
						pstmt.setObject(index++, o);
					}
					pstmt.addBatch();
					if((i+1)%30 == 0){			//防止内存溢出
						pstmt.executeBatch();
						pstmt.clearBatch();
					}
				}
			}else{
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			System.out.println("******SQL LOG******" + params.size() + " times: " + sql);
		} catch (SQLException e) {
			System.out.println("******SQL LOG****** ROLLBACK");
			conn.rollback();
			throw e;
		} finally {
			close(conn, pstmt, null);
		}
		return true;
	}
	
	/**
	 * 批量insert/update/delete: sql语句不一样
	 * @param sqls
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static boolean executeBatch(String[] sqls, List<Object[]> params) throws SQLException{
		if(null == sqls){
			return false;
		}
		int len = sqls.length;
		if(len == 0 || params.size() != len){
			return false;
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			for(int i=0; i<len; i++){
				String sql = sqls[i];
				pstmt = conn.prepareStatement(sql);
				Object[] os = params.get(i);
				if(null != os){
					int index = 1;
					for (Object o : os) {
						pstmt.setObject(index++, o);
					}
				}
				pstmt.executeUpdate();
			}
			conn.commit();
			for(int i=0; i<len; i++){
				System.out.println("******SQL LOG******" + sqls[i]);
			}
		} catch (SQLException e) {
			System.out.println("******SQL LOG****** ROLLBACK");
			conn.rollback();
			throw e;
		} finally {
			close(conn, pstmt, null);
		}
		return true;
	}
	/**
	 * 单记录查询
	 * @param sql
	 * @param params
	 * @param converter
	 * @return
	 * @throws Exception 
	 */
	public static <T> T queryToBean(String sql, Object[] params, IResultSetConverter<T> converter) throws Exception{
		if (null == sql || sql.trim().length() == 0 || null == converter) {
			return null;
		}
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
			while(rs.next()){
				t = converter.conver(rs);
				break;
			}
			//System.out.println("******SQL LOG******" + sql);
		} catch (SQLException e) {
			System.out.println("******SQL LOG****** EXCEPTION");
			t = null;
			throw e;
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
	 * @throws Exception 
	 */
	public static <T> T queryToBean(String sql, IResultSetConverter<T> converter) throws Exception{
		return queryToBean(sql, null, converter);
	}
	
	/**
	 * 多记录查询
	 * @param sql
	 * @param params
	 * @param converter
	 * @return
	 * @throws Exception 
	 */
	public static <T> List<T> queryToList(String sql, Object[] params, IResultSetConverter<T> converter) throws Exception{
		if (null == sql || sql.trim().length() == 0 || null == converter) {
			return null;
		}
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
			System.out.println("******SQL LOG******" + sql);
		} catch (SQLException e) {
			System.out.println("******SQL LOG****** EXCEPTION");
			list = null;
			throw e;
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
	 * @throws Exception 
	 */
	public static <T> List<T> queryToList(String sql, IResultSetConverter<T> converter) throws Exception{
		return queryToList(sql, null, converter);
	}
	
	/**
	 * 分页查询
	 * @param sql
	 * @param params
	 * @param converter
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	public static <T> PageContent<T> queryByPagination(String sql, Object[] params,
			IResultSetConverter<T> converter, PageInfo page) throws Exception {
		if (null == sql || sql.trim().length() == 0 || null == converter) {
			return null;
		}
		PageContent<T> content = new PageContent<T>();
		String totalSql = "SELECT COUNT(*) FROM (" + sql + ") AS a";
		int totalRecords = queryToBean(totalSql, params, ConverterFactory.getConverter(Integer.class));
		if(null == page){
			page = new PageInfo();
		}
		int totalPage = totalRecords % page.getPageSize() == 0 ? 
				totalRecords / page.getPageSize() : 
				totalRecords / page.getPageSize() + 1;
		page.setTotalPage(totalPage);
		sql += " LIMIT ?, ?";
		if(page.getCurrPage() > totalPage && totalPage > 0){
			page.setCurrPage(totalPage);
		}
		Object[] newParams = null;
		int currRecordIndex = (page.getCurrPage() - 1)*page.getPageSize();
		int endRecordIndex = currRecordIndex + page.getPageSize();
		if(null == params){
			newParams = new Object[]{currRecordIndex, endRecordIndex};
		}else{
			int len = params.length;
			newParams = new Object[len+2];
			System.arraycopy(params, 0, newParams, 0, len);
			newParams[len] = currRecordIndex;
			newParams[len+1] = endRecordIndex;
		}
		content.setContent(queryToList(sql, newParams, converter));
		page.setCurrPage(page.getCurrPage());
		content.setPage(page);
		return content;
	}
	
	/**
	 * 无动态参数分页查询
	 * @param sql
	 * @param converter
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	public static <T> PageContent<T> queryByPagination(String sql,
			IResultSetConverter<T> converter, PageInfo page) throws Exception {
		return queryByPagination(sql, null, converter, page);
	}
	
}

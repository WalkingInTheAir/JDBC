package com.jdbc.db.bean;

import java.util.List;
/**
 * 分页bean
 * @author Administrator
 * @param <T>
 */
public class Pagination<T> {
	
	private int currPage;			//当前页
	private int totalPage;			//总页数
	private int pageSize;			//每页记录数
	private List<T> content;		//内容
	
	public Pagination(int pageSize){
		this(pageSize, 0);
	}
	public Pagination(int pageSize, int currPage){
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = 0;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
}

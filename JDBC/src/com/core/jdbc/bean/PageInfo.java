package com.core.jdbc.bean;

import net.sf.json.JSONObject;


public class PageInfo {
	private int currPage = 1; // 当前页
	private int totalPage = 1; // 总页数
	private int pageSize = 10; // 每页记录数
	
	public PageInfo(){
		
	}
	public PageInfo(int currPage) {
		this.currPage = currPage;
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
	
	public boolean isFirstPage(){
		return this.currPage == 1;
	}
	
	public boolean isLastPage(){
		return this.currPage == this.pageSize;
	}
	
	public JSONObject toJSONObj(){
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("currPage", this.currPage);
		jsonObj.put("pageSize", this.pageSize);
		jsonObj.put("totalPage", this.totalPage);
		jsonObj.put("isFirstPage", isFirstPage());
		jsonObj.put("isLastPage", isLastPage());
		return jsonObj;
	}
}

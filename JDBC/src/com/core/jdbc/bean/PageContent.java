package com.core.jdbc.bean;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 分页bean
 * @author Administrator
 * @param <T>
 */
public class PageContent<T> {
	private List<T> content;		//内容
	private PageInfo page;			//分页信息
	
	public void setCurrPage(int currPage){
		if(null == page){
			page = new PageInfo();
		}
		page.setCurrPage(currPage);
	}
	
	public void setTotalPage(int totalPage){
		if(null == page){
			page = new PageInfo();
		}
		page.setTotalPage(totalPage);
	}
	
	public void setPageSize(int pageSize){
		if(null == page){
			page = new PageInfo();
		}
		page.setPageSize(pageSize);
	}
	
	public PageInfo getPage() {
		return page;
	}
	public void setPage(PageInfo page) {
		this.page = page;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	
	public JSONObject toJSONObj(){
		JSONObject json = new JSONObject();
		json.put("pageInfo", this.page);
		json.put("content", JSONArray.fromObject(this.content));
		return json;
	}
}

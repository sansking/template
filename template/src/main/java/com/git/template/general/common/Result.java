package com.git.template.general.common;

import java.io.Serializable;
import java.util.List;

public class Result<E> implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private List<E> dataList;
	private int code;
	private String msg;
	
	public Result(List<E> dataList, int code, String msg) {
		super();
		this.dataList = dataList;
		this.code = code;
		this.msg = msg;
	}

	public List<E> getDataList() {
		return dataList;
	}

	public void setDataList(List<E> dataList) {
		this.dataList = dataList;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}

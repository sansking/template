package com.git.template.general.netty.msg;

import java.io.Serializable;

/**
 * 响应码枚举类 Created by 叶云轩 on 2017/6/13-11:53 Concat tdg_yyx@foxmail.com
 */
public enum ResponseCodeEnum implements Serializable {

	// region authentication code
	REQUEST_SUCCESS(10000, "请求成功"), SERVER_ERROR(99999, "服务器内部错误"), ;

	// region 提供对外访问的方法,无需更改
	/**
	 * 响应码
	 */
	private Integer code;
	/**
	 * 响应信息
	 */
	private String msg;

	ResponseCodeEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	// endregion
}
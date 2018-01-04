package com.andy.myproject_007.bean;

import java.io.Serializable;


/**
 * @author leeandy007
 * @date:2015-6-6 下午02:48:06
 * @Desc: 响应android客户端抽象类
 * @version :
 * @param <T>
 *
 */
public abstract class BaseAndroidResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 返回码1:成功；0:失败
	 * */
	private Integer code;

	/**
	 * 数据信息
	 */
	private T data;

	/**
	 * 提示信息
	 * */
	private String message;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

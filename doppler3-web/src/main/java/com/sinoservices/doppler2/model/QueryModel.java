package com.sinoservices.doppler2.model;

import java.io.Serializable;

public class QueryModel implements Serializable {

	private static final long serialVersionUID = -1996290050126917947L;
	private String id;
	private String logInfo;
	private String color;

	public QueryModel(String id, String logInfo, String color) {
		this.id = id;
		this.logInfo = logInfo;
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}

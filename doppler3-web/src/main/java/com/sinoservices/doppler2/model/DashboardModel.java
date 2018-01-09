package com.sinoservices.doppler2.model;

import java.io.Serializable;
import java.util.List;

import com.sinoservices.doppler2.bo.ErrorDetail;
import com.sinoservices.doppler2.bo.ReqDetail;

public class DashboardModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8509524525290022274L;
	private int appNum;
	private int moduleNum;
	private int appReqNum;
	private int moduleReqNum;
	private int errorNum;
	private List<ReqDetail> appReqList;
	private List<ReqDetail> moduleReqList;
	private List<ErrorDetail> errorList;
	private List<ReqDetail> errorStat;

	public int getAppNum() {
		return appNum;
	}

	public void setAppNum(int appNum) {
		this.appNum = appNum;
	}

	public int getModuleNum() {
		return moduleNum;
	}

	public void setModuleNum(int moduleNum) {
		this.moduleNum = moduleNum;
	}

	public int getAppReqNum() {
		return appReqNum;
	}

	public void setAppReqNum(int appReqNum) {
		this.appReqNum = appReqNum;
	}

	public int getModuleReqNum() {
		return moduleReqNum;
	}

	public void setModuleReqNum(int moduleReqNum) {
		this.moduleReqNum = moduleReqNum;
	}

	public int getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(int errorNum) {
		this.errorNum = errorNum;
	}

	public List<ReqDetail> getAppReqList() {
		return appReqList;
	}

	public void setAppReqList(List<ReqDetail> appReqList) {
		this.appReqList = appReqList;
	}

	public List<ReqDetail> getModuleReqList() {
		return moduleReqList;
	}

	public void setModuleReqList(List<ReqDetail> moduleReqList) {
		this.moduleReqList = moduleReqList;
	}

	public List<ErrorDetail> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ErrorDetail> errorList) {
		this.errorList = errorList;
	}

	public List<ReqDetail> getErrorStat() {
		return errorStat;
	}

	public void setErrorStat(List<ReqDetail> errorStat) {
		this.errorStat = errorStat;
	}

}

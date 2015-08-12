package com.hseya.medtech.jobcard.bean;

import java.util.ArrayList;

public class JobCardDataBean {
	private String jobCardNO;
	private String jobDate;
	private String customer;
	private String customerID;
	private String equcment;
	private String equcmentID;
	private String visitReason;
	private String service;
	private String partUsed;
	private String filamnetCounter;
	private String hvCounter;
	private String logTime;
	private String downTime;
	private String emailContent;
	//	private EngineerDataBean engineer1;
	//	private EngineerDataBean engineer2;
	//	private EngineerDataBean engineer3;
	//	private EngineerDataBean engineer4;
	//	private EngineerDataBean engineer5;
	//	private EngineerDataBean engineer6;
	private String visualCheck;
	private String inspectionCheck;
	private String customerSignPath;
	private String engineerSignPath;

	private String lastUpdatedate;
	private String engineerUser;



	private ArrayList<EngineerDataBean> engBean;
	private ArrayList<PartsBean> partBean;

	public String getJobDate() {
		return jobDate;
	}
	public void setJobDate(String jobDate) {
		this.jobDate = jobDate;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getEqucment() {
		return equcment;
	}
	public void setEqucment(String equcment) {
		this.equcment = equcment;
	}
	public String getVisitReason() {
		return visitReason;
	}
	public void setVisitReason(String visitReason) {
		this.visitReason = visitReason;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPartUsed() {
		return partUsed;
	}
	public void setPartUsed(String partUsed) {
		this.partUsed = partUsed;
	}
	public String getFilamnetCounter() {
		return filamnetCounter;
	}
	public void setFilamnetCounter(String filamnetCounter) {
		this.filamnetCounter = filamnetCounter;
	}
	public String getHvCounter() {
		return hvCounter;
	}
	public void setHvCounter(String hvCounter) {
		this.hvCounter = hvCounter;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public String getJobCardNO() {
		return jobCardNO;
	}
	public void setJobCardNO(String jobCardNO) {
		this.jobCardNO = jobCardNO;
	}


	public String getCustomerSignPath() {
		return customerSignPath;
	}
	public void setCustomerSignPath(String customerSignPath) {
		this.customerSignPath = customerSignPath;
	}
	public String getEngineerSignPath() {
		return engineerSignPath;
	}
	public void setEngineerSignPath(String engineerSignPath) {
		this.engineerSignPath = engineerSignPath;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getEqucmentID() {
		return equcmentID;
	}
	public void setEqucmentID(String equcmentID) {
		this.equcmentID = equcmentID;
	}

	public ArrayList<EngineerDataBean> getEngBean() {
		return engBean;
	}
	public void setEngBean(ArrayList<EngineerDataBean> engBean) {
		this.engBean = engBean;
	}
	public String getEngineerUser() {
		return engineerUser;
	}
	public void setEngineerUser(String engineerUser) {
		this.engineerUser = engineerUser;
	}
	public String getLastUpdatedate() {
		return lastUpdatedate;
	}
	public void setLastUpdatedate(String lastUpdatedate) {
		this.lastUpdatedate = lastUpdatedate;
	}
	public ArrayList<PartsBean> getPartBean() {
		return partBean;
	}
	public void setPartBean(ArrayList<PartsBean> partBean) {
		this.partBean = partBean;
	}
	public String getEmailContent() {
		return emailContent;
	}
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	public String getVisualCheck() {
		return visualCheck;
	}
	public void setVisualCheck(String visualCheck) {
		this.visualCheck = visualCheck;
	}
	public String getInspectionCheck() {
		return inspectionCheck;
	}
	public void setInspectionCheck(String inspectionCheck) {
		this.inspectionCheck = inspectionCheck;
	}





}

package com.hseya.medtech.utill;

import java.util.ArrayList;

import com.hseya.medtech.jobcard.bean.CustomerBean;
import com.hseya.medtech.jobcard.bean.EngineerGetBean;
import com.hseya.medtech.jobcard.bean.EquickmentBean;
import com.hseya.medtech.jobcard.bean.JobCardDataBean;
import com.hseya.medtech.jobcard.bean.PartsBean;

public class VarList {



	public static String USER_NAME="";
	public static String LOGINUSER_ID="";



//	public static final String URL ="http://10.0.2.2/jobcards/WS/";
	public static final String URL ="http://www.medtech.com.au/jobcards/WS/";
	
	
	public static ArrayList<CustomerBean> CUSTOMER_LIST=null;

	public static ArrayList<EquickmentBean> EQCIPMENTS_LIST=null;

	public static ArrayList<EngineerGetBean> ENGINEER_LIST      = null;


	public static String SELECTED_ID="";
	public static String SELECTED_ID_VAL="";

	public static JobCardDataBean JOB_BEAN;
	public static ArrayList<PartsBean> PART_BEAN;
	public static JobCardDataBean UNFINISHED_JOB_BEAN;
	public static JobCardDataBean UPLOADBEAN=null;


	public static String JOBCARD_DATE=""; 
	public static String SELETCED_BTN=""; 

	public static String USERNAME=""; 
	public static String PASSWORD=""; 

	public static boolean ISVALIDLOGIN=false; 

}

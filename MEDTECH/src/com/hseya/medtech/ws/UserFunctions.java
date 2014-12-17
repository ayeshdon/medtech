package com.hseya.medtech.ws;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hseya.medtech.jobcard.bean.EngineerDataBean;
import com.hseya.medtech.jobcard.bean.JobCardDataBean;
import com.hseya.medtech.jobcard.bean.PartsBean;
import com.hseya.medtech.utill.Base64;
import com.hseya.medtech.utill.ConstList;
import com.hseya.medtech.utill.VarList;


public class UserFunctions {

	private JSONParser jsonParser;


	private static final String ITEM                = "item";
	private static final String LOGIN               = "login";
	private static final String CUSTOMER            = "customer";
	private static final String ENGINEER            = "engineer";
	private static final String ENGINEER_UPLOAD     = "engineer_upload";
	private static final String PART                = "part";
	private static final String JOBCARD_PDF         = "pdf";
	private static final String UPLOAD              = "upload";

	//	private static String register_tag = "register";
	//	private static String addads_tag = "addadsa";

	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}

	/**
	 * function get item list form server Request
	 * @param email
	 * @param password
	 * */
	public JSONObject getItemListFunction(){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(ConstList.TAG, ITEM));

		JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);

		return jsonlocal;
	}
	/**
	 * function get login value form server Request
	 * @param email
	 * @param password
	 * */
	public JSONObject getLoginValueFunction(String userName,String password){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(ConstList.TAG, LOGIN));
		params.add(new BasicNameValuePair("name", userName));
		params.add(new BasicNameValuePair("password", password));

		JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);

		return jsonlocal;
	}
	/**
	 * function get Customer list form server Request
	 * @param email
	 * @param password
	 * */
	public JSONObject getCustomerListFunction(){

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(ConstList.TAG, CUSTOMER));

			JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);

			return jsonlocal;
		} catch (Exception e) {
			return null;
		}


	}
	/**
	 * function get Customer list form server Request
	 * @param email
	 * @param password
	 * */
	public JSONObject getEngineerListFunction(){

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(ConstList.TAG, ENGINEER));

			JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);

			return jsonlocal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}
	/**
	 * upload engineers detail to remote database 
	 * */
	public JSONObject uploadEngineerDetail(EngineerDataBean bean,String ID,String index){

		try {


			System.out.println("sign path : "+ bean.getSignPath()); 
			Bitmap bitmapCus = BitmapFactory.decodeFile(bean.getSignPath()); 
			ByteArrayOutputStream streamCus = new ByteArrayOutputStream();
			bitmapCus.compress(Bitmap.CompressFormat.PNG, 90, streamCus); //compress to which format you want.
			byte [] byte_arrCus = streamCus.toByteArray();
			String imagePath = Base64.encodeBytes(byte_arrCus);





			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(ConstList.TAG, ENGINEER_UPLOAD));
			params.add(new BasicNameValuePair(ConstList.JOBNO, ID));
			params.add(new BasicNameValuePair(ConstList.WORKTIME, bean.getWorkedHours()));
			params.add(new BasicNameValuePair(ConstList.DATECREATED, bean.getJobStart()));
			params.add(new BasicNameValuePair(ConstList.TRAVELTIME, bean.getTravelTime()));
			params.add(new BasicNameValuePair(ConstList.JOBFINISHED, bean.getJobFinished()));
			params.add(new BasicNameValuePair(ConstList.ENG_ID, bean.getEngID()));
			params.add(new BasicNameValuePair(ConstList.ENG_SIGN, imagePath));

			//			System.out.println("image path : "+bean.getSignPath()); 

			if (!bean.getSignPath().equals("")) {
				params.add(new BasicNameValuePair(ConstList.ENG_SIGN_NAME, ID+index));
			}else{
				params.add(new BasicNameValuePair(ConstList.ENG_SIGN_NAME,""));
			}

			//			System.out.println(" engineer ID : "+bean.getEngID()); 
			//			System.out.println(" engineer sign  : "+imagePath); 
			params.add(new BasicNameValuePair(ConstList.DATE, ""));

			JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);

			return jsonlocal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}
	
	
	
	/*
	 * create pdf in server 
	 * */
	public JSONObject createPDF(String ID,String engID){

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(ConstList.TAG, JOBCARD_PDF));
			params.add(new BasicNameValuePair(ConstList.JOBNO, ID));
//			params.add(new BasicNameValuePair(ConstList.JOBCARD_ENGINEER, ""));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_ENGINEER_NAME, VarList.USER_NAME));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_ENGINEER, engID));


			JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);

			return jsonlocal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}





	/**
	 * insert job card details to remote database
	 * */
	public JSONObject uploadJobCard(JobCardDataBean bean){

		try {

			//			System.out.println("sign path final eng: "+ bean.getEngineerSignPath()); 
			//			System.out.println("sign path final cus: "+ bean.getCustomerSignPath()); 

			//			Bitmap bitmap = BitmapFactory.decodeFile(bean.getEngineerSignPath()); 
			//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//			bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
			//			byte [] byte_arr = stream.toByteArray();
			//			String image_str = Base64.encodeBytes(byte_arr);

			Bitmap bitmapCus = BitmapFactory.decodeFile(bean.getCustomerSignPath()); 
			ByteArrayOutputStream streamCus = new ByteArrayOutputStream();
			bitmapCus.compress(Bitmap.CompressFormat.PNG, 90, streamCus); //compress to which format you want.
			byte [] byte_arrCus = streamCus.toByteArray();
			String image_strCus = Base64.encodeBytes(byte_arrCus);



			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(ConstList.TAG, UPLOAD));

			//			System.out.println(" Engineer ID : "+  bean.getEngineerUser());

			System.out.println("upload user id : "+bean.getEngineerUser()); 
			System.out.println("upload user name : "+VarList.USER_NAME); 

			params.add(new BasicNameValuePair(ConstList.JOBCARD_ENGINEER,        bean.getEngineerUser()));
//			params.add(new BasicNameValuePair(ConstList.JOBCARD_ENGINEER,        ""));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_ENGINEER_NAME,   VarList.USER_NAME));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_CREATEDATE,      bean.getJobDate()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_CUSTOMER,        bean.getCustomerID()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_EQUIPMENT,       bean.getEqucmentID()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_OREDERNO,        bean.getJobCardNO()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_VISITREASON,     bean.getVisitReason()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_SERVICEDETAILS,  bean.getService()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_PARTUSED,        bean.getPartUsed()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_FILAMCOUNTER,    bean.getFilamnetCounter()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_HVCOUNTER,       bean.getHvCounter()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_LOGTIME,         bean.getLogTime()  ));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_DOUNTIME,        bean.getDownTime()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_EMAIL,           bean.getEmailContent()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_FINIDHDATE,      bean.getLastUpdatedate()));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_CUSTOMERSIGN,    image_strCus));
			params.add(new BasicNameValuePair(ConstList.JOBCARD_ENGINEERSIGN,    bean.getEngineerSignPath()));


			if (bean.getVisualCheck()==null) {
				params.add(new BasicNameValuePair(ConstList.JOBCARD_VISUAL,         "NO"));
			}else{
				params.add(new BasicNameValuePair(ConstList.JOBCARD_VISUAL,         "YES"));
			}

			if (bean.getInspectionCheck()==null) {
				params.add(new BasicNameValuePair(ConstList.JOBCARD_INSPECTION,      "NO"));
			}else{
				params.add(new BasicNameValuePair(ConstList.JOBCARD_INSPECTION,      "YES"));
			}

			//			params.add(new BasicNameValuePair("image",    image_str));
			//			params.add(new BasicNameValuePair("imageCUS",    image_strCus));


			JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);


			//			this.uploadFile(bean.getEngineerSignPath(),"123467");

			return jsonlocal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}



	/**
	 * upload part  detail to remote database 
	 * */
	public JSONObject uploadPartDetail(PartsBean bean,String ID){

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(ConstList.TAG, PART));
			params.add(new BasicNameValuePair(ConstList.JOBNO, ID));
			params.add(new BasicNameValuePair(ConstList.PART_NAME, bean.getPartName()));
			params.add(new BasicNameValuePair(ConstList.PART_ID, bean.getPartID()));
			params.add(new BasicNameValuePair(ConstList.PART_QUANTITY, bean.getPartQuantitys()));

			//			System.out.println(" part name: "+bean.getPartName()); 
			params.add(new BasicNameValuePair(ConstList.DATE, ""));

			JSONObject jsonlocal = jsonParser.getJSONFromUrl(VarList.URL, params);

			return jsonlocal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}

}

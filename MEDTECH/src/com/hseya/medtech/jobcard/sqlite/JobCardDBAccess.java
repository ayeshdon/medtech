package com.hseya.medtech.jobcard.sqlite;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hseya.medtech.jobcard.bean.CustomerBean;
import com.hseya.medtech.jobcard.bean.EngineerDataBean;
import com.hseya.medtech.jobcard.bean.EngineerGetBean;
import com.hseya.medtech.jobcard.bean.EquickmentBean;
import com.hseya.medtech.jobcard.bean.ItemsBean;
import com.hseya.medtech.jobcard.bean.JobCardDataBean;
import com.hseya.medtech.jobcard.bean.PartsBean;
import com.hseya.medtech.jobcard.unfinished.bean.UnfinishedDataBean;
import com.hseya.medtech.utill.VarList;

public class JobCardDBAccess {


	private static final String DATABASE_NAME="MEDITECH_DB";
	private static final String TABLE_NAME="jobcard";
	private static final String ENGINEER_HIS="engineer_his";
	private static final int TABLE_VERSION=1;
	private static final String ITEMS="items";
	private static final String CUSTOMER_TB="customer";
	private static final String ENGINEER_TB="engineer";
	private static final String PART_TB="partdetails";




	private final Context context;
	private SQLiteDatabase DB;
	private databaseHelper dbHelper;
	private String jobcardID;

	private static final String KEY_ID          =   "_ID";
	private static final String ENGINEER        =   "ENGINEER";
	private static final String DATECREATED     =   "DATECREATED";
	private static final String CUSTOMER        =   "CUSTOMER";
	private static final String EQUIPMENT       =   "EQUIPMENT";
	private static final String ORDERNO         =   "ORDERNO";
	private static final String VISITREASON     =   "VISITREASON";
	private static final String SERVICEDETAILS  =   "SERVICEDETAILS";
	private static final String PARTSUSED       =   "PARTSUSED";
	private static final String FILAMENTCOUNTER =   "FILAMENTCOUNTER";
	private static final String HVCOUNTER       =   "HVCOUNTER";
	private static final String LOGTIME         =   "LOGTIME";
	private static final String DOWNTIME        =   "DOWNTIME";
	private static final String CUSSIGN         =   "CUSSIGN";
	private static final String ENGSIGN         =   "ENGSIGN";
	private static final String CREATEDATE      =   "CREATEDATE";
	private static final String LASTUPDATEDATE  =   "LASTUPDATEDATE";
	private static final String STATUS          =   "STATUS";
	private static final String VAL             =   "VAL";
	private static final String INSPECTION      =   "INSPECTION";
	private static final String VISUAL          =   "VISUAL";

	//------------ engineer table colums-----

	private static final String ENGINEERID  = "ENGINEERID";
	private static final String DATE        = "DATE";
	private static final String JOBSTART    = "JOBSTART";
	private static final String JOBFINISHED = "JOBFINISHED";
	private static final String TIMEWORK    = "TIMEWORK";
	private static final String TRAVELTIME  = "TRAVELTIME";
	private static final String JOBCARDID   = "JOBCARDID";


	//------------------ part table ------------------
	private static final String PART_NAME            =   "PART_NAME";
	private static final String QUANTITY             =   "QUANTITY";
	private static final String PARTID               =   "PARTID";

	//------------------------------------------


	private static final String EQUIPMENTID       =   "EQUIPMENTID";
	private static final String EQUIPMENTTYPE     =   "EQUIPMENTTYPE";
	private static final String MANUFACTURER      =   "MANUFACTURER";
	private static final String MODELNO           =   "MODELNO";
	private static final String SERIAL            =   "SERIAL";
	private static final String INSTALLATIONDATE  =   "INSTALLATIONDATE";

	//--------------------------------------------------

	private static final String CUSTOMERID        =   "CUSTOMERID";
	private static final String NAME              =   "NAME";
	private static final String CUSCONTACTNO      =   "CUSCONTACTNO";
	private static final String CONTACTPERSON     =   "CONTACTPERSON";
	private static final String PERSONCONTACTNO   =   "PERSONCONTACTNO";
	private static final String EMAIL             =   "EMAIL";


	//-------------------------------------------


	private static final String CONTACTNO             =   "CONTACTNO";

	//-------------------------------------------



	public JobCardDBAccess(Context c){
		this.context=c;

	}

	/*
	 * open database connection
	 */
	public JobCardDBAccess openDB(){
		try {


			dbHelper=new databaseHelper(context);
			DB=dbHelper.getWritableDatabase();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}


	/*
	 * close database connection
	 */
	public void closeDB(){

		dbHelper.close();
	}

	private class databaseHelper extends SQLiteOpenHelper{

		public databaseHelper(Context context) {

			super(context,DATABASE_NAME,null,TABLE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {


				db.execSQL("CREATE TABLE " +ENGINEER_HIS +"("+
						KEY_ID           +   " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						JOBCARDID        +   " TEXT  ,"+
						ENGINEERID       +   " TEXT  ,"+
						DATE             +   " TEXT  ,"+
						JOBSTART         +   " TEXT  ,"+
						JOBFINISHED      +   " TEXT  ,"+
						TIMEWORK         +   " TEXT  ,"+
						TRAVELTIME       +   " TEXT  ,"+
						ENGSIGN          +   " TEXT  ,"+
						CREATEDATE       +   " TEXT  ,"+
						LASTUPDATEDATE   +   " TEXT   "+
						");"

						);

				//----------------------------------------------

				db.execSQL("CREATE TABLE " +PART_TB +"("+
						KEY_ID           +   " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						PART_NAME        +   " TEXT  ,"+
						PARTID           +   " TEXT  ,"+
						JOBCARDID        +   " TEXT  ,"+
						QUANTITY         +   " TEXT  ,"+						
						CREATEDATE       +   " TEXT  ,"+
						LASTUPDATEDATE   +   " TEXT   "+
						");"

						);

				//------------------------------------------------
				db.execSQL("CREATE TABLE " +TABLE_NAME +"("+
						KEY_ID         +   " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						ENGINEER       +   " TEXT ,"+
						DATECREATED    +   " TEXT ,"+
						CUSTOMER       +   " TEXT , "+
						EQUIPMENT      +   " TEXT , "+
						ORDERNO        +   " TEXT , "+
						VISITREASON    +   " TEXT , "+
						SERVICEDETAILS +   " TEXT , "+
						PARTSUSED      +   " TEXT , "+
						FILAMENTCOUNTER+   " TEXT , "+
						HVCOUNTER      +   " TEXT , "+
						LOGTIME        +   " TEXT , "+
						DOWNTIME       +   " TEXT , "+
						CUSSIGN        +   " TEXT , "+
						ENGSIGN        +   " TEXT , "+
						INSPECTION     +   " TEXT , "+
						VISUAL         +   " TEXT , "+
						STATUS         +   " INTEGER , "+
						VAL            +   " INTEGER , "+
						CREATEDATE     +   " TEXT , "+
						LASTUPDATEDATE +   " TEXT   " +
						");"

						);



				//-----------------------------------------------------



				db.execSQL("CREATE TABLE " +ITEMS +"("+ 
						KEY_ID           +   " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						EQUIPMENTID      +   " TEXT  , "+
						EQUIPMENTTYPE    +   " TEXT  , "+
						CUSTOMER         +   " TEXT  , "+
						MANUFACTURER     +   " TEXT  , "+
						MODELNO          +   " TEXT  , "+
						SERIAL           +   " TEXT  , "+
						INSTALLATIONDATE +   " TEXT  , "+
						LASTUPDATEDATE   +   " TEXT   "+
						");"

						);



				//-------------------------------------------------------

				db.execSQL("CREATE TABLE " + CUSTOMER_TB +"("+ 
						KEY_ID             +   " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						CUSTOMERID         +   " TEXT  , "+
						NAME               +   " TEXT  , "+
						CUSCONTACTNO       +   " TEXT  , "+
						CONTACTPERSON      +   " TEXT  , "+
						PERSONCONTACTNO    +   " TEXT  , "+
						EMAIL              +   " TEXT  , "+
						LASTUPDATEDATE     +   " TEXT   "+
						");"

						);



				//-------------------------------------------------------
				db.execSQL("CREATE TABLE " + ENGINEER_TB +"("+ 
						KEY_ID             +   " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						ENGINEERID         +   " TEXT  , "+
						NAME               +   " TEXT  , "+
						EMAIL              +   " TEXT  , "+
						CONTACTNO          +   " TEXT  , "+
						LASTUPDATEDATE     +   " TEXT   "+
						");"

						);

				//-------------------------------------------

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

			db.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);
			db.execSQL(" DROP TABLE IF EXISTS "+ENGINEER_HIS);
			db.execSQL(" DROP TABLE IF EXISTS "+ITEMS);
			db.execSQL(" DROP TABLE IF EXISTS "+CUSTOMER_TB);
			db.execSQL(" DROP TABLE IF EXISTS "+ENGINEER_TB);
			db.execSQL(" DROP TABLE IF EXISTS "+PART_TB);

			onCreate(db);
		}



	}


	/*
	 * insert data to jabcard table
	 */

	public void insertDB(JobCardDataBean bean) {
		try {


			ContentValues cv=new ContentValues();

			cv.put(ENGINEER, VarList.LOGINUSER_ID);
			cv.put(DATECREATED, bean.getJobDate());
			cv.put(CUSTOMER, bean.getCustomerID());
			cv.put(EQUIPMENT, bean.getEqucmentID()); 
			cv.put(ORDERNO, bean.getJobCardNO());
			cv.put(VISITREASON, bean.getVisitReason());
			cv.put(SERVICEDETAILS, bean.getService());
			cv.put(PARTSUSED, bean.getPartUsed());
			cv.put(FILAMENTCOUNTER, bean.getFilamnetCounter());
			cv.put(HVCOUNTER, bean.getHvCounter());
			cv.put(LOGTIME, bean.getLogTime());
			cv.put(DOWNTIME, bean.getDownTime());
			cv.put(CUSSIGN, bean.getCustomerSignPath());
			cv.put(ENGSIGN, bean.getEngineerSignPath());
			cv.put(VISUAL, bean.getVisualCheck());
			cv.put(INSPECTION, bean.getInspectionCheck());
			cv.put(STATUS, 1);
			cv.put(VAL, 1);
			cv.put(CREATEDATE, new Date().toString()); 
			cv.put(LASTUPDATEDATE, new Date().toString()); 



			long i =DB.insert(TABLE_NAME, null, cv);

			jobcardID=String.valueOf(i);

			//-------------------------------------------------

			ArrayList<EngineerDataBean> dataBeanList=bean.getEngBean();



			for (int j = 0; j < dataBeanList.size(); j++) { 

				EngineerDataBean data=dataBeanList.get(j);

				ContentValues cv1=new ContentValues();


				cv1.put(JOBCARDID, jobcardID);
				cv1.put(ENGINEERID, data.getEngID());
				cv1.put(DATE, ""); 
				cv1.put(JOBSTART,data.getJobStart());
				cv1.put(JOBFINISHED, data.getJobFinished());
				cv1.put(TIMEWORK, data.getWorkedHours());
				cv1.put(TRAVELTIME, data.getTravelTime());
				cv1.put(ENGSIGN, data.getSignPath()); 
				cv1.put(CREATEDATE, new Date().toString()); 
				cv1.put(LASTUPDATEDATE, new Date().toString()); 

				DB.insert(ENGINEER_HIS, null, cv1);
			}


			//-------insert part details--------------------

			ArrayList<PartsBean> partBean=bean.getPartBean();



			for (int j = 0; j < partBean.size(); j++) { 

				PartsBean data=partBean.get(j);

				ContentValues cv2=new ContentValues();


				cv2.put(JOBCARDID, jobcardID);
				cv2.put(PART_NAME, data.getPartName());
				cv2.put(QUANTITY, data.getPartQuantitys());
				cv2.put(PARTID, data.getPartID());
				cv2.put(CREATEDATE, new Date().toString()); 
				cv2.put(LASTUPDATEDATE, new Date().toString()); 

				DB.insert(PART_TB, null, cv2);
			}

			//---------------------------

			Log.d("--------------------------", jobcardID); 
		} catch (Exception e) {
			e.printStackTrace();
			//	Log.d("ERROR", e.getMessage());
		}
	}





	public ArrayList<UnfinishedDataBean> getAll(int status) { 
		ArrayList<UnfinishedDataBean>dataList=new ArrayList<UnfinishedDataBean>();
		try{



			String[] colums=new String[]{KEY_ID,ORDERNO,CUSTOMER,LASTUPDATEDATE,VAL};

			Cursor cus=DB.query(TABLE_NAME, colums, STATUS +" = "+status, null, null, null, null);


			int rowID    = cus.getColumnIndex(ORDERNO);
			int rowName  = cus.getColumnIndex(CUSTOMER);
			int rowDate  = cus.getColumnIndex(LASTUPDATEDATE);
			int rowkeyID = cus.getColumnIndex(KEY_ID);
			int val      = cus.getColumnIndex(VAL);




			for(cus.moveToFirst();!cus.isAfterLast();cus.moveToNext()){
				UnfinishedDataBean testBean=new UnfinishedDataBean();
				//result+=cus.getString(rowID)+" "+cus.getString(rowName)+" "+cus.getString(rowAge);
				testBean.setID(cus.getString(rowID));

				String cusName="";
				for (int i = 0; i < VarList.CUSTOMER_LIST.size(); i++) {

					if (VarList.CUSTOMER_LIST.get(i).getCus_ID().equals(cus.getString(rowName))) { 

						cusName=VarList.CUSTOMER_LIST.get(i).getCus_name();
						break;
					}
				}
				testBean.setCustomerName(cusName);
				testBean.setDate(cus.getString(rowDate));
				testBean.setKeyID(cus.getString(rowkeyID));
				testBean.setVal(cus.getString(val));

				dataList.add(testBean);
			}

			if (cus != null && !cus.isClosed()) {
				cus.close();
			} 


		} catch (Exception e) {
			//Log.d("ERROR----------------------------3333333333333333-----------------------------", e.getMessage());
			e.printStackTrace();
		}
		return dataList;
	}



	public void insertFirstDB(JobCardDataBean bean) {
		try {


			ContentValues cv=new ContentValues();

			cv.put(ENGINEER, VarList.LOGINUSER_ID);
			cv.put(DATECREATED, bean.getJobDate());

			cv.put(CUSTOMER, bean.getCustomerID());

			cv.put(EQUIPMENT, bean.getEqucmentID()); 


			cv.put(ORDERNO, bean.getJobCardNO());
			cv.put(VISITREASON, bean.getVisitReason());
			cv.put(SERVICEDETAILS, bean.getService());
			cv.put(PARTSUSED, bean.getPartUsed());
			cv.put(FILAMENTCOUNTER, bean.getFilamnetCounter());
			cv.put(HVCOUNTER, bean.getHvCounter());
			cv.put(LOGTIME, bean.getLogTime());
			cv.put(DOWNTIME, bean.getDownTime());
			cv.put(STATUS, 0);
			cv.put(VAL, 0);
			cv.put(CREATEDATE, new Date().toString()); 
			cv.put(LASTUPDATEDATE, new Date().toString()); 



			long i=DB.insert(TABLE_NAME, null, cv);

			jobcardID=String.valueOf(i);




			ArrayList<PartsBean> partBean=bean.getPartBean();



			for (int j = 0; j < partBean.size(); j++) { 

				PartsBean data=partBean.get(j);

				ContentValues cv2=new ContentValues();

				cv2.put(PART_NAME, data.getPartName());
				cv2.put(JOBCARDID, jobcardID);
				cv2.put(QUANTITY, data.getPartQuantitys());
				cv2.put(PARTID, data.getPartID());
				cv2.put(CREATEDATE, new Date().toString()); 
				cv2.put(LASTUPDATEDATE, new Date().toString()); 

				DB.insert(PART_TB, null, cv2);
			}

			//	Log.d("--------------------------", jobcardID); 
		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("ERROR", e.getMessage());
		}
	}

	/*/
	 * get job card details according to ID
	 */

	public JobCardDataBean getJobcardDetials(long l,Boolean flag) {
		try {


			String[] colums=new String[]{KEY_ID,ENGINEER,DATECREATED,CUSTOMER,EQUIPMENT,ORDERNO,
					VISITREASON,SERVICEDETAILS,PARTSUSED,FILAMENTCOUNTER,HVCOUNTER,LOGTIME,DOWNTIME};

			JobCardDataBean bean=new JobCardDataBean();









			Cursor cus=DB.query(TABLE_NAME, colums, KEY_ID +" = "+l, null, null, null, null);

			if (cus != null) {
				cus.moveToFirst();
				bean.setJobDate(cus.getString(2)) ;

				String cusID=cus.getString(3);

				bean.setCustomerID(cusID) ;

				int index=-1;


				for (int i = 0; i < VarList.CUSTOMER_LIST.size(); i++) { 

					if (VarList.CUSTOMER_LIST.get(i).getCus_ID().equals(cusID)) { 

						index=i;
						break;
					} 

				}

				bean.setCustomer(VarList.CUSTOMER_LIST.get(index).getCus_name()); 

				String eqID=cus.getString(4);

				bean.setEqucmentID(eqID) ;


				int EQindex=-1;


				for (int i = 0; i < VarList.EQCIPMENTS_LIST.size(); i++) { 

					if (VarList.EQCIPMENTS_LIST.get(i).getID().equals(eqID)) { 

						EQindex=i;
						break;
					} 

				}

				bean.setEqucment(VarList.EQCIPMENTS_LIST.get(EQindex).getName());

				bean.setJobCardNO(cus.getString(5)) ;
				bean.setVisitReason(cus.getString(6)) ;
				bean.setService(cus.getString(7)) ;
				bean.setPartUsed(cus.getString(8)) ;
				bean.setFilamnetCounter(cus.getString(9)) ;
				bean.setHvCounter(cus.getString(10)) ;
				bean.setLogTime(cus.getString(11)) ;
				bean.setDownTime(cus.getString(12)) ;




				//------------------------------part get ----------------------------------------
				String[] columsPart=new String[]{KEY_ID,PART_NAME,QUANTITY,PARTID};

				Cursor cusPart=DB.query(PART_TB, columsPart, JOBCARDID +" = "+l, null, null, null, null);

				int partID                  = cusPart.getColumnIndex(KEY_ID);
				int partNameIndex           = cusPart.getColumnIndex(PART_NAME);
				int partQuantityIndex       = cusPart.getColumnIndex(QUANTITY);
				int partIDIndex             = cusPart.getColumnIndex(PARTID);


				ArrayList<PartsBean>partList=new ArrayList<PartsBean>();

				for(cusPart.moveToFirst();!cusPart.isAfterLast();cusPart.moveToNext()){

					PartsBean partBean=new PartsBean();

					partBean.setID(cusPart.getString(partID)); 
					partBean.setPartName(cusPart.getString(partNameIndex));
					partBean.setPartQuantitys(cusPart.getString(partQuantityIndex));
					partBean.setPartID(cusPart.getString(partIDIndex));

					partList.add(partBean);

				}

				bean.setPartBean(partList); 


				if (cusPart != null && !cusPart.isClosed()) {
					cusPart.close();
				} 


				//--------------- add engineer details---------------
				if (flag) {


					String[] columsENG=new String[]{KEY_ID,ENGINEERID,DATE,JOBSTART,JOBFINISHED,TIMEWORK,TRAVELTIME,ENGSIGN};

					Cursor cusENG=DB.query(ENGINEER_HIS, columsENG, JOBCARDID +" = "+l, null, null, null, null);


					int ID            = cusENG.getColumnIndex(KEY_ID);
					int engID         = cusENG.getColumnIndex(ENGINEERID);
					int engsign       = cusENG.getColumnIndex(ENGSIGN);
					int engJobStart   = cusENG.getColumnIndex(JOBSTART);
					int engFinished   = cusENG.getColumnIndex(JOBFINISHED);
					int engTimeWork   = cusENG.getColumnIndex(TIMEWORK);
					int engTravelTime = cusENG.getColumnIndex(TRAVELTIME);


					ArrayList<EngineerDataBean>dataList=new ArrayList<EngineerDataBean>();

					for(cusENG.moveToFirst();!cusENG.isAfterLast();cusENG.moveToNext()){
						EngineerDataBean testBean=new EngineerDataBean();
						//result+=cus.getString(rowID)+" "+cus.getString(rowName)+" "+cus.getString(rowAge);
						testBean.setEngID(cusENG.getString(engID ));

						String engName="";
						for (int i = 0; i < VarList.ENGINEER_LIST.size(); i++) {

							if (VarList.ENGINEER_LIST.get(i).getID().equals(cusENG.getString(engID))) { 

								engName=VarList.ENGINEER_LIST.get(i).getEngName();
								break;
							}
						}
						testBean.setEngName(engName);
						testBean.setID(cusENG.getString(ID));
						//						testBean.setDate(cusENG.getString(engDate));
						testBean.setJobStart(cusENG.getString(engJobStart));
						testBean.setJobFinished(cusENG.getString(engFinished));
						testBean.setWorkedHours(cusENG.getString(engTimeWork));
						testBean.setTravelTime(cusENG.getString(engTravelTime));
						testBean.setSignPath(cusENG.getString(engsign));
						dataList.add(testBean);
					}

					bean.setEngBean(dataList);

					if (cusENG != null && !cusENG.isClosed()) {
						cusENG.close();
					} 


				}
				//---------------------------------------------------

				if (cus != null && !cus.isClosed()) {
					cus.close();
				} 



				return bean;
			}
			return null;

		} catch (Exception e) {
			//Log.d("eeeeeeeeeeeeeeeeeeeeeeeeeeeee", e.getMessage().toString());
			e.printStackTrace();
			return null;
		}
	}




	/*
	 * insert engineer details 
	 */


	public void insertENG(JobCardDataBean bean) {
		try {


			ContentValues cv=new ContentValues();

			cv.put(ENGINEER, VarList.LOGINUSER_ID);
			cv.put(DATECREATED, bean.getJobDate());
			cv.put(CUSTOMER, bean.getCustomerID());
			cv.put(EQUIPMENT, bean.getEqucmentID()); 
			cv.put(ORDERNO, bean.getJobCardNO());
			cv.put(VISITREASON, bean.getVisitReason());
			cv.put(SERVICEDETAILS, bean.getService());
			cv.put(PARTSUSED, bean.getPartUsed());
			cv.put(FILAMENTCOUNTER, bean.getFilamnetCounter());
			cv.put(HVCOUNTER, bean.getHvCounter());
			cv.put(LOGTIME, bean.getLogTime());
			cv.put(DOWNTIME, bean.getDownTime());
			cv.put(STATUS, 0);
			cv.put(VAL, 2);
			cv.put(CREATEDATE, new Date().toString()); 
			cv.put(LASTUPDATEDATE, new Date().toString()); 



			long i =DB.insert(TABLE_NAME, null, cv);

			jobcardID=String.valueOf(i);

			//----------------- insert part------------------------

			ArrayList<PartsBean> partBean=bean.getPartBean();



			for (int j = 0; j < partBean.size(); j++) { 

				PartsBean data=partBean.get(j);

				ContentValues cv2=new ContentValues();

				cv2.put(PART_NAME, data.getPartName());
				cv2.put(JOBCARDID, jobcardID);
				cv2.put(QUANTITY, data.getPartQuantitys());
				cv2.put(PARTID, data.getPartID());
				cv2.put(CREATEDATE, new Date().toString()); 
				cv2.put(LASTUPDATEDATE, new Date().toString()); 

				DB.insert(PART_TB, null, cv2);
			}



			//-------------------------------------------------

			ArrayList<EngineerDataBean> dataBeanList=bean.getEngBean();



			for (int j = 0; j < dataBeanList.size(); j++) { 

				EngineerDataBean data=dataBeanList.get(j);

				ContentValues cv1=new ContentValues();


				cv1.put(JOBCARDID, jobcardID);
				cv1.put(ENGINEERID, data.getEngID());
				cv1.put(DATE, ""); 
				cv1.put(JOBSTART,data.getJobStart());
				cv1.put(JOBFINISHED, data.getJobFinished());
				cv1.put(TIMEWORK, data.getWorkedHours());
				cv1.put(TRAVELTIME, data.getTravelTime());
				cv1.put(ENGSIGN, data.getSignPath());  
				cv1.put(CREATEDATE, new Date().toString()); 
				cv1.put(LASTUPDATEDATE, new Date().toString()); 

				DB.insert(ENGINEER_HIS, null, cv1);
			}

			//Log.d("--------------------------", jobcardID); 
		} catch (Exception e) {
			e.printStackTrace();
			//	Log.d("ERROR", e.getMessage());
		}
	}






	/*
	 * update job card details
	 */
	public void updateFirstDB(JobCardDataBean bean,long val) {
		try {


			ContentValues cv=new ContentValues();

			cv.put(ENGINEER, VarList.LOGINUSER_ID);
			cv.put(DATECREATED, bean.getJobDate());

			//if (!bean.getCustomerID().equals("") || bean.getCustomerID()!=null) {
			cv.put(CUSTOMER, bean.getCustomerID());
			//}

			//if (!bean.getEqucmentID().equals("") || bean.getEqucmentID()!=null) {
			cv.put(EQUIPMENT, bean.getEqucmentID()); 
			//}



			cv.put(ORDERNO, bean.getJobCardNO());
			cv.put(VISITREASON, bean.getVisitReason());
			cv.put(SERVICEDETAILS, bean.getService());
			cv.put(PARTSUSED, bean.getPartUsed());
			cv.put(FILAMENTCOUNTER, bean.getFilamnetCounter());
			cv.put(HVCOUNTER, bean.getHvCounter());
			cv.put(LOGTIME, bean.getLogTime());
			cv.put(DOWNTIME, bean.getDownTime());
			//			cv.put(STATUS, 0);
			//			cv.put(VAL, 0);
			cv.put(LASTUPDATEDATE, new Date().toString()); 



			//DB.insert(TABLE_NAME, null, cv);

			DB.update(TABLE_NAME, cv, KEY_ID + " = "+val	, null);

			//----------------- part update ---------------------------------------

			ArrayList<PartsBean> partBean=bean.getPartBean();



			for (int j = 0; j < partBean.size(); j++) { 

				PartsBean data=partBean.get(j);

				ContentValues cv2=new ContentValues();

				cv2.put(PART_NAME, data.getPartName());
				cv2.put(JOBCARDID, val);
				cv2.put(QUANTITY, data.getPartQuantitys());
				cv2.put(PARTID, data.getPartID());
				cv2.put(CREATEDATE, new Date().toString()); 
				cv2.put(LASTUPDATEDATE, new Date().toString()); 

				//				if (data.getFlag().equals("0")) {
				//					DB.insert(PART_TB, null, cv2);
				//				}else{
				//					DB.update(PART_TB, cv2,  KEY_ID + " = "+data.getID(), null);
				//				}
				if (data.getID()!=null ) {
					DB.update(PART_TB, cv2,  KEY_ID + " = "+data.getID(), null);
				}else{
					DB.insert(PART_TB, null, cv2);
				}

			}


			//---------------------------------------
			//	Log.d("--------------------------", jobcardID); 
		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("ERROR", e.getMessage());
		}
	}






	//-------------------------------------------------------------------------------------------



	/*
	 * update job card details
	 */
	public void updateENG(JobCardDataBean bean,long val) {
		try {


			ContentValues cv=new ContentValues();

			cv.put(ENGINEER, VarList.LOGINUSER_ID);
			cv.put(DATECREATED, bean.getJobDate());
			cv.put(CUSTOMER, bean.getCustomerID());
			cv.put(EQUIPMENT, bean.getEqucmentID()); 
			cv.put(ORDERNO, bean.getJobCardNO());
			cv.put(VISITREASON, bean.getVisitReason());
			cv.put(SERVICEDETAILS, bean.getService());
			cv.put(PARTSUSED, bean.getPartUsed());
			cv.put(FILAMENTCOUNTER, bean.getFilamnetCounter());
			cv.put(HVCOUNTER, bean.getHvCounter());
			cv.put(LOGTIME, bean.getLogTime());
			cv.put(DOWNTIME, bean.getDownTime());
			//			cv.put(STATUS, 0);
			cv.put(VAL, 2);
			cv.put(LASTUPDATEDATE, new Date().toString()); 



			//DB.insert(TABLE_NAME, null, cv);

			DB.update(TABLE_NAME, cv, KEY_ID + " = "+val	, null);





			//----------------------part details---------------------

			ArrayList<PartsBean> partBean=bean.getPartBean();



			for (int j = 0; j < partBean.size(); j++) { 

				PartsBean data=partBean.get(j);

				ContentValues cv2=new ContentValues();

				cv2.put(PART_NAME, data.getPartName());
				cv2.put(JOBCARDID, val);
				cv2.put(QUANTITY, data.getPartQuantitys());
				cv2.put(PARTID, data.getPartID());
				cv2.put(CREATEDATE, new Date().toString()); 
				cv2.put(LASTUPDATEDATE, new Date().toString()); 

				if (data.getID()!=null ) {
					DB.update(PART_TB, cv2,  KEY_ID + " = "+data.getID(), null);
				}else{
					DB.insert(PART_TB, null, cv2);
				}

			}





			//----------------------------------------------------------

			ArrayList<EngineerDataBean> dataBeanList=bean.getEngBean();



			for (int j = 0; j < dataBeanList.size(); j++) { 

				EngineerDataBean data=dataBeanList.get(j);

				ContentValues cv1=new ContentValues();


				//				cv1.put(JOBCARDID, jobcardID);
				cv1.put(ENGINEERID, data.getEngID());
				cv1.put(DATE, ""); 
				cv1.put(JOBSTART,data.getJobStart());
				cv1.put(JOBFINISHED, data.getJobFinished());
				cv1.put(TIMEWORK, data.getWorkedHours());
				cv1.put(TRAVELTIME, data.getTravelTime());

				cv1.put(CREATEDATE, new Date().toString()); 
				cv1.put(LASTUPDATEDATE, new Date().toString()); 

				if (data.getFlag().equals("0")) {
					//DB.update(ENGINEER_HIS, cv1, KEY_ID+ " = "+data.getID(), null);
				}else{
					cv1.put(JOBCARDID, VarList.SELECTED_ID);
					cv1.put(ENGSIGN, data.getSignPath());
					DB.insert(ENGINEER_HIS, null, cv1);
				}


			}



			//	Log.d("--------------------------", jobcardID); 
		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("ERROR", e.getMessage());
		}
	}

	/*
	 * update full job card
	 */
	public void updateFull(JobCardDataBean bean,long val){


		ContentValues cv=new ContentValues();

		cv.put(ENGINEER, VarList.LOGINUSER_ID);
		cv.put(DATECREATED, bean.getJobDate());
		cv.put(CUSTOMER, bean.getCustomerID());
		cv.put(EQUIPMENT, bean.getEqucmentID()); 
		cv.put(ORDERNO, bean.getJobCardNO());
		cv.put(VISITREASON, bean.getVisitReason());
		cv.put(SERVICEDETAILS, bean.getService());
		cv.put(PARTSUSED, bean.getPartUsed());
		cv.put(FILAMENTCOUNTER, bean.getFilamnetCounter());
		cv.put(HVCOUNTER, bean.getHvCounter());
		cv.put(LOGTIME, bean.getLogTime());
		cv.put(DOWNTIME, bean.getDownTime());
		cv.put(CUSSIGN, bean.getCustomerSignPath());
		cv.put(ENGSIGN, bean.getEngineerSignPath());
		cv.put(STATUS, 1);
		cv.put(VAL, 1);
		cv.put(CREATEDATE, new Date().toString()); 
		cv.put(LASTUPDATEDATE, new Date().toString()); 



		long i =DB.update(TABLE_NAME, cv, KEY_ID + " = "+val	, null);

		jobcardID=String.valueOf(i);

		//-------------------------------------------------

		ArrayList<EngineerDataBean> dataBeanList=bean.getEngBean();



		for (int j = 0; j < dataBeanList.size(); j++) { 

			EngineerDataBean data=dataBeanList.get(j);

			ContentValues cv1=new ContentValues();


			cv1.put(JOBCARDID, jobcardID);
			cv1.put(ENGINEERID, data.getEngID());
			cv1.put(DATE, ""); 
			cv1.put(JOBSTART,data.getJobStart());
			cv1.put(JOBFINISHED, data.getJobFinished());
			cv1.put(TIMEWORK, data.getWorkedHours());
			cv1.put(TRAVELTIME, data.getTravelTime());
			cv1.put(CREATEDATE, new Date().toString()); 
			cv1.put(LASTUPDATEDATE, new Date().toString()); 

			DB.update(ENGINEER_HIS, cv1, KEY_ID+ " = "+data.getID(), null);
		}

	}



	/*
	 * insert item db
	 */

	public void insertItemDB(ItemsBean bean) {
		try {


			ContentValues cv=new ContentValues();

			cv.put(EQUIPMENTID, bean.getEquipmentID());
			cv.put(EQUIPMENTTYPE, bean.getEquipmentType());
			cv.put(CUSTOMER, bean.getCustomer()); 
			cv.put(MANUFACTURER, bean.getManufactureDate());
			cv.put(MODELNO, bean.getModelNo());
			cv.put(SERIAL, bean.getSerialNo());
			cv.put(INSTALLATIONDATE, bean.getInstallDate());
			cv.put(LASTUPDATEDATE, new Date().toString()); 



			DB.insert(ITEMS, null, cv);


		} catch (Exception e) {
			e.printStackTrace();
			//	Log.d("ERROR", e.getMessage());
		}
	}



	public void dropTable() {

		//    String sql = "drop table " + ITEMS;
		try {
			DB.delete(ITEMS, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("ERROR", e.getMessage());
		}
	}

	//----------------------------------------------------------

	public ArrayList<EquickmentBean> getAllItems() { 
		ArrayList<EquickmentBean>dataList=new ArrayList<EquickmentBean>();
		try{

			String[] colums=new String[]{KEY_ID,EQUIPMENTID,EQUIPMENTTYPE};

			Cursor cus=DB.query(ITEMS, colums, null, null, null, null, null);



			int EqID=cus.getColumnIndex(EQUIPMENTID);
			int eqType=cus.getColumnIndex(EQUIPMENTTYPE);
			//			int rowDate=cus.getColumnIndex(LASTUPDATEDATE);
			//			int rowkeyID=cus.getColumnIndex(KEY_ID);
			//			int val=cus.getColumnIndex(VAL);




			for(cus.moveToFirst();!cus.isAfterLast();cus.moveToNext()){
				EquickmentBean testBean=new EquickmentBean();
				testBean.setID(cus.getString(EqID));

				//				String cusName="";
				//				for (int i = 0; i < VarList.CUSTOMER_LIST.size(); i++) {
				//
				//					if (VarList.CUSTOMER_LIST.get(i).getCus_ID().equals(cus.getString(rowName))) { 
				//
				//						cusName=VarList.CUSTOMER_LIST.get(i).getCus_name();
				//						break;
				//					}
				//				}
				//				testBean.setCustomerName(cusName);
				testBean.setName(cus.getString(eqType));
				//				testBean.setKeyID(cus.getString(eqType));
				//				testBean.setVal(cus.getString(val));
				dataList.add(testBean);
			}

			if (cus != null && !cus.isClosed()) {
				cus.close();
			} 


		} catch (Exception e) {
			//Log.d("ERROR----------------------------3333333333333333-----------------------------", e.getMessage());
			e.printStackTrace();
		}
		return dataList;
	}



	//------------------------------------------------------------------------------


	/*
	 * insert customers db
	 */

	public void insertCustomerDB(CustomerBean bean) {
		try {







			ContentValues cv=new ContentValues();

			cv.put(CUSTOMERID, bean.getCus_ID());
			cv.put(NAME, bean.getCus_name());
			cv.put(CUSCONTACTNO, bean.getContactNo()); 
			cv.put(CONTACTPERSON, bean.getContactPerson());
			cv.put(PERSONCONTACTNO, bean.getPersontoContanct());
			cv.put(EMAIL, bean.getEmail());




			DB.insert(CUSTOMER_TB, null, cv);


		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("ERROR", e.getMessage());
		}
	}



	public void dropCustomerTable() {

		//    String sql = "drop table " + ITEMS;
		try {
			DB.delete(CUSTOMER_TB, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("ERROR", e.getMessage());
		}
	}



	//----------------------------------------------------------


	//----------------------------------------------------------

	public ArrayList<CustomerBean> getAllCustomer() { 
		ArrayList<CustomerBean>dataList=new ArrayList<CustomerBean>();
		try{



			String[] colums=new String[]{KEY_ID,CUSTOMERID,NAME};

			Cursor cus=DB.query(CUSTOMER_TB, colums, null, null, null, null, null);



			int cusID=cus.getColumnIndex(CUSTOMERID);
			int cusName=cus.getColumnIndex(NAME);





			for(cus.moveToFirst();!cus.isAfterLast();cus.moveToNext()){
				CustomerBean testBean=new CustomerBean();
				testBean.setCus_ID(cus.getString(cusID));


				testBean.setCus_name(cus.getString(cusName));

				dataList.add(testBean);
			}

			if (cus != null && !cus.isClosed()) {
				cus.close();
			} 


		} catch (Exception e) {
			//Log.d("ERROR----------------------------3333333333333333-----------------------------", e.getMessage());
			e.printStackTrace();
		}
		return dataList;
	}

	//-------------------------------

	//------------------------------------------------------------------------------


	/*
	 * insert customers db
	 */

	public void insertEngineerDB(EngineerGetBean bean) {
		try {







			ContentValues cv=new ContentValues();

			cv.put(ENGINEERID, bean.getID());
			cv.put(NAME, bean.getEngName());
			cv.put(CONTACTNO, bean.getContactNO()); 
			cv.put(EMAIL, bean.getEmail());




			DB.insert(ENGINEER_TB, null, cv);


		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ERROR", e.getMessage());
		}
	}



	public void dropEngineerTable() {

		//    String sql = "drop table " + ITEMS;
		try {
			DB.delete(ENGINEER_TB, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ERROR", e.getMessage());
		}
	}




	public ArrayList<EngineerGetBean> getAllEngineer() { 
		ArrayList<EngineerGetBean>dataList=new ArrayList<EngineerGetBean>();
		try{


			EngineerGetBean fisrtBean=new EngineerGetBean();

			fisrtBean.setID("");
			fisrtBean.setEngName("-- Select Engineer --");


			dataList.add(fisrtBean);

			String[] colums=new String[]{KEY_ID,ENGINEERID,NAME};

			Cursor cus=DB.query(ENGINEER_TB, colums, null, null, null, null, null);



			int engID=cus.getColumnIndex(ENGINEERID);
			int engName=cus.getColumnIndex(NAME);





			for(cus.moveToFirst();!cus.isAfterLast();cus.moveToNext()){
				EngineerGetBean testBean=new EngineerGetBean();
				testBean.setID(cus.getString(engID));


				testBean.setEngName(cus.getString(engName));

				dataList.add(testBean);
			}

			if (cus != null && !cus.isClosed()) {
				cus.close();
			} 


		} catch (Exception e) {
			//Log.d("ERROR----------------------------3333333333333333-----------------------------", e.getMessage());
			e.printStackTrace();
		}
		return dataList;
	}


	//----------------- get all details to upload to server ----------------------


	@SuppressWarnings("finally")
	public JobCardDataBean getAllDetails(String jobCardID){

		JobCardDataBean bean=new JobCardDataBean();

		try{
			//--------------------------------

			String[] colums=new String[]{KEY_ID,ENGINEER,DATECREATED,CUSTOMER,EQUIPMENT,ORDERNO,
					VISITREASON,SERVICEDETAILS,PARTSUSED,FILAMENTCOUNTER,HVCOUNTER,LOGTIME,DOWNTIME,CUSSIGN,ENGSIGN,
					LASTUPDATEDATE,INSPECTION,VISUAL};


			System.out.println(" ");
			System.out.println(" jobCardID : "+jobCardID);
			System.out.println(" ");



			long l=Long.parseLong(jobCardID);




			Cursor cus=DB.query(TABLE_NAME, colums, KEY_ID +" = "+l, null, null, null, null);

			if (cus != null) {
				cus.moveToFirst();

				bean.setJobDate(cus.getString(2)) ;
				bean.setEngineerUser(cus.getString(1));
				String cusID=cus.getString(3);
				bean.setCustomerID(cusID) ;
				String eqID=cus.getString(4);
				bean.setEqucmentID(eqID) ;
				bean.setJobCardNO(cus.getString(5)) ;
				bean.setVisitReason(cus.getString(6)) ;
				bean.setService(cus.getString(7)) ;
				bean.setPartUsed(cus.getString(8)) ;
				bean.setFilamnetCounter(cus.getString(9)) ;
				bean.setHvCounter(cus.getString(10)) ;
				bean.setLogTime(cus.getString(11)) ;
				bean.setDownTime(cus.getString(12)) ;
				bean.setCustomerSignPath(cus.getString(13)) ;
				bean.setEngineerSignPath(cus.getString(14)) ;
				bean.setLastUpdatedate(cus.getString(15)) ;
				bean.setVisualCheck(cus.getString(16));
				bean.setInspectionCheck(cus.getString(17));



				//-----------------------------------

			}

			if (cus != null && !cus.isClosed()) {
				cus.close();
			} 



			//----------------------get part details

			String[] columsPart=new String[]{KEY_ID,PART_NAME,QUANTITY,PARTID};

			Cursor cusPart=DB.query(PART_TB, columsPart, JOBCARDID +" = "+l, null, null, null, null);

			int partID                  = cusPart.getColumnIndex(KEY_ID);
			int partNameIndex           = cusPart.getColumnIndex(PART_NAME);
			int partIDIndex           = cusPart.getColumnIndex(PARTID);
			int partQuantityIndex       = cusPart.getColumnIndex(QUANTITY);


			ArrayList<PartsBean>partList=new ArrayList<PartsBean>();

			for(cusPart.moveToFirst();!cusPart.isAfterLast();cusPart.moveToNext()){

				PartsBean partBean=new PartsBean();

				partBean.setID(cusPart.getString(partID)); 
				partBean.setPartName(cusPart.getString(partNameIndex));
				partBean.setPartQuantitys(cusPart.getString(partQuantityIndex)); 
				partBean.setPartID(cusPart.getString(partIDIndex)); 

				partList.add(partBean);

			}

			bean.setPartBean(partList); 


			if (cusPart != null && !cusPart.isClosed()) {
				cusPart.close();
			} 
















			//----------------get engineer details -----------------
			String[] columsENG=new String[]{KEY_ID,ENGINEERID,DATE,JOBSTART,JOBFINISHED,TIMEWORK,TRAVELTIME,ENGSIGN};

			Cursor cusENG=DB.query(ENGINEER_HIS, columsENG, JOBCARDID +" = "+l, null, null, null, null);


			int ID            = cusENG.getColumnIndex(KEY_ID);
			int engID         = cusENG.getColumnIndex(ENGINEERID);
			//			int engDate       = cusENG.getColumnIndex(DATE);
			int engJobStart   = cusENG.getColumnIndex(JOBSTART);
			int engFinished   = cusENG.getColumnIndex(JOBFINISHED);
			int engTimeWork   = cusENG.getColumnIndex(TIMEWORK);
			int engTravelTime = cusENG.getColumnIndex(TRAVELTIME);
			int engsign       = cusENG.getColumnIndex(ENGSIGN);

			ArrayList<EngineerDataBean>dataList=new ArrayList<EngineerDataBean>();

			System.out.println(" ");
			System.out.println("cusENG size :  "+cusENG.getCount());
			System.out.println(" ");

			for(cusENG.moveToFirst();!cusENG.isAfterLast();cusENG.moveToNext()){

				EngineerDataBean testBean=new EngineerDataBean();

				testBean.setEngID(cusENG.getString(engID ));
				testBean.setID(cusENG.getString(ID));
				testBean.setJobStart(cusENG.getString(engJobStart));
				testBean.setJobFinished(cusENG.getString(engFinished));
				testBean.setWorkedHours(cusENG.getString(engTimeWork));
				testBean.setSignPath(cusENG.getString(engsign));
				testBean.setTravelTime(cusENG.getString(engTravelTime));


				dataList.add(testBean);
			}

			System.out.println(" ");
			System.out.println(" dataList.size : "+dataList.size());
			System.out.println(" ");

			bean.setEngBean(dataList);

			if (cusENG != null && !cusENG.isClosed()) {
				cusENG.close();
			} 






			//-------------------------------------------------
		} catch (Exception e) {
			Log.d("ERROR", e.toString());
			e.printStackTrace();
			//return null;
		}finally{
			return bean;
		}
	}
	//------------------------------------------------------------------

	public void deleteEngineerDetails(String ID) {
		try {


			DB.delete(ENGINEER_HIS,KEY_ID + " = ?",
					new String[] { String.valueOf(ID) });



		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ERROR", e.getMessage());
		}
	}

	public void deleteJobcard(String ID) {
		try {


			DB.delete(TABLE_NAME,KEY_ID + " = ?",
					new String[] { String.valueOf(ID) });



		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ERROR", e.getMessage());
		}
	}

}

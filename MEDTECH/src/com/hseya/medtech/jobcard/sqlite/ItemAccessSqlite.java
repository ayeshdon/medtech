package com.hseya.medtech.jobcard.sqlite;


import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hseya.medtech.jobcard.bean.ItemsBean;


public class ItemAccessSqlite {


	private static final String DATABASE_NAME="MEDITECH_DB";
	private static final String TABLE_NAME="ITEMS";

	private static final int TABLE_VERSION=1;

	private final Context context;
	private SQLiteDatabase DB;
	private databaseHelper dbHelper;


	private static final String KEY_ID            =   "_ID";
	private static final String EQUIPMENTID       =   "EQUIPMENTID";
	private static final String EQUIPMENTTYPE     =   "EQUIPMENTTYPE";
	private static final String CUSTOMER          =   "CUSTOMER";
	private static final String MANUFACTURER      =   "MANUFACTURER";
	//private static final String ORDERNO           =   "ORDERNO";
	private static final String MODELNO           =   "MODELNO";
	private static final String SERIAL            =   "SERIAL";
	private static final String INSTALLATIONDATE  =   "INSTALLATIONDATE";
	private static final String LASTUPDATEDATE    =   "LASTUPDATEDATE";



	public ItemAccessSqlite(Context c){
		this.context=c;

	}


	/*
	 * open database connection
	 */
	public ItemAccessSqlite openDB(){
		try {


			dbHelper=new databaseHelper(context);
			DB=dbHelper.getWritableDatabase();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}


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


				db.execSQL("CREATE TABLE " +TABLE_NAME +"("+
						KEY_ID           +   " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						EQUIPMENTID      +   " TEXT  ,"+
						EQUIPMENTTYPE    +   " TEXT  ,"+
						CUSTOMER         +   " TEXT  ,"+
						MANUFACTURER     +   " TEXT  ,"+
						MODELNO          +   " TEXT  ,"+
						SERIAL           +   " TEXT  ,"+
						INSTALLATIONDATE +   " TEXT  ,"+
						LASTUPDATEDATE   +   " TEXT   "+
						");"

				);





			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

			db.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);

			onCreate(db);
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



			DB.insert(TABLE_NAME, null, cv);


		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ERROR", e.getMessage());
		}
	}



}

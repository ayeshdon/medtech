package com.hseya.medtech.sync.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hseya.medtech.MianMenuActivity;
import com.hseya.medtech.R;
import com.hseya.medtech.jobcard.bean.CustomerBean;
import com.hseya.medtech.jobcard.bean.EngineerGetBean;
import com.hseya.medtech.jobcard.bean.ItemsBean;
import com.hseya.medtech.jobcard.sqlite.JobCardDBAccess;
import com.hseya.medtech.utill.ConnectionDetector;
import com.hseya.medtech.utill.MessageClass;
import com.hseya.medtech.ws.UserFunctions;

@SuppressWarnings("deprecation")
public class SyncMainActivity extends Activity implements OnClickListener {

	private Button backBTN, itemBTN, cusBTN, engBTN;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ITEMS = "itemArray";
	private static final String TAG_CUSTOMER = "cusArray";
	private static final String TAG_ENGINEER = "engArray";

	JSONArray ItemsArray = null, CusArray = null, EngArray = null;

	private ArrayList<ItemsBean> itemList;
	private ArrayList<CustomerBean> cusList;
	private ArrayList<EngineerGetBean> engList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.syncdb);

		backBTN = (Button) findViewById(R.id.syncExitBtn);
		backBTN.setOnClickListener(this);
		itemBTN = (Button) findViewById(R.id.syncItemBtn);
		itemBTN.setOnClickListener(this);
		engBTN = (Button) findViewById(R.id.syncEngineer);
		engBTN.setOnClickListener(this);

		cusBTN = (Button) findViewById(R.id.synCusBtn);
		cusBTN.setOnClickListener(this);
	}

	private void backBtnAction() {

		try {

			Intent callSync = new Intent(SyncMainActivity.this,
					MianMenuActivity.class);
			startActivity(callSync);
			SyncMainActivity.this.finish();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {

		backBtnAction();

	}

	@Override
	public void onClick(View v) {
		ConnectionDetector con = new ConnectionDetector(this);
		switch (v.getId()) {
		case R.id.syncExitBtn:
			backBtnAction();

			break;

		case R.id.syncItemBtn:

			if (con.isConnectingToInternet()) {

				new PostToServer().execute("ayesh test").toString();

			} else {
				Toast.makeText(SyncMainActivity.this, MessageClass.NO_INTERNET,
						Toast.LENGTH_LONG).show();

			}

			break;

		case R.id.syncEngineer:

			if (con.isConnectingToInternet()) {

				new PostToServerEng().execute("ayesh test").toString();

			} else {
				Toast.makeText(SyncMainActivity.this, MessageClass.NO_INTERNET,
						Toast.LENGTH_LONG).show();

			}

			break;

		case R.id.synCusBtn:

			if (con.isConnectingToInternet()) {

				new PostToServerCus().execute("ayesh test").toString();

			} else {
				Toast.makeText(SyncMainActivity.this, MessageClass.NO_INTERNET,
						Toast.LENGTH_LONG).show();

			}

			break;
		default:
			break;
		}

	}

	// -------------------------------------------------------------------------
	public static final int progress_bar_type = 1;

	private JSONObject json;
	private ProgressDialog pDialog;

	private class PostToServer extends AsyncTask<String, String, String> {
		private String retrunMsg = "";
		// private ArrayList<ItemsBean> itemList;

		private static final String EQUIPMENTID = "EQUIPMENTID";
		private static final String EQUIPMENTTYPE = "EQUIPMENTTYPE";
		private static final String CUSTOMER = "CUSTOMER";
		private static final String MANUFACTURER = "MANUFACTURER";
		private static final String MODELNO = "MODELNO";
		private static final String SERIAL = "SERIAL";
		private static final String INSTALLATIONDATE = "INSTALLATIONDATE";

		@Override
		protected void onPostExecute(String result) {
			try {

				dismissDialog(progress_bar_type);

				JobCardDBAccess dbClass = new JobCardDBAccess(
						SyncMainActivity.this);
				dbClass.openDB();
				dbClass.dropTable();
				for (int i = 0; i < itemList.size(); i++) {

					dbClass.insertItemDB(itemList.get(i));

				}
				dbClass.closeDB();

				Toast.makeText(SyncMainActivity.this, retrunMsg,
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(SyncMainActivity.this,
						MessageClass.ITEM_LOAD_ERROR, Toast.LENGTH_LONG).show();
			}

		}

		@Override
		protected String doInBackground(String... params) {

			try {

				UserFunctions userFunction = new UserFunctions();
				json = userFunction.getItemListFunction();

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					ItemsArray = json.getJSONArray(TAG_ITEMS);

					itemList = new ArrayList<ItemsBean>();

					for (int i = 0; i < ItemsArray.length(); i++) {
						JSONObject c = ItemsArray.getJSONObject(i);

						ItemsBean bean = new ItemsBean();

						bean.setEquipmentID(c.getString(EQUIPMENTID));
						bean.setEquipmentType(c.getString(EQUIPMENTTYPE));
						bean.setCustomer(c.getString(CUSTOMER));
						bean.setManufactureDate(c.getString(MANUFACTURER));
						bean.setModelNo(c.getString(MODELNO));
						bean.setSerialNo(c.getString(SERIAL));
						bean.setInstallDate(c.getString(INSTALLATIONDATE));

						itemList.add(bean);

					}

					retrunMsg = MessageClass.ITEM_SYNC_SCUCESS;

				} else {
					retrunMsg = json.getString("msg");
				}

				Log.d("MAIN", json.toString());
				return retrunMsg;

			} catch (Exception e) {
				retrunMsg = MessageClass.ITEM_LOAD_ERROR;
			}
			return retrunMsg;
		}

		@Override
		protected void onProgressUpdate(String... values) {

			Toast.makeText(SyncMainActivity.this, values[0], Toast.LENGTH_LONG)
					.show();

			pDialog.setProgress(Integer.parseInt(values[0]));

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}
	}

	// ----------------------------------------------------------
	// private JSONObject cusjson,cusjsonLogin;
	// private ProgressDialog pDialog;

	private class PostToServerCus extends AsyncTask<String, String, String> {
		private String retrunMsg = "";
		// private ArrayList<ItemsBean> itemList;

		private static final String CUSTOMERID = "CUSTOMERID";
		private static final String NAME = "NAME";
		private static final String CUSCONTACTNO = "CUSCONTACTNO";
		private static final String CONTACTPERSON = "CONTACTPERSON";
		private static final String PERSONCONTACTNO = "PERSONCONTACTNO";
		private static final String EMAIL = "EMAIL";

		@Override
		protected void onPostExecute(String result) {
			try {

				dismissDialog(progress_bar_type);

				JobCardDBAccess dbClass = new JobCardDBAccess(
						SyncMainActivity.this);
				dbClass.openDB();
				dbClass.dropCustomerTable();
				for (int i = 0; i < cusList.size(); i++) {
					//
					dbClass.insertCustomerDB(cusList.get(i));
					//
				}
				dbClass.closeDB();

				Toast.makeText(SyncMainActivity.this, retrunMsg,
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(SyncMainActivity.this,
						MessageClass.CUSTOMER_LOAD_ERROR, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {

			try {

				UserFunctions userFunction = new UserFunctions();
				json = userFunction.getCustomerListFunction();

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					CusArray = json.getJSONArray(TAG_CUSTOMER);

					cusList = new ArrayList<CustomerBean>();

					for (int i = 0; i < CusArray.length(); i++) {
						JSONObject c = CusArray.getJSONObject(i);
						//
						CustomerBean bean = new CustomerBean();
						//
						bean.setCus_ID(c.getString(CUSTOMERID));
						bean.setCus_name(c.getString(NAME));
						bean.setContactNo(c.getString(CUSCONTACTNO));
						bean.setContactPerson(c.getString(CONTACTPERSON));
						bean.setEmail(c.getString(EMAIL));
						bean.setPersontoContanct(c.getString(PERSONCONTACTNO));
						// bean.setInstallDate(c.getString(INSTALLATIONDATE));
						//
						//
						cusList.add(bean);

					}

					retrunMsg = MessageClass.CUS_SYNC_SCUCESS;

				} else {
					retrunMsg = json.getString("msg");
				}

				Log.d("MAIN", json.toString());
				return retrunMsg;

			} catch (Exception e) {
				e.printStackTrace();
				retrunMsg = MessageClass.CUSTOMER_LOAD_ERROR;
			}
			return retrunMsg;
		}

		@Override
		protected void onProgressUpdate(String... values) {

			// Toast.makeText(SyncMainActivity.this ,values[0]
			// ,Toast.LENGTH_LONG).show();

			pDialog.setProgress(Integer.parseInt(values[0]));

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}
	}

	// ----------------------------------------------------------

	// private JSONObject engjson,engjsonLogin;

	private class PostToServerEng extends AsyncTask<String, String, String> {
		private String retrunMsg = "";

		private static final String ENGINEERID = "ENGINEERID";
		private static final String NAME = "NAME";
		private static final String CONTACTNO = "CONTACTNO";
		private static final String EMAIL = "EMAIL";

		@Override
		protected void onPostExecute(String result) {
			try {

				dismissDialog(progress_bar_type);

				JobCardDBAccess dbClass = new JobCardDBAccess(
						SyncMainActivity.this);
				dbClass.openDB();
				dbClass.dropEngineerTable();
				for (int i = 0; i < engList.size(); i++) {
					//
					dbClass.insertEngineerDB(engList.get(i));
					//
				}
				dbClass.closeDB();

				Toast.makeText(SyncMainActivity.this, retrunMsg,
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(SyncMainActivity.this,
						MessageClass.CUSTOMER_LOAD_ERROR, Toast.LENGTH_LONG)
						.show();
			}

		}

		@Override
		protected String doInBackground(String... params) {

			try {

				UserFunctions userFunction = new UserFunctions();
				json = userFunction.getEngineerListFunction();

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					EngArray = json.getJSONArray(TAG_ENGINEER);

					engList = new ArrayList<EngineerGetBean>();

					for (int i = 0; i < EngArray.length(); i++) {
						JSONObject c = EngArray.getJSONObject(i);

						EngineerGetBean bean = new EngineerGetBean();

						bean.setID(c.getString(ENGINEERID));
						bean.setEngName(c.getString(NAME));
						bean.setContactNO(c.getString(CONTACTNO));
						bean.setEmail(c.getString(EMAIL));

						engList.add(bean);

					}
					//
					retrunMsg = MessageClass.ENG_SYNC_SCUCESS;

				} else {
					retrunMsg = json.getString("msg");
				}

				// Log.d("MAIN", json.toString());
				return retrunMsg;

			} catch (Exception e) {
				e.printStackTrace();
				retrunMsg = MessageClass.CUSTOMER_LOAD_ERROR;
			}
			return retrunMsg;
		}

		@Override
		protected void onProgressUpdate(String... values) {

			// Toast.makeText(SyncMainActivity.this ,values[0]
			// ,Toast.LENGTH_LONG).show();

			pDialog.setProgress(Integer.parseInt(values[0]));

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}
	}

	// ----------------------------------------------------------

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage(" Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setCancelable(true);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}

}

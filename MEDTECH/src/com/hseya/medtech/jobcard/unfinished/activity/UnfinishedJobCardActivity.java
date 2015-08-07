package com.hseya.medtech.jobcard.unfinished.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hseya.medtech.MianMenuActivity;
import com.hseya.medtech.R;
import com.hseya.medtech.jobcard.activity.JobCardActivity;
import com.hseya.medtech.jobcard.bean.EngineerDataBean;
import com.hseya.medtech.jobcard.bean.PartsBean;
import com.hseya.medtech.jobcard.sqlite.JobCardDBAccess;
import com.hseya.medtech.jobcard.unfinished.bean.UnfinishedDataBean;
import com.hseya.medtech.utill.ConnectionDetector;
import com.hseya.medtech.utill.ConstList;
import com.hseya.medtech.utill.MessageClass;
import com.hseya.medtech.utill.VarList;
import com.hseya.medtech.ws.UserFunctions;

public class UnfinishedJobCardActivity extends Activity implements
		OnItemClickListener {

	private ListView listView;
	private List<UnfinishedDataBean> arrayOfList = new ArrayList<UnfinishedDataBean>();

	private static String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.unfinishedjobcradlist);

			listView = (ListView) findViewById(R.id.listviewdisplay);
			listView.setOnItemClickListener(this);

			setupList();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void setupList() {
		try {
			
			JobCardDBAccess sqlObj = new JobCardDBAccess(this);

			sqlObj.openDB();
			if (VarList.SELETCED_BTN.equals(ConstList.FINISHED_JOBCARD)) {

				arrayOfList = sqlObj.getAll(1);

			} else if (VarList.SELETCED_BTN
					.equals(ConstList.UNFINISHED_JOBCARD)) {

				arrayOfList = sqlObj.getAll(0);
			}

			sqlObj.closeDB();

			setAdapterToListview();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int pos, long arg3) {

		
		

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(this.getTitle()+ " decision");
		alertDialogBuilder.setMessage("Are you sure?");
		alertDialogBuilder.setPositiveButton("Open",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {


				openJobCard(pos);
			} 

		});

		alertDialogBuilder.setNegativeButton("Close",new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog,int id) {

				dialog.cancel();

				dialog.dismiss();



			}

		});

		alertDialogBuilder.setNeutralButton("Delete",new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialdeleteAllog,int id) {

				if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD) ){


					JobCardDBAccess db=new JobCardDBAccess(UnfinishedJobCardActivity.this);
					db.deleteAll(arrayOfList.get(pos).getKeyID());

					setupList();


				}
			}

			
		});



		AlertDialog alertDialog = alertDialogBuilder.create();

		// show alert

		alertDialog.show();

	}

	private void openJobCard(int pos) {
		try {
			
			
			VarList.SELECTED_ID = arrayOfList.get(pos).getKeyID();
			VarList.SELECTED_ID_VAL = arrayOfList.get(pos).getVal();

			if (VarList.SELETCED_BTN.equals(ConstList.FINISHED_JOBCARD)) {

				JobCardDBAccess db = new JobCardDBAccess(this);

				db.openDB();
				VarList.UPLOADBEAN = db.getAllDetails(arrayOfList.get(pos)
						.getKeyID());
				db.closeDB();

				ConnectionDetector con = new ConnectionDetector(this);

				if (con.isConnectingToInternet()) {
					new PostToServerEng().execute("");
				} else {
					Toast.makeText(UnfinishedJobCardActivity.this,
							MessageClass.NO_INTERNET, Toast.LENGTH_LONG).show();
				}

			} else if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {

				Intent call = new Intent(UnfinishedJobCardActivity.this,
						JobCardActivity.class);
				startActivity(call);
				UnfinishedJobCardActivity.this.finish();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setAdapterToListview() {
		NewsRowAdapter objAdapter = new NewsRowAdapter(
				UnfinishedJobCardActivity.this, R.layout.listview, arrayOfList);
		listView.setAdapter(objAdapter);
	}

	@Override
	public void onBackPressed() {

		Intent call = new Intent(UnfinishedJobCardActivity.this,
				MianMenuActivity.class);
		startActivity(call);
		UnfinishedJobCardActivity.this.finish();
	}

	// ----------------------------------------------------------

	public static final int progress_bar_type = 1;

	private JSONObject json;
	private ProgressDialog pDialog;

	private class PostToServerEng extends AsyncTask<String, String, String> {
		private String retrunMsg = "";


		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			dismissDialog(progress_bar_type);

			VarList.UPLOADBEAN = null;

			Toast.makeText(UnfinishedJobCardActivity.this, retrunMsg,
					Toast.LENGTH_LONG).show();

		}

		@Override
		protected String doInBackground(String... params) {

			try {

				UserFunctions userFunction = new UserFunctions();
				json = userFunction.uploadJobCard(VarList.UPLOADBEAN);


				int success = json.getInt(TAG_SUCCESS);
				String ID = json.getString("count");

				if (success == 1) {
					ArrayList<EngineerDataBean> beanList = VarList.UPLOADBEAN
							.getEngBean();


					for (int i = 0; i < beanList.size(); i++) {

						json = userFunction.uploadEngineerDetail(
								beanList.get(i), ID, String.valueOf(i));

					}

					ArrayList<PartsBean> partbean = VarList.UPLOADBEAN
							.getPartBean();

					for (int i = 0; i < partbean.size(); i++) {

						json = userFunction.uploadPartDetail(partbean.get(i),
								ID);

					}

					try {

						Boolean flag = true;
						try {
							json = userFunction.createPDF(ID,
									VarList.UPLOADBEAN.getEngineerUser());


						} catch (Exception e) {

							Toast.makeText(UnfinishedJobCardActivity.this,
									MessageClass.JOBCARD_UPLOAD_ERROR,
									Toast.LENGTH_LONG).show();
							flag = false;
						}

						int successPDF = json.getInt(TAG_SUCCESS);

						if (successPDF == 1) {
							if (flag) {

								JobCardDBAccess dbClass = new JobCardDBAccess(
										UnfinishedJobCardActivity.this);

								dbClass.openDB();
								dbClass.deleteEngineerDetails(VarList.SELECTED_ID);
								dbClass.deleteJobcard(VarList.SELECTED_ID);
								dbClass.closeDB();

								retrunMsg = MessageClass.JOBCARD_UPLOAD_SCUCESS;

								Intent call = new Intent(
										UnfinishedJobCardActivity.this,
										MianMenuActivity.class);
								startActivity(call);
								UnfinishedJobCardActivity.this.finish();

							}

						}

					} catch (Exception e) {
						Toast.makeText(UnfinishedJobCardActivity.this,
								MessageClass.JOBCARD_UPLOAD_ERROR,
								Toast.LENGTH_LONG).show();

					}

				} else {
					retrunMsg = json.getString("msg");
				}

				return retrunMsg;

			} catch (Exception e) {
				e.printStackTrace();
				retrunMsg = MessageClass.UPLOAD_JOBCARD_LOAD_ERROR;
			}
			return retrunMsg;
		}

		@Override
		protected void onProgressUpdate(String... values) {

			Toast.makeText(UnfinishedJobCardActivity.this, values[0],
					Toast.LENGTH_LONG).show();

			pDialog.setProgress(Integer.parseInt(values[0]));

		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Uploading....");
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

package com.hseya.medtech;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hseya.medtech.jobcard.activity.JobCardActivity;
import com.hseya.medtech.jobcard.bean.CustomerBean;
import com.hseya.medtech.jobcard.bean.EngineerGetBean;
import com.hseya.medtech.jobcard.bean.EquickmentBean;
import com.hseya.medtech.jobcard.sqlite.JobCardDBAccess;
import com.hseya.medtech.jobcard.unfinished.activity.UnfinishedJobCardActivity;
import com.hseya.medtech.sync.activity.SyncMainActivity;
import com.hseya.medtech.utill.ConstList;
import com.hseya.medtech.utill.VarList;

public class MianMenuActivity extends Activity implements OnClickListener {

	private Button createJobCardBtn, exitBtn, syncDBBtn, upladJobsBtn,
			unfinisedCard, signOutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);

		loadFromDataBase();

		TextView userIDtxt = (TextView) findViewById(R.id.userID);
		userIDtxt.setText("Welcome " + VarList.USER_NAME);

		createJobCardBtn = (Button) findViewById(R.id.createjobcardBtn);
		createJobCardBtn.setOnClickListener(this);

		signOutBtn = (Button) findViewById(R.id.signOutBtn);
		signOutBtn.setOnClickListener(this);

		exitBtn = (Button) findViewById(R.id.exitBtn);
		exitBtn.setOnClickListener(this);

		syncDBBtn = (Button) findViewById(R.id.syncdbBtn);
		syncDBBtn.setOnClickListener(this);

		upladJobsBtn = (Button) findViewById(R.id.uploadjobBtn);
		upladJobsBtn.setOnClickListener(this);

		unfinisedCard = (Button) findViewById(R.id.unfinishedJobcard);
		unfinisedCard.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.createjobcardBtn:

			VarList.SELETCED_BTN = ConstList.CREATE_JOBCARD;

			Intent call = new Intent(MianMenuActivity.this,
					JobCardActivity.class);
			startActivity(call);
			MianMenuActivity.this.finish();

			break;

		case R.id.syncdbBtn:

			Intent callSync = new Intent(MianMenuActivity.this,
					SyncMainActivity.class);
			startActivity(callSync);
			MianMenuActivity.this.finish();

			break;

		case R.id.uploadjobBtn:
			/* upload unfinished job card */

			VarList.SELETCED_BTN = ConstList.FINISHED_JOBCARD;

			Intent call2 = new Intent(MianMenuActivity.this,
					UnfinishedJobCardActivity.class);
			startActivity(call2);
			MianMenuActivity.this.finish();

			break;

		case R.id.exitBtn:

			exitAction();
			break;

		case R.id.unfinishedJobcard:

			VarList.SELETCED_BTN = ConstList.UNFINISHED_JOBCARD;

			Intent call3 = new Intent(MianMenuActivity.this,
					UnfinishedJobCardActivity.class);
			startActivity(call3);
			MianMenuActivity.this.finish();

			break;

		case R.id.signOutBtn:

			logoutAction();

			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		exitAction();
	}

	/*
	 * app exit action with out logout
	 */
	private void exitAction() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Exit");

		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure you want to exit without logout?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								MianMenuActivity.this.finish();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.logoutmenu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		/*
		 * log out action for menu item
		 */
		if (item.getItemId() == R.id.logoutmenu) {

			logoutAction();

		}

		return super.onOptionsItemSelected(item);
	}

	private void logoutAction() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Logout");

		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure you want to logout?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								SharedPreferences loginSetting = getSharedPreferences(
										ConstList.SHARED_FILE, 0);
								SharedPreferences.Editor editor = loginSetting
										.edit();
								editor.putBoolean(ConstList.LOGIN_FLAG, false);
								editor.commit();

								Intent call = new Intent(MianMenuActivity.this,
										LoginActivity.class);
								startActivity(call);
								MianMenuActivity.this.finish();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	/*
	 * load constant data from data base
	 */
	private void loadFromDataBase() {

		// ----- add customer list ----

		ArrayList<CustomerBean> addCusBean = new ArrayList<CustomerBean>();

		JobCardDBAccess db = new JobCardDBAccess(this);

		db.openDB();

		addCusBean = db.getAllCustomer();

		db.closeDB();

		VarList.CUSTOMER_LIST = addCusBean;

		// ---------------------------------------------

		ArrayList<EquickmentBean> addEQBean = new ArrayList<EquickmentBean>();

		JobCardDBAccess dbClass = new JobCardDBAccess(this);

		dbClass.openDB();

		addEQBean = dbClass.getAllItems();

		dbClass.closeDB();

		VarList.EQCIPMENTS_LIST = addEQBean;

		// ----------------------------

		// ----- add engineer list ---
		ArrayList<EngineerGetBean> engNameList = new ArrayList<EngineerGetBean>();

		JobCardDBAccess dbEng = new JobCardDBAccess(this);

		dbEng.openDB();

		engNameList = dbEng.getAllEngineer();

		dbEng.closeDB();

		VarList.ENGINEER_LIST = engNameList;

		// -----

	}

}

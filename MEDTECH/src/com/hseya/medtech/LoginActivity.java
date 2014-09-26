package com.hseya.medtech;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hseya.medtech.utill.ConnectionDetector;
import com.hseya.medtech.utill.ConstList;
import com.hseya.medtech.utill.MessageClass;
import com.hseya.medtech.utill.VarList;
import com.hseya.medtech.ws.UserFunctions;

public class LoginActivity extends Activity implements OnClickListener ,OnTouchListener{

	private EditText userNameTxt,passwordTxt;
	private Button loginBtn;
	private String userName,password;
	private static final String TAG_SUCCESS     = "success";
	private static final String TAG_USERID      = "userID";
	private View layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);


		SharedPreferences loginSetting=getSharedPreferences(ConstList.SHARED_FILE, 0);
		Boolean flag=loginSetting.getBoolean(ConstList.LOGIN_FLAG, false);



		if (flag) {

			VarList.USER_NAME=loginSetting.getString(ConstList.USER_NAME, "");
			VarList.LOGINUSER_ID=loginSetting.getString(ConstList.USER_ID, "");

			System.out.println("login user name : "+VarList.USER_NAME);
			System.out.println("login user id : "+VarList.LOGINUSER_ID); 

			Intent call=new Intent(LoginActivity.this,MianMenuActivity.class);
			startActivity(call);
			LoginActivity.this.finish();

		} else {



			setContentView(R.layout.login);

			layout = findViewById(R.id.loginlayout);
			layout.setOnTouchListener(this);




			userNameTxt=(EditText) findViewById(R.id.userNameET);
			passwordTxt=(EditText) findViewById(R.id.passwordET);


			loginBtn=(Button) findViewById(R.id.loginBT);
			loginBtn.setOnClickListener(this);

		}
	}






	@Override
	public void onClick(View v) {


		switch (v.getId()) {
		case R.id.loginBT: 



			userName=userNameTxt.getText().toString();
			password=passwordTxt.getText().toString();

			ConnectionDetector con=new ConnectionDetector(this);

			if (con.isConnectingToInternet()) {

				if (userName.equals("")) { 
					Toast.makeText(this,MessageClass.LOGIN_USERNAME_EMPTY ,Toast.LENGTH_LONG).show();

				}else if (password.equals("")) { 
					Toast.makeText(this,MessageClass.LOGIN_PASSWORD_EMPTY ,Toast.LENGTH_LONG).show();

				}else{

					//---------------check login with remote database

					VarList.USERNAME=userName;
					VarList.PASSWORD=password;

					new PostToServer().execute("");


				}







			}else {
				Toast.makeText(LoginActivity.this	,MessageClass.NO_INTERNET ,Toast.LENGTH_LONG).show();
			}







			break;

		default:
			break;
		}



	}






	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		if(v==layout){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
			return true;
		}
		return false;
	}



	public static final int progress_bar_type = 1;

	private JSONObject json;
	private ProgressDialog pDialog;
	int success =0;
	private String userID="";



	private class PostToServer extends AsyncTask<String, String, String>{
		private String retrunMsg="";
		//		private ArrayList<ItemsBean> itemList;





		@Override
		protected void onPostExecute(String result) {
			dismissDialog(progress_bar_type);

			if (success==1) {

				VarList.ISVALIDLOGIN=true;

				SharedPreferences loginSetting=getSharedPreferences(ConstList.SHARED_FILE, 0);
				SharedPreferences.Editor editor=loginSetting.edit();
				editor.putBoolean(ConstList.LOGIN_FLAG, true); 
				editor.putString(ConstList.USER_NAME, userName); 
				editor.putString(ConstList.USER_ID, userID); 
				editor.commit();

				VarList.USER_NAME=userName;
				VarList.LOGINUSER_ID=userID;



				Intent call=new Intent(LoginActivity.this,MianMenuActivity.class);
				startActivity(call);
				LoginActivity.this.finish();	



			}else{
				Toast.makeText(LoginActivity.this,MessageClass.LOGIN_INVALID ,Toast.LENGTH_LONG).show();
				VarList.ISVALIDLOGIN=false;
			}



		}

		@Override
		protected String doInBackground(String... params) {

			try {


				UserFunctions userFunction = new UserFunctions();
				json = userFunction.getLoginValueFunction(VarList.USERNAME, VarList.PASSWORD);


				success = json.getInt(TAG_SUCCESS);
				userID= json.getString(TAG_USERID);


				Log.d("MAIN", json.toString());
				return retrunMsg;

			} catch (Exception e) {
				retrunMsg=MessageClass.ITEM_LOAD_ERROR;
			}
			return retrunMsg; 
		}

		@Override
		protected void onProgressUpdate(String... values) {


			pDialog.setProgress(Integer.parseInt(values[0]));


		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}
	}

	//----------------------------------------------------------



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

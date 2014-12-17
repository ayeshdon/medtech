package com.hseya.medtech.jobcard.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hseya.medtech.MianMenuActivity;
import com.hseya.medtech.R;
import com.hseya.medtech.jobcard.bean.EngineerDataBean;
import com.hseya.medtech.jobcard.bean.JobCardDataBean;
import com.hseya.medtech.jobcard.bean.PartsBean;
import com.hseya.medtech.jobcard.sqlite.JobCardDBAccess;
import com.hseya.medtech.utill.ConnectionDetector;
import com.hseya.medtech.utill.ConstList;
import com.hseya.medtech.utill.MessageClass;
import com.hseya.medtech.utill.ValidateClass;
import com.hseya.medtech.utill.VarList;
import com.hseya.medtech.ws.UserFunctions;

public class CaptureSignature extends Activity implements OnClickListener {

	private static String TAG_SUCCESS = "success";
	private Dialog engDailog, partDailog, jobcardDailog;

	private LinearLayout mContent;
	private signature mSignature;
	private Button mClear, mGetSign, mCancel, viewPartsBtn, viewEngBtn,
			viewJobCard, saveLocalBtn;
	public static String tempDir;
	public int count = 1;
	public String current = null, engCurrent = null;
	private Bitmap mBitmap;
	private View mView;
	private File mypath;
	public static final int progress_bar_type = 1;
	private int index = 1;
	private int SIZE = 0;
	private ArrayList<PartsBean> beanList = null;
	private ArrayList<EngineerDataBean> engBeanList = null;

	private CheckBox inspectionCheck, visualCheck;

	private JobCardDataBean databean;

	private String uniqueId;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signature);

		viewPartsBtn = (Button) findViewById(R.id.viewParts);
		viewPartsBtn.setOnClickListener(this);

		visualCheck = (CheckBox) findViewById(R.id.visualChck);
		visualCheck.setOnClickListener(this);
		inspectionCheck = (CheckBox) findViewById(R.id.inspectionChck);
		inspectionCheck.setOnClickListener(this);

		viewEngBtn = (Button) findViewById(R.id.viewEng);
		viewEngBtn.setOnClickListener(this);

		viewJobCard = (Button) findViewById(R.id.viewJobcard);
		viewJobCard.setOnClickListener(this);

		saveLocalBtn = (Button) findViewById(R.id.saveLocalBtn);
		saveLocalBtn.setOnClickListener(this);

		databean = VarList.JOB_BEAN;
		tempDir = Environment.getExternalStorageDirectory() + "/"
				+ getResources().getString(R.string.external_dir) + "/";

		File directory = new File(tempDir);

		if (!directory.exists()) {
			directory.mkdirs();

		}

		uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_"
				+ Math.random() + "_" + VarList.USER_NAME + "CUS";
		// engUniqueId =getTodaysDate() + "_" + getCurrentTime() + "_" +
		// Math.random()+"_"+VarList.USER_NAME+"ENG";

		current = uniqueId + ".png";
		// engCurrent = engUniqueId + ".png";

		mypath = new File(directory, current);
		// engmypath = new File(directory,engCurrent);

		mContent = (LinearLayout) findViewById(R.id.customerSignature);
		// engContent = (LinearLayout)
		// findViewById(R.id.egineerSignaturelayout);

		mSignature = new signature(this, null, "C");
		// engSignature = new signature(this, null,"E");

		mSignature.setBackgroundColor(Color.WHITE);
		// engSignature.setBackgroundColor(Color.WHITE);

		mContent.addView(mSignature, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		// engContent.addView(engSignature, LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT);

		mClear = (Button) findViewById(R.id.clear);
		mGetSign = (Button) findViewById(R.id.getsign);
		mGetSign.setEnabled(false);
		saveLocalBtn.setEnabled(false);

		mCancel = (Button) findViewById(R.id.cancel);

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// ----------------------------------------------------------

				boolean isItwork = true;

				JobCardDataBean jobBeanSave = VarList.JOB_BEAN;

				if (VarList.SELETCED_BTN.equals(ConstList.CREATE_JOBCARD)) {
					try {

						// save data
						JobCardDBAccess dbSQl = new JobCardDBAccess(
								CaptureSignature.this);
						dbSQl.openDB();
						dbSQl.insertENG(jobBeanSave);

						dbSQl.closeDB();

					} catch (Exception e) {
						isItwork = false;
						e.printStackTrace();

					} finally {

						// resetFeild();

						if (isItwork) {

							Toast.makeText(
									CaptureSignature.this,
									MessageClass.JOBCARD_UPLOAD_LOCAL_PART_SUCCESS,
									Toast.LENGTH_LONG).show();

							VarList.JOB_BEAN = null;

							Intent call2 = new Intent(CaptureSignature.this,
									MianMenuActivity.class);
							startActivity(call2);
							CaptureSignature.this.finish();

						} else {

							Toast.makeText(CaptureSignature.this,
									MessageClass.JOBCARD_FIAL_LOCAL_SUCCESS,
									Toast.LENGTH_LONG).show();

						}
					}
				} else if (VarList.SELETCED_BTN
						.equals(ConstList.UNFINISHED_JOBCARD)) {
					try {
						// update data

						JobCardDBAccess dbSQl = new JobCardDBAccess(
								CaptureSignature.this);
						dbSQl.openDB();

						// dbSQl.insertENG(jobBeanSave);
						dbSQl.updateENG(jobBeanSave,
								Long.parseLong(VarList.SELECTED_ID));
						
						
						dbSQl.closeDB();

					} catch (Exception e) {
						isItwork = false;
						e.printStackTrace();
					} finally {

						// resetFeild();

						if (isItwork) {

							Toast.makeText(
									CaptureSignature.this,
									MessageClass.JOBCARD_UPLOAD_LOCAL_PART_SUCCESS,
									Toast.LENGTH_LONG).show();

							VarList.JOB_BEAN = null;

							Intent call2 = new Intent(CaptureSignature.this,
									MianMenuActivity.class);
							startActivity(call2);
							CaptureSignature.this.finish();

						} else {

							Toast.makeText(CaptureSignature.this,
									MessageClass.JOBCARD_FIAL_LOCAL_SUCCESS,
									Toast.LENGTH_LONG).show();

						}
					}

				}

				// ----------------------------------------------------------

			}
		});

		mView = mContent;
		// engView=engContent;

		mClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mSignature.clear();
				// engSignature.clear();

				mGetSign.setEnabled(false);
				saveLocalBtn.setEnabled(false);

			}

		});

		mGetSign.setOnClickListener(new OnClickListener() {
			public void onClick(View v)

			{

				try {

					EditText sendEmail = (EditText) findViewById(R.id.sendEmail);
					String sendMail = sendEmail.getText().toString();

					boolean flag = true;

					if (!sendMail.equals("")) {

						if (!ValidateClass.isValidEmailAddress(sendMail)) {

							flag = false;
							Toast.makeText(CaptureSignature.this,
									MessageClass.SENDEMAIL_INVALID,
									Toast.LENGTH_LONG).show();

						} else {
							flag = true;
						}

					} else {

						flag = true;
					}

					if (flag) {
						mView.setDrawingCacheEnabled(true);

						mSignature.save(mView, "C");

						databean.setEngineerSignPath(sendMail);
						databean.setEmailContent("");
						VarList.JOB_BEAN = null;
						VarList.JOB_BEAN = databean;

						ConnectionDetector cd = new ConnectionDetector(
								getApplicationContext());

						if (cd.isConnectingToInternet()) {

							new PostToServerEng().execute("");

						} else {

							updateToLocalDataBase(getResources().getString(
									R.string.no_internet_save_local));
						}

					}

					// Log.d("Test", "asdas");
				} catch (Exception e) {

					e.printStackTrace();
					Toast.makeText(CaptureSignature.this,
							"Unknown Error form Server ", Toast.LENGTH_LONG)
							.show();
				}
			}

		});

	}

	/*
	 * update data to local database
	 */
	private void updateToLocalDataBase(String msg) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Information")
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								saveContact();

							}
						}).setNegativeButton("No", null) // Do nothing on no
				.show();

		// --------------------------------

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private String getTodaysDate() {

		final Calendar c = Calendar.getInstance();
		int todaysDate = (c.get(Calendar.YEAR) * 10000)
				+ ((c.get(Calendar.MONTH) + 1) * 100)
				+ (c.get(Calendar.DAY_OF_MONTH));
		return (String.valueOf(todaysDate));

	}

	private String getCurrentTime() {

		final Calendar c = Calendar.getInstance();
		int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000)
				+ (c.get(Calendar.MINUTE) * 100) + (c.get(Calendar.SECOND));
		return (String.valueOf(currentTime));

	}

	public class signature extends View {
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		// private String FLAG;
		private final RectF dirtyRect = new RectF();

		public signature(Context context, AttributeSet attrs, String FLAG) {
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		public void save(View v, String check) {

			if (check.equals("C")) {
				// customer signature

				if (mBitmap == null) {
					mBitmap = Bitmap.createBitmap(mContent.getWidth(),
							mContent.getHeight(), Bitmap.Config.RGB_565);
					;
				}

				Canvas canvas = new Canvas(mBitmap);
				try {
					FileOutputStream mFileOutStream = new FileOutputStream(
							mypath);

					v.draw(canvas);
					mBitmap.compress(Bitmap.CompressFormat.PNG, 90,
							mFileOutStream);
					mFileOutStream.flush();
					mFileOutStream.close();
					// Images.Media.insertImage(getContentResolver(), mBitmap,
					// "title", null);
					databean.setCustomerSignPath(mypath.getPath());

				} catch (Exception e) {
					Log.v("log_tag", "-----------------------------------rrrr");
					e.printStackTrace();
				}

			}

		}

		public void clear() {
			path.reset();
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float eventX = event.getX();
			float eventY = event.getY();

			mGetSign.setEnabled(true);
			saveLocalBtn.setEnabled(true);

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				path.moveTo(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
				return true;

			case MotionEvent.ACTION_MOVE:

			case MotionEvent.ACTION_UP:

				resetDirtyRect(eventX, eventY);
				int historySize = event.getHistorySize();
				for (int i = 0; i < historySize; i++) {
					float historicalX = event.getHistoricalX(i);
					float historicalY = event.getHistoricalY(i);
					expandDirtyRect(historicalX, historicalY);
					path.lineTo(historicalX, historicalY);
				}
				path.lineTo(eventX, eventY);
				break;

			default:
				debug("Ignored touch event: " + event.toString());
				return false;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;

			return true;
		}

		private void debug(String string) {
		}

		private void expandDirtyRect(float historicalX, float historicalY) {
			if (historicalX < dirtyRect.left) {
				dirtyRect.left = historicalX;
			} else if (historicalX > dirtyRect.right) {
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top) {
				dirtyRect.top = historicalY;
			} else if (historicalY > dirtyRect.bottom) {
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY) {
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Warning");

		alertDialogBuilder
				.setMessage("If you go back this job card will be lost")
				.setCancelable(false)
				.setPositiveButton("Go to home",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								Intent call = new Intent(CaptureSignature.this,
										MianMenuActivity.class);
								startActivity(call);
								CaptureSignature.this.finish();

							}
						})
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	public void saveContact() {
		boolean isItwork = true;
		try {

			JobCardDBAccess dbClass = new JobCardDBAccess(this);

			dbClass.openDB();

			if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {

				dbClass.updateFull(VarList.JOB_BEAN,
						Long.parseLong(VarList.SELECTED_ID));

			} else {

				dbClass.insertDB(VarList.JOB_BEAN);

			}

			dbClass.closeDB();

		} catch (Exception e) {
			isItwork = false;
			e.printStackTrace();
		} finally {
			if (isItwork) {

				Toast.makeText(CaptureSignature.this,
						MessageClass.JOBCARD_UPLOAD_LOCAL_SUCCESS,
						Toast.LENGTH_LONG).show();

				VarList.JOB_BEAN = null;

				Intent call = new Intent(CaptureSignature.this,
						MianMenuActivity.class);
				startActivity(call);
				CaptureSignature.this.finish();

			} else {

				Toast.makeText(CaptureSignature.this,
						MessageClass.JOBCARD_FIAL_LOCAL_SUCCESS,
						Toast.LENGTH_LONG).show();

			}

		}

	}

	private ProgressDialog pDialog;

	// ----------------------------------------------------------

	// public static final int progress_bar_type = 1;

	private JSONObject json;

	// private ProgressDialog pDialog;

	private class PostToServerEng extends AsyncTask<String, String, String> {
		private String retrunMsg = "";

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			try {

				dismissDialog(progress_bar_type);

				VarList.JOB_BEAN = null;

				Toast.makeText(CaptureSignature.this, retrunMsg,
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {

			try {

				UserFunctions userFunction = new UserFunctions();
				VarList.JOB_BEAN.setEngineerUser(VarList.LOGINUSER_ID);

				json = userFunction.uploadJobCard(VarList.JOB_BEAN);

				int success = json.getInt(TAG_SUCCESS);
				String ID = json.getString("count");

				if (success == 1) {
					ArrayList<EngineerDataBean> beanList = VarList.JOB_BEAN
							.getEngBean();

					for (int i = 0; i < beanList.size(); i++) {

						json = userFunction.uploadEngineerDetail(
								beanList.get(i), ID, String.valueOf(i));

					}

					// --------------------------------------------------
					ArrayList<PartsBean> partbean = VarList.JOB_BEAN
							.getPartBean();

					if (partbean != null) {

						if (partbean.size() > 0) {
							for (int i = 0; i < partbean.size(); i++) {

								json = userFunction.uploadPartDetail(
										partbean.get(i), ID);
							}
						}

					}
					// -----------------------------------------------
					json = userFunction.createPDF(ID,
							VarList.JOB_BEAN.getEngineerUser());

					int successPDF = json.getInt(TAG_SUCCESS);

					if (successPDF == 1) {

						if (VarList.SELETCED_BTN
								.equals(ConstList.UNFINISHED_JOBCARD)) {

							JobCardDBAccess dbClass = new JobCardDBAccess(
									CaptureSignature.this);

							dbClass.openDB();
							dbClass.deleteEngineerDetails(VarList.SELECTED_ID);
							dbClass.deleteJobcard(VarList.SELECTED_ID);
							dbClass.closeDB();
						}

						retrunMsg = MessageClass.JOBCARD_UPLOAD_SCUCESS;

						Intent call = new Intent(CaptureSignature.this,
								MianMenuActivity.class);
						startActivity(call);
						CaptureSignature.this.finish();

					}

				} else {
					retrunMsg = json.getString("msg");
				}

				Log.d("MAIN", json.toString());
				return retrunMsg;

			} catch (Exception e) {
				e.printStackTrace();
				retrunMsg = MessageClass.UPLOAD_JOBCARD_LOAD_ERROR;
			}
			return retrunMsg;
		}

		@Override
		protected void onProgressUpdate(String... values) {

			Toast.makeText(CaptureSignature.this, values[0], Toast.LENGTH_LONG)
					.show();

			pDialog.setProgress(Integer.parseInt(values[0]));

		}

		@SuppressWarnings("deprecation")
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

	private EditText partQuantityTxt, partNameTxt, partIDTxt;
	private EditText engNameTxt, engStartDateTxt, engFinishDate, EngWorkTime,
			engTravelTime;
	private EditText customerTXT, visitReasonTXT, serviceTXT,
			fileamentCounterTXT, opendDateTxt, hvCounterTXT, logTimeTXT,
			downtimeTXT, jobcardNoTxt, equicmentTXT;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.viewParts:

			try {

				partDailog = new Dialog(this);
				partDailog.setContentView(R.layout.viewparts_layout);
				partDailog.setCancelable(true);
				partDailog.setTitle("View Used Parts");

				partNameTxt = (EditText) partDailog
						.findViewById(R.id.viewPartNameTxt);
				partQuantityTxt = (EditText) partDailog
						.findViewById(R.id.viewPartQuantityTxt);
				partIDTxt = (EditText) partDailog
						.findViewById(R.id.viewPartIDTxt);

				beanList = VarList.JOB_BEAN.getPartBean();

				SIZE = beanList.size();

				partNameTxt.setText(beanList.get(0).getPartName());
				partQuantityTxt.setText(beanList.get(0).getPartQuantitys());
				partIDTxt.setText(beanList.get(0).getPartID());

				Button cancelBtnEQ = (Button) partDailog
						.findViewById(R.id.viewPartCloaseBtn);
				cancelBtnEQ.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SIZE = 0;
						index = 1;
						beanList = null;

						partDailog.dismiss();

					}
				});

				Button addNextBtn = (Button) partDailog
						.findViewById(R.id.viewPartAddNextBtn);
				addNextBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (SIZE > index) {

							partNameTxt.setText(beanList.get(index)
									.getPartName());
							partQuantityTxt.setText(beanList.get(index)
									.getPartQuantitys());
							partIDTxt.setText(beanList.get(index).getPartID());

							index++;
						} else if (SIZE == index) {
							Toast.makeText(getApplicationContext(),
									"No Parts Available", Toast.LENGTH_LONG)
									.show();
						}

					}
				});

				partDailog.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		// -------------------------------------------------

		case R.id.viewEng:

			try {

				engDailog = new Dialog(this);
				engDailog.setContentView(R.layout.viewengineer_layout);
				engDailog.setCancelable(true);
				engDailog.setTitle("View Engineer Details");

				engNameTxt = (EditText) engDailog
						.findViewById(R.id.viewEngName);
				engStartDateTxt = (EditText) engDailog
						.findViewById(R.id.viewEngStartDate);
				engFinishDate = (EditText) engDailog
						.findViewById(R.id.viewEngFinishDate);
				EngWorkTime = (EditText) engDailog
						.findViewById(R.id.viewTimeworkedEng);
				engTravelTime = (EditText) engDailog
						.findViewById(R.id.viewTravelTimeEng);

				engBeanList = VarList.JOB_BEAN.getEngBean();

				SIZE = engBeanList.size();

				engNameTxt.setText(engBeanList.get(0).getEngName());
				engStartDateTxt.setText(engBeanList.get(0).getJobStart());
				engFinishDate.setText(engBeanList.get(0).getJobFinished());
				EngWorkTime.setText(engBeanList.get(0).getWorkedHours());
				engTravelTime.setText(engBeanList.get(0).getTravelTime());

				Button cancelBtnEQ = (Button) engDailog
						.findViewById(R.id.viewEngClosebtn);
				cancelBtnEQ.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SIZE = 0;
						index = 1;
						engBeanList = null;

						engDailog.dismiss();

					}
				});

				Button addNextBtn = (Button) engDailog
						.findViewById(R.id.viewengineerBtn);
				addNextBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (SIZE > index) {

							engNameTxt.setText(engBeanList.get(index)
									.getEngName());
							engStartDateTxt.setText(engBeanList.get(index)
									.getJobStart());
							engFinishDate.setText(engBeanList.get(index)
									.getJobFinished());
							EngWorkTime.setText(engBeanList.get(index)
									.getWorkedHours());
							engTravelTime.setText(engBeanList.get(index)
									.getTravelTime());

							index++;
						} else if (SIZE == index) {
							Toast.makeText(getApplicationContext(),
									"No Engineer Available", Toast.LENGTH_LONG)
									.show();
						}

					}
				});

				engDailog.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.viewJobcard:

			try {

				jobcardDailog = new Dialog(this);
				jobcardDailog.setContentView(R.layout.viewjobcard_layout);
				jobcardDailog.setCancelable(true);
				jobcardDailog.setTitle("View Jobcard Details");

				opendDateTxt = (EditText) jobcardDailog
						.findViewById(R.id.viewopendate);
				jobcardNoTxt = (EditText) jobcardDailog
						.findViewById(R.id.viewjobcardNo);
				customerTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewcustomertxt);
				equicmentTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewequpmetnTypetxt);
				visitReasonTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewvisitReason);

				serviceTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewservicetxt);
				fileamentCounterTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewfileamentCounterTxt);
				hvCounterTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewhvCounterTxt);
				logTimeTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewlogTimeTxt);
				downtimeTXT = (EditText) jobcardDailog
						.findViewById(R.id.viewdowntimeTxt);

				opendDateTxt.setText(VarList.JOB_BEAN.getJobDate());
				jobcardNoTxt.setText(VarList.JOB_BEAN.getJobCardNO());
				customerTXT.setText(VarList.JOB_BEAN.getCustomer());
				equicmentTXT.setText(VarList.JOB_BEAN.getEqucment());
				visitReasonTXT.setText(VarList.JOB_BEAN.getVisitReason());

				serviceTXT.setText(VarList.JOB_BEAN.getService());
				fileamentCounterTXT.setText(VarList.JOB_BEAN
						.getFilamnetCounter());
				hvCounterTXT.setText(VarList.JOB_BEAN.getHvCounter());
				logTimeTXT.setText(VarList.JOB_BEAN.getLogTime());
				downtimeTXT.setText(VarList.JOB_BEAN.getDownTime());

				Button cancelBtnEQ = (Button) jobcardDailog
						.findViewById(R.id.viewJobcardCloseBtn);
				cancelBtnEQ.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						jobcardDailog.dismiss();

					}
				});

				jobcardDailog.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.visualChck:

			if (((CheckBox) v).isChecked()) {

				databean.setVisualCheck("Y");
			} else {
				databean.setVisualCheck("N");
			}

			break;

		case R.id.inspectionChck:

			if (((CheckBox) v).isChecked()) {

				databean.setInspectionCheck("Y");
			} else {
				databean.setInspectionCheck("N");
			}

			break;

		case R.id.saveLocalBtn:

			try {

				EditText sendEmail = (EditText) findViewById(R.id.sendEmail);
				String sendMail = sendEmail.getText().toString();

				boolean flag = true;

				if (!sendMail.equals("")) {

					if (!ValidateClass.isValidEmailAddress(sendMail)) {

						flag = false;
						Toast.makeText(CaptureSignature.this,
								MessageClass.SENDEMAIL_INVALID,
								Toast.LENGTH_LONG).show();

					} else {
						flag = true;
					}

				} else {

					flag = true;
				}

				if (flag) {
					mView.setDrawingCacheEnabled(true);

					mSignature.save(mView, "C");

					databean.setEngineerSignPath(sendMail);
					databean.setEmailContent("");
					VarList.JOB_BEAN = null;
					VarList.JOB_BEAN = databean;

					updateToLocalDataBase(getResources().getString(
							R.string.save_local));

				}

			} catch (Exception e) {

				e.printStackTrace();
				Toast.makeText(CaptureSignature.this, "Unknown Error  ",
						Toast.LENGTH_LONG).show();
			}

			break;

		default:
			break;
		}
	}

}

package com.hseya.medtech.jobcard.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hseya.medtech.MianMenuActivity;
import com.hseya.medtech.R;
import com.hseya.medtech.jobcard.bean.EngineerDataBean;
import com.hseya.medtech.jobcard.bean.JobCardDataBean;
import com.hseya.medtech.jobcard.sqlite.JobCardDBAccess;
import com.hseya.medtech.utill.ConstList;
import com.hseya.medtech.utill.MessageClass;
import com.hseya.medtech.utill.VarList;

public class EngineerTimeDetailsActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnTouchListener {


	private Spinner eng1Spinner;
	private View layout;
//	private String eng1Name="",eng1ID="";


	private LinearLayout engContent;
	private signature engSignature;
	private Bitmap engBitmap;
	//	private View engView;
	private File engmypath;
	private String engUniqueId;
	public static String tempDir;
	public String engCurrent=null;


	private Button getSignBTN,addNewEngineerBTN,closeBtn,closeSaveBtn;

	private ArrayList<EngineerDataBean> engBean;

	private EditText eng1JobStart,eng1Jobend,eng1Timework,eng1TimeTravel;
	CustomDateTimePicker customDate,customJobStart,customJobFinished;
	private int SIZE=0;
	private File directory;

	private int index=1,key=1;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {



		super.onCreate(savedInstanceState);


		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.engineertime);

		//---------------------------------------------------------
		tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";


		directory=new File(tempDir);

		if (!directory.exists()){
			directory.mkdirs();

		}


		engUniqueId =getTodaysDate() + getCurrentTime() + Math.random()+VarList.USER_NAME+"eng";


		engContent = (LinearLayout) findViewById(R.id.egineerSignaturelayout1);
		engSignature = new signature(this, null,"E");

		engSignature.setBackgroundColor(Color.WHITE);

		engContent.addView(engSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		//--------------------------------------------------------------------------------------


		customDate = new CustomDateTimePicker(this,
				new CustomDateTimePicker.ICustomDateTimeListener() {

			@Override
			public void onSet(Dialog dialog, Calendar calendarSelected,
					Date dateSelected, int year, String monthFullName,
					String monthShortName, int monthNumber, int date,
					String weekDayFullName, String weekDayShortName,
					int hour24, int hour12, int min, int sec,
					String AM_PM) {

			}

			@Override
			public void onCancel() {

			}
		});

		customJobStart = new CustomDateTimePicker(this,
				new CustomDateTimePicker.ICustomDateTimeListener() {

			@Override
			public void onSet(Dialog dialog, Calendar calendarSelected,
					Date dateSelected, int year, String monthFullName,
					String monthShortName, int monthNumber, int date,
					String weekDayFullName, String weekDayShortName,
					int hour24, int hour12, int min, int sec,
					String AM_PM) {

				eng1JobStart.setText( calendarSelected
						.get(Calendar.DAY_OF_MONTH)
						+ ":" + monthShortName + ":" + year
						+ ":" + hour24 + ":" + min
						+ " " + AM_PM);
			}

			@Override
			public void onCancel() {

			}
		});


		customJobFinished = new CustomDateTimePicker(this,
				new CustomDateTimePicker.ICustomDateTimeListener() {

			@Override
			public void onSet(Dialog dialog, Calendar calendarSelected,
					Date dateSelected, int year, String monthFullName,
					String monthShortName, int monthNumber, int date,
					String weekDayFullName, String weekDayShortName,
					int hour24, int hour12, int min, int sec,
					String AM_PM) {

				eng1Jobend.setText( calendarSelected
						.get(Calendar.DAY_OF_MONTH)
						+ ":" + monthShortName + ":" + year
						+ ":" + hour24 + ":" + min
						+ " " + AM_PM);
			}

			@Override
			public void onCancel() {

			}
		});



		//--------------------------------------------------------------------------
		layout = findViewById(R.id.engineerID);
		layout.setOnTouchListener(this);

		getSignBTN=(Button) findViewById(R.id.engsignature);
		getSignBTN.setOnClickListener(this);

		engBean=new ArrayList<EngineerDataBean>();

		addNewEngineerBTN=(Button) findViewById(R.id.addnewengineerBtn);
		addNewEngineerBTN.setOnClickListener(this);

		closeSaveBtn=(Button) findViewById(R.id.savecloseEng);
		closeSaveBtn.setOnClickListener(this);


		closeBtn=(Button) findViewById(R.id.engClosebtn);
		closeBtn.setOnClickListener(this);

		eng1Spinner=(Spinner) findViewById(R.id.spinnerEng1);
		eng1Spinner.setOnItemSelectedListener(this);

		//		ArrayAdapter<String> spinnerList1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, VarList.ENGINEER_NAME_LIST);


		ArrayList<String>eng_name=new ArrayList<String>();


		for (int i = 0; i < VarList.ENGINEER_LIST.size(); i++) {
			eng_name.add(VarList.ENGINEER_LIST.get(i).getEngName());

		}



		ArrayAdapter<String> spinnerList1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, eng_name);
		spinnerList1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eng1Spinner.setAdapter(spinnerList1);



		eng1JobStart   =  (EditText) findViewById(R.id.jobStartEng1);

		eng1JobStart.setInputType(InputType.TYPE_NULL);
		eng1JobStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customJobStart.showDialog();

			}
		});



		eng1Jobend     =  (EditText) findViewById(R.id.jobFinishedEng1);

		eng1Jobend.setInputType(InputType.TYPE_NULL);
		eng1Jobend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customJobFinished.showDialog();

			}
		});

		eng1Timework   =  (EditText) findViewById(R.id.timeworkedEng1);
		eng1TimeTravel =  (EditText) findViewById(R.id.travelTimeEng1);



		//-----------------------------------
		if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {

			JobCardDataBean jobeditBean=VarList.JOB_BEAN;

			engBean=	jobeditBean.getEngBean();
			//----------------------------------------------------
			addNewEngineerBTN.setEnabled(true);
			if (engBean!=null) {



				SIZE =engBean.size();

				if (SIZE>0) {

					String engID=	engBean.get(0).getEngID();
					int j=-1;
					for (int i = 0; i < VarList.ENGINEER_LIST.size(); i++) {

						if (VarList.ENGINEER_LIST.get(i).getID().equals(engID)) {
							j=i;
							break;


						}
					}

					eng1Spinner.setSelection(j);
					//					eng1Date.setText(engBean.get(0).getDate());
					eng1JobStart.setText(engBean.get(0).getJobStart());
					eng1Jobend.setText(engBean.get(0).getJobFinished());
					eng1Timework.setText(engBean.get(0).getWorkedHours());
					eng1TimeTravel.setText(engBean.get(0).getTravelTime()); 
					
					
					//--------------------------------
					eng1Spinner.setEnabled(false);
					eng1JobStart.setEnabled(false);
					eng1Jobend.setEnabled(false);
					eng1TimeTravel.setEnabled(false);
					eng1Timework.setEnabled(false);
					
					//---------------------------------
				}

			}else{

				engBean=new ArrayList<EngineerDataBean>();

			}

		}




		//////////////////////////

	}

	private void backbuttonAction(){

		String dailogHearder="";
		if (VarList.SELETCED_BTN.equals(ConstList.CREATE_JOBCARD)) {

			dailogHearder="If you go back this job card will not save";

		}else if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {

			dailogHearder="If you go back this changer will not save";

		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);


		alertDialogBuilder.setTitle("Warning");


		alertDialogBuilder
		.setMessage(dailogHearder)


		.setCancelable(false)
		.setPositiveButton("Exit Job Card",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				Intent call=new Intent(EngineerTimeDetailsActivity.this,MianMenuActivity.class);
				startActivity(call);
				EngineerTimeDetailsActivity.this.finish();

			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				dialog.cancel();
			}
		});


		AlertDialog alertDialog = alertDialogBuilder.create();


		alertDialog.show();
	}


	@Override
	public void onBackPressed() {

		backbuttonAction();



	}




	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int pos,
			long arg3) {

		switch (v.getId()) {
		case R.id.spinnerEng1:

//			eng1Name=VarList.ENGINEER_LIST.get(pos).getEngName();
//			eng1ID=VarList.ENGINEER_LIST.get(pos).getID();

			break;



		default:
			break;
		}



	}




	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}




	@Override
	public void onClick(View v) {
		try {

			switch (v.getId()) {
			case R.id.addnewengineerBtn:

				if (engineerValidation()) {


					//-------------------------------------------------------------------------------------
					if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) { 




						if (index<=SIZE) { 


							//-----------------------------------------------
							//							engBean.remove(index);
							EngineerDataBean bean1=new EngineerDataBean();

							bean1.setEngName(eng1Spinner.getSelectedItem().toString());
							bean1.setEngID(VarList.ENGINEER_LIST.get(eng1Spinner.getSelectedItemPosition()).getID());
							bean1.setJobStart(eng1JobStart.getText().toString());
							bean1.setJobFinished(eng1Jobend.getText().toString());
							bean1.setTravelTime(eng1TimeTravel.getText().toString());
							bean1.setWorkedHours(eng1Timework.getText().toString());
							bean1.setSignPath(engBean.get(index-1).getSignPath());
							//							bean1.setDate(eng1Date.getText().toString());

							if (engBean.get(index-1)!=null) {
								bean1.setID(engBean.get(index-1).getID()); 

							}
							bean1.setFlag("0");
							engBean.set(index-1, bean1);

							//-----------------------------

							if (index<SIZE) { 
								if (engBean.get(index)!=null) {


									String engID=	engBean.get(index).getEngID();
									int j=-1;
									for (int i = 0; i < VarList.ENGINEER_LIST.size(); i++) {

										if (VarList.ENGINEER_LIST.get(i).getID().equals(engID)) {
											j=i;
											break;


										}
									}

									eng1Spinner.setSelection(j);
									//									eng1Date.setText(engBean.get(index).getDate());
									eng1JobStart.setText(engBean.get(index).getJobStart());
									eng1Jobend.setText(engBean.get(index).getJobFinished());
									eng1Timework.setText(engBean.get(index).getWorkedHours());
									eng1TimeTravel.setText(engBean.get(index).getTravelTime()); 
									
									eng1Spinner.setEnabled(false);
									eng1JobStart.setEnabled(false);
									eng1Jobend.setEnabled(false);
									eng1TimeTravel.setEnabled(false);
									eng1Timework.setEnabled(false);


								}
							}else{



								eng1Spinner.setEnabled(true);
								eng1JobStart.setEnabled(true);
								eng1Jobend.setEnabled(true);
								eng1TimeTravel.setEnabled(true);
								eng1Timework.setEnabled(true);

								eng1Spinner.setSelection(0);
								eng1JobStart.setText("");
								eng1Jobend.setText("");
								eng1Jobend.setText("");
								eng1Timework.setText("");
								eng1TimeTravel.setText("");
								//								eng1Date.setText("");

								//----------------------change ----

								getSignBTN.setEnabled(true);
								closeSaveBtn.setEnabled(true);

								//--------------------------



							}
							index++;
							//--------------------------------------------------
						}else{

							//---------------------
							EngineerDataBean bean1=new EngineerDataBean();

							bean1.setEngName(eng1Spinner.getSelectedItem().toString());
							bean1.setEngID(VarList.ENGINEER_LIST.get(eng1Spinner.getSelectedItemPosition()).getID());
							bean1.setJobStart(eng1JobStart.getText().toString());
							bean1.setJobFinished(eng1Jobend.getText().toString());
							bean1.setTravelTime(eng1TimeTravel.getText().toString());
							bean1.setWorkedHours(eng1Timework.getText().toString());
							//							bean1.setDate(eng1Date.getText().toString());


							//--------------------------------------------------------------------

							engContent.setDrawingCacheEnabled(true);
							bean1.setSignPath(engSignature.save(engContent,"E"));

							//---------------------------------------------------------------------
							eng1Spinner.setSelection(0);
							eng1JobStart.setText("");
							eng1Jobend.setText("");
							eng1Jobend.setText("");
							eng1Timework.setText("");
							eng1TimeTravel.setText("");
							//							eng1Date.setText("");

							bean1.setFlag("1");
							engBean.add(bean1);

							//							engBean.set(index-1, bean1);

							getSignBTN.setEnabled(true);
							closeSaveBtn.setEnabled(true);



						}
					}else{


						EngineerDataBean bean1=new EngineerDataBean();

						bean1.setEngName(eng1Spinner.getSelectedItem().toString());
						bean1.setEngID(VarList.ENGINEER_LIST.get(eng1Spinner.getSelectedItemPosition()).getID());
						bean1.setJobStart(eng1JobStart.getText().toString());
						bean1.setJobFinished(eng1Jobend.getText().toString());
						bean1.setTravelTime(eng1TimeTravel.getText().toString());
						bean1.setWorkedHours(eng1Timework.getText().toString());
						//						bean1.setDate(eng1Date.getText().toString());
						//----------------------------------------------
						engContent.setDrawingCacheEnabled(true);
						bean1.setSignPath(engSignature.save(engContent,"E"));


						//------------------------------------------

						eng1Spinner.setSelection(0);
						eng1JobStart.setText("");
						eng1Jobend.setText("");
						eng1Jobend.setText("");
						eng1Timework.setText("");
						eng1TimeTravel.setText("");
						//						eng1Date.setText("");

						engBean.add(bean1);

						getSignBTN.setEnabled(true);
						closeSaveBtn.setEnabled(true);





					}
					//-------------------------------------------------------------
				}
				break;

			case R.id.engsignature:
				JobCardDataBean jobBean =VarList.JOB_BEAN;



				jobBean.setEngBean(engBean);

				VarList.JOB_BEAN=jobBean;



				Intent call=new Intent(EngineerTimeDetailsActivity.this,CaptureSignature.class);
				startActivity(call);
				EngineerTimeDetailsActivity.this.finish();



				break;

			case R.id.savecloseEng:
				boolean isItwork=true;
				//closeSaveBtn.setEnabled(false);

				JobCardDataBean jobBeanSave = VarList.JOB_BEAN;



				jobBeanSave.setEngBean(engBean);

				VarList.JOB_BEAN=jobBeanSave;

				if (VarList.SELETCED_BTN.equals(ConstList.CREATE_JOBCARD)) {
					try {


						//save data
						JobCardDBAccess dbSQl=new JobCardDBAccess(this);
						dbSQl.openDB();
						dbSQl.insertENG(jobBeanSave);

						dbSQl.closeDB();
					
					} catch (Exception e) {
						isItwork=false;
						e.printStackTrace();
					}finally{

						//resetFeild();

						if (isItwork) {

							Toast.makeText(this,MessageClass.JOBCARD_UPLOAD_LOCAL_PART_SUCCESS ,Toast.LENGTH_LONG).show();

							VarList.JOB_BEAN=null;


							Intent call2=new Intent(EngineerTimeDetailsActivity.this,MianMenuActivity.class);
							startActivity(call2);
							EngineerTimeDetailsActivity.this.finish();

						} else {

							Toast.makeText(this,MessageClass.JOBCARD_FIAL_LOCAL_SUCCESS ,Toast.LENGTH_LONG).show();

						}
					}
				}else if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {
					try {
						// update data

						JobCardDBAccess dbSQl=new JobCardDBAccess(this);
						dbSQl.openDB();

						//					dbSQl.insertENG(jobBeanSave);
						dbSQl.updateENG(jobBeanSave, Long.parseLong(VarList.SELECTED_ID));
						dbSQl.closeDB();

					} catch (Exception e) {
						isItwork=false;
						e.printStackTrace();
					}finally{

						//resetFeild();

						if (isItwork) {

							Toast.makeText(this,MessageClass.JOBCARD_UPLOAD_LOCAL_PART_SUCCESS ,Toast.LENGTH_LONG).show();

							VarList.JOB_BEAN=null;


							Intent call2=new Intent(EngineerTimeDetailsActivity.this,MianMenuActivity.class);
							startActivity(call2);
							EngineerTimeDetailsActivity.this.finish();

						} else {

							Toast.makeText(this,MessageClass.JOBCARD_UPLOAD_PRESS_ANDNEW_ENG ,Toast.LENGTH_LONG).show();

						}
					}


				}



				break;

			case R.id.engClosebtn:
				backbuttonAction();
				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this,MessageClass.ERROR_IN_BUTTON ,Toast.LENGTH_LONG).show();
		}


	}

	private Boolean engineerValidation(){


		try {

			Boolean flag=false;

			if (eng1Spinner.getSelectedItemPosition() == 0) {

				Toast.makeText(this,MessageClass.ENGINEER_NAME_EMPTY ,Toast.LENGTH_LONG).show();

			}else if(eng1JobStart.getText().toString()==null || eng1JobStart.getText().toString().equals("")){

				Toast.makeText(this,MessageClass.ENGINEER_JOBSTART_EMPTY ,Toast.LENGTH_LONG).show();

			}else if(eng1Jobend.getText().toString()==null || eng1Jobend.getText().toString().equals("")){

				Toast.makeText(this,MessageClass.ENGINEER_JOBEND_EMPTY ,Toast.LENGTH_LONG).show();

			}else if(eng1Timework.getText().toString()==null || eng1Timework.getText().toString().equals("")){

				Toast.makeText(this,MessageClass.ENGINEER_WORKEDTIME_EMPTY ,Toast.LENGTH_LONG).show();

			}else if(eng1TimeTravel.getText().toString()==null || eng1TimeTravel.getText().toString().equals("")){

				Toast.makeText(this,MessageClass.ENGINEER_TRAVELTIME_EMPTY ,Toast.LENGTH_LONG).show();

			}else{

				flag=true;
			}



			return flag;



		} catch (Exception e) {
			return false;
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
	//------------------------------------------------------------------------------

	private String getTodaysDate() { 

		final Calendar c = Calendar.getInstance();
		int todaysDate =     (c.get(Calendar.YEAR) * 10000) + 
		((c.get(Calendar.MONTH) + 1) * 100) + 
		(c.get(Calendar.DAY_OF_MONTH));
		return(String.valueOf(todaysDate));

	}

	private String getCurrentTime() {

		final Calendar c = Calendar.getInstance();
		int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) + 
		(c.get(Calendar.MINUTE) * 100) + 
		(c.get(Calendar.SECOND));
		return(String.valueOf(currentTime));

	}






	public class signature extends View 
	{
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		//private String FLAG;
		private final RectF dirtyRect = new RectF();

		public signature(Context context, AttributeSet attrs,String FLAG) 
		{
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		public String save(View v,String check) 
		{

			String returnPath="";



			if (check.equals("E")) {
				// engineer signature 



				if(engBitmap == null)
				{
					engBitmap =  Bitmap.createBitmap (engContent.getWidth(), engContent.getHeight(), Bitmap.Config.RGB_565);;
				}


				Canvas canvas = new Canvas(engBitmap);
				try
				{

					engUniqueId=engUniqueId.replace(".", "");
					engCurrent = engUniqueId+""+(key++) + ".png";
					engmypath = new File(directory,engCurrent);


					FileOutputStream mFileOutStream = new FileOutputStream(engmypath);

					v.draw(canvas); 
					engBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream); 
					mFileOutStream.flush();
					mFileOutStream.close();
					//Images.Media.insertImage(getContentResolver(), engBitmap, "title", null);

					//					databean.setEngineerSignPath(engmypath.getPath());

					returnPath= engmypath.getPath();
					engSignature.clear();
				}
				catch(Exception e) 
				{ 
					e.printStackTrace();
					returnPath ="";
				} 

			}
			return returnPath;




		}

		public void clear() 
		{
			path.reset();
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) 
		{
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) 
		{
			float eventX = event.getX();
			float eventY = event.getY();
			
			addNewEngineerBTN.setEnabled(true);
			//			mGetSign.setEnabled(true);

			switch (event.getAction()) 
			{
			case MotionEvent.ACTION_DOWN:
				path.moveTo(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
				return true;

			case MotionEvent.ACTION_MOVE:

			case MotionEvent.ACTION_UP:

				resetDirtyRect(eventX, eventY);
				int historySize = event.getHistorySize();
				for (int i = 0; i < historySize; i++) 
				{
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

		private void debug(String string){
		}

		private void expandDirtyRect(float historicalX, float historicalY) 
		{
			if (historicalX < dirtyRect.left) 
			{
				dirtyRect.left = historicalX;
			} 
			else if (historicalX > dirtyRect.right) 
			{
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top) 
			{
				dirtyRect.top = historicalY;
			} 
			else if (historicalY > dirtyRect.bottom) 
			{
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY) 
		{
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}












	//-----------------------------------------------------------------
}

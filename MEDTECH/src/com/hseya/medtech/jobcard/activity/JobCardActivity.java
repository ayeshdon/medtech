package com.hseya.medtech.jobcard.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.hseya.medtech.MianMenuActivity;
import com.hseya.medtech.R;
import com.hseya.medtech.jobcard.bean.JobCardDataBean;
import com.hseya.medtech.jobcard.bean.PartsBean;
import com.hseya.medtech.jobcard.sqlite.JobCardDBAccess;
import com.hseya.medtech.jobcard.unfinished.activity.UnfinishedJobCardActivity;
import com.hseya.medtech.utill.ConstList;
import com.hseya.medtech.utill.MessageClass;
import com.hseya.medtech.utill.VarList;

@SuppressLint("SimpleDateFormat")
public class JobCardActivity extends Activity implements OnTouchListener,
		OnClickListener {

	private View layout;
	private EditText opentDateTxt, inputSearch, serviceTxt, eqicType,
			jobCardNoTXT;
	private Dialog dialog, eqDailog, partDailog;
	private ListView lv;
	private Button nextBTN, closeBTN, closeSaveBTN;
	private EditText partNameTxt, partQuantityTxt, partIDTxt;

	private EditText customerTXT, visitReasonTXT, partusedTXT,
			fileamentCounterTXT, hvCounterTXT, logTimeTXT, downtimeTXT;
	ArrayAdapter<String> adapter;

	String jobCardNo, customer, equcment, visitReason, service, partUsed,
			filamnetCounter, hvCounter, logTime, downTime;
	private String dateNow, customerID, eqicmentID;

	private JobCardDataBean displayBean;

	// -----------------------------------

	private ArrayList<PartsBean> partBean;

	// -------------------------------------
	CustomDateTimePicker custom;
	private int SIZE = 0;

	private int index = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		VarList.JOB_BEAN = null;

		Calendar currentDate = Calendar.getInstance();

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");

		dateNow = formatter.format(currentDate.getTime());

		super.onCreate(savedInstanceState);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.jobcard);

		// --------------------------

		custom = new CustomDateTimePicker(this,
				new CustomDateTimePicker.ICustomDateTimeListener() {

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {

						logTimeTXT.setText(calendarSelected
								.get(Calendar.DAY_OF_MONTH)
								+ ":"
								+ monthShortName
								+ ":"
								+ year
								+ ":"
								+ hour24
								+ ":" + min + " " + AM_PM);
					}

					@Override
					public void onCancel() {

					}
				});

		// ---------------------
		layout = findViewById(R.id.jobcardlayout);
		layout.setOnTouchListener(this);

		opentDateTxt = (EditText) findViewById(R.id.opendate);
		opentDateTxt.setText(dateNow);

		customerTXT = (EditText) findViewById(R.id.customertxt);
		customerTXT.setOnClickListener(this);
		customerTXT.clearFocus();

		eqicType = (EditText) findViewById(R.id.equpmetnTypetxt);
		eqicType.setOnClickListener(this);
		eqicType.clearFocus();

		partusedTXT = (EditText) findViewById(R.id.partusedtxt);
		partusedTXT.setOnClickListener(this);
		partusedTXT.clearFocus();

		jobCardNoTXT = (EditText) findViewById(R.id.jobcardNo);
		// customerTXT=(EditText) findViewById(R.id.customertxt);
		// eqicType=(EditText) findViewById(R.id.equpmetnTypetxt);
		visitReasonTXT = (EditText) findViewById(R.id.visitReason);
		serviceTxt = (EditText) findViewById(R.id.servicetxt);

		fileamentCounterTXT = (EditText) findViewById(R.id.fileamentCounterTxt);

		hvCounterTXT = (EditText) findViewById(R.id.hvCounterTxt);

		logTimeTXT = (EditText) findViewById(R.id.logTimeTxt);
		logTimeTXT.setOnClickListener(this);
		logTimeTXT.clearFocus();
		// logTimeTXT.setInputType(InputType.TYPE_NULL);

		downtimeTXT = (EditText) findViewById(R.id.downtimeTxt);

		nextBTN = (Button) findViewById(R.id.engineerdetailsBtn);
		nextBTN.setOnClickListener(this);

		closeBTN = (Button) findViewById(R.id.closebtn);
		closeBTN.setOnClickListener(this);

		closeSaveBTN = (Button) findViewById(R.id.closesaveBtn);
		closeSaveBTN.setOnClickListener(this);

		if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {
			// closeSaveBTN.setEnabled(false);
			/* set value to edit text field */
			closeSaveBTN.setText(MessageClass.SAVE_MSG);

			JobCardDBAccess dbClass = new JobCardDBAccess(this);

			dbClass.openDB();

			if (VarList.SELECTED_ID_VAL.equals("2")) {
				displayBean = dbClass.getJobcardDetials(
						Long.parseLong(VarList.SELECTED_ID), true);
			} else {
				displayBean = dbClass.getJobcardDetials(
						Long.parseLong(VarList.SELECTED_ID), false);
			}
			// ------------------------------------------------
			VarList.PART_BEAN = displayBean.getPartBean();
			// --------------------------------------------------------------------
			dbClass.closeDB();

			opentDateTxt.setText(displayBean.getJobDate());
			customerTXT.setText(displayBean.getCustomer());
			eqicType.setText(displayBean.getEqucment());
			jobCardNoTXT.setText(displayBean.getJobCardNO());
			visitReasonTXT.setText(displayBean.getVisitReason());
			serviceTxt.setText(displayBean.getService());
			partusedTXT.setText(displayBean.getPartUsed());
			fileamentCounterTXT.setText(displayBean.getFilamnetCounter());
			hvCounterTXT.setText(displayBean.getHvCounter());
			logTimeTXT.setText(displayBean.getLogTime());
			downtimeTXT.setText(displayBean.getDownTime());

		} else if (VarList.SELETCED_BTN.equals(ConstList.CREATE_JOBCARD)) {

			closeSaveBTN.setEnabled(true);

		}

	}

	@Override
	public void onBackPressed() {

		backBtnPressedAction();
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		if (v == layout) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.customertxt:

			try {

				dialog = new Dialog(this);

				dialog.setContentView(R.layout.fetch_customer);

				dialog.setCancelable(true);

				dialog.setTitle("Select Customer");

				lv = (ListView) dialog.findViewById(R.id.list_view);

				inputSearch = (EditText) dialog.findViewById(R.id.inputSearch);

				ArrayList<String> cus_name = new ArrayList<String>();

				for (int i = 0; i < VarList.CUSTOMER_LIST.size(); i++) {
					cus_name.add(VarList.CUSTOMER_LIST.get(i).getCus_name());

				}

				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, cus_name);
				lv.setAdapter(adapter);

				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long arg3) {
						customerTXT.setText(parent.getItemAtPosition(position)
								.toString());

						customerID = VarList.CUSTOMER_LIST.get(position)
								.getCus_ID();

						dialog.dismiss();
					}

				});

				inputSearch.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence cs, int arg1,
							int arg2, int arg3) {
						JobCardActivity.this.adapter.getFilter().filter(cs);
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {

					}

					@Override
					public void afterTextChanged(Editable arg0) {
					}
				});

				Button cancelBtn = (Button) dialog
						.findViewById(R.id.customercancelbtn);

				cancelBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				dialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.equpmetnTypetxt:
			try {

				eqDailog = new Dialog(this);

				eqDailog.setContentView(R.layout.fecth_eq);

				eqDailog.setCancelable(true);

				eqDailog.setTitle("Select Equipment");

				lv = (ListView) eqDailog.findViewById(R.id.list_viewEQ);

				inputSearch = (EditText) eqDailog
						.findViewById(R.id.inputSearchEQ);

				ArrayList<String> EQ_name = new ArrayList<String>();

				for (int i = 0; i < VarList.EQCIPMENTS_LIST.size(); i++) {
					EQ_name.add(VarList.EQCIPMENTS_LIST.get(i).getName());

				}

				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, EQ_name);
				lv.setAdapter(adapter);

				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long arg3) {
						// Toast.makeText(JobCardActivity.this,"rrrrrr - "+parent.getItemAtPosition(position).toString()
						// ,Toast.LENGTH_LONG).show();
						eqicType.setText(parent.getItemAtPosition(position)
								.toString());
						eqicmentID = VarList.EQCIPMENTS_LIST.get(position)
								.getID();
						eqDailog.dismiss();
					}

				});

				inputSearch.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence cs, int arg1,
							int arg2, int arg3) {
						// When user changed the Text
						JobCardActivity.this.adapter.getFilter().filter(cs);
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
					}
				});

				Button cancelBtnEQ = (Button) eqDailog
						.findViewById(R.id.eqcancelbtn);

				cancelBtnEQ.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						eqDailog.dismiss();

					}
				});

				eqDailog.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.engineerdetailsBtn:
			try {
				if (isValidated()) {

					JobCardDataBean dataBean = new JobCardDataBean();

					dataBean.setJobDate(dateNow);

					VarList.JOBCARD_DATE = jobCardNo;

					dataBean.setVisitReason(visitReason);
					dataBean.setService(service);
					dataBean.setPartUsed(partUsed);
					dataBean.setPartBean(VarList.PART_BEAN);

					dataBean.setFilamnetCounter(filamnetCounter);
					dataBean.setHvCounter(hvCounter);
					dataBean.setLogTime(logTime);
					dataBean.setJobCardNO(jobCardNo);
					dataBean.setDownTime(downTime);

					dataBean.setCustomerID(customerID);
					dataBean.setCustomer(customer);

					dataBean.setEqucment(equcment);
					dataBean.setEqucmentID(eqicmentID);

					if (VarList.SELETCED_BTN
							.equals(ConstList.UNFINISHED_JOBCARD)) {

						// ----------------------------------

						if (customerID == null) {
							dataBean.setCustomerID(displayBean.getCustomerID());

						} else {
							dataBean.setCustomerID(customerID);
						}
						if (eqicmentID == null) {

							dataBean.setEqucmentID(displayBean.getEqucmentID());

						} else {
							dataBean.setEqucmentID(eqicmentID);
						}

						// ---------------------------------------

						dataBean.setCustomerID(displayBean.getCustomerID());

						if (displayBean.getEngBean() != null) {
							dataBean.setEngBean(displayBean.getEngBean());
						}

					}

					VarList.JOB_BEAN = dataBean;

					resetFeild();

					Intent call = new Intent(JobCardActivity.this,
							EngineerTimeDetailsActivity.class);
					startActivity(call);
					JobCardActivity.this.finish();

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// ----------------------------------------------------------------------------------------

		case R.id.partusedtxt:

			index = 1;
			// Toast.makeText(this, "part test", Toast.LENGTH_LONG).show();
			try {

				partDailog = new Dialog(this);
				partDailog.setContentView(R.layout.partused_layout);
				partDailog.setCancelable(true);
				partDailog.setTitle("Add Used Parts");

				partNameTxt = (EditText) partDailog
						.findViewById(R.id.partNameTxt);
				partQuantityTxt = (EditText) partDailog
						.findViewById(R.id.partQuantityTxt);
				partIDTxt = (EditText) partDailog.findViewById(R.id.partID);

				if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {
					partBean = displayBean.getPartBean();

					partNameTxt.setText(partBean.get(0).getPartName());
					partQuantityTxt.setText(partBean.get(0).getPartQuantitys());
					partIDTxt.setText(partBean.get(0).getPartID());
					// partBean.get(0).setFlag("1");

					SIZE = partBean.size();

				} else {
					partBean = new ArrayList<PartsBean>();
				}

				Button cancelBtnEQ = (Button) partDailog
						.findViewById(R.id.partCloaseBtn);
				cancelBtnEQ.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						VarList.PART_BEAN = null;
						partBean = null;
						partDailog.dismiss();

					}
				});

				Button addNextBtn = (Button) partDailog
						.findViewById(R.id.partAddNextBtn);
				addNextBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// -----------add next button action here
						// ----------------

						// EditText partNameTxt=(EditText)
						// partDailog.findViewById(R.id.partNameTxt);
						// EditText partQuantityTxt=(EditText)
						// partDailog.findViewById(R.id.partQuantityTxt);

						String partName = partNameTxt.getText().toString();
						String partQuantity = partQuantityTxt.getText()
								.toString();
						String partID = partIDTxt.getText().toString();

						if (VarList.SELETCED_BTN
								.equals(ConstList.UNFINISHED_JOBCARD)) {

							// -----edit part here ---------

							if (SIZE > index) {
								partNameTxt.setText(partBean.get(index)
										.getPartName());
								partQuantityTxt.setText(partBean.get(index)
										.getPartQuantitys());
								partIDTxt.setText(partBean.get(index)
										.getPartID());
								partBean.get(index).setFlag("1");
							} else if (SIZE == index) {
								partNameTxt.setText("");
								partQuantityTxt.setText("");
								partIDTxt.setText("");

							} else {

								if (partName.equals("") || partName == null) {

									Toast.makeText(JobCardActivity.this,
											MessageClass.PART_NAME_EMPTY,
											Toast.LENGTH_LONG).show();

								} else if (partQuantity.equals("")
										|| partQuantity == null) {

									Toast.makeText(JobCardActivity.this,
											MessageClass.PART_QUANTITY_EMPTY,
											Toast.LENGTH_LONG).show();
								} else if (partID.equals("") || partID == null) {

									Toast.makeText(JobCardActivity.this,
											MessageClass.PART_ID_EMPTY,
											Toast.LENGTH_LONG).show();

								} else {
									PartsBean bean = new PartsBean();
									bean.setPartName(partName);
									bean.setPartID(partID);
									bean.setPartQuantitys(partQuantity);

									// partBean.set(index, bean);
									partBean.add(bean);
									// partBean.add(bean);
									partNameTxt.setText("");
									partIDTxt.setText("");
									partQuantityTxt.setText("");

									// VarList.PART_BEAN=partBean;

								}

							}

							// partNameTxt.setText(partBean.get(0).getPartName());
							// partQuantityTxt.setText(partBean.get(0).getPartQuantitys());

							VarList.PART_BEAN = partBean;
							index++;

						} else {

							if (partName.equals("") || partName == null) {

								Toast.makeText(JobCardActivity.this,
										MessageClass.PART_NAME_EMPTY,
										Toast.LENGTH_LONG).show();

							} else if (partQuantity.equals("")
									|| partQuantity == null) {

								Toast.makeText(JobCardActivity.this,
										MessageClass.PART_QUANTITY_EMPTY,
										Toast.LENGTH_LONG).show();
							} else if (partID.equals("") || partID == null) {

								Toast.makeText(JobCardActivity.this,
										MessageClass.PART_ID_EMPTY,
										Toast.LENGTH_LONG).show();

							} else {
								PartsBean bean = new PartsBean();
								bean.setPartName(partName);
								bean.setPartID(partID);
								bean.setPartQuantitys(partQuantity);
								bean.setFlag("0");

								partBean.add(bean);
								partNameTxt.setText("");
								partQuantityTxt.setText("");
								partIDTxt.setText("");

								VarList.PART_BEAN = partBean;

							}

						}

					}
				});

				Button finishButton = (Button) partDailog
						.findViewById(R.id.partFinishBtn);
				finishButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (!partBean.isEmpty()) {

							partusedTXT.setText("Add Success");
							partDailog.dismiss();
						} else {

							Toast.makeText(JobCardActivity.this,
									"Please Enter Part Details",
									Toast.LENGTH_LONG).show();
						}

					}
				});
				partDailog.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		// ----------------------------------------------------------------------------

		case R.id.closebtn:

			backBtnPressedAction();

			break;
		case R.id.logTimeTxt:
			custom.showDialog();
			break;

		case R.id.closesaveBtn:
			boolean isItwork = false;

			try {

				if (isValidated()) {

					isItwork = true;
					JobCardDataBean dataBean = new JobCardDataBean();

					dataBean.setJobDate(dateNow);

					VarList.JOBCARD_DATE = jobCardNo;
					if (VarList.SELETCED_BTN
							.equals(ConstList.UNFINISHED_JOBCARD)) {

					}

					dataBean.setCustomer(customer);
					dataBean.setEqucment(equcment);
					dataBean.setVisitReason(visitReason);
					dataBean.setService(service);
					dataBean.setPartUsed(partUsed);

					dataBean.setPartBean(VarList.PART_BEAN);

					dataBean.setFilamnetCounter(filamnetCounter);
					dataBean.setHvCounter(hvCounter);
					dataBean.setLogTime(logTime);
					dataBean.setJobCardNO(jobCardNo);
					dataBean.setDownTime(downTime);

					if (VarList.SELETCED_BTN
							.equals(ConstList.UNFINISHED_JOBCARD)) {

						if (customerID == null) {
							dataBean.setCustomerID(displayBean.getCustomerID());

						} else {
							dataBean.setCustomerID(customerID);
						}
						if (eqicmentID == null) {

							dataBean.setEqucmentID(displayBean.getEqucmentID());

						} else {
							dataBean.setEqucmentID(eqicmentID);
						}
					} else {
						dataBean.setCustomerID(customerID);
						dataBean.setEqucmentID(eqicmentID);
					}

					VarList.JOB_BEAN = dataBean;

					JobCardDBAccess dbClass = new JobCardDBAccess(this);

					dbClass.openDB();

					if (VarList.SELETCED_BTN
							.equals(ConstList.UNFINISHED_JOBCARD)) {

						dbClass.updateFirstDB(VarList.JOB_BEAN,
								Long.parseLong(VarList.SELECTED_ID));

					} else {
						dbClass.insertFirstDB(VarList.JOB_BEAN);

					}
					dbClass.closeDB();

				}
			} catch (Exception e) {

				isItwork = false;
				e.printStackTrace();
			} finally {
				resetFeild();

				if (isItwork) {

					if (VarList.SELETCED_BTN
							.equals(ConstList.UNFINISHED_JOBCARD)) {
						Toast.makeText(this,
								MessageClass.JOBCARD_UPDATE_LOCAL_PART_SUCCESS,
								Toast.LENGTH_LONG).show();

						VarList.JOB_BEAN = null;

						Intent call = new Intent(JobCardActivity.this,
								MianMenuActivity.class);
						startActivity(call);
						JobCardActivity.this.finish();

					} else {
						Toast.makeText(this,
								MessageClass.JOBCARD_UPLOAD_LOCAL_PART_SUCCESS,
								Toast.LENGTH_LONG).show();

						VarList.JOB_BEAN = null;

						Intent call = new Intent(JobCardActivity.this,
								MianMenuActivity.class);
						startActivity(call);
						JobCardActivity.this.finish();

					}
				} else {

					Toast.makeText(this,
							MessageClass.JOBCARD_FIAL_LOCAL_SUCCESS,
							Toast.LENGTH_LONG).show();

				}

				// }

			}

			break;

		default:
			break;
		}

	}

	/*
	 * back button pressed action
	 */

	private void backBtnPressedAction() {
		if (VarList.SELETCED_BTN.equals(ConstList.UNFINISHED_JOBCARD)) {

			Intent call = new Intent(JobCardActivity.this,
					UnfinishedJobCardActivity.class);
			startActivity(call);
			JobCardActivity.this.finish();

		} else if (VarList.SELETCED_BTN.equals(ConstList.CREATE_JOBCARD)) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			alertDialogBuilder.setTitle("Warning");

			alertDialogBuilder
					.setMessage("If you go back this job card will be lost")
					.setCancelable(false)
					.setPositiveButton("Go to home",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									VarList.JOB_BEAN = null;

									Intent call = new Intent(
											JobCardActivity.this,
											MianMenuActivity.class);
									startActivity(call);
									JobCardActivity.this.finish();

								}
							})
					.setNegativeButton("Close",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		}

	}

	private Boolean isValidated() {
		Boolean flag = true;
		try {

			jobCardNo = jobCardNoTXT.getText().toString();
			customer = customerTXT.getText().toString();
			equcment = eqicType.getText().toString();
			visitReason = visitReasonTXT.getText().toString();
			service = serviceTxt.getText().toString();
			partUsed = partusedTXT.getText().toString();
			filamnetCounter = fileamentCounterTXT.getText().toString();
			hvCounter = hvCounterTXT.getText().toString();
			logTime = logTimeTXT.getText().toString();
			downTime = downtimeTXT.getText().toString();

			VarList.JOBCARD_DATE = jobCardNo;

			if (jobCardNo == null || jobCardNo.equals("")) {

				Toast.makeText(this, MessageClass.ORDER_NO_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;
			}

			else if (customer == null || customer.equals("")) {

				Toast.makeText(this, MessageClass.CUSTOMER_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

			} else if (equcment == null || equcment.equals("")) {

				Toast.makeText(this, MessageClass.EQUICKMENT_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

			} else if (visitReason == null || visitReason.equals("")) {

				Toast.makeText(this, MessageClass.VISIT_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

			} else if (service == null || service.equals("")) {

				Toast.makeText(this, MessageClass.SERVICE_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

				// }else if (partUsed==null || partUsed.equals("")) {
				//
				// Toast.makeText(this,MessageClass.VISIT_EMPTY
				// ,Toast.LENGTH_LONG).show();
				// flag=false;

			} else if (filamnetCounter == null || filamnetCounter.equals("")) {

				Toast.makeText(this, MessageClass.FIL_COUNTER_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

			} else if (hvCounter == null || hvCounter.equals("")) {

				Toast.makeText(this, MessageClass.HV_COUNTER_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

			} else if (logTime == null || logTime.equals("")) {

				Toast.makeText(this, MessageClass.LOG_TIMEE_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

			} else if (downTime == null || downTime.equals("")) {

				Toast.makeText(this, MessageClass.DOWN_TIMEE_EMPTY,
						Toast.LENGTH_LONG).show();
				flag = false;

			} else {

				flag = true;
			}

			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}

	}

	private void resetFeild() {

		jobCardNo = "";
		customer = "";
		equcment = "";
		visitReason = "";
		service = "";
		partUsed = "";
		filamnetCounter = "";
		hvCounter = "";
		logTime = "";
		downTime = "";
	}

}

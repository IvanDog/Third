package com.example.inspector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class IntegratedQueryActivity extends Activity {
	private final static int INTEGRATED_QUERY = 101;
	private Spinner mAdministrativeDivisionSP;
	private Spinner mParkingNameSP;
	private Spinner mParkingNumberSP;
	private EditText mTollCollectorNameET;
	private EditText mTollCollectorNumberET;
	private EditText mLicensePlateET;
	private Spinner mCarTypeSP;
	private Spinner mParkingTypeSP;
	private Spinner mLocationNumberSP;
	private Spinner mPaymentStateSP;
	private EditText mExpenseET;
	private Button mQueryBT;
	private ProgressDialog mProgDialog = null;// 搜索时进度条
	private TextWatcher mNameWatcher = null;
	private TextWatcher mNumberWatcher = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integrated_query);
		mAdministrativeDivisionSP=(Spinner)findViewById(R.id.sp_administrative_division_integrated_query);
		mParkingNameSP=(Spinner)findViewById(R.id.sp_parking_name_integrated_query);
		mParkingNumberSP=(Spinner)findViewById(R.id.sp_parking_number_integrated_query);
		mParkingNameSP.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
	        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
	                long id) {
				if(position==1){
					mParkingNumberSP.setSelection(1);
				}else if(position==2){
					mParkingNumberSP.setSelection(2);
				}
	        }
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		mParkingNumberSP.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
	        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
	                long id) {
				if(position==1){
					mParkingNameSP.setSelection(1);
				}else if(position==2){
					mParkingNameSP.setSelection(2);
				}
	        }
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		mNameWatcher = new NameWatcher();
		mNumberWatcher = new NumberWatcher();
		mTollCollectorNameET=(EditText)findViewById(R.id.et_tool_collector_name_integrated_query);
		mTollCollectorNumberET=(EditText)findViewById(R.id.et_toll_collector_number_integrated_query);
		mTollCollectorNameET.addTextChangedListener(mNameWatcher);
    	mTollCollectorNumberET.addTextChangedListener(mNumberWatcher);
		mTollCollectorNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mTollCollectorNameET.addTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.removeTextChangedListener(mNumberWatcher);
                } else {
                	mTollCollectorNameET.removeTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.addTextChangedListener(mNumberWatcher);
                }
            }
        });
		mTollCollectorNumberET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mTollCollectorNameET.removeTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.addTextChangedListener(mNumberWatcher);
                } else {
                	mTollCollectorNameET.addTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.removeTextChangedListener(mNumberWatcher);
                }
            }
        });
		mLicensePlateET=(EditText)findViewById(R.id.et_license_plate_integrated_query);
		mCarTypeSP=(Spinner)findViewById(R.id.sp_car_type_integrated_query);
		mParkingTypeSP=(Spinner)findViewById(R.id.sp_parking_type_integrated_query);
		mLocationNumberSP=(Spinner)findViewById(R.id.sp_location_number_integrated_query);
		//mPaymentStateSP=(Spinner)findViewById(R.id.sp_payment_state_integrated_query);
		mExpenseET=(EditText)findViewById(R.id.et_expense_integrated_query);
		mQueryBT=(Button)findViewById(R.id.bt_confirm_search_integrated_query);
		mQueryBT.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	showProgressDialog();
	            Message msg = new Message();
	            msg.what=INTEGRATED_QUERY;
	            mHandler.sendMessageDelayed(msg, 2000);
		    }
		});
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
        getActionBar().setDisplayHomeAsUpEnabled(true); 
	}
	
	public class NameWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	// TODO Auto-generated method stub
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        	if(mTollCollectorNameET.getText()!=null && mTollCollectorNameET.getText().toString().equals("peter")){
        		mTollCollectorNumberET.setText("101884");
        	}
        }
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}
	}
	
	public class NumberWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	// TODO Auto-generated method stub
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        	if( mTollCollectorNumberET.getText()!=null && mTollCollectorNumberET.getText().toString().equals("101884")){
        		mTollCollectorNameET.setText("peter");
        	}
        }
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	         case android.R.id.home:  
	             finish();  
	             break;    
	        default:  
	             break;  
	    }  
	    return super.onOptionsItemSelected(item);  
	  }  
		
	private Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage (Message msg) {
	        super.handleMessage(msg);
	        switch (msg.what) {
            case INTEGRATED_QUERY:
            	String administrativeDivision = mAdministrativeDivisionSP.getSelectedItem().toString();
	        	if(administrativeDivision.equals("请选择行政区域")){
	        		administrativeDivision = null;
	        	}
	        	String parkingName = mParkingNameSP.getSelectedItem().toString();
	        	if(parkingName.equals("请选择车场名称")){
	        		parkingName=null;
	        	}
	        	String parkingNumber = mParkingNumberSP.getSelectedItem().toString();
	        	if(parkingNumber.equals("请选择车场编号")){
	        		parkingNumber=null;
	        	}
	        	String tollCollectionName = mTollCollectorNameET.getText().toString();
	        	if(tollCollectionName.equals("")){
	        		tollCollectionName=null;
	        	}
	        	String tollCollectionNumber = mTollCollectorNumberET.getText().toString();
	        	if(tollCollectionNumber.equals("")){
	        		tollCollectionNumber=null;
	        	}
	        	String licensePlate = mLicensePlateET.getText().toString();
	        	if(licensePlate.equals("")){
	        		licensePlate=null;
	        	}
	        	String carType = mCarTypeSP.getSelectedItem().toString();
	        	if(carType.equals("车辆类型")){
	        		carType=null;
	        	}
	        	String parkingType = mParkingTypeSP.getSelectedItem().toString();
	        	if(parkingType.equals("停车类型")){
	        		parkingType=null;
	        	}
	        	int locationNumber = -1;
	        	if((mLocationNumberSP.getSelectedItem().toString()).equals("泊位编号")){
	        		locationNumber = 0;
	        	}else{
	            	locationNumber = Integer.parseInt(mLocationNumberSP.getSelectedItem().toString());
	        	}
	        	int expense = -1;
	        	Log.e("yifan","mExpenseET.getText():" + mExpenseET.getText().toString());
	        	if((mExpenseET.getText().toString()).equals("")){
		        	expense = -1;
	        	}else{
		        	expense = Integer.parseInt(mExpenseET.getText().toString());
	        	}
				Intent intent = new Intent(IntegratedQueryActivity.this,IntegratedQueryResultActivity.class);
				Bundle bundle = new Bundle();
        		bundle.putString("administrativeDivision", administrativeDivision);
        		bundle.putString("parkingName", parkingName);
        		bundle.putString("parkingnumber", parkingNumber);
        		bundle.putString("toolcollectorName", tollCollectionName);
        		bundle.putString("toolcollectorNumber", tollCollectionNumber);
        		bundle.putString("licenseplate", licensePlate);
        		bundle.putInt("locationnumber", locationNumber);
        		bundle.putString("cartype", carType);
        		bundle.putString("parkingtype", parkingType);
        		bundle.putInt("expense", expense);
        		intent.putExtras(bundle);
				startActivity(intent);
				dismissProgressDialog();
                break;
	        default:
	            break;
	        }
	    }
	};
	
	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (mProgDialog == null)
			mProgDialog = new ProgressDialog(this);
		    mProgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    mProgDialog.setIndeterminate(false);
		    mProgDialog.setCancelable(true);
		    mProgDialog.setMessage("正在查询");
		    mProgDialog.show();
	    }

	/**
	 * 隐藏进度框
	 */
	private void dismissProgressDialog() {
		if (mProgDialog != null) {
			mProgDialog.dismiss();
		}
	}
	
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){  
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction()!=null && intent.getAction().equals("ExitApp")){
				finish();
			}else if(intent.getAction()!=null && intent.getAction().equals("BackMain")){
				finish();
			}
		}            
    }; 
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}

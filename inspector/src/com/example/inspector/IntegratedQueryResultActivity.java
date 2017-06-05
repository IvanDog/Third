package com.example.inspector;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class IntegratedQueryResultActivity extends FragmentActivity {
	public static final int TYPE_UNFINISHED_PAYMENT_STATE = 101;
	public static final int TYPE_FINISHED_PAYMENT_STATE_MOBILE = 102;
	public static final int TYPE_FINISHED_PAYMENT_STATE_CASH = 103;
	public static final int TYPE_FINISHED_PAYMENT_STATE_BALANCE = 104;
	public static final int TYPE_UNFINISHED_PAYMENT_STATE_LEAVE = 105;
	public static final int SEARCH_SUCCESS =201;
	private DBAdapter mDBAdapter;
	private Cursor mCursor;
	private Fragment mUnfinishedPaymentStateFragment;
	private Fragment mFinishedPaymentStateMobileFragment;
	private Fragment mFinishedPaymentStateCashFragment;
	private Fragment mFinishedPaymentStateBalanceFragment;
	private Fragment mUnfinishedPaymentStateLeaveFragment;
	private TextView mUnfinishedPaymentStateTV;
	private TextView mFinishedPaymentStateMobileTV;
	private TextView mFinishedPaymentStateCashTV;
	private TextView mFinishedPaymentStateBalanceTV;
	private TextView mUnfinishedPaymentStateLeaveTV;
	private TextView mDisplayDateTV;
    private int mYear;
    private int mMonth; 
    private int mDay;
	private Button mSearchBT;
	private int mCurrentId;
	private String mAdministrativeDivision;
	private String mParkingName;
	private String mParkingNumber;
	private String mTollCollectorName;
	private String mTollCollectorNumber;
	private String mLicenseNumber;
	private String mCarType;
	private String mParkingType;
	private int mLocationNumber;
	private int mExpense;
	private OnClickListener mTabClickListener = new OnClickListener() {
        @Override  
        public void onClick(View v) {  
            if (v.getId() != mCurrentId) {//如果当前选中跟上次选中的一样,不需要处理  
                changeSelect(v.getId());//改变图标跟文字颜色的选中   
                changeFragment(v.getId(),formatDate(),mAdministrativeDivision,
            			mParkingName, mParkingNumber, mTollCollectorName, mTollCollectorNumber, mLicenseNumber, mCarType,
            			mParkingType, mLocationNumber, mExpense);//fragment的切换  
                mCurrentId = v.getId();//设置选中id  
            }  
        }  
    };  
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			mYear = year;
			mMonth = month+1;
			mDay = day;
			updateDisplayDate();
		}
	};
    private void updateDisplayDate(){
    	mDisplayDateTV.setText(mYear + "-" + mMonth + "-" + mDay);
    }
	private void DisplayDateDialog(){
		DatePickerDialog dialog = new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
		dialog.getDatePicker().setMaxDate(new Date().getTime());  //设置日期最大值
		Date min = new Date(2017-1900, 0, 1);
		dialog.getDatePicker().setMinDate(min.getTime());
		dialog.show();
	}
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_result_integrated_query);
		mDBAdapter = new DBAdapter(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mAdministrativeDivision = bundle.getString("administrativeDivision");
		mParkingName = bundle.getString("parkingName");
		mParkingNumber = bundle.getString("parkingnumber");
		mTollCollectorName = bundle.getString("toolcollectorName");
		mTollCollectorNumber = bundle.getString("toolcollectorNumber");
		mLicenseNumber = bundle.getString("licenseplate");
		mCarType = bundle.getString("cartype");
		mParkingType = bundle.getString("parkingtype");
		mLocationNumber = bundle.getInt("locationnumber");
		mExpense = bundle.getInt("expense");
		
        mUnfinishedPaymentStateTV = (TextView) findViewById(R.id.tv_payment_state_unfinished_integrated_query);
        mFinishedPaymentStateMobileTV = (TextView) findViewById(R.id.tv_payment_state_finished_mobile_integrated_query);
        mFinishedPaymentStateCashTV = (TextView) findViewById(R.id.tv_payment_state_finished_cash_integrated_query);
        mFinishedPaymentStateBalanceTV = (TextView) findViewById(R.id.tv_payment_state_finished_balance_integrated_query);
        mUnfinishedPaymentStateLeaveTV = (TextView) findViewById(R.id.tv_payment_state_unfinished_leave_integrated_query);
        mUnfinishedPaymentStateTV.setOnClickListener(mTabClickListener); 
        mFinishedPaymentStateMobileTV.setOnClickListener(mTabClickListener);
        mFinishedPaymentStateCashTV.setOnClickListener(mTabClickListener); 
        mFinishedPaymentStateBalanceTV.setOnClickListener(mTabClickListener); 
        mUnfinishedPaymentStateLeaveTV.setOnClickListener(mTabClickListener);
        mDisplayDateTV = (TextView)findViewById(R.id.tv_display_date_integrated_query); 
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplayDate();
        mDisplayDateTV.setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View v){
        	   DisplayDateDialog();
           }
        });
        mSearchBT=(Button) findViewById(R.id.bt_search_date_integrated_query);
        mSearchBT.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				//String date = mHistoryDateSP.getSelectedItem().toString();
		        changeFragment(mCurrentId, formatDate(),mAdministrativeDivision,
		    			mParkingName, mParkingNumber, mTollCollectorName, mTollCollectorNumber, mLicenseNumber, mCarType,
		    			mParkingType, mLocationNumber, mExpense);
		        Message msg = new Message();
		        msg.what=SEARCH_SUCCESS;
		        mHandler.sendMessage(msg);
			}
		});
        changeSelect(R.id.tv_payment_state_unfinished_integrated_query);
        changeFragment(R.id.tv_payment_state_unfinished_integrated_query,formatDate(),mAdministrativeDivision,
    			mParkingName, mParkingNumber, mTollCollectorName, mTollCollectorNumber, mLicenseNumber, mCarType,
    			mParkingType, mLocationNumber, mExpense);
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        registerReceiver(mReceiver, filter); 
	}
	
	private void changeFragment(int resId, String date, String administrativeDivision, String parkingName, String parkingNumber,
			String tollCollectorName, String tollCollectorNumber, String licenseNumber, String carType, String parkingType, int locationNumber, int expense) {  
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务  
        hideFragments(transaction);//隐藏所有fragment  
        if(resId==R.id.tv_payment_state_unfinished_integrated_query){
        	mUnfinishedPaymentStateFragment = new IntegratedResultFragment(TYPE_UNFINISHED_PAYMENT_STATE, date, administrativeDivision,
        			parkingName, parkingNumber, tollCollectorName, tollCollectorNumber, licenseNumber, carType, parkingType, locationNumber, expense);
        	 transaction.replace(R.id.history_payment_container, mUnfinishedPaymentStateFragment);
        }else if(resId==R.id.tv_payment_state_finished_mobile_integrated_query){
        	mFinishedPaymentStateMobileFragment = new IntegratedResultFragment(TYPE_FINISHED_PAYMENT_STATE_MOBILE, date, administrativeDivision,
        			parkingName, parkingNumber, tollCollectorName, tollCollectorNumber, licenseNumber, carType, parkingType, locationNumber, expense);  
        	transaction.replace(R.id.history_payment_container, mFinishedPaymentStateMobileFragment);
        }else if(resId==R.id.tv_payment_state_finished_cash_integrated_query){
        	mFinishedPaymentStateCashFragment = new IntegratedResultFragment(TYPE_FINISHED_PAYMENT_STATE_CASH, date, administrativeDivision,
        			parkingName, parkingNumber, tollCollectorName, tollCollectorNumber, licenseNumber, carType, parkingType, locationNumber, expense);  
        	transaction.replace(R.id.history_payment_container, mFinishedPaymentStateCashFragment);
        }else if(resId==R.id.tv_payment_state_finished_balance_integrated_query){
        	mFinishedPaymentStateBalanceFragment = new IntegratedResultFragment(TYPE_FINISHED_PAYMENT_STATE_BALANCE, date, administrativeDivision,
        			parkingName, parkingNumber, tollCollectorName, tollCollectorNumber, licenseNumber, carType, parkingType, locationNumber, expense);  
        	transaction.replace(R.id.history_payment_container, mFinishedPaymentStateBalanceFragment);
        }else if(resId==R.id.tv_payment_state_unfinished_leave_integrated_query){
        	mUnfinishedPaymentStateLeaveFragment = new IntegratedResultFragment(TYPE_UNFINISHED_PAYMENT_STATE_LEAVE, date, administrativeDivision,
        			parkingName, parkingNumber, tollCollectorName, tollCollectorNumber, licenseNumber, carType, parkingType, locationNumber, expense);  
        	transaction.replace(R.id.history_payment_container, mUnfinishedPaymentStateLeaveFragment);
        }
        transaction.commit();//提交事务  
    }

	private void hideFragments(FragmentTransaction transaction){  
        if (mUnfinishedPaymentStateFragment != null) {
        	transaction.hide(mUnfinishedPaymentStateFragment);
        }else if(mFinishedPaymentStateMobileFragment!=null){
        	transaction.hide(mFinishedPaymentStateMobileFragment);
        }else if(mFinishedPaymentStateCashFragment!=null){
        	transaction.hide(mFinishedPaymentStateCashFragment);
        }else if(mFinishedPaymentStateBalanceFragment!=null){
        	transaction.hide(mFinishedPaymentStateBalanceFragment);
        }else if(mUnfinishedPaymentStateLeaveFragment!=null){
        	transaction.hide(mUnfinishedPaymentStateLeaveFragment);
        }
    }

	private void changeSelect(int resId) {  
		mCurrentId = resId;
		mUnfinishedPaymentStateTV.setSelected(false);
		mUnfinishedPaymentStateTV.setBackgroundResource(R.color.gray);
		mFinishedPaymentStateMobileTV.setSelected(false);  
		mFinishedPaymentStateMobileTV.setBackgroundResource(R.color.gray);
		mFinishedPaymentStateCashTV.setSelected(false);
		mFinishedPaymentStateCashTV.setBackgroundResource(R.color.gray);
		mFinishedPaymentStateBalanceTV.setSelected(false);
		mFinishedPaymentStateBalanceTV.setBackgroundResource(R.color.gray);
		mUnfinishedPaymentStateLeaveTV.setSelected(false);  
		mUnfinishedPaymentStateLeaveTV.setBackgroundResource(R.color.gray);
        switch (resId) {  
        case R.id.tv_payment_state_unfinished_integrated_query:  
        	mUnfinishedPaymentStateTV.setSelected(true);  
        	mUnfinishedPaymentStateTV.setBackgroundResource(R.color.orange);
            break;  
        case R.id.tv_payment_state_finished_mobile_integrated_query:  
        	mFinishedPaymentStateMobileTV.setSelected(true);  
        	mFinishedPaymentStateMobileTV.setBackgroundResource(R.color.orange);
            break;
        case R.id.tv_payment_state_finished_cash_integrated_query:  
        	mFinishedPaymentStateCashTV.setSelected(true);  
        	mFinishedPaymentStateCashTV.setBackgroundResource(R.color.orange);
            break;
        case R.id.tv_payment_state_finished_balance_integrated_query:  
        	mFinishedPaymentStateBalanceTV.setSelected(true);  
        	mFinishedPaymentStateBalanceTV.setBackgroundResource(R.color.orange);
            break;
        case R.id.tv_payment_state_unfinished_leave_integrated_query:  
        	mUnfinishedPaymentStateLeaveTV.setSelected(true);  
        	mUnfinishedPaymentStateLeaveTV.setBackgroundResource(R.color.orange);
            break;
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
	
	public String formatDate(){
		StringBuilder dateBuffer = new StringBuilder(Integer.toString(mYear) + "-");
		if(mMonth<10){
			dateBuffer.append("0");
		}
		dateBuffer.append(Integer.toString(mMonth) + "-");
		if(mDay<10){
			dateBuffer.append("0");
		}
		dateBuffer.append(Integer.toString(mDay));
		return dateBuffer.toString();
	}
	
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEARCH_SUCCESS:
                	Toast.makeText(getApplicationContext(), "查询成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){  
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction()!=null && intent.getAction().equals("ExitApp")){
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

package com.example.inspector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ParkingSpaceDetailActivity extends FragmentActivity {
	private Fragment mParkingInformationFragment;
	private Fragment mTodayRecordFragment;
	private TextView mParkingInformationTV;
	private TextView mRecordOfTodayTV;
	private int mCurrentId;
	private String mLicensePlateNumber;
	private OnClickListener mTabClickListener = new OnClickListener() {
        @Override  
        public void onClick(View v) {  
            if (v.getId() != mCurrentId) {//如果当前选中跟上次选中的一样,不需要处理  
                changeSelect(v.getId());//改变图标跟文字颜色的选中   
                changeFragment(v.getId());//fragment的切换  
                mCurrentId = v.getId();//设置选中id  
            }  
        }  
    };  
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_parking_space_detail);
        mParkingInformationTV = (TextView) findViewById(R.id.tv_parking_Information_parking_detail);
        mRecordOfTodayTV = (TextView) findViewById(R.id.tv_record_today_parking_detail);
        mParkingInformationTV.setOnClickListener(mTabClickListener); 
        mRecordOfTodayTV.setOnClickListener(mTabClickListener);
        changeSelect(R.id.tv_parking_Information_parking_detail);
        changeFragment(R.id.tv_parking_Information_parking_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
	}

	private void changeFragment(int resId) {  
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务  
        hideFragments(transaction);//隐藏所有fragment  
        if(resId==R.id.tv_parking_Information_parking_detail){
        	mParkingInformationFragment = new ParkingInformationFragment();
        	 transaction.replace(R.id.parking_container, mParkingInformationFragment);
        }else if(resId==R.id.tv_record_today_parking_detail){
        	mTodayRecordFragment = new TodayRecordFragment();  
        	transaction.replace(R.id.parking_container, mTodayRecordFragment);
        }
        transaction.commit();//一定要记得提交事务  
    }

	private void hideFragments(FragmentTransaction transaction){  
        if (mParkingInformationFragment != null) {
        	transaction.hide(mParkingInformationFragment);
        }else if(mTodayRecordFragment!=null){
        	transaction.hide(mTodayRecordFragment);
        }
    }

	private void changeSelect(int resId) {  
		mParkingInformationTV.setSelected(false);
		mParkingInformationTV.setBackgroundResource(R.color.gray);
		mRecordOfTodayTV.setSelected(false);  
		mRecordOfTodayTV.setBackgroundResource(R.color.gray);
        switch (resId) {  
        case R.id.tv_parking_Information_parking_detail:  
        	mParkingInformationTV.setSelected(true);
        	mParkingInformationTV.setBackgroundResource(R.color.orange);
            break;  
        case R.id.tv_record_today_parking_detail:  
        	mRecordOfTodayTV.setSelected(true);
        	mRecordOfTodayTV.setBackgroundResource(R.color.orange);
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

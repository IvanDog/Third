package com.example.inspector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ParkingInformationFragment extends Fragment {
	private View mView;
	private TextView mParkNumberTV;
	private TextView mLocationNumberTV;
	private TextView mLicenseNumberTV;
	private TextView mCarTypeTV;
	private TextView mParkTypeTV;
	private TextView mStartTimeTV;
	private String mParkNumber;
	private int mLocationNumber;
	private String mCarType;
	private String mParkType;
	private String mStartTime;
	private DBAdapter mDBAdapter;
	private String mLicensePlateNumber;
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	mView = inflater.inflate(R.layout.fragment_parking_space_detail, container, false);
	        return mView;
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        mLocationNumber = getActivity().getIntent().getExtras().getInt("locationNumber");
	        mParkNumber = getActivity().getIntent().getStringExtra("parkNumber");
	        mDBAdapter = new DBAdapter(getActivity());
			mParkNumberTV = (TextView)mView.findViewById(R.id.tv_parking_number_parking_detail);
			mParkNumberTV.setText("车场编号: " + mParkNumber);
	        mLocationNumberTV=(TextView)mView.findViewById(R.id.tv_location_number_parking_detail);
	        mLocationNumberTV.setText("泊位编号: " + mLocationNumber);
			mLicenseNumberTV=(TextView)mView.findViewById(R.id.tv_license_plate_number_parking_detail);
			mCarTypeTV=(TextView)mView.findViewById(R.id.tv_car_type_parking_detail);
			mParkTypeTV=(TextView)mView.findViewById(R.id.tv_parking_type_parking_detail);
	        mStartTimeTV=(TextView) mView.findViewById(R.id.tv_start_time_parking_detail);
	        setParkingInformation();
	    }

	    @Override
	    public void onStart() {
	        super.onStart();
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	        //new SQLThread().start();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	    }

	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	    }

	    @Override
	    public void onDetach() {
	        super.onDetach();
	    }

		public void setParkingInformation(){
	        	mDBAdapter.open();
	        	Cursor cursor = mDBAdapter.getParkingByLocationNumber(mLocationNumber,mParkNumber);
	            try {
	            	      cursor.moveToFirst();
	            	      mLicensePlateNumber =  cursor.getString(cursor.getColumnIndex("licenseplate"));
	      		          mCarType = cursor.getString(cursor.getColumnIndex("cartype"));
	      		          mParkType = cursor.getString(cursor.getColumnIndex("parkingtype"));
	      		          mStartTime = cursor.getString(cursor.getColumnIndex("starttime"));
	      				  mLicenseNumberTV.setText("车牌号: " + mLicensePlateNumber);
	      		    	  mCarTypeTV.setText("车辆类型: " + mCarType);
	      		          mParkTypeTV.setText("泊车类型: " + mParkType);
	      		          mStartTimeTV.setText(mStartTime);
	            }
	            catch (Exception e) {
	                    e.printStackTrace();
	            } finally{
	                	if(cursor!=null){
	                		cursor.close();
	                    }
	            }
		}
	    /*public class SQLThread extends Thread {
	        @Override
	        public void run () {
	        	mDBAdapter.open();
	        	Cursor cursor = mDBAdapter.getParkingByLocationNumber(mLocationNumber,mParkNumber);
	            try {
	            	      cursor.moveToFirst();
	            	      mLicensePlateNumber =  cursor.getString(cursor.getColumnIndex("licenseplate"));
	      		          mCarType = cursor.getString(cursor.getColumnIndex("cartype"));
	      		          mParkType = cursor.getString(cursor.getColumnIndex("parkingtype"));
	      		          mStartTime = cursor.getString(cursor.getColumnIndex("starttime"));
	      				  mLicenseNumberTV.setText("车牌号: " + mLicensePlateNumber);
	      		    	  mCarTypeTV.setText("车辆类型: " + mCarType);
	      		          mParkTypeTV.setText("泊车类型: " + mParkType);
	      		          mStartTimeTV.setText(mStartTime);
	            }
	            catch (Exception e) {
	                    e.printStackTrace();
	            } finally{
	                	if(cursor!=null){
	                		cursor.close();
	                    }
	            }
	        }
	    }*/
	    
		private Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage (Message msg) {
	            super.handleMessage(msg);
	            switch (msg.what) {
	                default:
	                    break;
	            }
	        }
	    };
}

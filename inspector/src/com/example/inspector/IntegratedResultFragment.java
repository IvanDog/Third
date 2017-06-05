package com.example.inspector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class IntegratedResultFragment extends Fragment {
	public static final int TYPE_UNFINISHED_PAYMENT_STATE = 101;
	public static final int TYPE_FINISHED_PAYMENT_STATE_MOBILE = 102;
	public static final int TYPE_FINISHED_PAYMENT_STATE_CASH = 103;
	public static final int TYPE_FINISHED_PAYMENT_STATE_BALANCE = 104;
	public static final int TYPE_UNFINISHED_PAYMENT_STATE_LEAVE = 105;
	public static final int NO_PARKING_RECORD =201;
	private View mView;
	private ListView mListView;
	private View mDivider;
	private TextView mEmptyNotifyTV;
	private int mType;
	private String mDate;
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
	private DBAdapter mDBAdapter;
	public IntegratedResultFragment(int type, String date, String administrativeDivision, String parkingName, String parkingNumber,
			String tollCollectorName, String tollCollectorNumber, String licenseNumber, String carType, String parkingType, int locationNumber, int expense){
		mType = type;
		mDate = date;
		mAdministrativeDivision = administrativeDivision;
		mParkingName = parkingName;
		mParkingNumber = parkingNumber;
		mTollCollectorName = tollCollectorName;
		mTollCollectorNumber = tollCollectorNumber;
		mLicenseNumber = licenseNumber;
		mCarType = carType;
		mParkingType = parkingType;
		mLocationNumber = locationNumber;
		mExpense = expense;
	}
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
	    	mView = inflater.inflate(R.layout.fragment_result_integrated_query, container, false);
	        mListView=(ListView)mView.findViewById(R.id.list_result_integrated_query);  
	        mDivider=mView.findViewById(R.id.list_divider_integrated_query);  
	        mEmptyNotifyTV=(TextView)mView.findViewById(R.id.tv_notify_resule_list_empty_integrated_query);  
	        mDBAdapter = new DBAdapter(getActivity());
	        List<Map<String, Object>> list=getData(mType);  
	        mListView.setAdapter(new IntegratedQueryResultListAdapter(getActivity(), list)); 
	        return mView;
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	    }

	    @Override
	    public void onStart() {
	        super.onStart();
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
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

	    public List<Map<String, Object>> getData(int type){  
	        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
	        setHistoryRecord(mDate,mType,list);
	        return list;  
	    }
	    
	    public void setHistoryRecord(String date,int type,List<Map<String, Object>> list){
	    	mDBAdapter.open();
	    	String paymentState = new String();
	    	if(mType==TYPE_UNFINISHED_PAYMENT_STATE){
	    		paymentState = "未付";
	    	}else if(mType==TYPE_FINISHED_PAYMENT_STATE_MOBILE){
	    		paymentState = "移动支付";
	    	}else if(mType==TYPE_FINISHED_PAYMENT_STATE_CASH){
	    		paymentState = "现金支付";
	    	}else if(mType==TYPE_FINISHED_PAYMENT_STATE_BALANCE){
	    		paymentState = "余额支付";
	    	}else if(mType==TYPE_UNFINISHED_PAYMENT_STATE_LEAVE){
	    		paymentState = "逃费";
	    	}
	    	Cursor cursor = mDBAdapter.getParking(mAdministrativeDivision, mParkingName, mParkingNumber, mTollCollectorName,
        			mTollCollectorNumber, mLicenseNumber, mCarType, mParkingType, mLocationNumber, date+"%", null, null, mExpense);
	    	Log.e("yifan","count : " + cursor.getCount());
            int count = 0;
	        try {
	        	do{
	        		      Log.e("yifan","paymentpattern : " + cursor.getString(cursor.getColumnIndex("paymentpattern")));
	        	    	  if((cursor.getString(cursor.getColumnIndex("paymentpattern"))).equals(paymentState) ){
	        	    		  Map<String, Object> map=new HashMap<String, Object>();
	        	    		  map.put("administrativeDivision", cursor.getString(cursor.getColumnIndex("administrativedivision")));
	        	    		  map.put("parkingName", cursor.getString(cursor.getColumnIndex("parkingname")));
	        	    		  map.put("parkingnumber", cursor.getString(cursor.getColumnIndex("parkingnumber")));
	        	    		  map.put("toolcollectorName", cursor.getString(cursor.getColumnIndex("toolcollectorname")));
	        	    		  map.put("toolcollectorNumber", cursor.getString(cursor.getColumnIndex("toolcollectornumber")));
	        	    		  map.put("licensePlateNumber", cursor.getString(cursor.getColumnIndex("licenseplate")));
	        	    		  map.put("carType", cursor.getString(cursor.getColumnIndex("cartype")));
	        	    		  map.put("parkingType", cursor.getString(cursor.getColumnIndex("parkingtype")));
	        	    		  map.put("parkingLocation", cursor.getInt(cursor.getColumnIndex("locationnumber"))+"");
	        	    		  map.put("startTime", "入场: " + cursor.getString(cursor.getColumnIndex("starttime")));
	        	    		  if(cursor.getString(cursor.getColumnIndex("leavetime"))==null  || (cursor.getString(cursor.getColumnIndex("leavetime")).equals(""))){
	        	    			  map.put("leaveTime", null);
	        	    		  }else{
		        	    		  map.put("leaveTime", "离场: " + cursor.getString(cursor.getColumnIndex("leavetime")));
	        	    		  }
	        	    		  map.put("paymentState", cursor.getString(cursor.getColumnIndex("paymentpattern")));
	        	    		  if(cursor.getString(cursor.getColumnIndex("expense"))==null){
		        	    		  map.put("expense", null);
	        	    		  }else{
		        	    		  map.put("expense", cursor.getInt(cursor.getColumnIndex("expense"))+"");
	        	    		  }
	      		              list.add(map); 
	      		              count++;
	        	    	  }
	        	      }while(cursor.moveToNext());
	        }catch (Exception e) {
	                e.printStackTrace();
	        } finally{
       	            if(count==0){
    		            Message msg = new Message();
    		            msg.what=NO_PARKING_RECORD;
    		            mHandler.sendMessage(msg);
    	            }
	            	if(cursor!=null){
	            		cursor.close();
	                }
	        }
	    }
	    
		private Handler mHandler = new Handler() {
		    @Override
		    public void handleMessage (Message msg) {
		        super.handleMessage(msg);
		        switch (msg.what) {
		            case NO_PARKING_RECORD:
    		            //mDivider.setVisibility(View.GONE);
		            	mEmptyNotifyTV.setVisibility(View.VISIBLE);
		                break;
		            default:
		                break;
		        }
		    }
		};
		
}

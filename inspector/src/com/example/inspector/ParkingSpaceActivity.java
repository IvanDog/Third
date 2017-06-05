package com.example.inspector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ParkingSpaceActivity extends Activity {
	private ProgressBar mProgressBar;
	private ListView mListView;
	private TextView mlicenseplatenumber;
	private String mLicenseNumber;
	private DBAdapter mDBAdapter;
	private TextView mTotalParkingNumberTV;
	private TextView mIdleParkingNumberTV;
	private  static final int MAX_LOCATION_SIZE = 10;
	private int mIdleLocationNumber = 10;
	private static final String PARKING_NUMBER = "P1234";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parking_space_management);
		mDBAdapter = new DBAdapter(this);
		mProgressBar=(ProgressBar) findViewById(R.id.progressBar_parking_space);
		mProgressBar.setMax(MAX_LOCATION_SIZE);//设置进度条最大值
		mTotalParkingNumberTV=(TextView)findViewById(R.id.tv_parking_number_total_parking_space);
		mTotalParkingNumberTV.setText("车位总数：" + MAX_LOCATION_SIZE);
		mIdleParkingNumberTV=(TextView)findViewById(R.id.tv_parking_number_idle_parking_space);
		mListView=(ListView)findViewById(R.id.list_parking_detail);  
        List<Map<String, Object>> list=getData();  
        mListView.setAdapter(new ParkingPlaceListAdapter(this, list)); 
        mlicenseplatenumber=(TextView)findViewById(R.id.tv_license_plate_parking_detail);
        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	Map<String,Object> map=(Map<String,Object>)mListView.getItemAtPosition(arg2);
                String licensePlateNumber=(String)map.get("licensePlateNumber");
                int locationNumber=Integer.valueOf(map.get("locationNumber").toString());
                String parkNumber = PARKING_NUMBER;
                if(licensePlateNumber==null){
                	Intent intent = new Intent(ParkingSpaceActivity.this,TodayRecordActivity.class);
	        	    Bundle bundle = new Bundle();
	        	    bundle.putInt("locationNumber", locationNumber);
	        	    bundle.putString("parkNumber", parkNumber);
	        	    intent.putExtras(bundle);
	        	    startActivity(intent);
                }else{
                	Intent intent = new Intent(ParkingSpaceActivity.this,ParkingSpaceDetailActivity.class);
	        	    Bundle bundle = new Bundle();
	        	    bundle.putInt("locationNumber", locationNumber);
	        	    bundle.putString("parkNumber", parkNumber);
	        	    intent.putExtras(bundle);
	        	    startActivity(intent);
                }
            }
        });
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain"); 
        registerReceiver(mReceiver, filter); 
	}
    public List<Map<String, Object>> getData(){  
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
        for (int i = 1; i <= MAX_LOCATION_SIZE; i++) {  
            Map<String, Object> map=new HashMap<String, Object>();  
            map.put("locationNumber",  i+"");
            String licenseNumber = getLicenseNumber(i);
            map.put("licensePlateNumber", licenseNumber);
            if(licenseNumber!=null){
            	map.put("inUseIcon", R.drawable.ic_car_in_parking_24px);
            }
            if(licenseNumber!=null){
            	mIdleLocationNumber--;
            }
            list.add(map);  
        }
        mProgressBar.setProgress(mIdleLocationNumber);
        mIdleParkingNumberTV.setText("空闲车位：" + mIdleLocationNumber);
        return list;  
    }
    
    public String getLicenseNumber(int locationNumber){
    	mDBAdapter.open();
    	mLicenseNumber=null;
    	Cursor cursor = mDBAdapter.getParkingByLocationNumber(locationNumber, PARKING_NUMBER);
        try {
        	      cursor.moveToFirst();
        	      if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
      		          mLicenseNumber =  cursor.getString(cursor.getColumnIndex("licenseplate"));
        	      }
        }
        catch (Exception e) {
                e.printStackTrace();
        } finally{
            	if(cursor!=null){
            		cursor.close();
                }
        }
    	return mLicenseNumber;
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

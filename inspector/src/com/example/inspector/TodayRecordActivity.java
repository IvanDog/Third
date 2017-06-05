package com.example.inspector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TodayRecordActivity extends Activity {
	public static final int NO_TODAY_PARKING_RECORD =101;
	public static final int DISPLAY_TOTAL_FEE =102;
	private View mView;
	private ListView mListView;
	private TextView mTotalFeeTodayTV;
	private TextView mEmptyNotifyTV;
	private String mParkNumber;
	private int mLocationNumber;
	private DBAdapter mDBAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_record);
        mListView=(ListView)findViewById(R.id.list_record_today);  
        mTotalFeeTodayTV= (TextView)findViewById(R.id.tv_total_fee_activity_record_today);
        mEmptyNotifyTV=(TextView)findViewById(R.id.tv_notify_list_empty_activity_today_record);  
        Intent intent = getIntent();
        mParkNumber = intent.getExtras().getString("parkNumber");
        mLocationNumber=intent.getExtras().getInt("locationNumber");
        mDBAdapter = new DBAdapter(this);
        List<Map<String, Object>> list=getData();  
        mListView.setAdapter(new TodayRecordListAdapter(this, list)); 
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain"); 
        registerReceiver(mReceiver, filter); 
	}
	
	public List<Map<String, Object>> getData(){  
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd%"); 
		Date curDate = new Date(System.currentTimeMillis());
		String date = formatter.format(curDate);
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
        setTodayRecord(date,mLocationNumber,list);
        return list;  
    }

    public void setTodayRecord(String date,int locationNumber,List<Map<String, Object>> list){
    	mDBAdapter.open();
    	Cursor cursor = mDBAdapter.getParking(null,null,mParkNumber,null,null,null,null,null,mLocationNumber,date,null,null,-1);
    	Map<String, Object> titleMap=new HashMap<String, Object>();
    	titleMap.put("licensePlateNumber","牌照号码");
    	titleMap.put("startTime","入场时间");
    	titleMap.put("leaveTime", "离场时间");
    	titleMap.put("paymentState","支付状态");
    	titleMap.put("expense", "支付金额");
        list.add(titleMap); 
    	int count = 0;
        int totalFee = 0;
        try {
        	do{
        	    		  Map<String, Object> map=new HashMap<String, Object>();
        	    		  map.put("licensePlateNumber", cursor.getString(cursor.getColumnIndex("licenseplate")));
        	    		  map.put("startTime", "入场: " + cursor.getString(cursor.getColumnIndex("starttime")));
        	    		  if(cursor.getString(cursor.getColumnIndex("leavetime"))==null){
        	    			  map.put("leaveTime", null);
        	    		  }else{
	        	    		  map.put("leaveTime", "离场: " + cursor.getString(cursor.getColumnIndex("leavetime")));
        	    		  }
        	    		  map.put("paymentState", cursor.getString(cursor.getColumnIndex("paymentpattern")));
        	    		  map.put("expense", cursor.getInt(cursor.getColumnIndex("expense")) + "元");
      		              list.add(map); 
      		              count++;
      		              totalFee = totalFee + cursor.getInt(cursor.getColumnIndex("expense"));
        	      }while(cursor.moveToNext());
        }catch (Exception e) {
                e.printStackTrace();
        } finally{
        	    if(count==0){
        		    list.remove(titleMap);
		            Message msg = new Message();
		            msg.what=NO_TODAY_PARKING_RECORD;
		            mHandler.sendMessage(msg);
        	    }else{
		            Message msg = new Message();
		            msg.obj = String.valueOf(totalFee);
		            msg.what=DISPLAY_TOTAL_FEE;
		            mHandler.sendMessage(msg);
        	    }
            	if(cursor!=null){
            		cursor.close();
                }
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
            case NO_TODAY_PARKING_RECORD:
            	mEmptyNotifyTV.setVisibility(View.VISIBLE);
            	mTotalFeeTodayTV.setVisibility(View.GONE);
                break;
            case DISPLAY_TOTAL_FEE:
            	mTotalFeeTodayTV.setText("今日收费合计: " + (String)msg.obj + "元");
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


package com.example.inspector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.inspector.R.drawable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MessageCenterActivity extends Activity {
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_center);
		mListView=(ListView)findViewById(R.id.list_message_center);  
        List<Map<String, Object>> list=getData();  
        mListView.setAdapter(new MessageCenterListAdapter(this, list)); 
        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	//TODO
            }
        });
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        registerReceiver(mReceiver, filter); 
	}

    public List<Map<String, Object>> getData(){  
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
        for (int i = 1; i <= 2; i++) {  
            Map<String, Object> map=new HashMap<String, Object>();  
            if(i==1){
                map.put("messageCenterImage",  drawable.ic_add_alert_black_18dp);
                map.put("messageCenterTitle", "考勤通知");
                map.put("messageCenterDetail", "您5月30日出现一次考勤异常");
                map.put("messageCenterTime", "2017.05.31" + " " + "09:00:30");
                map.put("messageCenterDetailHide", "您5月30日上班打卡时间08:40:36(上班时间9:00)，" +
                		"下班打卡时间15:30:23(下班时间17:30)，存在异常，请联系考勤员确认。");
            }else if(i==2){
                map.put("messageCenterImage",  drawable.ic_error_outline_black_18dp);
                map.put("messageCenterTitle", "异常泊车");
                map.put("messageCenterDetail", "八里台工业园区—易华录停车场出现异常泊车");
                map.put("messageCenterTime", "2017.05.31" + " " + "09:30:30");
                map.put("messageCenterDetailHide", "八里台工业园区—易华录停车场出现异常泊车，现象为超长泊车，" +
                		"泊位号3，请前去查看。");
            }
            list.add(map);  
        }  
        return list;  
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
			}
		}            
    }; 
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}

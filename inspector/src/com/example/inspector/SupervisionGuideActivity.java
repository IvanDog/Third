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

public class SupervisionGuideActivity extends Activity {
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supervision_guide);
		mListView=(ListView)findViewById(R.id.list_supervision_guide);  
        List<Map<String, Object>> list=getData();  
        mListView.setAdapter(new SupervisionGuideListAdapter(this, list)); 
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
        for (int i = 1; i <= 3; i++) {  
            Map<String, Object> map=new HashMap<String, Object>();  
            if(i==1){
                map.put("supervisionGuideImage",  drawable.ic_error_outline_black_18dp);
                map.put("supervisionGuideTitle", "监督条例1");
                map.put("supervisionGuideDetail", "收费员不在场处罚条例");
                map.put("supervisionGuideDetailHide", "1.收费员不在岗10分钟内，罚款10元。" + "\n" +
                		"2.收费员不在岗10-30分钟，罚款100元。" + "\n" + "3.收费员不在岗30分钟以上，罚款200元。");
            }else if(i==2){
                map.put("supervisionGuideImage",  drawable.ic_error_outline_black_18dp);
                map.put("supervisionGuideTitle", "监督条例2");
                map.put("supervisionGuideDetail", "收费员未开具发票处罚条例");
                map.put("supervisionGuideDetailHide", "1.收费员未开具发票金额10元内，罚款20元。" + "\n" +
                		"2.收费员未开具发票金额10-50元，罚款100元。" + "\n" + "3.收费员未开具发票金额50元以上，罚款200元。");
            }else if(i==3){
                map.put("supervisionGuideImage",  drawable.ic_error_outline_black_18dp);
                map.put("supervisionGuideTitle", "监督条例3");
                map.put("supervisionGuideDetail", "收费员乱收费处罚条例");
                map.put("supervisionGuideDetailHide", "1.收费员违规收费10元内，罚款20元。" + "\n" +
                		"2.收费员违规收费10-50分钟，罚款100元。" + "\n" + "3.收费员违规收费50元以上，罚款200元。");
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

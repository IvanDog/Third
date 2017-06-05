package com.example.inspector;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingListAdapter extends BaseAdapter {  
	  
    private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public int clickPosition = -1;
    public ParkingListAdapter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }
    
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
    	private TextView parkingNameTV; 
        private TextView distanceTV;  
        private TextView locationTV;  
        private TextView parkingNumberTotalTV; 
        private TextView parkingNumberInUseTV;
        private TextView parkingNumberIdleTV;
        public View navigation;  
    }  
    @Override  
    public int getCount() {  
        return data.size();  
    }  
    /** 
     * 获得某一位置的数据 
     */  
    @Override  
    public Object getItem(int position) {  
        return data.get(position);  
    }  
    /** 
     * 获得唯一标识 
     */  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        Zujian zujian=null;  
        if(convertView==null){  
            zujian=new Zujian();  
            //获得组件，实例化组件  
            convertView=layoutInflater.inflate(R.layout.list_parking_list, null);  
            zujian.parkingNameTV=(TextView)convertView.findViewById(R.id.tv_parking_name_parking_list);  
            zujian.distanceTV=(TextView)convertView.findViewById(R.id.tv_distance_parking_list);  
            zujian.locationTV=(TextView)convertView.findViewById(R.id.tv_location_parking_list);  
            zujian.parkingNumberTotalTV=(TextView)convertView.findViewById(R.id.tv_parking_number_total_parking_list);  
            zujian.parkingNumberInUseTV = (TextView)convertView.findViewById(R.id.tv_parking_number_in_use_parking_list); 
            zujian.parkingNumberIdleTV = (TextView)convertView.findViewById(R.id.tv_parking_number_idle_parking_list); 
            zujian.navigation=(View)convertView.findViewById(R.id.linear_navigation_parking_list);
            
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        convertView.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
				Intent intent = new Intent(context,ParkingSpaceActivity.class);
				context.startActivity(intent);
        	}
        });
        zujian.parkingNameTV.setText((String)data.get(position).get("parkingName"));
        zujian.distanceTV.setText(((Integer)data.get(position).get("distance")).toString() + "米");
        zujian.locationTV.setText((String)data.get(position).get("location"));  
        zujian.parkingNumberTotalTV.setText((String)data.get(position).get("parkingNumberTotal"));  
        zujian.parkingNumberInUseTV.setText((String)data.get(position).get("parkingNumberInUse"));  
        zujian.parkingNumberIdleTV.setText((String)data.get(position).get("parkingNumberIdle"));
        zujian.navigation.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
				if (isAvilible(context, "com.autonavi.minimap")) {
                    try{  
                         Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=driver&poiname=name&lat="+data.get(position).get("latitude")+"&lon="+data.get(position).get("longtitude")+"&dev=0");  
                         context.startActivity(intent);   
                    } catch (URISyntaxException e)  {
                    	e.printStackTrace(); 
                    } 
                }else{
                        Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
                        Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");  
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
                        context.startActivity(intent);
                    }
			}
            });
        return convertView;  
    }  
  
    /* 检查手机上是否安装了指定的软件 
     * @param context 
     * @param packageName：应用包名 
     * @return 
     */  
        public static boolean isAvilible(Context context, String packageName){   
            //获取packagemanager   
            final PackageManager packageManager = context.getPackageManager();  
            //获取所有已安装程序的包信息   
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);  
            //用于存储所有已安装程序的包名   
            List<String> packageNames = new ArrayList<String>();  
            //从pinfo中将包名字逐一取出，压入pName list中   
            if(packageInfos != null){   
                for(int i = 0; i < packageInfos.size(); i++){   
                    String packName = packageInfos.get(i).packageName;   
                    packageNames.add(packName);   
                }   
            }   
          //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE   
            return packageNames.contains(packageName);  
         }   
} 
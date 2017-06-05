package com.example.inspector;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IntegratedQueryResultListAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public IntegratedQueryResultListAdapter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{
    	public TextView administrativeDivisionTV;
    	public TextView parkingNameTV;
    	public TextView parkingNumberTV;
    	public TextView tollCollectorNameTV;
    	public TextView tollCollectorNumberTV;
        public TextView licensePlateNumberTV; 
        public TextView carTypeTV;
        public TextView parkingTypeTV;
        public TextView startTimeTV; 
        public TextView leaveTimeTV; 
        public TextView parkingLocationTV; 
        public TextView paymentBillTV; 
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
    public View getView(int position, View convertView, ViewGroup parent) {  
        Zujian zujian=null;  
        if(convertView==null){  
            zujian=new Zujian();  
            //获得组件，实例化组件  
            convertView=layoutInflater.inflate(R.layout.list_result_integrated_query, null);  
            zujian.administrativeDivisionTV=(TextView)convertView.findViewById(R.id.tv_administrative_division_integrated_query); 
            zujian.parkingNameTV=(TextView)convertView.findViewById(R.id.tv_parking_name_integrated_query);  
            zujian.parkingNumberTV=(TextView)convertView.findViewById(R.id.tv_parking_number_integrated_query);  
            zujian.tollCollectorNameTV=(TextView)convertView.findViewById(R.id.tv_toll_collector_name_integrated_query);  
            zujian.tollCollectorNumberTV=(TextView)convertView.findViewById(R.id.tv_toll_collector_number_integrated_query);  
            zujian.licensePlateNumberTV=(TextView)convertView.findViewById(R.id.tv_license_plate_integrated_query); 
            zujian.carTypeTV=(TextView)convertView.findViewById(R.id.tv_car_type_integrated_query);  
            zujian.parkingTypeTV=(TextView)convertView.findViewById(R.id.tv_parking_type_integrated_query);  
            zujian.startTimeTV=(TextView)convertView.findViewById(R.id.tv_start_time_integrated_query);  
            zujian.leaveTimeTV=(TextView)convertView.findViewById(R.id.tv_leave_time_integrated_query);  
            zujian.parkingLocationTV=(TextView)convertView.findViewById(R.id.tv_parking_number_integrated_query);  
            zujian.paymentBillTV=(TextView)convertView.findViewById(R.id.tv_expense_integrated_query);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.administrativeDivisionTV.setText((String)data.get(position).get("administrativeDivision"));  
        zujian.parkingNameTV.setText((String)data.get(position).get("parkingName"));  
        zujian.parkingNumberTV.setText((String)data.get(position).get("parkingnumber"));  
        zujian.tollCollectorNameTV.setText("收费员:" + (String)data.get(position).get("toolcollectorName"));  
        zujian.tollCollectorNumberTV.setText("工号:" + (String)data.get(position).get("toolcollectorNumber"));  
        zujian.licensePlateNumberTV.setText((String)data.get(position).get("licensePlateNumber"));  
        zujian.carTypeTV.setText((String)data.get(position).get("carType"));  
        zujian.parkingTypeTV.setText((String)data.get(position).get("parkingType"));  
        zujian.startTimeTV.setText((String)data.get(position).get("startTime"));  
        zujian.leaveTimeTV.setText((String)data.get(position).get("leaveTime"));  
        String parkingLocation = (String)data.get(position).get("parkingLocation");
        if(parkingLocation.equals("泊位")){
        	zujian.parkingLocationTV.setText(parkingLocation);  
        }else if(parkingLocation.length()==1){
            zujian.parkingLocationTV.setText("泊位:" + "A000" + parkingLocation);  
        }else if(parkingLocation.length()==2){
            zujian.parkingLocationTV.setText("泊位:" + "A00" + parkingLocation);  
        }
        zujian.paymentBillTV.setText((String)data.get(position).get("expense") + "元"); 
        return convertView;  
    }

}

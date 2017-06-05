package com.example.inspector;

import java.util.List;
import java.util.Map;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SupervisionGuideListAdapter extends BaseAdapter {  
	  
    private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;
    public int clickPosition = -1;
    public Boolean flag = false;
    public SupervisionGuideListAdapter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
        private TextView supervisionGuideTitleTV;
        private TextView supervisionGuideDetailTV;
        private ImageView entersupervisionGuideDetail;
    	private LinearLayout supervisionGuideHideDetail;
    	private TextView supervisionGuideDetailHideTV;
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
            convertView=layoutInflater.inflate(R.layout.list_supervision_guide, null);  
            zujian.supervisionGuideTitleTV=(TextView)convertView.findViewById(R.id.tv_title_supervision_guide);  
            zujian.supervisionGuideDetailTV=(TextView)convertView.findViewById(R.id.tv_detail_supervision_guide);  
            zujian.entersupervisionGuideDetail=(ImageView)convertView.findViewById(R.id.iv_enter_detail_supervision_guide);
            zujian.supervisionGuideHideDetail=(LinearLayout)convertView.findViewById(R.id.list_supervision_guide_hide);
            zujian.supervisionGuideDetailHideTV=(TextView)convertView.findViewById(R.id.tv_supervision_guide_detail_hide);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.supervisionGuideTitleTV.setText((String)data.get(position).get("supervisionGuideTitle")); 
        zujian.supervisionGuideDetailTV.setText((String)data.get(position).get("supervisionGuideDetail")); 
        zujian.supervisionGuideDetailHideTV.setText((String)(data.get(position).get("supervisionGuideDetailHide"))); 
		Drawable drawable = context.getResources().getDrawable((Integer)data.get(position).get("supervisionGuideImage"));
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
		zujian.supervisionGuideTitleTV.setCompoundDrawables(drawable, null, null, null);
		if (clickPosition == position) {
		    if (zujian.entersupervisionGuideDetail.isSelected()) {
		    	    zujian.entersupervisionGuideDetail.setSelected(false);
		    	    zujian.entersupervisionGuideDetail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                    zujian.supervisionGuideHideDetail.setVisibility(View.GONE);
                    clickPosition=-1;
		    }else{       
		    	    zujian.entersupervisionGuideDetail.setSelected(true);
		    	    zujian.entersupervisionGuideDetail.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    zujian.supervisionGuideHideDetail.setVisibility(View.VISIBLE);
              }
        } else {
        	zujian.supervisionGuideHideDetail.setVisibility(View.GONE);
        	zujian.entersupervisionGuideDetail.setSelected(false);
    	    zujian.entersupervisionGuideDetail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
        }
		zujian.entersupervisionGuideDetail.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	clickPosition = position;
		    	notifyDataSetChanged();
		    }
		});
        return convertView;  
    }  
  
} 
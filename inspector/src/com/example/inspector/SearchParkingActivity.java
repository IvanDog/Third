package com.example.inspector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchParkingActivity extends Activity implements  LocationSource, AMapLocationListener, AMap.OnMapClickListener,
AMap.OnInfoWindowClickListener,AMap.InfoWindowAdapter,AMap.OnMarkerClickListener,PoiSearch.OnPoiSearchListener,Inputtips.InputtipsListener,OnGeocodeSearchListener,RouteSearch.OnRouteSearchListener{
	private Context mContext;
	private TextView mCityTV;
	private View mRelativeParkingsList;
	private ListView mParkingsList=null; 
	private TextView mAllParkingTypeParkingListTV;
	private TextView mOutsideParkingTypeParkingListTV;
	private TextView mInsideParkingTypeParkingListTV;
	private TextView mEmptyParkingListNotifyTV;
	private TextView mNotifyInputLocationTV;
	private ListView mSearchList=null;
	private ListView mParkingDetailList=null;
	private TextView mEmptyParkingDetailNotifyTV;
	private AlertDialog mDialog;
	private AlertDialog mParkingDetailDialog;
    private View mContainer;
    private TextView mPoiNameTV;
	private String mCurrentCity = "天津";
	
	private TextView mAllParkingTypeTV;
	private TextView mOutsideParkingTypeTV;
	private TextView mInsideParkingTypeTV;
	private String mParkingType = "150903|150904|150905|150906";
    private int mCurrentId;
    private int mCurrentParkingTypeId;
	private MapView mapView;//地图控件
	private AMap mAMAP;//地图控制器对象

	private DBAdapter mDBAdapter;
	private static final String PARKING_NUMBER = "P1234";
	private  static final int MAX_LOCATION_SIZE = 10;
	private int mInUseCount = -1; 
	
	private Toast mToast;
	private Toast mLocationToast;
	
	/**
 	 *      定位需要的声明
 	 */
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mLocationListener = null;//定位监听器
    private boolean isFirstLoc = true;//标识，用于判断是否只显示一次定位信息和用户重新定位
    private AutoCompleteTextView mKey = null;
    private ImageView mParkingIV;
    private ImageView mDeleteIV;
    private PoiSearch mPoiSearch;
    private MyLocationStyle mMyLocationStyle;
    private PoiResult poiResult;//the result of the poi
    private int currentPage = 0;//the page start with 0
    private PoiSearch.Query mQuery;//poi query
    private Marker locationMarker;//选择点
    private Marker mDetailMarker;
    private Marker mlastMarker;
    private myPoiOverlay poiOverlay;//poi图层
    private List<PoiItem> poiItems;//poi数据
    private PoiItem mPoi;
    private RelativeLayout mPoiDetail;
	private LatLonPoint lp = new LatLonPoint(39.1366672021, 117.2100419600);
    private String keyWord = "";
    private int mSearchTag = 0;
    private List<Map<String, Object>> mList=new ArrayList<Map<String,Object>>(); 
    private GeocodeSearch mGeocoderSearch;
    private RouteSearch mRouteSearch;
    protected LatLonPoint mStartLatlng ;
    protected LatLonPoint mEndLatlng ;
    private DriveRouteResult mDriveRouteResult;
	private ProgressDialog mProgDialog = null;// 搜索时进度条
	private boolean mIsZoomByRoute;
	
	private View mDialogParkingDetail;
	private TextView mDialogParkingNameTV;
	private View mDialogParkingNumberDetails;
	private TextView mDialogParkingNumberTotalTV;
	private TextView mDialogParkingNumberInUseTV;
	private TextView mDialogParkingNumberFreeTV;
	private TextView mDialogDetailTV;
	private TextView mNavigationTV;
	private Double mCurrentDialogLatitude;
	private Double mCurrentDialogLongtitude;
    private static final int EVENT_SHOW_DIALOG = 101;
    private static final int EVENT_DISPLAY_USER_INFORMATION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        mContext=this;
		mDBAdapter = new DBAdapter(this);
     	/**
     	 *      停车场信息对话框
     	 */
    	mDialogParkingDetail = (View)findViewById(R.id.dialog_parking_information_search);
    	mDialogParkingDetail.setAlpha(0.8f);
    	mDialogParkingNameTV = (TextView)findViewById(R.id.tv_poi_name_dialog);
    	mDialogParkingNumberTotalTV = (TextView)findViewById(R.id.tv_parking_number_all_dialog);
    	mDialogParkingNumberInUseTV = (TextView)findViewById(R.id.tv_parking_number_in_use_dialog);
    	mDialogParkingNumberFreeTV = (TextView)findViewById(R.id.tv_parking_number_free_dialog);
    	mDialogDetailTV = (TextView)findViewById(R.id.tv_parking_detail_dialog);
    	mDialogDetailTV.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
				Intent intent = new Intent(SearchParkingActivity.this,ParkingSpaceActivity.class);
				startActivity(intent);
    		}
    	});
    	mNavigationTV = (TextView)findViewById(R.id.tv_navigation_dialog);
    	mNavigationTV.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
				if (isAvilible(getApplicationContext(), "com.autonavi.minimap")) {
                    try{  
                         Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=driver&poiname=name&lat="+mCurrentDialogLatitude+"&lon="+mCurrentDialogLongtitude+"&dev=0");  
                         startActivity(intent);   
                    } catch (URISyntaxException e)  {
                    	e.printStackTrace(); 
                    } 
                }else{
                	    if(mToast!=null){
                	    	mToast.cancel();
                	    }
                	    mToast = Toast.makeText(getApplicationContext(), "您尚未安装高德地图", Toast.LENGTH_LONG);
                	    mToast.show();
                        Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");  
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
                        startActivity(intent);
                 }
    		}
    	});
    	
    	
     	/**
     	 *      城市选择控件
     	 */
        mCityTV=(TextView)findViewById(R.id.tv_city);  
        
        
     	/**
     	 *      切换停车场类别控件
     	 */
    	mAllParkingTypeTV=(TextView)findViewById(R.id.tv_all_parking_type_search);
		mAllParkingTypeTV.setBackgroundResource(R.color.gray);
    	mAllParkingTypeTV.setOnClickListener(mTabClickListener);
        mOutsideParkingTypeTV=(TextView)findViewById(R.id.tv_outside_parking_type_search);
		mOutsideParkingTypeTV.setBackgroundResource(R.color.gray);
        mOutsideParkingTypeTV.setOnClickListener(mTabClickListener);
    	mInsideParkingTypeTV=(TextView)findViewById(R.id.tv_inside_parking_type_search);
		mInsideParkingTypeTV.setBackgroundResource(R.color.gray);
    	mInsideParkingTypeTV.setOnClickListener(mTabClickListener);
    	changeSelect(R.id.tv_all_parking_type_search,1);
    	
     	/**
     	 *      地图控件
     	 */
        mapView = (MapView)findViewById(R.id.map_search);
        mapView.onCreate(savedInstanceState);
        mAMAP = mapView.getMap();          //获取地图对象
        mAMAP.setOnMapClickListener(this);
        mAMAP.setOnMarkerClickListener(this);
        mAMAP.setOnInfoWindowClickListener(this);
        mAMAP.setInfoWindowAdapter(this);
        UiSettings settings = mAMAP.getUiSettings();            //设置显示定位按钮 并且可以点击
        mAMAP.setLocationSource((LocationSource) this);            //设置定位监听
        settings.setMyLocationButtonEnabled(true);            // 是否显示定位按钮
        settings.setZoomControlsEnabled(false); //显示zoom按钮
        settings.setZoomGesturesEnabled(true);
        mAMAP.setMyLocationEnabled(true);            // 是否可触发定位并显示定位层
        mMyLocationStyle = new MyLocationStyle();
        mMyLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add_location_48px));
        mMyLocationStyle.radiusFillColor(android.R.color.transparent);
        mMyLocationStyle.strokeColor(android.R.color.transparent);
        mAMAP.setMyLocationStyle(mMyLocationStyle);
        
        initLoc();
        
     	/**
     	 *      设置地图移动时的回调
     	 */
        mAMAP.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            	// TODO Auto-generated method stub
            }
            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    if(!mIsZoomByRoute){
                    	if(lp.getLatitude() != cameraPosition.target.latitude || lp.getLongitude() != cameraPosition.target.longitude){
                        	lp = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
                        	showProgressDialog();
                            doSearchQuery(mCurrentCity,false);
                            Log.e("yifan","onCameraChangeFinish->doSearchQuery");
                    	}
                    }
            }
        });
        
        mContainer=(View)findViewById(R.id.frame_container_search);
        mRelativeParkingsList=(View)findViewById(R.id.relative_parking_list);
        mParkingsList=(ListView)findViewById(R.id.list_parking_list);  
        
    	mAllParkingTypeParkingListTV=(TextView)findViewById(R.id.tv_all_parking_type_parking_list);
		mAllParkingTypeParkingListTV.setBackgroundResource(R.color.gray);
		mAllParkingTypeParkingListTV.setOnClickListener(mParkingTabClickListener);
        mOutsideParkingTypeParkingListTV=(TextView)findViewById(R.id.tv_outside_parking_type_parking_list);
        mOutsideParkingTypeParkingListTV.setBackgroundResource(R.color.gray);
        mOutsideParkingTypeParkingListTV.setOnClickListener(mParkingTabClickListener);
    	mInsideParkingTypeParkingListTV=(TextView)findViewById(R.id.tv_inside_parking_type_parking_list);
		mInsideParkingTypeParkingListTV.setBackgroundResource(R.color.gray);
    	mInsideParkingTypeParkingListTV.setOnClickListener(mParkingTabClickListener);
    	changeSelect(R.id.tv_all_parking_type_parking_list,2);
    	
        mEmptyParkingListNotifyTV=(TextView)findViewById(R.id.tv_empty_list_notify_parking_list_main);  
        mNotifyInputLocationTV=(TextView)findViewById(R.id.tv_notify_input_location_parking_list_main);  
        mParkingsList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	//TODO
            }
        });
        
        
     	/**
     	 *      经纬度解析对象，构造 GeocodeSearch 对象，并设置监听
     	 */
        mGeocoderSearch = new GeocodeSearch(this);
        mGeocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
           	     Log.e("yifan","onRegeocodeSearched");
            }
 
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int returnCode) {
                //判断请求是否成功(1000为成功，其他为失败)
           	 Log.e("yifan","onGeocodeSearched");
                if (returnCode == 1000) {
                    if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                            && geocodeResult.getGeocodeAddressList().size() > 0) {
                        GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                        Log.e("yifan", "经纬度值:" + address.getLatLonPoint() + "位置描述:"
                                + address.getFormatAddress());
                        lp = new LatLonPoint(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude());
                        doSearchQuery(address.getCity(),true);
                        Log.e("yifan","onGeocodeSearched->doSearchQuery");
                    }
                }
            }
        });
        
        
        mSearchList= (ListView) findViewById(R.id.list_search);
        mSearchList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	Map<String,Object> map=(Map<String,Object>)mSearchList.getItemAtPosition(arg2);
                String name=(String)map.get("name");
            	mKey.setText(name);
                //通过 GeocodeQuery(java.lang.String locationName, java.lang.String city) 设置查询参数，调用 GeocodeSearch 的 getFromLocationNameAsyn(GeocodeQuery geocodeQuery) 方法发起请求
                GeocodeQuery query = new GeocodeQuery(mKey.getText().toString().trim(), mCurrentCity);
                //发起请求
                mGeocoderSearch.getFromLocationNameAsyn(query);
                Log.e("yifan","getFromLocationNameAsyn");
            	mSearchList.setVisibility(View.GONE);
            	mSearchTag=1;
            }
        });
        
        mParkingIV=(ImageView)findViewById(R.id.iv_parking_list);
        mParkingIV.setOnClickListener(new OnClickListener(){
             @Override
             public void onClick(View v){
            	   if(mContainer.getVisibility()==View.VISIBLE){
            		     if(mLocationToast!=null){
                		     mLocationToast.cancel();
            		     }
            		     if(mToast!=null){
            		    	 mToast.cancel();
            		     }
                	     mContainer.setVisibility(View.GONE);
                	     if(mParkingsList.getAdapter()!=null){
                    	       mRelativeParkingsList.setVisibility(View.VISIBLE);
                               if(mCurrentId==R.id.tv_all_parking_type_search){
                            	   changeSelect(R.id.tv_all_parking_type_parking_list,2);
                                   mCurrentParkingTypeId = R.id.tv_all_parking_type_parking_list;
                               }else if(mCurrentId==R.id.tv_outside_parking_type_search){
                            	   changeSelect(R.id.tv_outside_parking_type_parking_list,2);
                            	   mCurrentParkingTypeId = R.id.tv_outside_parking_type_parking_list;
                               }else if(mCurrentId==R.id.tv_inside_parking_type_search){
                            	   changeSelect(R.id.tv_inside_parking_type_parking_list,2);
                            	   mCurrentParkingTypeId = R.id.tv_inside_parking_type_parking_list;
                               }
                    	       mParkingsList.setVisibility(View.VISIBLE);
                        	   if(mParkingsList.getAdapter().getCount()!=0){
                            	   mParkingsList.setVisibility(View.VISIBLE);
                        		   mEmptyParkingListNotifyTV.setVisibility(View.GONE);
                        		   mNotifyInputLocationTV.setVisibility(View.GONE);
                        	   }else{
                        		   mParkingsList.setVisibility(View.GONE);
                        		   mNotifyInputLocationTV.setVisibility(View.GONE);
                        		   mEmptyParkingListNotifyTV.setVisibility(View.VISIBLE);
                        	   }
                	      }else{
                	    	  mRelativeParkingsList.setVisibility(View.GONE);
                		     mEmptyParkingListNotifyTV.setVisibility(View.GONE);
                		     mNotifyInputLocationTV.setVisibility(View.VISIBLE);
                	      }
            	 }else if(mContainer.getVisibility()==View.GONE){
                	 mContainer.setVisibility(View.VISIBLE);
                     if(mCurrentParkingTypeId == R.id.tv_all_parking_type_parking_list){
                  	     changeSelect(R.id.tv_all_parking_type_search,1);
                         mCurrentId=R.id.tv_all_parking_type_search;
                     }else if(mCurrentParkingTypeId == R.id.tv_outside_parking_type_parking_list){
                  	     changeSelect(R.id.tv_outside_parking_type_search,1);
                  	     mCurrentId=R.id.tv_outside_parking_type_search;
                     }else if(mCurrentParkingTypeId == R.id.tv_inside_parking_type_parking_list){
                  	     changeSelect(R.id.tv_inside_parking_type_search,1);
                  	     mCurrentId=R.id.tv_inside_parking_type_search;
                     }
                	 mRelativeParkingsList.setVisibility(View.GONE);
                	 mNotifyInputLocationTV.setVisibility(View.GONE);
            		 mEmptyParkingListNotifyTV.setVisibility(View.GONE);
            	 }
             }
        });

     	/**
     	 *      搜索栏控件与搜索监听
     	 */
        mKey=(AutoCompleteTextView) findViewById(R.id.ac_search_input);
        OnKeyListener onKeyListener = new OnKeyListener() {
            @Override  
            public boolean onKey(View v, int keyCode, KeyEvent event) {  
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){  
                    /*隐藏软键盘*/  
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
                    if(inputMethodManager.isActive()){  
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);  
                    }   
                    showProgressDialog();
                    //通过 GeocodeQuery(java.lang.String locationName, java.lang.String city) 设置查询参数，调用 GeocodeSearch 的 getFromLocationNameAsyn(GeocodeQuery geocodeQuery) 方法发起请求
                    GeocodeQuery query = new GeocodeQuery(mKey.getText().toString().trim(), "天津");
                    //发起请求
                    mGeocoderSearch.getFromLocationNameAsyn(query);
                    return true;  
                }  
                return false;  
            }  
        };  
        mKey.setOnKeyListener(onKeyListener);  
        mDeleteIV=(ImageView)findViewById(R.id.iv_search_delete);
        mDeleteIV.setOnClickListener(new OnClickListener(){
        	@Override
        	public  void onClick(View v){
        		mKey.setText("");
        	}
        });
        
     	/**
     	 *      搜索栏控件联想监听
     	 */
        class InputtipsListener implements Inputtips.InputtipsListener{
			@Override
			public void onGetInputtips(List<Tip> list, int resultCode) {
				Log.e("yifan","onGetInputtips->resultCode is " + resultCode);
			      if (resultCode == 1000 && mSearchTag==0) {// 正确返回
			    	  mSearchList.setVisibility(View.VISIBLE);
			    	  mDialogParkingDetail.setVisibility(View.GONE);
			            List<Map<String,Object>> searchList=new ArrayList<Map<String, Object>>() ;
			            for (int i=0;i<list.size();i++){
			                Map<String, Object> hashMap=new HashMap<String, Object>();
			                hashMap.put("name",list.get(i).getName());
			                hashMap.put("address",list.get(i).getDistrict());//将地址信息取出放入HashMap中
			                searchList.add(hashMap);//将HashMap放入表中
			            }
			            ParkingSearchAdapter searchAdapter=new ParkingSearchAdapter(getApplicationContext(),searchList);//新建一个适配器
			            mSearchList.setAdapter(searchAdapter);//为listview适配
			            searchAdapter.notifyDataSetChanged();//动态更新listview
			    }
		    }
        };
         mKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if(newText.equals("")){
                	mSearchTag=0;
                	mSearchList.setVisibility(View.GONE);
                	mDialogParkingDetail.setVisibility(View.VISIBLE);
                }else{
                	if(mSearchTag==0){
                        InputtipsQuery inputquery = new InputtipsQuery(newText, mCurrentCity);
                        inputquery.setCityLimit(true);//将获取到的结果进行城市限制筛选
                        Inputtips inputTips = new Inputtips(SearchParkingActivity.this, inputquery);
                        inputTips.setInputtipsListener(new InputtipsListener());
                        inputTips.requestInputtipsAsyn();
                	}
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
         IntentFilter filter = new IntentFilter();  
         filter.addAction("ExitApp");  
         filter.addAction("BackMain");  
         registerReceiver(mReceiver, filter); 
         getActionBar().setDisplayHomeAsUpEnabled(true); 
    }  
         
	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	         case android.R.id.home:  
    		     if(mLocationToast!=null){
        		     mLocationToast.cancel();
    		     }
    		     if(mToast!=null){
    		    	 mToast.cancel();
    		     }
	             finish();  
	             break;    
	        default:  
	             break;  
	    }  
	    return super.onOptionsItemSelected(item);  
	  }  
    
	/**
	 *      切换停车场类型监听
	 */
	private OnClickListener mTabClickListener = new OnClickListener() {
        @Override  
        public void onClick(View v) {  
			if (v.getId() != mCurrentId) {//如果当前选中跟上次选中的一样,不需要处理  
                changeSelect(v.getId(),1);//改变图标跟文字颜色的选中   
                mCurrentId = v.getId();//设置选中id  
                if(mCurrentId==R.id.tv_all_parking_type_search){
                	mParkingType = "150903|150904|150905|150906";
                	doSearchQuery(mCurrentCity,false);
                }else if(mCurrentId==R.id.tv_outside_parking_type_search){
                	mParkingType = "150906";
                	doSearchQuery(mCurrentCity,false);
                }else if(mCurrentId==R.id.tv_inside_parking_type_search){
                	mParkingType = "150903|150904|150905";
                	doSearchQuery(mCurrentCity,false);
                }
            }  
        }  
    };  
    
	/**
	 *      切换停车场类型监听
	 */
	private OnClickListener mParkingTabClickListener = new OnClickListener() {
        @Override  
        public void onClick(View v) {  
			if (v.getId() != mCurrentParkingTypeId) {//如果当前选中跟上次选中的一样,不需要处理  
                changeSelect(v.getId(),2);//改变图标跟文字颜色的选中   
                mCurrentParkingTypeId = v.getId();//设置选中id  
                if(mCurrentParkingTypeId==R.id.tv_all_parking_type_parking_list){
                	mParkingType = "150903|150904|150905|150906";
                	doSearchQuery(mCurrentCity,false);
                }else if(mCurrentParkingTypeId==R.id.tv_outside_parking_type_parking_list){
                	mParkingType = "150906";
                	doSearchQuery(mCurrentCity,false);
                }else if(mCurrentParkingTypeId==R.id.tv_inside_parking_type_parking_list){
                	mParkingType = "150903|150904|150905";
                	doSearchQuery(mCurrentCity,false);
                }
            }  
        }  
    };  
    
	/**
	 *      切换停车场控件颜色切换
	 */
	private void changeSelect(int resId,int dislplayType) {
		if(dislplayType==1){
			mAllParkingTypeTV.setSelected(false);
			mAllParkingTypeTV.setBackgroundResource(R.color.gray);
			mOutsideParkingTypeTV.setSelected(false);
			mOutsideParkingTypeTV.setBackgroundResource(R.color.gray);
			mInsideParkingTypeTV.setSelected(false);
			mInsideParkingTypeTV.setBackgroundResource(R.color.gray);
	        switch (resId) {  
	            case R.id.tv_all_parking_type_search:  
	        	    mAllParkingTypeTV.setSelected(true);  
	        	    mAllParkingTypeTV.setBackgroundResource(R.color.orange);
	                break;  
	            case R.id.tv_outside_parking_type_search:  
	        	    mOutsideParkingTypeTV.setSelected(true);  
	        	    mOutsideParkingTypeTV.setBackgroundResource(R.color.orange);
	                break;
	            case R.id.tv_inside_parking_type_search:  
	        	    mInsideParkingTypeTV.setSelected(true);  
	        	    mInsideParkingTypeTV.setBackgroundResource(R.color.orange);
	                break;  
	        }  
		}else if(dislplayType==2){
			mAllParkingTypeParkingListTV.setSelected(false);
			mAllParkingTypeParkingListTV.setBackgroundResource(R.color.gray);
			mOutsideParkingTypeParkingListTV.setSelected(false);
			mOutsideParkingTypeParkingListTV.setBackgroundResource(R.color.gray);
			mInsideParkingTypeParkingListTV.setSelected(false);
			mInsideParkingTypeParkingListTV.setBackgroundResource(R.color.gray);
	        switch (resId) {  
	            case R.id.tv_all_parking_type_parking_list:  
	            	mAllParkingTypeParkingListTV.setSelected(true);  
	            	mAllParkingTypeParkingListTV.setBackgroundResource(R.color.orange);
	                break;  
	            case R.id.tv_outside_parking_type_parking_list:  
	            	mOutsideParkingTypeParkingListTV.setSelected(true);  
	            	mOutsideParkingTypeParkingListTV.setBackgroundResource(R.color.orange);
	                break;
	            case R.id.tv_inside_parking_type_parking_list:  
	            	mInsideParkingTypeParkingListTV.setSelected(true);  
	            	mInsideParkingTypeParkingListTV.setBackgroundResource(R.color.orange);
	                break;  
	        }  
		}
    }
	
	

	/**
	 *      搜索停车场
	 */
    protected void doSearchQuery(String city, final boolean isSearchType){
    	keyWord = mKey.getText().toString().trim();
    	mQuery = new PoiSearch.Query("", mParkingType , city);//150900
        mPoiSearch = new PoiSearch(SearchParkingActivity.this, mQuery);
        mPoiSearch.setBound(new PoiSearch.SearchBound(lp,500,true));
        mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int errcode) {
                //判断搜索成功
                if (errcode == 1000) {
                    if (null != poiResult/* && poiResult.getPois().size() > 0*/) {
                    	mList.clear();
                        for (int i = 0; i < poiResult.getPois().size(); i++) {
                        	if(i==0){
                        		setPoiItemDisplayContent(poiResult.getPois().get(i));
                        	}
                            Log.e("TAG_MAIN", "POI的名称=" + poiResult.getPois().get(i).getTitle());
                            Log.e("TAG_MAIN", "POI的距离=" + poiResult.getPois().get(i).getDistance());
                            Log.e("TAG_MAIN", "POI的地址=" + poiResult.getPois().get(i).getSnippet());
                            Map<String, Object> map=new HashMap<String, Object>();  
                            map.put("parkingName", poiResult.getPois().get(i).getTitle());
                            map.put("distance", poiResult.getPois().get(i).getDistance());
                            map.put("location", poiResult.getPois().get(i).getAdName() + poiResult.getPois().get(i).getBusinessArea() + poiResult.getPois().get(i).getSnippet());  
                            map.put("parkingNumberTotal", "总车位:" + MAX_LOCATION_SIZE); 
                            mInUseCount = getInUseParkingNumber();
                            map.put("parkingNumberInUse", "占用:" + mInUseCount); 
                            map.put("parkingNumberIdle", "空闲:" + (MAX_LOCATION_SIZE - mInUseCount)); 
                            map.put("latitude", poiResult.getPois().get(i).getLatLonPoint().getLatitude());
                            map.put("longtitude", poiResult.getPois().get(i).getLatLonPoint().getLongitude());
                            mList.add(map);  
                        }
                        mParkingsList.setAdapter(new ParkingListAdapter(mContext, mList));
                    }
                    if(isSearchType){
                    	//将地图移动到定位点
                        mAMAP.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lp.getLatitude(), lp.getLongitude())));
                    }
                    //是否是同一条
                  if(poiResult.getQuery().equals(mQuery)){
                      poiItems = poiResult.getPois();
                      //获取poitem数据
                      List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
                      if(poiItems !=null && poiItems.size()>0) {
                          //清除POI信息
                          //whetherToShowDetailInfo(false);
                          //并还原点击marker样式
                          if (mlastMarker != null) {
                              resetlastmarker();
                          }
                          //清除之前的结果marker样式
                          if (poiOverlay != null) {
                              poiOverlay.removeFromMap();
                          }
                          //新的marker
                          mAMAP.clear();
                          Log.e("yifan","doSearchQuery->clear");
                          poiOverlay = new myPoiOverlay(mAMAP, poiItems);
                          poiOverlay.addToMap();
                          //poiOverlay.zoomToSpan();

                          mAMAP.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                                  .icon(BitmapDescriptorFactory
                                          .fromBitmap(BitmapFactory.decodeResource(
                                                  getResources(), R.drawable.ic_add_location_48px)))
                                  .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
                          dismissProgressDialog();
                      }else if (suggestionCities !=null && suggestionCities.size()>0){
                          if(mToast!=null){
                        	  mToast.cancel();
                          }
                          mToast = Toast.makeText(getApplicationContext(), "showSuggestCity", Toast.LENGTH_LONG);
                          mToast.show();
                          showSuggestCity(suggestionCities);
                      } else {
                          //新的marker
                          mAMAP.clear();
                          poiOverlay = new myPoiOverlay(mAMAP, poiItems);
                          poiOverlay.addToMap();
                          //poiOverlay.zoomToSpan();

                          mAMAP.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                                  .icon(BitmapDescriptorFactory
                                          .fromBitmap(BitmapFactory.decodeResource(
                                                  getResources(), R.drawable.ic_add_location_48px)))
                                  .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
                          dismissProgressDialog();
                          setPoiItemDisplayContent(null);
                          if(mToast!=null){
                        	  mToast.cancel();
                          }
                          mToast = Toast.makeText(getApplicationContext(), "未发现附近停车场", Toast.LENGTH_SHORT);
                          mToast.show();
                      }
                  }else{
                	   Log.e("yifan","query not consistent.");
                  }
              
                }
            }
            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            	//TODO
            }
        });
        mPoiSearch.searchPOIAsyn();
    }
    
	/**
	 *      初始化定位
	 */
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener((AMapLocationListener) this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    
	/**
	 *      定位回调函数
	 */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                mStartLatlng = new LatLonPoint(Double.valueOf(amapLocation.getLatitude()), Double.valueOf(amapLocation.getLongitude()));
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getCity();
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                	mCurrentCity=amapLocation.getCity();
                	mCityTV.setText(mCurrentCity.replace("市", ""));
                    //设置缩放级别
                    mAMAP.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    mAMAP.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    lp = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
                    doSearchQuery(amapLocation.getCity(),true);
                    Log.e("yifan","onLocationChanged->doSearchQuery");
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mLocationListener.onLocationChanged(amapLocation);
                    //添加图钉
                    //aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCity() + ""  + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    if(mLocationToast!=null){
                    	mLocationToast.cancel();
                    }
                    mLocationToast = Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT);
                    mLocationToast.show();
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                if(mToast!=null){
                	mToast.cancel();
                }
                mToast = Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG);
                mToast.show();
            }
        }
    }

	/**
	 *      自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
	 */
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
         //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
       options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add_location_48px));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        //options.snippet("就是这里");
        //设置多少帧刷新一次图片资源
        options.period(60);
        return options;
    }
    
	/**
	 *      激活定位
	 */
    @Override
    public void activate(OnLocationChangedListener listener) {
    	mLocationListener = listener;
    }

	/**
	 *      停止定位
	 */
    @Override
    public void deactivate() {
    	mLocationListener = null;
    }
    
    /**
     * 重新绘制加载地图
     */
    @Override
    protected void onResume() {
       super.onResume();
       mapView.onResume();
    }
    
    /**
     * 暂停地图的绘制
     */
    @Override
    protected void onPause() {
       super.onPause();
       mapView.onPause();
	     if(mLocationToast!=null){
		     mLocationToast.cancel();
	     }
	     if(mToast!=null){
	    	 mToast.cancel();
	     }
    }
    
    /**
     * 保存地图当前的状态方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
       mapView.onSaveInstanceState(outState);
    }
    
    /**
     * 销毁地图、取消注册
     */
    @Override
    protected void onDestroy() {
       super.onDestroy();
       mapView.onDestroy();
       unregisterReceiver(mReceiver);
    }
    
    @Override
    public void onPoiItemSearched(PoiItem arg0,int arg1){
    	// TODO Auto-generated method stub
    }
    
    
    private int[] markers = {
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
            R.drawable.ic_local_parking_32px,
    };
    
    
    private void showSuggestCity(List<SuggestionCity> cities){
        String infomation = "推荐城市\n";
        for(int i = 0;i<cities.size();i++){
            infomation += "城市名称：" + cities.get(i).getCityName() + "城市区号：" + cities.get(i).getCityCode() + "城市编码：" + cities.get(i).getAdCode() + "\n";
        }
        if(mToast!=null){
      	  mToast.cancel();
        }
        mToast = Toast.makeText(getApplicationContext(), infomation, Toast.LENGTH_LONG);
        mToast.show();
    }
    
    
	/**
	 *      将之前点击的marker还原为原来的状态
	 */
  private void resetlastmarker() {
      Log.e("yifan", "resetlastmarker");
      int index = poiOverlay.getPoiIndex(mlastMarker);
      //20个以内的marker显示图标
      mlastMarker.setIcon(BitmapDescriptorFactory
                  .fromBitmap(BitmapFactory.decodeResource(
                          getResources(),
                          markers[index])));
      mlastMarker = null;
  }
  
  
  @Override
  public void onMapClick(LatLng arg0){
      if(mlastMarker!=null){
          resetlastmarker();
      }
  }
  
  
  public boolean onMarkerClick(Marker marker) {
      if (marker.getObject() != null) {
      //显示相关的位置信息
          //whetherToShowDetailInfo(true);
          try {
              PoiItem mCurrentPoi = (PoiItem) marker.getObject();
              if (mlastMarker == null) {
                  mlastMarker = marker;
              } else {
                 //还原原来的marker
                  resetlastmarker();
                  mlastMarker = marker;
              }
              mDetailMarker = marker;
              //按下后的显示图标
              mDetailMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_parking_32px)));
              setPoiItemDisplayContent(mCurrentPoi);
          } catch (Exception e) {

          }
      } else {
          resetlastmarker();
      }
      return true;
  }
  
  
  private void setPoiItemDisplayContent(PoiItem mCurrentPoi){
	  if(mCurrentPoi!=null){
		  mDialogParkingDetail.setVisibility(View.VISIBLE);
		  mDialogParkingNameTV.setText(mCurrentPoi.getTitle());
	      mDialogParkingNumberTotalTV.setText("总数: " + MAX_LOCATION_SIZE + "");
	       mInUseCount = getInUseParkingNumber();
	      mDialogParkingNumberInUseTV.setText("占用: " + getInUseParkingNumber());
	      mDialogParkingNumberFreeTV.setText("空闲:" + (MAX_LOCATION_SIZE - mInUseCount));
		  mCurrentDialogLatitude = mCurrentPoi.getLatLonPoint().getLatitude();
		  mCurrentDialogLongtitude = mCurrentPoi.getLatLonPoint().getLongitude();
	  }else{
		  mDialogParkingDetail.setVisibility(View.GONE);
	  }
  }
  
  @Override
  public View getInfoContents(Marker arg0){
      return null;
  }
  
  @Override
  public View getInfoWindow(Marker arg0){
      return null;
  }
  
  @Override
  public void onInfoWindowClick(Marker arg0){
		// TODO Auto-generated method stub
  }
  
  
	/**
	 *      myPoiOverlay类，该类下面有多个方法
	 */
  private class myPoiOverlay{
      private AMap mamap;
      private List<PoiItem> mPois;
      private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
      //构造函数，传进来的是amap对象和查询到的结果items  mPois
      public myPoiOverlay(AMap amap,List<PoiItem>pois){
          mamap = amap;
          mPois = pois;
      }

  	/**
  	 *      增加Maker到地图中
  	 */
  public void addToMap(){
	  Log.e("yifan","addtomap");
      for(int i=0;i<mPois.size();i++){
          Marker marker = mamap.addMarker(getMarkerOptions(i));
          PoiItem item = mPois.get(i);
          marker.setObject(item);
          mPoiMarks.add(marker);
      }
  }
  
	/**
	 *      移除所有的marker
	 */
  public void removeFromMap(){
      for(Marker mark: mPoiMarks){
          mark.remove();
      }
  }
  
	/**
	 *      移动镜头到当前的视角
	 */
  public void zoomToSpan(){
      if(mPois !=null && mPois.size()>0){
          if(mamap ==null) return;
          LatLngBounds bounds = getLatLngBounds();
         //瞬间移动到目标位置
          mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,100));
      }
  }

  private LatLngBounds getLatLngBounds(){
      LatLngBounds.Builder b = LatLngBounds.builder();
      for(int i=0;i<mPois.size();i++){
          b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),mPois.get(i).getLatLonPoint().getLongitude()));
      }
      return b.build();
  }

  private MarkerOptions getMarkerOptions(int index){
      return new MarkerOptions()
              .position(new LatLng(mPois.get(index).getLatLonPoint()
              .getLatitude(),mPois.get(index)
              .getLatLonPoint().getLongitude()))
              .title(getTitle(index)).snippet(getSnippet(index))
              .icon(getBitmapDescriptor(index));

  }

  protected String getTitle(int index){
      return mPois.get(index).getTitle();
  }
  protected String getSnippet(int index){
      return mPois.get(index).getSnippet();
  }
  
	/**
	 *      获取位置，第几个index就第几个poi
	 */
  public int getPoiIndex(Marker marker){
      for(int i=0;i<mPoiMarks.size();i++){
          if(mPoiMarks.get(i).equals(marker)){
              return i;
          }
      }
      return -1;
  }

  public PoiItem getPoiItem(int index) {
	  if (index < 0 || index >= mPois.size()) {
          return null;
      }
      return mPois.get(index);
  }
  
  protected BitmapDescriptor getBitmapDescriptor(int arg0){
      if(arg0<10){
          BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                  BitmapFactory.decodeResource(getResources(),markers[arg0]));
          return icon;
      }else {
          BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                  BitmapFactory.decodeResource(getResources(),R.drawable.ic_local_parking_32px));
          return icon;
      }
  }
}
  

  
  private Handler mHandler = new Handler() {
      @Override
      public void handleMessage (Message msg) {
          super.handleMessage(msg);
          switch (msg.what) {
              case EVENT_SHOW_DIALOG:
                  break;
              default:
                  break;
          }
      }
  };
        
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
       
       public String sendCityInfo(){//将前面定位数据中的city数据传过来
           String info;//前面定位所在城市信息
           Intent intent=this.getIntent();
           info=intent.getStringExtra("city");
           return info;
       }

    
	/**
	 * 驾驶路线规划
	 */
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
    	// TODO Auto-generated method stub
    }
    
	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (mProgDialog == null)
			mProgDialog = new ProgressDialog(this);
		    mProgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    mProgDialog.setIndeterminate(false);
		    mProgDialog.setCancelable(true);
		    mProgDialog.setMessage("正在搜索");
		    mProgDialog.show();
	    }

	/**
	 * 隐藏进度框
	 */
	private void dismissProgressDialog() {
		if (mProgDialog != null) {
			mProgDialog.dismiss();
		}
	}
	
	/**
	 * 获取正在使用的泊位数量
	 */
    public int getInUseParkingNumber(){
    	mDBAdapter.open();
    	Cursor cursor = mDBAdapter.getParkingByLocationNumber(0, PARKING_NUMBER);
    	int  count = 0;
        try {
          do{
      	      if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
		          count++;
	          }
          }while(cursor.moveToNext());
        }
        catch (Exception e) {
                e.printStackTrace();
        } finally{
            	if(cursor!=null){
            		cursor.close();
                }
        }
    	return count;
    }
    
  @Override
  public void onPoiSearched(PoiResult result, int rcode){
	// TODO Auto-generated method stub
  }
  
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
    	// TODO Auto-generated method stub
    }
    
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
    	// TODO Auto-generated method stub
    }
    
    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
    	// TODO Auto-generated method stub
    }
    
	@Override
	public void onGetInputtips(List<Tip> arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
    }

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
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

}

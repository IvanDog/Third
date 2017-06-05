package com.example.inspector;

import java.io.ByteArrayOutputStream;

import com.example.inspector.IntegratedQueryActivity.NameWatcher;
import com.example.inspector.IntegratedQueryActivity.NumberWatcher;
import com.example.inspector.R.drawable;

import printerdemo.PrinterClass;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InspectActivity extends Activity {
	private TextView mInspectIdTV;
	private TextView mInspectorNameTV;
	private TextView mInspectorNumberTV;
	private Spinner mAdministrativeDivisionSP;
	private Spinner mParkingNameSP;
	private Spinner mParkingNumberSP;
	private EditText mTollCollectorNameET;
	private EditText mTollCollectorNumberET;
	private EditText mPhenomenonDescribeET;
	private Spinner mClauseSP;
	private EditText mPenaltyET;
	private Button mPrintBT;
	private Button mConfirmBT;

	private ImageView mPhotoFirstIV;
	private ImageView mPhotoSecondIV;
	private ImageView mPhotoThirdIV;
	private ImageView mPhotoFourthIV;
	private ImageView mPhotoAddIV;
	
	private String mInspectId;
	private String mInspectorName;
	private String mInspectorNumber;
	private String mAdministrativeDivision;
	private String mParkingName;
	private String mParkingNumber;
	private String mTollCollectionName;
	private String mTollCollectionNumber;
	private String mClause;
	private String mPenalty;
	private Bitmap mPhotoFirst = null;
	private Bitmap mPhotoSecond = null;
	private Bitmap mPhotoThird = null;
	private Bitmap mPhotoFourth = null;
	private PrinterClass mPrinter;
    private boolean mPaperTemState = true;
    private int mRecindex = 0;
    private String mRecviceMessage = "";
    private boolean mConfirmState = false;
    private Context mContext;
	private ProgressDialog mProgDialog = null;// 搜索时进度条
	private TextWatcher mNameWatcher = null;
	private TextWatcher mNumberWatcher = null;
	
	private static final int EVENT_PRINT= 101;
	private static final int EVENT_CONFIRM= 102;
	private static final int EVENT_ERROR_FINISH_INFORMATION= 103;
	private static final int EVENT_ERROR_FINISH_CONFIRM = 104;
	private final static int TAKE_PHOTO =201;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspect);
		mContext = this;
		mInspectIdTV=(TextView)findViewById(R.id.tv_id_inspect);
		mInspectIdTV.setText(R.string.inspect_id_fixed);
		mInspectorNameTV=(TextView)findViewById(R.id.tv_inspector_name_inspect);
		mInspectorNameTV.setText(R.string.inspect_name_fixed);
		mInspectorNumberTV=(TextView)findViewById(R.id.tv_inspector_number_inspect);
		mInspectorNumberTV.setText(R.string.inspect_number_fixed);
		mAdministrativeDivisionSP=(Spinner)findViewById(R.id.sp_administrative_division_inspect);
		mParkingNameSP=(Spinner)findViewById(R.id.sp_parking_name_inspect);
		mParkingNumberSP=(Spinner)findViewById(R.id.sp_parking_number_inspect);
		mParkingNameSP.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
	        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
	                long id) {
				if(position==1){
					mParkingNumberSP.setSelection(1);
				}else if(position==2){
					mParkingNumberSP.setSelection(2);
				}
	        }
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		mParkingNumberSP.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
	        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
	                long id) {
				if(position==1){
					mParkingNameSP.setSelection(1);
				}else if(position==2){
					mParkingNameSP.setSelection(2);
				}
	        }
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mNameWatcher = new NameWatcher();
		mNumberWatcher = new NumberWatcher();
		mTollCollectorNameET=(EditText)findViewById(R.id.et_toll_collector_name_inspect);
		mTollCollectorNumberET=(EditText)findViewById(R.id.et_toll_collector_number_inspect);
		mTollCollectorNameET.addTextChangedListener(mNameWatcher);
    	mTollCollectorNumberET.addTextChangedListener(mNumberWatcher);
		mTollCollectorNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mTollCollectorNameET.addTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.removeTextChangedListener(mNumberWatcher);
                } else {
                	mTollCollectorNameET.removeTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.addTextChangedListener(mNumberWatcher);
                }
            }
        });
		mTollCollectorNumberET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	mTollCollectorNameET.removeTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.addTextChangedListener(mNumberWatcher);
                } else {
                	mTollCollectorNameET.addTextChangedListener(mNameWatcher);
                	mTollCollectorNumberET.removeTextChangedListener(mNumberWatcher);
                }
            }
        });
		mClauseSP=(Spinner)findViewById(R.id.sp_clause_inspect);
		mPenaltyET=(EditText)findViewById(R.id.et_penalty_inspect);
		mPhotoFirstIV = (ImageView)findViewById(R.id.iv_photo_first_inspect);
		mPhotoFirstIV.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
				final AlertDialog dialog = new AlertDialog.Builder(InspectActivity.this).create();
				ImageView img = (ImageView)imgEntryView.findViewById(R.id.iv_large_image);
				Button deleteBT = (Button)imgEntryView.findViewById(R.id.bt_delete_image);
				img.setImageBitmap(mPhotoFirst);
				dialog.setView(imgEntryView); // 自定义dialog
				dialog.show();
				imgEntryView.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				        dialog.cancel();
				    }
			    });
				deleteBT.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				    	mPhotoFirst = null;
				    	mPhotoFirstIV.setImageResource(drawable.ic_photo_background_64px);
				        dialog.cancel();
				    }
			    });
			}
        });
		mPhotoSecondIV = (ImageView)findViewById(R.id.iv_photo_second_inspect);
		mPhotoSecondIV.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
				final AlertDialog dialog = new AlertDialog.Builder(InspectActivity.this).create();
				ImageView img = (ImageView)imgEntryView.findViewById(R.id.iv_large_image);
				Button deleteBT = (Button)imgEntryView.findViewById(R.id.bt_delete_image);
				img.setImageBitmap(mPhotoSecond);
				dialog.setView(imgEntryView); // 自定义dialog
				dialog.show();
				imgEntryView.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				        dialog.cancel();
				    }
			    });
				deleteBT.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				    	mPhotoSecond = null;
				    	mPhotoSecondIV.setImageResource(drawable.ic_photo_background_64px);
				        dialog.cancel();
				    }
			    });
			}
        });
		mPhotoThirdIV = (ImageView)findViewById(R.id.iv_photo_third_inspect);
		mPhotoThirdIV.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
				final AlertDialog dialog = new AlertDialog.Builder(InspectActivity.this).create();
				ImageView img = (ImageView)imgEntryView.findViewById(R.id.iv_large_image);
				Button deleteBT = (Button)imgEntryView.findViewById(R.id.bt_delete_image);
				img.setImageBitmap(mPhotoThird);
				dialog.setView(imgEntryView); // 自定义dialog
				dialog.show();
				imgEntryView.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				        dialog.cancel();
				    }
			    });
				deleteBT.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				    	mPhotoThird = null;
				    	mPhotoThirdIV.setImageResource(drawable.ic_photo_background_64px);
				        dialog.cancel();
				    }
			    });
			}
        });
		mPhotoFourthIV = (ImageView)findViewById(R.id.iv_photo_fourth_inspect);
		mPhotoFourthIV.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
				final AlertDialog dialog = new AlertDialog.Builder(InspectActivity.this).create();
				ImageView img = (ImageView)imgEntryView.findViewById(R.id.iv_large_image);
				Button deleteBT = (Button)imgEntryView.findViewById(R.id.bt_delete_image);
				img.setImageBitmap(mPhotoFourth);
				dialog.setView(imgEntryView); // 自定义dialog
				dialog.show();
				imgEntryView.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				        dialog.cancel();
				    }
			    });
				deleteBT.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				    	mPhotoFourth = null;
				    	mPhotoFourthIV.setImageResource(drawable.ic_photo_background_64px);
				        dialog.cancel();
				    }
			    });
			}
        });
		mPhotoAddIV = (ImageView)findViewById(R.id.iv_photo_add_inspect);
		mPhotoAddIV.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				if(mPhotoFirst!=null && mPhotoSecond!=null && mPhotoThird!=null && mPhotoFourth!=null){
					Toast.makeText(getApplicationContext(), "最多可添加4张图片",Toast.LENGTH_SHORT).show();
				}else{
					openTakePhoto();
				}
			}
        });
		mPrintBT=(Button)findViewById(R.id.bt_print_inspect);
		mPrintBT.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	if(mConfirmState){
		    		mInspectId = mInspectIdTV.getText().toString();
			    	mInspectorName = mInspectorNameTV.getText().toString();
			    	mInspectorNumber = mInspectorNumberTV.getText().toString();
		        	mAdministrativeDivision = mAdministrativeDivisionSP.getSelectedItem().toString();
		        	if(mAdministrativeDivision.equals("请选择行政区域")){
		        		mAdministrativeDivision = null;
		        	}
		        	mParkingName = mParkingNameSP.getSelectedItem().toString();
		        	if(mParkingName.equals("请选择车场名称")){
		        		mParkingName=null;
		        	}
		        	mParkingNumber = mParkingNumberSP.getSelectedItem().toString();
		        	if(mParkingNumber.equals("请选择车场编号")){
		        		mParkingNumber=null;
		        	}
		        	if((mTollCollectorNameET.getText().toString()).equals("")){
			        	mTollCollectionName = null;
		        	}else{
			        	mTollCollectionName = mTollCollectorNameET.getText().toString();
		        	}
		        	if((mTollCollectorNumberET.getText().toString()).equals("")){
		        		mTollCollectionNumber = null;
		        	}else{
			        	mTollCollectionNumber = mTollCollectorNumberET.getText().toString();
		        	}
		        	mClause = mClauseSP.getSelectedItem().toString();
		        	if(mClause.equals("请选择监督条例")){
		        		mClause=null;
		        	}
		        	if((mPenaltyET.getText().toString()).equals("")){
		        		mPenalty = null;
		        	}else{
			        	mPenalty = mPenaltyET.getText().toString();
		        	}
		        	if((mAdministrativeDivision==null) || (mParkingName==null) || (mParkingNumber==null)
		        			||(mTollCollectionName==null) || (mTollCollectionNumber==null)
		        			|| (mClause==null) || (mPenalty==null)){
						Message msg = new Message();
		                msg.what = EVENT_ERROR_FINISH_INFORMATION;
		                mHandler.sendMessage(msg);
		        	}else{
						Message msg = new Message();
		                msg.what = EVENT_PRINT;
		                mHandler.sendMessage(msg);
		        	}
		    	}else{
					Message msg = new Message();
	                msg.what = EVENT_ERROR_FINISH_CONFIRM;
	                mHandler.sendMessage(msg);
		    	}
		    }
		});
		mConfirmBT=(Button)findViewById(R.id.bt_confirm_inspect);
		mConfirmBT.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	mInspectId = mInspectIdTV.getText().toString();
		    	mInspectorName = mInspectorNameTV.getText().toString();
		    	mInspectorNumber = mInspectorNumberTV.getText().toString();
	        	mAdministrativeDivision = mAdministrativeDivisionSP.getSelectedItem().toString();
	        	if(mAdministrativeDivision.equals("请选择行政区域")){
	        		mAdministrativeDivision = null;
	        	}
	        	mParkingName = mParkingNameSP.getSelectedItem().toString();
	        	if(mParkingName.equals("请选择车场名称")){
	        		mParkingName=null;
	        	}
	        	mParkingNumber = mParkingNumberSP.getSelectedItem().toString();
	        	if(mParkingNumber.equals("请选择车场编号")){
	        		mParkingNumber=null;
	        	}
	        	if((mTollCollectorNameET.getText().toString()).equals("")){
		        	mTollCollectionName = null;
	        	}else{
		        	mTollCollectionName = mTollCollectorNameET.getText().toString();
	        	}
	        	if((mTollCollectorNumberET.getText().toString()).equals("")){
	        		mTollCollectionNumber = null;
	        	}else{
		        	mTollCollectionNumber = mTollCollectorNumberET.getText().toString();
	        	}
	        	mClause = mClauseSP.getSelectedItem().toString();
	        	if(mClause.equals("请选择监督条例")){
	        		mClause=null;
	        	}
	        	if((mPenaltyET.getText().toString()).equals("")){
	        		mPenalty = null;
	        	}else{
		        	mPenalty = mPenaltyET.getText().toString();
	        	}
	        	Log.e("gouyifan","Penalty is " + mPenalty);
	        	if((mAdministrativeDivision==null) || (mParkingName==null) || (mParkingNumber==null)
	        			||(mTollCollectionName==null) || (mTollCollectionNumber==null)  || (mClause==null) || (mPenalty==null)){
					Message msg = new Message();
	                msg.what = EVENT_ERROR_FINISH_INFORMATION;
	                mHandler.sendMessage(msg);
	        	}else{
					Message msg = new Message();
	                msg.what = EVENT_CONFIRM;
	                mHandler.sendMessage(msg);
	        	}
		    }
		});
	   /*mPrinter = new PrinterClass();
	   mPrinter.setPrinterResponseMessageListener(new PrinterClass.PrinterResponseMessageListener() {
            public void response(byte[] RecMessage) {
                if(mRecindex ==1){
                	mRecviceMessage =mPrinter.bytesToHex(RecMessage,0,RecMessage.length);
                }else if(mRecindex ==2){
                	//TODO
                }
            }
        });*/
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
        getActionBar().setDisplayHomeAsUpEnabled(true); 
	}
	
	public class NameWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	// TODO Auto-generated method stub
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        	if(mTollCollectorNameET.getText()!=null && mTollCollectorNameET.getText().toString().equals("peter")){
        		mTollCollectorNumberET.setText("101884");
        	}
        }
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}
	}
	
	public class NumberWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	// TODO Auto-generated method stub
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        	if( mTollCollectorNumberET.getText()!=null && mTollCollectorNumberET.getText().toString().equals("101884")){
        		mTollCollectorNameET.setText("peter");
        	}
        }
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
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
    
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_PRINT:
                	final StringBuffer sb = new StringBuffer();
                	sb.append(mInspectId)
                	.append("\n").append(mInspectorName).append("  ").append(mInspectorNumber)
                	.append("\n").append("行政区域: " + mAdministrativeDivision)
                	.append("\n").append("停车场名称:" + mParkingName)
                	.append("\n").append("停车场编号:" + mParkingNumber)
                	.append("\n").append("收费员: " + mTollCollectionName).append("  ").append("工号: " + mTollCollectionNumber)
                	.append("\n").append("处罚依据:" + mClause)
                	.append("\n").append("罚款金额:" + mPenalty)
                	.append("\n").append(mContext.getString(R.string.supervise_telephone_fixed) + "");
    				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
    				View printView = inflater.inflate(R.layout.dialog_print_preview, null); // 加载自定义的布局文件
    				final AlertDialog dialog = new AlertDialog.Builder(InspectActivity.this).create();
    				TextView printTV = (TextView)printView.findViewById(R.id.tv_print_preview_dialog);
    				printTV.setText(sb.toString());
    				Button printBT = (Button)printView.findViewById(R.id.bt_print_dialog);
    				dialog.setView(printView); // 自定义dialog
    				dialog.show();
    				printBT.setOnClickListener(new OnClickListener() {
    				    public void onClick(View paramView) {
    	                    //mPrinter.printer_uart_on();
    	                    //mPrinter.send(sb.toString());
    	                	Toast.makeText(getApplicationContext(), "该设备不支持打印功能", Toast.LENGTH_SHORT).show();
    	                	//Toast.makeText(getApplicationContext(), "打印成功", Toast.LENGTH_SHORT).show();
    				        dialog.cancel();
    				    }
    			    });
                	break;
                case EVENT_CONFIRM:
                	Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                	mAdministrativeDivisionSP.setEnabled(false);
                	mParkingNameSP.setEnabled(false);
                	mParkingNumberSP.setEnabled(false);
                	mTollCollectorNameET.setEnabled(false);
                	mTollCollectorNumberET.setEnabled(false);
                	mClauseSP.setEnabled(false);
                	mPenaltyET.setEnabled(false);
                	mConfirmState = true;
                	break;
                case EVENT_ERROR_FINISH_INFORMATION:
                	Toast.makeText(getApplicationContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                	break;
                case EVENT_ERROR_FINISH_CONFIRM:
                	Toast.makeText(getApplicationContext(), "请提交后再打印信息", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };
    
    private int doGetData(){
        SharedPreferences settings = getSharedPreferences("settings", BIND_AUTO_CREATE);
        return settings.getInt("data",9600);
    }
    
    private void openTakePhoto(){
		 /**
		 * 在启动拍照之前最好先判断一下sdcard是否可用
		 */
		     String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
		     if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
		          Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		          startActivityForResult(intent,TAKE_PHOTO);
		     }else {
		          Toast.makeText(InspectActivity.this,"sdcard不可用",Toast.LENGTH_SHORT).show();
		     }
		}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	       super.onActivityResult(requestCode, resultCode, data);
	       if (data!= null) {
	           switch (requestCode) {
	               case TAKE_PHOTO: //拍摄图片并选择
	               if (data.getData() != null|| data.getExtras() != null){ //防止没有返回结果
	                   Uri uri =data.getData();
	                   if (uri != null) {
	                	   if(mPhotoFirst==null){
		                	   mPhotoFirst =BitmapFactory.decodeFile(uri.getPath()); //拿到图片
	                	   }else if(mPhotoSecond==null){
	                		   mPhotoSecond =BitmapFactory.decodeFile(uri.getPath()); //拿到图片
	                	   }else if(mPhotoThird==null){
	                		   mPhotoThird =BitmapFactory.decodeFile(uri.getPath()); //拿到图片
	                	   }else if(mPhotoFourth==null){
	                		   mPhotoFourth =BitmapFactory.decodeFile(uri.getPath()); //拿到图片
	                	   }
	                   }
	                   Bundle bundle =data.getExtras();
	                   if (bundle != null){
	    	               if (mPhotoFirst == null) {
	                    	   mPhotoFirst =(Bitmap) bundle.get("data");
	   	                    }else 	if (mPhotoSecond == null) {
	   	                    	mPhotoSecond =(Bitmap) bundle.get("data");
		   	                }else if (mPhotoThird == null) {
		   	                	mPhotoThird =(Bitmap) bundle.get("data");
		   	                }else if (mPhotoFourth == null) {
		   	                	mPhotoFourth =(Bitmap) bundle.get("data");
		   	                }
	                   } 
	               }
	               if(mPhotoFirst!=null){
		               mPhotoFirstIV.setImageBitmap(mPhotoFirst);
	               }
	               if(mPhotoSecond!=null){
		               mPhotoSecondIV.setImageBitmap(mPhotoSecond);
	               }
	               if(mPhotoThird!=null){
		               mPhotoThirdIV.setImageBitmap(mPhotoThird);
	               }
	               if(mPhotoFourth!=null){
		               mPhotoFourthIV.setImageBitmap(mPhotoFourth);
	               }
	               break;
	          }
	      }
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
		    mProgDialog.setMessage("正在查询");
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
}

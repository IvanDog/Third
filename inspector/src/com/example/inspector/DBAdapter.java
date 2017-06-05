package com.example.inspector;

import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.SQLException;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.util.Log;  
  
public class DBAdapter {  
  
    static final String KEY_ROWID = "_id";  
    static final String KEY_ADMINISTRATIVE_DIVISION = "administrativedivision";
    static final String KEY_PARKING_NAME = "parkingname";
    static final String KEY_PARKING_NUMBER = "parkingnumber";
    static final String KEY_TOLL_COLLECTOR_NAME = "toolcollectorname";
    static final String KEY_TOLL_COLLECTOR_NUMBER = "toolcollectornumber";
    static final String KEY_LICENSE_PLATE = "licenseplate";  
    static final String KEY_CAR_TYPE = "cartype";
    static final String KEY_PARKING_TYPE="parkingtype";
    static final String KEY_LOCATION_NUMBER="locationnumber";
    static final String KEY_START_TIME="starttime";
    static final String KEY_LEAVE_TIME="leavetime";
    static final String KEY_EXPENSE="expense";
    static final String KEY_PAYMENT_PATTERN="paymentpattern";
    static final String TAG = "DBAdapter";  
      
    static final String DATABASE_NAME = "inspectorDB";  
    static final String DATABASE_PARKING_TABLE = "parkings";  
    static final int DATABASE_VERSION = 1;  
      
    static final String DATABASE_PARKING_CREATE =   
           "create table parkings( _id integer primary key autoincrement, " +   
           "administrativedivision varchar(20), parkingname varchar(20), parkingnumber varchar(20), " +
           "toolcollectorname varchar(20), toolcollectornumber varchar(20), " +
           "licenseplate varchar(20), cartype varchar(20), parkingtype varchar(20), " +
           "locationnumber integer,starttime varchar(50),leavetime  varchar(50),expense  integer, " +
           "paymentpattern varchar(20));";  
    final Context context;  
      
    DatabaseHelper DBHelper;  
    SQLiteDatabase db;  
      
    public DBAdapter(Context cxt)  
    {  
        this.context = cxt;  
        DBHelper = new DatabaseHelper(context);  
    }  
      
    private static class DatabaseHelper extends SQLiteOpenHelper  
    {  
  
        DatabaseHelper(Context context)  
        {  
            super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        }  
        @Override  
        public void onCreate(SQLiteDatabase db) {   
            try  
            {  
                db.execSQL(DATABASE_PARKING_CREATE);  
                android.util.Log.d("yifan","DATABASE_CREATE" );
            }  
            catch(SQLException e)  
            {  
                e.printStackTrace();  
            }  
        }  
  
        @Override  
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
            Log.wtf(TAG, "Upgrading database from version "+ oldVersion + "to "+  
             newVersion + ", which will destroy all old data");  
            db.execSQL("DROP TABLE IF EXISTS parkings");  
            onCreate(db);  
        }  
    }  
      
    //open the database  
    public DBAdapter open() throws SQLException  
    {  
        db = DBHelper.getWritableDatabase();  
        return this;  
    }

    //close the database  
    public void close()  
    {  
        DBHelper.close();  
    }  

    //insert parking information  into the database  
    public long insertParking(String administrativeDivision, String parkingName, String parkingNumber, 
    		String toolCollectorName, String toolCollectorNumber, 
    		String licensePlate, String carType, String parkingType, int locationNumber,
    		String startTime,String leaveTime, int expense, String paymentPattern)  
    {  
        ContentValues initialValues = new ContentValues();  
        initialValues.put(KEY_ADMINISTRATIVE_DIVISION, administrativeDivision);  
        initialValues.put(KEY_PARKING_NAME, parkingName);
        initialValues.put(KEY_PARKING_NUMBER, parkingNumber);
        initialValues.put(KEY_TOLL_COLLECTOR_NAME, toolCollectorName);
        initialValues.put(KEY_TOLL_COLLECTOR_NUMBER, toolCollectorNumber);
        initialValues.put(KEY_LICENSE_PLATE, licensePlate);  
        initialValues.put(KEY_CAR_TYPE, carType);
        initialValues.put(KEY_PARKING_TYPE, parkingType);
        initialValues.put(KEY_LOCATION_NUMBER, locationNumber);
        initialValues.put(KEY_START_TIME, startTime);
        initialValues.put(KEY_LEAVE_TIME, leaveTime);
        initialValues.put(KEY_EXPENSE, expense);
        initialValues.put(KEY_PAYMENT_PATTERN, paymentPattern);
        return db.insert(DATABASE_PARKING_TABLE, null, initialValues);  
    }

    //delete a particular parking information
    public boolean deleteParking(long rowId)  
    {  
        return db.delete(DATABASE_PARKING_TABLE, KEY_ROWID + "=" +rowId, null) > 0;  
    }
    
    //delete all parking information
    public boolean deleteParking()  
    {  
        return db.delete(DATABASE_PARKING_TABLE, null, null) > 0;  
    }

    //get all the parking information 
    public Cursor getAllParkings()  
    {          
    	Cursor mCursor =  
         db.query(DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID, KEY_ADMINISTRATIVE_DIVISION,
        		KEY_PARKING_NAME, KEY_PARKING_NUMBER, KEY_TOLL_COLLECTOR_NAME, KEY_TOLL_COLLECTOR_NUMBER,
        		KEY_LICENSE_PLATE,KEY_CAR_TYPE, KEY_PARKING_TYPE, KEY_LOCATION_NUMBER, KEY_START_TIME, KEY_LEAVE_TIME,
        		KEY_EXPENSE, KEY_PAYMENT_PATTERN }, null, null, null, null, null); 
        if (mCursor != null)  
            mCursor.moveToFirst();  
        return mCursor;  
    }

    //get a particular parking  information
    public Cursor getParkingById(long rowId) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID, KEY_ADMINISTRATIVE_DIVISION,
                		KEY_PARKING_NAME, KEY_PARKING_NUMBER, KEY_TOLL_COLLECTOR_NAME, KEY_TOLL_COLLECTOR_NUMBER,
                		KEY_LICENSE_PLATE,KEY_CAR_TYPE, KEY_PARKING_TYPE, KEY_LOCATION_NUMBER, KEY_START_TIME, KEY_LEAVE_TIME,
                		KEY_EXPENSE, KEY_PAYMENT_PATTERN }, KEY_ROWID + "=" + rowId, null, null, null, null, null);  
        if (mCursor != null)  
            mCursor.moveToFirst();  
        return mCursor;  
    }

    //get a particular parking  information
    public Cursor getParking(String administrativeDivision, String parkingName, String parkingNumber, 
    		String toolCollectorName, String toolCollectorNumber, 
    		String licensePlate, String carType, String parkingType, int locationNumber,
    		String startTime,String leaveTime, String paymentPattern, int expense) throws SQLException  
    {  
    	String administrativeDivisionQueryStatement = "";
    	String parkingNameQueryStatement = "";
    	String parkingNumberQueryStatement = "";
    	String toolCollectorNameQueryStatement = "";
    	String toolCollectorNumberQueryStatement = "";
    	String licensePlateQueryStatement = "";
    	String carTypeQueryStatement = "";
    	String parkingTypeQueryStatement = "";
    	String locationNumberQueryStatement = "";
    	String startTimeQueryStatement = "";
    	String leaveTimeQueryStatement = "";
    	String expenseQueryStatement = "";
    	String paymentPatternQueryStatement = "";
    	if(administrativeDivision!=null){
    		administrativeDivisionQueryStatement = KEY_ADMINISTRATIVE_DIVISION + "="  +  "\""  + administrativeDivision +  "\"" ;
    	}else{
    		administrativeDivisionQueryStatement = KEY_ADMINISTRATIVE_DIVISION + "!="  + "\""  + "" +  "\"";
    	}
    	if(parkingName!=null){
    		parkingNameQueryStatement =  KEY_PARKING_NAME + "=" +  "\"" + parkingName +  "\"";
    	}else{
    		parkingNameQueryStatement =  KEY_PARKING_NAME + "!=" + "\"" + "" +  "\"";
    	}
    	if(parkingNumber!=null){
    		parkingNumberQueryStatement = KEY_PARKING_NUMBER + "="+  "\"" + parkingNumber +  "\"";
    	}else{
    		parkingNumberQueryStatement =  KEY_PARKING_NAME + "!=" + "\"" + "" +  "\"";
    	}
    	if(toolCollectorName!=null){
    		toolCollectorNameQueryStatement = KEY_TOLL_COLLECTOR_NAME + "="+  "\"" + toolCollectorName +  "\"";
    	}else{
    		toolCollectorName = null;
    		toolCollectorNameQueryStatement =  KEY_TOLL_COLLECTOR_NAME + "!=" + "\"" + "" +  "\"";
    	}
    	if(toolCollectorNumber!=null){
    		toolCollectorNumberQueryStatement = KEY_TOLL_COLLECTOR_NUMBER + "="+  "\"" + toolCollectorNumber +  "\"";
    	}else{
    		toolCollectorNumber = null;
    		toolCollectorNumberQueryStatement =  KEY_TOLL_COLLECTOR_NUMBER + "!=" + "\"" + "" +  "\"";
    	}
    	if(licensePlate!=null){
    		licensePlateQueryStatement = KEY_LICENSE_PLATE + "="+  "\"" + licensePlate +  "\"";
    	}else{
    		licensePlate = null;
    		licensePlateQueryStatement =  KEY_LICENSE_PLATE + "!=" + "\"" + "" +  "\"";
    	}
    	if(carType!=null){
    		carTypeQueryStatement = KEY_CAR_TYPE + "="+  "\"" + carType +  "\"";
    	}else{
    		carTypeQueryStatement =  KEY_CAR_TYPE + "!=" + "\"" + "" +  "\"";
    	}
    	if(parkingType!=null){
    		parkingTypeQueryStatement = KEY_PARKING_TYPE + "="+  "\"" + parkingType +  "\"";
    	}else{
    		parkingTypeQueryStatement =  KEY_PARKING_TYPE + "!=" +  "\"" + "" +  "\"";
    	}
    	if(locationNumber!=0){
    		locationNumberQueryStatement = KEY_LOCATION_NUMBER + "=" + "\"" + locationNumber + "\"";
    	}else{
    		locationNumberQueryStatement = KEY_LOCATION_NUMBER + ">=" + "\"" + 0 + "\"";
    		//locationNumberQueryStatement =  "\"" + locationNumber + "\"" + "=" + "\"" + locationNumber + "\"";
    	}
    	if(startTime!=null){
    		startTimeQueryStatement = KEY_START_TIME + " LIKE " + "\"" +startTime + "\"";
    	}else{
    		startTimeQueryStatement =  KEY_START_TIME + "!=" + "\"" +"" + "\"";
    	}
    	if(leaveTime!=null){
    		leaveTimeQueryStatement = KEY_LEAVE_TIME + " LIKE " + "\"" +leaveTime + "\"";
    	}else{
    		leaveTimeQueryStatement =  KEY_LEAVE_TIME + "!=" + "\"" +"" + "\"";
    	}
    	if(expense!=-1){
    		expenseQueryStatement = KEY_EXPENSE + "=" + "\"" + expense + "\"";
    	}else{
    		expenseQueryStatement =  KEY_EXPENSE + ">=" + "\"" + 0 + "\"";
    	}
    	if(paymentPattern!=null){
    		paymentPatternQueryStatement = KEY_PAYMENT_PATTERN + "="+  "\"" + paymentPattern +  "\"";
    	}else{
    		paymentPatternQueryStatement =  KEY_PAYMENT_PATTERN + "!=" + "\"" + "" +  "\"";
    	}
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID, KEY_ADMINISTRATIVE_DIVISION,
                		KEY_PARKING_NAME, KEY_PARKING_NUMBER, KEY_TOLL_COLLECTOR_NAME, KEY_TOLL_COLLECTOR_NUMBER,
                		KEY_LICENSE_PLATE,KEY_CAR_TYPE, KEY_PARKING_TYPE, KEY_LOCATION_NUMBER, KEY_START_TIME, KEY_LEAVE_TIME,
                		KEY_EXPENSE, KEY_PAYMENT_PATTERN }, 
                		administrativeDivisionQueryStatement +
                		" AND "  + parkingNameQueryStatement +
                		" AND " + parkingNumberQueryStatement +
                		" AND " + toolCollectorNameQueryStatement +
                		" AND " + toolCollectorNumberQueryStatement +
                		" AND " + licensePlateQueryStatement +
                		" AND " + carTypeQueryStatement +
                		" AND " + parkingTypeQueryStatement +
                		" AND " + locationNumberQueryStatement + 
                		" AND " + startTimeQueryStatement +
                		" AND " + expenseQueryStatement, null, null, null, KEY_ROWID + " DESC", null);
/*    	Cursor mCursor = 
    	db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID, KEY_ADMINISTRATIVE_DIVISION,
        		KEY_PARKING_NAME, KEY_PARKING_NUMBER, KEY_TOLL_COLLECTOR_NAME, KEY_TOLL_COLLECTOR_NUMBER,
        		KEY_LICENSE_PLATE,KEY_CAR_TYPE, KEY_PARKING_TYPE, KEY_LOCATION_NUMBER, KEY_START_TIME, KEY_LEAVE_TIME,
        		KEY_EXPENSE, KEY_PAYMENT_PATTERN }, null, null, null, null, KEY_ROWID + " DESC", null); */ 
        if (mCursor != null)  
            mCursor.moveToFirst();  
        return mCursor;  
    }
    //get a particular parking  information
    public Cursor getParkingByLocationNumber(int locationNumber,String parkingNumber) throws SQLException  
    {  
    	String parkingNumberQueryStatement = "";
    	String locationNumberQueryStatement = "";
    	if(parkingNumber!=null){
    		parkingNumberQueryStatement = KEY_PARKING_NUMBER + "="+  "\"" + parkingNumber +  "\"";
    	}else{
    		parkingNumberQueryStatement =  KEY_PARKING_NUMBER + "!=" + "\"" + "" +  "\"";
    	}
    	if(locationNumber!=0){
    		locationNumberQueryStatement = KEY_LOCATION_NUMBER + "=" + "\"" + locationNumber + "\"";
    	}else{
    		locationNumberQueryStatement = KEY_LOCATION_NUMBER + ">=" + "\"" + 0 + "\"";
    	}
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID, KEY_ADMINISTRATIVE_DIVISION,
                		KEY_PARKING_NAME, KEY_PARKING_NUMBER, KEY_TOLL_COLLECTOR_NAME, KEY_TOLL_COLLECTOR_NUMBER,
                		KEY_LICENSE_PLATE,KEY_CAR_TYPE, KEY_PARKING_TYPE, KEY_LOCATION_NUMBER, KEY_START_TIME, KEY_LEAVE_TIME,
                		KEY_EXPENSE, KEY_PAYMENT_PATTERN}, 
                		parkingNumberQueryStatement + " AND " + locationNumberQueryStatement, null, null, null, KEY_ROWID + " DESC", null);  
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;  
    }

    //updates a parking information  
    public boolean updateParking(long rowId, String leaveTime, int expense, String paymentPattern)  
    {  
        ContentValues values = new ContentValues();  
        values.put(KEY_LEAVE_TIME, leaveTime);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_PAYMENT_PATTERN, paymentPattern);
        return db.update(DATABASE_PARKING_TABLE, values, KEY_ROWID + "=" +rowId, null) > 0;  
    }
    
    //updates a parking information  
    public boolean updateUnfinishedParking(String leaveTime, int expense, String paymentPattern)  
    {  
        ContentValues values = new ContentValues();  
        values.put(KEY_LEAVE_TIME, leaveTime);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_PAYMENT_PATTERN, paymentPattern);
        return db.update(DATABASE_PARKING_TABLE, values, KEY_EXPENSE + " = " +  0, null) > 0;  
    }
    
    /*//get a particular parking  information
    public Cursor getParkingByLicensePlate(String licenseNumber) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID,KEY_LICENSE_PLATE,KEY_CAR_TYPE,
                		KEY_PARKING_TYPE,KEY_LOCATION_NUMBER,KEY_START_TIME,KEY_LEAVE_TIME,
                		KEY_EXPENSE,KEY_PAYMENT_PATTERN,KEY_ARRIVING_IMAGE},KEY_LICENSE_PLATE + " = " + "\"" +licenseNumber + "\"", null, null, null, KEY_ROWID + " DESC", null);  
        if (mCursor != null){
        	mCursor.moveToFirst();  	
        }
        return mCursor;  
    }

    //get a particular parking  information
    public Cursor getParkingByStartTime(String time) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID,KEY_LICENSE_PLATE,KEY_CAR_TYPE,
                		KEY_PARKING_TYPE,KEY_LOCATION_NUMBER,KEY_START_TIME,KEY_LEAVE_TIME,
                		KEY_EXPENSE,KEY_PAYMENT_PATTERN,KEY_ARRIVING_IMAGE},KEY_START_TIME + " LIKE " + "\"" +time + "\"", null, null, null, KEY_ROWID + " DESC", null);  
        if (mCursor != null){
        	mCursor.moveToFirst();  	
        }
        return mCursor;  
    }

    //updates a parking information  
    public boolean updateParking(long rowId, String licensePlate, String carType, String parkingType, int locationNumber,String startTime,
    		String leaveTime, String expense, String paymentPattern,byte[] arrivingImage)  
    {  
        ContentValues values = new ContentValues();  
        values.put(KEY_LICENSE_PLATE, licensePlate);  
        values.put(KEY_CAR_TYPE, carType);
        values.put(KEY_PARKING_TYPE, parkingType);
        values.put(KEY_LOCATION_NUMBER, locationNumber);
        values.put(KEY_START_TIME, startTime);
        values.put(KEY_LEAVE_TIME, leaveTime);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_PAYMENT_PATTERN, paymentPattern);
        values.put(KEY_ARRIVING_IMAGE, arrivingImage);
        return db.update(DATABASE_PARKING_TABLE, values, KEY_ROWID + "=" +rowId, null) > 0;  
    }  

    //updates a parking information  
    public boolean updateParking(long rowId, String leaveTime, String expense, String paymentPattern)  
    {  
        ContentValues values = new ContentValues();  
        values.put(KEY_LEAVE_TIME, leaveTime);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_PAYMENT_PATTERN, paymentPattern);
        return db.update(DATABASE_PARKING_TABLE, values, KEY_ROWID + "=" +rowId, null) > 0;  
    } */
} 
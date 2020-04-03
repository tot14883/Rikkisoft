package com.rikkeisoft.rikkonbi.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class myDBHelper implements MainActivityPresenter.View.ConnectedWifi{
    private final String TAG = "myDBHelper";
    DBHelper myhelper;
    private boolean mCheck;
    private PresenterCheckWiFi mCheckWiFi;

    public myDBHelper(Context context){
        myhelper = new DBHelper(context);
        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();
    }

    public long insertData(int mid ,String title, String content ,String desciption ,String isRead , String createdOn){
        long id = 0;
        try {
            SQLiteDatabase dbb = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.UID, mid);
            contentValues.put(DBHelper.TITLE, title);
            contentValues.put(DBHelper.CONTENT, content);
            contentValues.put(DBHelper.DESCRIPTION, desciption);
            contentValues.put(DBHelper.ISREAD, isRead);
            contentValues.put(DBHelper.CREATEDON, createdOn);
            id = dbb.insert(DBHelper.TABLE_NAME, null, contentValues);
        }catch (Exception e){
            //CHECK error
        }
        return id;
    }

    //แก้
    public List<String> getData(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {DBHelper.UID,DBHelper.TITLE,DBHelper.CONTENT,DBHelper.DESCRIPTION,DBHelper.ISREAD,DBHelper.CREATEDON};
        Cursor cursor =db.query(DBHelper.TABLE_NAME,columns,null,null,null,null,DBHelper.UID+" DESC");
        ArrayList notification = new ArrayList();
        notification.clear();
        Gson gson =new Gson();

        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(DBHelper.UID));
            String title =cursor.getString(cursor.getColumnIndex(DBHelper.TITLE));
            String content =cursor.getString(cursor.getColumnIndex(DBHelper.CONTENT));
            String description =cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION));
            String isRead =cursor.getString(cursor.getColumnIndex(DBHelper.ISREAD));
            String createdOn =cursor.getString(cursor.getColumnIndex(DBHelper.CREATEDON));

            try {
                notification.add(dataSend(cid,title,content,description,isRead,createdOn));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<String> responseData = new ArrayList<String>();
        responseData.clear();
        for(int i = 0;i < notification.size();i++){
            responseData.add(gson.toJson(notification.get(i)));
        }
        return responseData;
    }

    public int countRows(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {DBHelper.UID,DBHelper.TITLE,DBHelper.CONTENT,DBHelper.DESCRIPTION,DBHelper.ISREAD,DBHelper.CREATEDON};
        Cursor cursor =db.query(DBHelper.TABLE_NAME,columns,null,null,null,null,null);
        int count= cursor.getCount();
        return count;
    }
    public JsonObject dataSend(int uid , String title ,String content ,String description,String isRead,String createdOn) throws JSONException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", uid);
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("createdOn", createdOn);
        jsonObject.addProperty("isRead",isRead);

        return jsonObject;
    }

    public int delete(String id) {
        int count=0;
        checkConnect();
        if (mCheck) {
            SQLiteDatabase db = myhelper.getWritableDatabase();
            String[] whereArgs = {id};

         count = db.delete(DBHelper.TABLE_NAME, DBHelper.UID + "=?", whereArgs);
        }else{
            count = -100;
        }
        return count;
    }

    public void deleteAll(){

        SQLiteDatabase db = myhelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME,null,null);
        db.close();
    }

    public int updateName(String id , String isRead)
    {
        int count = 0;
        checkConnect();
        if(mCheck) {
            SQLiteDatabase db = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.ISREAD, isRead);
            String[] whereArgs = {id};
            count = db.update(DBHelper.TABLE_NAME, contentValues, DBHelper.UID + " = ?", whereArgs);
        }else{
            count = -100;
        }
        return count;
    }

    public int updateAll(String mid ,String title, String content ,String desciption ,String isRead , String createdOn)
    {    int count = 0;
        checkConnect();
        if(mCheck){
            SQLiteDatabase db = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.UID, mid);
            contentValues.put(DBHelper.TITLE, title);
            contentValues.put(DBHelper.CONTENT, content);
            contentValues.put(DBHelper.DESCRIPTION, desciption);
            contentValues.put(DBHelper.ISREAD, isRead);
            contentValues.put(DBHelper.CREATEDON, createdOn);
            String[] whereArgs = {mid};
            count = db.update(DBHelper.TABLE_NAME, contentValues, DBHelper.UID + " = ?", whereArgs);
        }
        else{
          count = -100;
        }
        return count;

    }
    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }
    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            mCheck = false;
        }
        else{
            mCheck = true;
        }
    }


    static class DBHelper extends SQLiteOpenHelper{
      private final String TAG = "DBHelper";

      private static final String DATABASE_NAME = "notification_db";    // Database Name
      private static final String TABLE_NAME = "notification";   // Table Name
      private static final int DATABASE_Version = 1;    // Database Version
      private static final String UID= "id";     // Column I (Primary Key)
      private static final String TITLE = "title";    //Column II
      private static final String CONTENT = "content";
      private static final String DESCRIPTION = "description";
      private static final String ISREAD = "isRead";
      private static final String CREATEDON = "createdOn";
      private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
              " ("+UID+" INTEGER PRIMARY KEY , "+TITLE+" VARCHAR(255) ,"+
              CONTENT+" VARCHAR(225) ,"+DESCRIPTION+" VARCHAR(225) ,"+ ISREAD+" VARCHAR(225) ,"
              +CREATEDON+" VARCHAR(225));";
      private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
      private Context context;

      public DBHelper(Context context){
           super(context,DATABASE_NAME,null,DATABASE_Version);
           this.context = context;
      }
      @Override
      public void onCreate(SQLiteDatabase db) {
           try{
               db.execSQL(CREATE_TABLE);
           }catch (Exception e){
               Log.e(TAG,e.getMessage());
           }
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         try{
             Log.d(TAG,"OnUpgrade");
             db.execSQL(DROP_TABLE);
             onCreate(db);
         }catch (Exception e){
             Log.e(TAG,e.getMessage());
         }
      }


  }
}

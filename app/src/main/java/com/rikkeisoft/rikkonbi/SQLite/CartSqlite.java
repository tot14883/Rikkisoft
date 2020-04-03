package com.rikkeisoft.rikkonbi.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CartSqlite {
    private final String TAG = "CartSqlite";
    DBHelper myhelp;

    public CartSqlite(Context context){
        myhelp = new DBHelper(context);
    }

    public long InsertData(int mId,String product_name,String image_url,
                           int price,int quantity,int max_quan){

        long id = 0;
        try {
            SQLiteDatabase dbb = myhelp.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.UID, mId);
            contentValues.put(DBHelper.NAME, product_name);
            contentValues.put(DBHelper.IMAGE, image_url);
            contentValues.put(DBHelper.PRICE, price);
            contentValues.put(DBHelper.QUANTITY, quantity);
            contentValues.put(DBHelper.MAX_QUAN, max_quan);
            id = dbb.insert(DBHelper.TABLE_NAME, null, contentValues);
        }catch (Exception e){
            id = updataData(String.valueOf(mId),product_name,image_url,price,quantity,max_quan);
        }
        return id;
    }

    public long getProfilesCount() {
        SQLiteDatabase db = myhelp.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_NAME);
        db.close();
        return count;
    }

    public List<String> getData(){
        SQLiteDatabase db = myhelp.getWritableDatabase();
        String[] columns = {DBHelper.UID,DBHelper.NAME, DBHelper.IMAGE,
               DBHelper.PRICE, DBHelper.QUANTITY, DBHelper.MAX_QUAN};
        Cursor cursor =db.query(DBHelper.TABLE_NAME,columns,null,null,null,null,null);
        ArrayList cart = new ArrayList();
        cart.clear();
        Gson gson =new Gson();

        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(DBHelper.UID));
            String name =cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String image =cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE));
            int price =cursor.getInt(cursor.getColumnIndex(DBHelper.PRICE));
            int quantity =cursor.getInt(cursor.getColumnIndex(DBHelper.QUANTITY));
            int max_qua =cursor.getInt(cursor.getColumnIndex(DBHelper.MAX_QUAN));

            try {
                cart.add(dataSend(cid,name,image,price,quantity,max_qua));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<String> responseData = new ArrayList<String>();
        responseData.clear();
        for(int i = 0;i < cart.size();i++){
            responseData.add(gson.toJson(cart.get(i)));
        }
        return responseData;
    }

    public void deleteAll(){

        SQLiteDatabase db = myhelp.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME,null,null);
        db.close();
    }

    public int delete(String id) {
        int count=0;

        SQLiteDatabase db = myhelp.getWritableDatabase();
        String[] whereArgs = {id};

        count = db.delete(DBHelper.TABLE_NAME, DBHelper.UID + "=?", whereArgs);

        return count;
    }
    public int updataData(String mId,String product_name,String image_url,
                          int price,int quantity,int max_quan){
        int count = 0;

        SQLiteDatabase db = myhelp.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.NAME,product_name);
        contentValues.put(DBHelper.IMAGE,image_url);
        contentValues.put(DBHelper.PRICE,price);
        contentValues.put(DBHelper.QUANTITY,quantity);
        contentValues.put(DBHelper.MAX_QUAN,max_quan);
        String[] whereArgs = {mId};
        count = db.update(DBHelper.TABLE_NAME, contentValues, DBHelper.UID + " = ?", whereArgs);

        return count;
    }

    public int updataProduct(String mId,int quantity,int max_quan){
        int count = 0;

        SQLiteDatabase db = myhelp.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.QUANTITY,quantity);
        contentValues.put(DBHelper.MAX_QUAN,max_quan);
        String[] whereArgs = {mId};
        count = db.update(DBHelper.TABLE_NAME, contentValues, DBHelper.UID + " = ?", whereArgs);

        return count;
    }

    public JsonObject dataSend(int mId,String product_name,String image_url,
                               int price,int quantity,int max_quan) throws JSONException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", mId);
        jsonObject.addProperty("name", product_name);
        jsonObject.addProperty("imageUrl", image_url);
        jsonObject.addProperty("price", price);
        jsonObject.addProperty("quantity", quantity);
        jsonObject.addProperty("maxOrderQuantity",max_quan);

        return jsonObject;
    }


    static class DBHelper extends SQLiteOpenHelper{
        private final String TAG = "DBHelper";


        private static final String DATABASE_NAME = "cart_db";    // Database Name
        private static final String TABLE_NAME = "cart";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID= "id";     // Column I (Primary Key)
        private static final String NAME = "product_name";    //Column II
        private static final String IMAGE = "imageUrl";
        private static final String PRICE = "price";
        private static final String QUANTITY = "quantity";
        private static final String MAX_QUAN = "Max_OrderQuantity";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY , "+NAME+" VARCHAR(255) ,"+
                IMAGE+" VARCHAR(400) ,"+PRICE+" INTEGER ,"+ QUANTITY+" INTEGER ,"
                +MAX_QUAN+" INTEGER);";
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

package Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import model.NotificationModel;

/**
 * Created by Abhijit on 30-12-2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Map.db";
    public static final String TABLE_NAME = "notification";
    public static final String Address_of_incident = "Address_of_incident";
    public static final String attribute_1 = "attribute_1";
    public static final String attribute_2 = "attribute_2";
    public static final String attribute_3 = "attribute_3";
    public static final String cause_of_incident = "cause_of_incident";
    public static final String city_id = "city_id";
    public static final String date_time = "date_time";
    public static final String description_of_incident = "description_of_incident";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String message = "message";
    public static final String obj_part_of_incident = "obj_part_of_incident";
    public static final String pro_uid = "pro_uid";
    public static final String report_by = "report_by";
    public static final String sap_notification = "sap_notification";
    public static final String title = "title";
    private HashMap hp;

    public DataBaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                pro_uid + " text primary key " +
                Address_of_incident + " text " +
                attribute_1 + " text " +
                attribute_2 + " text " +
                attribute_3 + " text " +
                cause_of_incident + " text " +
                city_id + " text " +
                date_time + " text " +
                description_of_incident + " text " +
                latitude + " text " +
                longitude + " text " +
                message + " text " +
                obj_part_of_incident + " text " +
                report_by + " text " +
                sap_notification + " text ");
        Log.e("database","created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNotification(NotificationModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(pro_uid,model.getPro_uid());
        contentValues.put(Address_of_incident,model.getAddress_of_incident());
        contentValues.put(attribute_1,model.getAttribute_1());
        contentValues.put(attribute_2,model.getAttribute_2());
        contentValues.put(attribute_3,model.getAttribute_3());
        contentValues.put(cause_of_incident,model.getCause_of_incident());
        contentValues.put(city_id,model.getCity_id());
        contentValues.put(date_time,model.getDate_time());
        contentValues.put(description_of_incident,model.getDescription_of_incident());
        contentValues.put(latitude,model.getLatitude());
        contentValues.put(longitude,model.getLongitude());
        contentValues.put(message,model.getMessage());
        contentValues.put(obj_part_of_incident,model.getObj_part_of_incident());
        contentValues.put(report_by,model.getReport_by());
        contentValues.put(sap_notification,model.getSap_notification());
        db.insert(TABLE_NAME,null,contentValues);
        if (numberOfRows()>0)
            Log.e("database value",""+numberOfRows());
        return true;
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME + " where " + pro_uid + "= " + id,null);
        return cursor;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public Integer deleteTabel (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                pro_uid +" = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllNotifications() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TABLE_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}

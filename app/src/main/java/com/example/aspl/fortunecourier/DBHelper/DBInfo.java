package com.example.aspl.fortunecourier.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aspl on 5/2/18.
 */

public class DBInfo {

    Context context;
    SQLiteDatabase database;
    DatabaseHelper db_helper;

    public DBInfo(Context context) {
        this.context=context;
        db_helper=new DatabaseHelper(context);
    }

    public void addOrUpdateHelpText(String name,String desc){
        database=db_helper.OpenWritable();

        ContentValues values = new ContentValues();
        int count=getAlarmCount(name);
        values.put(DatabaseHelper.COL_APP_INFO_NAME, name);
        values.put(DatabaseHelper.COL_APP_INFO_DESCRIPTION, desc);

        if(count<1){
            long insert = database.insert(DatabaseHelper.TABLE_APP_INFO, null, values);
            database.close();
             Log.v("--", "insert successfull");
        }else{
            int checkinsert = database.update(DatabaseHelper.TABLE_APP_INFO, values,(DatabaseHelper.COL_APP_INFO_NAME + " = ?"),new String[] { String.valueOf(name)});
            database.close();
            Log.v("--", "update successfull");
        }
    }

    public int getCountOfAlarm(String screenName){
        int count = 0;
        try {
            DatabaseHelper DH = new DatabaseHelper(context);
            SQLiteDatabase db=DH.OpenReadable();
            String countQuery = "SELECT * FROM " + DatabaseHelper.TABLE_APP_INFO+" WHERE "+DatabaseHelper.COL_APP_INFO_NAME+" = '"+screenName+"'";
            Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getDescription(String alarmNumber){

        String selectQuery = "SELECT  * FROM "+DatabaseHelper.TABLE_APP_INFO+" where "+DatabaseHelper.COL_APP_INFO_NAME+" = '"+alarmNumber+"'";
        database=db_helper.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        String str = "";
        int x=0;
        if (c.moveToFirst()){
            do {
                str = c.getString(c.getColumnIndex(DatabaseHelper.COL_APP_INFO_DESCRIPTION));
            } while (c.moveToNext());
        }
        c.close();
        database.close();
        return str;
    }


    public int getAlarmCount(String screenName){
        String selectQuery = "SELECT  * FROM "+DatabaseHelper.TABLE_APP_INFO+" where "
                + DatabaseHelper.COL_APP_INFO_NAME +" = '"+screenName+"'";

        DatabaseHelper DH = new DatabaseHelper(context);
        SQLiteDatabase database = DH.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        int count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }

   /* public void updateSmartWakeStatus(int userid,String alarmNumber,String smartWakeStatus)
    {
        database=db_helper.OpenWritable();
        int count =getAlarmCount(userid, alarmNumber);
        if(count<1)
        {
            //do nothing
        }else
        {
            //update battery status
            String QueryToUpdateUserData="UPDATE "+ DatabaseHelper.TABLE_ALARM +
                    " SET "+ DatabaseHelper.COL_SMART_WAKE_STATUS + " = '"+smartWakeStatus+ "'"+
                    " WHERE "
                    + DatabaseHelper.COL_ALARM_NUMBER + " = '" + alarmNumber + "' AND "
                    + DatabaseHelper.COL_USER_ID + " = '" + userid + "'";

            Cursor cursor=	 database.rawQuery(QueryToUpdateUserData, null);

            int c = cursor.getCount();
            cursor.close();
            database.close();
        }
    }*/
}



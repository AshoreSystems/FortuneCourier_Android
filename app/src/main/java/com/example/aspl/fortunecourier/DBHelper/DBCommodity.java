package com.example.aspl.fortunecourier.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.aspl.fortunecourier.model.Commodity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class DBCommodity {
    Context context;
    SQLiteDatabase database;
    DatabaseHelper db_helper;

    public DBCommodity(Context context) {
        this.context=context;
        db_helper=new DatabaseHelper(context);
    }

    public void addCommodity(Commodity commodity){
        database=db_helper.OpenWritable();

        ContentValues values = new ContentValues();
        //int count=getCommodityCount(commodity.getCommodity_id());

        values.put(DatabaseHelper.COL_COMMODITY_DESC, commodity.getCommodity_desc());
        values.put(DatabaseHelper.COL_UNIT_OF_MEASURES, commodity.getUnit_of_measures());
        values.put(DatabaseHelper.COL_QUANTITY, commodity.getQuantity());
        values.put(DatabaseHelper.COL_COMMODITY_WEIGHT, commodity.getCommodity_weight());
        values.put(DatabaseHelper.COL_COMMODITY_WEIGHT_UNIT, commodity.getCommodity_weight_unit());
        values.put(DatabaseHelper.COL_CUSTOMS_VALUE, commodity.getCustoms_value());
        values.put(DatabaseHelper.COL_CUSTOMS_VALUE_UNIT, commodity.getCustoms_value_unit());
        values.put(DatabaseHelper.COL_COUNTRY_OF_MANUFACTURE, commodity.getCountry_of_manufacture());
        values.put(DatabaseHelper.COL_HARMONIZED_CODE, commodity.getHarmonized_code());
        values.put(DatabaseHelper.COL_EXPORT_LICENSE_NO, commodity.getExport_license_no());
        values.put(DatabaseHelper.COL_EXPIRATION_DATE, commodity.getExpiration_date());
        values.put(DatabaseHelper.COL_IS_TOTALS,commodity.getIsTotals());

        long insert = database.insert(DatabaseHelper.TABLE_COMMODITY, null, values);
        database.close();
        Log.v("--", "insert successfull");

      /* if(count<1){
            long insert = database.insert(DatabaseHelper.TABLE_COMMODITY, null, values);
            database.close();
            Log.v("--", "insert successfull");
        }else{
            int checkinsert = database.update(DatabaseHelper.TABLE_COMMODITY, values,(DatabaseHelper.COL_ID + " = ?"),new String[] { String.valueOf(commodity.getCommodity_id())});
            database.close();
            Log.v("--", "update successfull");
        }*/
    }

    public void updateCommodity(String commodityId,Commodity commodity){
        database=db_helper.OpenWritable();

        ContentValues values = new ContentValues();
        //int count=getCommodityCount(commodity.getCommodity_id());

        values.put(DatabaseHelper.COL_COMMODITY_DESC, commodity.getCommodity_desc());
        values.put(DatabaseHelper.COL_UNIT_OF_MEASURES, commodity.getUnit_of_measures());
        values.put(DatabaseHelper.COL_QUANTITY, commodity.getQuantity());
        values.put(DatabaseHelper.COL_COMMODITY_WEIGHT, commodity.getCommodity_weight());
        values.put(DatabaseHelper.COL_COMMODITY_WEIGHT_UNIT, commodity.getCommodity_weight_unit());
        values.put(DatabaseHelper.COL_CUSTOMS_VALUE, commodity.getCustoms_value());
        values.put(DatabaseHelper.COL_CUSTOMS_VALUE_UNIT, commodity.getCustoms_value_unit());
        values.put(DatabaseHelper.COL_COUNTRY_OF_MANUFACTURE, commodity.getCountry_of_manufacture());
        values.put(DatabaseHelper.COL_HARMONIZED_CODE, commodity.getHarmonized_code());
        values.put(DatabaseHelper.COL_EXPORT_LICENSE_NO, commodity.getExport_license_no());
        values.put(DatabaseHelper.COL_EXPIRATION_DATE, commodity.getExpiration_date());
        values.put(DatabaseHelper.COL_IS_TOTALS,commodity.getIsTotals());

        int checkinsert = database.update(DatabaseHelper.TABLE_COMMODITY, values,(DatabaseHelper.COL_ID + " = ?"),new String[] {commodityId});
        database.close();
        Log.v("--", "insert successfull");

      /* if(count<1){
            long insert = database.insert(DatabaseHelper.TABLE_COMMODITY, null, values);
            database.close();
            Log.v("--", "insert successfull");
        }else{
            int checkinsert = database.update(DatabaseHelper.TABLE_COMMODITY, values,(DatabaseHelper.COL_ID + " = ?"),new String[] { String.valueOf(commodity.getCommodity_id())});
            database.close();
            Log.v("--", "update successfull");
        }*/
    }


    public Commodity getCommodity(String commodity_Id){

        Commodity commodity = new Commodity();
        String selectQuery = "SELECT  * FROM "+DatabaseHelper.TABLE_COMMODITY+" where "+DatabaseHelper.COL_ID+" = '"+commodity_Id+"'";
        database=db_helper.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        int x=0;
        if (c.moveToFirst()){
            do {

                commodity.setCommodity_desc(c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_DESC)));
                commodity.setUnit_of_measures(c.getString(c.getColumnIndex(DatabaseHelper.COL_UNIT_OF_MEASURES)));
                commodity.setQuantity(c.getString(c.getColumnIndex(DatabaseHelper.COL_QUANTITY)));
                commodity.setCommodity_weight(c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT)));
                commodity.setCommodity_weight_unit(c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT_UNIT)));
                commodity.setCustoms_value(c.getString(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE)));
                commodity.setCustoms_value_unit(c.getString(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE_UNIT)));
                commodity.setCountry_of_manufacture(c.getString(c.getColumnIndex(DatabaseHelper.COL_COUNTRY_OF_MANUFACTURE)));
                commodity.setHarmonized_code(c.getString(c.getColumnIndex(DatabaseHelper.COL_HARMONIZED_CODE)));
                commodity.setExport_license_no(c.getString(c.getColumnIndex(DatabaseHelper.COL_EXPORT_LICENSE_NO)));
                commodity.setExpiration_date(c.getString(c.getColumnIndex(DatabaseHelper.COL_EXPIRATION_DATE)));
                commodity.setIsTotals(c.getString(c.getColumnIndex(DatabaseHelper.COL_IS_TOTALS)));
            } while (c.moveToNext());
        }
        c.close();
        database.close();
        return commodity;
    }


    public int getCommodityCount(){
        String selectQuery = "SELECT  * FROM "+DatabaseHelper.TABLE_COMMODITY;//+" where " + DatabaseHelper.COL_ID +" = '"+commodity_id+"'";

        DatabaseHelper DH = new DatabaseHelper(context);
        SQLiteDatabase database = DH.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        int count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }


    public ArrayList<Commodity> getAllCommodityData(){
        ArrayList<Commodity> arrayListOfCommodity= new ArrayList<>();

        String selectQuery = "SELECT  * FROM "+DatabaseHelper.TABLE_COMMODITY;//+" where "+DatabaseHelper.COL_USER_ID+" = "+userId;

        database=db_helper.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        int x=0;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Commodity commodity = new Commodity();

                commodity.setCommodity_id(c.getString(c.getColumnIndex(DatabaseHelper.COL_ID)));
                commodity.setCommodity_desc(c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_DESC)));
                commodity.setUnit_of_measures(c.getString(c.getColumnIndex(DatabaseHelper.COL_UNIT_OF_MEASURES)));
                commodity.setQuantity(c.getString(c.getColumnIndex(DatabaseHelper.COL_QUANTITY)));
                commodity.setCommodity_weight(c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT)));
                commodity.setCommodity_weight_unit(c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT_UNIT)));
                commodity.setCustoms_value(c.getString(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE)));
                commodity.setCustoms_value_unit(c.getString(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE_UNIT)));
                commodity.setCountry_of_manufacture(c.getString(c.getColumnIndex(DatabaseHelper.COL_COUNTRY_OF_MANUFACTURE)));
                commodity.setHarmonized_code(c.getString(c.getColumnIndex(DatabaseHelper.COL_HARMONIZED_CODE)));
                commodity.setExport_license_no(c.getString(c.getColumnIndex(DatabaseHelper.COL_EXPORT_LICENSE_NO)));
                commodity.setExpiration_date(c.getString(c.getColumnIndex(DatabaseHelper.COL_EXPIRATION_DATE)));
                commodity.setIsTotals(c.getString(c.getColumnIndex(DatabaseHelper.COL_IS_TOTALS)));
                arrayListOfCommodity.add(commodity);

            } while (c.moveToNext());
        }
        c.close();
        database.close();
        return arrayListOfCommodity;
    }

    public Commodity getSumOfShipment(){
        ArrayList<Commodity> arrayListOfCommodity= new ArrayList<>();

        String selectQuery = "SELECT  * FROM "+DatabaseHelper.TABLE_COMMODITY; //+" where "+DatabaseHelper.COL_IS_TOTALS+" = As totals";//+As totals;

        database=db_helper.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        Commodity commodity = new Commodity();
        long totalQuanity = 0;
        double totalWeight = 0;;
        double totalCustomValue = 0;;
        String strWeightUnit,strCustomValueUnit;


        // looping through all rows and adding to list
        Log.e("Harshas",""+c.getCount());
        if (c.moveToFirst()) {
            do {

                if(c.getString(c.getColumnIndex(DatabaseHelper.COL_IS_TOTALS)).equalsIgnoreCase("total")){
                    totalQuanity = totalQuanity+c.getLong(c.getColumnIndex(DatabaseHelper.COL_QUANTITY));
                    totalWeight = totalWeight+c.getDouble(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT));
                    totalCustomValue = totalCustomValue+c.getDouble(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE));

                }else {
                    totalQuanity = totalQuanity+c.getLong(c.getColumnIndex(DatabaseHelper.COL_QUANTITY));
                    totalWeight = totalWeight+(c.getDouble(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT))*c.getDouble(c.getColumnIndex(DatabaseHelper.COL_QUANTITY)));
                    totalCustomValue = totalCustomValue+(c.getDouble(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE))*c.getDouble(c.getColumnIndex(DatabaseHelper.COL_QUANTITY)));

                }
                strWeightUnit = c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT_UNIT));
                strCustomValueUnit = c.getString(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE_UNIT));


            } while (c.moveToNext());
            commodity.setQuantity(String.valueOf(totalQuanity));
            commodity.setCommodity_weight(String.valueOf(totalWeight));
            commodity.setCustoms_value(String.valueOf(totalCustomValue));
            commodity.setCustoms_value_unit(strCustomValueUnit);
            commodity.setCommodity_weight_unit(strWeightUnit);

        }
        c.close();
        database.close();
        return commodity;
    }

    public void deleteTableData(Context context){

        DatabaseHelper DH = new DatabaseHelper(context);
        SQLiteDatabase db=DH.OpenWritable();

        db.execSQL("delete from "+ DatabaseHelper.TABLE_COMMODITY);
        db.close();
    }

    public void deleteCommodity(String userId){
        String strQuery = "delete from "+DatabaseHelper.TABLE_COMMODITY+" where "+DatabaseHelper.COL_ID+" = '" + userId + "'";
        DatabaseHelper DH = new DatabaseHelper(context);
        SQLiteDatabase database = DH.getReadableDatabase();
        database.execSQL(strQuery);
        database.close();
    }

    public ArrayList<HashMap<String,String>> getAllCommodityHashMapData(){
        ArrayList<HashMap<String,String>> arrayListOfCommodity= new ArrayList<>();

        String selectQuery = "SELECT  * FROM "+DatabaseHelper.TABLE_COMMODITY;//+" where "+DatabaseHelper.COL_USER_ID+" = "+userId;

        database=db_helper.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        int x=0;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("customs_value",c.getString(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE)));
                hashMap.put("customs_currency",c.getString(c.getColumnIndex(DatabaseHelper.COL_CUSTOMS_VALUE_UNIT)));
                hashMap.put("customs_description",c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_DESC)));
                hashMap.put("quantity_unit",c.getString(c.getColumnIndex(DatabaseHelper.COL_UNIT_OF_MEASURES)));
                hashMap.put("quantity",c.getString(c.getColumnIndex(DatabaseHelper.COL_QUANTITY)));
                hashMap.put("customs_weight",c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT)));
                hashMap.put("country_of_manufacture",c.getString(c.getColumnIndex(DatabaseHelper.COL_COUNTRY_OF_MANUFACTURE)));
                hashMap.put("customs_weight_unit",c.getString(c.getColumnIndex(DatabaseHelper.COL_COMMODITY_WEIGHT_UNIT)));
                hashMap.put("as_totals",c.getString(c.getColumnIndex(DatabaseHelper.COL_IS_TOTALS)));
                hashMap.put("harmonized_code",c.getString(c.getColumnIndex(DatabaseHelper.COL_HARMONIZED_CODE)));
                arrayListOfCommodity.add(hashMap);

            } while (c.moveToNext());
        }
        c.close();
        database.close();
        return arrayListOfCommodity;
    }
}
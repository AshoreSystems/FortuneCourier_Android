package com.example.aspl.fortunecourier.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.aspl.fortunecourier.activity.customer.CommodityDetailsCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.ShipmentDetailsCustomerActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class SessionManager {


	// Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
      
    // Sharedpref file name
    private static final String PREF_NAME = "FortuneCourier";

	public static final String KEY_C_ID = "KEY_C_ID";
	public static final String KEY_C_FIRST_NAME = "KEY_C_FIRST_NAME";
	public static final String KEY_C_LAST_NAME = "KEY_C_LAST_NAME";
	public static final String KEY_C_EMAIL = "KEY_C_EMAIL";
	public static final String KEY_FB_ID = "KEY_FB_ID";
	public static final String KEY_C_PROFILE_URL = "KEY_C_PROFILE_URL";
	public static final String KEY_A_PROFILE_URL = "KEY_A_PROFILE_URL";

	public static final String KEY_C_PHONE_NO = "C_PHONE_NO";
	public static final String KEY_C_DIALLING_CODE = "KEY_C_DIALLING_CODE";
	public static final String KEY_IS_C_REMEMBERED = "KEY_IS_REMEMBERED";
	public static final String KEY_IS_C_LOGOUT = "KEY_IS_C_LOGOUT";
	public static final String KEY_CDT_DEVICE_ID = "KEY_CDT_DEVICE_ID";
	public static final String KEY_C_PASSWORD = "KEY_C_PASSWORD";
	public static final String KEY_CDT_DEVICE_TOKEN = "KEY_CDT_DEVICE_TOKEN";

	public static final String KEY_A_ID = "KEY_A_ID";
	public static final String KEY_A_FIRST_NAME = "KEY_A_FIRST_NAME";
	public static final String KEY_A_LAST_NAME = "KEY_A_LAST_NAME";
	public static final String KEY_A_EMAIL = "KEY_A_EMAIL";
	public static final String KEY_A_PHONE_NO = "KEY_A_PHONE_NO";
	public static final String KEY_A_DIALLING_CODE = "KEY_A_DIALLING_CODE";
	public static final String KEY_IS_A_REMEMBERED = "KEY_IS_A_REMEMBERED";
	public static final String KEY_IS_A_LOGOUT = "KEY_IS_A_LOGOUT";
	public static final String KEY_WHICH_USER = "KEY_WHICH_USER"; // 1= Customer , 2= Associate


	//From Create Shipment Screen

	public static final String KEY_F_COUNTRY_CODE = "KEY_F_COUNTRY_CODE";
	public static final String KEY_F_STATE = "KEY_F_STATE";
	public static final String KEY_F_COMPANY = "KEY_F_COMPANY";
	public static final String KEY_F_CONTACT_NAME = "KEY_F_CONTACT_NAME";
	public static final String KEY_F_ADDRESSLINE_1= "KEY_F_ADDRESSLINE_1";
	public static final String KEY_F_ADDRESSLINE_2= "KEY_F_ADDRESSLINE_2";
	public static final String KEY_F_ZIP_CODE = "KEY_F_ZIP_CODE";
	public static final String KEY_F_CITY = "KEY_F_CITY";
	public static final String KEY_F_DIALLING_CODE = "KEY_F_DIALLING_CODE";
	public static final String KEY_F_PHONE_NUMBER = "KEY_F_PHONE_NUMBER";

	public static final String KEY_T_COUNTRY_CODE = "KEY_T_COUNTRY_CODE";
	public static final String KEY_T_STATE = "KEY_T_STATE";
	public static final String KEY_T_COMPANY = "KEY_T_COMPANY";
	public static final String KEY_T_CONTACT_NAME = "KEY_T_CONTACT_NAME";
	public static final String KEY_T_ADDRESSLINE_1= "KEY_T_ADDRESSLINE_1";
	public static final String KEY_T_ADDRESSLINE_2= "KEY_T_ADDRESSLINE_2";
	public static final String KEY_T_ZIP_CODE = "KEY_T_ZIP_CODE";
	public static final String KEY_T_CITY = "KEY_T_CITY";
	public static final String KEY_T_DIALLING_CODE = "KEY_T_DIALLING_CODE";
	public static final String KEY_T_PHONE_NUMBER = "KEY_T_PHONE_NUMBER";

	public static final String KEY_SHIP_DATE = "KEY_SHIP_DATE";
	public static final String KEY_CARRIER_CODE = "KEY_CARRIER_CODE";

	public static final String KEY_SERVICE_TYPE = "KEY_SERVICE_TYPE";
	public static final String KEY_SERVICE_TYPE_NAME = "KEY_SERVICE_TYPE_NAME";

	public static final String KEY_PICKUP_DROP = "KEY_PICKUP_DROP";
	public static final String KEY_PICKUP_DROP_NAME = "KEY_PICKUP_DROP_NAME";

	public static final String KEY_PACKAGE_TYPE= "KEY_PACKAGE_TYPE";
	public static final String KEY_PACKAGE_TYPE_NAME= "KEY_PACKAGE_TYPE_NAME";

	public static final String KEY_PACKAGE_COUNT= "KEY_PACKAGE_COUNT";
	public static final String KEY_IS_IDENTICAL = "KEY_IS_IDENTICAL";

	public static final String KEY_CONTAINER_TYPE = "KEY_CONTAINER_TYPE";
	public static final String KEY_SHAPE = "KEY_SHAPE";

	public static final String KEY_SIZES = "KEY_SIZES";

	public static final String KEY_DELIVERY_TIMESTAMP = "KEY_DELIVERY_TIMESTAMP";
	public static final String KEY_DELIVERY_DATE = "KEY_DELIVERY_DATE";

	public static final String KEY_SELECTED_SERVICE_DESCRIPTION = "KEY_SELECTED_SERVICE_DESCRIPTION";
	public static final String KEY_SELECTED_SERVICE_TYPE = "KEY_SELECTED_SERVICE_TYPE";

	public static final String KEY_AMOUNT = "KEY_AMOUNT";
	public static final String KEY_CS_TAX1 = "KEY_CS_TAX1";
	public static final String KEY_CS_TAX2 = "KEY_CS_TAX2";
	public static final String KEY_BASE_AMOUNT = "KEY_BASE_AMOUNT";
	public static final String KEY_SURCHARGE = "KEY_SURCHARGE";
	public static final String KEY_ADMISSION_COMMISSION = "KEY_ADMISSION_COMMISSION";



	public static final String KEY_MASTER_TRACKING_NO = "KEY_SELECTED_SERVICE_DESCRIPTION";
	public static final String KEY_SHIPMENT_ID = "KEY_SHIPMENT_ID";
	public static final String KEY_CURRENCY_UNIT ="KEY_CURRENCY_UNIT";
	public static final String KEY_CUSTOM_VALUE = "KEY_CUSTOM_VALUE";
	public static final String KEY_CUSTOM_DESC = "KEY_CUSTOM_DESC";

	/*
	public static final String KEY_ADT_DEVICE_ID = "KEY_ADT_DEVICE_ID";
	public static final String KEY_ADT_DEVICE_TOKEN = "KEY_ADT_DEVICE_TOKEN";*/
	public static final String KEY_A_PASSWORD = "KEY_A_PASSWORD";
	public static final String KEY_SHIPMENT_PURPOSE = "KEY_SHIPMENT_PURPOSE";
	public static final String KEY_PACKAGE_CONTENTS = "KEY_PACKAGE_CONTENTS";

	public static final String KEY_TOTAL_SHIPMENT_WEIGHT = "KEY_TOTAL_SHIPMENT_WEIGHT";
	public static final String KEY_TOTAL_SHIPMENT_WEIGHT_UNIT="KEY_TOTAL_SHIPMENT_WEIGHT_UNIT";
	public static final String KEY_TOTAL_CARRIAGE_VALUE = "KEY_TOTAL_CARRIAGE_VALUE";
	public static final String KEY_TOTAL_CARRIAGE_VALUE_UNIT = "KEY_TOTAL_CARRIAGE_VALUE_UNIT";
	public static final String KEY_COLLECTION_FEES ="KEY_COLLECTION_FEES";
	public static final String KEY_IS_FROM_CALCULATE_RATES ="KEY_IS_FROM_CALCULATE_RATES";


	// Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

	public void putStringData(String keyname,String value) { 
		editor.putString(keyname, value); 
		editor.commit(); 
	}   
	
	/** 
	 * @return the string data from the prefs 
	 * */ 
	public String getStringData(String keyName) { 
		return pref.getString(keyName,""); 
	} 
	
	/** 
	 * Set the int data in the preferences.
	 */ 
	public void putIntData(String keyname,int value) { 
		editor.putInt(keyname, value); 
		editor.commit(); 
	}   
	
	/** 
	 * @return the boolean data from the prefs 
	 * */ 
	
	public int getIntData(String keyName) { 
		return pref.getInt(keyName,0); 
	} 
	
	/** 
	 * Set the boolean data in the preferences.
	 */ 
	public void putBooleanData(String keyname,boolean value) { 
		editor.putBoolean(keyname, value); 
		editor.commit(); 
	}   
	
	/** 
	 * @return the boolean data from the prefs 
	 * */ 
	public boolean getBooleanData(String keyName) { 
		return pref.getBoolean(keyName,false); 
	} 
	
	/** 
	 * Set the long data in the preferences.
	 */ 
	public void putLongData(String keyname,long value) { 
		editor.putLong(keyname, value); 
		editor.commit(); 
	}   
	
	/** 
	 * @return the long data from the prefs 
	 * */ 
	public long getLongData(String keyName) { 
		return pref.getLong(keyName,99); 
	} 

	/** 
	 * Set the Array list data in the preferences.
	 */ 
	public void putArraylistData(String keyname,ArrayList<String> arrayList) { 		
		editor.putInt(keyname,arrayList.size()); 
		for (int i = 0; i < arrayList.size(); i++) {
			editor.remove(keyname+ i);
			editor.putString(keyname + i,arrayList.get(i));
		}
		editor.commit();
	}

	/** 
	 * @return the Array list data from the prefs 
	 * */ 
	public ArrayList<String> getArraylistData(String keyName) { 		
		ArrayList<String> arrayList = new ArrayList<String>();
		int arrayListSize = pref.getInt(keyName, 0);
		for (int i = 0; i < arrayListSize; i++) {
			arrayList.add(pref.getString(keyName+ i, null));
		}
		return arrayList;		
	}
	
	/** 
	 * Set the Array list data in the preferences.
	 */ 
	public void putArraylistIntData(String keyname,ArrayList<Integer> arrayList) { 		
		editor.putInt(keyname,arrayList.size()); 
		for (int i = 0; i < arrayList.size(); i++) {
			editor.remove(keyname+ i);
			editor.putInt(keyname + i,arrayList.get(i));
		}
		editor.commit();
	}   
	
	/** 
	 * @return the Array list data from the prefs 
	 * */ 
	public ArrayList<Integer> getArraylistIntData(String keyName) { 		
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		int arrayListSize = pref.getInt(keyName, 0);
		for (int i = 0; i < arrayListSize; i++) {
			//arrayList.add(pref.getString(keyName+ i, null));
			arrayList.add(pref.getInt(keyName+ i, 0));
		}
		return arrayList;		
	} 
	
	public void putArrayHashSetData(String key, HashSet<String> values){
	        editor.putStringSet(key, values);
	        editor.apply();
	}
	
	public HashSet<String> getHashSetData(String key) {
		 HashSet<String> hash_set=new HashSet<String>();
		 
		 if(pref.getStringSet(key, new HashSet<String>())!=null){
			 hash_set.addAll(pref.getStringSet(key, new HashSet<String>()));
		 }else{
			 hash_set=null;
		 }	       
		 return hash_set;
	}
	
	/**
	 * remove data from pref
	 * @param keyName
	 */
	public void removeData(String keyName){
		editor.remove(keyName);
		editor.commit();
	}	
	
	public void clearAllData(){
		editor.clear();
		editor.commit();
	}



}
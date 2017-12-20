package com.example.aspl.fortunecourier.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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


	/*
	public static final String KEY_ADT_DEVICE_ID = "KEY_ADT_DEVICE_ID";
	public static final String KEY_ADT_DEVICE_TOKEN = "KEY_ADT_DEVICE_TOKEN";*/
	public static final String KEY_A_PASSWORD = "KEY_A_PASSWORD";


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
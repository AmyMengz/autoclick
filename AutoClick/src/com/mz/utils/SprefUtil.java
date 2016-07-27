package com.mz.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SprefUtil {
	private static final String spName="config";
	public static final String C_COUNT="count";
	public static final String C_MARKET="market";
	public static final String C_SELECTION="selection";
	public static final String C_AUTO_MARKET="autoMarket";
	public static final String C_AUTO_HOOK="autoHook";
	public static final String C_INSTALL_TIME="install_time";
	
	public static final String C_AUTO_CLICK="autoClick";
	public static final String C_AUTO_REBOOT="autoReboot";
	public static final String C_HALF_DOWNLOAD="hanfDown";
	
	
	
	public static final String C_HOOK_CLICKED="hook_clicked";
	public static final boolean DEFAULT_HOOK_CLICKED=true;
//	public static final boolean DEFAULT_HOOK_UNCLICKED=true;
	
	private static SharedPreferences preferences=null;
	private static SharedPreferences getCountSP(Context context){
		if(preferences==null){
			preferences=context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		}
		return preferences;
	}
	public static boolean putInt(Context context,String key,int value){
		SharedPreferences sp=getCountSP(context);
		return sp.edit().putInt(key, value).commit();
	}
	public static boolean putString(Context context,String key,String value){
		SharedPreferences sp=getCountSP(context);
		return sp.edit().putString(key, value).commit();
	}
	public static boolean putBool(Context context,String key,boolean value){
		SharedPreferences sp=getCountSP(context);
		return sp.edit().putBoolean(key, value).commit();
	}
	public static String getString(Context context,String key,String defaultV) {
		return getCountSP(context).getString(key, defaultV);
	}
	public static int getInt(Context context,String key,int defaultV) {
		return getCountSP(context).getInt(key, defaultV);
	}
	public static boolean getBool(Context context,String key,boolean defaultV){
		return getCountSP(context).getBoolean(key, defaultV);
	}

}

package com.mz.utils;

import java.util.List;

import com.mz.autoclick.AutoClickService;
import com.mz.autoclick.MainActivity2;
import com.mz.autoclick.R;
import com.mz.config.Contants;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

public  class ActivityUtil {

	public static void openOtherApk(Context context,String packageName){
		Intent intent=context.getPackageManager().getLaunchIntentForPackage(packageName);
		Logger.i(Contants.BAIDU_TAG,"intent=================="+intent);
		if(intent!=null){
			context.startActivity(intent);
		}
		
	}
	public static void openHOOKAPK(Context context) {
		try {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.MAIN");
			intent.setClassName("com.hdjad.github","com.hdjad.github.activity.LoginActivity");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void openYYB(Context context) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(
				"com.tencent.android.qqdownloader",
				"com.tencent.midas.wx.APMidasWXPayActivity"));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static String getPackageName(Context context, String path) {
		String packageName = null;
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		if(info!=null){
			packageName = info.packageName;
		}
		return packageName;
	}
	/**
	 * 权限检测
	 * @param context
	 */
	public static void showPermission(Context context) {
		if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
				&& !isNoSwitch(context)) {
			Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			Toast.makeText(context,
					context.getString(R.string.need_permission),
					Toast.LENGTH_SHORT).show();
			
		}
	}
	/**
	 * 是否有获取栈顶权限
	 * @param context
	 * @return
	 */
	public static boolean isNoSwitch(Context context) {
		long ts = System.currentTimeMillis();
		UsageStatsManager usageStatsManager = (UsageStatsManager) context
				.getSystemService(Context.USAGE_STATS_SERVICE);
		List queryUsageStats = usageStatsManager.queryUsageStats(
				UsageStatsManager.INTERVAL_BEST, 0, ts);
		if (queryUsageStats == null || queryUsageStats.isEmpty()) {
			return false;
		}
		return true;
	}
	/**
	 * 获取栈顶Activity
	 * @param context
	 * @return
	 */
	public static String getLauncherTopApp(Context context) {
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> appInfos = activityManager.getRunningTasks(1);
			if(null !=appInfos && !appInfos.isEmpty()){
				return appInfos.get(0).topActivity.toString();
			}
		}else {
			UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
			//Query for events in the given time range. Events are only kept by the system for a few days.
			long endTime = System.currentTimeMillis();
			long beginTime = endTime - 10000;
			if(sUsageStatsManager == null){
				sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
			}
			String result="";
			UsageEvents.Event event  = new UsageEvents.Event();
			UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
			while(usageEvents.hasNextEvent()){
				usageEvents.getNextEvent(event);
				if(event.getEventType()==UsageEvents.Event.MOVE_TO_FOREGROUND){
					ComponentName com =new ComponentName(event.getPackageName(),event.getClassName());
					result = com.toString();
				}
			}
			if(!TextUtils.isEmpty(result)){
				return result;
			}			
		}
		return "";
	}
	
	public static void checkAccessibility(Context context){
		if(!isAccessibilitySettingsOn(context)){
			try {    
                //打开系统设置中辅助功能    
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);    
                context.startActivity(intent);    
                Toast.makeText(context, "找到AutoClick，然后开启服务即可", Toast.LENGTH_LONG).show();    
            } catch (Exception e) {    
                e.printStackTrace();    
            } 
		}
	}
	
	/**
	 * Android检测辅助功能是否开启
	 * @param mContext
	 * @return
	 */
	public static boolean isAccessibilitySettingsOn(Context mContext) {
	    int accessibilityEnabled = 0;
	    final String service = mContext.getPackageName() + "/" + AutoClickService.class.getCanonicalName();
	    try {
	        accessibilityEnabled = Settings.Secure.getInt(
	                mContext.getApplicationContext().getContentResolver(),
	                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
	        Logger.i("accessibilityEnabled = " + accessibilityEnabled);
	    } catch (Settings.SettingNotFoundException e) {
	    	Logger.i("Error finding setting, default accessibility to not found: "
	                + e.getMessage());
	    }
	    TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

	    if (accessibilityEnabled == 1) {
	    	Logger.i("***ACCESSIBILITY IS ENABLED*** -----------------");
	        String settingValue = Settings.Secure.getString(
	                mContext.getApplicationContext().getContentResolver(),
	                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
	        if (settingValue != null) {
	            mStringColonSplitter.setString(settingValue);
	            while (mStringColonSplitter.hasNext()) {
	                String accessibilityService = mStringColonSplitter.next();

	                Logger.i("-------------- > accessibilityService :: " + accessibilityService + " " + service);
	                if (accessibilityService.equalsIgnoreCase(service)) {
	                	Logger.i( "We've found the correct setting - accessibility is switched on!");
	                    return true;
	                }
	            }
	        }
	    } else {
	    	Logger.i("***ACCESSIBILITY IS DISABLED***");
	    }

	    return false;
	}
}

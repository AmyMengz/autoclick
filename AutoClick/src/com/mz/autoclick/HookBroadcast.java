package com.mz.autoclick;

import com.mz.config.Contants;
import com.mz.config.PackageNameConfig;
import com.mz.utils.ActivityUtil;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HookBroadcast extends BroadcastReceiver {
	boolean market = true;

	@Override
	public void onReceive(final Context context, Intent intent) {
		boolean autoMarket = SprefUtil.getBool(context,
				SprefUtil.C_AUTO_MARKET, true);

		if (intent.getAction().equals(PackageNameConfig.HDJ_GIT_BROADCAST)) {
			Logger.i("HookBroadcast===autoMarket----" + autoMarket);
			if (!autoMarket)
				return;
			int count = SprefUtil.getInt(context, SprefUtil.C_COUNT, 0);
			SprefUtil.putInt(context, SprefUtil.C_COUNT, count + 1);
			MainActivity.handleDownload();

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (market) {
						String top = ActivityUtil.getLauncherTopApp(context);// getTopActivity(context);
//						Logger.i("HookBroadcast===top----" + top);
						if (top.equals(PackageNameConfig.INSTALL_ACTIVITY)) {
							SprefUtil.putBool(context, SprefUtil.C_HOOK_CLICKED, false);
							
							int flag = -1;
							int waitTime = SprefUtil.getInt(context,SprefUtil.C_INSTALL_TIME, 0);
							Logger.i("HookBroadcast===InstallPage----market" + market+"  waitTime  "+waitTime);
							try {
								Thread.sleep(waitTime * 1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (SprefUtil.getBool(context,SprefUtil.C_AUTO_HOOK, true)) {
								ActivityUtil.openHOOKAPK(context);
								market = false;
							}

						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

		}

	}

	private boolean isNetWorkConneted(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null)
			return false;
		NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
		if (networkInfos == null || networkInfos.length <= 0)
			return false;
		for (int i = 0; i < networkInfos.length; i++) {
			if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		}
		return false;
	}

}

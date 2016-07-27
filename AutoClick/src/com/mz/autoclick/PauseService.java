package com.mz.autoclick;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.mz.utils.ActivityUtil;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;
import com.mz.utils.TTSUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class PauseService extends Service {
	int count = 0;
	int noRootCount = 0;
	String top = "", last = "";
	boolean lastRoot = true, nowRoot = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		checkTopActivity();
		showNotif();
		return START_STICKY;
	}

	private static final int NOTIFY_FAKEPLAYER_ID = 1339;

	void showNotif() {

		Intent i = new Intent(this, MainActivity.class);
		// 注意Intent的flag设置：FLAG_ACTIVITY_CLEAR_TOP:
		// 如果activity已在当前任务中运行，在它前端的activity都会被关闭，它就成了最前端的activity。FLAG_ACTIVITY_SINGLE_TOP:
		// 如果activity已经在最前端运行，则不需要再加载。设置这两个flag，就是让一个且唯一的一个activity（服务界面）运行在最前端。
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		Notification myNotify = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(getString(R.string.background_service))
				.setContentTitle(getString(R.string.background_service))
				.setContentText(getString(R.string.background_service_content))
				.setContentIntent(pi).build();
		// 设置notification的flag，表明在点击通知后，通知并不会消失，也在最右图上仍在通知栏显示图标。这是确保在activity中退出后，状态栏仍有图标可提下拉、点击，再次进入activity。
		myNotify.flags |= Notification.FLAG_NO_CLEAR;

		// 步骤 2：startForeground( int,
		// Notification)将服务设置为foreground状态，使系统知道该服务是用户关注，低内存情况下不会killed，并提供通知向用户表明处于foreground状态。
		startForeground(NOTIFY_FAKEPLAYER_ID, myNotify);
	}

	private void checkTopActivity() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					checkRebootFlag();
					String top = ActivityUtil.getLauncherTopApp(getApplicationContext());
//					Logger.i("PauseService===top "+top+"==count=="+count+" easy  ");
					if (!SprefUtil.getBool(getApplicationContext(),
							SprefUtil.C_AUTO_HOOK, true)) {
						try {
							Thread.sleep(5 * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						count = 0;
						continue;
					}
					top = ActivityUtil
							.getLauncherTopApp(getApplicationContext());
					if (!top.equals(last)) {
						last = top;
						count = 0;
					} else {
						last = top;
						count++;
						if (count >= 30) {
							SprefUtil.putBool(getApplicationContext(),SprefUtil.C_HOOK_CLICKED, false);
							
							
							count = 0;
							ActivityUtil.openHOOKAPK(getApplicationContext());
						}
						try {
							Thread.sleep(5* 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		}).start();

	}

	@Override
	public void onDestroy() {
		Intent localIntent = new Intent();
		localIntent.setClass(this, PauseService.class); // 销毁时重新启动Service
		this.startService(localIntent);
		super.onDestroy();
	}

	public void checkRebootFlag() {
		/*if (EasyOperateClickUtil.getNeedRebootFlag(getApplicationContext()) == EasyOperateClickUtil.NEED_REBOOT) {
			ActivityUtils.reBoot(getApplicationContext());
		}
		if (EasyOperateClickUtil.getAutoRebootFlag(this) == EasyOperateClickUtil.AUTOREBOOT) {
			Logger.i("noRootCount--------------------------==" + noRootCount);
			if (!CmdUtil.isRoot()) {
				lastRoot = nowRoot;
				nowRoot = false;
				if (lastRoot == nowRoot) {
					noRootCount++;
				}
				Logger.i("noRootCount--------------------------=="
						+ noRootCount + "  " + lastRoot + "  " + nowRoot);
				if (noRootCount >= GlobalConstants.NO_ROOT_COUNT) {

					TTSUtil.getInstance(this).ring();
					Intent in = new Intent(getApplicationContext(),
							AlertActivity.class);
					in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
				}
			} else {
				lastRoot = nowRoot;
				nowRoot = true;
				noRootCount = 0;
			}
		}*/

	}

}

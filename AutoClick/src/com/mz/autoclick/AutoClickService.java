package com.mz.autoclick;

import java.util.List;

import com.mz.config.Contants;
import com.mz.config.PackageNameConfig;
import com.mz.utils.AutoClickHelp;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;

import android.accessibilityservice.AccessibilityService;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ProgressBar;

public class AutoClickService extends AccessibilityService {

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
	
		Logger.i(event.getClassName() + "   " + event.getPackageName() + "===="
				+ AccessibilityEvent.eventTypeToString(event.getEventType())
				);
		if(SprefUtil.getBool(this, SprefUtil.C_AUTO_CLICK, false)){
			AutoClickHelp.handleEventByPAckageName(event,this);
		}
		
 
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

	public void onServiceConnected() {

	}

	public void init(int connectionId, IBinder windowToken) {

	}

	public boolean onGesture(int gestureId) {
		return false;

	}

	public boolean onKeyEvent(KeyEvent event) {
		return false;

	}

}

package com.mz.autoclick.marketjob;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public interface  MarketJob {
	public abstract void handleEvent(AccessibilityEvent event,AccessibilityService context);
//	public boolean isWindowChange(AccessibilityEvent event,String className);

}

package com.mz.utils;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.mz.autoclick.marketjob.BaiDuJob;
import com.mz.autoclick.marketjob.JobCreator;
import com.mz.autoclick.marketjob.MarketJob;
import com.mz.config.Contants;
import com.mz.config.PackageNameConfig;

public class AutoClickHelp {
	
	static MarketJob job;
	public static void handleEventByPAckageName(AccessibilityEvent event,AccessibilityService context) {
		job=JobCreator.createJob(event.getPackageName().toString());
		if(job!=null){
			job.handleEvent(event, context);
		}
	}
}

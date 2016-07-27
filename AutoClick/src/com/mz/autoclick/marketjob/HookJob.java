package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.PackageNameConfig;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class HookJob implements MarketJob {
//	private boolean isEasyBtnClicked=false;
	private static HookJob instance;
	public  HookJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleEvent(AccessibilityEvent event,AccessibilityService context) {
		AccessibilityNodeInfo nodeInfo=context.getRootInActiveWindow();
		if(nodeInfo== null) return;
		List<AccessibilityNodeInfo> easyList=nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.HDJ_HOOK_EASY_CLICK_VIEW_ID);
		if(easyList!=null&&easyList.size()>0){
			AccessibilityNodeInfo node=easyList.get(0);
			if(SprefUtil.getBool(context, SprefUtil.C_HOOK_CLICKED, SprefUtil.DEFAULT_HOOK_CLICKED)) return;
			SprefUtil.putBool(context, SprefUtil.C_HOOK_CLICKED, true);
			boolean res=node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			Logger.i_hook("==============="+res);
		}
//		HDJ_HOOK_EASY_CLICK_VIEW_ID

	}

}

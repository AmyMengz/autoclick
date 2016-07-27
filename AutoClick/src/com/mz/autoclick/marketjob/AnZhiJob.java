package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.PackageNameConfig;
import com.mz.utils.Logger;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class AnZhiJob implements MarketJob {

	public AnZhiJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleEvent(AccessibilityEvent event,AccessibilityService context) {
		AccessibilityNodeInfo nodeInfo = context.getRootInActiveWindow();
		if(nodeInfo==null) {
			return;
		}
		List<AccessibilityNodeInfo> downList = nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.AZ_DOWN_VIEW_ID);
		Logger.i("downList==============="+downList.size());
		if(downList!=null&&downList.size()>0){
			AccessibilityNodeInfo node = downList.get(0);
				boolean c = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				Logger.i("===========" + node.getText()+"  res "+c );
		}
		
		/*List<AccessibilityNodeInfo> progressList = nodeInfo.findAccessibilityNodeInfosByText("%");
		Logger.i("progressList==============="+progressList.size());
		for (int i = 0; i < progressList.size(); i++) {
			AccessibilityNodeInfo node = progressList.get(i);
			Logger.i("===========" + node.getText()+"    "+node.getClassName());
		}*/
	}

}

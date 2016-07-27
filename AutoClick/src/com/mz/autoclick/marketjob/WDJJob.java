package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.PackageNameConfig;
import com.mz.utils.Logger;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class WDJJob implements MarketJob {
	public WDJJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleEvent(AccessibilityEvent event,AccessibilityService context) {
		AccessibilityNodeInfo nodeInfo = context.getRootInActiveWindow();
		if(nodeInfo==null) {
			return;
		}
		List<AccessibilityNodeInfo> downList = nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.WDJ_DOWN_VIEW_ID);
		Logger.i("downList==============="+downList.size());
		if(downList!=null&&downList.size()==1){
			AccessibilityNodeInfo node = downList.get(0);
			Logger.i( "node===========" + node.getText()+"   "+node.getClassName());
//			if(click) return;
			if("°²×°".equals(node.getText())){
				boolean c = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				Logger.i("===========" + node.getText()+"  res "+c );
			}
		}
		


	}

}

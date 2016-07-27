package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.PackageNameConfig;
import com.mz.utils.Logger;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class YYBJob implements MarketJob {

	public YYBJob() {
	}

	@Override
	public void handleEvent(AccessibilityEvent event,AccessibilityService context) {
		AccessibilityNodeInfo nodeInfo=context.getRootInActiveWindow();
		if(nodeInfo==null){
			return;
		}
//		Logger.i_yyb("nodeInfo===="+nodeInfo.getClassName());
//		List<AccessibilityNodeInfo> textList=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.android.qqdownloader:id/r5");
//		if(textList!=null&&textList.size()>0){
//			AccessibilityNodeInfo node=textList.get(0);
//			Logger.i_yyb("======================textList===="+node.getText()+node.getClassName());
//			for (int i = 0; i < node.getChildCount(); i++) {
//				Logger.i_yyb("======================textList===="+i+"========"+node.getChild(i).getClassName());
//			}
//			
//		}
		//обть
		List<AccessibilityNodeInfo> downList=nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.YYB_DOWN_VIEW_ID);
		if(downList!=null&&downList.size()>0){
			AccessibilityNodeInfo node=downList.get(0);
			boolean res=node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			Logger.i_yyb("======================click===="+res+"  "+node.getClassName());
		}
		
	}

}

package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.PackageNameConfig;
import com.mz.utils.Logger;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class _360Job implements MarketJob {
	boolean isJump=false;
	public _360Job() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleEvent(AccessibilityEvent event,AccessibilityService context) {
//		Logger.i_360("33333333333333333============"+event.getBeforeText()+"==="+event.getText());
		AccessibilityNodeInfo nodeInfo = context.getRootInActiveWindow();
		if(nodeInfo==null){
			return;
		}
		
		List<AccessibilityNodeInfo> liuliangList= nodeInfo.findAccessibilityNodeInfosByText(PackageNameConfig._360_LIULIANG_TEXT);
		if(liuliangList!=null&&liuliangList.size()>0){
			AccessibilityNodeInfo node = liuliangList.get(1);
//			Logger.i_360(node.getText()+"");
			boolean res = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			Logger.i_360("liuliangList============"+node.getText()+"    "+res);
			
		}
		
		
		List<AccessibilityNodeInfo> jumpList= nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig._360_JUMP_VIEW_ID);
		if(jumpList!=null&&jumpList.size()>0){
			AccessibilityNodeInfo node = jumpList.get(0);
//			Logger.i_360(node.getText()+"");
			boolean res = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			Logger.i_360("Ìø¹ý-----------============"+node.getText()+"    "+res);
		}
		
		List<AccessibilityNodeInfo> downList= nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig._360_DOWN_VIEW_ID);
		if(downList!=null&&downList.size()>0){
			AccessibilityNodeInfo node = downList.get(0);
			
			boolean res = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			Logger.i_360("xiazai----------"+node.getText()+"    "+res);
		}
		
		//meiyou
		List<AccessibilityNodeInfo> progressList= nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig._360_PROGRESS_TEXT_ID);
		if(progressList!=null&&progressList.size()>0){
			AccessibilityNodeInfo node = progressList.get(0);
//			Logger.i_360(node.getText()+"");
			
			Logger.i_360("progress----------"+node.getText()+"    ");
		}
		
	}

}

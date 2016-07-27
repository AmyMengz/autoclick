package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.PackageNameConfig;
import com.mz.utils.ActivityUtil;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MUJob implements MarketJob {

	public MUJob() {
		
	}

	@Override
	public void handleEvent(AccessibilityEvent event,AccessibilityService context) {
		AccessibilityNodeInfo nodeInfo = context.getRootInActiveWindow();
		if(nodeInfo==null) {
			return;
		}
		//同意并免费使用
		List<AccessibilityNodeInfo> agreeList = nodeInfo.findAccessibilityNodeInfosByText(PackageNameConfig.MU_AGREE_VIEW_TEXT);
		if(agreeList!=null&&agreeList.size()>0){
			AccessibilityNodeInfo node = agreeList.get(0);
				boolean c = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				Logger.i("=agreeList==========" + node.getText()+"  res "+c );
		}
		//启用迅雷加速  确定按钮
		List<AccessibilityNodeInfo> okList = nodeInfo.findAccessibilityNodeInfosByText(PackageNameConfig.MU_SPEED_OK_VIEW_TEXT);
		if(okList!=null&&okList.size()>0){
			AccessibilityNodeInfo node = okList.get(0);
				boolean c = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				Logger.i("======================sure========================" + node.getText());
		}
		//下载按钮
		List<AccessibilityNodeInfo> downList = nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.MU_DOWN_VIEW_ID);//(PackageNameConfig.MU_DOWN_VIEW_TEXY);
		if(downList!=null&&downList.size()>0){
			AccessibilityNodeInfo node = downList.get(0);
			if(!TextUtils.isEmpty(node.getText())&&PackageNameConfig.MU_DOWN_VIEW_TEXY.equals(node.getText().toString())){
				boolean c = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				Logger.i("downList===========" + node.getText()+"  res "+c );
			}
			if(!TextUtils.isEmpty(node.getText())&&PackageNameConfig.MU_DOWN_INSTALL_VIEW_TEXY.equals(node.getText().toString())){
				Logger.i("downList===========" + node.getText());
				SprefUtil.putBool(context, SprefUtil.C_HOOK_CLICKED, false);
				ActivityUtil.openHOOKAPK(context);
				
			}
		}
		
	}

}

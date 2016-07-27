package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.PackageNameConfig;
import com.mz.utils.ActivityUtil;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class PPJob implements MarketJob {

	public PPJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleEvent(AccessibilityEvent event,
			AccessibilityService context) {
		AccessibilityNodeInfo nodeInfo = context.getRootInActiveWindow();
		if (nodeInfo == null) {
			return;
		}
		// 必备应用关闭
		List<AccessibilityNodeInfo> imgList = nodeInfo
				.findAccessibilityNodeInfosByViewId(PackageNameConfig.PP_PANEL_IMG_ID);
		if (imgList != null && imgList.size() > 0) {
			AccessibilityNodeInfo node = imgList.get(0);
			boolean c = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			Logger.i("必备应用关闭t==========" + node.getPackageName() + "   " + c);
		}
		// 
		List<AccessibilityNodeInfo> dialogList = nodeInfo
				.findAccessibilityNodeInfosByViewId(PackageNameConfig.PP_DIALOG_OPEN_VIEW_ID);
		if (dialogList != null && dialogList.size() > 0) {
			AccessibilityNodeInfo node = dialogList.get(0);
			boolean c = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			Logger.i("kaiqit==========" + node.getText() + "   "
					+ node.getPackageName() + "   " + c);
		}
		// 进度
		List<AccessibilityNodeInfo> progressList = nodeInfo
				.findAccessibilityNodeInfosByViewId(PackageNameConfig.PP_PROGRESS_TEXT_ID);
		if (progressList != null && progressList.size() > 0) {
			AccessibilityNodeInfo node = progressList.get(0);
			String prog = node.getText().toString();
			float f=0;
			if (prog.contains("%")) {
				int index = prog.indexOf("%");
				String progress = prog.substring(0, index);
				f=Float.parseFloat(progress);
				Logger.i("=progressList==========" + progress + "   ");
			}
			int percent=SprefUtil.getInt(context, SprefUtil.C_HALF_DOWNLOAD, 100);
			if(percent==100) return;
			if(f>=percent){
				SprefUtil.putBool(context, SprefUtil.C_HOOK_CLICKED, false);
				ActivityUtil.openHOOKAPK(context.getApplicationContext());
			}

		}
		// 下载按钮
		List<AccessibilityNodeInfo> downList = nodeInfo
				.findAccessibilityNodeInfosByViewId(PackageNameConfig.PP_DOWN_VIEW_ID);
		Logger.i("downList===============" + downList.size());
		for (int i = 0; i < downList.size(); i++) {
			AccessibilityNodeInfo node = downList.get(i);
			if (node.getClassName().equals("android.widget.TextView")) {
				if ("下载".equals(node.getText())) {
					boolean c = node
							.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					Logger.i("===========" + node.getText() + "  res " + c);
				}
			}

		}

	}
}

package com.mz.autoclick.marketjob;

import java.util.List;

import com.mz.config.Contants;
import com.mz.config.PackageNameConfig;
import com.mz.utils.ActivityUtil;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class BaiDuJob implements MarketJob{
	public  static boolean BAIDU_CLICKED=false;
	public  static boolean BAIDU_PROGRESS_CLICKED=false;

	public BaiDuJob() {
		
	}
	@Override
	public void handleEvent(AccessibilityEvent event,AccessibilityService context) {
		if(isWindowChange(event, PackageNameConfig.BAIDU_ACT_CLASSNAME)){
			BAIDU_CLICKED=false;
			BAIDU_PROGRESS_CLICKED=false;
		}
		AccessibilityNodeInfo nodeInfo = context.getRootInActiveWindow();
		if(nodeInfo==null) {
			return;
		}
		//流量下载  "继续下载"
		if(event.getClassName().equals(PackageNameConfig.BAIDU_LIULINAG_CLASSNAME)){
			List<AccessibilityNodeInfo> list_liuliang=nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.BAIDU_LIULIANG_DOWN_VIEW);

					if(list_liuliang!=null&&list_liuliang.size()>0){
				AccessibilityNodeInfo node=list_liuliang.get(0);
				boolean res=node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				Logger.i(Contants.BAIDU_TAG, "====liuliang=================="+res);
			}
		}
		//下载进度
		List<AccessibilityNodeInfo> list=nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.BAIDU_PROGRESS_TEXT_ID);
		
		if(list!=null&&list.size()>0){
			AccessibilityNodeInfo node=list.get(0);
//			Logger.i(Contants.BAIDU_TAG,node.getText()+"===============");
			
			if(!TextUtils.isEmpty(node.getText())){
				String progress=node.getText().toString();
				int index=progress.indexOf("%");
				if(index>0){
					BAIDU_CLICKED=true;
					String pro=progress.substring(0, index);
					float f=Float.parseFloat(pro);
					Logger.i(Contants.BAIDU_TAG,pro+"==============="+f+BAIDU_PROGRESS_CLICKED);
					int percent=SprefUtil.getInt(context, SprefUtil.C_HALF_DOWNLOAD, 100);
					if(percent==100) return;
					if(f>=percent){
						if(BAIDU_PROGRESS_CLICKED)return;
						BAIDU_PROGRESS_CLICKED=true;
						SprefUtil.putBool(context, SprefUtil.C_HOOK_CLICKED, false);
						ActivityUtil.openHOOKAPK(context.getApplicationContext());
					}
					
				}
			}
//			
		}
		//下载按钮
			List<AccessibilityNodeInfo> listdown_=nodeInfo.findAccessibilityNodeInfosByViewId(PackageNameConfig.BAIDU_DOWN_VIEW);
			Logger.i(Contants.BAIDU_TAG, "BAIDU========listdown_=="+listdown_.size()+BAIDU_CLICKED);
			if(listdown_!=null&&listdown_.size()>0){
				AccessibilityNodeInfo node=listdown_.get(0);
				if(BAIDU_CLICKED)return;
				boolean res=node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				if(res){
					BAIDU_CLICKED=true;
				}
				Logger.i(Contants.BAIDU_TAG,"===============res=="+res+"   "+node.isClickable());
				
			}	
	}
	public boolean isWindowChange(AccessibilityEvent event, String className) {
		return event.getClassName().equals(className)&&event.getEventType()==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
	}

}

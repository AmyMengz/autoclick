package com.mz.autoclick.marketjob;

import com.mz.config.PackageNameConfig;

public class JobCreator {

	
	public static MarketJob createJob(String packageName) {
		MarketJob job=null;
		
		switch (packageName2int(packageName)) {
		case PackageNameConfig.HOOK_ID:
			job=new HookJob();
			break;
		case PackageNameConfig.BAIDU_ID:
			job=new BaiDuJob();
			break;
		case PackageNameConfig.YYB_ID:
			job=new YYBJob();
			break;
		case PackageNameConfig._360_ID:
			job=new _360Job();
			break;
		case PackageNameConfig.WDJ_ID:
			job=new WDJJob();
			break;
		case PackageNameConfig.PP_ID:
			job=new PPJob();
			break;
		case PackageNameConfig.AZ_ID:
			job=new AnZhiJob();
			break;
		case PackageNameConfig.MU_ID:
			job=new MUJob();
			break;
		

		default:
			break;
		}
		return job;
	}
	private static int packageName2int(String packageName){
		if(packageName.equals(PackageNameConfig.BAIDU_PACKAGE_NAME)){
			return PackageNameConfig.BAIDU_ID;
		}
		else if (packageName.equals(PackageNameConfig.HDJ_HOOK_PACKAGE_NAME)) {
			return PackageNameConfig.HOOK_ID;
		}
		else if (packageName.equals(PackageNameConfig.YYB_PACKAGE_NAME)) {
			return PackageNameConfig.YYB_ID;
		}
		else if (packageName.equals(PackageNameConfig._360_PACKAGE_NAME)) {
			return PackageNameConfig._360_ID;
		}
		else if (packageName.equals(PackageNameConfig.WDJ_PACKAGE_NAME)) {
			return PackageNameConfig.WDJ_ID;
		}
		else if (packageName.equals(PackageNameConfig.PP_PACKAGE_NAME)) {
			return PackageNameConfig.PP_ID;
		}
		else if (packageName.equals(PackageNameConfig.AZ_PACKAGE_NAME)) {
			return PackageNameConfig.AZ_ID;
		}
		else if (packageName.equals(PackageNameConfig.MU_PACKAGE_NAME)) {
			return PackageNameConfig.MU_ID;
		}
		else {
			return -1;
		}
	}

}

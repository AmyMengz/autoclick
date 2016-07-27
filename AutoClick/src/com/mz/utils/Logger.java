package com.mz.utils;

import com.mz.config.Contants;

import android.util.Log;

public class Logger {
	public static void i(String tips) {
		if (Contants.SHOWTAG)
			Log.i(Contants.TAG, tips);
	}
	public static void e(String tips) {
		if (Contants.SHOWTAG)
			Log.i(Contants.TAG, tips);
	}

	public static void i(String name, String tips) {
		if (Contants.SHOWTAG)
			Log.i(name, tips);
	}

	public static void i_hook(String tips) {
		if (Contants.SHOWTAG)
			Log.i(Contants.HOOK_TAG, tips);
	}

	public static void i_baidu(String tips) {
		if (Contants.SHOWTAG)
			Log.i(Contants.BAIDU_TAG, tips);
	}

	public static void i_yyb(String tips) {
		if (Contants.SHOWTAG)
			Log.i(Contants.YYB_TAG, tips);
	}
	public static void i_360(String tips) {
		if (Contants.SHOWTAG)
			Log.i(Contants._360_TAG, tips);
	}

}

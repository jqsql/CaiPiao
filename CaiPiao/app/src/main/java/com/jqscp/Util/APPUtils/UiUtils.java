package com.jqscp.Util.APPUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import com.jqscp.MyApplication;

public class UiUtils {
	public static String[] getStringArray(int tabNames) {
		return getResource().getStringArray(tabNames);
	}

	public static Resources getResource() {
		return MyApplication.getMContext().getResources();
	}
	public static Context getContext(){
		return MyApplication.getMContext();
	}


	public static int getWindowsWidth(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	public static int getWindowsHeight(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	/**
	 * 手机格式化
	 * @param phone
	 */
	public static String setPhoneHide(String phone){
		if(phone==null || phone.isEmpty())
			return "";
		if(phone.length()!=11){
			return phone;
		}else {
			return phone.substring(0,3)+"****"+phone.substring(7,11);
		}


	}
}

package com.jqscp.Util.APPUtils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtils {

	private static int GRAVITY = Gravity.CENTER;

	public static void showLong(Context context, String message) {
		show(context, message, Toast.LENGTH_LONG);
	}

	public static void showShort(Context context, String message) {
		show(context, message, Toast.LENGTH_SHORT);
	}
	
	public static void showShortCenter(Context context, String message) {
		showCenter(context, message, Toast.LENGTH_SHORT);
	}

	

	public static void showLong(Context context, int textId) {
		show(context, textId, Toast.LENGTH_LONG);
	}

	public static void showShort(Context context, int textId) {
		show(context, textId, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, String text, int duration) {
		if(context!=null){
			Toast toast = Toast.makeText(context, text+"", duration);
//			toast.setGravity(GRAVITY, 80, 80);
			toast.show();
		}

	}
	
	private static void showCenter(Context context, String text, int duration) {
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(GRAVITY, 0, 0);
		toast.show();
	}

	public static void show(Context context, int textId, int duration) {
		if(context!=null){
			Toast toast = Toast.makeText(context, textId, duration);
			toast.setGravity(GRAVITY, 80, 80);
			toast.show();
		}
	}















}
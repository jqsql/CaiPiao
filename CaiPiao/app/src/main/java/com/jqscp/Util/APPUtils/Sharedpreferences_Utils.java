package com.jqscp.Util.APPUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 偏好设置(存放的信息都经过AES加密)
 */

public class Sharedpreferences_Utils {
    private SharedPreferences mPreferences;
    private final static String PREFERENCE_NAME="userInfo";//偏好设置存储文件名
    private static Sharedpreferences_Utils mUtils;

    public Sharedpreferences_Utils(Context context) {
        mPreferences=context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public static Sharedpreferences_Utils getInstance(Context context){
        if(mUtils==null){
            synchronized (Sharedpreferences_Utils.class){
                if(mUtils==null)
                    mUtils=new Sharedpreferences_Utils(context);
            }
        }
        return mUtils;
    }

    /**
     * 将数据存入偏好设置String
     */
    public Sharedpreferences_Utils setString(String key,String values){
        SharedPreferences.Editor editor=mPreferences.edit();
        try {
            editor.putString(key, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
        return this;
    }
    /**
     * 将数据存入偏好设置Int
     */
    public Sharedpreferences_Utils setInt(String key,int values){
        SharedPreferences.Editor editor=mPreferences.edit();
        try {
            editor.putString(key,""+values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
        return this;
    }
    /**
     * 将数据存入偏好设置Long
     */
    public Sharedpreferences_Utils setLong(String key,long values){
        SharedPreferences.Editor editor=mPreferences.edit();
        editor.putLong(key,values);
        editor.commit();
        return this;
    }
    /**
     * 将数据从偏好设置中取出Long
     */
    public long getLong(String key){
        long result=mPreferences.getLong(key,-1);//默认返回0
        return result;
    }
    /**
     * 将数据从偏好设置中取出String
     */
    public String getString(String key){
        String result=mPreferences.getString(key,"");//默认返回空
        if("".equals(result)) {
            return "";
        }
        try {
            return result;//解密取出
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 将数据从偏好设置中取出int
     */
    public int getInt(String key){
        String result=mPreferences.getString(key,"0");//默认返回0
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 将数据从偏好设置中取出Boolean
     */
    public Sharedpreferences_Utils setBoolean(String key,boolean values){
        SharedPreferences.Editor editor=mPreferences.edit();
        editor.putBoolean(key,values);
        editor.commit();
        return this;
    }
    /**
     * 将数据从偏好设置中取出int
     */
    public boolean getBoolean(String key){
        boolean result=mPreferences.getBoolean(key,false);//默认返回false
        return result;
    }
    /**
     * 移出信息
     */
    public Sharedpreferences_Utils remove(String key){
        mPreferences.edit().remove(key).commit();
        return this;
    }
    /**
     * 清除信息
     */
    public void clear(){
        mPreferences.edit().clear().commit();
    }
}

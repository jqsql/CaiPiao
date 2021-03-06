package com.jqscp.Util.BaseActivityUtils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Activity操作类
 */
public class BaseAppManager {
    private static BaseAppManager manager;
    private List<Activity> mActivities;

    private BaseAppManager() {
        mActivities = new LinkedList<>();
    }

    public static BaseAppManager getInstance() {
        if (null == manager) {
            synchronized (BaseAppManager.class) {
                if (null == manager) {
                    manager = new BaseAppManager();
                }
            }
        }
        return manager;
    }

    public List<Activity> getActivities() {
        return mActivities;
    }

    public void setActivities(List<Activity> activities) {
        mActivities = activities;
    }

    /**
     * 添加Activity
     *
     * @param activity
     */
    public synchronized void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public int size() {
        return mActivities.size();
    }

    /**
     * 取得栈顶Activity
     *
     * @return
     */
    public synchronized Activity getFrontActivity() {
        return size() > 0 ? mActivities.get(size() - 1) : null;
    }

    /**
     * 移除Activity
     *
     * @param activity
     */
    public synchronized void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    /**
     * 移除某个Activity之上的所有activity
     *
     * @param activity
     */
    public synchronized void removeAllTopActivity(Activity activity) {
        for (int i = mActivities.size() - 1; i >= 0; i--) {
            if (mActivities.get(i) == activity) {
                return;
            }
            if (mActivities.get(i) != null)
                mActivities.get(i).finish();
            mActivities.remove(i);
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void clear() {
        for (int i = mActivities.size() - 1; i > -1; i--) {
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 结束所有后台Activity
     */
    public synchronized void clearBackActivities() {
        for (int i = mActivities.size() - 2; i > -1; i--) {
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
        }
    }
}

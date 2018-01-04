package com.andy.myproject_007.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import com.andy.myproject_007.R;
import com.andy.myproject_007.util.StringUtil;

import java.util.ArrayList;

public class MainApplication extends Application {

    private static Context context;

    private static ArrayList<Activity> mActivitys = new ArrayList<Activity>();

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
    }

    /**
     * 添加Activity到集合中
     */
    private void addActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * 从集合中移除Activity
     */
    private void removeActivity(Activity activity) {
        mActivitys.remove(activity);
    }

    /**
     * Activity的全局监听管理回调
     * */
    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (StringUtil.isEmpty(mActivitys)) {
                return;
            }
            if (mActivitys.contains(activity)) {
                removeActivity(activity);
            }
        }
    };

    /**
     * 关闭所有的Activity
     */
    public static void closeActivity() {
        for (Activity activity : mActivitys) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    /**
     * 杀死该应用进程
     * */
    public static void killApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 重新启动APP
     * */
    public static void restartApp() {
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 获取域名URL
     */
    public static String getURL() {
        String url = null;
        boolean isDebug = context.getResources().getBoolean(R.bool.isDebug);
        if (isDebug) {
            url = context.getResources().getString(R.string.debug_url);
        } else {
            try {
                url = context.getResources().getString(R.string.domain_url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }

//    /**
//     * 获取LoginBean
//     * */
//    public static UserBean getUserBean(Context context){
//        UserBean bean = (UserBean) CacheUtil.getValue(context, Constants.KEY_USER_CACHE);
//        return bean;
//    }

//    /**
//     * 设置LoginBean
//     * */
//    public static void setUserBean(Context context, UserBean mUserBean){
//        CacheUtil.putValue(context, Constants.KEY_USER_CACHE, mUserBean);
//    }

}

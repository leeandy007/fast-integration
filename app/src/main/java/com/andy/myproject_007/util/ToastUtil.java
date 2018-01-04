package com.andy.myproject_007.util;

import android.widget.Toast;

import com.andy.myproject_007.common.MainApplication;


/**
 * @Desc Toast工具类
 */
public class ToastUtil {

    private static ToastUtil mToastUtil;

    public static ToastUtil getInstance(){
        if(mToastUtil == null){
            synchronized (ToastUtil.class) {
                if (mToastUtil == null) {
                    mToastUtil = new ToastUtil();
                }
            }
        }
        return mToastUtil;
    }

    public void Short(CharSequence message) {
        Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void Short(int resId) {
        Toast.makeText(MainApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public void Long(CharSequence message) {
        Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_LONG).show();
    }
    public void Long(int resId) {
        Toast.makeText(MainApplication.getContext(), resId, Toast.LENGTH_LONG).show();
    }




}

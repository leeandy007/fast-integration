package com.andy.myproject_007.util;

/**
 * Created by leeandy007 on 2017/4/19.
 */

public class EventUtil {

    private static long mExitTime;

    public static boolean isDoubleHit(){
        if((System.currentTimeMillis() - mExitTime) > 2000){
            mExitTime = System.currentTimeMillis();
            return false;
        } else {
            return true;
        }
    }

}

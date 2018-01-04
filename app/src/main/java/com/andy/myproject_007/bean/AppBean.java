package com.andy.myproject_007.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by leeandy007 on 2016/12/15.
 */

public class AppBean implements Serializable {

    private String name;

    private String packageName;

    private Drawable icon;

    public AppBean() {
    }

    public AppBean(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}

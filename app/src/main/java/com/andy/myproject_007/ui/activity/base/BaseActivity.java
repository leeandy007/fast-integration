package com.andy.myproject_007.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

	protected Activity _context;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayout(savedInstanceState);
		initView();
		initData();
		bindEvent();
	}
	
	/**
	 * 设定自定义布局
	 * */
	protected abstract void setLayout(Bundle savedInstanceState);
	
	/**
	 * 初始化控件
	 * */
	protected abstract void initView();
	
	/**
	 * 初始化数据
	 * */
	protected abstract void initData();

	/**
	 * 绑定控件事件
	 * */
	protected abstract void bindEvent();
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			doActivityResult(requestCode, intent);
		}
	}
	
	/**
	 * 带返回值跳转的数据的处理方法
	 * */
	protected abstract void doActivityResult(int requestCode, Intent intent);
	
	
}

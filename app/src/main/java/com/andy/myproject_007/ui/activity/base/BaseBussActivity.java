package com.andy.myproject_007.ui.activity.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.common.Constants;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

public abstract class BaseBussActivity extends BaseActivity {

	protected Toolbar mToolbar;
	protected boolean isShow = false;
	protected ImageButton ib_back;
	protected TextView tv_title;
	protected Button btn_right;

	/**
	 * @Desc 侧滑，返回
	 * */
	public enum Action{
		TOGGLE,
		BACK
	}

	/**
	 * @Desc 是否显示Toolbar
	 * */
	protected void isShowToolbarBar(boolean isShow){
		this.isShow = isShow;
	}

	protected void setTitle(String title) {
		tv_title.setText(title);
	}

	/**
	 * @Desc 初始化布局
	 * */
	@Override
	protected void setLayout(Bundle savedInstanceState) {
		setContentView(getLayout());
	}

	protected abstract int getLayout();

	/**
	 * @Desc 初始化控件
	 * */
	@Override
	protected void initView() {
		if (isShow) {
			mToolbar = (Toolbar) findViewById(R.id.toolbar);
		} else {
			ib_back = (ImageButton) findViewById(R.id.ib_back);
			tv_title = (TextView) findViewById(R.id.tv_title);
			ib_back.setBackgroundResource(R.drawable.btn_back);
			btn_right = (Button) findViewById(R.id.btn_right);
		}
	}

	/**
	 * @Desc 初始化数据
	 * */
	@Override
	protected void initData() {
		Intent intent = getIntent();
		if (!isShow) {
			String title = intent.getStringExtra(Constants.KEY_TITLE);
			setTitle(title);
		}
	}

	/**
	 * @Desc 绑定控件事件
	 * */
	@Override
	protected void bindEvent() {
		if (isShow) {
			bindNavigationEvent(Action.BACK, null);
		} else {
			ib_back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}

	/**
	 * @Desc 带返回值跳转的数据的处理方法
	 * @param requestCode 请求码
	 * @param intent 取值载体
	 * */
	@Override
	protected void doActivityResult(int requestCode, Intent intent) {

	}

	/**
	 * @Desc  初始化toolbar
	 * @param navigationResId 图片资源Id，0为不设置
	 * @param logoResId 图片资源Id，0为不设置
	 * @param title 标题
	 * @param subTitle 子标题
	 * */
	protected void initToolbar(int navigationResId, String title, int logoResId, String subTitle){
		if(isShow){
			if(navigationResId != 0){
				mToolbar.setNavigationIcon(navigationResId);
			}
			if(logoResId != 0){
				mToolbar.setLogo(logoResId);
			}
			if(!TextUtils.isEmpty(title)){
				mToolbar.setTitle(title);
				mToolbar.setTitleTextColor(Color.WHITE);
			}
			if(!TextUtils.isEmpty(subTitle)){
				mToolbar.setSubtitle(subTitle);
				mToolbar.setSubtitleTextColor(Color.WHITE);
			}
			setSupportActionBar(mToolbar);
		}
	}

	/**
	 * @Desc Navigation的点击事件
	 * @param action 侧滑或返回
	 * @param listener 点击监听事件，侧滑操作需要自定义，返回操作设置为null
	 * */
	protected void bindNavigationEvent(Action action, View.OnClickListener listener){
		switch (action){
			case TOGGLE:
				mToolbar.setNavigationOnClickListener(listener);
				break;
			case BACK:
				mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
				break;
		}

	}

	/**
	 * @Desc 返回键操作
	 */
	@Override
	public void onBackPressed() {
		this.finish();
		animBack();
	}

	/**
	 * @Desc 页面跳转动画
	 * */

	public void animNext(){
		/**<<<------右入左出*/
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	/**
	 * @Desc 页面返回动画
	 * */
	public void animBack(){
		/**------>>>左入右出*/
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}

	/**
	 * 点击空白处隐藏软键盘获取点击事件
	 * */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isHideInput(view, ev)) {
				HideSoftInput(view.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	// 判定是否需要隐藏
	private boolean isHideInput(View v, MotionEvent ev) {
		if (v != null && (v instanceof EditText)) {
			int[] l = {0, 0};
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	protected void requestPermission(Context context, int requestCode, PermissionListener listener, String... permission) {
		AndPermission.with(context).requestCode(requestCode).permission(permission).callback(listener).start();
	}
}

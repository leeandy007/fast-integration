package com.andy.myproject_007.ui.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.myproject_007.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;


/**
 * @Describe
 * @Author leeandy007
 * @Date 2016-9-2 下午2:05:15
 */
public abstract class BaseFragment extends Fragment {

	protected Context _context;
	protected FragmentCallBack mCallBack;

	/**
	 * Activity取Fragment所传递的值时调用的回调接口
	 */
	public interface FragmentCallBack {

		/**
		 * @param param Object...变参多个不固定个数不规定类型的返回结果
		 * @DESC Activity中调用取出Fragment中的值
		 **/
		public void setResult(Object... param);

	}

	public BaseFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(getLayout(), null);
		initView(v, savedInstanceState);
		initData();
		bindEvent();
		return v;
	}

	/**
	 * 初始化布局
	 */
	protected abstract int getLayout();

	/**
	 * 初始化控件
	 */
	protected abstract void initView(View v, Bundle savedInstanceState);

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 绑定控件事件
	 */
	protected abstract void bindEvent();

	/**
	 * 声明Fragment实例，所创建的回调接口必须要在Activity中实现
	 */
	@Override
	public void onAttach(Activity activity) {
		try {
			mCallBack = (FragmentCallBack) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentCallBack");
		}
		super.onAttach(activity);
	}

	/**
	 * 声明Fragment实例，所创建的回调接口必须要在Activity中实现
	 */
	@Override
	public void onAttach(Context context) {
		try {
			mCallBack = (FragmentCallBack) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement FragmentCallBack");
		}
		super.onAttach(context);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			doActivityResult(requestCode, intent);
		}
	}

	/**
	 * 带返回值跳转的数据的处理方法
	 */
	protected void doActivityResult(int requestCode, Intent intent) {};

	/**
	 * @param clszz  目标页面
	 * @param bundle 传值载体
	 * @Desc 正常页面跳转
	 */
	protected void startActivity(Class clszz, Bundle bundle) {
		Intent intent = new Intent(_context, clszz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		animNext();
	}

	/**
	 * @param clszz       目标页面
	 * @param bundle      传值
	 * @param requestCode 请求码
	 * @Desc 带返回值跳转
	 */
	protected void startActivityForResult(Class clszz, Bundle bundle, int requestCode) {
		Intent intent = new Intent(_context, clszz);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
		animNext();
	}

	/**
	 * @Desc 页面跳转动画
	 */

	public void animNext() {
		/**<<<------右入左出*/
		((Activity) _context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	/**
	 * @Desc 页面返回动画
	 */
	public void animBack() {
		/**------>>>左入右出*/
		((Activity) _context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	protected void requestPermission(Context context, int requestCode, PermissionListener listener, String... permission) {
		AndPermission.with(context).requestCode(requestCode).permission(permission).callback(listener).start();
	}

}

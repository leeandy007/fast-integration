package com.andy.myproject_007.ui.activity.sub.photo;

import android.content.Intent;
import com.andy.myproject_007.R;
import com.andy.myproject_007.ui.activity.base.BaseBussActivity;
import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;


/**
 * 系统相册多选
 */
public class SystemPhotosDetailActivity extends BaseBussActivity {

	private PhotoView imageView;
	private String path;

	@Override
	protected int getLayout() {
		_context = SystemPhotosDetailActivity.this;
		return R.layout.activity_system_photos_detail;
	}

	@Override
	public void initView() {
		imageView = (PhotoView) findViewById(R.id.pv_image);
	}

	@Override
	public void initData() {
		super.initData();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		Glide.with(_context).load(path).dontAnimate().placeholder(R.mipmap.default_pic).crossFade().into(imageView);
	}
	
	@Override
	public void bindEvent() {
		super.bindEvent();
	}
	
}

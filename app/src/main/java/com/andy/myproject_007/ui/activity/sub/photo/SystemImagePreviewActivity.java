package com.andy.myproject_007.ui.activity.sub.photo;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.FilePhotoBean;
import com.andy.myproject_007.ui.activity.base.BaseBussActivity;
import com.andy.myproject_007.ui.adapter.sub.photo.SystemImagePreviewAdapter;

import java.util.List;

/**
 * @Desc 主页面图片预览
 * */
public class SystemImagePreviewActivity extends BaseBussActivity {

	private ViewPager vp_photo;
	private SystemImagePreviewAdapter adapter;
	private int position;
	private List<FilePhotoBean> list;
	private TextView tv_photo_count;
	private Intent intent;

	@Override
	protected int getLayout() {
		_context = SystemImagePreviewActivity.this;
		isShowToolbarBar(true);
		return R.layout.activity_system_image_preview;
	}

	@Override
	public void initView() {
		super.initView();
		initToolbar(R.drawable.btn_back, null, 0, null);
		vp_photo = (ViewPager) findViewById(R.id.vp_photo);
		tv_photo_count = (TextView) findViewById(R.id.tv_photo_count);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		super.initData();
		intent = getIntent();
		list = (List<FilePhotoBean>) intent.getSerializableExtra("list");
		position = intent.getIntExtra("position", 0);
		setDataToView(list, position);
		tv_photo_count.setText((position+1)+"/"+list.size());
	}
	
	@Override
	public void bindEvent() {
		super.bindEvent();
		bindNavigationEvent(Action.BACK, null);
		vp_photo.addOnPageChangeListener(onPageChangeListener);
	}
	
	private void setDataToView(List<FilePhotoBean> list, int position){
		adapter = new SystemImagePreviewAdapter(_context, list, R.layout.adapter_system_photo_pageview_item);
		vp_photo.setAdapter(adapter);
		vp_photo.setCurrentItem(position);
	}


	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int position) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			tv_photo_count.setText((position +1) + "/" + list.size());
		}

	};
	

}

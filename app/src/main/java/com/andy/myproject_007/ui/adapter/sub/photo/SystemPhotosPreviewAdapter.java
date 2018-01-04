package com.andy.myproject_007.ui.adapter.sub.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.FilePhotoBean;
import com.andy.myproject_007.ui.adapter.base.ViewPagerAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class SystemPhotosPreviewAdapter extends ViewPagerAdapter<FilePhotoBean> {

	public SystemPhotosPreviewAdapter(Context context, List<FilePhotoBean> list, int resId) {
		super(context, list, resId);
	}

	@Override
	public void removeItem(ViewGroup viewGroup, int position, Object object) {
		viewGroup.removeView((View) object);
	}

	@Override
	public View dealView(final Context context, List<FilePhotoBean> list, int resId, int position, ViewGroup viewGroup, View view) {
		view = LayoutInflater.from(context).inflate(resId, viewGroup, false);
		final PhotoView imageView = (PhotoView) view.findViewById(R.id.iv_item_img);
		final ProgressBar pb_item_loading = (ProgressBar) view.findViewById(R.id.pb_item_loading);
		FilePhotoBean bean = (FilePhotoBean) getItem(position);
		Glide.with(context).load(bean.getPath()).dontAnimate().crossFade().into(imageView);
		viewGroup.addView(view, 0);
		view.setId(position);
		return view;
	}
	

}

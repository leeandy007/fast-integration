package com.andy.myproject_007.ui.adapter.sub.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.FilePhotoBean;
import com.andy.myproject_007.ui.adapter.base.BaseInfoAdapter;
import com.andy.myproject_007.ui.adapter.base.BaseInfoViewHolder;
import com.andy.myproject_007.ui.adapter.base.ViewHolderCreator;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

/**
 * 手机相册使用的Adapter
 */
public class SystemPhotosAdapter extends BaseInfoAdapter<FilePhotoBean> {

	private static int height;
	private static HashMap<String,String> selectedId;
	private static int type;

	public SystemPhotosAdapter(List<FilePhotoBean> list, ViewHolderCreator mViewHolderCreator, int height) {
		super(list, mViewHolderCreator);
		this.height = height;
		selectedId = new HashMap<String,String>();
	}

	public static class ViewHolder extends BaseInfoViewHolder<FilePhotoBean>{
		private ImageView imageView;
		private View groundImg;
		private ImageView iconImg;
		private TextView tvName, tvCount;
		private LinearLayout ll_tv;

		@Override
		public int getLayout() {
			return R.layout.adapter_system_photos_grid_item;
		}

		@Override
		public void initView(View view) {
			ll_tv = (LinearLayout) view.findViewById(R.id.ll_tv);
			imageView = (ImageView) view.findViewById(R.id.iv_photos_grid_item_img);
			tvName = (TextView) view.findViewById(R.id.tv_photos_grid_item_name);
			tvCount = (TextView) view.findViewById(R.id.tv_photos_grid_item_count);
			groundImg = view.findViewById(R.id.view_photos_grid_item_ground);
			iconImg = (ImageView) view.findViewById(R.id.iv_photos_grid_item_icon);
			LayoutParams param = imageView.getLayoutParams();
			param.height = height;
			imageView.setLayoutParams(param);
		}

		@Override
		public void initData(Context context, FilePhotoBean bean, int postion) {
			String path = bean.getName();
			if(type==1) {//文件
				iconImg.setImageResource(R.mipmap.select_img_pressed);
				if (selectedId.containsKey(bean.getId())) {
					groundImg.setVisibility(View.VISIBLE);
					iconImg.setVisibility(View.VISIBLE);
				} else {
					groundImg.setVisibility(View.GONE);
					iconImg.setVisibility(View.GONE);
				}
				ll_tv.setVisibility(View.GONE);
				Glide.with(context).load(bean.getPath()).dontAnimate().centerCrop().placeholder(R.mipmap.default_pic).crossFade().into(imageView);
			}else if(type==0){//文件夹
				iconImg.setVisibility(View.VISIBLE);
				iconImg.setImageResource(R.mipmap.file_document);
				Glide.with(context).load(bean.getPath()).dontAnimate().centerCrop().placeholder(R.mipmap.default_pic).crossFade().into(imageView);
				ll_tv.setVisibility(View.VISIBLE);
				tvName.setText(path);
				tvCount.setText("("+bean.getCount()+")");
				groundImg.setVisibility(View.GONE);
			}
		}


	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashMap<String, String> getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(HashMap<String, String> selectedId) {
		this.selectedId = selectedId;
	}

}

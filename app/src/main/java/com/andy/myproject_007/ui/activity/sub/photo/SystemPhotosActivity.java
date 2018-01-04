package com.andy.myproject_007.ui.activity.sub.photo;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.FilePhotoBean;
import com.andy.myproject_007.ui.activity.base.BaseBussActivity;
import com.andy.myproject_007.ui.adapter.base.ViewHolderCreator;
import com.andy.myproject_007.ui.adapter.sub.photo.SystemPhotosAdapter;
import com.andy.myproject_007.util.AndroidUtil;
import com.andy.myproject_007.util.StringUtil;
import com.andy.myproject_007.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 系统相册多选
 */
@SuppressWarnings("unchecked")
public class SystemPhotosActivity extends BaseBussActivity {
	
	private GridView gridView;
	private SystemPhotosAdapter adapter;
	private int width;
	private HashMap<String, ArrayList<FilePhotoBean>> map;
	private ArrayList<FilePhotoBean> documents;//文件夹
	private TextView tvEdit;
	private Button btn_preview, btn_sure;
	private RelativeLayout rl_button;
	private ArrayList<FilePhotoBean> selectedList = new ArrayList<FilePhotoBean>();
	private int limit;
	private ArrayList<FilePhotoBean> list;
	public final int REQUEST_SYSTEM_PHOTO_PREVIEW = 100;
	private Intent intent;
	private TextView tv_title;

	@Override
	protected int getLayout() {

		return R.layout.activity_system_photos;
	}

	@Override
	public void initView() {
		super.initView();
		initToolbar(R.drawable.btn_back, null, 0, null);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("所有图片");
		gridView = (GridView) findViewById(R.id.gv_system_photo);
		rl_button = (RelativeLayout) findViewById(R.id.rl_button);
		tvEdit = (TextView) findViewById(R.id.club_sub_common_photos_text);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_preview = (Button) findViewById(R.id.btn_preview);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth()/3;
		intent = getIntent();
		if(intent != null) {
			limit = intent.getIntExtra("limit", 0);
			if(intent.getSerializableExtra("photoList")!=null) {
				list = (ArrayList<FilePhotoBean>) intent.getSerializableExtra("photoList");
			}
		}
		map = AndroidUtil.listAlldir(_context);
		documents = new ArrayList<FilePhotoBean>();
		for (String name : map.keySet()) {
			FilePhotoBean bean = new FilePhotoBean();
			ArrayList<FilePhotoBean> list = map.get(name);
			String path = list.get(0).getPath();
			bean.setName(name);
			bean.setPath(path);
			bean.setType(0);
			bean.setCount(list.size()+"");
			documents.add(bean);
		}
		adapter = new SystemPhotosAdapter((ArrayList<FilePhotoBean>) documents.clone(), new ViewHolderCreator() {
			@Override
			public Object createHolder() {
				return new SystemPhotosAdapter.ViewHolder();
			}
		}, width);
		gridView.setAdapter(adapter);
	}

	@Override
	public void bindEvent() {
		super.bindEvent();
		bindNavigationEvent(Action.BACK, null);
		gridView.setOnItemClickListener(onItemClickListener);
		gridView.setOnItemLongClickListener(onItemLongClickListener);
		btn_sure.setOnClickListener(onClickListener);
		btn_preview.setOnClickListener(onClickListener);
	}

	private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                       int position, long id) {
			if(adapter.getType()!=0) {
				FilePhotoBean bean = (FilePhotoBean) adapter.getItem(position);
				String path = bean.getPath();
				if(!StringUtil.isEmpty(path)) {
					Intent intent = new Intent(_context, SystemPhotosDetailActivity.class);
					intent.putExtra("path", path);
					startActivity(intent);
				} else {
					ToastUtil.getInstance().Short("图片打开失败");
				}
			}
			return true;
		}

	};

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.btn_sure://确定
					HashMap<String, String> map = adapter.getSelectedId();
					if (map.isEmpty()) {
						ToastUtil.getInstance().Short("您未选中任何照片");
					} else {
						intent.putExtra("photoList", selectedList);
						setResult(RESULT_OK, intent);
						finish();
					}
					break;
				case R.id.btn_preview://取消编辑模式
					if(!StringUtil.isEmpty(selectedList)){
						intent = new Intent(_context, SystemPhotosPreviewActivity.class);
						intent.putExtra("selectedList", selectedList);
						startActivityForResult(intent, REQUEST_SYSTEM_PHOTO_PREVIEW);
						animNext();
					} else {
						ToastUtil.getInstance().Short("您未选中任何照片");
					}
					break;
				case R.id.ib_back://返回
					onBackPressed();
					break;
			}
		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                long id) {
			if(adapter.getType()==0) {//文件夹
				FilePhotoBean key = (FilePhotoBean) adapter.getItem(position);
				adapter.clearAll();
				adapter.setType(1);
				tv_title.setText(key.getName()==null?"":key.getName());
				adapter.add(map.get(key.getName()));
				gridView.setNumColumns(3);
				adapter.notifyDataSetChanged();
				adapter.notifyDataSetInvalidated();
				gridView.smoothScrollToPosition(0);
			} else if(adapter.getType()==1){//文件
				//编辑模式
				FilePhotoBean bean = (FilePhotoBean) adapter.getItem(position);
				if(list!=null&&!list.isEmpty()) {
					if(list.contains(bean)) {
						ToastUtil.getInstance().Short("您已选过该照片");
						return;
					}
				}
				if(adapter.getSelectedId().containsKey(bean.getId())) {
					adapter.getSelectedId().remove(bean.getId());
					for (FilePhotoBean photoBean : selectedList) {
						if(photoBean.getId().equals(bean.getId())){
							selectedList.remove(photoBean);
							break;
						}
					}
				} else {
					if(adapter.getSelectedId().size()>=limit) {//大于6不能再选择
						ToastUtil.getInstance().Short("您最多只能选择"+limit+"张");
					} else {
						adapter.getSelectedId().put(bean.getId(), bean.getName());
						selectedList.add(bean);
					}
				}
				tvEdit.setText("已选"+adapter.getSelectedId().size()+"张 / 最多"+limit+"张");
				adapter.notifyDataSetChanged();
				rl_button.setVisibility(View.VISIBLE);
			}
		}

	};

	@Override
	public void onBackPressed() {
		if(adapter.getType()==1){//如果当时处于文件状态截获返回按键
			rl_button.setVisibility(View.GONE);
			adapter.clearAll();
			adapter.setType(0);
			adapter.add((ArrayList<FilePhotoBean>)documents.clone());
			gridView.setNumColumns(3);
			tv_title.setText("所有图片");
			tvEdit.setText("已选0张 / 最多"+limit+"张");
			adapter.getSelectedId().clear();
			selectedList.clear();
			adapter.notifyDataSetChanged();
			adapter.notifyDataSetInvalidated();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void doActivityResult(int requestCode, Intent intent) {
		super.doActivityResult(requestCode, intent);
		switch (requestCode) {
		case REQUEST_SYSTEM_PHOTO_PREVIEW:
			if(null != intent){
				ArrayList<FilePhotoBean> sureList = (ArrayList<FilePhotoBean>) intent.getSerializableExtra("sureList");
				ArrayList<FilePhotoBean> cancelList = (ArrayList<FilePhotoBean>) intent.getSerializableExtra("cancelList");
				if(!StringUtil.isEmpty(selectedList)){
					selectedList.clear();
					selectedList.addAll(sureList);
				}
				for (FilePhotoBean bean : cancelList) {
					if(adapter.getSelectedId().containsKey(bean.getId())) {
						adapter.getSelectedId().remove(bean.getId());
					}
				}
				tvEdit.setText("已选"+adapter.getSelectedId().size()+"张 / 最多"+limit+"张");
				adapter.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
}

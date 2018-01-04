package com.andy.myproject_007.ui.activity.sub.photo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.FilePhotoBean;
import com.andy.myproject_007.ui.activity.base.BaseBussActivity;
import com.andy.myproject_007.ui.adapter.sub.photo.SystemPhotosPreviewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc 调用本地图库选中预览
 */
@SuppressWarnings("unchecked")
public class SystemPhotosPreviewActivity extends BaseBussActivity {

    private ViewPager vp_photo;
    private ImageView iv_selected;
    private TextView tv_photo_count;
    private Button btn_Sure;
    private SystemPhotosPreviewAdapter adapter;
    private List<FilePhotoBean> list;
    private int currentItem = 0, totalPage;
    private Map<String, Boolean> map = new HashMap<String, Boolean>();
    private ArrayList<FilePhotoBean> sureList;
    private ArrayList<FilePhotoBean> cancelList;
    private int selectedItem = -1;
    private int tag;
    private Intent intent;

    @Override
    protected int getLayout() {
        _context = SystemPhotosPreviewActivity.this;
        isShowToolbarBar(true);
        return R.layout.activity_system_photo_preview;
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar(R.drawable.btn_back, null, 0, null);
        tv_photo_count = (TextView) findViewById(R.id.tv_photo_count);
        vp_photo = (ViewPager) findViewById(R.id.vp_photo);
        iv_selected = (ImageView) findViewById(R.id.iv_selected);
        btn_Sure = (Button) findViewById(R.id.btn_Sure);
    }

    @Override
    public void initData() {
        super.initData();
        intent = this.getIntent();
        tag = intent.getIntExtra("tag", 1);
        if (intent != null) {
            list = (List<FilePhotoBean>) intent.getSerializableExtra("list");
            list.remove(null);
            totalPage = list.size();
            if (intent.getStringExtra("position") != null) {
                selectedItem = Integer.parseInt(intent.getStringExtra("position"));
            }
        }
        adapter = new SystemPhotosPreviewAdapter(_context, list, R.layout.adapter_system_photo_pageview_item);
        vp_photo.setAdapter(adapter);
        if (selectedItem != -1) {
            vp_photo.setCurrentItem(selectedItem);
            tv_photo_count.setText((selectedItem + 1) + "/" + totalPage);
        } else {
            tv_photo_count.setText("1/" + totalPage);
        }
        for (FilePhotoBean bean : list) {
            if (bean != null) {
                map.put(bean.getId(), true);
            }
        }
        switch (tag){
            case 2:
                iv_selected.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void bindEvent() {
        super.bindEvent();
        bindNavigationEvent(Action.BACK, null);
        vp_photo.addOnPageChangeListener(onPageChangeListener);
        iv_selected.setOnClickListener(onClickListener);
        btn_Sure.setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_selected:
                    FilePhotoBean bean = list.get(currentItem);
                    for (String id : map.keySet()) {
                        if (id == bean.getId()) {
                            if (map.get(id)) {
                                iv_selected.setImageResource(R.mipmap.select_img_normal);
                                map.put(id, false);
                            } else {
                                iv_selected.setImageResource(R.mipmap.select_img_pressed);
                                map.put(id, true);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.btn_Sure:
                    sureList = new ArrayList<FilePhotoBean>();
                    cancelList = new ArrayList<FilePhotoBean>();
                    for (FilePhotoBean photoBean : list) {
                        for (String id : map.keySet()) {
                            Boolean tag = map.get(id);
                            if (photoBean.getId() == id) {
                                if (tag) {
                                    sureList.add(photoBean);
                                } else {
                                    cancelList.add(photoBean);
                                }
                            }
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sureList", sureList);
                    bundle.putSerializable("cancelList", cancelList);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    _context.finish();
                    animBack();
                    break;
            }
        }
    };

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            tv_photo_count.setText((currentItem + 1) + "/" + totalPage);
            FilePhotoBean bean = list.get(position);
            for (String id : map.keySet()) {
                if (id == bean.getId()) {
                    if (map.get(id)) {
                        iv_selected.setImageResource(R.mipmap.select_img_pressed);
                    } else {
                        iv_selected.setImageResource(R.mipmap.select_img_normal);
                    }
                    break;
                }
            }
        }
    };

}

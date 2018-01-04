package com.andy.myproject_007.ui.activity.sub;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.AppBean;
import com.andy.myproject_007.common.Constants;
import com.andy.myproject_007.common.MainApplication;
import com.andy.myproject_007.ui.activity.base.BaseBussActivity;
import com.andy.myproject_007.ui.adapter.base.ViewHolderCreator;
import com.andy.myproject_007.ui.adapter.sub.MenuAdapter;
import com.andy.myproject_007.ui.adapter.sub.MenuGridAdapter;
import com.andy.myproject_007.ui.adapter.sub.MenuGridCheckAdapter;
import com.andy.myproject_007.ui.adapter.sub.photo.SystemPhotosAdapter;
import com.andy.myproject_007.util.AndroidUtil;
import com.andy.myproject_007.util.EventUtil;
import com.andy.myproject_007.util.LogUtil;
import com.andy.myproject_007.util.StringUtil;
import com.andy.myproject_007.util.ToastUtil;
import com.andy.myproject_007.widget.MarginDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends BaseBussActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigation_view;
    private RecyclerView rv_main;
    private GridView gv_main;
    private List<AppBean> appList = new ArrayList<>();
    private ArrayList<String> checkedList = new ArrayList<String>();
    private MenuGridCheckAdapter adapter;
    private Map<String, Object> checkedMap = null;
    private Set<String> setId = null;
    private String ids;

    @Override
    protected int getLayout() {
        _context = MainActivity.this;
        isShowToolbarBar(true);
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar(R.drawable.btn_menu, "我的框架", 0, null);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        gv_main = (GridView) findViewById(R.id.gv_main);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        bindNavigationEvent(Action.TOGGLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(navigation_view)) {
                    mDrawerLayout.closeDrawer(navigation_view);
                } else {
                    mDrawerLayout.openDrawer(navigation_view);
                }
            }
        });
        gv_main.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtil.getInstance().Debug("测试");
        PackageManager pm = _context.getPackageManager();
        List<PackageInfo> list = AndroidUtil.getAllApps(_context);
        for (PackageInfo pi : list) {
            String name = pm.getApplicationLabel(pi.applicationInfo).toString();
            String packName = pi.applicationInfo.packageName;
            Drawable icon = pm.getApplicationIcon(pi.applicationInfo);
            AppBean bean = new AppBean(name, packName, icon);
            appList.add(bean);
        }
        adapter = new MenuGridCheckAdapter(appList, checkedList, new ViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new MenuGridCheckAdapter.GridCheckViewHolder(R.id.cb);
            }
        });
        gv_main.setAdapter(adapter);
//        MenuGridAdapter adapter = new MenuGridAdapter(appList, new ViewHolderCreator() {
//            @Override
//            public Object createHolder() {
//                return new MenuGridAdapter.GridViewHolder();
//            }
//        });
//        gv_main.setAdapter(adapter);
//        rv_main.setLayoutManager(new GridLayoutManager(_context, 3));
//        rv_main.addItemDecoration(new MarginDecoration(_context));
//        MenuAdapter aadapter = new MenuAdapter(_context, appList, new ViewHolderCreator() {
//            @Override
//            public Object createHolder() {
//                View view = View.inflate(_context, R.layout.adapter_app_grid_item, null);
//                return new MenuAdapter.ViewHolder(view);
//            }
//        });
//        rv_main.setAdapter(aadapter);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(navigation_view)) {
            mDrawerLayout.closeDrawer(navigation_view);
        } else {
            if (EventUtil.isDoubleHit()) {
                MainApplication.closeActivity();
            } else {
                ToastUtil.getInstance().Short(Constants.EXIT_PROCESS);
            }
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            adapter.onItemClick(adapterView, view, i, l);
            checkedMap = adapter.getCheckedItems();
            if (!StringUtil.isEmpty(checkedMap)) {
                setId = checkedMap.keySet();
                ids = StringUtil.getCollectionToString(setId, ",");
            } else {
                ids = null;
            }
            checkedList = (ArrayList<String>) StringUtil.StringToListString(ids, ",");
            ToastUtil.getInstance().Long(ids);
        }
    };


}

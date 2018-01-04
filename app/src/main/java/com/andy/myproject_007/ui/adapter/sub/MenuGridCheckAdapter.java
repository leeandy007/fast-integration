package com.andy.myproject_007.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.AppBean;
import com.andy.myproject_007.ui.adapter.base.BaseCheckedAdapter;
import com.andy.myproject_007.ui.adapter.base.BaseCheckedHolder;
import com.andy.myproject_007.ui.adapter.base.BaseInfoAdapter;
import com.andy.myproject_007.ui.adapter.base.BaseInfoViewHolder;
import com.andy.myproject_007.ui.adapter.base.ViewHolderCreator;
import com.andy.myproject_007.util.AndroidUtil;
import com.andy.myproject_007.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by leeandy007 on 16/9/18.
 */
public class MenuGridCheckAdapter extends BaseCheckedAdapter<AppBean> {

    public MenuGridCheckAdapter(List<AppBean> list, ArrayList<String> checkedList, ViewHolderCreator mViewHolderCreator) {
        super(list, checkedList, mViewHolderCreator);
    }

    @Override
    protected void initCheckedItem(List<AppBean> list) {
        checkedItem = new HashMap<String, CheckedBean>();
        for (AppBean bean : list) {
            String name = bean.getName();
            if (!StringUtil.isEmpty(checkedList)) {
                if (checkedList.contains(name)) {
                    checkedItem.put(name, new CheckedBean(true, bean));
                } else {
                    checkedItem.put(name, new CheckedBean(false, bean));
                }
            } else {
                checkedItem.put(name, new CheckedBean(false, bean));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        AppBean bean = (AppBean) getItem(position);
        String name = bean.getName();
        boolean checked = checkedItem.get(name).isChecked();
        if (checked) {
            checkedItem.get(name).setChecked(false);
        } else {
            checkedItem.get(name).setChecked(true);
        }
        refresh();
    }

    @Override
    public void setItemView(Context context, BaseCheckedHolder holder, int position, View convertView) {
        AppBean bean = (AppBean) getItem(position);
        CheckedBean checked = checkedItem.get(bean.getName());
        holder.initData(context, bean, position, checked);
    }

    public static class GridCheckViewHolder extends BaseCheckedHolder<AppBean> {

        private LinearLayout ll_view;
        private ImageView iv_icon;
        private TextView tv_name;

        public GridCheckViewHolder(int checkedResId) {
            super(checkedResId);
        }

        @Override
        public int getLayout() {
            return R.layout.adapter_app_grid_item;
        }

        @Override
        public void initView(View view) {
            super.initView(view);
            ll_view = (LinearLayout) view.findViewById(R.id.ll_view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }

        @Override
        public void initData(Context context, AppBean bean, int position, CheckedBean checked) {
            mCheckBox.setChecked(checked.isChecked());
            tv_name.setText(bean.getName());
            iv_icon.setImageDrawable(bean.getIcon());
        }
    }


}

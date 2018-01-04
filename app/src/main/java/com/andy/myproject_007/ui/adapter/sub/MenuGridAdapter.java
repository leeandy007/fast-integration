package com.andy.myproject_007.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.bean.AppBean;
import com.andy.myproject_007.ui.adapter.base.BaseInfoAdapter;
import com.andy.myproject_007.ui.adapter.base.BaseInfoViewHolder;
import com.andy.myproject_007.ui.adapter.base.BaseRecyclerAdapter;
import com.andy.myproject_007.ui.adapter.base.BaseRecyclerViewHolder;
import com.andy.myproject_007.ui.adapter.base.ViewHolderCreator;
import com.andy.myproject_007.util.AndroidUtil;

import java.util.List;


/**
 * Created by leeandy007 on 16/9/18.
 */
public class MenuGridAdapter extends BaseInfoAdapter<AppBean> {

    public MenuGridAdapter(List<AppBean> list, ViewHolderCreator mViewHolderCreator) {
        super(list, mViewHolderCreator);
    }

    public static class GridViewHolder extends BaseInfoViewHolder<AppBean> {

        private LinearLayout ll_view;
        private ImageView iv_icon;
        private TextView tv_name;

        @Override
        public int getLayout() {
            return R.layout.adapter_app_grid_item;
        }

        @Override
        public void initView(View view) {
            ll_view = (LinearLayout) view.findViewById(R.id.ll_view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }

        @Override
        public void initData(Context context, AppBean bean, int position) {
            tv_name.setText(bean.getName());
            iv_icon.setImageDrawable(bean.getIcon());
            BindComponentEvent(context, bean);
        }

        protected void BindComponentEvent(Context context, AppBean bean) {
            ll_view.setOnClickListener(new MyOnClickListener(context, bean));
        }

        class MyOnClickListener implements View.OnClickListener{

            private Context context;
            private AppBean bean;

            public MyOnClickListener(Context context, AppBean bean){
                this.context = context;
                this.bean = bean;
            }

            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.ll_view) {
                    AndroidUtil.LaunchApp(context, bean.getPackageName());

                }
            }
        }
    }

}

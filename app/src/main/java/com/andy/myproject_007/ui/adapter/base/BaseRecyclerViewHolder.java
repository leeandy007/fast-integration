package com.andy.myproject_007.ui.adapter.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andy.myproject_007.R;

/**
 * Created by leeandy007 on 2017/6/15.
 */

public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View view) {
        super(view);
        initView(view);
    }

    public abstract void initView(View view);

    public abstract void initData(Context context, T t, int postion);

    public void startActivity(Context context, Class clszz, Bundle bundle){
        Intent intent = new Intent(context, clszz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}

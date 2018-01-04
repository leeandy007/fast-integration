package com.andy.myproject_007.ui.adapter.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.andy.myproject_007.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * Created by leeandy007 on 16/9/18.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected Context _context;
    protected List<T> _list;
    protected ViewHolderCreator mViewHolderCreator;

    public BaseRecyclerAdapter(Context _context, List<T> list, ViewHolderCreator mViewHolderCreator){
        this._context = _context;
        this._list = list;
        this.mViewHolderCreator = mViewHolderCreator;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (BaseRecyclerViewHolder) mViewHolderCreator.createHolder();
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public List<T> getList() {
        return _list;
    }

    public T getItem(int position){
        return _list.get(position);
    }

    public void replaceBean(int position , T t){
        _list.set(position, t);
        this.notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        _list.remove(position);
        this.notifyDataSetChanged();
    }

    public void clearAll() {
        _list.clear();
        this.notifyDataSetChanged();
    }

    public void add(List<T> beans) {
        _list.addAll(beans);
        this.notifyDataSetChanged();
    }

    public void refresh(){
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.initData(_context, getItem(position), position);
    }

    public void animNext(){
        /**<<<------右入左出*/
        ((Activity)_context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public void startActivity(Class clszz, Bundle bundle){
        Intent intent = new Intent(_context, clszz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        _context.startActivity(intent);
        animNext();
    }

    protected void requestPermission(Context context, int requestCode, PermissionListener listener, String... permission) {
        AndPermission.with(context).requestCode(requestCode).permission(permission).callback(listener).start();
    }
}

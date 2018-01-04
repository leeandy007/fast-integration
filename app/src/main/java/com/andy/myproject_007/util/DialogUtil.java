package com.andy.myproject_007.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andy.myproject_007.R;


/**
 * Created by leeandy007 on 2016/11/13.
 */

public class DialogUtil {

    public interface DialogCallBack{

        public void initView(View view);


    }

    /**
     * 返回带进度等待的dialog
     */
    public static Dialog getProgressDialog(Context context, boolean isTouchOutside, boolean btnBackNoEnabled, String tips) {
        Dialog dialog = new Dialog(context, R.style.FullScreenDialog);
        dialog.setCanceledOnTouchOutside(isTouchOutside);
        if(btnBackNoEnabled){
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        View view = View.inflate(context, R.layout.dialog_progress, null);
        ImageView ivloading = (ImageView) view.findViewById(R.id.load);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate_loading);
        ivloading.startAnimation(anim);
        TextView tvContent = (TextView) view.findViewById(R.id.loadText);
        tvContent.setText(tips);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = AndroidUtil.dip2px(context, 150);
        wl.width = AndroidUtil.dip2px(context, 150);
        window.setAttributes(wl);
        return dialog;
    }

    /**
     * 返回自定义布局的dialog
     */
    public static Dialog getDialog(Context context, int resId, boolean isTouchOutside, boolean btnBackNoEnabled, DialogCallBack mDialogCallBack) {
        final Dialog dialog = new Dialog(context, R.style.custom_window_dialog);
        dialog.setCanceledOnTouchOutside(isTouchOutside);
        if(btnBackNoEnabled){
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        View view = View.inflate(context, resId, null);
        view.setTag(dialog);
        mDialogCallBack.initView(view);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(view);
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()-50); //设置宽度
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    /**
     * 返回带listview的dialog
     * */
    public static void getListDialog(Context context, String title, BaseAdapter adapter,
                                     AdapterView.OnItemClickListener listener, boolean isTouchOutside, boolean isShowBtn, boolean isShowCancleBtn) {
        final Dialog dialog = new Dialog(context, R.style.custom_window_dialog);
        dialog.setCanceledOnTouchOutside(isTouchOutside);
        View view = View.inflate(context, R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.dialog_list_tvTitle);
        tvTitle.setText(title);
        ListView listView = (ListView) view.findViewById(R.id.dialog_list_listView);
        listView.setAdapter(adapter);
        listView.setTag(dialog);
        listView.setOnItemClickListener(listener);
        LinearLayout ll_btn = (LinearLayout) view.findViewById(R.id.ll_btn);
        Button btnSure = (Button) view.findViewById(R.id.btnSure);
        Button btnCancle = (Button) view.findViewById(R.id.btnCancle);
        if(isShowBtn){
            ll_btn.setVisibility(View.VISIBLE);
            btnSure.setVisibility(View.VISIBLE);
            if(isShowCancleBtn){
                btnCancle.setVisibility(View.VISIBLE);
            } else {
                btnCancle.setVisibility(View.GONE);
            }
            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (isShowCancleBtn) {
            ll_btn.setVisibility(View.VISIBLE);
            btnCancle.setVisibility(View.VISIBLE);
            if(isShowBtn){
                btnSure.setVisibility(View.VISIBLE);
            } else {
                btnSure.setVisibility(View.GONE);
            }
            btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(view);
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()-50); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 返回自定义布局的dialog
     */
    public static Dialog showDialog(Context context, String title, String content, View.OnClickListener mSureClick, View.OnClickListener mCancleClick, boolean isShowCancle) {
        final Dialog dialog = new Dialog(context, R.style.custom_window_dialog);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context, R.layout.dialog_content, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        Button btnSure = (Button) view.findViewById(R.id.btnSure);
        Button btnCancle = (Button) view.findViewById(R.id.btnCancle);
        btnSure.setTag(dialog);
        btnCancle.setTag(dialog);
        if(!isShowCancle){
            btnCancle.setVisibility(View.GONE);
        }
        tv_title.setText(title);
        tv_content.setText(content);
        btnSure.setOnClickListener(mSureClick);
        btnCancle.setOnClickListener(mCancleClick);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(view);
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()-50); //设置宽度
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    /**
     * 返回自定义布局的dialog
     */
    public static Dialog showDialog(Context context, String title, String content, DialogCallBack mDialogCallBack) {
        final Dialog dialog = new Dialog(context, R.style.custom_window_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        View view = View.inflate(context, R.layout.dialog_content, null);
        view.setTag(dialog);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        Button btnSure = (Button) view.findViewById(R.id.btnSure);
        Button btnCancle = (Button) view.findViewById(R.id.btnCancle);
        LinearLayout ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        ll_bottom.setVisibility(View.GONE);
        tv_title.setText(title);
        tv_content.setText(content);
        mDialogCallBack.initView(view);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(view);
        dialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()-50); //设置宽度
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }


}

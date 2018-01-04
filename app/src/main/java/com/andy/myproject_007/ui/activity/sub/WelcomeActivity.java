package com.andy.myproject_007.ui.activity.sub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.andy.myproject_007.R;
import com.andy.myproject_007.ui.activity.base.BaseBussActivity;

public class WelcomeActivity extends BaseBussActivity {
	
	private Activity _context;
    private ImageView iv_pic;
    protected Animation mFadeInScale;

    @Override
    protected int getLayout() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
        }
        _context = WelcomeActivity.this;
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        mFadeInScale = AnimationUtils.loadAnimation(_context, R.anim.image_scale);
        iv_pic.startAnimation(mFadeInScale);
    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	Intent intent = new Intent(_context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}

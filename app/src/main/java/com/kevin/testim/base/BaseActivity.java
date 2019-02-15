package com.kevin.testim.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.testim.R;
import com.kevin.testim.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2019/1/21<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public abstract class BaseActivity extends AppBaseActivity {
    @BindView(R.id.toolBar)
    public Toolbar toolbar;
    @BindView(R.id.rl_btn_back)
    public RelativeLayout rlBtnBack;
    @BindView(R.id.tvTitle)
    public TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
        initView();
        initListener();
        rlBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initListener();


    public void dismissBackButton() {
        rlBtnBack.setVisibility(View.GONE);
    }

    public void showBackButton() {
        rlBtnBack.setVisibility(View.VISIBLE);
    }

    public void showToast(String msg){
        ToastUtils.showToast(this,msg);
    }
}

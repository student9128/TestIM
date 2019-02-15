package com.kevin.testim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kevin.testim.MainActivity;
import com.kevin.testim.R;
import com.kevin.testim.base.BaseActivity;
import com.kevin.testim.constant.Constants;
import com.kevin.testim.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2019/1/23<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    showToast("登录成功");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    progressBar.setVisibility(View.GONE);
                    break;
                case 1002:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case 1003:
                    progressBar.setVisibility(View.GONE);
                    showToast("登录失败");
                    break;
            }
        }
    };


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        dismissBackButton();
        tvTitle.setText("登录");

    }

    @Override
    protected void initListener() {
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String username = etUsername.getText().toString().trim().toLowerCase();
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    showToast("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("密码不能为空");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                SPUtil.setSP(Constants.USERNAME, this, username);
                EMClient.getInstance().login(username, password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
//                        Looper.prepare();
//                        showToast("登录成功");
//                        Looper.loop();
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        handler.sendEmptyMessage(Constants.STATE_LOGIN_SUCCESSFULLY);
                    }

                    @Override
                    public void onError(int code, String error) {
                        Log.d("Main", code + "::" + error);
                        Log.d("Main", "登录失败");
                        handler.sendEmptyMessage(Constants.STATE_LOGIN_FAILURE);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.i("Main", "progress=" + progress + "\tstatus:" + status);
                        handler.sendEmptyMessage(Constants.STATE_LOGIN_REQUESTING);
                    }
                });
                break;
        }
    }

}

package com.kevin.testim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kevin.testim.R;
import com.kevin.testim.activity.LoginActivity;
import com.kevin.testim.base.BaseFragment;
import com.kevin.testim.constant.Constants;
import com.kevin.testim.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public static SettingFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS, arg);
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {
        tvUsername.setText(SPUtil.getStringSP(Constants.USERNAME, mActivity));

    }

    @Override
    protected void initListener() {
        btnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                progressBar.setVisibility(View.VISIBLE);
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onError(int code, String error) {

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
                break;
        }
    }

}

package com.kevin.testim.base;

import android.support.v7.app.AppCompatActivity;

import com.kevin.testim.util.LogK;
import com.kevin.testim.util.ToastUtils;

/**
 * Created by Kevin on 2019/1/21<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class AppBaseActivity extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();


    public void showToast(String message) {
        ToastUtils.showToast(this, message);
    }

    public void showLongToast(String message) {
        ToastUtils.showLongToast(this, message);
    }

    public void printLoge(String str) {
        LogK.e(TAG, str);
    }

    public void printLogd(String str) {
        LogK.d(TAG, str);
    }

    public void printLogi(String str) {
        LogK.i(TAG, str);
    }

    public void printLogv(String str) {
        LogK.v(TAG, str);
    }

    public void printLogw(String str) {
        LogK.w(TAG, str);
    }
}

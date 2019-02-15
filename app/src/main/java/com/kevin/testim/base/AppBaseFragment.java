package com.kevin.testim.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.kevin.testim.util.LogK;
import com.kevin.testim.util.ToastUtils;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class AppBaseFragment extends Fragment {
    public String TAG = getClass().getSimpleName();
    public FragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();

    }

    public void showToast(String message) {
        ToastUtils.showToast(mActivity, message);
    }

    public void showLongToast(String message) {
        ToastUtils.showLongToast(mActivity, message);
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

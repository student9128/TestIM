package com.kevin.testim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.kevin.testim.R;
import com.kevin.testim.activity.ChatDetailActivity;
import com.kevin.testim.adapter.FriendListAdapter;
import com.kevin.testim.base.BaseFragment;
import com.kevin.testim.constant.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class FriendListFragment extends BaseFragment implements OnRefreshListener {
    @BindView(R.id.rv_recycler_view)
    RecyclerView rvRecyclerView;
    @BindView(R.id.srl_smart_refresh)
    SmartRefreshLayout srlSmartRefresh;
    private List<String> contacts = new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private FriendListAdapter adapter;

    public static FriendListFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS, arg);
        FriendListFragment fragment = new FriendListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        rvRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FriendListAdapter(mActivity, data);
        rvRecyclerView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {
                    printLoge("请求好友列表异常：" + e.getErrorCode() + "--" + e.getDescription());
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(contacts);
                    }
                });
            }
        }).start();


    }

    @Override
    protected void initListener() {
        srlSmartRefresh.setOnRefreshListener(this);
        adapter.setOnItemClickListener(new FriendListAdapter.OnFriendItemClickListener() {
            @Override
            public void onFriendItemClick(int position) {
                String userName = data.get(position);
                Intent intent = new Intent(mActivity, ChatDetailActivity.class);
                intent.putExtra("username", data.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {
                    printLoge("刷新好友列表异常：" + e.getErrorCode() + "--" + e.getDescription());
                    e.printStackTrace();
                }
            }
        }).start();
        adapter.updateData(contacts);
        srlSmartRefresh.finishRefresh();
    }
}

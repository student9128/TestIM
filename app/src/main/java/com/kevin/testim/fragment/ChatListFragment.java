package com.kevin.testim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.kevin.testim.R;
import com.kevin.testim.activity.ChatDetailActivity;
import com.kevin.testim.adapter.ChatListAdapter;
import com.kevin.testim.base.BaseFragment;
import com.kevin.testim.constant.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class ChatListFragment extends BaseFragment implements ChatListAdapter.OnItemClickListener, OnRefreshListener {
    @BindView(R.id.rv_recycler_view)
    RecyclerView rvRecyclerView;
    @BindView(R.id.srl_smart_refresh)
    SmartRefreshLayout srlSmartRefresh;
    private ChatListAdapter adapter;
    private EMChatManager emChatManager;

    private List<EMConversation> conversationList = new ArrayList<>();
    private static ChatListFragment fragment;

    public static ChatListFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS, arg);
        fragment = new ChatListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ChatListFragment getFragment() {
        return fragment;
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView() {
        adapter = new ChatListAdapter(mActivity, conversationList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        rvRecyclerView.setLayoutManager(layoutManager);
        rvRecyclerView.setAdapter(adapter);

        emChatManager = EMClient.getInstance().chatManager();
        Map<String, EMConversation> allConversations = emChatManager.getAllConversations();

        adapter.updateData(loadConversationList());


    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(this);
        srlSmartRefresh.setOnRefreshListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STATE_NET_CONNECTED:
                    break;
                case Constants.STATE_NET_DISCONNECTED:
                    break;
                case Constants.CHAT_LIST_REFRESH:
                    if (adapter != null) {
                        adapter.updateData(loadConversationList());//刷新会话列表
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    /**
     * 刷新会话列表
     */
    public void refreshUI() {
        if (!handler.hasMessages(Constants.CHAT_LIST_REFRESH)) {
            handler.sendEmptyMessage(Constants.CHAT_LIST_REFRESH);
        }
    }


    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }


    @Override
    public void onChatItemClickListener(int position) {
        Intent intent = new Intent(mActivity, ChatDetailActivity.class);
        intent.putExtra("username", conversationList.get(position).conversationId());
        startActivity(intent);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshUI();
        srlSmartRefresh.finishRefresh();
    }
}

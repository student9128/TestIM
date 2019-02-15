package com.kevin.testim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.kevin.testim.R;
import com.kevin.testim.adapter.ChatDetailAdapter;
import com.kevin.testim.base.BaseActivity;
import com.kevin.testim.constant.Constants;
import com.kevin.testim.util.SoftKeyBoardListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2019/1/23<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class ChatDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rv_recycler_view)
    RecyclerView rvRecyclerView;
    @BindView(R.id.et_text_input)
    EditText etTextInput;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.rl_message_box)
    ConstraintLayout rlMessageBox;
    @BindView(R.id.cl_container)
    ConstraintLayout clContainer;
    private ChatDetailAdapter adapter;
    private EMConversation conversation;
    private List<EMMessage> messageData = new ArrayList<>();
    private String username;
    private SoftKeyBoardListener softKeyBoardListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.MESSAGE_RECEIVED:
                    break;
                case Constants.MEESAGE_RECALLED:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_chat_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        tvTitle.setText(username);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
        adapter = new ChatDetailAdapter(this, messageData);
        rvRecyclerView.setLayoutManager(linearLayoutManager);
        rvRecyclerView.setAdapter(adapter);
        conversation = EMClient.getInstance().chatManager().getConversation(username, EMConversation.EMConversationType.Chat, true);
        if (conversation == null) {
            return;
        }
        conversation.markAllMessagesAsRead();//标记已读
        List<EMMessage> allMessages = conversation.getAllMessages();
        adapter.updateData(allMessages);
//        moveToBottom();
        rvRecyclerView.scrollToPosition(messageData.size() - 1);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        setSoftKeyBoardListener();
    }

    private void moveToBottom() {
        rvRecyclerView.smoothScrollToPosition(messageData.size() - 1);
    }

    @Override
    protected void initListener() {
        tvSend.setOnClickListener(this);
        etTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    tvSend.setEnabled(false);
                } else {
                    tvSend.setEnabled(true);

                }
            }
        });
        etTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                printLogd("hasFocus="+hasFocus);
                if (hasFocus) {
                    rvRecyclerView.scrollToPosition(messageData.size()-1);
                }
            }
        });
        etTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    rvRecyclerView.scrollToPosition(messageData.size()-1);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                String content = etTextInput.getText().toString();
                EMMessage message = EMMessage.createTxtSendMessage(content, username);
                EMClient.getInstance().chatManager().sendMessage(message);
                etTextInput.setText("");
                tvSend.setEnabled(false);
                adapter.addMessage(message);
                moveToBottom();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        conversation.markAllMessagesAsRead();
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.addReceivedMessage(messages);
                    moveToBottom();
                }
            });
        }

        @Override
        public void onCmdMessageReceived(final List<EMMessage> messages) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    adapter.addReceivedMessage(messages);
                    moveToBottom();
                }
            });
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled(final List<EMMessage> messages) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.addRecallMessage(messages);
                    moveToBottom();
                }
            });
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };


    private void setSoftKeyBoardListener() {
        //软键盘显示/隐藏监听
        softKeyBoardListener = new SoftKeyBoardListener(this);
        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //此处的height为软键盘的高度
                //软键盘显示，底部发送消息和灰色背景显示，EditText获取焦点
//                rl_sendMessage.setVisibility(View.VISIBLE);
//                shape_bg.setVisibility(View.VISIBLE);
//                editText.setFocusable(true);
//                editText.setFocusableInTouchMode(true);
//                editText.setCursorVisible(true);
//                editText.requestFocus();
                rvRecyclerView.scrollToPosition(messageData.size()-1);
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘隐藏，底部发送消息和灰色背景隐藏，EditText失去焦点
//                rl_sendMessage.setVisibility(View.GONE);
//                shape_bg.setVisibility(View.GONE);
//                editText.setFocusable(false);
//                editText.setFocusableInTouchMode(false);
//                editText.setCursorVisible(false);
            }
        });
    }
}

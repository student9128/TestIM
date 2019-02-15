package com.kevin.testim.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.kevin.testim.R;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private Context context;
    List<EMConversation> conversationList;

    public ChatListAdapter(Context context, List<EMConversation> conversationList) {
        this.context = context;
        this.conversationList = conversationList;
    }

    public ChatListAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<EMConversation> d) {
        conversationList.clear();
        conversationList.addAll(d);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_chat_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        EMConversation emConversation = conversationList.get(position);
        String username = emConversation.conversationId();
        EMMessage lastMessage = emConversation.getLastMessage();
        Log.d("ChatListAdapter", "lastMessage=" + ((EMTextMessageBody) lastMessage.getBody()).getMessage());

        if (emConversation.getType() == EMConversation.EMConversationType.GroupChat) {

        } else if (emConversation.getType() == EMConversation.EMConversationType.ChatRoom) {

        } else {
            //单聊
            holder.tvNickname.setText(username);
            holder.tvMsg.setText(((EMTextMessageBody) lastMessage.getBody()).getMessage());
            holder.tvTime.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            holder.tvMsgUnreadCount.setVisibility(emConversation.getUnreadMsgCount() == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.tvMsgUnreadCount.setText(String.valueOf(emConversation.getUnreadMsgCount()));
        }
        holder.clContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChatItemClickListener(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_nickname)
        TextView tvNickname;
        @BindView(R.id.tv_msg)
        TextView tvMsg;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_msg_unread_count)
        TextView tvMsgUnreadCount;
        @BindView(R.id.container)
        ConstraintLayout clContainer;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onChatItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;

    }
}

package com.kevin.testim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.kevin.testim.R;
import com.kevin.testim.constant.Constants;
import com.kevin.testim.util.SPUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2019/1/23<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.MyViewHolder> {

    private Context context;
    private List<EMMessage> data;

    public ChatDetailAdapter(Context context) {
        this.context = context;
    }

    public ChatDetailAdapter(Context context, List<EMMessage> data) {
        this.context = context;
        this.data = data;
    }

    public void updateData(List<EMMessage> d) {
        data.clear();
        data.addAll(d);
        notifyDataSetChanged();
    }

    public void addMessage(EMMessage message) {
        data.add(message);
        notifyItemRangeInserted(data.size(), 1);
    }

    public void addReceivedMessage(List<EMMessage> messages) {
        data.addAll(messages);
        notifyItemRangeInserted(data.size() - messages.size(), messages.size());
    }

    public void addRecallMessage(List<EMMessage> messages) {
        data.removeAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        String from = data.get(position).getFrom();//获取发送者用户名
        //判断用户名是否和当前账号用户名一样，一样就是发送，不一样就是接收
        return from.equalsIgnoreCase(SPUtil.getStringSP(Constants.USERNAME,context)) ? Constants.CHAT_TYPE_SEND : Constants.CHAT_TYPE_RECEIVE;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        Log.d("Chat", "view" + viewType);
        if (viewType == Constants.CHAT_TYPE_RECEIVE) {
            v = LayoutInflater.from(context).inflate(R.layout.adapter_item_chat_receive, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.adapter_item_chat_send, parent, false);
        }
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvMsg.setText(((EMTextMessageBody) data.get(position).getBody()).getMessage());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_msg)
        TextView tvMsg;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

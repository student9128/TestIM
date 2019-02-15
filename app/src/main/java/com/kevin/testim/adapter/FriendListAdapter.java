package com.kevin.testim.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.kevin.testim.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2019/1/28<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    private Context context;
    private List<String> data;

    public FriendListAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    public void updateData(List<String> d) {
        data.clear();
        data.addAll(d);
        notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_item_friend_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvNickname.setText(data.get(position));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFriendItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_nickname)
        TextView tvNickname;
//        @BindView(R.id.tv_msg)
//        TextView tvMsg;
        @BindView(R.id.container)
        ConstraintLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    private OnFriendItemClickListener listener;
    public interface OnFriendItemClickListener{
        void onFriendItemClick(int position);
    }
    public void setOnItemClickListener(OnFriendItemClickListener l){
        listener = l;
    }
}

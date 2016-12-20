package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tongzhen.mooc.entities.ChatInfo;
import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 16/12/20.
 */
public class ChatContentAdapter extends RecyclerView.Adapter<ChatContentAdapter.ViewHolder> {
    private Context context;
    private List<ChatInfo> chatInfoList;

    public ChatContentAdapter(Context context, List<ChatInfo> chatInfoList) {
        this.context = context;
        this.chatInfoList = chatInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_chat, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        ChatInfo chatInfo = chatInfoList.get(position);
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return chatInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

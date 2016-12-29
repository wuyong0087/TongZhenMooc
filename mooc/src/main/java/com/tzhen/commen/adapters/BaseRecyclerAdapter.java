package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by wuyong on 16/12/29.
 */
public abstract class BaseRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected OnItemClickListener onItemClickListener;
    protected Context context;
    protected LayoutInflater inflater;

    public BaseRecyclerAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setupListener(final T holder, final int position) {
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        setupViews(holder, position);

        setupListener(holder, position);
    }

    protected abstract void setupViews(T holder, int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }
}

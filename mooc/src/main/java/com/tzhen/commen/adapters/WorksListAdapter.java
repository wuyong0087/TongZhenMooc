package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongzhen.mooc.entities.WorksInfo;
import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 16/12/13.
 */
public class WorksListAdapter extends RecyclerView.Adapter<WorksListAdapter.ViewHolder> {
    private List<WorksInfo> worksInfoList;
    private Context context;

    public WorksListAdapter(List<WorksInfo> worksInfoList, Context context) {
        this.worksInfoList = worksInfoList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_works, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return worksInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNickname, tvDate, tvCourse, tvTitle, tvDuration,
        tvShares, tvLikes, tvComments;
        ImageView ivHeader, ivVipIcon, ivWorksCover;
        public ViewHolder(View view) {
            super(view);

            tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvCourse = (TextView) view.findViewById(R.id.tv_curse);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDuration = (TextView) view.findViewById(R.id.tv_duration);

            tvLikes = (TextView) view.findViewById(R.id.tv_likes);
            tvShares = (TextView) view.findViewById(R.id.tv_shares);
            tvComments = (TextView) view.findViewById(R.id.tv_comments);

            ivHeader = (ImageView) view.findViewById(R.id.iv_header);
            ivVipIcon = (ImageView) view.findViewById(R.id.iv_level_icon);
            ivWorksCover = (ImageView) view.findViewById(R.id.iv_cover);
        }
    }
}

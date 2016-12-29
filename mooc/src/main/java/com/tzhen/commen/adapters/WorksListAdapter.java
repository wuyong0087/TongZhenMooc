package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tzhen.commen.utils.CircleTransform;
import com.tzhen.commen.utils.DateUtils;
import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 16/12/13.
 */
public class WorksListAdapter extends BaseRecyclerAdapter<WorksListAdapter.ViewHolder> {
    private List<WorksInfo> worksInfoList;

    public WorksListAdapter(List<WorksInfo> worksInfoList, Context context) {
        super(context);
        this.worksInfoList = worksInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_works, null);
        return new ViewHolder(view);
    }

    protected void setupViews(ViewHolder holder, int position) {
        WorksInfo worksInfo = worksInfoList.get(position);

        Glide.with(context).load(worksInfo.getHead()).transform(new CircleTransform(context)).into(holder.ivHeader);
        Glide.with(context).load(worksInfo.getScreenshot_m()).placeholder(R.drawable.shape_place_holder).into(holder.ivWorksCover);

        holder.tvNickname.setText(worksInfo.getNickname());
        holder.tvCourse.setText(worksInfo.getCourse_title());
        holder.tvDuration.setText(context.getString(R.string.works_length, DateUtils.parseLongToHH_mm_ss(worksInfo.getDuration())));
        holder.tvDate.setText(DateUtils.parseLongToyyyy_MM_dd(worksInfo.getAddtime()));
        holder.tvTitle.setText(worksInfo.getTitle());

        holder.tvComments.setText(worksInfo.getComments() + "");
        holder.tvLikes.setText(worksInfo.getPraise() + "");
        holder.tvShares.setText(worksInfo.getShare() + "");
    }

    @Override
    public int getItemCount() {
        return worksInfoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
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

package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.QuestionInfo;
import com.tzhen.commen.utils.DateUtils;
import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 16/12/29.
 */
public class QAListAdapter extends BaseRecyclerAdapter<QAListAdapter.ViewHolder> {
    private List<QuestionInfo> questionInfoList;

    public QAListAdapter(Context context, List<QuestionInfo> questionInfoList) {
        super(context);
        this.questionInfoList = questionInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_qa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return questionInfoList == null ? 0 : questionInfoList.size();
    }

    @Override
    protected void setupViews(ViewHolder holder, int position) {
        QuestionInfo questionInfo = questionInfoList.get(position);

        Glide.with(context).load(questionInfo.getHead()).into(holder.ivHeader);
        Glide.with(context).load(questionInfo.getImg()).into(holder.ivQuestionCover);
        holder.ivVipIcon.setImageResource(questionInfo.getIs_verify() == 1 ? R.drawable.shape_transparent : R.drawable.vip);

        holder.tvNickname.setText(questionInfo.getNickname());
        holder.tvDate.setText(DateUtils.parseLongToyyyy_MM_dd(questionInfo.getAddtime()));
        holder.tvQuestionContent.setText(questionInfo.getQuestion());
        holder.tvCourse.setText(questionInfo.getCourse_title());

        holder.tvIdeas.setText(questionInfo.getWriting());
        holder.tvWorks.setText(questionInfo.getVideos());
        holder.tvSameAsks.setText(questionInfo.getWatch());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname, tvDate, tvCourse, tvQuestionContent;
        TextView tvWorks, tvIdeas, tvSameAsks;
        ImageView ivHeader, ivVipIcon, ivQuestionCover;

        public ViewHolder(View view) {
            super(view);

            tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvCourse = (TextView) view.findViewById(R.id.tv_curse);
            tvQuestionContent = (TextView) view.findViewById(R.id.tv_question_content);

            tvIdeas = (TextView) view.findViewById(R.id.tv_ideas);
            tvWorks = (TextView) view.findViewById(R.id.tv_works_answers);
            tvSameAsks = (TextView) view.findViewById(R.id.tv_same_asks);

            ivHeader = (ImageView) view.findViewById(R.id.iv_header);
            ivVipIcon = (ImageView) view.findViewById(R.id.iv_level_icon);
            ivQuestionCover = (ImageView) view.findViewById(R.id.iv_question_image);
        }
    }
}

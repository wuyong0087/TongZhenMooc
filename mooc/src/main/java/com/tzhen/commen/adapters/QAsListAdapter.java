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
 * Created by wuyong on 2016/12/24.
 */
public class QAsListAdapter extends RecyclerView.Adapter<QAsListAdapter.ViewHolder> {
    private Context context;
    private List<QuestionInfo> questionInfoList;
    private QAsListAdapter.OnItemClickListener onItemClickListener;

    public QAsListAdapter(Context context, List<QuestionInfo> questionInfoList) {
        this.context = context;
        this.questionInfoList = questionInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_question, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setupViews(holder, position);

        setupItemClickListener(holder);
    }

    private void setupViews(ViewHolder holder, int position) {
        QuestionInfo info = questionInfoList.get(position);

        Glide.with(context).load(info.getHead()).into(holder.ivHeader);
        Glide.with(context).load(info.getImg()).into(holder.ivQuestionCover);

        holder.tvNickname.setText(info.getNickname());
        holder.tvDate.setText(DateUtils.parseLongToyyyy_MM_dd_HH_mm(info.getAddtime()));
        holder.tvQuestionContent.setText(info.getQuestion());
        holder.tvWorksAnswers.setText(info.getVideos() + "");
        holder.tvIdeas.setText(info.getWriting() + "");
        holder.tvSameAsks.setText(info.getWatch() + "");
    }

    @Override
    public int getItemCount() {
        return questionInfoList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setupItemClickListener(final ViewHolder holder) {
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getLayoutPosition());
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivQuestionCover;
        ImageView ivLevelIcon;
        ImageView ivHeader;

        TextView tvNickname, tvDate, tvQuestionContent;
        TextView tvWorksAnswers, tvIdeas, tvSameAsks;

        public ViewHolder(View view) {
            super(view);

            ivQuestionCover = (ImageView) view.findViewById(R.id.iv_question_cover);
            ivLevelIcon = (ImageView) view.findViewById(R.id.iv_level_icon);
            ivHeader = (ImageView) view.findViewById(R.id.iv_header);

            tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvQuestionContent = (TextView) view.findViewById(R.id.tv_question_content);

            tvWorksAnswers = (TextView) view.findViewById(R.id.tv_works_answers);
            tvIdeas = (TextView) view.findViewById(R.id.tv_ideas);
            tvSameAsks = (TextView) view.findViewById(R.id.tv_same_asks);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
}

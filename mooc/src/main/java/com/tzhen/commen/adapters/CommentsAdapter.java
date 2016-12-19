package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.CommentInfo;
import com.tzhen.commen.utils.CircleTransform;
import com.tzhen.commen.utils.DateUtils;
import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 16/12/19.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<CommentInfo> commentInfoList;
    private OnHeaderClickListener onHeaderClickListener;

    public CommentsAdapter(Context context, List<CommentInfo> commentInfoList) {
        this.context = context;
        this.commentInfoList = commentInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentInfo commentInfo = commentInfoList.get(position);

        setupViews(holder, commentInfo);

        setupListener(holder, position);

    }

    private void setupListener(ViewHolder holder, final int position) {
        if (onHeaderClickListener != null){
            holder.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onHeaderClickListener.onHeaderClick(position);
                }
            });
        }
    }

    private void setupViews(ViewHolder holder, CommentInfo commentInfo) {
        holder.tvNickname.setText(commentInfo.getNickname());
        holder.tvAddDate.setText(DateUtils.parseLongToyyyy_MM_dd_HH_mm(commentInfo.getAddtime()));
        holder.tvCommentContent.setText(commentInfo.getContent());

        Glide.with(context).load(commentInfo.getHead())
                .transform(new CircleTransform(context))
                .placeholder(R.drawable.shape_place_holder)
                .into(holder.ivHeader);
    }

    @Override
    public int getItemCount() {
        return commentInfoList.size();
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNickname, tvAddDate, tvCommentContent;
        ImageView ivHeader;
        public ViewHolder(View view) {
            super(view);

            tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
            tvAddDate = (TextView) view.findViewById(R.id.tv_add_date);
            tvCommentContent = (TextView) view.findViewById(R.id.tv_comment_content);

            ivHeader = (ImageView) view.findViewById(R.id.iv_header);
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(int position);
    }
}

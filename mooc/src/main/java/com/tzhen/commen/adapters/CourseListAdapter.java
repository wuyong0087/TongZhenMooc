package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.CourseInfo;
import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 2016/12/21.
 */
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    private Context context;
    private List<CourseInfo> courseInfoList;
    private OnItemClickListener onItemClickListener;

    public CourseListAdapter(Context context, List<CourseInfo> courseInfoList) {
        this.context = context;
        this.courseInfoList = courseInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setupViews(holder, position);

        addListener(holder, position);
    }

    private void addListener(ViewHolder holder, final int position) {
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }

    private void setupViews(ViewHolder holder, int position) {
        CourseInfo courseInfo = courseInfoList.get(position);

        Glide.with(context).load(courseInfo.getCover()).into(holder.ivCourseCover);

        holder.tvTitle.setText(courseInfo.getCourse_title());
        holder.tvSchoolName.setText(courseInfo.getSchool());

        holder.tvVideos.setText(context.getString(R.string.course_videos, courseInfo.getVideos()));
        holder.tvQuestions.setText(context.getString(R.string.course_questions, courseInfo.getQuestions()));
        holder.tvEnrollments.setText(context.getString(R.string.course_enrollments, courseInfo.getEnrollments()));
    }

    @Override
    public int getItemCount() {
        return courseInfoList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCourseCover;
        TextView tvTitle;
        TextView tvSchoolName;
        TextView tvVideos, tvQuestions, tvEnrollments;

        public ViewHolder(View view) {
            super(view);

            ivCourseCover = (ImageView) view.findViewById(R.id.iv_course_cover);

            tvTitle = (TextView) view.findViewById(R.id.tv_course_title);
            tvSchoolName = (TextView) view.findViewById(R.id.tv_school_name);

            tvVideos = (TextView) view.findViewById(R.id.tv_videos);
            tvQuestions = (TextView) view.findViewById(R.id.tv_questions);
            tvEnrollments = (TextView) view.findViewById(R.id.tv_enrollments);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemLongCLick(int position);
    }
}

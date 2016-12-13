package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tongzhen.mooc.entities.WorksInfo;
import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 16/12/13.
 */
public class WorksListAdapter extends RecyclerView.Adapter<WorksListAdapter.ViewHolder> {
    private List<WorksInfo> worksInfos;
    private Context context;

    public WorksListAdapter(List<WorksInfo> worksInfos, Context context) {
        this.worksInfos = worksInfos;
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
        return worksInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

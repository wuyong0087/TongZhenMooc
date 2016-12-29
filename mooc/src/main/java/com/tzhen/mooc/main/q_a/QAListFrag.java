package com.tzhen.mooc.main.q_a;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tongzhen.mooc.entities.QuestionInfo;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.WorksListPresenter;
import com.tongzhen.mooc.views.QuestionListView;
import com.tongzhen.mooc.views.WorksListView;
import com.tzhen.commen.adapters.BaseRecyclerAdapter;
import com.tzhen.commen.adapters.QAListAdapter;
import com.tzhen.commen.adapters.WorksListAdapter;
import com.tzhen.commen.fragment.LazyLoadFrag;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_qa_list)
public abstract class QAListFrag extends LazyLoadFrag<QuestionListInfo> implements QuestionListView, BaseRecyclerAdapter.OnItemClickListener {

    @ViewById(R.id.sl_refresh) protected SwipeRefreshLayout rlRefresh;

    @ViewById(R.id.rcv_qa_list) protected RecyclerView rcvQAList;

    @ViewById(R.id.tv_tips) protected TextView tvTips;

    @Inject protected Persistence persistence;

    @Inject protected Navigator navigator;

    private QAListAdapter qaListAdapter;

    protected int currentPage = 1;

    protected String uid;

    private boolean isLoadMore;

    private List<QuestionInfo> questionInfoList;

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initViews() {
        super.initViews();

        uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);

        setupViews();

        lazyLoad();
    }

    private void setupViews() {

        rlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                loadData();
            }
        });

        isPrepared = true;
    }

    public abstract void loadData();

    @Override
    protected void lazyLoad() {
        if (isVisible && isPrepared && !hasLoadData) {
            loadData();
        }
    }

    @Override
    public void onSuccess(QuestionListInfo value) {
        rlRefresh.setRefreshing(false);
        super.onSuccess(value);
        if (value.getResult() == ResultCodes.OK) {
            hasLoadData = true;
            tvTips.setVisibility(View.GONE);

            if (isLoadMore) {
                questionInfoList.addAll(value.getQuestionInfoList());
            } else {
                questionInfoList = value.getQuestionInfoList();
            }

            qaListAdapter = new QAListAdapter(getContext(), questionInfoList);
            rcvQAList.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvQAList.setAdapter(qaListAdapter);
            qaListAdapter.setOnItemClickListener(this);
        } else {
            if (isLoadMore) {
                showMsg(value.getErrorMsg());
            } else {
                tvTips.setVisibility(View.VISIBLE);
            }
            resetParams();
        }
    }

    @Override
    public void onError(String message, Throwable e) {
        super.onError(message, e);
        resetParams();
        rlRefresh.setRefreshing(false);
    }

    private void resetParams() {
        isLoadMore = false;
        currentPage -= 1;
    }

    @Override
    public void onItemClick(int position) {
        QuestionInfo questionInfo = questionInfoList.get(position);
//        navigator.toWorksInfo(getActivity(), questionInfo.getQid());
    }

    @Override
    public void onItemLongClick(int position) {

    }
}

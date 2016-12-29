package com.tzhen.mooc.fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tongzhen.mooc.entities.QuestionInfo;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.QAListPresenter;
import com.tongzhen.mooc.views.QAListView;
import com.tzhen.commen.adapters.QAsListAdapter;
import com.tzhen.commen.config.AppConfig;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/24.
 */
@EFragment(R.layout.fragment_question_list)
public class QAListFragment extends BaseFragment<QuestionListInfo> implements QAListView, QAsListAdapter.OnItemClickListener {
    @ViewById(R.id.sl_refresh)
    SwipeRefreshLayout rlRefresh;

    @ViewById(R.id.rcv_qa_list)
    RecyclerView rcvWorksList;

    @ViewById(R.id.tv_tips)
    TextView tvTips;

    @Inject
    QAListPresenter presenter;

    @Inject
    Persistence persistence;

    @Inject
    Navigator navigator;

    private QAsListAdapter qaListAdapter;

    private int currentPage = 1;

    private boolean isLoad;

    private List<QuestionInfo> questionInfoList;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        setupViews();

        loadData();
    }

    private void setupViews() {

        rlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                loadData();
            }
        });

        rcvWorksList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int items = qaListAdapter.getItemCount();
                    if (items > (currentPage * AppConfig.PAGE_MAX_ITEM) - 3) {
                        isLoad = true;
                        loadData();
                    }
                }
            }
        });

        isPrepared = true;
    }

    private void loadData() {
        tvTips.setVisibility(View.GONE);
        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, isLoad ? currentPage + 1 : currentPage, AppConfig.PAGE_MAX_ITEM);
    }

    @Override
    public void onSuccess(QuestionListInfo value) {
        rlRefresh.setRefreshing(false);
        super.onSuccess(value);
        if (value.getResult() == ResultCodes.OK) {
            hasLoadData = true;

        } else {
            if (isLoad) {
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
        isLoad = false;
        currentPage -= 1;
    }

    @Override
    public void onItemClick(int position) {
        QuestionInfo info = questionInfoList.get(position);
        navigator.toQuestionDetail(getContext(), info.getQid());
    }

    @Override
    public void onItemLongClick(int position) {

    }
}

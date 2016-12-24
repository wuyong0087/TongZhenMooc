package com.tzhen.mooc.main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.WorksListPresenter;
import com.tongzhen.mooc.views.WorksListView;
import com.tzhen.commen.adapters.WorksListAdapter;
import com.tzhen.commen.config.AppConfig;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_works_list)
public class WorksListFragment extends BaseFragment<WorksListInfo> implements WorksListView, WorksListAdapter.OnItemClickListener {
    private static final String ARG_WORK_LIST_TYPE = "ARG_WORK_LIST_TYPE";

    @ViewById(R.id.sl_refresh)
    SwipeRefreshLayout rlRefresh;

    @ViewById(R.id.rcv_works_list)
    RecyclerView rcvWorksList;

    @ViewById(R.id.tv_tips)
    TextView tvTips;

    @Inject
    WorksListPresenter presenter;

    @Inject
    Persistence persistence;

    @Inject
    Navigator navigator;

    @FragmentArg(ARG_WORK_LIST_TYPE)
    int listType;

    private WorksListAdapter worksListAdapter;

    private int currentPage = 1;

    private boolean isLoad;

    private List<WorksInfo> worksInfoList;

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
                    int items = worksListAdapter.getItemCount();
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
        presenter.loadWorksList(this, uid, isLoad ? currentPage + 1 : currentPage, AppConfig.PAGE_MAX_ITEM, listType);
    }

    @Override
    public void onSuccess(WorksListInfo value) {
        rlRefresh.setRefreshing(false);
        super.onSuccess(value);
        if (value.getResult() == ResultCodes.OK) {
            hasLoadData = true;

            if (isLoad) {
                worksInfoList.addAll(value.getWorksInfoList());
            } else {
                worksInfoList = value.getWorksInfoList();
            }

            worksListAdapter = new WorksListAdapter(worksInfoList, getContext());
            rcvWorksList.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvWorksList.setAdapter(worksListAdapter);
            worksListAdapter.setOnItemClickListener(this);
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
    public void onItemClick(WorksListAdapter.ViewHolder holder, int position) {
        WorksInfo worksInfo = worksInfoList.get(position);
        navigator.toWorksInfo(getActivity(), worksInfo.getVid());
    }

    @Override
    public void onItemLongClick(WorksListAdapter.ViewHolder holder, int position) {

    }
}

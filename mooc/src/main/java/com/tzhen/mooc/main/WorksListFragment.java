package com.tzhen.mooc.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.WorksListPresenter;
import com.tongzhen.mooc.views.WorksListView;
import com.tzhen.commen.adapters.WorksListAdapter;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_works_list)
public class WorksListFragment extends BaseFragment<WorksListInfo> implements WorksListView {
    private static final String ARG_WORK_LIST_TYPE = "ARG_WORK_LIST_TYPE";
    @ViewById(R.id.rcv_works_list)
    RecyclerView rcvWorksList;

    @Inject
    WorksListPresenter presenter;

    @Inject
    Persistence persistence;

    @FragmentArg(ARG_WORK_LIST_TYPE)
    int listType;

    private WorksListAdapter worksListAdapter;

    private int page = 1;
    private int max = 100;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

    }

    @Override
    protected void lazyLoad() {
        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.loadWorksList(this, uid, page, max, listType);
    }

    @Override
    public void onSuccess(WorksListInfo value) {
        if (value.getResult() == ResultCodes.OK){

            worksListAdapter = new WorksListAdapter(value.getWorksInfoList(), getContext().getApplicationContext());
            rcvWorksList.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvWorksList.setAdapter(worksListAdapter);

        } else{
            showMsg(value.getErrorMsg());
        }
    }
}

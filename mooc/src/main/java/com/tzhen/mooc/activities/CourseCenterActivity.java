package com.tzhen.mooc.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tongzhen.mooc.entities.CourseInfo;
import com.tongzhen.mooc.entities.CourseListInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.CourseListPresenter;
import com.tongzhen.mooc.views.CourseListView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.adapters.CourseListAdapter;
import com.tzhen.commen.config.AppConfig;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/21.
 */
@EActivity(R.layout.activity_course_list)
public class CourseCenterActivity extends BaseActivity<CourseListInfo> implements CourseListView, CourseListAdapter.OnItemClickListener {
    private static final String EXTRA_SUBJECT = "EXTRA_SUBJECT";
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.rcv_course_list)
    RecyclerView rcvCourseList;

    @ViewById(R.id.sl_refresh)
    SwipeRefreshLayout slRefresh;

    @Inject
    CourseListPresenter presenter;

    @Inject
    Navigator navigator;

    @Extra(EXTRA_SUBJECT) int subject;

    private int page = 1;

    private CourseListAdapter courseListAdapter;
    private List<CourseInfo> courseInfoList;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        initToolBar(toolbar, true);
        setToolbarTitle(getString(R.string.course_center));

        presenter.attachView(this, subject, page, AppConfig.PAGE_MAX_ITEM);
    }

    @Override
    public void onSuccess(CourseListInfo value) {
        if (ResultCodes.OK == value.getResult()){
            courseInfoList = value.getCourseInfoList();
            courseListAdapter = new CourseListAdapter(this, courseInfoList);

            rcvCourseList.setLayoutManager(new LinearLayoutManager(this));
            rcvCourseList.setAdapter(courseListAdapter);
            courseListAdapter.setOnItemClickListener(this);
        } else{
            showMsg(value.getErrorMsg());
        }
    }

    @Override
    public void onItemClick(int position) {
        CourseInfo courseInfo = courseInfoList.get(position);
        navigator.toCourseInfo(this, courseInfo.getCid());
    }

    @Override
    public void onItemLongCLick(int position) {

    }
}

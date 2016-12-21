package com.tzhen.mooc.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.CourseIntroduceInfo;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.CourseInfoPresenter;
import com.tongzhen.mooc.views.CourseInfoView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.adapters.CourseInfoAdapter;
import com.tzhen.mooc.R;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/21.
 */
@EActivity(R.layout.activity_course_info)
public class CourseInfoActivity extends BaseActivity<CourseIntroduceInfo> implements CourseInfoView {

    private static final String EXTRA_CID = "EXTRA_CID";

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.iv_course_cover)
    ImageView ivCourseCover;

    @ViewById(R.id.tab_types)
    TabLayout tabTypes;

    @ViewById(R.id.vp_course_info)
    ViewPager vpCourseInfo;

    @Extra(EXTRA_CID) int cid;

    @Inject
    CourseInfoPresenter presenter;

    @Inject
    Persistence persistence;

    private String uid;

    private CourseInfoAdapter courseInfoAdapter;
    private List<Fragment> frags;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        setupViews();


        loadCourseInfo();
    }

    private void setupViews() {
        initToolBar(toolbar, true);

        frags = new ArrayList<>();
        courseInfoAdapter = new CourseInfoAdapter(this, getSupportFragmentManager(), frags);
        vpCourseInfo.setAdapter(courseInfoAdapter);

        tabTypes.setupWithViewPager(vpCourseInfo);
    }

    private void loadCourseInfo() {
        uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, cid, uid);
    }

    @Override
    public void onSuccess(CourseIntroduceInfo value) {
        if (ResultCodes.OK == value.getResult()){
            Glide.with(this).load(value.getCover()).into(ivCourseCover);

            setToolbarTitle(value.getTitle());

        } else{
            showMsg(value.getErrorMsg());
        }
    }

    @Override
    public void onGetWorks(WorksListInfo worksListInfo) {

    }

    @Override
    public void onGetQuestions(QuestionListInfo questionListInfo) {

    }
}

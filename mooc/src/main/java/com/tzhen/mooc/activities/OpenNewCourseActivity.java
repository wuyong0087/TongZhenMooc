package com.tzhen.mooc.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.presenters.OpenNewCoursePresenter;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/26.
 */
@EActivity(R.layout.activity_open_new_course)
public class OpenNewCourseActivity extends BaseActivity<BaseInfo> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tv_subject)
    TextView tvSubject;

    @ViewById(R.id.tv_course_title) TextView tvCourseTitle;

    @ViewById(R.id.et_course_description)
    EditText etDescription;

    @ViewById(R.id.iv_course_cover)
    ImageView ivCourseCover;

    @Inject
    OpenNewCoursePresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        initToolBar(toolbar, true);
        setToolbarTitle(getString(R.string.open_new_course));
    }

    @Click({R.id.ll_choose_subject, R.id.ll_course_title, R.id.ll_course_cover})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.ll_choose_subject:

                break;
            case R.id.ll_course_title:

                break;
            case R.id.ll_course_cover:

                break;
            case R.id.btn_submit:

                break;
        }
    }
}

package com.tzhen.mooc.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.entities.types.Gender;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.entities.types.StatusCode;
import com.tongzhen.mooc.presenters.UserInfoPresenter;
import com.tongzhen.mooc.views.UserInfoView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.adapters.BaseRecyclerAdapter;
import com.tzhen.commen.adapters.WorksListAdapter;
import com.tzhen.commen.utils.CircleTransform;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/19.
 */
@EActivity(R.layout.activity_user_info)
public class UserInfoActivity extends BaseActivity<UserInfo> implements UserInfoView, BaseRecyclerAdapter.OnItemClickListener {

    private static final String EXTRA_OID = "EXTRA_OID";

    @ViewById(R.id.toolbar) Toolbar toolbar;

    @ViewById(R.id.iv_header) ImageView ivHeader;

    @ViewById(R.id.tv_nickname) TextView tvNickname;

    @ViewById(R.id.tv_sign_words) TextView tvSignWords;

    @ViewById(R.id.tv_scores) TextView tvScores;

    @ViewById(R.id.tv_follows) TextView tvFollows;

    @ViewById(R.id.tv_fans) TextView tvFans;

    @ViewById(R.id.tv_title) TextView tvTitle;

    @ViewById(R.id.rcv_works_list) RecyclerView rcvWorksList;

    @ViewById(R.id.ll_empty_tips) LinearLayout llEmptyTips;

    @Inject UserInfoPresenter presenter;

    @Inject Persistence persistence;

    @Inject Navigator navigator;

    @Extra(EXTRA_OID) String oid;

    private WorksListAdapter worksListAdapter;

    private List<WorksInfo> worksInfoList;

    private int val;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        initToolBar(toolbar, true);
        setToolbarTitle(getString(R.string.user_profile));

        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, uid, oid);
    }

    @Override
    public void onSuccess(UserInfo value) {
        if (ResultCodes.OK == value.getResult()){
            Glide.with(this).load(value.getHead())
                    .placeholder(R.drawable.test)
                    .into(ivHeader);

            tvNickname.setText(value.getNickname());
            tvSignWords.setText(value.getDescription());
            tvScores.setText(value.getScore() + "");
            tvFollows.setText(value.getFollows() + "");
            tvFans.setText(value.getFans() + "");

            int sex = value.getSex();
            String title = getString(sex == Gender.MALE ? R.string.he : R.string.she);
            tvTitle.setText(getString(R.string.user_shared, title));

            worksInfoList = value.getVideos();
            if (worksInfoList.size() > 1){
                llEmptyTips.setVisibility(View.GONE);
                worksListAdapter = new WorksListAdapter(worksInfoList, this);
                rcvWorksList.setLayoutManager(new LinearLayoutManager(this));
                rcvWorksList.setAdapter(worksListAdapter);
                worksListAdapter.setOnItemClickListener(this);
            } else{
                llEmptyTips.setVisibility(View.VISIBLE);
            }

            val = value.getIs_follows();
        } else {
            showMsg(value.getErrorMsg());
        }
    }

    @Override
    public void onItemClick(int position) {
        WorksInfo worksInfo = worksInfoList.get(position);

        navigator.toWorksInfo(this, worksInfo.getVid());
    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Click({R.id.tv_follow, R.id.tv_chat})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.tv_follow:
                follow();
                break;
            case R.id.tv_chat:
                navigator.toChat(this, oid);
                break;
        }
    }

    private void follow() {
        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.follow(this, oid, uid, val ^ 1);
    }

    @Override
    public void onFollow(BaseInfo baseInfo) {
        if (ResultCodes.OK == baseInfo.getResult()){
            String tips = getString(val == StatusCode.YES ? R.string.un_follow_success : R.string.follow_success);
            showMsg(tips);
            val = val ^ 1;
        } else{
            showMsg(baseInfo.getErrorMsg());
        }
    }
}

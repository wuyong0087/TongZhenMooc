package com.tzhen.mooc.activities;

import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.UserInfoPresenter;
import com.tongzhen.mooc.views.UserInfoView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.utils.CircleTransform;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/19.
 */
@EActivity(R.layout.activity_user_info)
public class UserInfoActivity extends BaseActivity<UserInfo> implements UserInfoView {

    private static final String EXTRA_OID = "EXTRA_OID";
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.iv_header)
    ImageView ivHeader;

    @ViewById(R.id.tv_nickname)
    TextView tvNickname;

    @ViewById(R.id.tv_sign_words) TextView tvSignWords;

    @ViewById(R.id.tv_scores) TextView tvScores;

    @ViewById(R.id.tv_follows) TextView tvFollows;

    @ViewById(R.id.tv_fans) TextView tvFans;

    @Inject
    UserInfoPresenter presenter;

    @Inject
    Persistence persistence;

    @Inject
    Navigator navigator;

    @Extra(EXTRA_OID) String oid;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        initToolBar(toolbar, true);

        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, uid, oid);
    }

    @Override
    public void onSuccess(UserInfo value) {
        if (ResultCodes.OK == value.getResult()){
            Glide.with(this).load(value.getHead()).transform(new CircleTransform(this)).into(ivHeader);

            tvNickname.setText(value.getNickname());
            tvSignWords.setText(value.getDescription());
            tvScores.setText(value.getScore() + "");
            tvFollows.setText(value.getFollows() + "");
            tvFans.setText(value.getFans() + "");
        } else {
            showMsg(value.getErrorMsg());
        }
    }
}

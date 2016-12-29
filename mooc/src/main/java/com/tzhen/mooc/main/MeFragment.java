package com.tzhen.mooc.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.entities.types.StatusCode;
import com.tongzhen.mooc.presenters.MePresenter;
import com.tongzhen.mooc.views.MeView;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.commen.utils.CircleTransform;
import com.tzhen.mooc.R;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_me)
public class MeFragment extends BaseFragment<UserInfo> implements MeView {

    @ViewById(R.id.tv_school_name)
    TextView tvSchoolName;

    @ViewById(R.id.tv_verify_state)
    TextView tvVerifyState;

    @ViewById(R.id.iv_header)
    ImageView ivHeader;

    @ViewById(R.id.tv_nickname) TextView tvNickname;

    @ViewById(R.id.tv_sign_words) TextView tvSignWords;

    @ViewById(R.id.tv_scores) TextView tvScores;

    @ViewById(R.id.tv_follows) TextView tvFollows;

    @ViewById(R.id.tv_fans) TextView tvFans;

    @Inject
    MePresenter presenter;

    @Inject
    Persistence persistence;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, uid);
    }

    @Click({R.id.ll_my_university, R.id.ll_my_videos, R.id.ll_my_notes, R.id.ll_my_qa, R.id.ll_my_shared, R.id.ll_my_favorites})
    public void onViewsClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_university:

                break;
            case R.id.ll_my_videos:

                break;
            case R.id.ll_my_notes:

                break;
            case R.id.ll_my_qa:

                break;
            case R.id.ll_my_shared:

                break;
            case R.id.ll_my_favorites:

                break;
        }
    }

    @Override
    public void onSuccess(UserInfo value) {
        if (ResultCodes.OK == value.getResult()){
            Glide.with(getContext()).load(value.getHead()).transform(new CircleTransform(getContext())).into(ivHeader);

            tvNickname.setText(value.getNickname());
            tvSignWords.setText(value.getDescription());
            tvScores.setText(value.getScore() + "");
            tvFollows.setText(value.getFollows() + "");
            tvFans.setText(value.getFans() + "");
            String verify = getString(value.getIs_verify() == StatusCode.YES ? R.string.verifyed : R.string.un_verify);
            tvVerifyState.setText(getString(R.string.school_verify_state, verify));

        } else{
            showMsg(value.getErrorMsg());
        }
    }
}

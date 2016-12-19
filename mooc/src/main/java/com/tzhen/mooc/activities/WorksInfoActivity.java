package com.tzhen.mooc.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CommentInfo;
import com.tongzhen.mooc.entities.CommentListInfo;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.WorksInfoPresenter;
import com.tongzhen.mooc.views.WorksInfoView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.adapters.CommentsAdapter;
import com.tzhen.commen.utils.CircleTransform;
import com.tzhen.commen.utils.DateUtils;
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
@EActivity(R.layout.activity_works_info)
public class WorksInfoActivity extends BaseActivity<WorksInfo> implements WorksInfoView, CommentsAdapter.OnHeaderClickListener {
    private static final String EXTRA_VID = "EXTRA_VID";

    @ViewById(R.id.iv_works_cover)
    ImageView ivCover;

    @ViewById(R.id.iv_header) ImageView ivHeader;

    @ViewById(R.id.tv_nickname)
    TextView tvNickname;

    @ViewById(R.id.tv_date) TextView tvDate;

    @ViewById(R.id.tv_course) TextView tvCourse;

    @ViewById(R.id.tv_comments) TextView tvComments;

    @ViewById(R.id.rcv_comments)
    RecyclerView rcvComments;

    @ViewById(R.id.et_comment_content)
    EditText etCommentContent;

    @Extra(EXTRA_VID) int vid;

    @Inject
    WorksInfoPresenter presenter;

    @Inject
    Persistence persistence;

    @Inject
    Navigator navigator;

    private List<CommentInfo> commentInfoList;
    private CommentsAdapter adapter;
    private String comment;
    private String uid;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, uid, vid);
    }

    @Override
    public void onSuccess(WorksInfo value) {
        if (ResultCodes.OK == value.getResult()){
            Glide.with(this)
                    .load(value.getHead())
                    .transform(new CircleTransform(this))
                    .placeholder(R.drawable.shape_place_holder)
                    .into(ivHeader);
            Glide.with(this)
                    .load(value.getScreenshot_m())
                    .placeholder(R.drawable.shape_place_holder)
                    .into(ivCover);

            tvNickname.setText(value.getNickname());
            tvComments.setText(getString(R.string.works_info_comments, value.getComments()));
            tvCourse.setText(value.getCourse_title());
            tvDate.setText(DateUtils.parseLongToyyyy_MM_dd_HH_mm(value.getAddtime()));

        } else{
            showMsg(value.toString());
        }
    }

    @Override
    public void onLoadedComments(CommentListInfo commentListInfo) {
        if (ResultCodes.OK == commentListInfo.getResult()){
            commentInfoList = commentListInfo.getCommentInfoList();
            adapter = new CommentsAdapter(this, commentListInfo.getCommentInfoList());
            rcvComments.setLayoutManager(new LinearLayoutManager(this));
            rcvComments.setAdapter(adapter);
            adapter.setOnHeaderClickListener(this);
        } else{
//            showMsg(commentListInfo.getErrorMsg());
        }
    }

    @Override
    public void onCommentPost(BaseInfo baseInfo) {
        hideProgress();
        if (ResultCodes.OK == baseInfo.getResult()){
            presenter.loadComments(this, vid, 1, 100);
        } else{
            showMsg(baseInfo.getErrorMsg());
        }
    }

    @Click(R.id.tv_send)
    public void sendComment(){
        comment = etCommentContent.getText().toString().trim();

        presenter.sendComment(this, vid, uid, comment);
    }

    @Override
    public void onHeaderClick(int position) {
        CommentInfo commentInfo = commentInfoList.get(position);
        navigator.toUserInfo(this, commentInfo.getUid());
    }
}

package com.tzhen.mooc.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CommentInfo;
import com.tongzhen.mooc.entities.CommentListInfo;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.entities.types.StatusCode;
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

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.iv_works_cover)
    ImageView ivCover;

    @ViewById(R.id.iv_header)
    ImageView ivHeader;

    @ViewById(R.id.tv_nickname)
    TextView tvNickname;

    @ViewById(R.id.tv_date)
    TextView tvDate;

    @ViewById(R.id.tv_course)
    TextView tvCourse;

    @ViewById(R.id.tv_comments)
    TextView tvComments;

    @ViewById(R.id.rcv_comments)
    RecyclerView rcvComments;

    @ViewById(R.id.et_comment_content)
    EditText etCommentContent;

    @Extra(EXTRA_VID)
    int vid;

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

    private MenuItem menuItemCollect;
    private MenuItem menuItemLike;
    private int likeVal;
    private int collectVal;
    private String oid;
    private CircleTransform circleTransform;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        initToolBar(toolbar, true);
        setToolbarTitle(getString(R.string.back));

        circleTransform = new CircleTransform(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.works_info_menu, menu);
        menuItemCollect = menu.findItem(R.id.action_collect);
        menuItemLike = menu.findItem(R.id.action_like);

        loadWorksInfo();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_like:
                like();
                break;

            case R.id.action_share:
                share();
                break;

            case R.id.action_collect:
                collect();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(WorksInfo value) {
        if (ResultCodes.OK == value.getResult()) {
            oid = value.getUid();

            Glide.with(this)
                    .load(value.getHead())
                    .transform(circleTransform)
                    .placeholder(R.drawable.test)
                    .into(ivHeader);
            Glide.with(this)
                    .load(value.getScreenshot_m())
                    .placeholder(R.drawable.test)
                    .into(ivCover);

            tvNickname.setText(value.getNickname());
            tvComments.setText(getString(R.string.works_info_comments, value.getComments()));
            tvCourse.setText(value.getCourse_title());
            tvDate.setText(DateUtils.parseLongToyyyy_MM_dd_HH_mm(value.getAddtime()));

            collectVal = value.getIs_collection();
            likeVal = value.getIs_praise();

            setupMenuCollectIcon(collectVal);

            setupMenuLikeIcon(likeVal);
        } else {
            showMsg(value.toString());
        }
    }

    private void loadWorksInfo() {
        uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, uid, vid);
    }

    @Override
    public void onLoadedComments(CommentListInfo commentListInfo) {
        if (ResultCodes.OK == commentListInfo.getResult()) {
            commentInfoList = commentListInfo.getCommentInfoList();
            adapter = new CommentsAdapter(this, commentListInfo.getCommentInfoList());
            rcvComments.setLayoutManager(new LinearLayoutManager(this));
            rcvComments.setAdapter(adapter);
            adapter.setOnHeaderClickListener(this);
        } else {
//            showMsg(commentListInfo.getErrorMsg());
        }
    }

    @Override
    public void onCommentPost(BaseInfo baseInfo) {
        hideProgress();
        if (ResultCodes.OK == baseInfo.getResult()) {
            presenter.loadComments(this, vid, 1, 100);
        } else {
            showMsg(baseInfo.getErrorMsg());
        }
    }

    @Override
    public void onLike(BaseInfo baseInfo) {
        hideProgress();
        if (ResultCodes.OK == baseInfo.getResult()) {
            showMsg(getString(likeVal == StatusCode.NO ? R.string.like_success : R.string.like_cancel_success));
            likeVal = likeVal == StatusCode.NO ? 1 : 0;
            setupMenuLikeIcon(likeVal);
        } else {
            showMsg(getString(R.string.like_failed));
        }
    }

    @Override
    public void onCollect(BaseInfo baseInfo) {
        if (ResultCodes.OK == baseInfo.getResult()) {
            showMsg(getString(collectVal == StatusCode.NO ? R.string.collect_success : R.string.collect_cancel_success));
            collectVal = collectVal == StatusCode.NO ? 1 : 0;
            setupMenuCollectIcon(collectVal);
        } else {
            showMsg(getString(R.string.collect_failed));
        }
    }

    @Click(R.id.tv_send)
    public void sendComment() {
        comment = etCommentContent.getText().toString().trim();

        presenter.sendComment(this, vid, uid, comment);
    }

    @Override
    public void onHeaderClick(int position) {
        String oid = commentInfoList.get(position).getUid();
        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        if (uid.equals(oid)) {

        } else {
            navigator.toUserInfo(this, oid);
        }
    }

    private void collect() {
        presenter.collect(this, vid, uid, collectVal);
    }

    private void share() {

    }

    private void like() {
        presenter.like(this, vid, uid, likeVal);
    }

    private void setupMenuLikeIcon(int likeVal) {

        menuItemLike.setIcon(likeVal == StatusCode.YES ? R.drawable.icon_like : R.drawable.icon_share);
    }

    public void setupMenuCollectIcon(int collectVal) {

        menuItemCollect.setIcon(collectVal == StatusCode.YES ? R.drawable.collect_on : R.drawable.collect);
    }


    @Click({R.id.iv_header})
    public void onViewsClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header:
                navigator.toUserInfo(this, oid);
                break;
        }
    }
}

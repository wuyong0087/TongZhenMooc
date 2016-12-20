package com.tzhen.mooc.activities;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.ChatInfo;
import com.tongzhen.mooc.entities.ChatListInfo;
import com.tongzhen.mooc.entities.ChatParams;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.ChatPresenter;
import com.tongzhen.mooc.views.ChatView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.adapters.ChatContentAdapter;
import com.tzhen.commen.utils.StringUtils;
import com.tzhen.mooc.R;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/20.
 */
@EActivity(R.layout.activity_chat)
public class ChatActivity extends BaseActivity<ChatListInfo> implements ChatView {
    private static final String EXTRA_OID = "EXTRA_OID";
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.rcv_chat_content)
    RecyclerView rcvChatContent;

    @ViewById(R.id.et_chat_content)
    EditText etChatContent;

    @ViewById(R.id.rcv_plus_list) RecyclerView rcvPlusList;

    @Inject
    ChatPresenter presenter;

    @Inject
    Persistence persistence;

    @Extra(EXTRA_OID) String oid;

    private List<ChatInfo> chatInfoList;

    private String uid;

    private String chatContent;

    private ChatContentAdapter chatContentAdapter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        initToolBar(toolbar, true);

        loadChatContent();
    }

    private void loadChatContent() {
        uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        presenter.attachView(this, uid, oid);
    }

    @Click({R.id.tv_send, R.id.iv_plus})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.tv_send:
                sendChat();
                break;
            case R.id.iv_plus:
                int visibility = rcvPlusList.getVisibility();
                rcvPlusList.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private void sendChat() {
        String chatContent = etChatContent.getText().toString().trim();
        if (StringUtils.isEmpty(chatContent)){
            showMsg(getString(R.string.please_input_content));
            return;
        }
        ChatParams params = new ChatParams.Builder()
                .setAddtime(System.currentTimeMillis())
                .setContent(chatContent)
                .setOid(oid)
                .setUid(uid)
                .setType(ChatParams.TEXT)
                .build();
        presenter.sendChatContent(this, params);
    }

    @Override
    public void onSendChat(BaseInfo baseInfo) {
        if (ResultCodes.OK == baseInfo.getResult()){

        } else{
            showMsg(baseInfo.getErrorMsg());
        }
    }

    @Override
    public void onSuccess(ChatListInfo value) {
        if (ResultCodes.OK == value.getResult()){
            chatInfoList = value.getChatInfoList();
        } else{
            showMsg(value.getErrorMsg());
        }
    }

}

package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.ChatListInfo;
import com.tongzhen.mooc.entities.ChatParams;
import com.tongzhen.mooc.interactors.ChatSendUseCase;
import com.tongzhen.mooc.interactors.ChatUseCase;
import com.tongzhen.mooc.views.ChatView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/20.
 */
public class ChatPresenter implements Presenter<ChatView> {
    private ChatUseCase chatUseCase;
    private ChatSendUseCase chatSendUseCase;

    @Inject
    public ChatPresenter(ChatUseCase chatUseCase, ChatSendUseCase chatSendUseCase) {
        this.chatUseCase = chatUseCase;
        this.chatSendUseCase = chatSendUseCase;
    }

    @Override
    public void attachView(ChatView view) {
        chatUseCase.execute(new BaseProgressViewSubscriber<ChatView, ChatListInfo>(view) {
        });
    }

    public void attachView(ChatView view, String uid, String oid) {
        chatUseCase.signParams(oid, uid);
        attachView(view);
    }

    public void sendChatContent(ChatView view, ChatParams params){
        chatSendUseCase.signPrams(params);
        chatSendUseCase.execute(new BaseProgressViewSubscriber<ChatView, BaseInfo>(view) {
            @Override
            public void onNext(BaseInfo baseInfo) {
                view.onSendChat(baseInfo);
            }
        });
    }

    @Override
    public void destroy() {

    }

    @Override
    public void retry() {

    }
}

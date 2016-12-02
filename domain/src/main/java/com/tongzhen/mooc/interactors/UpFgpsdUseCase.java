package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class UpFgpsdUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private String username;
    private String new_psd;

    @Inject
    protected UpFgpsdUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.up_fagpsd(username, new_psd);
    }

    public void signParams(String usernam, String new_psd){
        this.username = usernam;
        this.new_psd = new_psd;
    }
}

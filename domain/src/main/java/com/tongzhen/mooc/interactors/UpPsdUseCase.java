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
public class UpPsdUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private String old_psd;
    private String new_psd;

    @Inject
    protected UpPsdUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.up_psd(uid, old_psd, new_psd);
    }

    public void signParams(String uid, String old_psd, String new_psd){
        this.uid = uid;
        this.old_psd = old_psd;
        this.new_psd = new_psd;
    }
}

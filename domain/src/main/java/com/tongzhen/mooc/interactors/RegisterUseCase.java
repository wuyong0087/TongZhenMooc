package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.RegisterInfo;
import com.tongzhen.mooc.entities.params.RegisterParams;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class RegisterUseCase extends BaseUseCase<RegisterInfo> {
    private final SessionRepository sessionRepository;
    private String username;
    private String password;
    private String nickname;
    private int sex;
    private int country;
    private String description;

    @Inject
    protected RegisterUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<RegisterInfo> buildUseCaseObservable() {
        return sessionRepository.register(username, password, nickname, sex, country, description);
    }

    public void signParams(RegisterParams params){
        this.username = params.getUsername();
        this.password = params.getPassword();
        this.nickname = params.getNickname();
        this.sex = params.getSex();
        this.country = params.getCountry();
        this.description = params.getDescription();
    }
}

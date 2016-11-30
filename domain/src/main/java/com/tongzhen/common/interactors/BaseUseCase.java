package com.tongzhen.common.interactors;

import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public abstract class BaseUseCase<T> {
    private final SubscribeOn subscribeOn;
    private final ObserveOn observeOn;
    private Subscription subscription = Subscriptions.empty();

    protected BaseUseCase(SubscribeOn subscribeOn, ObserveOn observeOn) {
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
    }

    public void execute(Subscriber<T> subscriber) {
        subscription = buildUseCaseObservable()
                .subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler())
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    protected abstract Observable<T> buildUseCaseObservable();

}

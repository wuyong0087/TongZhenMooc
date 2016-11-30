package com.tongzhen.common.schedulers;

import rx.Scheduler;

public interface SubscribeOn {
    Scheduler getScheduler();
}

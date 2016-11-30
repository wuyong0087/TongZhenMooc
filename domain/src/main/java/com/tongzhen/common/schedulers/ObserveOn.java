package com.tongzhen.common.schedulers;

import rx.Scheduler;

public interface ObserveOn {
    Scheduler getScheduler();
}

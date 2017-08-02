package com.jelvix.kotlincodesample.util.bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class RxBus {
    private static RxBus instance;
    private Subject<RxBusEvent> subject = PublishSubject.create();

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    public void sendEvent(RxBusEvent rxBusEvent) {
        subject.onNext(rxBusEvent);
    }

    public Observable<RxBusEvent> getEvents() {
        return subject;
    }

}

package com.jelvix.kotlincodesample.util.bus;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class RxBusEvent {

    public static final String RELATION_CHANGE = "RELATION_CHANGE";
    public static final String COMMENTS_CHANGE = "COMMENTS_CHANGE";

    private String eventKey;
    private Object eventObject;

    public RxBusEvent(String eventKey, Object eventObject) {
        this.eventKey = eventKey;
        this.eventObject = eventObject;
    }

    public String getEventKey() {
        return eventKey;
    }

    public Object getEventObject() {
        return eventObject;
    }
}

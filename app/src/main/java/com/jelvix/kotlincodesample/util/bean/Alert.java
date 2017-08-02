package com.jelvix.kotlincodesample.util.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class Alert {

    public static final int ERROR = 0;
    public static final int SUCCESS = 1;

    @IntDef({ERROR, SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ModeAlert {}

    @ModeAlert
    private int type = ERROR;
    private String message;
    private DoOnSuccess doOnSuccess;
    private DoOnError doOnError;

    public static Builder newBuilder() {
        return new Alert().new Builder();
    }

    private Alert() {
        // private constructor
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public DoOnSuccess getDoOnSuccess() {
        return doOnSuccess;
    }

    public DoOnError getDoOnError() {
        return doOnError;
    }

    public class Builder {

        private Builder() {
            // private constructor
        }

        public Builder setType(@ModeAlert int type) {
            Alert.this.type = type;
            return this;
        }

        public Builder setMessage(String message) {
            Alert.this.message = message;
            return this;
        }

        public Builder setDoOnSuccess(DoOnSuccess doOnSuccess) {
            Alert.this.doOnSuccess = doOnSuccess;
            return this;
        }

        public Builder setDoOnError(DoOnError doOnError) {
            Alert.this.doOnError = doOnError;
            return this;
        }

        public Alert create() {
            return Alert.this;
        }
    }


    /* Functional interface */

    @FunctionalInterface
    public interface DoOnError {
        void doOnError();
    }

    @FunctionalInterface
    public interface DoOnSuccess {
        void doOnSuccess();
    }


}

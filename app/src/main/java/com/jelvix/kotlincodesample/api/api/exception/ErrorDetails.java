package com.jelvix.kotlincodesample.api.api.exception;

import com.google.gson.annotations.SerializedName;
import com.jelvix.kotlincodesample.api.api.entity.Meta;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class ErrorDetails {

    @SerializedName("meta")
    private Meta meta;

    private int code;
    @SerializedName("error_type")
    private String errorType;
    @SerializedName("error_message")
    private String errorMessage;

    public ErrorDetails(Integer code, String error) {
        meta = new Meta();
        meta.setCode(code);
        meta.setErrorMessage(error);
    }

    public static ErrorDetails generateInternalServerError() {
        return new ErrorDetails(500, "Server unavailable");
    }

    public static ErrorDetails generateNoInternetError() {
        return new ErrorDetails(ApiException.CODE_NO_INTERNET, "No internet");
    }

    public String getMessage() {
        return meta != null ? meta.getErrorMessage() : errorMessage;
    }

    public String getErrorType() {
        return meta != null ? meta.getErrorType() : errorType;
    }

    public Integer getCode() {
        return meta != null ? meta.getCode() : code;
    }

}
package com.jelvix.kotlincodesample.api.api.exception;

import com.google.gson.Gson;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class ApiException extends Exception {

    public static final int CODE_NO_INTERNET = -1;
    public static final int CODE_BAD_CREDENTIALS = -2;
    public static final int CODE_API_BAD_REQUEST = 400;
    public static final String TYPE_NO_AUTHORIZED = "OAuthAccessTokenException";
    private final ErrorDetails error;
    private final int errorCode;

    public ApiException(int errorCode, String errorDetails) {
        this.errorCode = errorCode;
        if (errorCode >= 500) {
            error = ErrorDetails.generateInternalServerError();
            return;
        }
        Gson gson = new Gson();
        error = gson.fromJson(errorDetails, ErrorDetails.class);
    }

    public ApiException(int errorCode, ErrorDetails errorDetails) {
        this.errorCode = errorCode;
        this.error = errorDetails;
    }

    public static ApiException generateNoInternetError() {
        return new ApiException(ApiException.CODE_NO_INTERNET, ErrorDetails.generateNoInternetError());
    }

    @Override
    public String getMessage() {
        return error.getMessage();
    }

    public String getErrorType() {
        return error.getErrorType();
    }

    public int getErrorCode() {
        return errorCode;
    }
}

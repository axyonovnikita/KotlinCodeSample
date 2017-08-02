package com.jelvix.kotlincodesample.api;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class ApiConstants {

    private static String FORMAT_URL = "%1$s?client_id=%2$s&redirect_uri=%3$s&response_type=token&display=touch&scope=likes+comments+relationships+follower_list+public_content";

    public static final int DEFAULT_LIMIT = 20;

    public static final String URL_AUTH = "https://api.instagram.com/oauth/authorize/";
    public static final String URL_CALLBACK = "https://jelvix.com";
    public static final String CLIENT_ID = "3ebd84e64121496b8403ce51af3e0e4b";
    public static final String URL_INSTAGRAM_LOGIN = "https://www.instagram.com/accounts/login/";
    public static final String URL_INSTAGRAM_AUTHORIZE = "https://www.instagram.com/oauth/authorize/";


    public static final String FRAGMENT_ACCESS_TOKEN = "access_token";
    public static final String QUERY_ERROR_DESCRIPTION = "error_description";

    public static final String URL_AUTH_FULL = String.format(FORMAT_URL, URL_AUTH, CLIENT_ID, URL_CALLBACK);

    public static final String ACTION_FOLLOW = "follow";
    public static final String ACTION_UNFOLLOW = "unfollow";

    public static final String STATUS_FOLLOWED = "followed_by";
    public static final String STATUS_FOLLOWS = "follows";
    public static final String STATUS_NONE = "none";
}

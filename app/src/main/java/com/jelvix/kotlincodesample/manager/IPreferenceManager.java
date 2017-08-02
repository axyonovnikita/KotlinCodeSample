package com.jelvix.kotlincodesample.manager;

import com.jelvix.kotlincodesample.api.api.entity.User;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface IPreferenceManager {

    boolean isSignedIn();

    void setToken(String token);

    String getToken();

    void setProfile(User user);

    User getProfile();

    String getUserId();

    void logout();
}

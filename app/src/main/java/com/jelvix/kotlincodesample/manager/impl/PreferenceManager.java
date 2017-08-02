package com.jelvix.kotlincodesample.manager.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.preference.PreferenceHelper_;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class PreferenceManager implements IPreferenceManager {

    private final Gson gson;
    private final PreferenceHelper_ preference;

    public PreferenceManager(Context context) {
        this.gson = new Gson();
        preference = new PreferenceHelper_(context);
    }

    @Override
    public String getToken() {
        return preference.accessToken().get();
    }

    @Override
    public void setProfile(User user) {
        preference.edit()
                .profile().put(gson.toJson(user))
                .userId().put(user.getId())
                .apply();
    }

    @Override
    public User getProfile() {
        return gson.fromJson(preference.profile().get(), User.class);
    }

    @Override
    public String getUserId() {
        return preference.userId().get();
    }

    @Override
    public boolean isSignedIn() {
        return preference.isSignedIn().getOr(false);
    }

    @Override
    public void setToken(String token) {
        preference.edit()
                .accessToken().put(token)
                .isSignedIn().put(true)
                .apply();
    }

    @Override
    public void logout() {
        preference.edit()
                .accessToken().remove()
                .profile().remove()
                .userId().remove()
                .isSignedIn().remove()
                .apply();
    }
}

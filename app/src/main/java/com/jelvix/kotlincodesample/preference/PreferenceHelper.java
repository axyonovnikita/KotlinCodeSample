package com.jelvix.kotlincodesample.preference;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface PreferenceHelper {

    boolean isSignedIn();

    String accessToken();

    String profile();

    String userId();

}

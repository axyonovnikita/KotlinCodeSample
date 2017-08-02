package com.jelvix.kotlincodesample.ui.activity.root;

import com.jelvix.kotlincodesample.ui.activity.navigation.INavigatorView;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface IRootNavigator extends INavigatorView {

    void setTitle(CharSequence title);

    void changeAppBarState(boolean isNeedExpand);

    void changeAppBarBehavior(boolean isNeedScroll);
}

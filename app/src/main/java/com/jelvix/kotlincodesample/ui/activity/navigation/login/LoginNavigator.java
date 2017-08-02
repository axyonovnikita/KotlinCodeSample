package com.jelvix.kotlincodesample.ui.activity.navigation.login;

import android.content.Intent;

import com.jelvix.kotlincodesample.ui.activity.base.NavigationActivity;
import com.jelvix.kotlincodesample.ui.activity.navigation.Screens;
import com.jelvix.kotlincodesample.ui.activity.root.RootActivity_;
import com.jelvix.kotlincodesample.ui.fragment.dialog.ChooseDefaultUserBottomDialogFragment_;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class LoginNavigator implements Navigator {

    private NavigationActivity activity;

    public LoginNavigator(NavigationActivity activity) {
        this.activity = activity;
    }

    @Override
    public void applyCommand(Command command) {
        if (command instanceof Forward) {
            Forward forward = (Forward) command;
            String screenKey = forward.getScreenKey();
            switch (screenKey) {
                case Screens.FRAGMENT_DIALOG_DEFAULT_USER:
                    ChooseDefaultUserBottomDialogFragment_.builder().build()
                            .show(activity.getSupportFragmentManager(), screenKey);
                    break;
                case Screens.ACTIVITY_ROOT:
                    RootActivity_.intent(activity)
                            .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .start();
                    break;
            }
        } else if (command instanceof Back) {
            activity.finish();
        }
    }
}

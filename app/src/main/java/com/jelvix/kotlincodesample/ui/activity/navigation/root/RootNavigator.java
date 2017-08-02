package com.jelvix.kotlincodesample.ui.activity.navigation.root;

import android.support.v4.app.Fragment;

import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.activity.base.NavigationActivity;
import com.jelvix.kotlincodesample.ui.activity.navigation.BaseNavigator;
import com.jelvix.kotlincodesample.ui.activity.navigation.Screens;
import com.jelvix.kotlincodesample.ui.fragment.dialog.UnfollowDialogFragment_;
import com.jelvix.kotlincodesample.ui.fragment.root.CommentsFragment_;
import com.jelvix.kotlincodesample.ui.fragment.root.ProfileFragment_;
import com.jelvix.kotlincodesample.ui.fragment.root.RelationFragment_;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.IRelationView;

import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class RootNavigator extends BaseNavigator {

    public RootNavigator(NavigationActivity activity, int containerId) {
        super(activity, containerId);
    }

    @Override
    public void applyCommand(Command command) {
        if (command instanceof Forward) {
            Forward forward = (Forward) command;
            String screenKey = forward.getScreenKey();
            switch (screenKey) {
                case Screens.FRAGMENT_DIALOG_UNFOLLOW:
                    Object data = ((Forward) command).getTransitionData();
                    User user = null;
                    if (data != null) {
                        user = ((UnfollowRoute) data).getUser();
                    }
                    UnfollowDialogFragment_.builder().user(user).build()
                            .show(activity.getSupportFragmentManager(), screenKey);
                    break;
                default:
                    super.applyCommand(command);
            }
        } else {
            super.applyCommand(command);
        }
    }

    @Override
    protected Fragment createFragment(String screenKey, Object data) {
        switch (screenKey) {
            case Screens.FRAGMENT_ROOT_PROFILE:
                User user = null;
                if (data != null) {
                    user = ((ProfileRoute) data).getUser();
                }
                return ProfileFragment_.builder().user(user).build();
            case Screens.FRAGMENT_ROOT_RELATION:
                int relationMode = IRelationView.FOLLOWERS_SCREEN;
                Media relationMedia = null;
                if (data != null) {
                    RelationRoute relationRoute = (RelationRoute) data;
                    relationMode = relationRoute.getRelationMode();
                    relationMedia = relationRoute.getMedia();
                }
                return RelationFragment_.builder().relationMode(relationMode).media(relationMedia).build();
            case Screens.FRAGMENT_ROOT_COMMENTS:
                Media commentMedia = null;
                if (data != null) {
                    commentMedia = ((CommentsRoute) data).getMedia();
                }
                return CommentsFragment_.builder().media(commentMedia).build();
        }
        return null;
    }

    @Override
    protected boolean createActivity(String screenKey, Object data) {
        // TODO: 27.04.17
        return super.createActivity(screenKey, data);
    }
}

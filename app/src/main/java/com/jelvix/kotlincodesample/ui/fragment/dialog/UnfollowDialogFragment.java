package com.jelvix.kotlincodesample.ui.fragment.dialog;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.fragment.base.dialog.BaseComponentDialogFragment;
import com.jelvix.kotlincodesample.ui.fragment.dialog.presenter.IUnfollowDialogView;
import com.jelvix.kotlincodesample.ui.fragment.dialog.presenter.UnfollowDialogPresenter;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.CircleTransformation;
import com.jelvix.kotlincodesample.util.StringHelper;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EFragment(R.layout.fragment_dialog_unfollow)
public class UnfollowDialogFragment extends BaseComponentDialogFragment implements IUnfollowDialogView {

    @InjectPresenter
    UnfollowDialogPresenter unfollowDialogPresenter;

    @ViewById(R.id.userAvatar_imageView)
    protected ImageView userAvatar_imageView;
    @ViewById(R.id.unfollow_textView)
    protected TextView unfollow_textView;

    @FragmentArg
    protected User user;


    /* lifecycle */

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @AfterViews
    protected void afterView() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        CheckNull.check(user.getProfilePicture(), avatarUrl -> {
            Picasso.with(getContext()).load(avatarUrl)
                    .fit()
                    .centerCrop()
                    .noPlaceholder()
                    .transform(new CircleTransformation())
                    .into(userAvatar_imageView);
        });
        String username = "@" + user.getUsername();
        String text = getString(R.string.question_unfollow, username);
        unfollow_textView.setText(StringHelper.makeSubstringBold(text, username));
    }


    /* views */

    @Click(R.id.cancel_button)
    protected void cancel_button_click() {
        dismiss();
    }

    @Click(R.id.unfollow_button)
    protected void unfollow_button_click() {
        unfollowDialogPresenter.unfollowUser(user);
    }


    /* public methods */

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onUserUnfollow() {
        dismiss();
    }
}

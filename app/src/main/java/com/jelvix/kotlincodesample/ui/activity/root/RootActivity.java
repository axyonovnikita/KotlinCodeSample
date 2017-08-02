package com.jelvix.kotlincodesample.ui.activity.root;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.activity.base.NavigationActivity;
import com.jelvix.kotlincodesample.ui.activity.navigation.root.RootNavigator;
import com.jelvix.kotlincodesample.ui.activity.root.presenter.IRootView;
import com.jelvix.kotlincodesample.ui.activity.root.presenter.RootPresenter;
import com.jelvix.kotlincodesample.ui.fragment.root.IRootAppBarListener;
import com.jelvix.kotlincodesample.ui.view.DoubleClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import ru.terrakok.cicerone.Navigator;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EActivity(R.layout.activity_root)
public class RootActivity extends NavigationActivity implements IRootView, IRootNavigator {

    @InjectPresenter
    RootPresenter rootPresenter;

    @InstanceState
    @Extra
    protected User user;

    @ViewById(R.id.back_imageView)
    protected ImageView back_imageView;
    @ViewById(R.id.title_textView)
    protected TextView title_textView;
    @ViewById(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;
    @ViewById(R.id.appBarContent_view)
    protected View appBarContent_view;


    /* navigation */

    @Override
    protected Navigator getNavigator() {
        return new RootNavigator(this, R.id.container);
    }


    /* lifecycle */

    @AfterViews
    protected void afterViews() {
        appBarLayout.setOnTouchListener(onAppBarDoubleClickListener);
        changeBackButtonState();
        getSupportFragmentManager().addOnBackStackChangedListener(this::changeBackButtonState);
    }


    /* views */

    @Click(R.id.back_imageView)
    protected void back_imageView_click() {
        onBackPressed();
    }

    @Click(R.id.logout_imageView)
    protected void logout_imageView_click() {
        logout();
    }


    /* public methods */

    @Override
    public void setTitle(CharSequence title) {
        title_textView.setText(title);
    }

    @Override
    public void changeAppBarState(boolean isNeedExpand) {
        appBarLayout.setExpanded(isNeedExpand);
    }

    @Override
    public void changeAppBarBehavior(boolean isNeedScroll) {
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) appBarContent_view.getLayoutParams();
        if (isNeedScroll) {
            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL + AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS + AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        } else {
            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);
        }
    }


    /* private methods */

    private void changeBackButtonState() {
        boolean isNeedShow = getSupportFragmentManager().getBackStackEntryCount() >= 1;
        back_imageView.setVisibility(isNeedShow ? View.VISIBLE : View.GONE);
        ViewGroup.MarginLayoutParams titleLayoutParams = (ViewGroup.MarginLayoutParams) title_textView.getLayoutParams();
        int titleMargin = isNeedShow ? 0 : getResources().getDimensionPixelOffset(R.dimen.offset_normal1);
        titleLayoutParams.leftMargin = titleMargin;
        titleLayoutParams.rightMargin = titleMargin;
    }


    /* callbacks */

    private DoubleClickListener onAppBarDoubleClickListener = new DoubleClickListener() {
        @Override
        public void onDoubleClick(View v) {
            Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (topFragment instanceof IRootAppBarListener) {
                ((IRootAppBarListener) topFragment).onAppBarDoubleClick();
            }
        }
    };
}

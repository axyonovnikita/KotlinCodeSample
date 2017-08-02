package com.jelvix.kotlincodesample.ui.fragment.root;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.ui.activity.root.IRootNavigator;
import com.jelvix.kotlincodesample.ui.adapter.CommentsRecyclerViewAdapter;
import com.jelvix.kotlincodesample.ui.adapter.holder.CommentHolder;
import com.jelvix.kotlincodesample.ui.fragment.base.BaseComponentFragment;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.CommentsPresenter;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.ICommentsView;
import com.jelvix.kotlincodesample.ui.view.touch.SimpleItemTouchHelperCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EFragment(R.layout.fragment_comments)
public class CommentsFragment extends BaseComponentFragment<IRootNavigator> implements ICommentsView, IRootAppBarListener {

    @InstanceState
    @FragmentArg
    protected Media media;

    @InjectPresenter
    CommentsPresenter commentsPresenter;

    @ProvidePresenter
    CommentsPresenter provideCommentsPresenter() {
        return new CommentsPresenter(media);
    }

    @ViewById(R.id.progressBar)
    protected ProgressBar progressBar;
    @ViewById(R.id.comments_recyclerView)
    protected RecyclerView comments_recyclerView;
    @ViewById(R.id.comment_textInputLayout)
    protected TextInputLayout comment_textInputLayout;
    @ViewById(R.id.comment_editText)
    protected TextInputEditText comment_editText;
    @ViewById(R.id.comment_progressBar)
    protected ProgressBar comment_progressBar;
    @ViewById(R.id.send_imageView)
    protected ImageView send_imageView;


    /* lifecycle */

    @AfterViews
    protected void afterView() {
        getNavigatorView().changeAppBarState(true);
        getNavigatorView().changeAppBarBehavior(false);
        getNavigatorView().setTitle(getString(R.string.comments));
    }

    @Override
    public void onDestroy() {
        getNavigatorView().changeAppBarBehavior(true);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        comments_recyclerView.setAdapter(null);
        super.onDestroyView();
    }


    /* views */

    @Click(R.id.send_imageView)
    protected void send_imageView_click() {
        commentsPresenter.sendComment(comment_editText.getText().toString());
        comment_progressBar.setVisibility(View.VISIBLE);
        send_imageView.setEnabled(false);
        comment_editText.setEnabled(false);
    }


    /* public method */

    @Override
    public void setCommentsAdapter(CommentsRecyclerViewAdapter commentsAdapter) {
        comments_recyclerView.setAdapter(commentsAdapter);
        comments_recyclerView.setItemAnimator(null);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(commentsAdapter, getContext(), ItemTouchHelper.LEFT) {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof CommentHolder && !((CommentHolder) viewHolder).isCanDelete()) {
                    return ItemTouchHelper.ACTION_STATE_IDLE;
                }
                return super.getMovementFlags(recyclerView, viewHolder);
            }
        });
        itemTouchHelper.attachToRecyclerView(comments_recyclerView);
    }

    @Override
    public void scrollToPosition(int position) {
        comments_recyclerView.post(() -> ((LinearLayoutManager) comments_recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0));
    }

    @Override
    public void onSendComment(int position, boolean isSuccess) {
        comment_progressBar.setVisibility(View.GONE);
        send_imageView.setEnabled(true);
        comment_editText.setEnabled(true);
        if (isSuccess) {
            comment_editText.setText(null);
            comments_recyclerView.post(() -> ((LinearLayoutManager) comments_recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0));
        }
    }

    @Override
    public void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAppBarDoubleClick() {
        comments_recyclerView.post(() -> ((LinearLayoutManager) comments_recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0));
    }
}

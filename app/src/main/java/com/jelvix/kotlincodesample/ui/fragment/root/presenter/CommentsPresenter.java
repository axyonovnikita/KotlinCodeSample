package com.jelvix.kotlincodesample.ui.fragment.root.presenter;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.jelvix.kotlincodesample.api.api.entity.Comment;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.api.api.entity.response.CommentResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.CommentsResponse;
import com.jelvix.kotlincodesample.manager.IApiManager;
import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.presenter.base.RxBasePresenter;
import com.jelvix.kotlincodesample.ui.activity.navigation.Screens;
import com.jelvix.kotlincodesample.ui.activity.navigation.root.ProfileRoute;
import com.jelvix.kotlincodesample.ui.adapter.CommentsRecyclerViewAdapter;
import com.jelvix.kotlincodesample.util.bus.RxBus;
import com.jelvix.kotlincodesample.util.bus.RxBusEvent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@InjectViewState(view = ICommentsView.class)
public class CommentsPresenter extends RxBasePresenter<ICommentsView> {

    @Inject
    IApiManager apiManager;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    Context applicationContext;
    @Inject
    Router router;

    private Media media;
    private CommentsRecyclerViewAdapter commentsRecyclerViewAdapter;

    public CommentsPresenter(Media media) {
        getPresenterComponent().inject(this);
        this.media = media;
        String userId = preferenceManager.getUserId();
        commentsRecyclerViewAdapter = new CommentsRecyclerViewAdapter(applicationContext, userId, userId.equals(media.getUser().getId()), commentHandler);
        getViewState().setCommentsAdapter(commentsRecyclerViewAdapter);
        loadComments();
    }

    public void sendComment(String text) {
        addDisposable(start(Schedulers.io(), apiManager.sendComment(media.getId(), text))
                .map(CommentResponse::getComment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(comment -> {
                    media.setCommentCount(media.getComments().getCount() + 1);
                    RxBus.getInstance().sendEvent(new RxBusEvent(RxBusEvent.COMMENTS_CHANGE, media));
                    getViewState().onSendComment(commentsRecyclerViewAdapter.addLastComment(comment), true);
                }, (throwable) -> {
                    getViewState().onSendComment(commentsRecyclerViewAdapter.getItemCount(), false);
                    handleError(throwable);
                }));
    }


    /* private methods */

    private void loadComments() {
        addDisposable(start(Schedulers.io(), apiManager.getMediaComments(media.getId()))
                .map(CommentsResponse::getComments)
                .map(comments -> {
                    Comment firstComment = media.getCaption();
                    if (firstComment != null) {
                        firstComment.setIsCaption(true);
                        comments.add(0, firstComment);
                    }
                    return comments;
                })
                .compose(applySingleProgress())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(comments -> {
                    commentsRecyclerViewAdapter.addNewItemList(comments);
                    getViewState().scrollToPosition(comments.size() - 1);
                }, this::handleError));
    }

    private void deleteComment(Comment comment) {
        addDisposable(start(Schedulers.io(), apiManager.deleteComment(media.getId(), comment.getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> {
                    media.setCommentCount(media.getComments().getCount() - 1);
                    if (comment.isCaption()) {
                        media.setCaption(null);
                    }
                    RxBus.getInstance().sendEvent(new RxBusEvent(RxBusEvent.COMMENTS_CHANGE, media));
                    commentsRecyclerViewAdapter.deleteComment(comment);
                }, (throwable) -> {
                    commentsRecyclerViewAdapter.mergeComment(comment);
                    handleError(throwable);
                }));
    }


    /* callbacks */

    private CommentsRecyclerViewAdapter.CommentHandler commentHandler = new CommentsRecyclerViewAdapter.CommentHandler() {
        @Override
        public void onCommentDelete(Comment comment) {
            deleteComment(comment);
        }

        @Override
        public void onAvatarClick(User user) {
            router.navigateTo(Screens.FRAGMENT_ROOT_PROFILE, new ProfileRoute(user));
        }
    };
}

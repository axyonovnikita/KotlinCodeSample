package com.jelvix.kotlincodesample.ui.fragment.dialog.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.api.api.entity.response.RelationshipResponse;
import com.jelvix.kotlincodesample.manager.IApiManager;
import com.jelvix.kotlincodesample.presenter.base.RxBasePresenter;
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

@InjectViewState(view = IUnfollowDialogView.class)
public class UnfollowDialogPresenter extends RxBasePresenter<IUnfollowDialogView> {

    @Inject
    IApiManager apiManager;
    @Inject
    Router router;

    public UnfollowDialogPresenter() {
        getPresenterComponent().inject(this);
    }


    /* public methods */

    public void unfollowUser(User user) {
        addDisposable(start(Schedulers.io(), apiManager.unfollowUser(user.getId()))
                .map(RelationshipResponse::getRelationship)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(relation -> {
                    user.updateRelationship(relation);
                    RxBus.getInstance().sendEvent(new RxBusEvent(RxBusEvent.RELATION_CHANGE, user));
                    getViewState().onUserUnfollow();
                }, this::handleError));
    }
}

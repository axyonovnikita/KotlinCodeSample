package com.jelvix.kotlincodesample.presenter.base.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;
import com.jelvix.kotlincodesample.presenter.base.IView;

import java.util.Iterator;
import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class AlertStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
        Iterator<ViewCommand<View>> iterator = currentState.iterator();
        while (iterator.hasNext()) {
            ViewCommand<View> entry = iterator.next();

            if (entry.getStrategyType() == AlertStrategy.class) {
                iterator.remove();
                break;
            }
        }
        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
        if (incomingCommand.getTag().equals(IView.ACTION_HIDE)) {
            currentState.remove(incomingCommand);
        }
    }
}

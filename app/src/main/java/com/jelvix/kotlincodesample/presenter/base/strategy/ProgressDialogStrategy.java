package com.jelvix.kotlincodesample.presenter.base.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

import java.util.Iterator;
import java.util.List;

import static com.jelvix.kotlincodesample.presenter.base.IView.ACTION_HIDE;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class ProgressDialogStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
        Iterator<ViewCommand<View>> iterator = currentState.iterator();
        while (iterator.hasNext()) {
            ViewCommand<View> entry = iterator.next();

            if (entry.getStrategyType() == ProgressDialogStrategy.class) {
                iterator.remove();
                break;
            }
        }
        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
        if (incomingCommand.getTag().equals(ACTION_HIDE)) {
            currentState.remove(incomingCommand);
        }
    }
}

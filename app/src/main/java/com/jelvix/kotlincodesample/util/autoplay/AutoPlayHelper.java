package com.jelvix.kotlincodesample.util.autoplay;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.annimon.stream.Stream;
import com.jelvix.kotlincodesample.BuildConfig;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class AutoPlayHelper {

    private static final int MAX_PERCENT = 100;

    private PublishSubject<RecyclerView> scrollSubject = PublishSubject.create();
    private final Rect currentViewRect = new Rect();
    private IAutoPlayViewHolder lastPlayingVideoHolder;

    public AutoPlayHelper() {
        init();
    }


     /* public methods */

    public void onScrollStateChange(RecyclerView recyclerView) {
        scrollSubject.onNext(recyclerView);
    }

    public void stopPlaying() {
        if (lastPlayingVideoHolder != null) {
            lastPlayingVideoHolder.stopPlay();
            lastPlayingVideoHolder = null;
        }
    }

    public void release(RecyclerView recyclerView) {
        Stream.range(0, recyclerView.getAdapter().getItemCount())
                .map(recyclerView::findViewHolderForAdapterPosition)
                .filter(viewHolder -> viewHolder instanceof IAutoPlayViewHolder)
                .map(viewHolder -> (IAutoPlayViewHolder) viewHolder)
                .forEach(IAutoPlayViewHolder::release);
        lastPlayingVideoHolder = null;
        if (scrollSubject != null) {
            scrollSubject.onComplete();
        }
    }


    /* private methods */

    private void init() {
        scrollSubject
                .filter(recyclerView -> {
                    RecyclerView.Adapter recyclerViewAdapter = recyclerView.getAdapter();
                    boolean isAutoPlayAdapter = recyclerViewAdapter instanceof IAutoPlayAdapter;
                    return !isAutoPlayAdapter || ((IAutoPlayAdapter) recyclerViewAdapter).isNeedAutoPlaying();
                })
                .flatMap(recyclerView -> {
                    int firstVisibleItemPositions = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null)[0];
                    int lastVisibleItemPositions = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPositions(null)[0];
                    return getMostVisibleViewHolderObservable(recyclerView, firstVisibleItemPositions, lastVisibleItemPositions);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(viewHolder -> {
                    if (lastPlayingVideoHolder != null && lastPlayingVideoHolder != viewHolder) {
                        lastPlayingVideoHolder.stopPlay();
                        lastPlayingVideoHolder = null;
                    }
                })
                .filter(viewHolder -> viewHolder instanceof IAutoPlayViewHolder)
                .map(viewHolder -> (IAutoPlayViewHolder) viewHolder)
                .filter(viewHolder -> viewHolder != lastPlayingVideoHolder)
                .subscribeOn(Schedulers.io())
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoHolder -> {
                    videoHolder.startPlay();
                    lastPlayingVideoHolder = videoHolder;
                }, throwable -> {
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace();
                    }
                });
    }

    private ObservableSource<? extends RecyclerView.ViewHolder> getMostVisibleViewHolderObservable(RecyclerView recyclerView, int firstVisibleItemPositions, int lastVisibleItemPositions) {
        return Observable.range(firstVisibleItemPositions, lastVisibleItemPositions - firstVisibleItemPositions + 1)
                .map(position -> recyclerView.findViewHolderForLayoutPosition(position).itemView)
                .map(view -> {
                    view.getLocalVisibleRect(currentViewRect);
                    int percents = (currentViewRect.bottom - currentViewRect.top) * MAX_PERCENT / recyclerView.getHeight();
                    return new ViewWithPercentVisibility(percents, view);
                })
                .toList()
                .toObservable()
                .map(viewsWithPercent -> Stream.of(viewsWithPercent).max((o1, o2) -> o1.percents - o2.percents).get().view)
                .map(recyclerView::findContainingViewHolder);
    }


    /* inner classes */

    private class ViewWithPercentVisibility {
        private int percents;
        private View view;

        ViewWithPercentVisibility(int percents, View view) {
            this.percents = percents;
            this.view = view;
        }
    }
}

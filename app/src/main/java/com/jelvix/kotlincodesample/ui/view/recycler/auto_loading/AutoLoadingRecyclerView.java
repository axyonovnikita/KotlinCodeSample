package com.jelvix.kotlincodesample.ui.view.recycler.auto_loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.ApiConstants;
import com.jelvix.kotlincodesample.ui.view.recycler.BackgroundExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class AutoLoadingRecyclerView<Item> extends RecyclerView {

    protected PublishSubject<PaginationInfo> scrollLoadingChannel;
    protected Disposable loadNewItemsDisposable;
    protected Disposable subscribeToLoadingChannelDisposable;
    protected ILoading<Item> iLoading;
    protected AutoLoadingRecyclerViewAdapter<? extends ViewHolder, Item> autoLoadingRecyclerViewAdapter;
    //attr
    protected boolean isFooterEnabled;
    private DoOnError doOnError;

    public AutoLoadingRecyclerView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(null);
        }
    }

    public AutoLoadingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    public AutoLoadingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    /**
     * required method
     * call after init all parameters in AutoLoadedRecyclerView
     */
    public void startLoading(ILoading<Item> itemILoading, DoOnError doOnError) {
        this.doOnError = doOnError;
        setSaveEnabled(true);
        this.iLoading = itemILoading;

        // if all data was loaded then new download is not needed
        PaginationInfo paginationInfo = getPaginationInfo();
        if (paginationInfo.isAllPortionsLoaded()) {
            if (isFooterEnabled) {
                getAdapter().enableFooter(false);
            }
            return;
        }
        // if first portion was loaded then subscribe to LoadingChannel
        if (paginationInfo.isFirstPortionLoaded()) {
            subscribeToLoadingChannel();
        } else {
            loadNewItems(paginationInfo);
        }
    }

    public void unSubscribe() {
        setAdapter(null);
        scrollLoadingChannel.onComplete();
        if (subscribeToLoadingChannelDisposable != null && !subscribeToLoadingChannelDisposable.isDisposed()) {
            subscribeToLoadingChannelDisposable.dispose();
        }
        if (loadNewItemsDisposable != null && !loadNewItemsDisposable.isDisposed()) {
            loadNewItemsDisposable.dispose();
        }
    }

    public void reset() {
        getPaginationInfo().reset();
        restart();
        if (isFooterEnabled) {
            getAdapter().enableFooter(true);
        }
        startLoading(iLoading, doOnError);
    }

    protected void init(@Nullable AttributeSet attrs) {
        restart();
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AutoLoadingRecyclerView);
            try {
                isFooterEnabled = typedArray.getBoolean(R.styleable.AutoLoadingRecyclerView_isFooterEnabled, true);
            } finally {
                typedArray.recycle();
            }
        }
    }

    protected void restart() {
        removeOnScrollListener(scrollingChannelListener);
        scrollLoadingChannel = PublishSubject.create();
        startScrollingChannel();
    }

    protected void startScrollingChannel() {
        addOnScrollListener(scrollingChannelListener);
    }

    private OnScrollListener scrollingChannelListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int position = getLastVisibleItemPosition();
            int updatePosition = getAdapter().getItemCount() - 1 - (ApiConstants.DEFAULT_LIMIT / 2);
            if (position >= updatePosition) {
                scrollLoadingChannel.onNext(getPaginationInfo());
            }
        }
    };

    public int getLastVisibleItemPosition() {
        Class recyclerViewLMClass = getLayoutManager().getClass();
        if (recyclerViewLMClass == LinearLayoutManager.class || LinearLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            return linearLayoutManager.findLastVisibleItemPosition();
        } else if (recyclerViewLMClass == StaggeredGridLayoutManager.class || StaggeredGridLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] into = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            List<Integer> intoList = new ArrayList<>();
            for (int i : into) {
                intoList.add(i);
            }
            return Collections.max(intoList);
        }
        throw new AutoLoadingRecyclerViewExceptions("Unknown LayoutManager class: " + recyclerViewLMClass.toString());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (isInEditMode()) {
            super.setAdapter(adapter);
            return;
        }

        if (adapter instanceof AutoLoadingRecyclerViewAdapter) {
            super.setAdapter(adapter);
        } else {
            throw new AutoLoadingRecyclerViewExceptions("Adapter must be implement IAutoLoadedAdapter");
        }
    }

    public AutoLoadingRecyclerViewAdapter<? extends ViewHolder, Item> getAdapter() {
        if (autoLoadingRecyclerViewAdapter == null) {
            throw new AutoLoadingRecyclerViewExceptions("Null adapter. Please initialise adapter!");
        }
        return autoLoadingRecyclerViewAdapter;
    }

    private PaginationInfo getPaginationInfo() {
        return getAdapter().getPaginationInfo();
    }

    /**
     * required method
     */
    public void setAdapter(AutoLoadingRecyclerViewAdapter<? extends ViewHolder, Item> autoLoadingRecyclerViewAdapter) {
        this.autoLoadingRecyclerViewAdapter = autoLoadingRecyclerViewAdapter;
        if (this.autoLoadingRecyclerViewAdapter != null) {
            this.autoLoadingRecyclerViewAdapter.setFooterEnabled(isFooterEnabled);
        }
        super.setAdapter(autoLoadingRecyclerViewAdapter);
    }

    public ILoading<Item> getLoadingObservable() {
        if (iLoading == null) {
            throw new AutoLoadingRecyclerViewExceptions("Null LoadingObservable. Please initialise LoadingObservable!");
        }
        return iLoading;
    }

    public void setLoadingObservable(ILoading<Item> iLoading) {
        this.iLoading = iLoading;
    }

    protected void subscribeToLoadingChannel() {
        subscribeToLoadingChannelDisposable = scrollLoadingChannel
                .subscribe(offsetAndLimit -> {
                    subscribeToLoadingChannelDisposable.dispose();
                    loadNewItems(offsetAndLimit);
                }, throwable -> {
                    if (doOnError != null) {
                        doOnError.handleError(throwable);
                    }
                });
    }

    protected void loadNewItems(PaginationInfo offsetAndLimit) {
        loadNewItemsDisposable = getLoadingObservable().getLoadingObservable(offsetAndLimit)
                .subscribeOn(Schedulers.from(BackgroundExecutor.getSafeBackgroundExecutor()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemList -> {
                    PaginationInfo paginationInfo = getPaginationInfo();
                    paginationInfo.setFirstPortionLoaded(true);
                    getAdapter().addNewItemList(itemList);
                    getAdapter().notifyItemInserted(getAdapter().getItemCount() - itemList.size());
                    if (paginationInfo.getMaxId() != null) {
                        subscribeToLoadingChannel();
                    } else {
                        if (isFooterEnabled) {
                            getAdapter().enableFooter(false);
                        }
                        paginationInfo.setAllPortionsLoaded(true);
                    }
                }, throwable -> {
                    if (doOnError != null) {
                        doOnError.handleError(throwable);
                    }
                    subscribeToLoadingChannel();
                });
    }

    @Override
    protected void onDetachedFromWindow() {
        unSubscribe();
        super.onDetachedFromWindow();
    }

    @Override
    protected void dispatchSaveInstanceState(final SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    public void setDoOnError(DoOnError doOnError) {
        this.doOnError = doOnError;
    }


    /* Functional interface */

    @FunctionalInterface
    public interface DoOnError {
        void handleError(Throwable throwable);
    }
}

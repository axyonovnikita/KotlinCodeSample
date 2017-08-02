package com.jelvix.kotlincodesample.ui.view.indicator;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.jelvix.kotlincodesample.R;

import icepick.Icepick;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CircleIndicatorRecyclerView extends RecyclerView {

    private static final int MAX_COUNT_DEFAULT = 4;

    private ViewPager viewPager;
    private CircleIndicatorAdapter adapter;
    LinearLayoutManager layoutManager;

    @icepick.State
    int currentPosition;
    @icepick.State
    Integer firstVisiblePosition;
    @icepick.State
    Integer lastVisiblePosition;
    private int indicatorSize;
    private int maxMediaCount;
    private int maxIndicatorCount = MAX_COUNT_DEFAULT;

    public CircleIndicatorRecyclerView(Context context) {
        super(context);
        init();
    }

    public CircleIndicatorRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleIndicatorRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
    }

    protected void init() {
        setFocusable(false);
        setFocusableInTouchMode(false);
        setEnabled(false);
        setSaveEnabled(true);
        setNestedScrollingEnabled(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (adapter != null) {
            changeIndicator(currentPosition);
        }
    }


    /* public methods */

    public void setViewPager(ViewPager viewPager) {
        if (this.viewPager != null) {
            this.viewPager.removeOnPageChangeListener(onPageChangeListener);
        }
        this.viewPager = viewPager;
        initIndicators();
    }

    public void setMaxIndicatorCount(int maxIndicatorCount) {
        this.maxIndicatorCount = maxIndicatorCount;
    }


    /* private methods */

    private void initIndicators() {
        indicatorSize = getContext().getResources().getDimensionPixelSize(R.dimen.indicator_size);
        int mediaCount = viewPager.getAdapter().getCount();
        maxMediaCount = Math.min(mediaCount, maxIndicatorCount);

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = maxMediaCount * indicatorSize;
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(layoutManager);
        adapter = new CircleIndicatorAdapter(mediaCount);
        setAdapter(adapter);
        this.viewPager.addOnPageChangeListener(onPageChangeListener);

        if (firstVisiblePosition == null) {
            firstVisiblePosition = 0;
            lastVisiblePosition = maxMediaCount - 1;
        } else {
            layoutManager.scrollToPositionWithOffset(firstVisiblePosition, 0);
            adapter.setCurrentPosition(currentPosition, firstVisiblePosition, lastVisiblePosition);
        }
    }

    private void changeIndicator(int position) {
        int difference = Math.abs(position - currentPosition);
        if ((position >= currentPosition) && (position >= lastVisiblePosition)) {
            smoothScrollBy(indicatorSize * difference, 0);
            if ((lastVisiblePosition + difference) <= adapter.getItemCount() - 1) {
                firstVisiblePosition += difference;
                lastVisiblePosition += difference;
            } else {
                lastVisiblePosition = adapter.getItemCount() - 1;
                firstVisiblePosition = lastVisiblePosition - maxIndicatorCount + 1;
            }
        } else if ((position <= currentPosition) && (position <= firstVisiblePosition)) {
            smoothScrollBy(-indicatorSize, 0);
            if ((firstVisiblePosition - difference) >= 0) {
                firstVisiblePosition -= difference;
                lastVisiblePosition -= difference;
            } else {
                firstVisiblePosition = 0;
                lastVisiblePosition = firstVisiblePosition + maxMediaCount - 1;
            }
        }
        currentPosition = position;
        adapter.setCurrentPosition(position, firstVisiblePosition, lastVisiblePosition);
    }


    /* callbacks */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //ignored
        }

        @Override
        public void onPageSelected(int position) {
            changeIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //ignored
        }
    };


    /* instance state */

    @Override
    public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
    }
}

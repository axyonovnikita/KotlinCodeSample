package com.jelvix.kotlincodesample.ui.view.indicator;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jelvix.kotlincodesample.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CircleIndicatorAdapter extends RecyclerView.Adapter<CircleIndicatorViewHolder> {

    @IntDef({STATE_SELECTED, STATE_NORMAL, STATE_MEDIUM, STATE_SMALL, STATE_INVISIBLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CircleIndicatorState {}
    public static final int STATE_SELECTED = 0;
    public static final int STATE_NORMAL = 1;
    public static final int STATE_MEDIUM = 2;
    public static final int STATE_SMALL = 3;
    public static final int STATE_INVISIBLE = 4;

    private int mediaCount;
    private int currentPosition;
    private int firstPosition;
    private int lastPosition;

    public CircleIndicatorAdapter(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    @Override
    public CircleIndicatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_indicator, parent, false);
        return new CircleIndicatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CircleIndicatorViewHolder holder, int position) {
        @CircleIndicatorState int state;
        if (position == currentPosition) {
            state = STATE_SELECTED;
        } else if ((firstPosition > 0 && position == (firstPosition + 1))
                || ((lastPosition < (mediaCount - 1)) && position == (lastPosition - 1))) {
            state = STATE_MEDIUM;
        } else if ((firstPosition > 0 && position == firstPosition)
                || ((lastPosition < (mediaCount - 1)) && position == lastPosition)) {
            state = STATE_SMALL;
        } else if (position < firstPosition || position > lastPosition) {
            state = STATE_INVISIBLE;
        } else {
            state = STATE_NORMAL;
        }
        holder.bind(state);
    }

    public void setCurrentPosition(int currentPosition, int firstPosition, int lastPosition) {
        this.currentPosition = currentPosition;
        this.firstPosition = firstPosition;
        this.lastPosition = lastPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mediaCount;
    }
}

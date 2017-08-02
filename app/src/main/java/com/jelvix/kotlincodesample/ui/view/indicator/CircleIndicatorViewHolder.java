package com.jelvix.kotlincodesample.ui.view.indicator;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.jelvix.kotlincodesample.R;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CircleIndicatorViewHolder extends RecyclerView.ViewHolder {

    private final float SCALE_NORMAL = 1f;
    private final float SCALE_MEDIUM = 0.75f;
    private final float SCALE_SMALL = 0.5f;
    private final float SCALE_INVISIBLE = 0.2f;
    private final int ANIMATION_DURATION = 200;

    private ImageView indicator_imageView;
    private Context context;
    private float currentScale;
    @CircleIndicatorAdapter.CircleIndicatorState
    private int currentState;

    public CircleIndicatorViewHolder(View itemView) {
        super(itemView);
        indicator_imageView = (ImageView) itemView.findViewById(R.id.indicator_imageView);
        context = itemView.getContext();
    }

    public void bind(@CircleIndicatorAdapter.CircleIndicatorState int state) {
        indicator_imageView.clearColorFilter();
        final float scale = getScaleByState(state);
        switch (state) {
            case CircleIndicatorAdapter.STATE_SELECTED:
                indicator_imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
                break;
            case CircleIndicatorAdapter.STATE_NORMAL:
            case CircleIndicatorAdapter.STATE_MEDIUM:
            case CircleIndicatorAdapter.STATE_SMALL:
            case CircleIndicatorAdapter.STATE_INVISIBLE:
                indicator_imageView.setColorFilter(ContextCompat.getColor(context, R.color.gray));
                break;
        }
        Animation animation = new ScaleAnimation(currentScale, scale, currentScale, scale,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(ANIMATION_DURATION);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setFillBefore(true);
        indicator_imageView.startAnimation(animation);
        if (currentState == CircleIndicatorAdapter.STATE_INVISIBLE) {
            Animation showAnimation = new AlphaAnimation(0, 1);
            showAnimation.setDuration(ANIMATION_DURATION);
            indicator_imageView.startAnimation(showAnimation);
        }
        if (state == CircleIndicatorAdapter.STATE_INVISIBLE) {
            Animation hideAnimation = new AlphaAnimation(1, 0);
            hideAnimation.setDuration(ANIMATION_DURATION);
            indicator_imageView.startAnimation(hideAnimation);
        }
        currentScale = scale;
        currentState = state;
    }

    private float getScaleByState(@CircleIndicatorAdapter.CircleIndicatorState int state) {
        switch (state) {
            case CircleIndicatorAdapter.STATE_SELECTED:
                return SCALE_NORMAL;
            case CircleIndicatorAdapter.STATE_NORMAL:
                return SCALE_NORMAL;
            case CircleIndicatorAdapter.STATE_MEDIUM:
                return SCALE_MEDIUM;
            case CircleIndicatorAdapter.STATE_SMALL:
                return SCALE_SMALL;
            case CircleIndicatorAdapter.STATE_INVISIBLE:
                return SCALE_INVISIBLE;
            default:
                return SCALE_NORMAL;
        }
    }
}

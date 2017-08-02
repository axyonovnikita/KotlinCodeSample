package com.jelvix.kotlincodesample.ui.adapter.holder.media.full;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.ui.adapter.CarouselPagerAdapter;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;
import com.jelvix.kotlincodesample.ui.view.indicator.CircleIndicatorRecyclerView;
import com.jelvix.kotlincodesample.util.MediaHelper;
import com.jelvix.kotlincodesample.util.autoplay.IAutoPlayViewHolder;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CarouselHolder extends PostHolder implements IAutoPlayViewHolder {

    private static final int INVALID_POSITION = -1;

    private ViewPager carousel_viewPager;
    private CarouselPagerAdapter carouselPagerAdapter;
    private CircleIndicatorRecyclerView indicator_recyclerView;
    private Media media;
    private boolean isNeedPlay;
    private int currentPosition;
    private int lastPlayingPosition = INVALID_POSITION;

    public CarouselHolder(View itemView) {
        super(itemView);
        carousel_viewPager = (ViewPager) itemView.findViewById(R.id.carousel_viewPager);
        indicator_recyclerView = (CircleIndicatorRecyclerView) itemView.findViewById(R.id.indicator_recyclerView);
    }

    @Override
    public void bind(Media media, MediaRecyclerViewAdapter.MediaHandler mediaHandler) {
        super.bind(media, mediaHandler);
        this.media = media;
        int mediaSize = MediaHelper.getDisplayWidth();
        ViewGroup.LayoutParams layoutParams = carousel_viewPager.getLayoutParams();
        layoutParams.height = mediaSize;
        layoutParams.width = mediaSize;
        carouselPagerAdapter = new CarouselPagerAdapter(media.getCarouselMedias(), itemView.getContext());
        carousel_viewPager.setAdapter(carouselPagerAdapter);
        carousel_viewPager.addOnPageChangeListener(onPageChangeListener);
        indicator_recyclerView.setViewPager(carousel_viewPager);
        carousel_viewPager.setCurrentItem(media.getSavedPagerPosition());
    }


    /* auto play methods */

    @Override
    public void startPlay() {
        isNeedPlay = true;
        int currentPosition = media.getSavedPagerPosition();
        carouselPagerAdapter.startPlay(currentPosition);
        lastPlayingPosition = currentPosition;
    }

    @Override
    public void stopPlay() {
        isNeedPlay = false;
        if (lastPlayingPosition == INVALID_POSITION) {
            return;
        }
        carouselPagerAdapter.stopPlay(lastPlayingPosition);
        lastPlayingPosition = INVALID_POSITION;
    }

    @Override
    public void release() {
        carouselPagerAdapter.releaseVideoViews();
    }

    @Override
    public void reset() {
        carouselPagerAdapter.resetVideoView(currentPosition);
    }

    @Override
    public void setup() {
        carouselPagerAdapter.setupVideoView(currentPosition);
    }


    /* callbacks */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //ignored
        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            media.setSavedPagerPosition(position);
            carouselPagerAdapter.stopPlay(lastPlayingPosition);
            lastPlayingPosition = INVALID_POSITION;
            if (isNeedPlay) {
                carouselPagerAdapter.startPlay(position);
                lastPlayingPosition = position;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //ignored
        }
    };
}

package com.jelvix.kotlincodesample.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.devbrackets.android.exomedia.ui.widget.VideoControlsMobile;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.jelvix.kotlincodesample.api.api.entity.CarouselMedia;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.util.BitmapUtils;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.MediaHelper;
import com.jelvix.kotlincodesample.util.VideoHelper;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CarouselPagerAdapter extends PagerAdapter {

    private List<CarouselMedia> carouselList = new ArrayList<>();
    private SparseArray<WeakReference<View>> viewSparseArray = new SparseArray<>();
    private VideoHelper videoHelper = new VideoHelper();

    public CarouselPagerAdapter(List<CarouselMedia> carouselList, Context context) {
        this.carouselList = carouselList;
        Stream.of(carouselList)
                .map(CarouselMedia::getImages)
                .filter(image -> image != null)
                .forEach(image -> Picasso.with(context).load(image.getStandardResolution().getUrl()).fetch());
    }

    @Override
    public int getCount() {
        return carouselList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view;
        CarouselMedia carousel = carouselList.get(position);
        Context context = container.getContext();
        int mediaSize = MediaHelper.getDisplayWidth();

        ViewGroup.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.height = ViewPager.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        switch (carousel.getMediaType()) {
            case Media.TYPE_IMAGE:
                view = new ImageView(context);
                view.setLayoutParams(layoutParams);
                ((ImageView) view).setImageBitmap(BitmapUtils.getTransparentPlaceholder(mediaSize, mediaSize));
                CheckNull.check(carousel.getImages().getStandardResolution().getUrl(), mediaUrl -> {
                    Picasso.with(context).load(mediaUrl)
                            .fit()
                            .centerCrop()
                            .noPlaceholder()
                            .into((ImageView) view);
                });
                break;
            case Media.TYPE_VIDEO:
                view = new VideoView(context);
                view.setLayoutParams(layoutParams);
                ((VideoView) view).setControls(new VideoControlsMobile(context));
                videoHelper.setupVideoView((VideoView) view, carouselList.get(position).getVideos());
                break;
            default:
                view = new View(context);
        }
        viewSparseArray.put(position, new WeakReference<>(view));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof VideoView) {
            videoHelper.releaseVideoView((VideoView) object, carouselList.get(position).getVideos());
        }
        viewSparseArray.remove(position);
        container.removeView((View) object);
    }


     /* auto play methods */

     public void setupVideoView(int currentPosition) {
         View view = getViewByPosition(currentPosition);
         if (view instanceof VideoView) {
             videoHelper.setupVideoView((VideoView) view, carouselList.get(currentPosition).getVideos());
         }
     }

     public void resetVideoView(int currentPosition) {
         View view = getViewByPosition(currentPosition);
         if (view instanceof VideoView) {
             videoHelper.resetVideoView((VideoView) view, carouselList.get(currentPosition).getVideos());
         }
     }

    public void startPlay(int currentPosition) {
        View view = getViewByPosition(currentPosition);
        if (view instanceof VideoView) {
            videoHelper.startPlay((VideoView) view, carouselList.get(currentPosition).getVideos());
        }
    }

    public void stopPlay(int lastPlayingPosition) {
        View view = getViewByPosition(lastPlayingPosition);
        if (view instanceof VideoView) {
            videoHelper.stopPlay((VideoView) view, carouselList.get(lastPlayingPosition).getVideos());
        }
    }

    public void releaseVideoViews() {
        Stream.range(0, viewSparseArray.size())
                .map(integer -> viewSparseArray.keyAt(integer))
                .forEach(key -> {
                    View view = getViewByPosition(key);
                    if (view instanceof VideoView) {
                        videoHelper.releaseVideoView((VideoView) view, carouselList.get(key).getVideos());
                    }
                });
    }


    /* private methods */

    @Nullable
    private View getViewByPosition(int position) {
        WeakReference<View> viewWeakReference = viewSparseArray.get(position);
        return viewWeakReference == null ? null : viewWeakReference.get();
    }
}

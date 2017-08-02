package com.jelvix.kotlincodesample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.adapter.holder.ProfileHolder;
import com.jelvix.kotlincodesample.ui.adapter.holder.media.PostSpanHolder;
import com.jelvix.kotlincodesample.ui.adapter.holder.media.full.CarouselHolder;
import com.jelvix.kotlincodesample.ui.adapter.holder.media.full.ImageHolder;
import com.jelvix.kotlincodesample.ui.adapter.holder.media.full.PostHolder;
import com.jelvix.kotlincodesample.ui.adapter.holder.media.full.VideoHolder;
import com.jelvix.kotlincodesample.ui.view.recycler.auto_loading.AutoLoadingRecyclerViewAdapter;
import com.jelvix.kotlincodesample.util.autoplay.IAutoPlayAdapter;
import com.jelvix.kotlincodesample.util.autoplay.IAutoPlayViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class MediaRecyclerViewAdapter extends AutoLoadingRecyclerViewAdapter<RecyclerView.ViewHolder, Media> implements IAutoPlayAdapter {

    public static final int FULL_MODE_SPAN_COUNT = 1;
    public static final int SPAN_MODE_SPAN_COUNT = 3;

    protected final int TYPE_HEADER = 3;
    private User user;
    private boolean isFullMode = true;
    private Context applicationContext;
    private String ownerId;
    private ProfileHandler profileHandler;
    private MediaHandler mediaHandler;

    public MediaRecyclerViewAdapter(Context applicationContext, String ownerId, ProfileHandler profileHandler, MediaHandler mediaHandler) {
        this.applicationContext = applicationContext;
        this.ownerId = ownerId;
        this.profileHandler = profileHandler;
        this.mediaHandler = mediaHandler;
    }


    /* lifecycle */

    @Override
    public RecyclerView.ViewHolder onNestedCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
            ProfileHolder profileHolder = new ProfileHolder(view);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) profileHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            profileHolder.itemView.setLayoutParams(layoutParams);
            return profileHolder;
        } else if (viewType == Media.TYPE_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_post_image, parent, false);
            return new ImageHolder(view);
        } else if (viewType == Media.TYPE_VIDEO) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_post_video, parent, false);
            return new VideoHolder(view);
        } else if (viewType == Media.TYPE_CAROUSEL) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_post_carousel, parent, false);
            return new CarouselHolder(view);
        } else if (viewType == Media.TYPE_IMAGE_SPAN || viewType == Media.TYPE_VIDEO_SPAN || viewType == Media.TYPE_CAROUSEL_SPAN) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_post_span, parent, false);
            return new PostSpanHolder(view);
        } else {
            throw new RuntimeException("Incompatible media type");
        }
    }

    @Override
    public void onNestedBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProfileHolder) {
            ((ProfileHolder) holder).bind(user, ownerId, isFullMode, profileHandler);
        } else if (holder instanceof PostHolder) {
            Media media = itemList.get(position - 1);
            ((PostHolder) holder).bind(media, mediaHandler);
        } else if (holder instanceof PostSpanHolder) {
            Media media = itemList.get(position - 1);
            ((PostSpanHolder) holder).bind(media, mediaHandler);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (isFullMode && holder instanceof IAutoPlayViewHolder) {
            ((IAutoPlayViewHolder) holder).setup();
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (isFullMode && holder instanceof IAutoPlayViewHolder) {
            ((IAutoPlayViewHolder) holder).reset();
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            int itemViewType = super.getItemViewType(position - 1);
            if (itemViewType == VIEW_TYPE_ITEM) {
                Media currentMedia = itemList.get(position - 1);
                return currentMedia.getMediaType(isFullMode);
            } else {
                return itemViewType;
            }
        }
    }


    /* public methods */

    @Override
    public void addNewItemList(List<Media> medias) {
        Stream.of(medias).forEach(post -> Picasso.with(applicationContext).load(post.getImages().getStandardResolution().getUrl()).fetch());
        super.addNewItemList(medias);
    }

    public void updateUser(User user) {
        this.user = user;
        notifyItemChanged(0);
    }

    public void updateMedia(Media media) {
        if (itemList.contains(media)) {
            int position = itemList.indexOf(media);
            itemList.remove(media);
            itemList.add(position, media);
        }
    }

    public void setMode(boolean fullMode) {
        this.isFullMode = fullMode;
        notifyDataSetChanged();
    }

    @Override
    public boolean isNeedAutoPlaying() {
        return isFullMode;
    }


    /* inner classes */

    public interface ProfileHandler {
        void onPostsClick();
        void onFollowersClick();
        void onFollowingClick();
        void onChangeMode(boolean isFullMode);
        void onChangeFollowingState(String outgoingStatus);
    }

    public interface MediaHandler {
        void onSmallMediaClick(Media media);
        void onLikeCountClick(Media media);
        void onLikeClick(Media media, PostHolder.LikeStatusHandler likeStatusHandler);
        void onCommentCountClick(Media media);
    }
}

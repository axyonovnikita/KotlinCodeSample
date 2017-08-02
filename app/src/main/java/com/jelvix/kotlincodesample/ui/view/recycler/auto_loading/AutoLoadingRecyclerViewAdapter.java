package com.jelvix.kotlincodesample.ui.view.recycler.auto_loading;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.jelvix.kotlincodesample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 *
 * Adapter for {@link AutoLoadingRecyclerView AutoLoadingRecyclerView}
 */

public abstract class AutoLoadingRecyclerViewAdapter<Holder extends RecyclerView.ViewHolder, Item> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final int VIEW_TYPE_PROGRESSBAR = 0;
    protected final int VIEW_TYPE_ITEM = 1;
    protected final int VIEW_EMPTY = 2;
    public final List<Item> itemList = new ArrayList<>();
    public boolean isFooterEnabled = true;
    @Nullable
    protected final EmptyItem emptyItem;
    private PaginationInfo paginationInfo = new PaginationInfo();

    public AutoLoadingRecyclerViewAdapter(@Nullable EmptyItem emptyItem) {
        this.emptyItem = emptyItem;
    }

    public AutoLoadingRecyclerViewAdapter() {
        this(null);
    }

    public PaginationInfo getPaginationInfo() {
        return paginationInfo;
    }

    public void updatePaginationKeys(String maxId) {
        this.paginationInfo.setMaxId(maxId);
    }

    public void setFooterEnabled(boolean footerEnabled) {
        isFooterEnabled = footerEnabled;
    }

    public void addNewItemList(List<Item> items) {
        items.removeAll(itemList);
        itemList.addAll(items);
    }

    public void mergeNewItemList(List<Item> items) {
        Stream.of(items).forEach(item -> {
            if (itemList.contains(item)) {
                int position = itemList.indexOf(item);
                itemList.remove(position);
                itemList.add(position, item);
            } else {
                itemList.add(item);
            }
        });
        enableFooter(false);
    }

    public void mergeItem(Item item) {
        int position = itemList.indexOf(item);
        itemList.set(position, item);
        notifyItemChanged(position);
    }

    public int getItemPosition(Item item) {
        return itemList.indexOf(item);
    }

    public void removeByPosition(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearItems() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public List<Item> getItems() {
        return itemList;
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = position >= itemList.size() ? VIEW_TYPE_PROGRESSBAR : VIEW_TYPE_ITEM;
        if (viewType == VIEW_TYPE_PROGRESSBAR) {
            viewType = isFooterEnabled ? VIEW_TYPE_PROGRESSBAR : VIEW_EMPTY;
        }

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_PROGRESSBAR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
            viewHolder = new ProgressViewHolder(view);
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        } else if (viewType == VIEW_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            viewHolder = new EmptyViewHolder(view);
        } else {
            viewHolder = onNestedCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bindEmptyItem(emptyItem);
        } else {
            onNestedBindViewHolder((Holder) holder, position);
        }
    }

    public abstract Holder onNestedCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onNestedBindViewHolder(Holder holder, int position);

    public void enableFooter(boolean isEnabled) {
        this.isFooterEnabled = isEnabled;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        boolean isProgressBar = isFooterEnabled;
        boolean isEmptyItem = (itemList.size() == 0 && emptyItem != null);

        return (isProgressBar || isEmptyItem) ? itemList.size() + 1 : itemList.size();
    }


    /* Inner Classes */

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public final ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public EmptyViewHolder(View item) {
            super(item);
            textView = (TextView) item.findViewById(R.id.textView);
        }

        public void bindEmptyItem(EmptyItem emptyItem) {
            if (emptyItem.stringRes != null) {
                textView.setText(emptyItem.stringRes);
            } else if (emptyItem.text != null) {
                textView.setText(emptyItem.text);
            }
        }
    }

    public static class EmptyItem {

        @Nullable
        @StringRes
        private Integer stringRes;
        @Nullable
        private String text;

        public EmptyItem(@StringRes @NonNull Integer stringRes) {
            this.stringRes = stringRes;
        }

        public EmptyItem(@NonNull String text) {
            this.text = text;
        }
    }
}

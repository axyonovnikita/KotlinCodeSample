package com.jelvix.kotlincodesample.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.adapter.holder.RelationHolder;
import com.jelvix.kotlincodesample.ui.view.recycler.auto_loading.AutoLoadingRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class RelationRecyclerViewAdapter extends AutoLoadingRecyclerViewAdapter<RelationHolder, User> {

    private Context applicationContext;
    private RelationHandler profileHandler;
    private String ownerId;

    public RelationRecyclerViewAdapter(Context applicationContext, String ownerId, RelationHandler profileHandler) {
        this.applicationContext = applicationContext;
        this.ownerId = ownerId;
        this.profileHandler = profileHandler;
    }

    @Override
    public RelationHolder onNestedCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relation, parent, false);
        return new RelationHolder(view);
    }

    @Override
    public void onNestedBindViewHolder(RelationHolder holder, int position) {
        User currentUser = itemList.get(position);
        holder.bind(currentUser, currentUser.getId().equals(ownerId), profileHandler);
    }

    @Override
    public void addNewItemList(List<User> users) {
        super.addNewItemList(users);
        Stream.of(users).forEach(user -> Picasso.with(applicationContext).load(user.getProfilePicture()).fetch());
    }

    public void updateRelation(User user) {
        if (itemList.contains(user)) {
            int position = itemList.indexOf(user);
            itemList.remove(position);
            itemList.add(position, user);
            notifyItemChanged(position);
        }
    }


    /* inner classes */

    public interface RelationHandler {
        void onItemClick(User user);

        void onFollowButtonClick(User user);
    }
}

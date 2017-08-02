package com.jelvix.kotlincodesample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Comment;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.adapter.holder.CommentHolder;
import com.jelvix.kotlincodesample.ui.view.touch.ItemTouchHelperAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentHolder> implements ItemTouchHelperAdapter {

    private Context applicationContext;
    private String ownerId;
    private boolean isMediaOwner;
    private List<Comment> itemList = new ArrayList<>();
    private CommentHandler commentHandler;

    public CommentsRecyclerViewAdapter(Context applicationContext, String ownerId, boolean isMediaOwner, CommentHandler commentHandler) {
        this.commentHandler = commentHandler;
        this.applicationContext = applicationContext;
        this.ownerId = ownerId;
        this.isMediaOwner = isMediaOwner;
    }

    public void addNewItemList(List<Comment> comments) {
        itemList.addAll(comments);
        Stream.of(comments).forEach(comment -> Picasso.with(applicationContext).load(comment.getFrom().getProfilePicture()).fetch());
        notifyDataSetChanged();
    }

    public int addLastComment(Comment comment) {
        int position = itemList.size();
        itemList.add(position, comment);
        notifyItemInserted(position);
        return position;
    }

    public void deleteComment(Comment comment) {
        int position = itemList.indexOf(comment);
        itemList.remove(comment);
        notifyItemRemoved(position);
    }

    public void mergeComment(Comment comment) {
        if (itemList.contains(comment)) {
            int position = itemList.indexOf(comment);
            itemList.get(position).updateComment(comment);
            notifyDataSetChanged();
        }
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment currentComment = itemList.get(position);
        boolean isCanDelete = isMediaOwner || currentComment.getFrom().getId().equals(ownerId);
        holder.bind(currentComment, isCanDelete, commentHandler);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        commentHandler.onCommentDelete(itemList.get(position));
    }


    /* inner classes */

    public interface CommentHandler {
        void onCommentDelete(Comment comment);

        void onAvatarClick(User user);
    }
}

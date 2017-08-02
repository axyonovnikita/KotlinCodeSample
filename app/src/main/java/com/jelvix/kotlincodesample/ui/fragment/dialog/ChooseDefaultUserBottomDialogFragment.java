package com.jelvix.kotlincodesample.ui.fragment.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.ui.activity.login.LoginActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EFragment(R.layout.fragment_bottom_sheet_container)
public class ChooseDefaultUserBottomDialogFragment extends BottomSheetDialogFragment {

    private ArrayList<BottomSheetRowItem> userList = new ArrayList<>();

    @ViewById(R.id.container)
    protected LinearLayout container;


    /* lifecycle */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList.add(new BottomSheetRowItem("Jelvix User1", "jelvixuser1", "User911"));
        userList.add(new BottomSheetRowItem("Jelvix User2", "jelvixuser2", "User911"));
        userList.add(new BottomSheetRowItem("Jelvix User3", "jelvixuser3", "User911"));
        userList.add(new BottomSheetRowItem("Jelvix User4", "jelvixuser4", "User911"));
        userList.add(new BottomSheetRowItem("Jelvix User5", "jelvixuser5", "User911"));
    }

    @AfterViews
    protected void afterViews() {
        for (BottomSheetRowItem bottomSheetRowItem : userList) {
            View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_default_user, container, false);
            final TextView textView = (TextView) rowView.findViewById(R.id.userName_textView);
            textView.setText(bottomSheetRowItem.getFullName());
            rowView.setOnClickListener(view -> {
                FragmentActivity activity = getActivity();
                if (activity instanceof LoginActivity) {
                    ((LoginActivity) activity).setCredentials(bottomSheetRowItem.getUsername(), bottomSheetRowItem.getPassword());
                }
                dismiss();
            });
            container.addView(rowView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentActivity activity = getActivity();
        if (activity instanceof LoginActivity) {
            ((LoginActivity) activity).animateChooseAccountButton(false);
        }
    }


    /* inner class */

    public class BottomSheetRowItem {

        private final String fullName;
        private final String username;
        private final String password;

        public BottomSheetRowItem(String fullName, String username, String password) {
            this.fullName = fullName;
            this.username = username;
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}

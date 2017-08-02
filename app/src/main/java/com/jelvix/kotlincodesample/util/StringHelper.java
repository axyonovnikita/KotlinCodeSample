package com.jelvix.kotlincodesample.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class StringHelper {

    public static Spannable makeCaption(String caption, String userName) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(userName);
        spannableStringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(" ");
        spannableStringBuilder.append(caption);
        return spannableStringBuilder;
    }

    public static Spannable makeSubstringBold(String text, String substring) {
        Spannable spannableString = new SpannableString(text);
        int firstSpacePosition = text.indexOf(substring);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), firstSpacePosition, firstSpacePosition + substring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}

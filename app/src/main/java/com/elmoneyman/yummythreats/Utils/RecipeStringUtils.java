package com.elmoneyman.yummythreats.Utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class RecipeStringUtils {

    public static SpannableStringBuilder mergeColoredText(String leftPart, String rightPart, int leftColor, int rightColor) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        final SpannableString leftPartSpannable = new SpannableString(leftPart);
        final SpannableString rightPartSpannable = new SpannableString(rightPart);
        leftPartSpannable.setSpan(new ForegroundColorSpan(leftColor), 0, leftPart.length(), 0);
        rightPartSpannable.setSpan(new ForegroundColorSpan(rightColor), 0, rightPart.length(), 0);
        return spannableStringBuilder.append(leftPartSpannable).append(" ").append(rightPartSpannable);
    }
}

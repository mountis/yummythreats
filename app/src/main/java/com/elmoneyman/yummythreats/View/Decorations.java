package com.elmoneyman.yummythreats.View;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.elmoneyman.yummythreats.R;


public class Decorations extends RecyclerView.ItemDecoration {

    private int margin;

    public Decorations(Context context) {
        margin = context.getResources().getDimensionPixelSize( R.dimen.spacing_small);
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}
package com.realgear.extensions.gridlayout;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

public class GridLayoutManagerExtended extends GridLayoutManager {

    public GridLayoutManagerExtended(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}

package com.weather.app.presentation.mainscreen

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class MarginDecoration(private val margin: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position == 0) {
            outRect.top = margin * 3
            outRect.bottom = margin * 3
            outRect.right = margin
            outRect.left = margin
        } else {
            outRect.top = margin
            outRect.bottom = margin
            outRect.right = margin * 3
            outRect.left = margin * 3
        }

    }
}
package com.example.galonapps.ui.component

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class VerticalMarginItemDecoration(
    /**
     * Vertical margin for all items.
     */
    private val verticalMargin: Int,
    /**
     * First item top margin.
     */
    private val firstItemTopMargin: Int = NOT_SET,
    /**
     * Last item bottom margin.
     */
    private val lastItemMargin: Int = NOT_SET
) : RecyclerView.ItemDecoration() {

    companion object {
        private const val NOT_SET = -1
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = if (isFirstItem(parent, view) && firstItemTopMargin != NOT_SET) {
                firstItemTopMargin
            } else {
                0
            }

            bottom = if (lastItemMargin == NOT_SET) {
                verticalMargin
            } else {
                if (isLastItem(parent, view, state)) {
                    lastItemMargin
                } else {
                    verticalMargin
                }
            }
        }
    }

    private fun isFirstItem(parent: RecyclerView, view: View) =
        parent.getChildAdapterPosition(view) == 0

    private fun isLastItem(parent: RecyclerView, view: View, state: RecyclerView.State) =
        parent.getChildAdapterPosition(view) == state.itemCount - 1
}

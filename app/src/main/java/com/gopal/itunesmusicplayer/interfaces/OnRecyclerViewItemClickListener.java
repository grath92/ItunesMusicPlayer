package com.gopal.itunesmusicplayer.interfaces;

import android.view.View;

/**
 * RecyclerView item click listener.
 */
public interface OnRecyclerViewItemClickListener {
    /**
     * Method to be called when an item in the Recycle view is clicked.
     *
     * @param view     view, that is clicked.
     * @param position position of the clicked view.
     */
    void onRecyclerViewItemClick(View view, int position);
}

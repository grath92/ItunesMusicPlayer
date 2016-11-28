package com.gopal.itunesmusicplayer.interfaces;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

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

package com.gopal.itunesmusicplayer.adapters;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gopal.itunesmusicplayer.R;
import com.gopal.itunesmusicplayer.apirequests.SongDetail;
import com.gopal.itunesmusicplayer.interfaces.OnRecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SongsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SongsAdapter";
    private OnRecyclerViewItemClickListener mRecyclerViewItemClick;

    private static final int ZERO = 10;

    private Context mContext;

    private ArrayList<SongDetail> mArrAllSongs;

    public SongsRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public SongsRecyclerAdapter(Context context, ArrayList<SongDetail> arrAllSongs) {
        this.mContext = context;
        this.mArrAllSongs = arrAllSongs;
    }

    public void setRecyclerViewItemClick(OnRecyclerViewItemClickListener mRecyclerViewItemClick) {
        this.mRecyclerViewItemClick = mRecyclerViewItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_song_item_view, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mainholder, int position) {
        if (mArrAllSongs != null) {
            ViewHolder holder = (ViewHolder) mainholder;
            SongDetail songDetail = mArrAllSongs.get(position);
            //song img
            if (songDetail.mArtworkUrl60 != null && !songDetail.mArtworkUrl60.equals(""))
                Picasso.with(mContext).load(songDetail.mArtworkUrl60).placeholder(R.drawable.group_2).into(holder.ivSong);
            //song title
            holder.tvSongTitle.setText(songDetail.mTrackName);
            //song other detail
            String strDesc = songDetail.mArtistName + " | " + songDetail.mTrackCensoredName;
            holder.tvSongDesc.setText(strDesc);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mArrAllSongs != null)
            return mArrAllSongs.size();
        else
            return ZERO;
    }

    //ViewHolder For different items of One List Item.....
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivSong;
        public TextView tvSongTitle;
        public TextView tvSongDesc;

        public ViewHolder(View item) {
            super(item);

            ivSong = (ImageView) item.findViewById(R.id.iv_song);
            tvSongTitle = (TextView) item.findViewById(R.id.tv_song_title);
            tvSongDesc = (TextView) item.findViewById(R.id.tv_song_description);

            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Call for RecyclerView item click......
            if (mRecyclerViewItemClick != null) {
                mRecyclerViewItemClick.onRecyclerViewItemClick(v, getAdapterPosition());
            }

        }
    }


}

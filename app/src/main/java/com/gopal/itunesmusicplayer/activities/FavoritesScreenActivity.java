package com.gopal.itunesmusicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gopal.Utility;
import com.gopal.database.DatabaseManager;
import com.gopal.database.Favorites;
import com.gopal.itunesmusicplayer.R;
import com.gopal.itunesmusicplayer.adapters.SongsRecyclerAdapter;
import com.gopal.itunesmusicplayer.apirequests.SongDetail;
import com.gopal.itunesmusicplayer.interfaces.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesScreenActivity extends AppCompatActivity {

    private static final String ALL_SONG = "All Songs - ";

    private ArrayList<SongDetail> arrAllSongs;
    private SongsRecyclerAdapter songsRecyclerAdapter;
    private DatabaseManager mDatabaseManager;

    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        arrAllSongs = new ArrayList<>();
        mDatabaseManager = DatabaseManager.getInstance(getApplicationContext());
        List<Favorites> arrFavorites = mDatabaseManager.getAllFavorites();

        for (int i=0;i<arrFavorites.size();i++){
            SongDetail songDetail = new SongDetail();
            songDetail.mArtworkUrl60 = arrFavorites.get(i).getSongPicUrl();
            songDetail.mArtworkUrl100 = arrFavorites.get(i).getSongPicUrl();
            songDetail.mArtistName = arrFavorites.get(i).getArtiestName();
            songDetail.mTrackName = arrFavorites.get(i).getTrackName();
            songDetail.mTrackId = arrFavorites.get(i).getTrackId();
            songDetail.mTrackCensoredName = arrFavorites.get(i).getTrackCensoredName();
            songDetail.mTrackTimeMillis = arrFavorites.get(i).getTotalTime();
            songDetail.mPreviewUrl = arrFavorites.get(i).getSongPreviewUrl();
            arrAllSongs.add(songDetail);
        }

        tvCount = (TextView) findViewById(R.id.tv_song_count);
        String str = ALL_SONG + arrFavorites.size();
        tvCount.setText(str);

        RecyclerView rcvFavorite = (RecyclerView) findViewById(R.id.rcv_favorite_songs);
        rcvFavorite.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        songsRecyclerAdapter = new SongsRecyclerAdapter(getApplicationContext(), arrAllSongs);
        rcvFavorite.setAdapter(songsRecyclerAdapter);
        songsRecyclerAdapter.setRecyclerViewItemClick(new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, int position) {
                Intent intentPlayerScreen = new Intent(getApplicationContext(), PlayerScreenActivity.class);
                SongDetail songDetail = arrAllSongs.get(position);
                intentPlayerScreen.putExtra(Utility.SONG_PIC_URL,songDetail.mArtworkUrl100);
                intentPlayerScreen.putExtra(Utility.ARTIST_NAME,songDetail.mArtistName);
                intentPlayerScreen.putExtra(Utility.TRACK_NAME,songDetail.mTrackName);
                intentPlayerScreen.putExtra(Utility.TRACK_ID,songDetail.mTrackId);
                intentPlayerScreen.putExtra(Utility.TRACK_CENSORED_NAME,songDetail.mTrackCensoredName);
                intentPlayerScreen.putExtra(Utility.TOTAL_TIME,songDetail.mTrackTimeMillis);
                intentPlayerScreen.putExtra(Utility.SONG_PREVIEW_URL,songDetail.mPreviewUrl);
                startActivity(intentPlayerScreen);
            }
        });

        //on swipe left or right recyclerView item.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcvFavorite);
    }

    //on swipe left or right recyclerView item.
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mDatabaseManager.deleteFavoriteById(arrAllSongs.get(viewHolder.getAdapterPosition()).mTrackId);
            mDatabaseManager.deleteTrackIdById(arrAllSongs.get(viewHolder.getAdapterPosition()).mTrackId);
            arrAllSongs.remove(viewHolder.getAdapterPosition());
            songsRecyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            String str = ALL_SONG + arrAllSongs.size();
            tvCount.setText(str);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

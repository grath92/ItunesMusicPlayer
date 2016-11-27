package com.gopal.itunesmusicplayer.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gopal.Utility;
import com.gopal.itunesmusicplayer.R;
import com.gopal.itunesmusicplayer.adapters.SongsRecyclerAdapter;
import com.gopal.itunesmusicplayer.apirequests.AllSongGetRequest;
import com.gopal.itunesmusicplayer.apirequests.AllSongs;
import com.gopal.itunesmusicplayer.apirequests.SongDetail;
import com.gopal.itunesmusicplayer.interfaces.OnRecyclerViewItemClickListener;
import com.gopal.network.ApiManager;

import java.util.ArrayList;

public class SearchScreenActivity extends AppCompatActivity {

    private static final String TAG = "SearchScreen";

    private static final String TERM = "shakira";

    private RecyclerView rcvAllSong;
    private SongsRecyclerAdapter songsRecyclerAdapter;
    private ArrayList<SongDetail> arrAllSongs;
    private RelativeLayout relativeLayout;
    private TextView tvCount;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        arrAllSongs = new ArrayList<>();
        relativeLayout = (RelativeLayout) findViewById(R.id.layout_relative);
        tvCount = (TextView) findViewById(R.id.tv_song_count);
        EditText edtSearch = (EditText) findViewById(R.id.et_search);
        edtSearch.addTextChangedListener(textWatcher);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();

        //api call for get all song
        apiCall(TERM);

        rcvAllSong = (RecyclerView) findViewById(R.id.rcv_song_list);
        rcvAllSong.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        songsRecyclerAdapter = new SongsRecyclerAdapter(getApplicationContext(), arrAllSongs);
        rcvAllSong.setAdapter(songsRecyclerAdapter);
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

    }

    private void apiCall(String term){
        progressBar.setVisibility(View.VISIBLE);
        AllSongGetRequest allSongsGetRequest = new AllSongGetRequest(term, new SongsSuccessListener(), new SongsErrorListener());
        ApiManager.getInstance(getApplicationContext()).addToRequestQueue(allSongsGetRequest);
    }

    //on editTxt txt change
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                String strTxt = s.toString().toLowerCase();
                strTxt = strTxt.replace(" ", "%20");
                apiCall(strTxt);
            }else {
                apiCall(TERM);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_favorites) {
            //OnClick Favorites, Do...
            Intent intentFavoriteActivity = new Intent(getApplicationContext(), FavoritesScreenActivity.class);
            startActivity(intentFavoriteActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //api call success
    private class SongsSuccessListener implements Response.Listener<AllSongs> {

        @Override
        public void onResponse(AllSongs response) {

            if (response != null) {
                if (Utility.DEBUG) Log.d(TAG, "Count: " + response.mResultCount);
                String strCount = "All Songs - "+response.mResultCount;
                tvCount.setText(strCount);
                if (response.mAllSongs != null){
                    arrAllSongs.clear();
                    songsRecyclerAdapter.notifyDataSetChanged();
                    arrAllSongs.addAll(response.mAllSongs);
                    songsRecyclerAdapter.notifyDataSetChanged();
                    rcvAllSong.setVisibility(View.VISIBLE);
                }
            }
            progressBar.setVisibility(View.GONE);

        }
    }

    //api call failure
    private class SongsErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (Utility.DEBUG) Log.d(TAG,"Error: "+error.networkResponse.toString());
            Snackbar.make(relativeLayout, "Connection error!!!", Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }

}

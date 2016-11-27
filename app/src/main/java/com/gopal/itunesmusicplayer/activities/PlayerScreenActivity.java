package com.gopal.itunesmusicplayer.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gopal.Utility;
import com.gopal.database.DatabaseManager;
import com.gopal.database.FavoritesDao;
import com.gopal.database.TrackIds;
import com.gopal.database.TrackIdsDao;
import com.gopal.itunesmusicplayer.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class PlayerScreenActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "PlayerScreen";

    private boolean isPlayPause, isFavorite = false;
    private MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();
    private Animation animBlink;

    private ImageView imvMenu, imvFavorite;
    private SeekBar mSeekBar;
    private TextView tvProgressTime;
    private FloatingActionButton fabPlayPause;
    private String strSongPicUrl, strTrackName, strArtistName, strTrackCensoredName, strTotalTime,strSongPreviewUrl;
    private Long mTrackId, mTotalTime, mId;

    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        strSongPicUrl = getIntent().getStringExtra(Utility.SONG_PIC_URL);
        strTrackName = getIntent().getStringExtra(Utility.TRACK_NAME);
        mTrackId = getIntent().getLongExtra(Utility.TRACK_ID,0);
        mTotalTime = getIntent().getLongExtra(Utility.TOTAL_TIME,0);
        strArtistName = getIntent().getStringExtra(Utility.ARTIST_NAME);
        strTrackCensoredName = getIntent().getStringExtra(Utility.TRACK_CENSORED_NAME);
        strSongPreviewUrl = getIntent().getStringExtra(Utility.SONG_PREVIEW_URL);
        strTotalTime = Utility.convertIntoMinute(mTotalTime);

        if (Utility.DEBUG) Log.d(TAG, "Song Pic Url: "+strSongPicUrl);
        if (Utility.DEBUG) Log.d(TAG, "Track Name: "+strTrackName);
        if (Utility.DEBUG) Log.d(TAG, "Track Id: "+mTrackId);
        if (Utility.DEBUG) Log.d(TAG, "Artist Name: "+strArtistName);
        if (Utility.DEBUG) Log.d(TAG, "Track Censored Name: "+strTrackCensoredName);
        if (Utility.DEBUG) Log.d(TAG, "Song Preview Url: "+strSongPreviewUrl);
        if (Utility.DEBUG) Log.d(TAG, "Total Time: "+strTotalTime);


        mDatabaseManager = DatabaseManager.getInstance(getApplicationContext());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);

        initializeView();

    }

    private void initializeView() {
        ImageView imvSongPic = (ImageView) findViewById(R.id.iv_song_img);
        imvMenu = (ImageView) findViewById(R.id.iv_menu);
        imvFavorite = (ImageView) findViewById(R.id.iv_favorite);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        tvProgressTime = (TextView) findViewById(R.id.tv_progress_time);
        TextView tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        TextView tvSongName = (TextView) findViewById(R.id.tv_song_name);
        TextView tvSongDesc = (TextView) findViewById(R.id.tv_song_desc);
        fabPlayPause = (FloatingActionButton) findViewById(R.id.fab_play_pause);
        List<TrackIds> trackIdses = mDatabaseManager.getAllTrackIds();
        if (trackIdses != null && trackIdses.size() != 0){
            for (int i=0; i<trackIdses.size();i++){
                if (mTrackId == trackIdses.get(i).getTrackId()){
                    mId = trackIdses.get(i).getTrackId();
                    if (Utility.DEBUG) Log.d(TAG, "mId: "+mId);
                    imvFavorite.setImageResource(R.drawable.shape_heart_red);
                    isFavorite = true;
                }
            }
        }

        if (strSongPicUrl != null && !strSongPicUrl.equals(""))
            Picasso.with(getApplicationContext()).load(strSongPicUrl).placeholder(R.drawable.group_2).into(imvSongPic);

        if (strTotalTime != null && !strTotalTime.equals(""))
            tvTotalTime.setText(strTotalTime);

        if (strTrackName != null && !strTrackName.equals(""))
            tvSongName.setText(strTrackName);

        if (strArtistName != null && !strArtistName.equals("")
                && strTrackCensoredName != null && !strTrackCensoredName.equals("")){
            String str = strArtistName + " | " + strTrackCensoredName;
            tvSongDesc.setText(str);
        }

        fabPlayPause.setOnClickListener(this);
        mSeekBar.setClickable(true);
        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        imvFavorite.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.equals(fabPlayPause)){
            if (!isPlayPause) {
                fabPlayPause.setImageResource(R.drawable.combined_shape_2);
                try {
                    mediaPlayer.setDataSource(strSongPreviewUrl);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
                    e.printStackTrace();
                }

                mSeekBar.setProgress(mediaPlayer.getCurrentPosition()/1000);
                myHandler.postDelayed(UpdateSongTime, 100);

                isPlayPause = true;
            } else {
                fabPlayPause.setImageResource(R.drawable.triangle);
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                isPlayPause = false;
            }
        }else if (v.equals(imvFavorite)){

            //imvFavorite.setAnimation(animBlink);

            if (isFavorite){
                //remove a row to favorite table
                mDatabaseManager.deleteFavoriteById(mId);
                if (Utility.DEBUG) Log.d(TAG,"favDel: "+ mDatabaseManager.deleteFavoriteById(mId));
                //remove a row to trackid table
                mDatabaseManager.deleteTrackIdById(mId);
                if (Utility.DEBUG) Log.d(TAG,"tIdDel: "+ mDatabaseManager.deleteTrackIdById(mId));

                imvFavorite.setImageResource(R.drawable.shape_heart);
                isFavorite = false;
            }else {

                //add a row to favorite table
                ContentValues favoriteValue = new ContentValues();
                favoriteValue.put(FavoritesDao.Properties.TrackId.columnName,mTrackId);
                favoriteValue.put(FavoritesDao.Properties.TotalTime.columnName,mTotalTime);
                favoriteValue.put(FavoritesDao.Properties.TrackName.columnName,strTrackName);
                favoriteValue.put(FavoritesDao.Properties.ArtiestName.columnName,strArtistName);
                favoriteValue.put(FavoritesDao.Properties.TrackCensoredName.columnName,strTrackCensoredName);
                favoriteValue.put(FavoritesDao.Properties.SongPicUrl.columnName,strSongPicUrl);
                favoriteValue.put(FavoritesDao.Properties.SongPreviewUrl.columnName,strSongPreviewUrl);
                mDatabaseManager.insertFavorite(favoriteValue);

                //add a row to trackid table
                ContentValues trackIdValue = new ContentValues();
                trackIdValue.put(TrackIdsDao.Properties.TrackId.columnName,mTrackId);
                mDatabaseManager.insertTrackId(trackIdValue);

                imvFavorite.setImageResource(R.drawable.shape_heart_red);
                isFavorite = true;
            }
        }

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            try {
                int startTime = mediaPlayer.getCurrentPosition();
                tvProgressTime.setText(Utility.convertIntoMinute((long) startTime));
                mSeekBar.setProgress(startTime/1000);
                myHandler.postDelayed(this, 100);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(mediaPlayer != null && fromUser){
                mediaPlayer.seekTo(progress * 1000);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_favorites:
                //OnClick Favorites, Do...
                Intent intentFavoriteActivity = new Intent(getApplicationContext(), FavoritesScreenActivity.class);
                startActivity(intentFavoriteActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

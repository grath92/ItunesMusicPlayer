package com.gopal;

import java.util.concurrent.TimeUnit;

public class Utility {

    public static final boolean DEBUG = true;

    public static final String SONG_PIC_URL = "songPicUrl";
    public static final String TRACK_ID = "trackId";
    public static final String TRACK_NAME = "trackName";
    public static final String ARTIST_NAME = "artistName";
    public static final String TRACK_CENSORED_NAME= "trackCensoredName";
    public static final String TOTAL_TIME= "totalTime";
    public static final String SONG_PREVIEW_URL= "songPreviewUrl";

    public static String convertIntoMinute(long milliseconds){
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}

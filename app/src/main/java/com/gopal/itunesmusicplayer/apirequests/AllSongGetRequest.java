package com.gopal.itunesmusicplayer.apirequests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.gopal.network.NetWorkRequest;


public class AllSongGetRequest extends NetWorkRequest<AllSongs> {

    private static final String URL = "/search?term=";
    private static final String LIMIT = "&limit=100/";

    public AllSongGetRequest(String TERM, Response.Listener<AllSongs> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, URL+TERM+LIMIT , null, AllSongs.class, false, listener, errorListener);
    }
}

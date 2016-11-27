package com.gopal.itunesmusicplayer.apirequests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Input model for {@link AllSongGetRequest}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllSongs {

    @JsonProperty("resultCount")
    public int mResultCount;

    @JsonProperty("results")
    public ArrayList<SongDetail> mAllSongs = new ArrayList<>();

}

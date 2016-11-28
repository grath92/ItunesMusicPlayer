package com.gopal.itunesmusicplayer.apirequests;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Input model for {@link AllSongGetRequest}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SongDetail {

    @JsonProperty("wrapperType")
    public String mWrapperType;

    @JsonProperty("kind")
    public String mKind;

    @JsonProperty("trackId")
    public long mTrackId;

    @JsonProperty("artistName")
    public String mArtistName;

    @JsonProperty("trackName")
    public String mTrackName;

    @JsonProperty("trackCensoredName")
    public String mTrackCensoredName;

    @JsonProperty("trackViewUrl")
    public String mTrackViewUrl;

    @JsonProperty("previewUrl")
    public String mPreviewUrl;

    @JsonProperty("artworkUrl30")
    public String mArtworkUrl30;

    @JsonProperty("artworkUrl60")
    public String mArtworkUrl60;

    @JsonProperty("artworkUrl100")
    public String mArtworkUrl100;

    @JsonProperty("collectionPrice")
    public double mCollectionPrice;

    @JsonProperty("trackPrice")
    public double mTrackPrice;

    @JsonProperty("trackRentalPrice")
    public double mTrackRentalPrice;

    @JsonProperty("collectionHdPrice")
    public double mCollectionHdPrice;

    @JsonProperty("trackHdPrice")
    public double mTrackHdPrice;

    @JsonProperty("trackHdRentalPrice")
    public double mTrackHdRentalPrice;

    @JsonProperty("releaseDate")
    public String mReleaseDate;

    @JsonProperty("collectionExplicitness")
    public String mCollectionExplicitness;

    @JsonProperty("trackExplicitness")
    public String mTrackExplicitness;

    @JsonProperty("trackTimeMillis")
    public long mTrackTimeMillis;

    @JsonProperty("country")
    public String mCountry;

    @JsonProperty("currency")
    public String mCurrency;

    @JsonProperty("primaryGenreName")
    public String mPrimaryGenreName;

    @JsonProperty("contentAdvisoryRating")
    public String mContentAdvisoryRating;

    @JsonProperty("longDescription")
    public String mLongDescription;

    @JsonProperty("hasITunesExtras")
    public boolean mHasITunesExtras;

}

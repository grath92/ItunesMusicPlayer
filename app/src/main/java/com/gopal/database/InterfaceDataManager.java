package com.gopal.database;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import android.content.ContentValues;

import java.util.List;


public interface InterfaceDataManager {
    /**
     * Closing available connections
     */
    public void closeDbConnections();

    public void dropDatabase();

    public List<Favorites> getAllFavorites();

    public List<TrackIds> getAllTrackIds();

    public void insertFavorite(ContentValues favorite);

    public void insertTrackId(ContentValues trackId);

    public boolean deleteFavoriteById(Long id);

    public boolean deleteTrackIdById(Long id);

    public void deleteFavorites();

    public void deleteTaskIds();


}

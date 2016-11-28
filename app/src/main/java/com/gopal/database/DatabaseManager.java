package com.gopal.database;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class DatabaseManager implements InterfaceDataManager {
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager mInstance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context mContext;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link Context}.
     */

    public DatabaseManager(final Context context) {
        this.mContext = context;
        if (mHelper == null)
            mHelper = new DaoMaster.DevOpenHelper(this.mContext, "mplayerdb", null);
    }

    /**
     * @param context The Android {@link Context}.
     * @return this.mInstance
     */
    public static DatabaseManager getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new DatabaseManager(context);
        }

        return mInstance;
    }


    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (mInstance != null) {
            mInstance = null;
        }
    }

    @Override
    public synchronized void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized List<Favorites> getAllFavorites() {
        List<Favorites> favorites = new ArrayList<>();
        try {
            openReadableDb();
            FavoritesDao favoritesDao = daoSession.getFavoritesDao();
            favorites.addAll(favoritesDao.queryBuilder().orderAsc(FavoritesDao.Properties.Id).list());

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favorites;
    }


    @Override
    public List<TrackIds> getAllTrackIds() {
        List<TrackIds> trackIdses = new ArrayList<>();
        try {
            openReadableDb();
            TrackIdsDao trackIdsDao = daoSession.getTrackIdsDao();
            trackIdses.addAll(trackIdsDao.queryBuilder().orderAsc(TrackIdsDao.Properties.Id).list());

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackIdses;
    }

    /**
     * Insert a favorite into the DB
     *
     * @param favorite to be inserted
     */
    @Override
    public void insertFavorite(ContentValues favorite) {
        try {
            if (favorite != null) {
                openWritableDb();
                database.insert(FavoritesDao.TABLENAME, null, favorite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Insert a TrackId into the DB
     *
     * @param trackId to be inserted
     */
    @Override
    public void insertTrackId(ContentValues trackId) {
        try {
            if (trackId != null) {
                openWritableDb();
                database.insert(TrackIdsDao.TABLENAME, null, trackId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a Favorite with a certain id from the DB
     *
     * @param id of Favorite to be deleted
     */
    @Override
    public synchronized boolean deleteFavoriteById(Long id) {
        try {
            openWritableDb();
            FavoritesDao favoritesDao = daoSession.getFavoritesDao();
            QueryBuilder<Favorites> queryBuilder = favoritesDao.queryBuilder().where(FavoritesDao.Properties.TrackId.eq(id));
            List<Favorites> favorites = queryBuilder.list();
            for (Favorites favorite : favorites) {
                favoritesDao.delete(favorite);
            }
            daoSession.clear();
            //favoritesDao.deleteByKey(id);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a TrackId with a certain id from the DB
     *
     * @param id of trakId to be deleted
     */
    @Override
    public synchronized boolean deleteTrackIdById(Long id) {
        try {
            openWritableDb();

            TrackIdsDao trackIdsDao = daoSession.getTrackIdsDao();
            QueryBuilder<TrackIds> queryBuilder = trackIdsDao.queryBuilder().where(TrackIdsDao.Properties.TrackId.eq(id));
            List<TrackIds> trackIdses = queryBuilder.list();
            for (TrackIds trackId : trackIdses) {
                trackIdsDao.delete(trackId);
            }
            //trackIdsDao.deleteByKey(id);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public synchronized void deleteFavorites() {
        try {
            openWritableDb();
            FavoritesDao favoritesDao = daoSession.getFavoritesDao();
            favoritesDao.deleteAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteTaskIds() {
        try {
            openWritableDb();
            TrackIdsDao trackIdsDao = daoSession.getTrackIdsDao();
            trackIdsDao.deleteAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

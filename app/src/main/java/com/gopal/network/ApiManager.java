package com.gopal.network;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Class to manage api requests, adding and removing api in and from request queue.
 */
public class ApiManager {
    private static ApiManager sInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    /**
     * Private constructor to achieve singleton
     *
     * @param context Context for creating {@link com.android.volley.RequestQueue} object
     */
    private ApiManager(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Method to return RequestQueue object, creates if needed.
     *
     * @return RequestQueue object
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * This method will return the singleton instance of this class, creates if needed.
     *
     * @param context Context for creating {@link com.android.volley.RequestQueue} object
     * @return Singleton instance of this class
     */
    public static ApiManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ApiManager(context);
        }
        return sInstance;
    }

    /**
     * Method to add {@link Request} to {@link RequestQueue}.
     *
     * @param request Request object to be added.
     * @param <T>     Request type to be added.
     */
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}

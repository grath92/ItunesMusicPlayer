package com.gopal.network;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import android.content.Context;
import android.content.SharedPreferences;

public class TokenPrefs {

    private static final String TOKEN = "token";
    private static final String APP_PREF = "appPrefs";
    private static final String LOG_TAG = TokenPrefs.class.getSimpleName();
    private static SharedPreferences sSharedPreferences;

    private static SharedPreferences getSharedPreferences() {
        if (sSharedPreferences == null) {
            sSharedPreferences = AppApplication.getInstance().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        }
        return sSharedPreferences;
    }

    /**
     * Save access-token, required to authenticate the user.
     *
     * @param accessToken AccessToken.
     */
    public static void saveAccessToken(String accessToken) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, accessToken).apply();
    }

    /**
     * Get access-token.
     */
    public static String getAccessToken() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(TOKEN, null);
    }

    /**
     * Get access-token.
     */
    public static void clearAccessToken() {
        SharedPreferences preferences = getSharedPreferences();
        preferences.getAll().clear();
    }

}

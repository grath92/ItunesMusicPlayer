package com.gopal.network;


import com.gopal.Utility;

/**
 * Class containing api base end-points.
 */
public class ApiEndPoint {
    private static final String URL_TEST = "http://itunes.apple.com";
    private static final String URL_LIVE = "http://itunes.apple.com";

    /**
     * Method to return api base according to build.
     *
     * @return api base end-point
     */
    public static String getApiBase() {

        if (Utility.DEBUG) {
            return URL_TEST;
        } else {
            return URL_LIVE;
        }
    }
}

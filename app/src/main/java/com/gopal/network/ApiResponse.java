package com.gopal.network;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

/**
 * Class to handle API errors.
 */
public class ApiResponse {
    public static final int SERVER_ERROR = 500;

    /**
     * Method to return error message.
     *
     * @param error {@link VolleyError} instance
     * @return error message
     */
    public static String getErrorMessage(VolleyError error) {
        String errorMessage;
        if (error instanceof NoConnectionError) {
            errorMessage = "No Internet Connection!";
        } else if (error.networkResponse == null) {
            errorMessage = "Internal error. Please try again later.";
        } else if (error.networkResponse.statusCode >= ApiResponse.SERVER_ERROR &&
                error.networkResponse.statusCode <= 599) {
            errorMessage = "Server error.";
        } else {
            errorMessage = "Internal error.";
        }
        return errorMessage;
    }
}

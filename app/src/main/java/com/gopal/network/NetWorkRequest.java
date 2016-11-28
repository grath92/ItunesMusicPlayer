package com.gopal.network;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gopal.Utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class NetWorkRequest<T> extends Request<T> {

    // charset for request
    protected static final String PROTOCOL_CHARSET = "utf-8";
    // content type for request
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private static final String TAG = "NetWorkRequest";
    private boolean isTokenRequired;
    private Object mRequestBody;
    private Listener<T> mListener;
    private Class<T> mOutputType;

    // Constructor
    public NetWorkRequest(int method, String url, Object requestBody, Class<T> outputType, boolean isTokenRequired, Listener<T> listener, ErrorListener errorListener) {
        super(method,
                ApiEndPoint.getApiBase() + url, // adding base url
                errorListener);

        mRequestBody = requestBody;
        this.isTokenRequired = isTokenRequired;
        mListener = listener;
        mOutputType = outputType;

        if (Utility.DEBUG) {
            printRequestLog(method, url, requestBody, outputType, isTokenRequired);
        }
        setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    // Method to print request log.
    private void printRequestLog(int method, String url, Object requestBody, Class<T> outputType, boolean isTokenRequired) {
        if (outputType != null) {
            Log.d(TAG, String.format("Method type : %d\nURL : %s\nOutput Type : %s\nIsTokenRequired : %s\n", method, ApiEndPoint.getApiBase() + url, outputType.getName(), isTokenRequired ? "True" : "False"));
        }
        if (requestBody != null) {
            try {
                Log.d(TAG, "RequestBody:" + Mapper.getInstance().writeValueAsString(requestBody.toString()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "RequestBody: null");
        }
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {

            if (response.statusCode == 401)//token expired
            {
                //makeLoginRequest();
            }

            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (Utility.DEBUG) {
                Log.d(TAG, "Response json :" + json);
            }
            return Response.success(
                    Mapper.getInstance().readValue(json, mOutputType),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (isTokenRequired) {// checks if access-token is required
            // get token from shared prefs.
            String token = TokenPrefs.getAccessToken();

            if (token == null) {
                throw new AuthFailureError("Access token not found");
            }
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Token " + token);
            // Print request headers in log.
            if (Utility.DEBUG) {
                Log.d(TAG, String.format("Headers : key-Authorization, value-%s", headers.get("Authorization")));
            }
            return headers;
        }
        return super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mRequestBody != null) {
            try {
                String requestBody = Mapper.getInstance().writeValueAsString(mRequestBody);
                return requestBody.getBytes(PROTOCOL_CHARSET);

            } catch (JsonProcessingException e) {
                try {
                    return mRequestBody.toString().getBytes(PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

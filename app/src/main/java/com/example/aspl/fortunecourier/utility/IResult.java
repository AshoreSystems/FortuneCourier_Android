package com.example.aspl.fortunecourier.utility;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by aspl on 16/11/17.
 */

public interface IResult {
    public void notifySuccess(String requestType,JSONObject response);
    public void notifyError(String requestType,VolleyError error);
}

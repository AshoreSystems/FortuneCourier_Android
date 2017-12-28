package com.example.aspl.fortunecourier.utility;

/**
 * Created by aspl on 20/12/17.
 */

public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response);
}

package com.example.aspl.fortunecourier.service;

import android.util.Log;

import com.example.aspl.fortunecourier.SplashActivity;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by aspl on 22/11/17.
 */

//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private SessionManager mSessionManager;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        mSessionManager = new SessionManager(MyFirebaseInstanceIDService.this);
        mSessionManager.putStringData(SessionManager.KEY_CDT_DEVICE_TOKEN,refreshedToken);

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
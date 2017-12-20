package com.example.aspl.fortunecourier.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Avion Team.
 * @Date 16 Aug 2016.
 * @VolleyWebservice For Calling Webservice
 */


public class VolleyWebservice {
    String WhichWebservice, DialogStr, URL;
    JSONObject obj;
    Context mContext;
    String TAG = "VolleyWebservice";
    ProgressDialog pDialog;
    Handler handler;

    Map<String, String> mParams;



    public VolleyWebservice(Activity activity, Handler handler, String WhichWebservice, String URL, Map<String, String> params) {
        mContext = activity;
        this.WhichWebservice = WhichWebservice;
        //this.mTransactionFragment = mTransactionFragment;
        this.URL = URL;
        mParams = params;
        this.handler=handler;
        callWebService();
    }


    private void callWebService() {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        // "TAG" used to cancel the request

        try {
            Log.e("OBJ in VolleyWebservice", obj.toString());
        } catch (Exception e) {

        }

        final JSONObject response = new JSONObject();

        pDialog = new ProgressDialog(mContext);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest mstringrequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // mPostCommentResponse.requestCompleted();
                Log.e(TAG, "Service--o/p-" + response);

                try {

                    Message msg = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("json", response);
                    msg.setData(b);
                    handler.sendMessage(msg);
                    pDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mPostCommentResponse.requestEndedWithError(error);
                try {
                    // mPostCommentResponse.requestEndedWithError(error);
                    Log.e("TAG", "Service--i/p-" + error);

                    String ErrorMessage = "";
                    String ErrorTitle = "";
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        ErrorMessage = "Connection Error. Please check your connection and try again.";
                        ErrorTitle = "Connection Error";
                    } else if (error instanceof AuthFailureError) {
                        ErrorMessage = "Auth Failure Error. Please try again later.";
                        ErrorTitle = "AuthFailureError";
                    } else if (error instanceof ServerError) {
                        ErrorMessage = "Server Error. Please try again later.";
                        ErrorTitle = "Server Error";
                    } else if (error instanceof NetworkError) {
                        ErrorMessage = "Network Error. Please check your Network and try again.";
                        ErrorTitle = "Network Error";
                    } else if (error instanceof ParseError) {
                        ErrorMessage = "Parse Error. Please try again.";
                        ErrorTitle = "Parse Error";
                    }

                    pDialog.dismiss();
                    // AndroidUtils.showNoNetworkConnectionDialog(mContext);
                    if (mContext != null) {


                        try {

                            Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }

                    }
                } catch (Exception e) {


                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params = mParams;

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        mstringrequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(mstringrequest);
    }


}


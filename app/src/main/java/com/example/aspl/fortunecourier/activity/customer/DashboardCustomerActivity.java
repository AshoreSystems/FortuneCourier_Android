package com.example.aspl.fortunecourier.activity.customer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.fragment.FragmentDrawerCustomer;
import com.example.aspl.fortunecourier.fragment.FriendsFragment;
import com.example.aspl.fortunecourier.fragment.HomeFragment;
import com.example.aspl.fortunecourier.fragment.MessagesFragment;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 13/11/17.
 */

public class DashboardCustomerActivity extends AppCompatActivity implements FragmentDrawerCustomer.FragmentDrawerListener {

    private static String TAG = DashboardCustomerActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawerCustomer drawerFragment;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    public final static int PERMISSIONS_REQUEST_FOR_READ_PHONE_STATE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_customer);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(DashboardCustomerActivity.this);
        cd = new ConnectionDetector(DashboardCustomerActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mToolbar.setTitleTextColor(Color.parseColor("#dfaa35"));
        // mToolbar.setForegroundGravity(Gravity.CENTER_HORIZONTAL);

        drawerFragment = (FragmentDrawerCustomer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        getIMEINumber();

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    private void getIMEINumber(){

       if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_FOR_READ_PHONE_STATE);
            } else {
                TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                IMEI_Number_Holder = mngr.getDeviceId();
                mSessionManager.putStringData(SessionManager.KEY_CDT_DEVICE_ID,IMEI_Number_Holder);
            }
        } else {
            TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            IMEI_Number_Holder = mngr.getDeviceId();
            mSessionManager.putStringData(SessionManager.KEY_CDT_DEVICE_ID,IMEI_Number_Holder);
       }

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.nav_item_dashboard);
                break;

            case 1:
                fragment = new FriendsFragment();
                title = getString(R.string.nav_item_generate_shipment);
                break;

            case 2:
                fragment = new MessagesFragment();
                title = getString(R.string.nav_item_track_shipment);
                break;

            case 3:
                fragment = new MessagesFragment();
                title = getString(R.string.nav_item_view_shipment_history);
                break;

            case 4:
                startActivity(new Intent(DashboardCustomerActivity.this, CheckRatesFromActivity.class));
                break;

            case 5:
                startActivity(new Intent(DashboardCustomerActivity.this, UpdateProfileCustomerActivity.class));
                break;

            case 6:
                if(AppConstant.IS_USER_FROM_FACEBOOK_SIGNUP){
                    startActivity(new Intent(DashboardCustomerActivity.this, ResetPasswordCustomerActivity.class));

                }else {
                    startActivity(new Intent(DashboardCustomerActivity.this, ChangePasswordCustomerActivity.class));

                }
                //drawer.closeDrawers();
                //return true;
                //startActivity(new Intent(DashboardCustomerActivity.this,ChangePasswordCustomerActivity.class));
                //fragment = new ChangePasswordFragment();
                //title = getString(R.string.nav_item_logout);
                break;

            case 7:
                startActivity(new Intent(DashboardCustomerActivity.this, ChangePhoneNoCustomerActivity.class));
                break;

            case 8:
                shareTextUrl();
                break;

            case 9:
                break;

            case 10:
                startActivity(new Intent(DashboardCustomerActivity.this,ContactUsCustomerActivity.class));

               /* if(cd.isConnectingToInternet()){
                    showDialogOnLogout(DashboardCustomerActivity.this);
                }else {
                    Snackbar.make(mToolbar,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }*/
                break;

            case 11:
                if(cd.isConnectingToInternet()){
                    showDialogOnLogout(DashboardCustomerActivity.this);
                }else {
                    Snackbar.make(mToolbar,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            TextView textView = new TextView(this);
            textView.setText(title);
            textView.setTextSize(20);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#dfaa35"));
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(textView);

            //getSupportActionBar().setTitle(title);
        }
    }

    private void showDialogOnLogout(Context context){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.custom_dialog_logout, null);
        alertDialog.setView(convertView);

        TextView popup_title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView popup_message = (TextView) convertView.findViewById(R.id.tv_message);

        Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = alertDialog.create();
        popup_title.setText(getResources().getString(R.string.logout));
        popup_message.setText(getResources().getString(R.string.logout_text));
        dialog.setCanceledOnTouchOutside(false);

        // for back button
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                logoutUser();
                dialog.dismiss();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
               // logoutUser();
            }
        });
        dialog.show();
    }

    public void logoutUser() {

        try {
            progressBar = new ProgressDialog(DashboardCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_logout);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    mSessionManager.putBooleanData(SessionManager.KEY_IS_C_LOGOUT,true);

                                    hideKeyboard();
                                    Intent i = new Intent(DashboardCustomerActivity.this, LoginCustomerActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();;
                                    //mSessionManager.clearAllData();
                                    //Snackbar.make(mToolbar,Json_response.getString(JSONConstant.MESSAGE),Snackbar.LENGTH_LONG).show();
                                } else {
                                    mSessionManager.clearAllData();
                                    Snackbar.make(mToolbar,Json_response.getString(JSONConstant.MESSAGE),Snackbar.LENGTH_LONG).show();
                                    thread.start();
                                }
                                progressBar.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBar.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            progressBar.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Log.e(TAG,"="+mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_ID));
                    Log.e(TAG,"="+mSessionManager.getStringData(IMEI_Number_Holder));

                    Map<String, String> params = new HashMap<String, String>();

                    params.put(JSONConstant.C_ID, mSessionManager.getStringData(SessionManager.KEY_C_ID));
                    params.put(JSONConstant.CDT_DEVICE_ID, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_ID));
                    params.put(JSONConstant.CDT_DEVICE_TYPE, "android");
                    params.put(JSONConstant.CDT_DEVICE_TOKEN, "kjdkjf");

                   // params.put(JSONConstant.CDT_DEVICE_TOKEN, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_TOKEN));

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    // TODO Auto-generated method stub
                    return "application/x-www-form-urlencoded";
                }
            };

            // Adding JsonObject request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Fortune Courier");
        share.putExtra(Intent.EXTRA_TEXT, " Hey check out app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
        startActivity(Intent.createChooser(share, "Share App"));
    }

    public static  boolean checkforPhone(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.e("checkforcamera true", "checkforcamera true");
            return true;

        } else {
            Log.e("checkforcamera false", "checkforcamera false");
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 3:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    IMEI_Number_Holder = mngr.getDeviceId();
                    mSessionManager.putStringData(SessionManager.KEY_CDT_DEVICE_ID,IMEI_Number_Holder);
                }
                break;

            default:
                break;
        }
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(DashboardCustomerActivity.this, LandingCustomerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}


package com.example.aspl.fortunecourier.activity.customer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.associate.LandingAssociateActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogClass;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LandingCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_become_user,tv_info,tv_existing_user,tv_portal,tv_check_rates;
    private Button btn_register_now;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    public final static int PERMISSIONS_REQUEST_FOR_READ_PHONE_STATE =1;
    SessionManager mSessionManager;
    private ProgressDialog progressBar;
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_landing);
        init();
    }


    private void init(){

        mSessionManager = new SessionManager(LandingCustomerActivity.this);
        cd = new ConnectionDetector(LandingCustomerActivity.this);

        tv_become_user = (TextView) findViewById(R.id.tv_become_user);
        tv_become_user.setOnClickListener(this);

        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_info.setOnClickListener(this);
        tv_info.setMovementMethod(new ScrollingMovementMethod());

        tv_existing_user = (TextView) findViewById(R.id.tv_existing_user);
        tv_existing_user.setOnClickListener(this);

        tv_portal = (TextView) findViewById(R.id.tv_portal);
        tv_portal.setOnClickListener(this);

        tv_check_rates = (TextView) findViewById(R.id.tv_check_rates);
        tv_check_rates.setOnClickListener(this);

        btn_register_now = (Button) findViewById(R.id.btn_register_now);
        btn_register_now.setOnClickListener(this);

        //to get IMEI number
        getIMEINumber();

        if(cd.isConnectingToInternet()){
            getCustomerInfo();
        }else {
            Snackbar.make(tv_portal,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_become_user:
                break;

            case R.id.tv_info:
                break;

            case R.id.tv_existing_user:
                    startActivity(new Intent(LandingCustomerActivity.this,LoginCustomerActivity.class));
                break;

            case R.id.tv_portal:
                    startActivity(new Intent(LandingCustomerActivity.this,LandingAssociateActivity.class));
                break;

            case R.id.tv_check_rates:
                    //startActivity(new Intent(LandingCustomerActivity.this,CreateShipmentFromActivity.class));
                    startActivity(new Intent(LandingCustomerActivity.this,CheckRatesFromActivity.class));
                break;

            case R.id.btn_register_now:
                    startActivity(new Intent(LandingCustomerActivity.this,SignUpCustomerActivity.class));
                break;

        }

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
       /* if ((int) Build.VERSION.SDK_INT < 23) {
            telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            IMEI_Number_Holder = telephonyManager.getDeviceId();
            mSessionManager.putStringData(SessionManager.KEY_CDT_DEVICE_ID,IMEI_Number_Holder);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_FOR_READ_PHONE_STATE);
            } else {
                telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                IMEI_Number_Holder = telephonyManager.getDeviceId();
                mSessionManager.putStringData(SessionManager.KEY_CDT_DEVICE_ID,IMEI_Number_Holder);

            }
        }*/
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

    public void getCustomerInfo() {

        try {
            progressBar = new ProgressDialog(LandingCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_dashboard_content);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if( Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    JSONObject jsonResponsePageContent = Json_response.getJSONObject(JSONConstant.PAGE_CONTENT);
                                    tv_info.setText(Html.fromHtml(jsonResponsePageContent.getString("pageContent")));
                                   /* CustomDialogClass customDialogClass = new CustomDialogClass(LandingCustomerActivity.this,jsonResponsePageContent.getString(JSONConstant.PAGENAME),jsonResponsePageContent.getString(JSONConstant.PAGECONTENT));
                                    customDialogClass.show();*/
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
}

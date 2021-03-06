package com.example.aspl.fortunecourier.activity.associate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.DashboardCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.OTPVerificationCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.RegisterPincodeCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.SignUpCustomerActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by aspl on 6/12/17.
 */

public class OTPVerificationAssociateActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private TextView tv_resend_otp;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private EditText editText_otp_code;
    private Button btn_verify;
    private ConnectionDetector cd;
    private TextInputLayout textInput_otp_code;
    String URL;
    String strPincode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        init();
        startTimer();
       /* if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
        startTimer();*/
    }

    private void init(){
        mSessionManager = new SessionManager(OTPVerificationAssociateActivity.this);
        cd = new ConnectionDetector(OTPVerificationAssociateActivity.this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        editText_otp_code = (EditText) findViewById(R.id.editText_otp_code);

        textInput_otp_code = (TextInputLayout) findViewById(R.id.textInput_otp_code);

        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);

        tv_resend_otp = (TextView) findViewById(R.id.tv_resend_otp);
        tv_resend_otp.setOnClickListener(this);


        if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.SIGNUP_OTP)){
            URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_verfiy_signup_otp);
        }else if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.FORGOT_OTP)){
            URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_verfiy_forgot_password_otp);
        }else if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.CHANGE_PHONE_OTP)) {
            URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_verify_phone_no_otp);
        }else if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.VERIFY_PINCODE_OTP)){
            strPincode = getIntent().getStringExtra("pincode");
            URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_verify_reset_pin_otp);
        }else {
            URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_verify_facebook_otp);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra(JSONConstant.MESSAGE);

                TextView tv = (TextView) findViewById(R.id.txtview);
                tv.setText(message);
            }
        }
    };

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void startTimer() {
        tv_resend_otp.setClickable(false);
        tv_resend_otp.setTextColor(ContextCompat.getColor(OTPVerificationAssociateActivity.this, R.color.colorYellow));
        new CountDownTimer(120000, 1000) {
            int secondsLeft = 0;

            public void onTick(long ms) {
                if (Math.round((float) ms / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((float) ms / 1000.0f);
                    // tv_resend_otp.setText("Resend ( " + secondsLeft + " )");
                    tv_resend_otp.setText("Time remaining "+String.format("%d : %d",
                            TimeUnit.MILLISECONDS.toMinutes( ms),
                            TimeUnit.MILLISECONDS.toSeconds(ms) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))));
                }
            }

            public void onFinish() {
                tv_resend_otp.setClickable(true);
                tv_resend_otp.setText("Resend");
                tv_resend_otp.setTextColor(ContextCompat.getColor(OTPVerificationAssociateActivity.this, R.color.colorRed));
            }
        }.start();
    }
    public void recivedSms(String message)
    {
        try
        {
            tv_resend_otp.setText(message);
        }
        catch (Exception e)
        {
        }
    }

    public void verifyOTP() {

        try {
            progressBar = new ProgressDialog(OTPVerificationAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();


            //String URL = "http://server.ashoresystems.com/~sivs/apis/verify_signup_otp";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                String status = Json_response.getString(JSONConstant.STATUS);
                                if (status.equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.SIGNUP_OTP)){
                                        mSessionManager.putStringData(SessionManager.KEY_A_ID,Json_response.getString(JSONConstant.A_ID));
                                        mSessionManager.putStringData(SessionManager.KEY_A_FIRST_NAME,Json_response.getString(JSONConstant.A_FIRST_NAME));
                                        mSessionManager.putStringData(SessionManager.KEY_A_LAST_NAME,Json_response.getString(JSONConstant.A_LAST_NAME));
                                        mSessionManager.putStringData(SessionManager.KEY_A_PROFILE_URL,Json_response.getString(JSONConstant.A_PROFILE_PIC));

                                        mSessionManager.putIntData(SessionManager.KEY_WHICH_USER,2);

                                       /* Intent i = new Intent(OTPVerificationAssociateActivity.this, DashboardAssociateActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);*/

                                        Toast.makeText(OTPVerificationAssociateActivity.this,getResources().getString(R.string.msg_signup),Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(OTPVerificationAssociateActivity.this, RegisterPincodeAssociateActivity.class);
                                        startActivity(i);

                                       // startActivity(new Intent(OTPVerificationAssociateActivity.this, DashboardAssociateActivity.class));
                                        finish();
                                    }else if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.FORGOT_OTP)){
                                        startActivity(new Intent(OTPVerificationAssociateActivity.this, ResetPasswordAssociateActivity.class));
                                    }else if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.VERIFY_PINCODE_OTP)) {

                                        Toast.makeText(OTPVerificationAssociateActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                                       /* Snackbar.make(btn_verify, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
                                        thread.start();*/
                                        Intent i = new Intent(OTPVerificationAssociateActivity.this, DashboardAssociateActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();

                                        //URL = getResources().getString(R.string.url_domain) + getResources().getString(R.string.url_verify_phone_no_otp);
                                    }
                                    else if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.CHANGE_PHONE_OTP)) {
                                        Toast.makeText(OTPVerificationAssociateActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();

                                       // Snackbar.make(btn_verify, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
                                        hideKeyboard();
                                        finish();
                                        //URL = getResources().getString(R.string.url_domain) + getResources().getString(R.string.url_verify_phone_no_otp);
                                    } else {
                                        AppConstant.IS_USER_FROM_FACEBOOK_SIGNUP = true;

                                        mSessionManager.putIntData(SessionManager.KEY_WHICH_USER,2);
                                        Intent i = new Intent(OTPVerificationAssociateActivity.this, DashboardAssociateActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        //startActivity(new Intent(OTPVerificationAssociateActivity.this, DashboardAssociateActivity.class));
                                        finish();
                                    }
                                } else {
                                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(OTPVerificationAssociateActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                                    customDialogForHelp.show();

                                    //Snackbar.make(btn_verify, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
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
                    Map<String, String> params = new HashMap<String, String>();

                    if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.SIGNUP_OTP)||getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.FORGOT_OTP)){
                        params.put(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
                        params.put(JSONConstant.AO_OTP, editText_otp_code.getText().toString().trim());
                    }else if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.VERIFY_PINCODE_OTP)){
                        params.put(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
                        params.put(JSONConstant.AO_OTP, editText_otp_code.getText().toString().trim());
                        params.put("a_security_pin",strPincode);
                    }else {
                        params.put(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
                        params.put(JSONConstant.AO_OTP, editText_otp_code.getText().toString().trim());
                        params.put(JSONConstant.A_DIALLING_CODE,mSessionManager.getStringData(SessionManager.KEY_A_DIALLING_CODE));
                        params.put(JSONConstant.A_PHONE_NO,mSessionManager.getStringData(SessionManager.KEY_A_PHONE_NO));
                    }
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


    public void resendOTP() {

        try {
            progressBar = new ProgressDialog(OTPVerificationAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();
            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_resend_otp);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                String status = Json_response.getString(JSONConstant.STATUS);
                                //Toast.makeText(getApplicationContext(), "" + status, Toast.LENGTH_SHORT).show();
                                if (status.equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    startTimer();
                                    //startActivity(new Intent(OTPVerificationCustomerActivity.this, LoginCustomerActivity.class));
                                } else {
                                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(OTPVerificationAssociateActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                                    customDialogForHelp.show();

                                    //Snackbar.make(btn_verify, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();

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
                    Log.e(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
                    params.put(JSONConstant.A_DIALLING_CODE, mSessionManager.getStringData(SessionManager.KEY_A_DIALLING_CODE));
                    params.put(JSONConstant.A_PHONE_NO, mSessionManager.getStringData(SessionManager.KEY_A_PHONE_NO));
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
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_verify:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    validateAndSubmit();
                    //verifyOTP();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(OTPVerificationAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(tv_resend_otp,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_resend_otp:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    requestFocus(editText_otp_code);
                    //resendOTP();

                    if(getIntent().getStringExtra(AppConstant.SIGNUP_OR_FORGOT_OTP).equalsIgnoreCase(AppConstant.VERIFY_PINCODE_OTP)){
                        resendVerifyOTP();
                    }else {
                        resendOTP();

                    }
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(OTPVerificationAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(tv_resend_otp,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;
        }

    }

    private void validateAndSubmit(){
        if(editText_otp_code.getText().toString().isEmpty()){
            textInput_otp_code.setError("Enter OTP");
            requestFocus(editText_otp_code);
        }else {
            textInput_otp_code.setErrorEnabled(false);
            verifyOTP();
        }
    }
    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void resendVerifyOTP() {
        progressBar = new ProgressDialog(OTPVerificationAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();

        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_forgot_pin);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
        System.out.println("=>" + params.toString());

        VolleyUtils.POST_METHOD(OTPVerificationAssociateActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.dismiss();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());

                    if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        Toast.makeText(OTPVerificationAssociateActivity.this, Json_response.getString(JSONConstant.MESSAGE), Toast.LENGTH_SHORT).show();
                        startTimer();
                        //startActivity(new Intent(OTPVerificationCustomerActivity.this, OTPVerificationCustomerActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.VERIFY_PINCODE_OTP));
                    } else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(OTPVerificationAssociateActivity.this, getResources().getString(R.string.app_name), jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                        }
                    }
                    progressBar.dismiss();
                } catch (JSONException e) {
                    progressBar.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

}


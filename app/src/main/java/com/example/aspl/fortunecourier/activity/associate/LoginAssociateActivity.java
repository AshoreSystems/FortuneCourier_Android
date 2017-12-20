package com.example.aspl.fortunecourier.activity.associate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.LandingCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.LoginCustomerActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogClass;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 3/11/17.
 */

public class LoginAssociateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_email,editText_password;
    private Button btn_login,btn_fb_login;
    private TextView tv_link_signup,tv_forgot_password,tv_terms_and_condition;
    private CheckBox checkbox_remember_me,checkbox_terms_conditions;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    private TextInputLayout textInput_email,textInput_password;
    private LinearLayout linearLayout_or;
    private TextView tv_new_user;

    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    public final static int PERMISSIONS_REQUEST_FOR_READ_PHONE_STATE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());

        init();
    }

    private void init(){

        mSessionManager = new SessionManager(LoginAssociateActivity.this);
        cd = new ConnectionDetector(LoginAssociateActivity.this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        textInput_email = (TextInputLayout) findViewById(R.id.textInput_email);

        textInput_password = (TextInputLayout) findViewById(R.id.textInput_password);

        tv_new_user = (TextView) findViewById(R.id.tv_new_user);
        tv_new_user.setOnClickListener(this);

        String first = "New User? ";
        String next = "<font color='#EE0000'> Sign Up</font>";
        tv_new_user.setText(Html.fromHtml(first + next));

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_fb_login = (Button) findViewById(R.id.btn_fb_login);
        btn_fb_login.setOnClickListener(this);
        btn_fb_login.setVisibility(View.GONE);

        tv_link_signup = (TextView) findViewById(R.id.tv_link_signup);
        tv_link_signup.setOnClickListener(this);

        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);

        tv_terms_and_condition = (TextView) findViewById(R.id.tv_terms_and_condition);
        tv_terms_and_condition.setOnClickListener(this);

        checkbox_terms_conditions = (CheckBox) findViewById(R.id.checkbox_terms_conditions);

        checkbox_remember_me = (CheckBox) findViewById(R.id.checkbox_remember_me);

        linearLayout_or = (LinearLayout) findViewById(R.id.linearLayout_or);
        linearLayout_or.setVisibility(View.GONE);

        getIMEINumber();

        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_A_REMEMBERED)){
            editText_email.setText(mSessionManager.getStringData(SessionManager.KEY_A_EMAIL));
            editText_password.setText(mSessionManager.getStringData(SessionManager.KEY_A_PASSWORD));
            checkbox_remember_me.setChecked(true);
            checkbox_terms_conditions.setChecked(true);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    Snackbar.make(btn_login,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_new_user:
                hideKeyboard();
                startActivity(new Intent(LoginAssociateActivity.this,SignUpAssociateActivity.class));
                break;

            case R.id.tv_forgot_password:
                hideKeyboard();
                 startActivity(new Intent(LoginAssociateActivity.this,ForgotPasswordAssociateActivity.class));
                break;

          /*  case R.id.btn_fb_login:
                AppConstant.IS_USER_FROM_FACEBOOK_SIGNUP = true;

                loginButton.performClick();
                break;*/

            case R.id.tv_terms_and_condition:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                   showTermsAndConditions();
                }else {
                    Snackbar.make(tv_terms_and_condition,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                startActivity(new Intent(LoginAssociateActivity.this,LandingAssociateActivity.class));
                finish();
               /* hideKeyboard();
                finish();*/
                break;
        }

    }

    public void validateAndSubmit() {

        if (editText_email.getText().toString().trim().isEmpty()) {
            textInput_email.setError(getResources().getString(R.string.err_msg_email));
            requestFocus(editText_email);

        } else  if(editText_password.getText().toString().trim().isEmpty() || editText_password.getText().toString().trim().length() < 4 || editText_password.getText().toString().trim().length() > 10) {
            textInput_password.setError("between 4 and 10 alphanumeric characters");
            textInput_email.setErrorEnabled(false);
            requestFocus(editText_password);

        } else if(!checkbox_terms_conditions.isChecked()){
            Snackbar.make(checkbox_terms_conditions,getResources().getString(R.string.err_msg_terms_and_condition),Snackbar.LENGTH_SHORT).show();

        }else {
            textInput_password.setErrorEnabled(false);
            getUserLoginValidation();
        }
    }

    public void getUserLoginValidation() {

        try {
            progressBar = new ProgressDialog(LoginAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_login);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                String status = Json_response.getString(JSONConstant.STATUS);
                               if (status.equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                   mSessionManager.putStringData(SessionManager.KEY_A_ID,Json_response.getString(JSONConstant.A_ID));
                                   mSessionManager.putStringData(SessionManager.KEY_A_FIRST_NAME,Json_response.getString(JSONConstant.A_FIRST_NAME));
                                   mSessionManager.putStringData(SessionManager.KEY_A_LAST_NAME,Json_response.getString(JSONConstant.A_LAST_NAME));
                                   mSessionManager.putStringData(SessionManager.KEY_A_PROFILE_URL,Json_response.getString(JSONConstant.A_PROFILE_PIC));

                                   mSessionManager.putBooleanData(SessionManager.KEY_IS_A_LOGOUT,false);

                                   if(checkbox_remember_me.isChecked()){
                                        mSessionManager.putBooleanData(SessionManager.KEY_IS_A_REMEMBERED,true);
                                        mSessionManager.putStringData(SessionManager.KEY_A_EMAIL,editText_email.getText().toString().trim());
                                        mSessionManager.putStringData(SessionManager.KEY_A_PASSWORD,editText_password.getText().toString().trim());

                                   }else {
                                        mSessionManager.putBooleanData(SessionManager.KEY_IS_A_REMEMBERED,false);
                                   }

                                   mSessionManager.putIntData(SessionManager.KEY_WHICH_USER,2);
                                   startActivity(new Intent(LoginAssociateActivity.this, DashboardAssociateActivity.class));
                                } else {
                                   Snackbar.make(editText_email, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
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
                    params.put(JSONConstant.A_EMAIL_ADDRESS, editText_email.getText().toString().trim());
                    params.put(JSONConstant.A_PASSWORD, editText_password.getText().toString().trim());
                    params.put(JSONConstant.ADT_DEVICE_TYPE, "android");
                    params.put(JSONConstant.ADT_DEVICE_ID,IMEI_Number_Holder);
                    params.put(JSONConstant.ADT_DEVICE_TOKEN,"KEY_CDT_DEVICE_TOKEN");
                   // params.put(JSONConstant.ADT_DEVICE_TOKEN, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_TOKEN));
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
            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI_Number_Holder = mngr.getDeviceId();
            mSessionManager.putStringData(SessionManager.KEY_CDT_DEVICE_ID, IMEI_Number_Holder);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
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

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void showTermsAndConditions() {

        try {
            progressBar = new ProgressDialog(LoginAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_terms_n_privacy);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                 if( Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                     JSONObject jsonResponsePageContent = Json_response.getJSONObject(JSONConstant.PAGE_CONTENT);
                                     CustomDialogClass customDialogClass = new CustomDialogClass(LoginAssociateActivity.this,jsonResponsePageContent.getString(JSONConstant.PAGENAME),jsonResponsePageContent.getString(JSONConstant.PAGECONTENT));
                                     customDialogClass.show();
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

              /*  @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.C_FACEBOOK_ID,fb_id);
                    params.put(JSONConstant.C_FIRST_NAME, f_name);
                    params.put(JSONConstant.C_LAST_NAME, l_name);
                    params.put(JSONConstant.C_EMAIL_ADDRESS, email);
                    params.put(JSONConstant.C_PROFILE_PIC,profile_url);

                    return params;
                }*/

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
}

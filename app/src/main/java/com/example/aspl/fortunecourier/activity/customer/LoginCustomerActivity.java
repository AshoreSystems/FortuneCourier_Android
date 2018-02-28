package com.example.aspl.fortunecourier.activity.customer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.SplashActivity;
import com.example.aspl.fortunecourier.activity.associate.LoginAssociateActivity;
import com.example.aspl.fortunecourier.activity.associate.SignUpAssociateActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogClass;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 3/11/17.
 */

public class LoginCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_email,editText_password;
    private Button btn_login,btn_fb_login;
    private TextView tv_link_signup,tv_forgot_password,tv_terms_and_condition,tv_new_user;
    private CheckBox checkbox_remember_me,checkbox_terms_conditions;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    private TextInputLayout textInput_email,textInput_password;

    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    public final static int PERMISSIONS_REQUEST_FOR_READ_PHONE_STATE =1;

    // facebook implementation

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());


       /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
        StrictMode.setThreadPolicy(policy);*/

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker(){
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken){

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //nextActivity(newProfile);
                try{
               //     Profile profile = newProfile.getCurrentProfile();
               //     System.out.println("=======Acc Display Name==2==>"+newProfile.getFirstName()+"=="+newProfile.getLastName()+"==="+newProfile.getId()+"==");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        init();
    }

    private void init(){

        mSessionManager = new SessionManager(LoginCustomerActivity.this);
        cd = new ConnectionDetector(LoginCustomerActivity.this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        textInput_email = (TextInputLayout) findViewById(R.id.textInput_email);

        textInput_password = (TextInputLayout) findViewById(R.id.textInput_password);

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_fb_login = (Button) findViewById(R.id.btn_fb_login);
        btn_fb_login.setOnClickListener(this);

        tv_link_signup = (TextView) findViewById(R.id.tv_link_signup);
        tv_link_signup.setOnClickListener(this);

        tv_new_user = (TextView) findViewById(R.id.tv_new_user);
        tv_new_user.setOnClickListener(this);

        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);

        tv_terms_and_condition = (TextView) findViewById(R.id.tv_terms_and_condition);
        tv_terms_and_condition.setOnClickListener(this);

        checkbox_terms_conditions = (CheckBox) findViewById(R.id.checkbox_terms_conditions);

        checkbox_remember_me = (CheckBox) findViewById(R.id.checkbox_remember_me);

        getIMEINumber();

        String first = "New User? ";
        String next = "<font color='#EE0000'> Sign Up</font>";
        tv_new_user.setText(Html.fromHtml(first + next));

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends","email","user_birthday","public_profile");

        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)){
            editText_email.setText(mSessionManager.getStringData(SessionManager.KEY_C_EMAIL));
            editText_password.setText(mSessionManager.getStringData(SessionManager.KEY_C_PASSWORD));
            checkbox_remember_me.setChecked(true);
            checkbox_terms_conditions.setChecked(true);
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult){

                GraphRequest request =  GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if(response.getError() != null){
                                    // handle error
                                }else{
                                    // get email and id of the user
                                    System.out.println("=======Acc Display Name====>"+me.optString("email")+"=="+me.optString("id")+"=="+me.toString());
                                    verifyFacebookLogin(me.optString("id"),me.optString("first_name"),me.optString("last_name"),me.optString("email"),"https://graph.facebook.com/" + me.optString("id").toString() + "/picture?type=large");
                                    String fb_user_image ="https://graph.facebook.com/" + me.optString("id").toString() + "/picture?type=large";
                                    System.out.println(fb_user_image);

                                   /* fb_user_names = me.optString("first_name")+" "+me.optString("last_name");
                                    fb_user_emailid = me.optString("email");
                                    fb_user_image ="https://graph.facebook.com/" + me.optString("id").toString() + "/picture?type=large";
                                    fb_user_gender = me.optString("gender");
                                    fb_user_social_id = me.optString("id");
                                    fb_user_social_type = "facebook";*/
                                    System.out.println(me.optString("first_name")+" "+me.optString("last_name")+"-"+me.optString("email")+"-"+me.optString("gender")+"-"+me.optString("id"));


                                    //getUserLoginValidation(fb_user_emailid,fb_user_social_type,fb_user_social_id);
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel(){
                // App code
            }

            @Override
            public void onError(FacebookException exception){
                // App code
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    //AppConstant.IS_USER_FROM_FACEBOOK_SIGNUP = false;
                    validateAndSubmit();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(LoginCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(btn_login,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_link_signup:
                hideKeyboard();
                startActivity(new Intent(LoginCustomerActivity.this,SignUpCustomerActivity.class));
                break;

            case R.id.tv_forgot_password:
                hideKeyboard();

                startActivity(new Intent(LoginCustomerActivity.this,ForgotPasswordCustomerActivity.class));
                break;

            case R.id.btn_fb_login:
                hideKeyboard();
                AppConstant.IS_USER_FROM_FACEBOOK_SIGNUP = true;
                try{
                    LoginManager.getInstance().logOut();
                }catch (Exception e){
                    e.printStackTrace();
                }
                loginButton.performClick();

                break;

            case R.id.tv_terms_and_condition:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                   showTermsAndConditions();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(LoginCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(tv_terms_and_condition,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_new_user:
                hideKeyboard();
                startActivity(new Intent(LoginCustomerActivity.this,SignUpCustomerActivity.class));
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                startActivity(new Intent(LoginCustomerActivity.this,LandingCustomerActivity.class));
                finish();
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
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(LoginCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_terms_and_condition));
            customDialogForHelp.show();
            //Snackbar.make(checkbox_terms_conditions,getResources().getString(R.string.err_msg_terms_and_condition),Snackbar.LENGTH_SHORT).show();

        }else {
            textInput_password.setErrorEnabled(false);
            getUserLoginValidation();
        }
    }

    public void getUserLoginValidation() {

        try {
            progressBar = new ProgressDialog(LoginCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_login);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                String status = Json_response.getString(JSONConstant.STATUS);
                               if (status.equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                   mSessionManager.putStringData(SessionManager.KEY_C_ID,Json_response.getString(JSONConstant.C_ID));
                                   mSessionManager.putStringData(SessionManager.KEY_C_FIRST_NAME,Json_response.getString(JSONConstant.C_FIRST_NAME));
                                   mSessionManager.putStringData(SessionManager.KEY_C_LAST_NAME,Json_response.getString(JSONConstant.C_LAST_NAME));
                                   mSessionManager.putStringData(SessionManager.KEY_C_PROFILE_URL,Json_response.getString(JSONConstant.C_PROFILE_PIC));

                                   mSessionManager.putBooleanData(SessionManager.KEY_IS_C_LOGOUT,false);
                                   if(checkbox_remember_me.isChecked()){
                                        mSessionManager.putBooleanData(SessionManager.KEY_IS_C_REMEMBERED,true);
                                        mSessionManager.putStringData(SessionManager.KEY_C_EMAIL,editText_email.getText().toString().trim());
                                        mSessionManager.putStringData(SessionManager.KEY_C_PASSWORD,editText_password.getText().toString().trim());

                                   }else {
                                        mSessionManager.putBooleanData(SessionManager.KEY_IS_C_REMEMBERED,false);
                                   }

                                   mSessionManager.putIntData(SessionManager.KEY_WHICH_USER,1);
                                   if(AppConstant.IS_FROM_OUTSIDE){
                                       Intent i = new Intent(LoginCustomerActivity.this, DashboardCustomerActivity.class);
                                       startActivity(i);
                                       finish();
                                   }else {
                                       Intent i = new Intent(LoginCustomerActivity.this, DashboardCustomerActivity.class);
                                       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       startActivity(i);
                                   }
                                } else {
                                   CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(LoginCustomerActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                                   customDialogForHelp.show();
                                  // Snackbar.make(editText_email, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
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
                    params.put(JSONConstant.C_EMAIL_ADDRESS, editText_email.getText().toString().trim());
                    params.put(JSONConstant.C_PASSWORD, editText_password.getText().toString().trim());
                    params.put(JSONConstant.CDT_DEVICE_TYPE, "android");
                    params.put(JSONConstant.CDT_DEVICE_ID,IMEI_Number_Holder);
                    //params.put(JSONConstant.CDT_DEVICE_TOKEN, "kjdkjf");

                    params.put(JSONConstant.CDT_DEVICE_TOKEN, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_TOKEN));

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


    public void verifyFacebookLogin(final String fb_id, final String f_name, final String l_name, final String email, final String profile_url) {

        try {
            Log.e("Facebook Response",fb_id+" "+f_name+" "+l_name+" "+email);
            progressBar = new ProgressDialog(LoginCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_facebook_login);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                //String status = Json_response.getString(JSONConstant.STATUS);
                                //Toast.makeText(LoginCustomerActivity.this,status,Toast.LENGTH_SHORT).show();
                                mSessionManager.putStringData(SessionManager.KEY_C_ID,Json_response.getString(JSONConstant.C_ID));
                               // if (Json_response.has(JSONConstant.C_PHONE_NO)) {


                                if (Json_response.getString(JSONConstant.C_PHONE_NO).isEmpty()) {
                                    //mSessionManager.putStringData(SessionManager.KEY_C_ID,Json_response.getString(JSONConstant.C_ID));
                                   /* mSessionManager.putStringData(SessionManager.KEY_C_FIRST_NAME,f_name);
                                    mSessionManager.putStringData(SessionManager.KEY_C_LAST_NAME,l_name);
                                    mSessionManager.putStringData(SessionManager.KEY_C_EMAIL,email);
                                    mSessionManager.putStringData(SessionManager.KEY_FB_ID,fb_id);
                                    mSessionManager.putStringData(SessionManager.KEY_C_PROFILE_URL,profile_url);*/

                                    mSessionManager.putStringData(SessionManager.KEY_C_FIRST_NAME,Json_response.getString(JSONConstant.C_FIRST_NAME));
                                    mSessionManager.putStringData(SessionManager.KEY_C_LAST_NAME,Json_response.getString(JSONConstant.C_LAST_NAME));
                                    mSessionManager.putStringData(SessionManager.KEY_C_EMAIL,Json_response.getString(JSONConstant.C_EMAIL_ADDRESS));
                                    mSessionManager.putStringData(SessionManager.KEY_FB_ID,Json_response.getString(JSONConstant.C_FACEBOOK_ID));
                                    mSessionManager.putStringData(SessionManager.KEY_C_PROFILE_URL,Json_response.getString(JSONConstant.C_PROFILE_PIC));
                                    startActivity(new Intent(LoginCustomerActivity.this, SetPhoneNumberCustomerActivity.class));
                                }else{

                                    mSessionManager.putStringData(SessionManager.KEY_C_FIRST_NAME,Json_response.getString(JSONConstant.C_FIRST_NAME));
                                    mSessionManager.putStringData(SessionManager.KEY_C_LAST_NAME,Json_response.getString(JSONConstant.C_LAST_NAME));
                                    mSessionManager.putStringData(SessionManager.KEY_C_PROFILE_URL,Json_response.getString(JSONConstant.C_PROFILE_PIC));

                                    /* mSessionManager.putStringData(SessionManager.KEY_C_FIRST_NAME,f_name);
                                    mSessionManager.putStringData(SessionManager.KEY_C_LAST_NAME,l_name);
                                    mSessionManager.putStringData(SessionManager.KEY_C_PROFILE_URL,profile_url);*/

                                    mSessionManager.putIntData(SessionManager.KEY_WHICH_USER,1);

                                    if(AppConstant.IS_FROM_OUTSIDE){
                                        Intent i = new Intent(LoginCustomerActivity.this, DashboardCustomerActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        Intent i = new Intent(LoginCustomerActivity.this, DashboardCustomerActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }

                                }


                                /*if (status.equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    mSessionManager.putStringData(SessionManager.KEY_C_ID,Json_response.getString(JSONConstant.C_ID));
                                    mSessionManager.putStringData(SessionManager.KEY_FIRST_NAME,f_name);
                                    mSessionManager.putStringData(SessionManager.KEY_LAST_NAME,l_name);
                                    mSessionManager.putStringData(SessionManager.KEY_EMAIL,email);
                                    mSessionManager.putStringData(SessionManager.KEY_FB_ID,fb_id);

                                    startActivity(new Intent(LoginCustomerActivity.this, UpdateProfileCustomerActivity.class));

                                    if(Json_response.getString("c_facebook_signed").equalsIgnoreCase("0")){
                                        mSessionManager.putStringData(SessionManager.KEY_C_ID,Json_response.getString(JSONConstant.C_ID));
                                        mSessionManager.putStringData(SessionManager.KEY_FIRST_NAME,f_name);
                                        mSessionManager.putStringData(SessionManager.KEY_LAST_NAME,l_name);
                                        mSessionManager.putStringData(SessionManager.KEY_EMAIL,email);
                                        mSessionManager.putStringData(SessionManager.KEY_FB_ID,fb_id);
                                        startActivity(new Intent(LoginCustomerActivity.this, UpdateProfileCustomerActivity.class));
                                    }else {
                                        mSessionManager.putStringData(SessionManager.KEY_C_ID,Json_response.getString(JSONConstant.C_ID));
                                        startActivity(new Intent(LoginCustomerActivity.this, DashboardCustomerActivity.class));
                                    }
                                } else {
                                    Snackbar.make(editText_email, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
                                }*/
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
                    params.put(JSONConstant.C_FACEBOOK_ID,fb_id);
                    params.put(JSONConstant.C_FIRST_NAME, f_name);
                    params.put(JSONConstant.C_LAST_NAME, l_name);
                    params.put(JSONConstant.C_EMAIL_ADDRESS, email);
                    params.put(JSONConstant.C_PROFILE_PIC,profile_url);
                    params.put("device_type","a");
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    // TODO Auto-generated method stub
                    return "application/x-www-form-urlencoded";
                }

                /*@Override
                public byte[] getBody() throws AuthFailureError{
                    String your_string_json = jobj_data.toString(); // put your json
                    return your_string_json.getBytes();
                }*/
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void showTermsAndConditions() {

        try {
            progressBar = new ProgressDialog(LoginCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_terms_n_privacy);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                 if( Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                     JSONObject jsonResponsePageContent = Json_response.getJSONObject(JSONConstant.PAGE_CONTENT);
                                     CustomDialogClass customDialogClass = new CustomDialogClass(LoginCustomerActivity.this,jsonResponsePageContent.getString(JSONConstant.PAGENAME),jsonResponsePageContent.getString(JSONConstant.PAGECONTENT));
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

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

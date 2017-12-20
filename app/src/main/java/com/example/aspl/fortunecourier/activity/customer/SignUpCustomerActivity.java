package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
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
import com.example.aspl.fortunecourier.dialog.CustomDialogClass;
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
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 3/11/17.
 */

public class SignUpCustomerActivity extends Activity implements View.OnClickListener {
    private TextInputLayout textInput_firstName, textInput_lastName, textInput_phoneNumber, textInput_email, textInput_business_name, textInput_password,textInput_confirm_password;
    private EditText editText_firstName, editText_lastName, editText_phoneNumber, editText_email, editText_business_name, editText_password, editText_confirm_password;
    private CheckBox checkbox_terms_conditions;
    private Button btn_register;
    private TextView tv_existing_user,tv_terms_and_condition;
    private ProgressDialog progressBar;

    CountryCodePicker ccp;
    private SessionManager mSessionManager;
    private ConnectionDetector cd;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Button btn_fb_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_customer);
        FacebookSdk.sdkInitialize(getApplicationContext());

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

    private void init() {

        mSessionManager = new SessionManager(SignUpCustomerActivity.this);
        cd = new ConnectionDetector(SignUpCustomerActivity.this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        btn_fb_login = (Button) findViewById(R.id.btn_fb_login);
        btn_fb_login.setOnClickListener(this);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends","email","user_birthday","public_profile");

        textInput_firstName = (TextInputLayout) findViewById(R.id.textInput_firstName);
        textInput_lastName = (TextInputLayout) findViewById(R.id.textInput_lastName);
        textInput_phoneNumber = (TextInputLayout) findViewById(R.id.textInput_phoneNumber);
        textInput_email = (TextInputLayout) findViewById(R.id.textInput_email);
        textInput_business_name = (TextInputLayout) findViewById(R.id.textInput_business_name);
        textInput_password = (TextInputLayout) findViewById(R.id.textInput_password);
        textInput_confirm_password = (TextInputLayout) findViewById(R.id.textInput_confirm_password);

        editText_firstName = (EditText) findViewById(R.id.editText_firstName);
        editText_lastName = (EditText) findViewById(R.id.editText_lastName);
        editText_phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_business_name = (EditText) findViewById(R.id.editText_business_name);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_confirm_password = (EditText) findViewById(R.id.editText_confirm_password);

        checkbox_terms_conditions = (CheckBox) findViewById(R.id.checkbox_terms_conditions);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        tv_existing_user = (TextView) findViewById(R.id.tv_existing_user);
        tv_existing_user.setOnClickListener(this);

        tv_terms_and_condition = (TextView) findViewById(R.id.tv_terms_and_condition);
        tv_terms_and_condition.setOnClickListener(this);

        editText_business_name.setVisibility(View.GONE);

        //to get Country code
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                hideKeyboard();

                if (cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    Snackbar.make(btn_register,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_existing_user:
                hideKeyboard();

                startActivity(new Intent(SignUpCustomerActivity.this,LoginCustomerActivity.class));
                break;

            case R.id.tv_terms_and_condition:
                hideKeyboard();

                if (cd.isConnectingToInternet()){
                    showTermsAndConditions();
                }else {
                    Snackbar.make(btn_register,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.header_layout_back:
                startActivity(new Intent(SignUpCustomerActivity.this,LandingCustomerActivity.class));
                finish();
                hideKeyboard();
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
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void validateAndSubmit() {
        if (editText_firstName.getText().toString().trim().isEmpty() || editText_firstName.getText().toString().trim().length() < 3) {
            textInput_firstName.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_firstName);
        } else if (editText_lastName.getText().toString().trim().isEmpty() || editText_lastName.getText().toString().trim().length() < 3) {
            textInput_lastName.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_lastName);
            textInput_firstName.setErrorEnabled(false);
        } else if (editText_phoneNumber.getText().toString().trim().isEmpty() || editText_phoneNumber.getText().toString().trim().length() != 10) {
            textInput_phoneNumber.setError(getResources().getString(R.string.err_msg_phonenumber));
            requestFocus(editText_phoneNumber);
            textInput_lastName.setErrorEnabled(false);
        } else if (editText_email.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString().trim()).matches()) {
            textInput_email.setError(getResources().getString(R.string.err_msg_email));
            requestFocus(editText_email);
            textInput_phoneNumber.setErrorEnabled(false);
        } else if (editText_password.getText().toString().trim().isEmpty() || editText_password.getText().toString().trim().length() < 7 || editText_password.getText().toString().trim().length() > 10) {
            textInput_password.setError(getResources().getString(R.string.err_msg_password));
            requestFocus(editText_password);
            textInput_email.setErrorEnabled(false);
        } else if (editText_confirm_password.getText().toString().trim().isEmpty() || editText_confirm_password.getText().toString().trim().length() < 4 || editText_confirm_password.getText().toString().trim().length() > 10 || !(editText_confirm_password.getText().toString().trim().equals(editText_password.getText().toString().trim()))) {
            textInput_confirm_password.setError(getResources().getString(R.string.err_msg_confirm_password));
            requestFocus(editText_confirm_password);
            textInput_password.setErrorEnabled(false);
        } else if( !checkbox_terms_conditions.isChecked()){
            Snackbar.make(checkbox_terms_conditions,getResources().getString(R.string.err_msg_terms_and_condition),Snackbar.LENGTH_SHORT).show();
        } else {
            textInput_confirm_password.setErrorEnabled(false);
            textInput_firstName.setErrorEnabled(false);
            textInput_lastName.setErrorEnabled(false);
            textInput_phoneNumber.setErrorEnabled(false);
            textInput_email.setErrorEnabled(false);
            textInput_password.setErrorEnabled(false);

            getUserLoginValidation();
        }

    }

    public void getUserLoginValidation() {

        try {
            progressBar = new ProgressDialog(SignUpCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_customer_signup);

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
                                    mSessionManager.putStringData(SessionManager.KEY_C_DIALLING_CODE,"+"+ccp.getFullNumber());
                                    mSessionManager.putStringData(SessionManager.KEY_C_PHONE_NO,editText_phoneNumber.getText().toString().trim());
                                    clearData();
                                    startActivity(new Intent(SignUpCustomerActivity.this, OTPVerificationCustomerActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.SIGNUP_OTP));
                                    hideKeyboard();
                                    finish();
                                } else {
                                    JSONObject jsonObject = Json_response.getJSONObject("error_messages");

                                    if (jsonObject.has(JSONConstant.C_FIRST_NAME)) {
                                        textInput_firstName.setError(jsonObject.getString(JSONConstant.C_FIRST_NAME));
                                        requestFocus(editText_firstName);
                                    }
                                    if (jsonObject.has(JSONConstant.C_LAST_NAME)) {
                                        textInput_lastName.setError(jsonObject.getString(JSONConstant.C_LAST_NAME));
                                        requestFocus(editText_lastName);

                                    }if (jsonObject.has(JSONConstant.C_PHONE_NO)) {
                                        textInput_phoneNumber.setError(jsonObject.getString(JSONConstant.C_PHONE_NO));
                                        requestFocus(editText_phoneNumber);

                                    }if (jsonObject.has(JSONConstant.C_EMAIL_ADDRESS)) {
                                        textInput_email.setError(jsonObject.getString(JSONConstant.C_EMAIL_ADDRESS));
                                        requestFocus(editText_email);

                                    }if (jsonObject.has(JSONConstant.C_PASSWORD)) {
                                        textInput_confirm_password.setError(jsonObject.getString(JSONConstant.C_PASSWORD));
                                        requestFocus(editText_confirm_password);

                                    }if (jsonObject.has(JSONConstant.C_CONFIRM_PASSWORD)) {
                                        textInput_password.setError(jsonObject.getString(JSONConstant.C_CONFIRM_PASSWORD));
                                        requestFocus(editText_password);
                                    }
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
                    params.put(JSONConstant.C_FIRST_NAME, editText_firstName.getText().toString().trim());
                    params.put(JSONConstant.C_LAST_NAME, editText_lastName.getText().toString().trim());
                    params.put(JSONConstant.C_PHONE_NO, editText_phoneNumber.getText().toString().trim());
                    params.put(JSONConstant.C_EMAIL_ADDRESS, editText_email.getText().toString().trim());
                    params.put(JSONConstant.C_PASSWORD, editText_password.getText().toString().trim());
                    params.put(JSONConstant.C_CONFIRM_PASSWORD, editText_confirm_password.getText().toString().trim());
                    params.put(JSONConstant.C_DIALLING_CODE, "+"+ccp.getFullNumber());
                    params.put(JSONConstant.CDT_DEVICE_ID, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_ID));
                    params.put(JSONConstant.CDT_DEVICE_TOKEN, "kjdkjf");
                    //params.put(JSONConstant.CDT_DEVICE_TOKEN, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_TOKEN));
                    params.put(JSONConstant.CDT_DEVICE_TYPE, "android");
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




    private void clearData(){
         editText_firstName.setText("");
         editText_lastName.setText("");
         editText_phoneNumber.setText("");
         editText_email.setText("");
         editText_password.setText("");
         editText_confirm_password.setText("");
    }

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void showTermsAndConditions() {

        try {
            progressBar = new ProgressDialog(SignUpCustomerActivity.this);
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
                                    CustomDialogClass customDialogClass = new CustomDialogClass(SignUpCustomerActivity.this,jsonResponsePageContent.getString(JSONConstant.PAGENAME),jsonResponsePageContent.getString(JSONConstant.PAGECONTENT));
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
            progressBar = new ProgressDialog(SignUpCustomerActivity.this);
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
                                    mSessionManager.putStringData(SessionManager.KEY_C_FIRST_NAME,f_name);
                                    mSessionManager.putStringData(SessionManager.KEY_C_LAST_NAME,l_name);
                                    mSessionManager.putStringData(SessionManager.KEY_C_EMAIL,email);
                                    mSessionManager.putStringData(SessionManager.KEY_FB_ID,fb_id);
                                    mSessionManager.putStringData(SessionManager.KEY_C_PROFILE_URL,profile_url);
                                    startActivity(new Intent(SignUpCustomerActivity.this, SetPhoneNumberCustomerActivity.class));
                                }else{
                                    mSessionManager.putStringData(SessionManager.KEY_C_FIRST_NAME,f_name);
                                    mSessionManager.putStringData(SessionManager.KEY_C_LAST_NAME,l_name);
                                    mSessionManager.putIntData(SessionManager.KEY_WHICH_USER,1);
                                    mSessionManager.putStringData(SessionManager.KEY_C_PROFILE_URL,profile_url);
                                    startActivity(new Intent(SignUpCustomerActivity.this, DashboardCustomerActivity.class));
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

}




package com.example.aspl.fortunecourier.activity.associate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.aspl.fortunecourier.activity.customer.SignUpCustomerActivity;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
import com.example.aspl.fortunecourier.dialog.CustomDialogClass;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Country;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by aspl on 2/11/17.
 */

public class SignUpAssociateActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private EditText editText_firstName,editText_lastName,editText_phoneNumber,editText_email,editText_business_name,editText_password,editText_confirm_password;
    private TextInputLayout textInput_firstName,textInput_lastName,textInput_phoneNumber,textInput_email,textInput_business_name,textInput_password,textInput_confirm_password;

    private CheckBox checkbox_terms_conditions;
    private Button btn_register;
    private TextView tv_existing_user,tv_terms_and_condition;
    private CountryCodePicker ccp;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    ArrayList<Country> arrayListCountries;
    ArrayList<String> countries;
    ArrayList<String> states;
    //private Spinner spinner_country,spinner_state;
    private SearchableSpinner spinner_country,spinner_state;

    private String strCountryName, strStateName, strCityName,strCountryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_associate);
        init();
    }

    private void init() {

        mSessionManager = new SessionManager(SignUpAssociateActivity.this);
        cd = new ConnectionDetector(SignUpAssociateActivity.this);

        arrayListCountries = new ArrayList<>();
        countries = new ArrayList<>();
        //countries.add(0,getResources().getString(R.string.lbl_select_country));

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

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

        //spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        spinner_country.setTitle(getResources().getString(R.string.lbl_select_country));
        spinner_country.setOnItemSelectedListener(this);

        //spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_state = (SearchableSpinner) findViewById(R.id.spinner_state);
        spinner_state.setTitle(getResources().getString(R.string.lbl_select_state));
        spinner_state.setOnItemSelectedListener(this);

        //to get Country code
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        String first = "Existing Associate? ";
        String next = "<font color='#EE0000'>Login</font>";
        tv_existing_user.setText(Html.fromHtml(first + next));

        if(cd.isConnectingToInternet()){
            getAllCountries();
        }else {
           // Snackbar.make(btn_register,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                hideKeyboard();
                if (cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                   // Snackbar.make(btn_register,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_existing_user:
                startActivity(new Intent(SignUpAssociateActivity.this,LoginAssociateActivity.class));
                break;

            case R.id.tv_terms_and_condition:
                hideKeyboard();
                if (cd.isConnectingToInternet()){
                    showTermsAndConditions();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(btn_register,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.header_layout_back:
                hideKeyboard();
                startActivity(new Intent(SignUpAssociateActivity.this,LandingAssociateActivity.class));
                finish();
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
        } else if (editText_business_name.getText().toString().trim().isEmpty()){
            textInput_business_name.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_business_name);
            textInput_email.setErrorEnabled(false);
        } else if (editText_password.getText().toString().trim().isEmpty() || editText_password.getText().toString().trim().length() < 7 || editText_password.getText().toString().trim().length() > 10) {
            textInput_password.setError(getResources().getString(R.string.err_msg_password));
            requestFocus(editText_password);
            textInput_business_name.setErrorEnabled(false);
        } else if (editText_confirm_password.getText().toString().trim().isEmpty() || editText_confirm_password.getText().toString().trim().length() < 4 || editText_confirm_password.getText().toString().trim().length() > 10 || !(editText_confirm_password.getText().toString().trim().equals(editText_password.getText().toString().trim()))) {
            textInput_confirm_password.setError(getResources().getString(R.string.err_msg_confirm_password));
            requestFocus(editText_confirm_password);
            textInput_password.setErrorEnabled(false);
        } else if(spinner_country.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_country));
            customDialogForHelp.show();
           // Snackbar.make(textInput_business_name,getResources().getString(R.string.err_msg_select_country),Snackbar.LENGTH_SHORT).show();
        } else if(spinner_state.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_state));
            customDialogForHelp.show();
           // Snackbar.make(textInput_business_name,getResources().getString(R.string.err_msg_select_state),Snackbar.LENGTH_SHORT).show();
        }else if( !checkbox_terms_conditions.isChecked()){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_terms_and_condition));
            customDialogForHelp.show();
            //Snackbar.make(checkbox_terms_conditions,getResources().getString(R.string.err_msg_terms_and_condition),Snackbar.LENGTH_SHORT).show();
        } else {
            textInput_confirm_password.setErrorEnabled(false);
            textInput_firstName.setErrorEnabled(false);
            textInput_lastName.setErrorEnabled(false);
            textInput_business_name.setErrorEnabled(false);
            textInput_phoneNumber.setErrorEnabled(false);
            textInput_email.setErrorEnabled(false);
            textInput_password.setErrorEnabled(false);

            getUserLoginValidation();
        }

    }

    public void getUserLoginValidation() {

        try {
            progressBar = new ProgressDialog(SignUpAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_customer_signup);

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
                                    mSessionManager.putStringData(SessionManager.KEY_A_DIALLING_CODE,"+"+ccp.getFullNumber());
                                    mSessionManager.putStringData(SessionManager.KEY_A_PHONE_NO,editText_phoneNumber.getText().toString().trim());
                                    clearData();
                                    startActivity(new Intent(SignUpAssociateActivity.this, OTPVerificationAssociateActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.SIGNUP_OTP));
                                    hideKeyboard();
                                    finish();
                                } else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                                    if (jsonObject.has(JSONConstant.A_FIRST_NAME)) {
                                        textInput_firstName.setError(jsonObject.getString(JSONConstant.A_FIRST_NAME));
                                        requestFocus(editText_firstName);
                                    }
                                    if (jsonObject.has(JSONConstant.A_LAST_NAME)) {
                                        textInput_lastName.setError(jsonObject.getString(JSONConstant.A_LAST_NAME));
                                        requestFocus(editText_lastName);

                                    }if (jsonObject.has(JSONConstant.A_PHONE_NO)) {
                                        textInput_phoneNumber.setError(jsonObject.getString(JSONConstant.A_PHONE_NO));
                                        requestFocus(editText_phoneNumber);

                                    }if (jsonObject.has(JSONConstant.A_BUSINESS_NAME)){
                                        textInput_business_name.setError(jsonObject.getString(JSONConstant.A_PHONE_NO));
                                        requestFocus(editText_business_name);

                                    }if (jsonObject.has(JSONConstant.A_EMAIL_ADDRESS)) {
                                        textInput_email.setError(jsonObject.getString(JSONConstant.A_EMAIL_ADDRESS));
                                        requestFocus(editText_email);

                                    }if (jsonObject.has(JSONConstant.A_PASSWORD)) {
                                        textInput_confirm_password.setError(jsonObject.getString(JSONConstant.A_PASSWORD));
                                        requestFocus(editText_confirm_password);

                                    }if (jsonObject.has(JSONConstant.A_CONFIRM_PASSWORD)) {
                                        textInput_password.setError(jsonObject.getString(JSONConstant.A_CONFIRM_PASSWORD));
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
                           // Snackbar.make(btn_register,getResources().getString(R.string.err_msg_server),Snackbar.LENGTH_SHORT).show();
                            progressBar.dismiss();
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_server));
                            customDialogForHelp.show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.A_FIRST_NAME, editText_firstName.getText().toString().trim());
                    params.put(JSONConstant.A_LAST_NAME, editText_lastName.getText().toString().trim());
                    params.put(JSONConstant.A_PHONE_NO, editText_phoneNumber.getText().toString().trim());
                    params.put(JSONConstant.A_EMAIL_ADDRESS, editText_email.getText().toString().trim());
                    params.put(JSONConstant.A_PASSWORD, editText_password.getText().toString().trim());
                    params.put(JSONConstant.A_CONFIRM_PASSWORD, editText_confirm_password.getText().toString().trim());
                    params.put(JSONConstant.A_DIALLING_CODE, "+"+ccp.getFullNumber());
                    params.put(JSONConstant.ADT_DEVICE_ID, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_ID));
                    params.put(JSONConstant.ADT_DEVICE_TOKEN, mSessionManager.getStringData(SessionManager.KEY_CDT_DEVICE_TOKEN));
                    params.put(JSONConstant.ADT_DEVICE_TYPE, "android");
                    params.put(JSONConstant.A_COUNTRY_CODE,strCountryCode);
                    params.put(JSONConstant.A_STATE,strStateName);
                    params.put(JSONConstant.A_BUSINESS_NAME,editText_business_name.getText().toString().trim());
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
        editText_business_name.setText("");
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
            progressBar = new ProgressDialog(SignUpAssociateActivity.this);
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
                                    CustomDialogClass customDialogClass = new CustomDialogClass(SignUpAssociateActivity.this,jsonResponsePageContent.getString(JSONConstant.PAGENAME),jsonResponsePageContent.getString(JSONConstant.PAGECONTENT));
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

    public void getAllCountries() {

        try {

            progressBar = new ProgressDialog(SignUpAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_get_countries);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString("status").equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    JSONArray jsonArray = Json_response.getJSONArray("countries");

                                    for(int i = 0; i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Country country = new Country();
                                        country.setCountry_code(jsonObject.getString("country_code"));
                                        country.setCountry_name(jsonObject.getString("country_name"));
                                        arrayListCountries.add(country);
                                        countries.add(jsonObject.getString("country_name"));
                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignUpAssociateActivity.this,
                                            android.R.layout.simple_spinner_item, countries);
                                    // set the view for the Drop down list
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // set the ArrayAdapter to the spinner
                                    spinner_country.setAdapter(dataAdapter);

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

    public void getAllStates(final String country_code) {

        try {
            progressBar = new ProgressDialog(SignUpAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_get_states);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            states = new ArrayList<>();
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString("status").equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    JSONArray jsonArray = Json_response.getJSONArray("states");
                                    Country country = new Country();

                                    for(int i = 0; i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        states.add(jsonObject.getString("state_name"));
                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignUpAssociateActivity.this,
                                            android.R.layout.simple_spinner_item, states);
                                    // set the view for the Drop down list
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // set the ArrayAdapter to the spinner
                                    spinner_state.setAdapter(dataAdapter);
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
                    params.put(JSONConstant.COUNTRY_CODE,country_code);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_country:

                strCountryCode = arrayListCountries.get(position).getCountry_code();
                strCountryName = spinner_country.getItemAtPosition(position).toString();
                if(cd.isConnectingToInternet()){
                    getAllStates(arrayListCountries.get(position).getCountry_code());
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SignUpAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(btn_register,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }

                /*if(position==0){
                    strCountryCode=getResources().getString(R.string.lbl_select_country);
                    progressBar.dismiss();
                }else {
                    strCountryCode = arrayListCountries.get(position-1).getCountry_code();
                    strCountryName = spinner_country.getItemAtPosition(position-1).toString();
                    if(cd.isConnectingToInternet()){
                        getAllStates(arrayListCountries.get(position-1).getCountry_code());
                    }else {
                        Snackbar.make(btn_register,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                    }
                }*/


                break;

            case R.id.spinner_state:

                strStateName = spinner_state.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}

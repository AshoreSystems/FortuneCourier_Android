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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
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
 * Created by aspl on 24/11/17.
 */

public class CreateShipmentToActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    private Button btn_next;
    private TextView tv_from_or_to;
    private String strCountryName, strStateName, strCityName,strCountryCode;
  /*  private Spinner spinner_country;
    private Spinner spinner_state;*/
    private SearchableSpinner spinner_country;
    private SearchableSpinner spinner_state;
    private Spinner spinner_city;
    private EditText editText_zip,editText_city,editText_company,editText_contact_name,editText_addressline1,editText_addressline2,editText_phoneNumber;
    private TextInputLayout textInput_zip,textInput_city,textInput_company,textInput_contact_name,textInput_addressline1,textInput_addressline2,textInput_phoneNumber;
    private ImageView img_info;

    ArrayList<Country> arrayListCountries;
    ArrayList<String> countries;
    ArrayList<String> states;
    ConnectionDetector cd;
    private SessionManager mSessionManager;
    private CountryCodePicker ccp;

    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shipment_from);
        init();
    }

    public void init(){

        cd = new ConnectionDetector(this);
        mSessionManager = new SessionManager(this);

        arrayListCountries = new ArrayList<>();
        countries = new ArrayList<>();

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        tv_from_or_to = (TextView) findViewById(R.id.tv_from_or_to);
        tv_from_or_to.setText(getResources().getString(R.string.lbl_to));

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        //spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        spinner_country.setOnItemSelectedListener(this);

        //spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_state = (SearchableSpinner) findViewById(R.id.spinner_state);
        spinner_state.setOnItemSelectedListener(this);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);


        if(cd.isConnectingToInternet()){
            getAllCountries();
        }else {
            Snackbar.make(spinner_city,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
        }


        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        textInput_zip = (TextInputLayout) findViewById(R.id.textInput_zip);
        textInput_city = (TextInputLayout) findViewById(R.id.textInput_city);
        textInput_company = (TextInputLayout) findViewById(R.id.textInput_company);
        textInput_contact_name = (TextInputLayout) findViewById(R.id.textInput_contact_name);
        textInput_addressline1 = (TextInputLayout) findViewById(R.id.textInput_addressline1);
        textInput_addressline2 = (TextInputLayout) findViewById(R.id.textInput_addressline2);
        textInput_phoneNumber = (TextInputLayout) findViewById(R.id.textInput_phoneNumber);

        editText_zip = (EditText) findViewById(R.id.editText_zip);
        editText_city = (EditText) findViewById(R.id.editText_city);
        editText_company = (EditText) findViewById(R.id.editText_company);
        editText_contact_name = (EditText) findViewById(R.id.editText_contact_name);
        editText_addressline1 = (EditText) findViewById(R.id.editText_addressline1);
        editText_addressline2 = (EditText) findViewById(R.id.editText_addressline2);
        editText_phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                hideKeyboard();
               // startActivity(new Intent(CreateShipmentToActivity.this,ShipmentDetailsCustomerActivity.class));
                if (cd.isConnectingToInternet()){
                    validateAndNext();
                }else {
                    Snackbar.make(btn_next,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.img_info:
                hideKeyboard();
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentToActivity.this, "Help", getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;
        }

    }

    private void validateAndNext(){
        if (spinner_country.getSelectedItemPosition()==-1){
            Snackbar.make(spinner_country,getResources().getString(R.string.err_msg_select_country), Toast.LENGTH_SHORT).show();
        }
       /* else if(editText_company.getText().toString().trim().isEmpty()){
            //textInput_city.setErrorEnabled(false);
            textInput_company.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_company);
        }*/
        else if(editText_contact_name.getText().toString().trim().isEmpty()){
            // textInput_company.setErrorEnabled(false);
            textInput_contact_name.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_contact_name);
        }else if(editText_addressline1.getText().toString().trim().isEmpty()){
            textInput_contact_name.setErrorEnabled(false);
            textInput_addressline1.setError(getResources().getString(R.string.err_msg_addressline1));
            requestFocus(editText_addressline1);
        } else if(editText_zip.getText().toString().trim().isEmpty()){
            textInput_addressline1.setErrorEnabled(false);
            textInput_zip.setError(getResources().getString(R.string.err_msg_zip));
            requestFocus(editText_zip);
        }else if(editText_city.getText().toString().trim().isEmpty()){
            textInput_zip.setErrorEnabled(false);
            textInput_city.setError(getResources().getString(R.string.err_msg_city));
            requestFocus(editText_city);
        }else if(spinner_state.getSelectedItemPosition()==-1){
            textInput_city.setErrorEnabled(false);
            Snackbar.make(spinner_country,getResources().getString(R.string.err_msg_select_state), Toast.LENGTH_SHORT).show();
        }else if(editText_phoneNumber.getText().toString().trim().isEmpty() || editText_phoneNumber.getText().toString().trim().length() != 10){
            textInput_phoneNumber.setError(getResources().getString(R.string.err_msg_phonenumber));
            requestFocus(editText_phoneNumber);
        }else {
            textInput_phoneNumber.setErrorEnabled(false);
            textInput_company.setErrorEnabled(false);
            textInput_contact_name.setErrorEnabled(false);
            textInput_addressline1.setErrorEnabled(false);
            textInput_zip.setErrorEnabled(false);
            textInput_city.setErrorEnabled(false);

            mSessionManager.putStringData(SessionManager.KEY_T_COUNTRY_CODE,strCountryCode);
            mSessionManager.putStringData(SessionManager.KEY_T_STATE,strStateName);
            mSessionManager.putStringData(SessionManager.KEY_T_COMPANY,editText_company.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_T_CONTACT_NAME,editText_contact_name.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_T_ADDRESSLINE_1,editText_addressline1.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_T_ADDRESSLINE_2,editText_addressline2.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_T_ZIP_CODE,editText_zip.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_T_CITY,editText_city.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_T_DIALLING_CODE,"+"+ccp.getFullNumber());
            mSessionManager.putStringData(SessionManager.KEY_T_PHONE_NUMBER,editText_phoneNumber.getText().toString().trim());

           //startActivity(new Intent(CreateShipmentToActivity.this,ShipmentDetailsCustomerActivity.class));

           verifyAddress();
        }

    }


    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_country:
                strCountryName = spinner_country.getItemAtPosition(position).toString();
                strCountryCode = arrayListCountries.get(position).getCountry_code();
                if(cd.isConnectingToInternet()){
                    getAllStates(arrayListCountries.get(position).getCountry_code());
                }else {
                    Snackbar.make(spinner_city,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.spinner_state:
                strStateName = spinner_state.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void getAllCountries() {

        try {
            progressBar = new ProgressDialog(CreateShipmentToActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Aut2017-12-28henticating ...");
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

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateShipmentToActivity.this,
                                            android.R.layout.simple_spinner_item, countries);
                                    // set the view for the Drop down list
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // set the ArrayAdapter to the spinner
                                    spinner_country.setAdapter(dataAdapter);

                                }
                                progressBar.dismiss();

                                if(AppConstant.IS_FROM_CALCULATE_RATES){
                                    AppConstant.IS_FROM_CREATE_SHIPMENT = true;
                                    editText_zip.setText(AppConstant.T_C_ZIPCODE);
                                    Log.e("Harshada","->"+AppConstant.T_C_COUNTRY_CODE);
                                    for (int i = 0;i<arrayListCountries.size();i++) {
                                        System.out.println(arrayListCountries.get(i).getCountry_code()+"satte name ->"+ AppConstant.F_C_COUNTRY_CODE);
                                        if (arrayListCountries.get(i).getCountry_code().equalsIgnoreCase(AppConstant.F_C_COUNTRY_CODE)) {
                                            spinner_country.setItemOnPosition(i);

                                        }
                                    }
                                }

                                /*if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)) {
                                    if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_LOGOUT)) {
                                        //for outer check rate functionality

                                    } else {
                                        editText_zip.setText(AppConstant.T_C_ZIPCODE);
                                        Log.e("Harshada","->"+AppConstant.T_C_COUNTRY_CODE);
                                        for (int i = 0;i<arrayListCountries.size();i++) {
                                            System.out.println(arrayListCountries.get(i).getCountry_code()+"satte name ->"+ AppConstant.F_C_COUNTRY_CODE);
                                            if (arrayListCountries.get(i).getCountry_code().equalsIgnoreCase(AppConstant.F_C_COUNTRY_CODE)) {
                                                spinner_country.setItemOnPosition(i);

                                            }
                                        }
                                    }
                                }*/

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
            progressBar = new ProgressDialog(CreateShipmentToActivity.this);
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

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateShipmentToActivity.this,
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

    public void verifyAddress(){
        try {
            progressBar = new ProgressDialog(CreateShipmentToActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_validate_address);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            states = new ArrayList<>();
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    AppConstant.T_C_COUNTRY_CODE = strCountryCode;
                                    AppConstant.T_C_ZIPCODE = editText_zip.getText().toString().trim();
                                    progressBar.dismiss();
                                    startActivity(new Intent(CreateShipmentToActivity.this,ShipmentDetailsCustomerActivity.class));
                                }else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                                    Snackbar.make(editText_zip, jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();

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
                    params.put(JSONConstant.C_COUNTRY_CODE,strCountryCode);
                    params.put(JSONConstant.C_STATE,strStateName);
                    params.put(JSONConstant.C_CITY,editText_city.getText().toString().trim());
                    params.put(JSONConstant.C_ZIPCODE,editText_zip.getText().toString().trim());
                    params.put(JSONConstant.C_ADDRESS_LINE1,editText_addressline1.getText().toString().trim());
                    params.put(JSONConstant.C_ADDRESS_LINE2,editText_addressline1.getText().toString().trim());
                    params.put(JSONConstant.C_PHONE_NO,editText_phoneNumber.getText().toString().trim());
                    params.put(JSONConstant.C_DIALLING_CODE,"+"+ccp.getFullNumber());
                    params.put(JSONConstant.CONTACT_NAME,editText_contact_name.getText().toString().trim());
                    params.put(JSONConstant.COMPANY_NAME,editText_company.getText().toString().trim());
                    System.out.println("Harshada  =>"+params.toString());

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

}



package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Country;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;
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

public class CreateShipmentFromCustomerActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener,TextWatcher{

    private Button btn_next;
    private TextView tv_from_or_to,tv_select_country;
    private String strCountryName, strStateName, strCityName,strCountryCode;
    /*private Spinner spinner_country;
    private Spinner spinner_state;*/
    private SearchableSpinner spinner_country,spinner_state;
    private Spinner spinner_city;

    private EditText editText_zip,editText_city,editText_company,editText_contact_name,editText_addressline1,editText_addressline2,editText_phoneNumber,editText_country,editText_email;//,editText_address;
    private TextInputLayout textInput_zip,textInput_city,textInput_company,textInput_contact_name,textInput_addressline1,textInput_addressline2,textInput_phoneNumber,textInput_country,textInput_email;//,textInput_address;

    ArrayList<Country> arrayListCountries;
    ArrayList<String> countries;
    ArrayList<String> states;
    ConnectionDetector cd;
    private SessionManager mSessionManager;

    private ProgressDialog progressBar;
    private ImageView img_info;
    private CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shipment_from);
        init();
    }

    public void init(){

        cd = new ConnectionDetector(this);
        mSessionManager = new SessionManager(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        tv_from_or_to = (TextView) findViewById(R.id.tv_from_or_to);
        tv_from_or_to.setText(getResources().getString(R.string.lbl_from));

        tv_select_country = (TextView) findViewById(R.id.tv_select_country);
        textInput_country = (TextInputLayout) findViewById(R.id.textInput_country);
        editText_country = (EditText) findViewById(R.id.editText_country);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        spinner_country.setTitle(getResources().getString(R.string.select_country));
        spinner_country.setOnItemSelectedListener(this);

        spinner_state = (SearchableSpinner) findViewById(R.id.spinner_state);
        spinner_state.setTitle(getResources().getString(R.string.lbl_select_state));
        spinner_state.setOnItemSelectedListener(this);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        if(cd.isConnectingToInternet()){
            getAllCountries();
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
        }


        textInput_zip = (TextInputLayout) findViewById(R.id.textInput_zip);
        textInput_city = (TextInputLayout) findViewById(R.id.textInput_city);
        textInput_company = (TextInputLayout) findViewById(R.id.textInput_company);
        textInput_contact_name = (TextInputLayout) findViewById(R.id.textInput_contact_name);
        textInput_addressline1 = (TextInputLayout) findViewById(R.id.textInput_addressline1);
        textInput_addressline2 = (TextInputLayout) findViewById(R.id.textInput_addressline2);
        textInput_phoneNumber = (TextInputLayout) findViewById(R.id.textInput_phoneNumber);
        textInput_email = (TextInputLayout) findViewById(R.id.textInput_email);


        editText_zip = (EditText) findViewById(R.id.editText_zip);
        editText_zip.addTextChangedListener(this);
        editText_city = (EditText) findViewById(R.id.editText_city);
        editText_company = (EditText) findViewById(R.id.editText_company);
        editText_contact_name = (EditText) findViewById(R.id.editText_contact_name);
        editText_addressline1 = (EditText) findViewById(R.id.editText_addressline1);
        editText_addressline2 = (EditText) findViewById(R.id.editText_addressline2);
        editText_phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);
        editText_email = (EditText) findViewById(R.id.editText_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                hideKeyboard();
               if (cd.isConnectingToInternet()){
                    validateAndNext();
                }else {
                   CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                   customDialogForHelp.show();

                   //Snackbar.make(btn_next,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.header_layout_back:
               // AppConstant.IS_FROM_CREATE_SHIPMENT = false;
                hideKeyboard();
                finish();
               /* if(AppConstant.IS_FROM_OUTSIDE){
                    Intent intent = new Intent(CreateShipmentFromCustomerActivity.this,CheckRateScreenCustomerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }else {
                    hideKeyboard();
                    finish();
                }*/

                break;
            case R.id.img_info:
                hideKeyboard();
                DBInfo dbInfo = new DBInfo(CreateShipmentFromCustomerActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this, "Help", dbInfo.getDescription("Create Shipment From Screen"));
                customDialogForHelp.show();
                break;
        }

    }

    private void validateAndNext(){
        if (spinner_country.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_country));
            customDialogForHelp.show();

            // Snackbar.make(spinner_country,getResources().getString(R.string.err_msg_select_country), Toast.LENGTH_SHORT).show();
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
        } else if (!editText_email.getText().toString().trim().isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString().trim()).matches()) {
            textInput_email.setError(getResources().getString(R.string.err_msg_email));
            requestFocus(editText_email);
            textInput_contact_name.setErrorEnabled(false);
        } else if(editText_addressline1.getText().toString().trim().isEmpty()){
            textInput_email.setErrorEnabled(false);
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
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_state));
            customDialogForHelp.show();

            //Snackbar.make(spinner_country,getResources().getString(R.string.err_msg_select_state), Toast.LENGTH_SHORT).show();
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

            mSessionManager.putStringData(SessionManager.KEY_F_COUNTRY_CODE,strCountryCode);
            mSessionManager.putStringData(SessionManager.KEY_F_STATE,strStateName);
            mSessionManager.putStringData(SessionManager.KEY_F_COMPANY,editText_company.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_F_CONTACT_NAME,editText_contact_name.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_F_ADDRESSLINE_1,editText_addressline1.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_F_ADDRESSLINE_2,editText_addressline2.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_F_ZIP_CODE,editText_zip.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_F_CITY,editText_city.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_F_DIALLING_CODE,"+"+ccp.getFullNumber());
            mSessionManager.putStringData(SessionManager.KEY_F_PHONE_NUMBER,editText_phoneNumber.getText().toString().trim());

            //startActivity(new Intent(CreateShipmentFromCustomerActivity.this,CreateShipmentToCustomerActivity.class));
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
                strCountryName = arrayListCountries.get(position).getCountry_name();
                strCountryCode = arrayListCountries.get(position).getCountry_code();
                ccp.setCountryForNameCode(strCountryCode);

                if(cd.isConnectingToInternet()){
                    getAllStates(arrayListCountries.get(position).getCountry_code());
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();

                   // Snackbar.make(spinner_city,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.spinner_state:
                strStateName = states.get(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void getAllCountries() {
        try {
            progressBar = new ProgressDialog(CreateShipmentFromCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_get_from_countries);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString("status").equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    JSONArray jsonArray = Json_response.getJSONArray("countries");
                                    arrayListCountries = new ArrayList<>();
                                    countries = new ArrayList<>();
                                    for(int i = 0; i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Country country = new Country();
                                        country.setCountry_code(jsonObject.getString("country_code"));
                                        country.setCountry_name(jsonObject.getString("country_name"));
                                        arrayListCountries.add(country);
                                        countries.add(jsonObject.getString("country_name"));
                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateShipmentFromCustomerActivity.this,
                                            android.R.layout.simple_spinner_item, countries);
                                    // set the view for the Drop down list
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // set the ArrayAdapter to the spinner
                                    spinner_country.setAdapter(dataAdapter);
                                }
                                progressBar.dismiss();

                                if(mSessionManager.getBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES)){
                                    editText_zip.setText(AppConstant.F_C_ZIPCODE);
                                    editText_zip.setTextColor(ContextCompat.getColor(CreateShipmentFromCustomerActivity.this,R.color.colorBlack));

                                    editText_zip.setEnabled(false);
                                    //editText_zip.setClickable(false);
                                    Log.e("Harshada","->"+ AppConstant.F_C_COUNTRY_CODE);
                                    for (int i = 0;i<arrayListCountries.size();i++) {
                                        System.out.println(arrayListCountries.get(i).getCountry_code()+"satte name ->"+ AppConstant.F_C_COUNTRY_CODE);
                                        if (arrayListCountries.get(i).getCountry_code().equalsIgnoreCase(AppConstant.F_C_COUNTRY_CODE)) {
                                            spinner_country.setItemOnPosition(i);
                                            strCountryCode = arrayListCountries.get(i).getCountry_code();
                                            ccp.setCountryForNameCode(strCountryCode);

                                            getAllStates(arrayListCountries.get(i).getCountry_code());

                                            spinner_country.setVisibility(View.GONE);
                                            tv_select_country.setVisibility(View.GONE);
                                            textInput_country.setVisibility(View.VISIBLE);
                                            editText_country.setTextColor(ContextCompat.getColor(CreateShipmentFromCustomerActivity.this,R.color.colorBlack));
                                            editText_country.setText(arrayListCountries.get(i).getCountry_name());
                                            editText_country.setEnabled(false);
                                            //spinner_country.setClickable(false);

                                        }
                                    }
                                }
                               /* if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)) {
                                    if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_LOGOUT)) {
                                        //for outer check rate functionality

                                    } else {
                                        editText_zip.setText(AppConstant.F_C_ZIPCODE);
                                        Log.e("Harshada","->"+AppConstant.F_C_COUNTRY_CODE);
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
            progressBar = new ProgressDialog(CreateShipmentFromCustomerActivity.this);
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

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateShipmentFromCustomerActivity.this,
                                            android.R.layout.simple_spinner_item, states);
                                    // set the view for the Drop down list
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // set the ArrayAdapter to the spinner
                                    spinner_state.setAdapter(dataAdapter);
                                }
                                progressBar.dismiss();

                                if(mSessionManager.getBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES)){
                                    if(AppConstant.F_C_COUNTRY_CODE.equalsIgnoreCase("US")){
                                        for(int i = 0; i<states.size(); i++) {
                                            if(AppConstant.F_C_STATE.equalsIgnoreCase(states.get(i))){
                                                spinner_state.setItemOnPosition(i);
                                                editText_city.setText(AppConstant.F_C_CITY);
                                            }
                                        }
                                    }
                                }

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
            progressBar = new ProgressDialog(CreateShipmentFromCustomerActivity.this);
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
                            //states = new ArrayList<>();
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    AppConstant.F_C_COUNTRY_CODE = strCountryCode;
                                    AppConstant.F_C_ZIPCODE = editText_zip.getText().toString().trim();
                                    progressBar.dismiss();
                                    startActivity(new Intent(CreateShipmentFromCustomerActivity.this,CreateShipmentToCustomerActivity.class));
                                }else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                                    if (jsonObject.has(JSONConstant.MESSAGE)) {
                                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                                        customDialogForHelp.show();
                                        //Snackbar.make(editText_zip, jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                                    }

                                    if(mSessionManager.getBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES)&&jsonObject.has(JSONConstant.C_ZIPCODE)){
                                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.C_ZIPCODE));
                                        customDialogForHelp.show();
                                        //Snackbar.make(editText_zip, jsonObject.getString(JSONConstant.C_ZIPCODE), Snackbar.LENGTH_SHORT).show();
                                    }else if(!mSessionManager.getBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES)&&jsonObject.has(JSONConstant.C_ZIPCODE)){
                                        textInput_zip.setError(jsonObject.getString(JSONConstant.C_ZIPCODE));
                                    }

                                   /* if (jsonObject.has(JSONConstant.C_ZIPCODE)){
                                        textInput_zip.setError(jsonObject.getString(JSONConstant.C_ZIPCODE));
                                    }*/

                                    if (jsonObject.has(JSONConstant.COUNTRY_CODE)){
                                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.COUNTRY_CODE));
                                        customDialogForHelp.show();
                                        //Snackbar.make(editText_zip, jsonObject.getString(JSONConstant.COUNTRY_CODE), Snackbar.LENGTH_SHORT).show();
                                        //textInput_zip.setError(jsonObject.getString(JSONConstant.MESSAGE));
                                    }

                                    if (jsonObject.has(JSONConstant.C_STATE)){
                                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.C_STATE));
                                        customDialogForHelp.show();
                                        //Snackbar.make(editText_zip, jsonObject.getString(JSONConstant.C_STATE), Snackbar.LENGTH_SHORT).show();
                                        //textInput_city.setError(jsonObject.getString(JSONConstant.C_STATE));
                                    }

                                    if (jsonObject.has(JSONConstant.C_CITY)){
                                        textInput_city.setError(jsonObject.getString(JSONConstant.C_CITY));
                                    }

                                    if (jsonObject.has(JSONConstant.C_ADDRESS_LINE1)){
                                        textInput_addressline1.setError(jsonObject.getString(JSONConstant.C_ADDRESS_LINE1));
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
                    params.put(JSONConstant.C_COUNTRY_CODE,strCountryCode);
                    params.put(JSONConstant.C_COUNTRY_CODE,strCountryCode);
                    params.put(JSONConstant.C_STATE,strStateName);
                    params.put(JSONConstant.C_CITY,editText_city.getText().toString().trim());
                    params.put(JSONConstant.C_ZIPCODE,editText_zip.getText().toString().trim());
                    params.put(JSONConstant.C_ADDRESS_LINE1,editText_addressline1.getText().toString().trim());
                    params.put(JSONConstant.C_ADDRESS_LINE2,editText_addressline2.getText().toString().trim());
                    params.put(JSONConstant.C_PHONE_NO,editText_phoneNumber.getText().toString().trim());
                    params.put(JSONConstant.C_DIALLING_CODE,"+"+ccp.getFullNumber());
                    params.put(JSONConstant.CONTACT_NAME,editText_contact_name.getText().toString().trim());
                    params.put(JSONConstant.COMPANY_NAME,editText_company.getText().toString().trim());
                    params.put("from_email",editText_email.getText().toString().trim());

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

     /*  String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_get_states);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.COUNTRY_CODE,"IN");

        VolleyUtils.POST_METHOD(CreateShipmentFromCustomerActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response);

            }
        });
*/


    private void getZipcodeDetails(String strCountryCode,String strZipcode){
        hideKeyboard();
        progressBar = new ProgressDialog(CreateShipmentFromCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_zipcode_lookup);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_COUNTRY_CODE,strCountryCode);
        params.put(JSONConstant.C_ZIPCODE,strZipcode);


        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(CreateShipmentFromCustomerActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.dismiss();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        editText_city.setText(Json_response.getString(JSONConstant.C_CITY));
                        //String strState = Json_response.getString(JSONConstant.A_STATE);
                        for (int i = 0;i<states.size();i++) {
                            if (states.get(i).equalsIgnoreCase(Json_response.getString(JSONConstant.C_STATE))) {
                                spinner_state.setItemOnPosition(i);
                            }
                        }
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(btn_get_details,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.dismiss();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (AppConstant.IS_FROM_CREATE_SHIPMENT) {

            if (cd.isConnectingToInternet()) {
                if (s.length() == 5) {
                    if (spinner_country.getSelectedItemPosition() == -1) {
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_country));
                        customDialogForHelp.show();
                       // Snackbar.make(spinner_country, getResources().getString(R.string.err_msg_select_country), Toast.LENGTH_SHORT).show();
                    } else if (strCountryCode.equalsIgnoreCase("US")) {
                        getZipcodeDetails(strCountryCode, editText_zip.getText().toString());
                    }
                }
            } else {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CreateShipmentFromCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                customDialogForHelp.show();
                //Snackbar.make(spinner_city, getResources().getString(R.string.err_msg_internet), Snackbar.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void afterTextChanged(Editable s) {

    }

}

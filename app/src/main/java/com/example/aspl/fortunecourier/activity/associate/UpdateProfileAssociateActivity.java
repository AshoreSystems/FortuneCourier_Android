package com.example.aspl.fortunecourier.activity.associate;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.BuildConfig;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.CheckRateScreenCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.CreateShipmentToCustomerActivity;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Country;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.RoundedImageView;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 6/12/17.
 */

public class UpdateProfileAssociateActivity  extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener,TextWatcher {

    private TextInputLayout textInput_firstName, textInput_lastName, textInput_phoneNumber, textInput_email,textInput_addressline1,textInput_addressline2, textInput_zip,textInput_business_name, textInput_password,textInput_confirm_password,textInput_city;
    private EditText editText_firstName, editText_lastName,editText_email,editText_addressline1,editText_addressline2, editText_zip,editText_phoneNumber,  editText_business_name, editText_password, editText_confirm_password,editText_city;
    private Button btn_update_profile,btn_edit_profile_pic;
    private File imageUri;
    private String picturePath = null;
    //private Spinner spinner_country, spinner_state;
    private SearchableSpinner spinner_country, spinner_state;
    //private Spinner spinner_state;
    private Spinner spinner_city;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    ArrayList<Country> arrayListCountries;
    ArrayList<String> countries;
    ArrayList<String> states;
    private RoundedImageView img_profile_pic;
    private String strCountryName, strStateName, strCityName,strCountryCode;//,strAssociateStateName;
    private static final int TAKE_PHOTO=1;
    private static final int CHOOSE_FROM_GALLERY=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(UpdateProfileAssociateActivity.this);
        cd = new ConnectionDetector(UpdateProfileAssociateActivity.this);

        arrayListCountries = new ArrayList<>();
        countries = new ArrayList<>();

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        textInput_firstName = (TextInputLayout) findViewById(R.id.textInput_firstName);
        textInput_lastName = (TextInputLayout) findViewById(R.id.textInput_lastName);
        textInput_email = (TextInputLayout) findViewById(R.id.textInput_email);
        textInput_addressline1 = (TextInputLayout) findViewById(R.id.textInput_addressline1);
        textInput_addressline2 = (TextInputLayout) findViewById(R.id.textInput_addressline2);
        textInput_zip = (TextInputLayout) findViewById(R.id.textInput_zip);
        textInput_city = (TextInputLayout) findViewById(R.id.textInput_city);
        textInput_business_name = (TextInputLayout) findViewById(R.id.textInput_business_name);
        textInput_business_name.setVisibility(View.VISIBLE);

        editText_firstName = (EditText) findViewById(R.id.editText_firstName);
        editText_lastName = (EditText) findViewById(R.id.editText_lastName);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_addressline1 = (EditText) findViewById(R.id.editText_addressline1);
        editText_addressline2 = (EditText) findViewById(R.id.editText_addressline2);
        editText_zip = (EditText) findViewById(R.id.editText_zip);
        //editText_zip.addTextChangedListener(this);

        editText_city = (EditText) findViewById(R.id.editText_city);
        editText_business_name = (EditText) findViewById(R.id.editText_business_name);

        img_profile_pic = (RoundedImageView) findViewById(R.id.img_profile_pic);
        img_profile_pic.setOnClickListener(this);

       /* spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_country.setOnItemSelectedListener(this);

        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_state.setOnItemSelectedListener(this);*/

        //spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        spinner_country.setTitle(getResources().getString(R.string.lbl_select_country));
        spinner_country.setOnItemSelectedListener(this);

        //spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_state = (SearchableSpinner) findViewById(R.id.spinner_state);
        spinner_state.setTitle(getResources().getString(R.string.lbl_select_state));
        spinner_state.setOnItemSelectedListener(this);

        btn_update_profile = (Button) findViewById(R.id.btn_update_profile);
        btn_update_profile.setOnClickListener(this);

        btn_edit_profile_pic = (Button) findViewById(R.id.btn_edit_profile_pic);
        btn_edit_profile_pic.setOnClickListener(this);

        //editText_zip.addTextChangedListener(getTextWatcher(editText_zip));

        if(cd.isConnectingToInternet()){
            getCustomerDetails();
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
           // Snackbar.make(spinner_city,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
        }

    }

    public boolean verifyStoragePermissions(){
        boolean check_permission = false;

        int permissionCheck1 = ContextCompat.checkSelfPermission(UpdateProfileAssociateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(UpdateProfileAssociateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UpdateProfileAssociateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            check_permission =false;
        }else{
            check_permission =true;
        }
        return  check_permission;
    }

    public void validateAndUpdateProfile(){
        if (editText_firstName.getText().toString().trim().isEmpty() || editText_firstName.getText().toString().trim().length() < 3) {
            textInput_firstName.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_firstName);
        } else if (editText_lastName.getText().toString().trim().isEmpty() || editText_lastName.getText().toString().trim().length() < 3) {
            textInput_lastName.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_lastName);
            textInput_firstName.setErrorEnabled(false);
        } else if (editText_email.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString().trim()).matches()) {
            textInput_email.setError(getResources().getString(R.string.err_msg_email));
            requestFocus(editText_email);
            textInput_lastName.setErrorEnabled(false);
        } else if (editText_business_name.getText().toString().trim().isEmpty()){
            textInput_business_name.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_business_name);
            textInput_email.setErrorEnabled(false);
        }else if (editText_addressline1.getText().toString().trim().isEmpty() || editText_addressline1.getText().toString().trim().length() < 3) {
            textInput_addressline1.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_addressline1);
            textInput_business_name.setErrorEnabled(false);
        } else if(spinner_country.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_country));
            customDialogForHelp.show();
            // Snackbar.make(editText_zip,getResources().getString(R.string.err_msg_select_country),Snackbar.LENGTH_SHORT).show();
            textInput_addressline1.setErrorEnabled(false);
        } else if (editText_zip.getText().toString().trim().isEmpty()) {
            textInput_zip.setError(getResources().getString(R.string.err_msg_zip));
            requestFocus(editText_zip);
        }else if(spinner_state.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_state));
            customDialogForHelp.show();
            // Snackbar.make(editText_zip,getResources().getString(R.string.err_msg_select_country),Snackbar.LENGTH_SHORT).show();
            textInput_zip.setErrorEnabled(false);
        }
        else if (editText_city.getText().toString().trim().isEmpty() || editText_city.getText().toString().trim().length() < 3) {
            textInput_city.setError(getResources().getString(R.string.err_msg_name));
            requestFocus(editText_city);
            textInput_zip.setErrorEnabled(false);
        } else {
            textInput_city.setErrorEnabled(false);
            textInput_zip.setErrorEnabled(false);
            textInput_addressline1.setErrorEnabled(false);
            textInput_email.setErrorEnabled(false);
            textInput_lastName.setErrorEnabled(false);
            textInput_firstName.setErrorEnabled(false);
            textInput_business_name.setErrorEnabled(false);
            sendCustomerDetails();
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
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(spinner_city,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.spinner_state:
                strStateName = spinner_state.getItemAtPosition(position).toString();
                //Toast.makeText(UpdateProfileCustomerActivity.this,""+spinner_state.getItemAtPosition(position),Toast.LENGTH_SHORT).show();

                break;

            case R.id.spinner_city:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.btn_update_profile:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    validateAndUpdateProfile();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(spinner_city,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_profile_pic:
                hideKeyboard();
                if(Build.VERSION.SDK_INT>22) {
                    if (verifyStoragePermissions()) {
                        selectImage();
                    }
                }else{
                    selectImage();
                }
                break;

            case R.id.btn_edit_profile_pic:
                hideKeyboard();
                if(Build.VERSION.SDK_INT>22) {
                    if (verifyStoragePermissions()) {
                        selectImage();
                    }
                }else{
                    selectImage();
                }
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

    public void getAllCountries() {

        try {
           showProgressDialog();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_get_countries);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>()/*   for (int i = 0;i<arrayListCountries.size();i++) {

                                        if (arrayListCountries.get(i).getCountry_code().equalsIgnoreCase(strCustomerCode)) {
                                            spinner_country.setSelection(i);
                                            getAllStates(arrayListCountries.get(i).getCountry_code());
                                        }
                                    }*/ {
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
                                        strCountryCode =jsonObject.getString("country_code");

                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UpdateProfileAssociateActivity.this,
                                            android.R.layout.simple_spinner_item, countries);
                                    // set the view for the Drop down list
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // set the ArrayAdapter to the spinner
                                    spinner_country.setAdapter(dataAdapter);
                                   for (int i = 0;i<arrayListCountries.size();i++) {

                                       if (arrayListCountries.get(i).getCountry_code().equalsIgnoreCase(strCountryCode)) {
                                           //spinner_country.setSelection(i);
                                           spinner_country.setItemOnPosition(i);
                                           //spinner_country.setItemOnPosition(dataAdapter.getItem(dataAdapter.getPosition(countries.get(i)) ),i);

                                           getAllStates(arrayListCountries.get(i).getCountry_code());
                                       }
                                   }

                                }
                                closeProgressBar();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            closeProgressBar();
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
          showProgressDialog();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_get_states);


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
                                }


                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UpdateProfileAssociateActivity.this,
                                        android.R.layout.simple_spinner_item, states);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_state.setAdapter(dataAdapter);

                                for (int i = 0;i<states.size();i++) {

                                    if (states.get(i).equalsIgnoreCase(strStateName)) {
                                        //spinner_state.setSelection(i);
                                        //spinner_state.setItemOnPosition(dataAdapter.getItem(dataAdapter.getPosition(states.get(i)) ),i);
                                        spinner_state.setItemOnPosition(i);

                                        //getAllStates(arrayListCountries.get(i).getCountry_code());
                                    }
                                }
                                closeProgressBar();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            closeProgressBar();
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

    public void getCustomerDetails() {

        try {
           showProgressDialog();
            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_get_associate_details);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            states = new ArrayList<>();
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    editText_firstName.setText(Json_response.getString(JSONConstant.A_FIRST_NAME));
                                    editText_lastName.setText(Json_response.getString(JSONConstant.A_LAST_NAME));
                                    editText_email.setText(Json_response.getString(JSONConstant.A_EMAIL_ADDRESS));
                                    editText_business_name.setText(Json_response.getString(JSONConstant.A_BUSINESS_NAME));
                                    editText_addressline1.setText(Json_response.getString(JSONConstant.A_ADDRESS_LINE1));
                                    editText_addressline2.setText(Json_response.getString(JSONConstant.A_ADDRESS_LINE2));
                                    strCountryCode = Json_response.getString(JSONConstant.A_COUNTRY_CODE);

                                    editText_city.setText(Json_response.getString(JSONConstant.A_CITY));
                                    strStateName= Json_response.getString(JSONConstant.A_STATE);
                                    System.out.println("Image ->"+Json_response.getString(JSONConstant.A_PROFILE_PIC));

                                    mSessionManager.putStringData(SessionManager.KEY_A_FIRST_NAME,Json_response.getString(JSONConstant.A_FIRST_NAME));
                                    mSessionManager.putStringData(SessionManager.KEY_A_LAST_NAME,Json_response.getString(JSONConstant.A_LAST_NAME));
                                    mSessionManager.putStringData(SessionManager.KEY_A_PROFILE_URL,Json_response.getString(JSONConstant.A_PROFILE_PIC));
                                    editText_zip.setText(Json_response.getString(JSONConstant.A_ZIPCODE));

                                    // editText_city.setText(Json_response.getString(JSONConstant.A_CITY));
                                    Picasso.with(UpdateProfileAssociateActivity.this)
                                            .load(Json_response.getString(JSONConstant.A_PROFILE_PIC)) //extract as User instance method
                                            //.transform(new CropCircleTransformation())
                                            .resize(120, 120)
                                            .into(img_profile_pic);

                                    closeProgressBar();
                                    getAllCountries();
                                }else {

                                }
                                closeProgressBar();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            closeProgressBar();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));

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


    public void sendCustomerDetails(){
        try {
           showProgressDialog();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_update_associate_profile);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            states = new ArrayList<>();
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    mSessionManager.putStringData(SessionManager.KEY_A_FIRST_NAME,editText_firstName.getText().toString().trim());
                                    mSessionManager.putStringData(SessionManager.KEY_A_LAST_NAME,editText_lastName.getText().toString().trim());
                                    mSessionManager.putStringData(SessionManager.KEY_A_PROFILE_URL,Json_response.getString(JSONConstant.A_PROFILE_PIC));
                                    //UpdateDrawerObeserver.getInstance().updateValue(getApplicationContext());
                                    //Snackbar.make(btn_edit_profile_pic,getResources().getString(R.string.msg_update_profile),Snackbar.LENGTH_SHORT).show();
                                    closeProgressBar();
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.msg_update_profile),Toast.LENGTH_SHORT).show();
                                     finish();
                                  /*  progressBar.dismiss();
                                    thread.start();*/

                                    // getAllCountries();
                                }else {

                                }
                                closeProgressBar();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            closeProgressBar();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));
                    params.put(JSONConstant.A_FIRST_NAME,editText_firstName.getText().toString().trim());
                    params.put(JSONConstant.A_LAST_NAME,editText_lastName.getText().toString().trim());
                    params.put(JSONConstant.A_EMAIL_ADDRESS,editText_email.getText().toString().trim());
                    //params.put(JSONConstant.C_PASSWORD,editText_firstName.getText().toString().trim());
                    // params.put(JSONConstant.C_CONFIRM_PASSWORD,editText_firstName.getText().toString().trim());
                    params.put(JSONConstant.A_COUNTRY_CODE,strCountryCode);
                    params.put(JSONConstant.A_STATE,strStateName);
                    params.put(JSONConstant.A_ZIPCODE,editText_zip.getText().toString().trim());
                    params.put(JSONConstant.A_ADDRESS_LINE1,editText_addressline1.getText().toString().trim());
                    params.put(JSONConstant.A_ADDRESS_LINE2,editText_addressline2.getText().toString().trim());
                    params.put(JSONConstant.A_CITY,editText_city.getText().toString().trim());
                    params.put(JSONConstant.A_BUSINESS_NAME,editText_business_name.getText().toString().trim());

                    if (imageUri != null){
                        //File image_file = new File(imageUri);
                        Bitmap selectedImage = decodeFile(imageUri);
                        // rotated
                        Bitmap thumbnail1 = imageOreintationValidator(selectedImage,imageUri.toString());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thumbnail1.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                        byte[] byteArray = stream.toByteArray();
                        String strBase64 = Base64.encodeToString(byteArray, 0);

                        params.put(JSONConstant.A_PROFILE_PIC,strBase64);
                        Log.e("check", "check6 "+strBase64);
                        System.out.println(strBase64);

                       /* if (myimage != null) {
                            try {
                                byte[] image;
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                myimage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                image = stream.toByteArray();
                                String strBase64 = Base64.encodeToString(image, 0);
                                params.put("profile_image", strBase64);
                                Log.e("###strBase64", "###strBase64" + strBase64);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }*/

                    }else{
                        params.put(JSONConstant.A_PROFILE_PIC,"");
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

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private Bitmap decodeFile(File f){
        try {
            // Decodes image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size to scale to
            final int REQUIRED_SIZE = 100;

            // Finds the correct scale value which should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decodes with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),null, o2);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            source.recycle();
            Date d = new Date();
            CharSequence s = DateFormat
                    .format("MM-dd-yy-hh-mm-ss", d.getTime());
            String fullPath = Environment.getExternalStorageDirectory()
                    + "/FortuneCourier_pic/" + s.toString() + ".jpg";
            if ((fullPath != null) && (new File(fullPath).exists())) {
                new File(fullPath).delete();
            }
            bitmap = null;
            err.printStackTrace();
        }
        return bitmap;
    }

    /**-----------------------on Activity Result----------------------------------------**/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                File selectedImage = imageUri;

                try {
                    Uri ext = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", selectedImage);
                    if (ext.toString().contains("png") || ext.toString().contains("jpeg") || ext.toString().contains("jpg")
                            || ext.toString().contains("gif")) {
                        //File image_file = new File(picturePath);
                        Bitmap thumbnail = decodeFile(selectedImage);
                        // rotated
                        Bitmap thumbnail1 = imageOreintationValidator(thumbnail, selectedImage.toString());
                        img_profile_pic.setImageBitmap(thumbnail1);
                    } else {
                        img_profile_pic.setBackgroundResource(R.drawable.ic_profile);
                        /*CustomDialogClass cd = new CustomDialogClass(getActivity(),
                                getResources().getString(R.string.text_invalid_file), getResources().getString(R.string.text_select_image_file));
                        cd.show();*/
                    }
                } catch (Exception e) {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_failed_to_load));
                    customDialogForHelp.show();
                  //  Snackbar.make(editText_city,"Failed to load",Snackbar.LENGTH_SHORT).show();
                  /*  Toast toast = Toast.makeText(this, getResources().getString(R.string.failed_to_load), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();*/
                }
            } else if (requestCode == CHOOSE_FROM_GALLERY) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                File file_img = new File(picturePath);
                imageUri = file_img;
                c.close();
                Bitmap thumbnail = decodeSampledBitmapFromResource(picturePath, 500, 500);
                img_profile_pic.setImageBitmap(thumbnail);
            } else {
                img_profile_pic.setBackgroundResource(R.drawable.ic_profile);
            }
        }
    }

    /**----------------------------------Image Coding---------------------------**/
    private void selectImage() {

        final CharSequence[] options = { getResources().getString(R.string.account_photo_option1), getResources().getString(R.string.account_photo_option2),getResources().getString(R.string.account_photo_option3) };

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileAssociateActivity.this);
        builder.setTitle(getResources().getString(R.string.account_photo_title));

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(item==0)
                {
                    try{
                        if(Build.VERSION.SDK_INT>=23){
                            int permissionCheckCamera1 = ContextCompat.checkSelfPermission(UpdateProfileAssociateActivity.this, Manifest.permission.CAMERA);
                            if (permissionCheckCamera1 != PackageManager.PERMISSION_GRANTED ){
                                ActivityCompat.requestPermissions(UpdateProfileAssociateActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                            }else{
                                openCamerMethod();
                            }
                        }else{
                            openCamerMethod();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if (item ==1)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, CHOOSE_FROM_GALLERY);
                }
                else if (item ==2) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    public void openCamerMethod(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        String root_sd = Environment.getExternalStorageDirectory().toString();
        File direct = new File(root_sd + "/FortuneCourier_pic");

        if (!direct.exists()) {
            if (!direct.mkdir()){
                Log.d("ok", "FortuneCourier_pic folder not created");
            }else{
                Log.d("ok", "FortuneCourier_pic folder created");
            }
        }
        Date d = new Date();
        CharSequence s = DateFormat.format("MM-dd-yy-hh-mm-ss",d.getTime());

	/*	File photo = new File(Environment.getExternalStorageDirectory()
				+ "/FortuneCourier_pic/"+ s.toString() + ".jpg");*/

        File f_img =  new File(Environment.getExternalStorageDirectory()
                + "/FortuneCourier_pic/"+ s.toString() + ".jpg");
        Uri mesgUri1 = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", f_img);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mesgUri1);
        imageUri = f_img;
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,int reqWidth,int reqHeight){

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (cd.isConnectingToInternet()) {
                if (s.length() == 5) {
                   /* if (strCountryCode!=null&&(strCountryCode.equalsIgnoreCase("US")||strCountryCode.equalsIgnoreCase("USA"))) {
                        getZipcodeDetails(strCountryCode, editText_zip.getText().toString());
                    }*/
                 /* if (spinner_country.getSelectedItemPosition() == -1) {
                        Snackbar.make(spinner_country, getResources().getString(R.string.err_msg_select_country), Toast.LENGTH_SHORT).show();
                    }
                  else */
                  if (strCountryCode.equalsIgnoreCase("US")) {
                        getZipcodeDetails(strCountryCode, editText_zip.getText().toString());
                    }
                }
            } else {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                customDialogForHelp.show();
                //Snackbar.make(spinner_city, getResources().getString(R.string.err_msg_internet), Snackbar.LENGTH_SHORT).show();
            }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private void getZipcodeDetails(String strCountryCode,String strZipcode){
        hideKeyboard();
        showProgressDialog();
        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_zipcode_lookup);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_COUNTRY_CODE,strCountryCode);
        params.put(JSONConstant.A_ZIPCODE,strZipcode);


        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(UpdateProfileAssociateActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                closeProgressBar();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        closeProgressBar();

                        editText_city.setText(Json_response.getString(JSONConstant.A_CITY));
                        //String strState = Json_response.getString(JSONConstant.A_STATE);
                        for (int i = 0;i<states.size();i++) {
                            if (states.get(i).equalsIgnoreCase(Json_response.getString(JSONConstant.A_STATE))) {
                                spinner_state.setItemOnPosition(i);
                            }
                        }
                    }else {
                        closeProgressBar();

                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(UpdateProfileAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(btn_get_details,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    closeProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressBar();
                }
            }
        });
    }

    private void closeProgressBar()
    {
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
    }

    public void showProgressDialog()
    {
        progressBar = new ProgressDialog(UpdateProfileAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }
}


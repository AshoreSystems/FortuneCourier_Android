package com.example.aspl.fortunecourier.activity.associate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.DBHelper.DBCommodity;
import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.CheckRatesFromCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.ShipmentDetailsCustomerActivity;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Country;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 28/11/17.
 */

public class CheckRatesToAssociateActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btn_next;
    private TextView tv_from_or_to;
   // private Spinner spinner_country;
    private EditText editText_zip;
    private TextInputLayout textInput_zip;
    private ConnectionDetector cd;
    private ProgressDialog progressBar;
    ArrayList<Country> arrayListCountries;
    ArrayList<String> countries;
    private String strCountryCode;
    private ImageView img_info;
    SearchableSpinner spinner_country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rates_from);
        init();
    }

    private void init() {

        cd = new ConnectionDetector(this);

        arrayListCountries = new ArrayList<>();
        countries = new ArrayList<>();

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        tv_from_or_to = (TextView) findViewById(R.id.tv_from_or_to);
        tv_from_or_to.setText(getResources().getString(R.string.lbl_to));

        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        spinner_country.setTitle(getResources().getString(R.string.lbl_select_country));
        spinner_country.setOnItemSelectedListener(this);
        AppConstant.SPINNER_TITLE = getResources().getString(R.string.lbl_select_country);


        editText_zip = (EditText) findViewById(R.id.editText_zip);

        textInput_zip = (TextInputLayout) findViewById(R.id.textInput_zip);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        //countries.add(0,getResources().getString(R.string.lbl_select_country));


        if (cd.isConnectingToInternet()) {
            getAllCountries();
        } else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRatesToAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
            //Snackbar.make(btn_next, getResources().getString(R.string.err_msg_internet), Snackbar.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                hideKeyboard();
                if (cd.isConnectingToInternet()) {
                    validateAndSubmit();
                } else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRatesToAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(btn_next, getResources().getString(R.string.err_msg_internet), Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.img_info:
                DBInfo dbInfo = new DBInfo(CheckRatesToAssociateActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRatesToAssociateActivity.this,"Help",dbInfo.getDescription("Calculate Rates To Screen"));
                customDialogForHelp.show();
                break;
        }

    }

    private void validateAndSubmit(){
        if(spinner_country.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRatesToAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_country));
            customDialogForHelp.show();
            //Snackbar.make(editText_zip,getResources().getString(R.string.err_msg_select_country),Snackbar.LENGTH_SHORT).show();
        } else if(editText_zip.getText().toString().trim().isEmpty()) {
            textInput_zip.setError(getResources().getString(R.string.err_msg_zip));
            requestFocus(editText_zip);
        } else {
            verifyAddress();
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void getAllCountries() {

        try {

            showProgressDialog();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_get_to_countries);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if (Json_response.getString("status").equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    JSONArray jsonArray = Json_response.getJSONArray("countries");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Country country = new Country();
                                        country.setCountry_code(jsonObject.getString("country_code"));
                                        country.setCountry_name(jsonObject.getString("country_name"));
                                        arrayListCountries.add(country);
                                        countries.add(jsonObject.getString("country_name"));
                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CheckRatesToAssociateActivity.this,
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.COUNTRY_CODE,AppConstant.F_A_COUNTRY_CODE);
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

    public void showProgressDialog(){
        progressBar = new ProgressDialog(CheckRatesToAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }

    public void verifyAddress() {
        try {
            showProgressDialog();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_validate_postal_code);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    AppConstant.T_A_COUNTRY_CODE = strCountryCode;
                                    AppConstant.T_A_ZIPCODE = editText_zip.getText().toString().trim();
                                    AppConstant.T_A_CITY = Json_response.getString("a_city");
                                    AppConstant.T_A_STATE =Json_response.getString("a_state");
                                    progressBar.dismiss();
                                    hideKeyboard();
                                    DBCommodity dbCommodity = new DBCommodity(CheckRatesToAssociateActivity.this);
                                    dbCommodity.deleteTableData(CheckRatesToAssociateActivity.this);
                                    startActivity(new Intent(CheckRatesToAssociateActivity.this, ShipmentDetailsAssociateActivity.class));
                                } else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                                    //Snackbar.make(editText_zip, jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRatesToAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                                    customDialogForHelp.show();
                                }
                                progressBar.dismiss();
                                hideKeyboard();

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
                            hideKeyboard();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.e("ZIP code", strCountryCode + "--" + editText_zip.getText().toString().trim());

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.A_COUNTRY_CODE, strCountryCode);
                    params.put(JSONConstant.A_ZIPCODE, editText_zip.getText().toString().trim());
                   /* params.put(JSONConstant.C_COUNTRY_CODE,strCountryCode);
                    params.put(JSONConstant.C_STATE,strStateName);
                    params.put(JSONConstant.C_CITY,editText_city.getText().toString().trim());
                    params.put(JSONConstant.C_ZIPCODE,editText_zip.getText().toString().trim());
                    params.put(JSONConstant.C_ADDRESS_LINE1,editText_address.getText().toString().trim());*/
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
        strCountryCode = arrayListCountries.get(position).getCountry_code();
        //strCountryCode = arrayListCountries.get(position-1).getCountry_code();
       /* if(position==0){
            strCountryCode=getResources().getString(R.string.lbl_select_country);
        }else {
            strCountryCode = arrayListCountries.get(position-1).getCountry_code();
        }*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

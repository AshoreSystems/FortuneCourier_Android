package com.example.aspl.fortunecourier.activity.customer;

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

import com.example.aspl.fortunecourier.DBHelper.DBCommodity;
import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.associate.UpdateProfileAssociateActivity;
import com.example.aspl.fortunecourier.adapter.ShipmentHistoryCustomerAdapter;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Commodity;
import com.example.aspl.fortunecourier.model.Country;
import com.example.aspl.fortunecourier.model.HistoryDetails;
import com.example.aspl.fortunecourier.model.QuantityDetails;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class CommodityCustomerActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private SearchableSpinner spinner_unit_of_measures,spinner_commodity,spinner_customs_currency,spinner_country_of_manufacture;
    private EditText editText_commodity_description,editText_quantity,editText_commodity_weight,editText_customs_value,editText_harmonized_code,editText_export_licence_no,editText_expiration_date;
    private TextInputLayout textInput_commodity_description,textInput_quantity,textInput_commodity_weight,textInput_customs_value;//,textInputLayout_harmonized_code,editText_export_licence_no,editText_expiration_date;

    private Button btn_add_commodity,btn_update_commodity;
    private TextView tv_weight_unit,tv_currency_unit;
    private String strWeightUnit="";
    private ConnectionDetector cd;
    private String strUnitOfMeasures,strAsTotals,strCountryOfManufacture,strCommodityId;
    private ProgressDialog progressBar;
    ArrayList<Country> arrayListCountries;
    ArrayList<String> countries;
    private ImageView img_info;

    ArrayList<QuantityDetails> arrayListQuantities;
    ArrayList<String> quantities;
    SessionManager mSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        init();
    }

    private void init(){
        cd = new ConnectionDetector(CommodityCustomerActivity.this);
        mSessionManager = new SessionManager(CommodityCustomerActivity.this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);


        spinner_unit_of_measures = (SearchableSpinner) findViewById(R.id.spinner_unit_of_measures);
        spinner_unit_of_measures.setOnItemSelectedListener(this);

        spinner_commodity = (SearchableSpinner) findViewById(R.id.spinner_commodity);
        spinner_commodity.setOnItemSelectedListener(this);

        spinner_customs_currency = (SearchableSpinner) findViewById(R.id.spinner_customs_currency);
        spinner_customs_currency.setOnItemSelectedListener(this);

        spinner_country_of_manufacture = (SearchableSpinner) findViewById(R.id.spinner_country_of_manufacture);
        spinner_country_of_manufacture.setOnItemSelectedListener(this);

        editText_commodity_description = (EditText) findViewById(R.id.editText_commodity_description);
        editText_quantity = (EditText) findViewById(R.id.editText_quantity);
        editText_commodity_weight = (EditText) findViewById(R.id.editText_commodity_weight);
        editText_customs_value = (EditText) findViewById(R.id.editText_customs_value);
        editText_harmonized_code = (EditText) findViewById(R.id.editText_harmonized_code);
        editText_export_licence_no = (EditText) findViewById(R.id.editText_export_licence_no);
        editText_expiration_date = (EditText) findViewById(R.id.editText_expiration_date);

        textInput_commodity_description = (TextInputLayout) findViewById(R.id.textInput_commodity_description);
        textInput_quantity = (TextInputLayout) findViewById(R.id.textInput_quantity);
        textInput_commodity_weight = (TextInputLayout) findViewById(R.id.textInput_commodity_weight);
        textInput_customs_value = (TextInputLayout) findViewById(R.id.textInput_customs_value);

        tv_weight_unit = (TextView) findViewById(R.id.tv_weight_unit);
        tv_currency_unit = (TextView) findViewById(R.id.tv_currency_unit);

        btn_add_commodity = (Button) findViewById(R.id.btn_add_commodity);
        btn_add_commodity.setOnClickListener(this);

        btn_update_commodity = (Button) findViewById(R.id.btn_update_commodity);
        btn_update_commodity.setOnClickListener(this);
        btn_update_commodity.setVisibility(View.GONE);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CommodityCustomerActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.array_customs_unit));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_commodity.setAdapter(dataAdapter);
        spinner_customs_currency.setAdapter(dataAdapter);

        spinner_commodity.setItemOnPosition(0);
        spinner_customs_currency.setItemOnPosition(0);

        Log.e("Harshada","F_COUNTRY_CODE"+mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE));
        if(mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE).equalsIgnoreCase("IN")){
            tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
            tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_inr));
            strWeightUnit = "KG";
        }else if(mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE).equalsIgnoreCase("CA")) {
            tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
            tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_cad));
            strWeightUnit = "LB";
        }else {
            tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
            tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_usd));
            strWeightUnit = "LB";
        }

        if(cd.isConnectingToInternet()){
            getUnitOfMeasures();
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
        }

        if(!getIntent().getStringExtra("action").equalsIgnoreCase("add")){
            strCommodityId = getIntent().getStringExtra("id");

            btn_update_commodity.setVisibility(View.VISIBLE);
            btn_add_commodity.setVisibility(View.GONE);
            DBCommodity dbCommodity = new DBCommodity(CommodityCustomerActivity.this);
            Commodity commodity = dbCommodity.getCommodity(getIntent().getStringExtra("id"));
            editText_commodity_description.setText(commodity.getCommodity_desc());
            editText_commodity_weight.setText(commodity.getCommodity_weight());
            editText_quantity.setText(commodity.getQuantity());
            editText_customs_value.setText(commodity.getCustoms_value());
            editText_harmonized_code.setText(commodity.getHarmonized_code());
            editText_export_licence_no.setText(commodity.getExport_license_no());
            editText_expiration_date.setText(commodity.getExpiration_date());
            tv_weight_unit.setText(commodity.getCommodity_weight_unit());
            tv_currency_unit.setText(commodity.getCustoms_value_unit());

            strUnitOfMeasures = commodity.getUnit_of_measures();
            strCountryOfManufacture = commodity.getCountry_of_manufacture();
            Log.e("Harshada","->"+strCountryOfManufacture+","+strUnitOfMeasures);

            if(commodity.getIsTotals().equalsIgnoreCase("total")){
                spinner_commodity.setItemOnPosition(0);
                spinner_customs_currency.setItemOnPosition(0);
                strAsTotals = "total";
            }else {
                spinner_commodity.setItemOnPosition(1);
                spinner_customs_currency.setItemOnPosition(1);
                strAsTotals = "unit";

            }

        }

    }

    private void validateAndSubmit(){
        if(editText_commodity_description.getText().toString().trim().isEmpty()){

            //editText_commodity_description.setError(getResources().getString(R.string.err_msg_enter_message));
            textInput_commodity_description.setError(getResources().getString(R.string.err_msg_enter_message));
            requestFocus(editText_commodity_description);

        }else if(spinner_unit_of_measures.getSelectedItemPosition()==-1){
            //editText_commodity_weight.set
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_unit_of_measure));
            customDialogForHelp.show();
            textInput_commodity_description.setErrorEnabled(false);

        }else if(editText_quantity.getText().toString().trim().isEmpty()){

            //editText_quantity.setError(getResources().getString(R.string.err_msg_enter_quantity));
            textInput_commodity_description.setErrorEnabled(false);
            textInput_quantity.setError(getResources().getString(R.string.err_msg_enter_quantity));
            requestFocus(editText_quantity);
        }
        else if(editText_commodity_weight.getText().toString().trim().isEmpty()){
           // editText_commodity_weight.setError(getResources().getString(R.string.err_msg_enter_commodity_weight));
            textInput_quantity.setErrorEnabled(false);
            textInput_commodity_weight.setError(getResources().getString(R.string.err_msg_enter_commodity_weight));
            requestFocus(editText_quantity);

        }else if(spinner_commodity.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_unit));
            customDialogForHelp.show();
        }
        else if(editText_customs_value.getText().toString().trim().isEmpty()){
           // editText_customs_value.setError(getResources().getString(R.string.err_msg_enter_customs_value));
            textInput_commodity_weight.setErrorEnabled(false);
            textInput_customs_value.setError(getResources().getString(R.string.err_msg_enter_customs_value));
            requestFocus(editText_quantity);

        }else if(spinner_customs_currency.getSelectedItemPosition()==-1){
            textInput_customs_value.setErrorEnabled(false);
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_unit));
            customDialogForHelp.show();
        }
        else if(spinner_country_of_manufacture.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_select_manufacture));
            customDialogForHelp.show();

        }else {
            textInput_commodity_description.setErrorEnabled(false);
            textInput_quantity.setErrorEnabled(false);
            textInput_commodity_weight.setErrorEnabled(false);
            textInput_customs_value.setErrorEnabled(false);

            Commodity commodity = new Commodity();
            commodity.setCommodity_desc(editText_commodity_description.getText().toString().trim());
            commodity.setUnit_of_measures(strUnitOfMeasures);
            commodity.setQuantity(editText_quantity.getText().toString().trim());
            commodity.setCommodity_weight(editText_commodity_weight.getText().toString().trim());
            //commodity.setCommodity_weight_unit(strCommodityUnit);
            commodity.setCommodity_weight_unit(strWeightUnit);
            commodity.setCustoms_value(editText_customs_value.getText().toString().trim());
            //commodity.setCustoms_value_unit(strCustomsCurrency);
            commodity.setCustoms_value_unit(tv_currency_unit.getText().toString());
            commodity.setCountry_of_manufacture(strCountryOfManufacture);
            commodity.setHarmonized_code(editText_harmonized_code.getText().toString().trim());
            commodity.setExport_license_no(editText_export_licence_no.getText().toString().trim());
            commodity.setExpiration_date(editText_expiration_date.getText().toString().trim());
            commodity.setIsTotals(strAsTotals);
            DBCommodity dbCommodity = new DBCommodity(CommodityCustomerActivity.this);
            //dbCommodity.addOrUpdateCommodity(commodity);
            if(!getIntent().getStringExtra("action").equalsIgnoreCase("add")) {
                dbCommodity.updateCommodity(strCommodityId,commodity);

            }else {
                dbCommodity.addCommodity(commodity);

            }
           // startActivity(new Intent(CommodityCustomerActivity.this,CommodityDetailsCustomerActivity.class));
            finish();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_commodity:
                if(cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;
            case R.id.btn_update_commodity:
                if(cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;
            case R.id.img_info:
                DBInfo dbInfo = new DBInfo(CommodityCustomerActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,"Help",dbInfo.getDescription("Calculate Rates Screen"));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

            case R.id.spinner_unit_of_measures:
                  strUnitOfMeasures = arrayListQuantities.get(position).getQuantity_unit();
                break;

            case R.id.spinner_commodity:
                spinner_customs_currency.setItemOnPosition(position);
                strAsTotals =spinner_commodity.getItemAtPosition(position).toString();
                if(position==0){
                    strAsTotals = "total";
                }else {
                    strAsTotals = "unit";
                }
                break;

            case R.id.spinner_customs_currency:
                spinner_commodity.setItemOnPosition(position);
                strAsTotals = spinner_customs_currency.getItemAtPosition(position).toString();
                if(position==0){
                    strAsTotals = "total";
                }else {
                    strAsTotals = "unit";
                }
                break;

            case R.id.spinner_country_of_manufacture:
                strCountryOfManufacture = arrayListCountries.get(position).getCountry_code();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getCountryOfManufacture(){

          //showProgressDialog();
            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_get_countries);


            VolleyUtils.GET_METHOD(CommodityCustomerActivity.this, URL, new VolleyResponseListener() {
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

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CommodityCustomerActivity.this, android.R.layout.simple_spinner_item, countries);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_country_of_manufacture.setAdapter(dataAdapter);
                                closeProgressBar();
                               if(!getIntent().getStringExtra("action").equalsIgnoreCase("add")){

                                   for(int i =0; i<arrayListCountries.size();i++){
                                       Log.e("Harshada","->"+strCountryOfManufacture+","+countries.get(i));

                                       if(arrayListCountries.get(i).getCountry_code().equalsIgnoreCase(strCountryOfManufacture)){
                                           spinner_country_of_manufacture.setItemOnPosition(i);
                                       }
                                   }


                               }

                        }else {
                            JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                            if (jsonObject.has(JSONConstant.MESSAGE)) {
                                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                                customDialogForHelp.show();
                                // Snackbar.make(img_info,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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

    private void getUnitOfMeasures(){

        showProgressDialog();
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_fetch_fedex_commodity_quantity_units);


        VolleyUtils.GET_METHOD(CommodityCustomerActivity.this, URL, new VolleyResponseListener() {
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
                        JSONArray jsonArray = Json_response.getJSONArray("quantity_units");
                        arrayListQuantities = new ArrayList<>();
                        quantities = new ArrayList<>();

                        for(int i = 0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            QuantityDetails quantityDetails = new QuantityDetails();
                            quantityDetails.setQuantity_unit(jsonObject.getString("quantity_unit"));
                            quantityDetails.setQuantity_description(jsonObject.getString("quantity_description"));

                            arrayListQuantities.add(quantityDetails);
                            quantities.add(jsonObject.getString("quantity_description"));

                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CommodityCustomerActivity.this, android.R.layout.simple_spinner_item, quantities);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_unit_of_measures.setAdapter(dataAdapter);

                        if(!getIntent().getStringExtra("action").equalsIgnoreCase("add")){
                            for(int i =0; i<arrayListQuantities.size();i++){
                                Log.e("Harshada","->"+strUnitOfMeasures+","+quantities.get(i));

                                if(arrayListQuantities.get(i).getQuantity_unit().equalsIgnoreCase(strUnitOfMeasures)){
                                    spinner_unit_of_measures.setItemOnPosition(i);
                                }
                            }

                        }
                        //closeProgressBar();
                        getCountryOfManufacture();

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            // Snackbar.make(img_info,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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

    public void showProgressDialog(){
        progressBar = new ProgressDialog(CommodityCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }

    private void closeProgressBar(){
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


}

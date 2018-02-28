package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.TrackShipmentDetailsActivity;
import com.example.aspl.fortunecourier.activity.associate.TrackShipmentAssociateActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 9/1/18.
 */

public class TrackShipmentCustomerActivity extends Activity implements View.OnClickListener{

    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    private EditText editText_track_id;
    private Button btn_get_details;
    private ProgressDialog progressBar;
    private ImageView img_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_shipment_associate);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(this);
        cd = new ConnectionDetector(this);
        editText_track_id = (EditText) findViewById(R.id.editText_track_id);
        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);
        btn_get_details = (Button) findViewById(R.id.btn_get_details);
        btn_get_details.setOnClickListener(this);
    }

    /*private void getTrackIdDetails(){
        progressBar = new ProgressDialog(TrackShipmentCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_track_shipment);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put(JSONConstant.MASTER_TRACKING_NO,editText_track_id.getText().toString().trim());
        //params.put(JSONConstant.SHIPMENT_ID,);
        //params.put(JSONConstant.CR_CARRIER_CODE,)

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(TrackShipmentCustomerActivity.this, URL, params, new VolleyResponseListener() {
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
                        //JSONObject jsonObject = Json_response.getJSONObject("shipment_details");

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(TrackShipmentCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
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
    }*/
    private void getTrackIdDetails(){
        progressBar = new ProgressDialog(TrackShipmentCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_track_shipment);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put(JSONConstant.MASTER_TRACKING_NO,editText_track_id.getText().toString().trim());
        //params.put(JSONConstant.SHIPMENT_ID,);
        //params.put(JSONConstant.CR_CARRIER_CODE,)

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(TrackShipmentCustomerActivity.this, URL, params, new VolleyResponseListener() {
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
                        JSONObject jsonObject = Json_response.getJSONObject("shipment_details");
                        Intent intent =  new Intent(TrackShipmentCustomerActivity.this,TrackShipmentDetailsActivity.class);
                        intent.putExtra("carrier_name",jsonObject.getString("carrier_name"));
                        intent.putExtra(JSONConstant.SERVICE_TYPE,jsonObject.getString(JSONConstant.SERVICE_TYPE));
                        intent.putExtra(JSONConstant.PACKAGING_TYPE,jsonObject.getString(JSONConstant.PACKAGING_TYPE));
                        intent.putExtra(JSONConstant.PACKAGECOUNT,jsonObject.getString(JSONConstant.PACKAGECOUNT));
                        intent.putExtra(JSONConstant.DROP_OFF_TYPE,jsonObject.getString(JSONConstant.DROP_OFF_TYPE));
                        intent.putExtra(JSONConstant.TO_CONTACT_NAME,jsonObject.getString(JSONConstant.TO_CONTACT_NAME));
                        intent.putExtra(JSONConstant.FROM_CONTACT_NAME,jsonObject.getString(JSONConstant.FROM_CONTACT_NAME));
                        intent.putExtra(JSONConstant.MASTER_TRACKING_NO,jsonObject.getString(JSONConstant.MASTER_TRACKING_NO));
                        intent.putExtra("to_address",jsonObject.getString(JSONConstant.TO_C_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.TO_C_CITY)+", "+jsonObject.getString(JSONConstant.TO_C_STATE)+", "+jsonObject.getString(JSONConstant.TO_C_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.TO_C_ZIPCODE));
                        intent.putExtra("from_address",jsonObject.getString(JSONConstant.FROM_C_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.FROM_C_CITY)+", "+jsonObject.getString(JSONConstant.FROM_C_STATE)+", "+jsonObject.getString(JSONConstant.FROM_C_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.FROM_C_ZIPCODE));
                        intent.putExtra("total_amount",jsonObject.getString("total_amount"));
                        intent.putExtra("status",jsonObject.getString("status"));
                        intent.putExtra("delivery_details",jsonObject.getString("expected_deliver_date")+" ( "+jsonObject.getString("expected_deliver_day")+" )");
                        intent.putExtra("shipment_details",jsonObject.getString("shipmentdate")+" ( "+jsonObject.getString("shipmentday")+" )");
                        intent.putExtra("is_identical",jsonObject.getString("is_identical"));

                        // {"status":"success","shipment_details":{"shipment_id":"ship_151435509510DC7B","admin_commission":"10.03","currency":"USD","Packages":[{"PackageWeight":"20","Length":"0","Height":"0","Width":"0","Girth":"","WeightUnit":"LB","DimensionUnit":"IN","CurrencyUnit":"USD","DesiredValue":"25"}],}}


                        JSONArray jsonArray = (JSONArray) jsonObject.get("Packages");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObjectPackage = jsonArray.getJSONObject(i);
                            if(i==0){
                                AppConstant.hashMapPackage1.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_1));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_WEIGHT,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_CURRENCY_UNIT,jsonObjectPackage.getString("CurrencyUnit"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_WEIGHT_UNIT,jsonObjectPackage.getString("WeightUnit"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_DECLARED_VALUE,jsonObjectPackage.getString("DesiredValue"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_LENGTH,jsonObjectPackage.getString("Length"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_WIDTH,jsonObjectPackage.getString("Width"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_HEIGHT,jsonObjectPackage.getString("Height"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_PACKAGE_DESC,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_DIMENSIONAL_UNIT, jsonObjectPackage.getString("DimensionUnit"));
                            }else if(i==1) {
                                AppConstant.hashMapPackage2.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_1));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_WEIGHT,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_CURRENCY_UNIT,jsonObjectPackage.getString("CurrencyUnit"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_WEIGHT_UNIT,jsonObjectPackage.getString("WeightUnit"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_DECLARED_VALUE,jsonObjectPackage.getString("DesiredValue"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_LENGTH,jsonObjectPackage.getString("Length"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_WIDTH,jsonObjectPackage.getString("Width"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_HEIGHT,jsonObjectPackage.getString("Height"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_PACKAGE_DESC,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage2.put(AppConstant.HM_DIMENSIONAL_UNIT, jsonObjectPackage.getString("DimensionUnit"));

                            }else if(i==2){
                                AppConstant.hashMapPackage3.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_1));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_WEIGHT,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_CURRENCY_UNIT,jsonObjectPackage.getString("CurrencyUnit"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_WEIGHT_UNIT,jsonObjectPackage.getString("WeightUnit"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_DECLARED_VALUE,jsonObjectPackage.getString("DesiredValue"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_LENGTH,jsonObjectPackage.getString("Length"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_WIDTH,jsonObjectPackage.getString("Width"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_HEIGHT,jsonObjectPackage.getString("Height"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_PACKAGE_DESC,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage3.put(AppConstant.HM_DIMENSIONAL_UNIT, jsonObjectPackage.getString("DimensionUnit"));

                            }else if(i==3){
                                AppConstant.hashMapPackage4.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_1));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_WEIGHT,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_CURRENCY_UNIT,jsonObjectPackage.getString("CurrencyUnit"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_WEIGHT_UNIT,jsonObjectPackage.getString("WeightUnit"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_DECLARED_VALUE,jsonObjectPackage.getString("DesiredValue"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_LENGTH,jsonObjectPackage.getString("Length"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_WIDTH,jsonObjectPackage.getString("Width"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_HEIGHT,jsonObjectPackage.getString("Height"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_PACKAGE_DESC,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage4.put(AppConstant.HM_DIMENSIONAL_UNIT, jsonObjectPackage.getString("DimensionUnit"));

                            }else if(i==4){
                                AppConstant.hashMapPackage5.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_1));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_WEIGHT,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_CURRENCY_UNIT,jsonObjectPackage.getString("CurrencyUnit"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_WEIGHT_UNIT,jsonObjectPackage.getString("WeightUnit"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_DECLARED_VALUE,jsonObjectPackage.getString("DesiredValue"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_LENGTH,jsonObjectPackage.getString("Length"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_WIDTH,jsonObjectPackage.getString("Width"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_HEIGHT,jsonObjectPackage.getString("Height"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_PACKAGE_DESC,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage5.put(AppConstant.HM_DIMENSIONAL_UNIT, jsonObjectPackage.getString("DimensionUnit"));

                            }else {
                                AppConstant.hashMapPackage1.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_1));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_WEIGHT,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_CURRENCY_UNIT,jsonObjectPackage.getString("CurrencyUnit"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_WEIGHT_UNIT,jsonObjectPackage.getString("WeightUnit"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_DECLARED_VALUE,jsonObjectPackage.getString("DesiredValue"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_LENGTH,jsonObjectPackage.getString("Length"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_WIDTH,jsonObjectPackage.getString("Width"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_HEIGHT,jsonObjectPackage.getString("Height"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_PACKAGE_DESC,jsonObjectPackage.getString("PackageWeight"));
                                AppConstant.hashMapPackage1.put(AppConstant.HM_DIMENSIONAL_UNIT, jsonObjectPackage.getString("DimensionUnit"));

                            }
                        }

                        startActivity(intent);

                        //  {"status":"success","shipment_details":{"shipment_id":"","carrier_name":"FedEX","service_type":"FedEx International Priority","PackagingType":"Your Packaging","PackageCount":1,"DropoffType":"","master_tracking_no":"771106546947","shipmentdate":"12\/29\/2017","shipmentday":"FRI","from_ac_country_code":"IN","from_ac_state":"TN","from_ac_address_line1":"","from_ac_city":"KANCHEEPURAM,","from_ac_zipcode":"","from_contact_name":"","to_contact_name":"","to_c_country_code":"US","to_c_state":"TX","to_c_address_line1":"","to_c_city":"MCKINNEY,","to_c_zipcode":"","expected_deliver_date":"01\/03\/2018","expected_deliver_day":"WED","status":"Delivered","base_amount":"","total_amount":"","surcharge":"","tax1":"","tax2":"","admin_commission":"","currency":"","is_identical":"","Packages":[{"Length":20,"Height":10,"Width":16,"Girth":"","DimensionUnit":"IN","CurrencyUnit":"","DesiredValue":"","PackageWeight":"21.8","WeightUnit":"LB"}]}}

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(TrackShipmentCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_details:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    if(editText_track_id.getText().toString().trim().isEmpty()){
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(TrackShipmentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.hint_track_number));
                        customDialogForHelp.show();
                        //Snackbar.make(btn_get_details,getResources().getString(R.string.hint_track_number),Snackbar.LENGTH_SHORT).show();
                    }else {
                        getTrackIdDetails();
                    }
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(TrackShipmentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(btn_get_details,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_info:
                hideKeyboard();
                DBInfo dbInfo = new DBInfo(TrackShipmentCustomerActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(TrackShipmentCustomerActivity.this, "Help", dbInfo.getDescription("Track Shipment Screen"));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

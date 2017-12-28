package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aspl on 22/12/17.
 */

public class SummeryActivity extends Activity implements View.OnClickListener{

    private TextView tv_ship_date,tv_from_name,tv_from_address,tv_to_name,tv_to_address,
            tv_service_type,tv_pickup_drop,tv_weight,tv_no_of_packages,tv_carrier,
            tv_package_type,tv_selected_service,tv_timestamp,tv_total_amount;
    private SessionManager mSessionManager;
    private LinearLayout ll_service_type;
    private Button btn_proceed_to_payment;
    private ImageView img_info;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summery);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(this);
        cd = new ConnectionDetector(this);

        tv_ship_date = (TextView) findViewById(R.id.tv_ship_date);
        tv_from_name = (TextView) findViewById(R.id.tv_from_name);
        tv_from_address = (TextView) findViewById(R.id.tv_from_address);
        tv_to_name = (TextView) findViewById(R.id.tv_to_name);
        tv_to_address = (TextView) findViewById(R.id.tv_to_address);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        tv_pickup_drop = (TextView) findViewById(R.id.tv_pickup_drop);
        tv_package_type = (TextView) findViewById(R.id.tv_package_type);
        tv_carrier = (TextView) findViewById(R.id.tv_carrier);

        ll_service_type = (LinearLayout) findViewById(R.id.ll_service_type);

        tv_weight = (TextView) findViewById(R.id.tv_weight);
        //tv_dimension = (TextView) findViewById(R.id.tv_dimension);
        tv_no_of_packages = (TextView) findViewById(R.id.tv_no_of_packages);

        tv_selected_service = (TextView) findViewById(R.id.tv_selected_service);
        tv_timestamp = (TextView) findViewById(R.id.tv_timestamp);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);

        btn_proceed_to_payment = (Button) findViewById(R.id.btn_proceed_to_payment);
        btn_proceed_to_payment.setOnClickListener(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);


        tv_ship_date.setText(mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
        tv_from_name.setText(mSessionManager.getStringData(SessionManager.KEY_F_CONTACT_NAME));
        tv_from_address.setText(mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_1)+", "+mSessionManager.getStringData(SessionManager.KEY_F_CITY)+", "+mSessionManager.getStringData(SessionManager.KEY_F_STATE)+", "+mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE)+","+mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
        tv_to_name.setText(mSessionManager.getStringData(SessionManager.KEY_T_CONTACT_NAME));
        tv_to_address.setText(mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_1)+", "+mSessionManager.getStringData(SessionManager.KEY_T_CITY)+", "+mSessionManager.getStringData(SessionManager.KEY_T_STATE)+", "+mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE)+","+mSessionManager.getStringData(SessionManager.KEY_T_ZIP_CODE));

         if(mSessionManager.getStringData(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE)).equals("UPS")){
             ll_service_type.setVisibility(View.GONE);
         }else {
             ll_service_type.setVisibility(View.VISIBLE);
         }

        tv_carrier.setText(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
        tv_service_type.setText(mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE_NAME));
        tv_pickup_drop.setText(mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP_NAME));
        tv_package_type.setText(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE_NAME));

        tv_no_of_packages.setText(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT));

        tv_selected_service .setText( mSessionManager.getStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION));

        tv_timestamp.setText(mSessionManager.getStringData(SessionManager.KEY_DELIVERY_TIMESTAMP));
        tv_total_amount.setText(mSessionManager.getStringData(SessionManager.KEY_AMOUNT));

        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
            if(!AppConstant.hashMapPackage1.isEmpty()) {
                tv_weight.setText(getPackageTitle("Packages")+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)));
            }
        }else {
            if(!AppConstant.hashMapPackage1.isEmpty()) {
                if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE))));

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))+"\n"
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE))));

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE))));

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage4.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_DECLARED_VALUE))));
                }else {
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage4.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage5.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage5.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage5.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage5.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage5.get(AppConstant.HM_DECLARED_VALUE))));

                }
            }
        }
    }

    private String getPackageTitle(String str){
        return str;

    }

    private String getPackageDetails(String weight,String weightUnit,String currencyUnit,String currency){
        String str;
        str= "Weight is "+weight+" "+weightUnit+" & Declared Value (Per package) is "+currencyUnit+" "+currency;
        return str;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_info:
                hideKeyboard();
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SummeryActivity.this, "Help", getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.btn_proceed_to_payment:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    createShipment();
                }else {
                    Snackbar.make(btn_proceed_to_payment,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void createShipment(){
        progressBar = new ProgressDialog(SummeryActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_create_shipment);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put(JSONConstant.FROM_C_COUNTRY_CODE,mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE));
        params.put(JSONConstant.FROM_C_STATE,mSessionManager.getStringData(SessionManager.KEY_F_STATE));
        params.put(JSONConstant.FROM_C_ZIPCODE,mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
        params.put(JSONConstant.FROM_C_ADDRESS_LINE1,mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_1));
        params.put(JSONConstant.FROM_C_CITY,mSessionManager.getStringData(SessionManager.KEY_F_CITY));
        params.put(JSONConstant.TO_C_COUNTRY_CODE,mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE));
        params.put(JSONConstant.TO_C_STATE,mSessionManager.getStringData(SessionManager.KEY_T_STATE));
        params.put(JSONConstant.TO_C_ZIPCODE,mSessionManager.getStringData(SessionManager.KEY_T_ZIP_CODE));
        params.put(JSONConstant.TO_C_ADDRESS_LINE1,mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_1));
        params.put(JSONConstant.TO_C_CITY,mSessionManager.getStringData(SessionManager.KEY_T_CITY));
        params.put(JSONConstant.PACKAGECOUNT,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT));
        params.put(JSONConstant.SERVICESHIPDATE,mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));

        params.put(JSONConstant.PACKAGING_TYPE,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));

        System.out.println("Harshada->"+params.toString());

        //params.put(JSONConstant.SERVICE_TYPE,editText_ship_date.getText().toString());
        if (mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE) != null &&!mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("UPS")){
            params.put(JSONConstant.SERVICE_TYPE,mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE));
        }
        params.put(JSONConstant.DROP_OFF_TYPE,mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));

        params.put(JSONConstant.DROPOFF_TYPE_DESCRIPTION,mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP_NAME));//FOR UPS

        params.put(JSONConstant.PACKAGING_TYPE_DESCRIPTION,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE_NAME));//FOR UPS

        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
            params.put(JSONConstant.IS_IDENTICAL,"1");
        }else {
            params.put(JSONConstant.IS_IDENTICAL,"0");
        }

        params.put(JSONConstant.CR_CARRIER_CODE,mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));


        try {
            List<JSONObject> jsonObj = new ArrayList<JSONObject>();

            for(HashMap<String, String> data : AppConstant.noOfPackages) {
                JSONObject obj = new JSONObject(data);
                jsonObj.add(obj);
            }

            JSONArray test = new JSONArray(jsonObj);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Packages",test);

            params.put(JSONConstant.PACKAGES,test.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(JSONConstant.FROM_CONTACT_NAME,mSessionManager.getStringData(SessionManager.KEY_F_CONTACT_NAME));
        params.put(JSONConstant.FROM_COMPANY_NAME,mSessionManager.getStringData(SessionManager.KEY_F_COMPANY));
        params.put(JSONConstant.FROM_C_PHONE_NO,mSessionManager.getStringData(SessionManager.KEY_F_PHONE_NUMBER));
        params.put(JSONConstant.FROM_C_DIALLING_CODE,mSessionManager.getStringData(SessionManager.KEY_F_DIALLING_CODE));

        params.put(JSONConstant.TO_CONTACT_NAME,mSessionManager.getStringData(SessionManager.KEY_T_CONTACT_NAME));
        params.put(JSONConstant.TO_COMPANY_NAME,mSessionManager.getStringData(SessionManager.KEY_T_COMPANY));
        params.put(JSONConstant.TO_C_PHONE_NO,mSessionManager.getStringData(SessionManager.KEY_T_PHONE_NUMBER));
        params.put(JSONConstant.TO_C_DIALLING_CODE,mSessionManager.getStringData(SessionManager.KEY_T_DIALLING_CODE));

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(SummeryActivity.this, URL, params, new VolleyResponseListener() {
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

                        Snackbar.make(btn_proceed_to_payment,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        mSessionManager.putStringData(SessionManager.KEY_MASTER_TRACKING_NO,Json_response.getString("master_tracking_no"));
                        mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_ID,Json_response.getString("shipment_id"));

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            Snackbar.make(btn_proceed_to_payment,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

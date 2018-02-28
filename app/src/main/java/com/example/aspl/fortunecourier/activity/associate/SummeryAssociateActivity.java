package com.example.aspl.fortunecourier.activity.associate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.DBHelper.DBInfo;
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

public class SummeryAssociateActivity extends Activity implements View.OnClickListener{

    private TextView tv_ship_date,tv_from_name,tv_from_address,tv_to_name,tv_to_address,
            tv_service_type,tv_pickup_drop,tv_weight,tv_no_of_packages,tv_carrier,
            tv_package_type,tv_selected_service,tv_timestamp,tv_total_amount,tv_special_service,tv_collection_fees,tv_customs_value,tv_customs_desc;
    private SessionManager mSessionManager;
    private LinearLayout ll_service_type,ll_special_service ,ll_collection_fees,ll_pickup_drop,ll_customs;
    private Button btn_proceed_to_payment;
    private ImageView img_info;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;
    private ArrayList<HashMap<String, String>> dataMapOfPackages;
    private String strCollectionFees="";
    private String strTotalAmount= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summery);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(this);
        cd = new ConnectionDetector(this);

        dataMapOfPackages = new ArrayList<>();

        tv_ship_date = (TextView) findViewById(R.id.tv_ship_date);
        tv_from_name = (TextView) findViewById(R.id.tv_from_name);
        tv_from_address = (TextView) findViewById(R.id.tv_from_address);
        tv_to_name = (TextView) findViewById(R.id.tv_to_name);
        tv_to_address = (TextView) findViewById(R.id.tv_to_address);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        tv_special_service = (TextView) findViewById(R.id.tv_special_service);
        tv_collection_fees = (TextView) findViewById(R.id.tv_collection_fees);
        tv_pickup_drop = (TextView) findViewById(R.id.tv_pickup_drop);
        tv_package_type = (TextView) findViewById(R.id.tv_package_type);
        tv_carrier = (TextView) findViewById(R.id.tv_carrier);
        tv_customs_value = (TextView) findViewById(R.id.tv_customs_value);
        tv_customs_desc = (TextView) findViewById(R.id.tv_customs_desc);

        ll_service_type = (LinearLayout) findViewById(R.id.ll_service_type);
        ll_special_service = (LinearLayout) findViewById(R.id.ll_special_service);
        ll_collection_fees = (LinearLayout) findViewById(R.id.ll_collection_fees);
        ll_pickup_drop = (LinearLayout) findViewById(R.id.ll_pickup_drop);
        ll_customs = (LinearLayout) findViewById(R.id.ll_customs);



        tv_weight = (TextView) findViewById(R.id.tv_weight);
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

        if(mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_2).isEmpty()){
            tv_from_address.setText(mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_1)+", "+mSessionManager.getStringData(SessionManager.KEY_F_CITY)+", "+mSessionManager.getStringData(SessionManager.KEY_F_STATE)+", "+mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE)+","+mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
        }else {
            tv_from_address.setText(mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_1)+", "+mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_2)+", "+mSessionManager.getStringData(SessionManager.KEY_F_CITY)+", "+mSessionManager.getStringData(SessionManager.KEY_F_STATE)+", "+mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE)+","+mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
        }

        tv_to_name.setText(mSessionManager.getStringData(SessionManager.KEY_T_CONTACT_NAME));

        if(mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_2).isEmpty()){
            tv_to_address.setText(mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_1)+", "+mSessionManager.getStringData(SessionManager.KEY_T_CITY)+", "+mSessionManager.getStringData(SessionManager.KEY_T_STATE)+", "+mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE)+","+mSessionManager.getStringData(SessionManager.KEY_T_ZIP_CODE));
        }else {
            tv_to_address.setText(mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_1)+", "+mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_2)+", "+mSessionManager.getStringData(SessionManager.KEY_T_CITY)+", "+mSessionManager.getStringData(SessionManager.KEY_T_STATE)+", "+mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE)+","+mSessionManager.getStringData(SessionManager.KEY_T_ZIP_CODE));
        }

        if(mSessionManager.getStringData(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE)).equals("UPS")){
            ll_service_type.setVisibility(View.GONE);
        }else {
            ll_service_type.setVisibility(View.VISIBLE);
        }



        if(AppConstant.noOfSpecialServices.size()<=0){
            ll_special_service.setVisibility(View.GONE);
        }else {
            ll_special_service.setVisibility(View.VISIBLE);
            String str = "";
            if(AppConstant.noOfSpecialServices.size()>0){
                for(int i = 0 ; i<AppConstant.noOfSpecialServices.size();i++){
                    HashMap<String, String> hashMap = AppConstant.noOfSpecialServices.get(i);
                    System.out.println("Hashmap "+ hashMap.toString());
                    if(hashMap.get("selected_option_description").equalsIgnoreCase(" ")){
                        if(!mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")){
                            str +=hashMap.get("special_service_description").toString();
                        }else {
                            str +=hashMap.get("special_service_description").toString()+" \nPrice : "+hashMap.get("CurrencyUnit").toString()+" "+hashMap.get("special_service_price").toString();;
                        }
                    }else {
                        if(!mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")){
                            str +=hashMap.get("special_service_description").toString();
                        }else {
                            str += hashMap.get("special_service_description").toString() + " ( " + hashMap.get("selected_option_description").toString() + " )\nPrice : "+hashMap.get("CurrencyUnit").toString()+" "+hashMap.get("special_service_price").toString();
                        }
                    }
                    if(i==AppConstant.noOfSpecialServices.size()-1){
                        str +="";
                    }else {
                        str +="\n";
                    }
                }
                tv_special_service.setText(str);
            }
        }

        tv_carrier.setText(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));

        if(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE) != null && mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("UPS")){
            tv_service_type.setText(AppConstant.SERVICE_TYPE_DESCR_FOR_UPS);
        }else {
            tv_service_type.setText(mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE_NAME));
        }

        if(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE) != null && mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")){
            //tv_service_type.setText(AppConstant.SERVICE_TYPE_DESCR_FOR_UPS);
            ll_pickup_drop.setVisibility(View.GONE);
        }else {
            tv_pickup_drop.setText(mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP_NAME));
        }

        //  tv_service_type.setText(mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE_NAME));
        tv_package_type.setText(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE_NAME));
        tv_no_of_packages.setText(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT));
        tv_selected_service.setText(Html.fromHtml( mSessionManager.getStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION)));

        // tv_selected_service .setText( mSessionManager.getStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION));
        tv_timestamp.setText(mSessionManager.getStringData(SessionManager.KEY_DELIVERY_DATE));
        tv_total_amount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        strTotalAmount = mSessionManager.getStringData(SessionManager.KEY_AMOUNT);

        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
            if(!AppConstant.hashMapPackage1.isEmpty()) {
                tv_weight.setText(getPackageTitle("Packages")+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)));
                dataMapOfPackages.add(AppConstant.hashMapPackage1);
            }
        }else {
            if(!AppConstant.hashMapPackage1.isEmpty()) {
                if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE))));
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE))));
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(AppConstant.hashMapPackage2);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE))));
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(AppConstant.hashMapPackage3);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage4.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_DECLARED_VALUE))));
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(AppConstant.hashMapPackage3);
                    dataMapOfPackages.add(AppConstant.hashMapPackage4);

                }else {
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage4.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage4.get(AppConstant.HM_DECLARED_VALUE)))
                            +"\n\n"+getPackageTitle(AppConstant.hashMapPackage5.get(AppConstant.HM_PACKAGE_NO)+"\n"+getPackageDetails(AppConstant.hashMapPackage5.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage5.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage5.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage5.get(AppConstant.HM_DECLARED_VALUE))));
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(AppConstant.hashMapPackage3);
                    dataMapOfPackages.add(AppConstant.hashMapPackage4);
                    dataMapOfPackages.add(AppConstant.hashMapPackage5);

                }
            }
        }

        if(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("UPS")&&mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP).equalsIgnoreCase("06")){
            if(cd.isConnectingToInternet()){
                getCollectionFees();
            }else {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SummeryAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                customDialogForHelp.show();
            }
        }

        if(mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE).equalsIgnoreCase("US")&& mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE).equalsIgnoreCase("US")) {
            ll_customs.setVisibility(View.GONE);
        }else {
            ll_customs.setVisibility(View.VISIBLE);
            tv_customs_value.setText("Custom Value : "+mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+mSessionManager.getStringData(SessionManager.KEY_CUSTOM_VALUE));
            tv_customs_desc.setText("Custom Description : "+mSessionManager.getStringData(SessionManager.KEY_CUSTOM_DESC));
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
        CustomDialogForHelp customDialogForHelp;

        switch (v.getId()) {
            case R.id.img_info:
                hideKeyboard();
                DBInfo dbInfo = new DBInfo(SummeryAssociateActivity.this);
                customDialogForHelp = new CustomDialogForHelp(SummeryAssociateActivity.this, "Help", dbInfo.getDescription("Create Shipment Summary Screen"));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.btn_proceed_to_payment:
                hideKeyboard();
                startActivity(new Intent(SummeryAssociateActivity.this,PaymentAssociateActivity.class));
               /* if(cd.isConnectingToInternet()){
                     createShipment();
                }else {
                    customDialogForHelp = new CustomDialogForHelp(SummeryCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(btn_proceed_to_payment,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }*/
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

    /*private void createShipment(){
        progressBar = new ProgressDialog(SummeryCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        btn_proceed_to_payment.setEnabled(false);
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_create_shipment);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put(JSONConstant.FROM_C_COUNTRY_CODE,mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE));
        params.put(JSONConstant.FROM_C_STATE,mSessionManager.getStringData(SessionManager.KEY_F_STATE));
        params.put(JSONConstant.FROM_C_ZIPCODE,mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
        params.put(JSONConstant.FROM_C_ADDRESS_LINE1,mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_1));
        params.put(JSONConstant.FROM_C_ADDRESS_LINE2,mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_2));


        params.put(JSONConstant.FROM_C_CITY,mSessionManager.getStringData(SessionManager.KEY_F_CITY));
        params.put(JSONConstant.TO_C_COUNTRY_CODE,mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE));
        params.put(JSONConstant.TO_C_STATE,mSessionManager.getStringData(SessionManager.KEY_T_STATE));
        params.put(JSONConstant.TO_C_ZIPCODE,mSessionManager.getStringData(SessionManager.KEY_T_ZIP_CODE));

        params.put(JSONConstant.TO_C_ADDRESS_LINE1,mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_1));
        params.put(JSONConstant.TO_C_ADDRESS_LINE2,mSessionManager.getStringData(SessionManager.KEY_T_ADDRESSLINE_2));

        params.put(JSONConstant.TO_C_CITY,mSessionManager.getStringData(SessionManager.KEY_T_CITY));
        params.put(JSONConstant.PACKAGECOUNT,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT));
        params.put(JSONConstant.SERVICESHIPDATE,mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));

        params.put(JSONConstant.PACKAGING_TYPE,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));

        System.out.println("Harshada->"+params.toString());

        //params.put(JSONConstant.SERVICE_TYPE,editText_ship_date.getText().toString());
        if (mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE) != null &&!mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("UPS")){
            params.put(JSONConstant.SERVICE_TYPE,mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE));
        }

        if (mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE) != null &&mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("UPS")){
            params.put("service_description",AppConstant.SERVICE_TYPE_DESCR_FOR_UPS);
        }else {
            params.put("service_description",mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE_NAME));
        }

        params.put(JSONConstant.SERVICE_TYPE,AppConstant.SERVICE_TYPE_FOR_UPS);
        params.put("expected_delivery_timestamp",AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS);

       *//* params.put(JSONConstant.SERVICE_TYPE,AppConstant.SERVICE_TYPE_FOR_UPS);
        params.put("expected_delivery_timestamp",AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS);
        params.put("service_description",AppConstant.SERVICE_TYPE_DESCR_FOR_UPS);*//*


        if (mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE) != null &&mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")){
            params.put(JSONConstant.CONTAINER,mSessionManager.getStringData(SessionManager.KEY_CONTAINER_TYPE));
            params.put("Shape",mSessionManager.getStringData(SessionManager.KEY_SHAPE));
            params.put(JSONConstant.SIZE,mSessionManager.getStringData(SessionManager.KEY_SIZES));
            params.put(JSONConstant.SERVICE_TYPE,mSessionManager.getStringData(SessionManager.KEY_SELECTED_SERVICE_TYPE));
            params.put("service_description",mSessionManager.getStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION));
            params.put("expected_delivery_timestamp",AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS);


        }

        params.put(JSONConstant.DROP_OFF_TYPE,mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));

        //params.put(JSONConstant.DROPOFF_TYPE_DESCRIPTION,mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP_NAME));//FOR UPS

        params.put(JSONConstant.PACKAGING_TYPE_DESCRIPTION,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE_NAME));//FOR UPS

        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
            params.put(JSONConstant.IS_IDENTICAL,"1");
        }else {
            params.put(JSONConstant.IS_IDENTICAL,"0");
        }

        params.put(JSONConstant.CR_CARRIER_CODE,mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));


        try {
            List<JSONObject> jsonObj = new ArrayList<JSONObject>();

            for(HashMap<String, String> data : dataMapOfPackages) {
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


        try {
            List<JSONObject> jsonObj = new ArrayList<JSONObject>();

            for(HashMap<String, String> data : AppConstant.noOfSpecialServices) {
                JSONObject obj = new JSONObject(data);
                jsonObj.add(obj);
            }

            JSONArray test = new JSONArray(jsonObj);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("special_services",test);

            params.put("special_services",test.toString());


            System.out.println(jsonObject.toString());

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

        params.put("BaseAmount",mSessionManager.getStringData(SessionManager.KEY_BASE_AMOUNT));
        params.put("Amount",strTotalAmount);
        params.put("cs_surcharge",mSessionManager.getStringData(SessionManager.KEY_SURCHARGE));
        params.put("cs_tax1",mSessionManager.getStringData(SessionManager.KEY_CS_TAX1));
        params.put("cs_tax2",mSessionManager.getStringData(SessionManager.KEY_CS_TAX2));
        params.put("admin_commission",mSessionManager.getStringData(SessionManager.KEY_ADMISSION_COMMISSION));
        params.put("collection_fee",strCollectionFees);

       *//* if(mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE).equalsIgnoreCase("US")&& mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE).equalsIgnoreCase("US")) {
            params.put("customs_currency","");
            params.put("customs_value","");
            params.put("customs_description","");

        }else {
            params.put("customs_currency",mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT));
            params.put("customs_value",mSessionManager.getStringData(SessionManager.KEY_CUSTOM_VALUE));
            params.put("customs_description",mSessionManager.getStringData(SessionManager.KEY_CUSTOM_DESC));

        }*//*


        params.put("PackageContents", mSessionManager.getStringData(SessionManager.KEY_PACKAGE_CONTENTS));
        params.put("ShipmentPurpose", mSessionManager.getStringData(SessionManager.KEY_SHIPMENT_PURPOSE));

        DBCommodity dbCommodity = new DBCommodity(SummeryCustomerActivity.this);


        try {
            List<JSONObject> jsonObj = new ArrayList<JSONObject>();

            for (HashMap<String, String> data : dbCommodity.getAllCommodityHashMapData()) {
                JSONObject obj = new JSONObject(data);
                jsonObj.add(obj);
            }

            JSONArray test = new JSONArray(jsonObj);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Commodities", test);

            params.put("Commodities", test.toString());


            System.out.println(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(SummeryCustomerActivity.this, URL, params, new VolleyResponseListener() {
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


                        if(AppConstant.T_C_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_C_COUNTRY_CODE.equalsIgnoreCase("US")||Json_response.getString("note").isEmpty()){
                            Toast.makeText(SummeryCustomerActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                            //Snackbar.make(btn_proceed_to_payment,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                            mSessionManager.putStringData(SessionManager.KEY_MASTER_TRACKING_NO,Json_response.getString("master_tracking_no"));
                            mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_ID,Json_response.getString("shipment_id"));
                            mSessionManager.putStringData(SessionManager.KEY_AMOUNT,strTotalAmount);
                            startActivity(new Intent(SummeryCustomerActivity.this,PaymentCustomerActivity.class));
                        }else {
                            showNote(SummeryCustomerActivity.this,Json_response.getString("note"));
                        }

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SummeryCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(btn_proceed_to_payment,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.dismiss();
                    btn_proceed_to_payment.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    private void getCollectionFees(){
        progressBar = new ProgressDialog(SummeryAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();

        btn_proceed_to_payment.setEnabled(false);

        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_pickup_rate);
        Map<String, String> params = new HashMap<String, String>();
        // params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));
        params.put(JSONConstant.FROM_A_COUNTRY_CODE,mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE));
        params.put(JSONConstant.FROM_A_STATE,mSessionManager.getStringData(SessionManager.KEY_F_STATE));
        params.put(JSONConstant.FROM_A_ZIPCODE,mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
        params.put(JSONConstant.FROM_A_ADDRESS_LINE1,mSessionManager.getStringData(SessionManager.KEY_F_ADDRESSLINE_1));
        params.put(JSONConstant.FROM_A_CITY,mSessionManager.getStringData(SessionManager.KEY_F_CITY));
        params.put(JSONConstant.SERVICESHIPDATE,mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
        params.put(JSONConstant.PACKAGECOUNT,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT));
        params.put(JSONConstant.TO_A_COUNTRY_CODE,mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE));
        params.put(JSONConstant.CR_CARRIER_CODE,mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
        params.put(JSONConstant.FROM_CONTACT_NAME,mSessionManager.getStringData(SessionManager.KEY_F_CONTACT_NAME));
        params.put(JSONConstant.FROM_COMPANY_NAME,mSessionManager.getStringData(SessionManager.KEY_F_COMPANY));
        params.put(JSONConstant.FROM_A_PHONE_NO,mSessionManager.getStringData(SessionManager.KEY_F_PHONE_NUMBER));
        params.put(JSONConstant.FROM_A_DIALLING_CODE,mSessionManager.getStringData(SessionManager.KEY_F_DIALLING_CODE));
        params.put(JSONConstant.A_EMAIL_ADDRESS,mSessionManager.getStringData(SessionManager.KEY_A_EMAIL));

        System.out.println("Harshada->"+params.toString());


        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
            params.put(JSONConstant.IS_IDENTICAL,"1");
        }else {
            params.put(JSONConstant.IS_IDENTICAL,"0");
        }

        try {
            List<JSONObject> jsonObj = new ArrayList<JSONObject>();

            for(HashMap<String, String> data : dataMapOfPackages) {
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

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(SummeryAssociateActivity.this, URL, params, new VolleyResponseListener() {
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
                        ll_collection_fees.setVisibility(View.VISIBLE);
                        tv_collection_fees.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+Json_response.getString("collection_fee"));
                        double totalAmount = Math.round(Double.parseDouble(mSessionManager.getStringData(SessionManager.KEY_AMOUNT))+Double.parseDouble(Json_response.getString("collection_fee")));
                        tv_total_amount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+String.valueOf(totalAmount));
                        mSessionManager.putStringData(SessionManager.KEY_AMOUNT,String.valueOf(totalAmount));
                        strTotalAmount =String.valueOf(totalAmount);
                        strCollectionFees = Json_response.getString("collection_fee");
                        mSessionManager.putStringData(SessionManager.KEY_COLLECTION_FEES,strTotalAmount);
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(SummeryAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                        }
                    }
                    progressBar.dismiss();
                    btn_proceed_to_payment.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showNote(Context context,String strMessage){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.custom_dialog_logout, null);
        alertDialog.setView(convertView);

        TextView popup_title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView popup_message = (TextView) convertView.findViewById(R.id.tv_message);

        Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
        btn_cancel.setVisibility(View.GONE);

        final android.app.AlertDialog dialog = alertDialog.create();
        //popup_title.setVisibility(View.VISIBLE);
        //popup_title.setText(getResources().getString(R.string.lbl_disclaimer));

        popup_message.setText(strMessage);
        dialog.setCanceledOnTouchOutside(false);

        // for back button
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                startActivity(new Intent(SummeryAssociateActivity.this,PaymentAssociateActivity.class));

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}

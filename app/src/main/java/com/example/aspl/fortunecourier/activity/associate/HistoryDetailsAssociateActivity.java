package com.example.aspl.fortunecourier.activity.associate;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.BuildConfig;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.TrackShipmentDetailsActivity;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;
import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by aspl on 21/12/17.
 */

public class HistoryDetailsAssociateActivity extends Activity implements View.OnClickListener{

    private TextView tv_ship_date,tv_from_name,tv_from_address,tv_to_name,tv_to_address,
            tv_service_type,tv_pickup_drop,tv_weight,tv_no_of_packages,tv_carrier,
            tv_package_type,tv_total_amount,tv_tracking_number,tv_status,tv_delivery_date,tv_special_service,tv_collection_fees,tv_customs_value,tv_customs_desc;
    private SessionManager mSessionManager;
    private LinearLayout ll_service_type,ll_special_service,ll_delivery_date,ll_pickup_drop,ll_collection_fees,ll_customs;
    private Button btn_track_now;
    private ImageView img_info;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;
    private String strShipmentId,strTrackingId="label";
    private Button btn_download_label;
    private String strPdf="";
    private static final int  MEGABYTE = 1024 * 1024;
    private String strCarrierCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
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
        tv_special_service = (TextView) findViewById(R.id.tv_special_service);
        tv_collection_fees = (TextView) findViewById(R.id.tv_collection_fees);
        tv_customs_value = (TextView) findViewById(R.id.tv_customs_value);
        tv_customs_desc = (TextView) findViewById(R.id.tv_customs_desc);


        tv_tracking_number = (TextView) findViewById(R.id.tv_tracking_number);

        ll_service_type = (LinearLayout) findViewById(R.id.ll_service_type);
        ll_special_service = (LinearLayout) findViewById(R.id.ll_special_service);
        ll_delivery_date = (LinearLayout) findViewById(R.id.ll_delivery_date);
        ll_pickup_drop = (LinearLayout) findViewById(R.id.ll_pickup_drop);
        ll_collection_fees = (LinearLayout) findViewById(R.id.ll_collection_fees);
        ll_customs = (LinearLayout) findViewById(R.id.ll_customs);


        tv_weight = (TextView) findViewById(R.id.tv_weight);
        //tv_dimension = (TextView) findViewById(R.id.tv_dimension);
        tv_no_of_packages = (TextView) findViewById(R.id.tv_no_of_packages);

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_delivery_date = (TextView) findViewById(R.id.tv_delivery_date);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);

        btn_track_now = (Button) findViewById(R.id.btn_track_now);
        btn_track_now.setOnClickListener(this);

        btn_download_label = (Button) findViewById(R.id.btn_download_label);
        btn_download_label.setOnClickListener(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        strShipmentId = getIntent().getStringExtra("ShipmentId");

        if(cd.isConnectingToInternet()){
            getHistoryDetails();
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(HistoryDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();        }
    }

    @Override
    public void onClick(View v) {

        CustomDialogForHelp customDialogForHelp;

        switch (v.getId()) {
            case R.id.img_info:
                //hideKeyboard();
                customDialogForHelp = new CustomDialogForHelp(HistoryDetailsAssociateActivity.this, "Help", getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                //hideKeyboard();
                finish();
                break;

           case R.id.btn_track_now:
                //hideKeyboard();
                if(cd.isConnectingToInternet()){
                   getTrackIdDetails();
                }else {
                    customDialogForHelp = new CustomDialogForHelp(HistoryDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(tv_carrier,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_download_label:
                if(cd.isConnectingToInternet()){
                    try{
                        if(Build.VERSION.SDK_INT>=23){
                            int permissionCheckCamera1 = ContextCompat.checkSelfPermission(HistoryDetailsAssociateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionCheckCamera1 != PackageManager.PERMISSION_GRANTED ){
                                ActivityCompat.requestPermissions(HistoryDetailsAssociateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }else{
                                showProgressBar();
                                if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                                    new DownloadFile().execute(strPdf, strTrackingId + ".gif");
                                }else {
                                    new DownloadFile().execute(strPdf, strTrackingId+".pdf");
                                }
                            }
                        }else{
                            showProgressBar();
                            if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                                new DownloadFile().execute(strPdf, strTrackingId + ".gif");
                            }else {
                                new DownloadFile().execute(strPdf, strTrackingId+".pdf");
                            }


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    customDialogForHelp = new CustomDialogForHelp(HistoryDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;
        }

    }

    private void getHistoryDetails(){
        showProgressBar();
        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_view_shipment_details);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));
        params.put(JSONConstant.SHIPMENT_ID,strShipmentId);

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(HistoryDetailsAssociateActivity.this, URL, params, new VolleyResponseListener() {
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
                        JSONObject jsonObject = Json_response.getJSONObject("shipment_details");
                        tv_carrier.setText(jsonObject.getString("carrier_name"));
                        tv_service_type.setText(Html.fromHtml( jsonObject.getString(JSONConstant.SERVICE_TYPE)));

                        //tv_service_type.setText(jsonObject.getString(JSONConstant.SERVICE_TYPE));
                        tv_package_type.setText(jsonObject.getString(JSONConstant.PACKAGING_TYPE));
                        tv_no_of_packages.setText(jsonObject.getString(JSONConstant.PACKAGECOUNT));
                        tv_pickup_drop.setText(jsonObject.getString(JSONConstant.DROP_OFF_TYPE));
                        tv_ship_date.setText(jsonObject.getString("shipmentdate")+" ( "+jsonObject.getString("shipmentday")+" )");
                        tv_to_name.setText(jsonObject.getString(JSONConstant.TO_CONTACT_NAME));
                        tv_from_name.setText(jsonObject.getString(JSONConstant.FROM_CONTACT_NAME));
                        tv_tracking_number.setText(jsonObject.getString(JSONConstant.MASTER_TRACKING_NO));

                        strTrackingId = jsonObject.getString(JSONConstant.MASTER_TRACKING_NO);

                        if(jsonObject.getString(JSONConstant.TO_A_ADDRESS_LINE2).isEmpty()){
                            tv_to_address.setText(jsonObject.getString(JSONConstant.TO_A_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.TO_A_CITY)+", "+jsonObject.getString(JSONConstant.TO_A_STATE)+", "+jsonObject.getString(JSONConstant.TO_A_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.TO_A_ZIPCODE));
                        }else {
                            tv_to_address.setText(jsonObject.getString(JSONConstant.TO_A_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.TO_A_ADDRESS_LINE2)+", "+jsonObject.getString(JSONConstant.TO_A_CITY)+", "+jsonObject.getString(JSONConstant.TO_A_STATE)+", "+jsonObject.getString(JSONConstant.TO_A_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.TO_A_ZIPCODE));
                        }

                        if(jsonObject.getString(JSONConstant.FROM_A_ADDRESS_LINE2).isEmpty()){
                            tv_from_address.setText(jsonObject.getString(JSONConstant.FROM_A_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.FROM_A_CITY)+", "+jsonObject.getString(JSONConstant.FROM_A_STATE)+", "+jsonObject.getString(JSONConstant.FROM_A_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.FROM_A_ZIPCODE));
                        }else {
                            tv_from_address.setText(jsonObject.getString(JSONConstant.FROM_A_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.FROM_A_ADDRESS_LINE2)+", "+jsonObject.getString(JSONConstant.FROM_A_CITY)+", "+jsonObject.getString(JSONConstant.FROM_A_STATE)+", "+jsonObject.getString(JSONConstant.FROM_A_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.FROM_A_ZIPCODE));
                        }

                        //tv_total_amount.setText("Total Amount : "+jsonObject.getString("total_amount")+"\nBase Amount : "+jsonObject.getString("base_amount")+"\n"+jsonObject.getString("cs_tax1_title")+" : "+jsonObject.getString("tax1")+"\n"+jsonObject.getString("cs_tax2_title")+" : "+jsonObject.getString("tax2")+"\n"+jsonObject.getString("cs_surcharge_title")+" : "+jsonObject.getString("surcharge"));
                        tv_total_amount.setText(jsonObject.getString("currency")+" "+jsonObject.getString("total_amount"));//
                        tv_status.setText(jsonObject.getString("status"));
                        if(jsonObject.getString("expected_deliver_date").isEmpty()){
                            ll_delivery_date.setVisibility(View.GONE);
                        }else {
                            tv_delivery_date.setText(jsonObject.getString("expected_deliver_date") + " ( " + jsonObject.getString("expected_deliver_day") + " )");

                        }

                        strCarrierCode = jsonObject.getString("carrier_name");

                       /* if(jsonObject.getString("carrier_name").equalsIgnoreCase("UPS")&&jsonObject.getString(JSONConstant.DROP_OFF_TYPE).equalsIgnoreCase("One Time Pickup")){
                            ll_collection_fees.setVisibility(View.VISIBLE);
                            tv_collection_fees.setText(jsonObject.getString("currency")+" "+jsonObject.getString("collection_fee"));
                            double totalAmount = Math.round(Double.parseDouble(jsonObject.getString("total_amount"))+Double.parseDouble(jsonObject.getString("collection_fee")));
                            tv_total_amount.setText(jsonObject.getString("currency")+" "+String.valueOf(totalAmount));
                        }*/

                        if(jsonObject.getString("carrier_name") != null && jsonObject.getString("carrier_name").equalsIgnoreCase("USPS")){
                            //tv_service_type.setText(AppConstant.SERVICE_TYPE_DESCR_FOR_UPS);
                            ll_pickup_drop.setVisibility(View.GONE);
                        }else {
                            tv_pickup_drop.setText(jsonObject.getString("DropoffType"));
                        }

                        strPdf = jsonObject.getString("pdf_label");

                        if(strPdf.equalsIgnoreCase("")){
                            //tv_service_type.setText(AppConstant.SERVICE_TYPE_DESCR_FOR_UPS);
                            btn_download_label.setVisibility(View.GONE);
                        }else {
                            btn_download_label.setVisibility(View.VISIBLE);
                        }

                        if(!(jsonObject.getString(JSONConstant.TO_A_COUNTRY_CODE).equalsIgnoreCase("US")&&jsonObject.getString(JSONConstant.FROM_A_COUNTRY_CODE).equalsIgnoreCase("US"))){
                            ll_customs.setVisibility(View.VISIBLE);
                            tv_customs_value.setText("Custom Value : "+jsonObject.getString("customs_currency")+" "+jsonObject.getString("customs_value"));
                            tv_customs_desc.setText("Custom Description : "+jsonObject.getString("customs_description"));
                        }else {
                            ll_customs.setVisibility(View.GONE);

                        }

                        JSONArray customOptnList = jsonObject.getJSONArray("special_services");

                        if(customOptnList.length()>0) {
                            String str="";
                           ll_special_service.setVisibility(View.VISIBLE);
                           for (int j = 0; j < customOptnList.length(); j++) {
                               JSONObject eachData = customOptnList.getJSONObject(j);

                               if(eachData.getString("selected_option_description").equalsIgnoreCase("")||eachData.getString("selected_option_description").equalsIgnoreCase(" ")){
                                   if(!mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")){
                                       str +=eachData.getString("special_service_description").toString();
                                   }else {
                                       str +=eachData.getString("special_service_description").toString()+" \nPrice : "+eachData.getString("CurrencyUnit").toString()+" "+eachData.getString("special_service_price").toString();;
                                   }
                                   //str +=eachData.getString("special_service_description");
                               }else {
                                   if(!mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")){
                                       str +=eachData.getString("special_service_description").toString();
                                   }else {
                                       str += eachData.getString("special_service_description").toString() + " ( " + eachData.getString("selected_option_description").toString() + " )\nPrice : "+eachData.getString("CurrencyUnit").toString()+" "+eachData.getString("special_service_price").toString();
                                   }
                                  // str +=eachData.getString("special_service_description")+" ( "+eachData.getString("selected_option_description")+" ) ";
                               }

                               if(j==customOptnList.length()-1){
                                   str +="";
                               }else {
                                   str +="\n";
                               }
                           }
                           tv_special_service.setText(str);
                       }else {
                           ll_special_service.setVisibility(View.GONE);
                       }


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
                                AppConstant.hashMapPackage2.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_2));
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
                                AppConstant.hashMapPackage3.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_3));
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
                                AppConstant.hashMapPackage4.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_4));
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
                                AppConstant.hashMapPackage5.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_5));
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

                        Log.e("Hi","->"+jsonArray.length());
                        if(jsonObject.getString("is_identical").equalsIgnoreCase("Yes")){
                            if(!AppConstant.hashMapPackage1.isEmpty()) {
                                tv_weight.setText(getPackageTitle("Packages")+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)));
                            }
                        }else {
                            if (!AppConstant.hashMapPackage1.isEmpty()) {
                                if (jsonObject.getString(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("1")) {
                                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE))));

                                } else if (jsonObject.getString(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("2")) {
                                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE))) + "\n"
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE))));

                                } else if (jsonObject.getString(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("3")) {
                                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE))));

                                } else if (jsonObject.getString(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("4")) {
                                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage4.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage4.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage4.get(AppConstant.HM_DECLARED_VALUE))));
                                } else {
                                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage4.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage4.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage4.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage4.get(AppConstant.HM_DECLARED_VALUE)))
                                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage5.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage5.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage5.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage5.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage5.get(AppConstant.HM_DECLARED_VALUE))));
                                }
                            }
                        }

                        closeProgressBar();





                       /* Snackbar.make(tv_carrier,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                          mSessionManager.putStringData(SessionManager.KEY_MASTER_TRACKING_NO,Json_response.getString("master_tracking_no"));
                          mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_ID,Json_response.getString("shipment_id"));*/

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(HistoryDetailsAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(tv_carrier,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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

    private String getPackageTitle(String str){
        return str;

    }

    private String getPackageDetails(String weight,String weightUnit,String currencyUnit,String currency){
        String str;
        str= "Weight is "+weight+" "+weightUnit+" & Declared Value (Per package) is "+currencyUnit+" "+currency;
        return str;

    }

    private void getTrackIdDetails(){
        showProgressBar();
        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_track_shipment);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));
        params.put(JSONConstant.MASTER_TRACKING_NO,tv_tracking_number.getText().toString().trim());
        params.put(JSONConstant.SHIPMENT_ID,strShipmentId);
        params.put(JSONConstant.CR_CARRIER_CODE,tv_carrier.getText().toString().trim());


        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(HistoryDetailsAssociateActivity.this, URL, params, new VolleyResponseListener() {
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
                        JSONObject jsonObject = Json_response.getJSONObject("shipment_details");
                        Intent intent =  new Intent(HistoryDetailsAssociateActivity.this,TrackShipmentDetailsActivity.class);
                        intent.putExtra("carrier_name",jsonObject.getString("carrier_name"));
                        intent.putExtra(JSONConstant.SERVICE_TYPE,jsonObject.getString(JSONConstant.SERVICE_TYPE));
                        intent.putExtra(JSONConstant.PACKAGING_TYPE,jsonObject.getString(JSONConstant.PACKAGING_TYPE));
                        intent.putExtra(JSONConstant.PACKAGECOUNT,jsonObject.getString(JSONConstant.PACKAGECOUNT));
                        intent.putExtra(JSONConstant.DROP_OFF_TYPE,jsonObject.getString(JSONConstant.DROP_OFF_TYPE));
                        intent.putExtra(JSONConstant.TO_CONTACT_NAME,jsonObject.getString(JSONConstant.TO_CONTACT_NAME));
                        intent.putExtra(JSONConstant.FROM_CONTACT_NAME,jsonObject.getString(JSONConstant.FROM_CONTACT_NAME));
                        intent.putExtra(JSONConstant.MASTER_TRACKING_NO,jsonObject.getString(JSONConstant.MASTER_TRACKING_NO));
                        intent.putExtra("to_address",jsonObject.getString(JSONConstant.TO_A_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.TO_A_CITY)+", "+jsonObject.getString(JSONConstant.TO_A_STATE)+", "+jsonObject.getString(JSONConstant.TO_A_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.TO_A_ZIPCODE));
                        intent.putExtra("from_address",jsonObject.getString(JSONConstant.FROM_A_ADDRESS_LINE1)+", "+jsonObject.getString(JSONConstant.FROM_A_CITY)+", "+jsonObject.getString(JSONConstant.FROM_A_STATE)+", "+jsonObject.getString(JSONConstant.FROM_A_COUNTRY_CODE)+", "+jsonObject.getString(JSONConstant.FROM_A_ZIPCODE));
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
                                AppConstant.hashMapPackage2.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_2));
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
                                AppConstant.hashMapPackage3.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_3));
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
                                AppConstant.hashMapPackage4.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_4));
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
                                AppConstant.hashMapPackage5.put(AppConstant.HM_PACKAGE_NO,getResources().getString(R.string.lbl_package_5));
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
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(HistoryDetailsAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
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

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FortuneCourier");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            //FileDownloader.downloadFile(fileUrl, pdfFile);
            downloadFile(fileUrl, pdfFile);
            return null;
        }
    }


    public  void downloadFile(String fileUrl, File directory){
        try {

            java.net.URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();

            closeProgressBar();

           /* File pdfFile = new File(Environment.getExternalStorageDirectory() + "/FortuneCourier/" + strTrackingId+".pdf");  // -> filename = maven.pdf
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri apkURI = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);
            pdfIntent.setDataAndType(apkURI, "application/pdf");
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(HistoryDetailsAssociateActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }*/

            File pdfFile;
            if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                pdfFile = new File(Environment.getExternalStorageDirectory() + "/FortuneCourier/" + strTrackingId + ".gif");  // -> filename = maven.pdf
            }else {
                pdfFile = new File(Environment.getExternalStorageDirectory() + "/FortuneCourier/" + strTrackingId + ".pdf");  // -> filename = maven.pdf
            }

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri apkURI = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);

            if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                pdfIntent.setDataAndType(apkURI, "image/*");
            }else {
                pdfIntent.setDataAndType(apkURI, "application/pdf");
            }

            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(HistoryDetailsAssociateActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showProgressBar(){
        progressBar = new ProgressDialog(HistoryDetailsAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
    }

    private void closeProgressBar(){
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
    }
}

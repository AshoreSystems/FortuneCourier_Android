package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.associate.CheckRateScreenAssociateActivity;
import com.example.aspl.fortunecourier.activity.associate.CheckRatesFromAssociateActivity;
import com.example.aspl.fortunecourier.adapter.CheckRateCustomerAdapter;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Rates;
import com.example.aspl.fortunecourier.utility.AppConstant;
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
 * Created by aspl on 28/11/17.
 */

public class CheckRateScreenCustomerActivity extends Activity implements View.OnClickListener{
    private RecyclerView recycler_checklist;
    private ImageView img_info ;
    private Button btn_create_shipment,btn_proceed;
    private SessionManager mSessionManager;
    private CheckRateCustomerAdapter recyclerViewAdapter;
    private ArrayList<Rates> ciArr;
    private ProgressDialog progressBar;
    private ArrayList<HashMap<String, String>> dataMapOfPackages;
    private String strServiceType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rate_screen);
        init();
    }

    private void init(){

        mSessionManager = new SessionManager(this);
        dataMapOfPackages = new ArrayList<HashMap<String, String>>();

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        btn_create_shipment = (Button) findViewById(R.id.btn_create_shipment);
        btn_create_shipment.setOnClickListener(this);

        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(this);

        recycler_checklist = (RecyclerView) findViewById(R.id.recycler_checklist);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recycler_checklist.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_checklist.getContext(), recyclerLayoutManager.getOrientation());
        recycler_checklist.addItemDecoration(dividerItemDecoration);

        ciArr = (ArrayList) getIntent().getExtras().getParcelableArrayList("RateList");

        strServiceType = ciArr.get(0).getServiceType();
        AppConstant.SERVICE_TYPE_FOR_UPS = ciArr.get(0).getServiceType();
        AppConstant.SERVICE_TYPE_DESCR_FOR_UPS = ciArr.get(0).getServiceDescription();

        recyclerViewAdapter = new CheckRateCustomerAdapter(ciArr,CheckRateScreenCustomerActivity.this);
        recycler_checklist.setAdapter(recyclerViewAdapter);


        if(AppConstant.IS_FROM_CREATE_SHIPMENT){
            btn_create_shipment.setText(getResources().getString(R.string.btn_proceed));
        }else {
            btn_create_shipment.setText(getResources().getString(R.string.lbl_header_create_shipment));

        }
        /*if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){
            btn_proceed.setVisibility(View.VISIBLE);
            btn_create_shipment.setVisibility(View.GONE);
        }else {
            btn_proceed.setVisibility(View.GONE);
            btn_create_shipment.setVisibility(View.VISIBLE);
        }*/

            Log.e("Harshada=>",mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
            // tv_first.performClick();
            if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                dataMapOfPackages.add(AppConstant.hashMapPackage1);
            }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                dataMapOfPackages.add(AppConstant.hashMapPackage2);
            }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                dataMapOfPackages.add(AppConstant.hashMapPackage3);
            }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                dataMapOfPackages.add(AppConstant.hashMapPackage4);
            }else {
                dataMapOfPackages.add(AppConstant.hashMapPackage5);
            }

            dataMapOfPackages.add(AppConstant.hashMapPackage1);

            dataMapOfPackages.remove(AppConstant.hashMapPackage2);
            dataMapOfPackages.remove(AppConstant.hashMapPackage3);
            dataMapOfPackages.remove(AppConstant.hashMapPackage4);
            dataMapOfPackages.remove(AppConstant.hashMapPackage5);

        }else {
            if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                dataMapOfPackages.add(AppConstant.hashMapPackage1);

            }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                dataMapOfPackages.add(AppConstant.hashMapPackage1);
                dataMapOfPackages.add(AppConstant.hashMapPackage2);

            }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                dataMapOfPackages.add(AppConstant.hashMapPackage1);
                dataMapOfPackages.add(AppConstant.hashMapPackage2);
                dataMapOfPackages.add(AppConstant.hashMapPackage3);

            }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                dataMapOfPackages.add(AppConstant.hashMapPackage1);
                dataMapOfPackages.add(AppConstant.hashMapPackage2);
                dataMapOfPackages.add(AppConstant.hashMapPackage3);
                dataMapOfPackages.add(AppConstant.hashMapPackage4);

            }else {
                dataMapOfPackages.add(AppConstant.hashMapPackage1);
                dataMapOfPackages.add(AppConstant.hashMapPackage2);
                dataMapOfPackages.add(AppConstant.hashMapPackage3);
                dataMapOfPackages.add(AppConstant.hashMapPackage4);
                dataMapOfPackages.add(AppConstant.hashMapPackage5);

            }
        }
           /* if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
                if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    dataMapOfPackages.add(AppConstant.hashMapPackage2);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                    dataMapOfPackages.add(AppConstant.hashMapPackage3);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                    dataMapOfPackages.add(AppConstant.hashMapPackage4);
                }else {
                    dataMapOfPackages.add(AppConstant.hashMapPackage5);
                }

                dataMapOfPackages.add(AppConstant.hashMapPackage1);
               *//* tv_package1.setText("Packages");
                linear_package1.setVisibility(View.VISIBLE);
                hideLayout(linear_package2);
                hideLayout(linear_package3);
                hideLayout(linear_package4);
                hideLayout(linear_package5);
                showPackageDetails(tv_package_details1,hashMapPackage1);
                hashMapPackage2.clear();
                hashMapPackage3.clear();
                hashMapPackage4.clear();
                hashMapPackage5.clear();*//*


            }else {
                if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(AppConstant.hashMapPackage2);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(AppConstant.hashMapPackage3);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){

                    dataMapOfPackages.add(AppConstant.hashMapPackage1);

                    dataMapOfPackages.add(AppConstant.hashMapPackage2);

                    dataMapOfPackages.add(AppConstant.hashMapPackage3);

                    dataMapOfPackages.add(AppConstant.hashMapPackage4);

                }else {
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);

                    dataMapOfPackages.add(AppConstant.hashMapPackage2);

                    dataMapOfPackages.add(AppConstant.hashMapPackage3);

                    dataMapOfPackages.add(AppConstant.hashMapPackage4);

                    dataMapOfPackages.add(AppConstant.hashMapPackage5);

                }
            }*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                finish();
                break;

            case R.id.img_info:
                DBInfo dbInfo = new DBInfo(CheckRateScreenCustomerActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRateScreenCustomerActivity.this,"Help",dbInfo.getDescription("Rate and Transit Screen"));
                customDialogForHelp.show();
                break;

           /* case R.id.btn_proceed:
                if (recyclerViewAdapter.lastSelectedPosition==-1){
                    Snackbar.make(btn_create_shipment,"Please select delivery date",Snackbar.LENGTH_SHORT).show();
                }else {
                    mSessionManager.putStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getServiceDescription());
                    mSessionManager.putStringData(SessionManager.KEY_DELIVERY_TIMESTAMP,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp());
                    mSessionManager.putStringData(SessionManager.KEY_DELIVERY_DATE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryDate());
                    mSessionManager.putStringData(SessionManager.KEY_AMOUNT,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount());
                    AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS = ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp();

                   *//* AppConstant.IS_FROM_CREATE_SHIPMENT = false;
                    AppConstant.IS_FROM_CALCULATE_RATES = false;*//*


                    //Snackbar.make(btn_create_shipment,"->"+ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount(),Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(CheckRateScreenCustomerActivity.this,SummeryCustomerActivity.class));

                }
                break;*/

            case R.id.btn_create_shipment:

                if (recyclerViewAdapter.lastSelectedPosition==-1){
                   customDialogForHelp = new CustomDialogForHelp(CheckRateScreenCustomerActivity.this,getResources().getString(R.string.app_name),"Please select delivery date");
                    customDialogForHelp.show();
                    //Snackbar.make(btn_create_shipment,"Please select delivery date",Snackbar.LENGTH_SHORT).show();
                }else {

                   if(AppConstant.IS_CUSTOMER_LOG_IN){
                       //Snackbar.make(btn_create_shipment,"->"+ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount(),Snackbar.LENGTH_SHORT).show();
                       mSessionManager.putStringData(SessionManager.KEY_SELECTED_SERVICE_TYPE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getServiceType());
                       mSessionManager.putStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getServiceDescription());
                       mSessionManager.putStringData(SessionManager.KEY_DELIVERY_TIMESTAMP,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp());
                       mSessionManager.putStringData(SessionManager.KEY_DELIVERY_DATE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryDate());
                       mSessionManager.putStringData(SessionManager.KEY_AMOUNT,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount());
                       mSessionManager.putStringData(SessionManager.KEY_CONTAINER_TYPE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getContainerType());
                       Log.e("Expected Date",""+ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp());

                       mSessionManager.putStringData(SessionManager.KEY_BASE_AMOUNT,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getBaseAmount());
                       mSessionManager.putStringData(SessionManager.KEY_SURCHARGE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getCs_surcharge());
                       mSessionManager.putStringData(SessionManager.KEY_CS_TAX1,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getCs_tax1());
                       mSessionManager.putStringData(SessionManager.KEY_CS_TAX2,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getCs_tax2());
                       mSessionManager.putStringData(SessionManager.KEY_ADMISSION_COMMISSION,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAdmin_commission());
                       AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS = ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp();

                       //startActivity(new Intent(CheckRateScreenCustomerActivity.this,CreateShipmentFromCustomerActivity.class));

                       if(!mSessionManager.getBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES)){
                          // createShipment();
                           startActivity(new Intent(CheckRateScreenCustomerActivity.this,SummeryCustomerActivity.class));

                       }else {
                           // AppConstant.IS_FROM_CALCULATE_RATES = true;
                           startActivity(new Intent(CheckRateScreenCustomerActivity.this,CreateShipmentFromCustomerActivity.class));
                       }
                   }else {
                       mSessionManager.putStringData(SessionManager.KEY_SELECTED_SERVICE_TYPE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getServiceType());
                       mSessionManager.putStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getServiceDescription());
                       mSessionManager.putStringData(SessionManager.KEY_DELIVERY_TIMESTAMP,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp());
                       mSessionManager.putStringData(SessionManager.KEY_DELIVERY_DATE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryDate());
                       mSessionManager.putStringData(SessionManager.KEY_AMOUNT,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount());
                       mSessionManager.putStringData(SessionManager.KEY_BASE_AMOUNT,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getBaseAmount());
                       mSessionManager.putStringData(SessionManager.KEY_SURCHARGE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getCs_surcharge());
                       mSessionManager.putStringData(SessionManager.KEY_CS_TAX1,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getCs_tax1());
                       mSessionManager.putStringData(SessionManager.KEY_CS_TAX2,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getCs_tax2());
                       mSessionManager.putStringData(SessionManager.KEY_ADMISSION_COMMISSION,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAdmin_commission());

                       mSessionManager.putStringData(SessionManager.KEY_CONTAINER_TYPE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getContainerType());
                       Log.e("Expected Date",""+ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp());

                       AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS = ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp();

                       //AppConstant.IS_FROM_CALCULATE_RATES = true;
                       mSessionManager.putBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES,true);
                       AppConstant.IS_FROM_OUTSIDE = true;
                       startActivity(new Intent(CheckRateScreenCustomerActivity.this,LoginCustomerActivity.class));
                       //for outer check rate functionality
                   }
                }

                break;
        }
    }

    /*private void createShipment(){
        progressBar = new ProgressDialog(CheckRateScreenCustomerActivity.this);
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
            params.put(JSONConstant.SERVICE_TYPE,strServiceType);
            params.put("expected_delivery_timestamp",AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS);

        }
        params.put(JSONConstant.DROP_OFF_TYPE,mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));

        params.put(JSONConstant.DROPOFF_TYPE_DESCRIPTION,mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP_NAME));//FOR UPS

        params.put(JSONConstant.PACKAGING_TYPE_DESCRIPTION,mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE_NAME));//FOR UPS
        params.put("service_description",mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE_NAME));

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

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(CheckRateScreenCustomerActivity.this, URL, params, new VolleyResponseListener() {
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

                        Toast.makeText(CheckRateScreenCustomerActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                        mSessionManager.putStringData(SessionManager.KEY_MASTER_TRACKING_NO,Json_response.getString("master_tracking_no"));
                        mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_ID,Json_response.getString("shipment_id"));
                        startActivity(new Intent(CheckRateScreenCustomerActivity.this,SummeryCustomerActivity.class));

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRateScreenCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(btn_create_shipment,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}

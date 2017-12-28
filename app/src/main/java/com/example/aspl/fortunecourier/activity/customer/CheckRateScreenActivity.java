package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.adapter.CheckRateAdapter;
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

public class CheckRateScreenActivity extends Activity implements View.OnClickListener{
    private RecyclerView recycler_checklist;
    private ImageView img_info ;
    private Button btn_create_shipment,btn_proceed;
    private SessionManager mSessionManager;
    CheckRateAdapter recyclerViewAdapter;
    ArrayList<Rates> ciArr;
    private ProgressDialog progressBar;
    ArrayList<HashMap<String, String>> dataMapOfPackages;




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

        recyclerViewAdapter = new CheckRateAdapter(ciArr,CheckRateScreenActivity.this);
        recycler_checklist.setAdapter(recyclerViewAdapter);

        if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){
            btn_proceed.setVisibility(View.VISIBLE);
            btn_create_shipment.setVisibility(View.GONE);
        }

            Log.e("Harshada=>",mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
            if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
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
               /* tv_package1.setText("Packages");
                linear_package1.setVisibility(View.VISIBLE);
                hideLayout(linear_package2);
                hideLayout(linear_package3);
                hideLayout(linear_package4);
                hideLayout(linear_package5);
                showPackageDetails(tv_package_details1,hashMapPackage1);
                hashMapPackage2.clear();
                hashMapPackage3.clear();
                hashMapPackage4.clear();
                hashMapPackage5.clear();*/


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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                finish();
                break;

            case R.id.img_info:
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRateScreenActivity.this,"Help",getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;

            case R.id.btn_proceed:
                if (recyclerViewAdapter.lastSelectedPosition==-1){
                    Snackbar.make(btn_create_shipment,"Please select delivery date",Snackbar.LENGTH_SHORT).show();
                }else {
                    mSessionManager.putStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getServiceDescription());
                    mSessionManager.putStringData(SessionManager.KEY_DELIVERY_TIMESTAMP,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp());
                    mSessionManager.putStringData(SessionManager.KEY_DELIVERY_DATE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryDate());
                    mSessionManager.putStringData(SessionManager.KEY_AMOUNT,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount());

                    AppConstant.IS_FROM_CREATE_SHIPMENT = false;
                    AppConstant.IS_FROM_CALCULATE_RATES = false;
                    //Snackbar.make(btn_create_shipment,"->"+ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount(),Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(CheckRateScreenActivity.this,SummeryActivity.class));

                }
                break;

            case R.id.btn_create_shipment:

                if (recyclerViewAdapter.lastSelectedPosition==-1){
                    Snackbar.make(btn_create_shipment,"Please select delivery date",Snackbar.LENGTH_SHORT).show();
                }else {
                    //Snackbar.make(btn_create_shipment,"->"+ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount(),Snackbar.LENGTH_SHORT).show();
                      mSessionManager.putStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getServiceDescription());
                      mSessionManager.putStringData(SessionManager.KEY_DELIVERY_TIMESTAMP,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryTimestamp());
                      mSessionManager.putStringData(SessionManager.KEY_DELIVERY_DATE,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getDeliveryDate());
                      mSessionManager.putStringData(SessionManager.KEY_AMOUNT,ciArr.get(recyclerViewAdapter.lastSelectedPosition).getAmount());

                    if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)) {
                        if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_LOGOUT)) {
                        } else {
                            if(!AppConstant.IS_FROM_CALCULATE_RATES){
                                createShipment();
                            }else {
                               // AppConstant.IS_FROM_CALCULATE_RATES = true;
                                startActivity(new Intent(CheckRateScreenActivity.this,CreateShipmentFromActivity.class));
                            }
                        }
                    }else {
                        //Toast.makeText(CheckRateScreenActivity.this,"hi 2",Toast.LENGTH_SHORT).show();
                        //for outer check rate functionality

                    }

                }

                break;
        }
    }

    private void createShipment(){
        progressBar = new ProgressDialog(CheckRateScreenActivity.this);
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
        params.put(JSONConstant.FROM_CONTACT_NAME,mSessionManager.getStringData(SessionManager.KEY_F_CONTACT_NAME));
        params.put(JSONConstant.FROM_COMPANY_NAME,mSessionManager.getStringData(SessionManager.KEY_F_COMPANY));
        params.put(JSONConstant.FROM_C_PHONE_NO,mSessionManager.getStringData(SessionManager.KEY_F_PHONE_NUMBER));
        params.put(JSONConstant.FROM_C_DIALLING_CODE,mSessionManager.getStringData(SessionManager.KEY_F_DIALLING_CODE));

        params.put(JSONConstant.TO_CONTACT_NAME,mSessionManager.getStringData(SessionManager.KEY_T_CONTACT_NAME));
        params.put(JSONConstant.TO_COMPANY_NAME,mSessionManager.getStringData(SessionManager.KEY_T_COMPANY));
        params.put(JSONConstant.TO_C_PHONE_NO,mSessionManager.getStringData(SessionManager.KEY_T_PHONE_NUMBER));
        params.put(JSONConstant.TO_C_DIALLING_CODE,mSessionManager.getStringData(SessionManager.KEY_T_DIALLING_CODE));

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(CheckRateScreenActivity.this, URL, params, new VolleyResponseListener() {
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

                        Snackbar.make(btn_create_shipment,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        mSessionManager.putStringData(SessionManager.KEY_MASTER_TRACKING_NO,Json_response.getString("master_tracking_no"));
                        mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_ID,Json_response.getString("shipment_id"));

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            Snackbar.make(btn_create_shipment,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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

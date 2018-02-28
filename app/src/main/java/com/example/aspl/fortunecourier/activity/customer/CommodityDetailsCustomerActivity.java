package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.DBHelper.DBCommodity;
import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.adapter.CommodityAdapter;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Commodity;
import com.example.aspl.fortunecourier.model.Rates;
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
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class CommodityDetailsCustomerActivity extends Activity implements View.OnClickListener{

    private RecyclerView recycler_commodity;
    private ArrayList<Commodity> arrayListOfCommodity ;
    private Button btn_add_commodity,btn_submit;
    private TextView tv_shipment_total_weight,tv_total_carriage_value;
    public TextView tv_total_quantity,tv_total_weight,tv_total_custom_value;
    public LinearLayout ll_commodity_details;
    private ImageView img_info;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager ;
    private ConnectionDetector cd;
    private ArrayList<HashMap<String, String>> dataMapOfPackages;
    ArrayList<Rates> ratesArrayList;
    double totalShipmentWeight,totalCarriageValue,totalCommodityWeight,totalCommodityValue;

    //private ArrayList<HashMap<String,String>> dataMapOfSpecialServices;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(CommodityDetailsCustomerActivity.this);
        cd = new ConnectionDetector(CommodityDetailsCustomerActivity.this);
        dataMapOfPackages = new ArrayList<HashMap<String, String>>();

        btn_add_commodity = (Button) findViewById(R.id.btn_add_commodity);
        btn_add_commodity.setOnClickListener(this);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        ll_commodity_details = (LinearLayout) findViewById(R.id.ll_commodity_details);

        tv_shipment_total_weight = (TextView) findViewById(R.id.tv_shipment_total_weight);
        tv_total_carriage_value = (TextView) findViewById(R.id.tv_total_carriage_value);
        tv_total_quantity = (TextView) findViewById(R.id.tv_total_quantity);
        tv_total_weight = (TextView) findViewById(R.id.tv_total_weight);
        tv_total_custom_value = (TextView) findViewById(R.id.tv_total_custom_value);

        recycler_commodity = (RecyclerView) findViewById(R.id.recycler_commodity);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recycler_commodity.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_commodity.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(CommodityDetailsCustomerActivity.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recycler_commodity.addItemDecoration(horizontalDecoration);

        DBCommodity dbCommodity = new DBCommodity(CommodityDetailsCustomerActivity.this);
        arrayListOfCommodity = dbCommodity.getAllCommodityData();

        CommodityAdapter recyclerViewAdapter = new CommodityAdapter(arrayListOfCommodity,CommodityDetailsCustomerActivity.this);
        recycler_commodity.setAdapter(recyclerViewAdapter);

        totalShipmentWeight = Double.valueOf(mSessionManager.getStringData(SessionManager.KEY_TOTAL_SHIPMENT_WEIGHT));
        totalCarriageValue = Double.valueOf(mSessionManager.getStringData(SessionManager.KEY_TOTAL_CARRIAGE_VALUE));
        tv_shipment_total_weight.setText("Shipment Total Weight : " + mSessionManager.getStringData(SessionManager.KEY_TOTAL_SHIPMENT_WEIGHT) + " " + mSessionManager.getStringData(SessionManager.KEY_TOTAL_SHIPMENT_WEIGHT_UNIT));
        tv_total_carriage_value.setText("Total Carriage Value : " + mSessionManager.getStringData(SessionManager.KEY_TOTAL_CARRIAGE_VALUE_UNIT) + " " + mSessionManager.getStringData(SessionManager.KEY_TOTAL_CARRIAGE_VALUE));

         updateCommodityDetails();

    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayListOfCommodity = new ArrayList<>();
        DBCommodity dbCommodity = new DBCommodity(CommodityDetailsCustomerActivity.this);
        arrayListOfCommodity = dbCommodity.getAllCommodityData();

        CommodityAdapter recyclerViewAdapter = new CommodityAdapter(arrayListOfCommodity,CommodityDetailsCustomerActivity.this);
        recycler_commodity.setAdapter(recyclerViewAdapter);
        updateCommodityDetails();
    }

    /* public void setTotalShipmentWeight(double weight){
        this.totalShipmentWeight = weight;
    }

    public double getTotalShipmentWeight(){
        return totalShipmentWeight;
    }

    public void setTotalCarriageValue(double weight){
        this.totalCarriageValue = weight;
    }

    public double getTotalCarriageValue(){
        return totalCarriageValue;
    }*/

    public void updateCommodityDetails(){
        DBCommodity dbCommodity = new DBCommodity(CommodityDetailsCustomerActivity.this);

        if(dbCommodity.getCommodityCount()>0){
            ll_commodity_details.setVisibility(View.VISIBLE);
            Commodity commodity = dbCommodity.getSumOfShipment();
            totalCommodityWeight = Double.valueOf(commodity.getCommodity_weight());
            totalCommodityValue =Double.valueOf(commodity.getCustoms_value());
            tv_total_quantity.setText("Total Quantity : "+commodity.getQuantity());
            tv_total_weight.setText("Total Weight : "+commodity.getCommodity_weight()+" "+commodity.getCommodity_weight_unit());
            tv_total_custom_value.setText("Total Custom Value : "+commodity.getCustoms_value_unit()+" "+commodity.getCustoms_value());
           /* if(totalShipmentWeight>totalCommodityWeight||totalCarriageValue>totalCommodityValue){
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_commodity));
                customDialogForHelp.show();
            }*/
        }else {
            ll_commodity_details.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_commodity:
                startActivity(new Intent(CommodityDetailsCustomerActivity.this,CommodityCustomerActivity.class).putExtra("action","add"));
                //finish();
                break;

            case R.id.btn_submit:
                DBCommodity dbCommodity = new DBCommodity(CommodityDetailsCustomerActivity.this);
                if(dbCommodity.getCommodityCount()>0){
                    Commodity commodity = dbCommodity.getSumOfShipment();
                    if(totalShipmentWeight<Double.valueOf(commodity.getCommodity_weight())){
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_custom_weight));
                        customDialogForHelp.show();
                    }else if(totalCarriageValue>Double.valueOf(commodity.getCustoms_value())){
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_custom_value));
                        customDialogForHelp.show();
                    }else if(!cd.isConnectingToInternet()){
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                        customDialogForHelp.show();
                    }else {
                        getCheckRateDetails();
                    }
                   /* if(totalShipmentWeight>Double.valueOf(commodity.getCommodity_weight())||totalCarriageValue>Double.valueOf(commodity.getCustoms_value())){
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_commodity));
                        customDialogForHelp.show();
                    }else if(!cd.isConnectingToInternet()){
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                        customDialogForHelp.show();
                    }else {
                        getCheckRateDetails();
                    }
*/
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.msg_select_commodity));
                    customDialogForHelp.show();
                }

                break;
            case R.id.img_info:
                DBInfo dbInfo = new DBInfo(CommodityDetailsCustomerActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,"Help",dbInfo.getDescription("Calculate Rates Screen"));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                finish();
                break;
        }
    }

    private void getCheckRateDetails(){
        progressBar = new ProgressDialog(CommodityDetailsCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_customer) +getResources().getString(R.string.url_check_rates);

        Map<String, String> params = new HashMap<String, String>();
            params.put(JSONConstant.CR_CARRIER_CODE, mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
            params.put("request_type", "check_rates");
            params.put(JSONConstant.SERVICE_TYPE, mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE));
            params.put(JSONConstant.FROM_C_COUNTRY_CODE, AppConstant.F_C_COUNTRY_CODE);

          //params.put(JSONConstant.FROM_C_COUNTRY_CODE, mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE));
           // params.put(JSONConstant.FROM_C_ZIPCODE, mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
            params.put(JSONConstant.FROM_C_ZIPCODE, AppConstant.F_C_ZIPCODE);
            params.put(JSONConstant.TO_C_COUNTRY_CODE, AppConstant.T_C_COUNTRY_CODE);
            params.put(JSONConstant.TO_C_ZIPCODE, AppConstant.T_C_ZIPCODE);
        // params.put(JSONConstant.TO_C_COUNTRY_CODE, mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE));
           // params.put(JSONConstant.TO_C_ZIPCODE, mSessionManager.getStringData(SessionManager.KEY_T_ZIP_CODE));
            params.put(JSONConstant.PACKAGECOUNT, mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT));
            params.put(JSONConstant.SERVICESHIPDATE, mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
            params.put(JSONConstant.PACKAGING_TYPE, mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));
            //params.put(JSONConstant.SERVICE_TYPE,strServiceType);

            if (mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT) != null && mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")) {
                params.put(JSONConstant.CONTAINER, "");
                params.put("Shape", mSessionManager.getStringData(SessionManager.KEY_SHAPE));

                params.put(JSONConstant.SIZE, mSessionManager.getStringData(SessionManager.KEY_SIZES));
            }

            params.put(JSONConstant.DROP_OFF_TYPE, mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));
            params.put(JSONConstant.PACKAGING_TYPE_DESCRIPTION, mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE_NAME));
            params.put(JSONConstant.DROPOFF_TYPE_DESCRIPTION, mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP_NAME));

            if (mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)) {
                params.put(JSONConstant.IS_IDENTICAL, "1");
            } else {
                params.put(JSONConstant.IS_IDENTICAL, "0");
            }


            params.put(JSONConstant.CR_CARRIER_CODE, mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));


            if (mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)) {
                if (!AppConstant.hashMapPackage1.isEmpty()) {
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                }
            } else {
                if (!AppConstant.hashMapPackage1.isEmpty()) {
                    if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);

                    } else if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);

                    } else if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);
                        dataMapOfPackages.add(AppConstant.hashMapPackage3);

                    } else if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);
                        dataMapOfPackages.add(AppConstant.hashMapPackage3);
                        dataMapOfPackages.add(AppConstant.hashMapPackage4);

                    } else {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);
                        dataMapOfPackages.add(AppConstant.hashMapPackage3);
                        dataMapOfPackages.add(AppConstant.hashMapPackage4);
                        dataMapOfPackages.add(AppConstant.hashMapPackage5);

                    }
                }
            }

            try {
                List<JSONObject> jsonObj = new ArrayList<JSONObject>();

                for (HashMap<String, String> data : dataMapOfPackages) {
                    JSONObject obj = new JSONObject(data);
                    jsonObj.add(obj);
                }

                JSONArray test = new JSONArray(jsonObj);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Packages", test);

                System.out.println(jsonObject.toString());
                params.put(JSONConstant.PACKAGES, test.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                List<JSONObject> jsonObj = new ArrayList<JSONObject>();

                for (HashMap<String, String> data : AppConstant.noOfSpecialServices) {
                    JSONObject obj = new JSONObject(data);
                    jsonObj.add(obj);
                }

                JSONArray test = new JSONArray(jsonObj);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("special_services", test);

                params.put("special_services", test.toString());


                System.out.println(jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            params.put(JSONConstant.FROM_C_STATE, "");
            params.put(JSONConstant.FROM_C_ADDRESS_LINE1, "");
            params.put(JSONConstant.FROM_C_CITY, "");
            params.put(JSONConstant.TO_C_STATE, "");
            params.put(JSONConstant.TO_C_ADDRESS_LINE1, "");
            params.put(JSONConstant.TO_C_CITY, "");


           /* if (mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE).equalsIgnoreCase("US") && mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE).equalsIgnoreCase("US")) {
                params.put("customs_currency", "");
                params.put("customs_value", "");
                params.put("customs_description", "");

            } else {
                params.put("customs_currency", mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT));
                params.put("customs_value", mSessionManager.getStringData(SessionManager.KEY_CUSTOM_VALUE));
                params.put("customs_description", mSessionManager.getStringData(SessionManager.KEY_CUSTOM_DESC));

            }*/

        params.put("PackageContents", mSessionManager.getStringData(SessionManager.KEY_PACKAGE_CONTENTS));
        params.put("ShipmentPurpose", mSessionManager.getStringData(SessionManager.KEY_SHIPMENT_PURPOSE));

        DBCommodity dbCommodity = new DBCommodity(CommodityDetailsCustomerActivity.this);


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

        VolleyUtils.POST_METHOD(CommodityDetailsCustomerActivity.this, URL,params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.dismiss();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());
                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                        progressBar.dismiss();
                        JSONArray jsonArray = Json_response.getJSONArray("rates");
                        //JSONArray jsonArray = (JSONArray) Json_response.get("rates");
                        Log.e("Hi","->"+jsonArray.length());
                        ratesArrayList = new ArrayList<>();
                        for(int i = 0; i< jsonArray.length() ; i++ ){
                            JSONObject jsonObjectRate = jsonArray.getJSONObject(i);

                            ratesArrayList.add(new Rates(jsonObjectRate.getString("ServiceType"),
                                    jsonObjectRate.getString("Amount"),
                                    jsonObjectRate.getString("ServiceDescription"),
                                    jsonObjectRate.getString("DeliveryDate"),
                                    jsonObjectRate.getString("DeliveryTimestamp"),
                                    jsonObjectRate.getString("BaseAmount"),
                                    jsonObjectRate.getString("cs_tax1"),
                                    jsonObjectRate.getString("cs_tax1_title"),
                                    jsonObjectRate.getString("cs_tax2"),
                                    jsonObjectRate.getString("cs_tax2_title"),
                                    jsonObjectRate.getString("cs_surcharge"),
                                    jsonObjectRate.getString("cs_surcharge_title"),
                                    jsonObjectRate.getString("admin_commission"),

                                    jsonObjectRate.getString("Container")

                            ));
                        }
                        startActivity(new Intent(CommodityDetailsCustomerActivity.this,CheckRateScreenCustomerActivity.class).putParcelableArrayListExtra("RateList",ratesArrayList));

                 /*       if(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")) {
                            JSONArray customOptnList = Json_response.getJSONArray("special_services");

                            dataMapOfSpecialServices = new ArrayList<>();

                            for (int i = 0; i < customOptnList.length(); i++) {
                                JSONObject eachData = customOptnList.getJSONObject(i);

                                HashMap<String, String> hashMapForSpecialServices = new HashMap<String, String>();
                                hashMapForSpecialServices.put("special_service", eachData.getString("special_service"));
                                hashMapForSpecialServices.put("special_service_description", eachData.getString("special_service_description"));

                                if (eachData.has("selected_option")) {
                                    hashMapForSpecialServices.put("selected_option", eachData.getString("selected_option"));
                                    hashMapForSpecialServices.put("selected_option_description", eachData.getString("selected_option_description"));

                                } else {
                                    hashMapForSpecialServices.put("selected_option", " ");
                                    hashMapForSpecialServices.put("selected_option_description", " ");

                                }
                                hashMapForSpecialServices.put("special_service_price", eachData.getString("special_service_price"));
                                hashMapForSpecialServices.put("CurrencyUnit", eachData.getString("CurrencyUnit"));
                                dataMapOfSpecialServices.add(hashMapForSpecialServices);
                            }
                            AppConstant.noOfSpecialServices = dataMapOfSpecialServices;

                            System.out.println("##" + dataMapOfSpecialServices.toString());
                        }*/
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                        progressBar.dismiss();
                        CustomDialogForHelp customDialogForHelp =new CustomDialogForHelp(CommodityDetailsCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.dismiss();
                }


            }
        });
    }

}

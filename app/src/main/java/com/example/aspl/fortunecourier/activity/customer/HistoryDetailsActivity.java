package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.HistoryDetails;
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
 * Created by aspl on 21/12/17.
 */

public class HistoryDetailsActivity extends Activity implements View.OnClickListener{

    private TextView tv_ship_date,tv_from_name,tv_from_address,tv_to_name,tv_to_address,
            tv_service_type,tv_pickup_drop,tv_weight,tv_no_of_packages,tv_carrier,
            tv_package_type,tv_selected_service,tv_timestamp,tv_total_amount;
    private SessionManager mSessionManager;
    private LinearLayout ll_service_type;
    //private Button btn_proceed_to_payment;
    private ImageView img_info;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;
    private String strShipmentId;

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

        ll_service_type = (LinearLayout) findViewById(R.id.ll_service_type);

        tv_weight = (TextView) findViewById(R.id.tv_weight);
        //tv_dimension = (TextView) findViewById(R.id.tv_dimension);
        tv_no_of_packages = (TextView) findViewById(R.id.tv_no_of_packages);

        tv_selected_service = (TextView) findViewById(R.id.tv_selected_service);
        tv_timestamp = (TextView) findViewById(R.id.tv_timestamp);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);

       /* btn_proceed_to_payment = (Button) findViewById(R.id.btn_proceed_to_payment);
        btn_proceed_to_payment.setOnClickListener(this);
 */
        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        strShipmentId = getIntent().getStringExtra("ShipmentId");

        if(cd.isConnectingToInternet()){
            createShipment();
        }else {
            Snackbar.make(tv_carrier,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_info:
                //hideKeyboard();
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(HistoryDetailsActivity.this, "Help", getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                //hideKeyboard();
                finish();
                break;

          /*  case R.id.btn_proceed_to_payment:
                //hideKeyboard();
                if(cd.isConnectingToInternet()){
                    createShipment();
                }else {
                    Snackbar.make(btn_proceed_to_payment,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;*/
        }

    }

    private void createShipment(){
        progressBar = new ProgressDialog(HistoryDetailsActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_view_shipment_details);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put(JSONConstant.SHIPMENT_ID,strShipmentId);

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(HistoryDetailsActivity.this, URL, params, new VolleyResponseListener() {
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
                        tv_carrier.setText(jsonObject.getString("carrier_name"));
                        tv_service_type.setText(jsonObject.getString(JSONConstant.SERVICE_TYPE));
                        tv_package_type.setText(jsonObject.getString(JSONConstant.PACKAGING_TYPE));
                        tv_no_of_packages.setText(jsonObject.getString(JSONConstant.PACKAGECOUNT));
                        tv_pickup_drop.setText(jsonObject.getString(JSONConstant.DROP_OFF_TYPE));
                        tv_ship_date.setText(jsonObject.getString("shipmentdate"));
                        tv_to_name.setText(jsonObject.getString("to_contact_name"));



                        progressBar.dismiss();

                        // "":{"shipment_id":"ship_151435509510DC7B",""master_tracking_no":"794656866660","shipmentday":"FRI","to_contact_name":"nilesh","country_of_origin_code":"US","state_of_origin_code":"IL","origin_addressline1":"415 w golf rd","city_of_origin":"Arlington Heights","origin_postal_code":"60005","from_contact_name":"nilesh","destination_country_code":"US","destination_state_code":"CA","destination_addressline1":"342 Grant Ave","destination_postal_code":"94108","destination_city":"San Francisco","expected_deliver_date":"01\/03\/2018","expected_deliver_day":"WED","status":"Pending","base_amount":"125.43","total_amount":"147.65","surcharge":"5.42","tax1":"4.06","tax2":"","admin_commission":"10.03","currency":"USD","packages":[{"PackageWeight":"20","Length":"0","Height":"0","Width":"0","Girth":"","WeightUnit":"LB","DimensionUnit":"IN","CurrencyUnit":"USD","DesiredValue":"25"}],"tax1_text":"Tax 1","tax2_text":"Tax 2","surcharge_text":"Surcharge"}}


                        // "":{"shipment_id":"ship_151435509510DC7B","carrier_name":"FedEX","service_type":"FEDEX_2_DAY","packaging_type":"FEDEX_BOX","package_count":"1","drop_off_type":"REGULAR_PICKUP","master_tracking_no":"794656866660","shipmentdate":"12\/29\/2017","shipmentday":"FRI","to_contact_name":"nilesh","country_of_origin_code":"US","state_of_origin_code":"IL","origin_addressline1":"415 w golf rd","city_of_origin":"Arlington Heights","origin_postal_code":"60005","from_contact_name":"nilesh","destination_country_code":"US","destination_state_code":"CA","destination_addressline1":"342 Grant Ave","destination_postal_code":"94108","destination_city":"San Francisco","expected_deliver_date":"01\/03\/2018","expected_deliver_day":"WED","status":"Pending","base_amount":"125.43","total_amount":"147.65","surcharge":"5.42","tax1":"4.06","tax2":"","admin_commission":"10.03","currency":"USD","packages":[{"PackageWeight":"20","Length":"0","Height":"0","Width":"0","Girth":"","WeightUnit":"LB","DimensionUnit":"IN","CurrencyUnit":"USD","DesiredValue":"25"}],"tax1_text":"Tax 1","tax2_text":"Tax 2","surcharge_text":"Surcharge"}}

                       /* Snackbar.make(tv_carrier,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        mSessionManager.putStringData(SessionManager.KEY_MASTER_TRACKING_NO,Json_response.getString("master_tracking_no"));
                        mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_ID,Json_response.getString("shipment_id"));
*/
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            Snackbar.make(tv_carrier,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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
}

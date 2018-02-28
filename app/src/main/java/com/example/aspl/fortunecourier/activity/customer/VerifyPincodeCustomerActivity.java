package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.aspl.fortunecourier.DBHelper.DBCommodity;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.associate.VerifyPincodeAssociateActivity;
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
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class VerifyPincodeCustomerActivity extends Activity implements View.OnClickListener{
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    private Button btn_verify;
    private PinView pinView_pincode;
    private ProgressDialog progressBar;
    private ArrayList<HashMap<String, String>> dataMapOfPackages;
    private String strTotalAmount= "";
    private TextView tv_forgot_pincode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_pincode);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(this);
        cd = new ConnectionDetector(this);
        dataMapOfPackages = new ArrayList<>();

        pinView_pincode = (PinView) findViewById(R.id.pinView_pincode);
        strTotalAmount = mSessionManager.getStringData(SessionManager.KEY_AMOUNT);

        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);

        tv_forgot_pincode = (TextView) findViewById(R.id.tv_forgot_pincode);
        tv_forgot_pincode.setOnClickListener(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(pinView_pincode, InputMethodManager.SHOW_IMPLICIT);



        if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
            if(!AppConstant.hashMapPackage1.isEmpty()) {
                dataMapOfPackages.add(AppConstant.hashMapPackage1);
            }
        }else {
            if(!AppConstant.hashMapPackage1.isEmpty()) {
                if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verify:
                hideKeyboard();
                validateAndVerify();
                break;
            case R.id.tv_forgot_pincode:
                hideKeyboard();
                startActivity(new Intent(VerifyPincodeCustomerActivity.this,ForgotPincodeCustomerActivity.class));
                break;
            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;
        }
    }

    private void validateAndVerify(){
        if(cd.isConnectingToInternet()) {
            if (pinView_pincode.getText().length() < 6) {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(VerifyPincodeCustomerActivity.this, getResources().getString(R.string.app_name), getResources().getString(R.string.err_msg_pincode));
                customDialogForHelp.show();
            } else {
                verifyPincode();
            }
        }
          else {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(VerifyPincodeCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                customDialogForHelp.show();
            }
        }


    private void verifyPincode() {
        showProgressDialog();

        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_verify_pin);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID, mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put("c_security_pin", pinView_pincode.getText().toString().trim());
        System.out.println("=>" + params.toString());

        VolleyUtils.POST_METHOD(VerifyPincodeCustomerActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                closeProgressBar();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());

                    if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        Toast.makeText(VerifyPincodeCustomerActivity.this, Json_response.getString(JSONConstant.MESSAGE), Toast.LENGTH_SHORT).show();
                        createShipment();

                    } else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(VerifyPincodeCustomerActivity.this, getResources().getString(R.string.app_name), jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                        }
                    }
                    //closeProgressBar();
                } catch (JSONException e) {
                    closeProgressBar();
                    e.printStackTrace();
                }
            }
        });
    }





    private void createShipment(){
       //showProgressDialog();
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

        /* params.put(JSONConstant.SERVICE_TYPE,AppConstant.SERVICE_TYPE_FOR_UPS);
        params.put("expected_delivery_timestamp",AppConstant.EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS);
        params.put("service_description",AppConstant.SERVICE_TYPE_DESCR_FOR_UPS);*/


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
            closeProgressBar();
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
            closeProgressBar();
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
        params.put("collection_fee",mSessionManager.getStringData(SessionManager.KEY_COLLECTION_FEES));

        /* if(mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE).equalsIgnoreCase("US")&& mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE).equalsIgnoreCase("US")) {
        params.put("customs_currency","");
        params.put("customs_value","");
        params.put("customs_description","");

    }else {
        params.put("customs_currency",mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT));
        params.put("customs_value",mSessionManager.getStringData(SessionManager.KEY_CUSTOM_VALUE));
        params.put("customs_description",mSessionManager.getStringData(SessionManager.KEY_CUSTOM_DESC));

    }*/


    params.put("PackageContents", mSessionManager.getStringData(SessionManager.KEY_PACKAGE_CONTENTS));
    params.put("ShipmentPurpose", mSessionManager.getStringData(SessionManager.KEY_SHIPMENT_PURPOSE));
    params.put("currency",mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT));
        //params.put("Amount",strGrandTotal);
    params.put("token",getIntent().getStringExtra("token"));
        //params.put("total_amount",mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        //params.put("discount",strDiscountAmount);
     params.put("coupon_code",AppConstant.APPLIED_COUPON_CODE);
     params.put("customer_id",getIntent().getStringExtra("customer_id"));
     params.put("save_card_details",getIntent().getStringExtra("save_card_details"));
     params.put("card_no_last_digits",getIntent().getStringExtra("card_no_last_digits"));
     params.put("cvv",getIntent().getStringExtra("cvv"));

    DBCommodity dbCommodity = new DBCommodity(VerifyPincodeCustomerActivity.this);


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
       closeProgressBar();
    }

    System.out.println("=>"+params.toString());

    VolleyUtils.POST_METHOD(VerifyPincodeCustomerActivity.this, URL, params, new VolleyResponseListener() {
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

                    DBCommodity dbCommodity= new DBCommodity(VerifyPincodeCustomerActivity.this);
                    dbCommodity.deleteTableData(VerifyPincodeCustomerActivity.this);

                    if(AppConstant.T_C_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_C_COUNTRY_CODE.equalsIgnoreCase("US")||Json_response.getString("note").isEmpty()){
                        Toast.makeText(VerifyPincodeCustomerActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                        //Snackbar.make(btn_proceed_to_payment,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        mSessionManager.putStringData(SessionManager.KEY_MASTER_TRACKING_NO,Json_response.getString("master_tracking_no"));
                        //mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_ID,Json_response.getString("shipment_id"));
                        mSessionManager.putStringData(SessionManager.KEY_AMOUNT,strTotalAmount);
                        startActivity(new Intent(VerifyPincodeCustomerActivity.this,LabelCustomerActivity.class).putExtra(JSONConstant.STATUS,JSONConstant.SUCCESS).putExtra(JSONConstant.MESSAGE,Json_response.getString(JSONConstant.MESSAGE)).putExtra("pdf",Json_response.getString("pdf_label")).putExtra(JSONConstant.MASTER_TRACKING_NO,Json_response.getString(JSONConstant.MASTER_TRACKING_NO)));
                        finish();

                        //startActivity(new Intent(VerifyPincodeCustomerActivity.this,PaymentCustomerActivity.class));
                    }else {
                        showNote(VerifyPincodeCustomerActivity.this,Json_response.getString("note"),Json_response.getString(JSONConstant.MESSAGE),Json_response.getString("pdf_label"),Json_response.getString(JSONConstant.MASTER_TRACKING_NO));
                    }
                    closeProgressBar();

                }else {
                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                    if (jsonObject.has(JSONConstant.MESSAGE)) {
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(VerifyPincodeCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();
                        //Snackbar.make(btn_proceed_to_payment,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                    }
                    closeProgressBar();
                }
                closeProgressBar();
            } catch (JSONException e) {
                e.printStackTrace();
                closeProgressBar();
            }
        }
    });
}


    private void showNote(Context context, String strMessage, final String jsonMessage, final String pdfLabel, final String trackingNo){

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
                startActivity(new Intent(VerifyPincodeCustomerActivity.this,LabelCustomerActivity.class).putExtra(JSONConstant.STATUS,JSONConstant.SUCCESS).putExtra(JSONConstant.MESSAGE,jsonMessage).putExtra("pdf",pdfLabel).putExtra(JSONConstant.MASTER_TRACKING_NO,trackingNo));
                finish();
                //startActivity(new Intent(VerifyPincodeCustomerActivity.this,LabelCustomerActivity.class));

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

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showProgressDialog(){
        progressBar = new ProgressDialog(VerifyPincodeCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        //progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }

    private void closeProgressBar(){
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
    }

}




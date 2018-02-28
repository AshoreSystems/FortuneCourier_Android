package com.example.aspl.fortunecourier;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;

/**
 * Created by aspl on 18/1/18.
 */

public class TrackShipmentDetailsActivity extends Activity implements View.OnClickListener{

    private TextView tv_ship_date,tv_from_name,tv_from_address,tv_to_name,tv_to_address,
            tv_service_type,tv_pickup_drop,tv_weight,tv_no_of_packages,tv_carrier,
            tv_package_type,tv_total_amount,tv_tracking_number,tv_status,tv_delivery_date;
    private SessionManager mSessionManager;
    private LinearLayout ll_service_type;
    private ImageView img_info;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;
    private String strShipmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_shipment_details);
        
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

        tv_tracking_number = (TextView) findViewById(R.id.tv_tracking_number);

        ll_service_type = (LinearLayout) findViewById(R.id.ll_service_type);

        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_no_of_packages = (TextView) findViewById(R.id.tv_no_of_packages);

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_delivery_date = (TextView) findViewById(R.id.tv_delivery_date);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        //strShipmentId = getIntent().getStringExtra("ShipmentId");

        tv_carrier.setText(getIntent().getStringExtra("carrier_name"));
        tv_service_type.setText(getIntent().getStringExtra(JSONConstant.SERVICE_TYPE));
        tv_package_type.setText(getIntent().getStringExtra(JSONConstant.PACKAGING_TYPE));
        tv_no_of_packages.setText(getIntent().getStringExtra(JSONConstant.PACKAGECOUNT));
        tv_pickup_drop.setText(getIntent().getStringExtra(JSONConstant.DROP_OFF_TYPE));
        tv_ship_date.setText(getIntent().getStringExtra("shipment_details"));
        tv_to_name.setText(getIntent().getStringExtra(JSONConstant.TO_CONTACT_NAME));
        tv_from_name.setText(getIntent().getStringExtra(JSONConstant.FROM_CONTACT_NAME));
        tv_tracking_number.setText(getIntent().getStringExtra(JSONConstant.MASTER_TRACKING_NO));
        tv_to_address.setText(getIntent().getStringExtra("to_address"));
        tv_from_address.setText(getIntent().getStringExtra("from_address"));
        //tv_total_amount.setText("Total Amount : "+getIntent().getStringExtra("total_amount")+"\nBase Amount : "+getIntent().getStringExtra("base_amount")+"\n"+getIntent().getStringExtra("cs_tax1_title")+" : "+getIntent().getStringExtra("tax1")+"\n"+getIntent().getStringExtra("cs_tax2_title")+" : "+getIntent().getStringExtra("tax2")+"\n"+getIntent().getStringExtra("cs_surcharge_title")+" : "+getIntent().getStringExtra("surcharge"));
        tv_total_amount.setText(getIntent().getStringExtra("total_amount"));
        tv_status.setText(getIntent().getStringExtra("status"));
        tv_delivery_date.setText(getIntent().getStringExtra("delivery_details"));

        if(getIntent().getStringExtra("is_identical").equalsIgnoreCase("Yes")){
            if(!AppConstant.hashMapPackage1.isEmpty()) {
                tv_weight.setText(getPackageTitle("Packages")+"\n"+getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT),AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT),AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)));
            }
        }else {
            if (!AppConstant.hashMapPackage1.isEmpty()) {
                if (getIntent().getStringExtra(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("1")) {
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE))));

                } else if (getIntent().getStringExtra(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("2")) {
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE))) + "\n"
                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE))));

                } else if (getIntent().getStringExtra(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("3")) {
                    tv_weight.setText(getPackageTitle(AppConstant.hashMapPackage1.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage1.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage1.get(AppConstant.HM_DECLARED_VALUE)))
                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage2.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage2.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage2.get(AppConstant.HM_DECLARED_VALUE)))
                            + "\n\n" + getPackageTitle(AppConstant.hashMapPackage3.get(AppConstant.HM_PACKAGE_NO) + "\n" + getPackageDetails(AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT), AppConstant.hashMapPackage3.get(AppConstant.HM_WEIGHT_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_CURRENCY_UNIT), AppConstant.hashMapPackage3.get(AppConstant.HM_DECLARED_VALUE))));

                } else if (getIntent().getStringExtra(JSONConstant.PACKAGECOUNT).equalsIgnoreCase("4")) {
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

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_info:
                //hideKeyboard();
                DBInfo dbInfo = new DBInfo(TrackShipmentDetailsActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(TrackShipmentDetailsActivity.this, "Help", dbInfo.getDescription("Track Shipment Screen Details"));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                //hideKeyboard();
                finish();
                break;
            
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
}

package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Carriers;
import com.example.aspl.fortunecourier.model.DropOff;
import com.example.aspl.fortunecourier.model.PackageDetails;
import com.example.aspl.fortunecourier.model.Rates;
import com.example.aspl.fortunecourier.model.ServiceTypes;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aspl on 24/11/17.
 */

public class ShipmentDetailsCustomerActivity extends Activity implements DatePickerDialog.OnDateSetListener, View.OnClickListener,AdapterView.OnItemSelectedListener{

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    private TextInputLayout textInput_ship_date,textInput_no_of_packages;
    private EditText editText_ship_date,editText_no_of_packages;
    static List<String> arrayListOfPackages = new ArrayList<>();
    private Button btn_get_quick_quote,btn_get_detailed_quote;
    private LayoutInflater layoutInflater;
    private TextInputLayout textInput_weight1, textInput_weight2, textInput_weight3, textInput_weight4, textInput_weight5;
    private TextInputLayout textInput_declared_value1, textInput_declared_value2, textInput_declared_value3, textInput_declared_value4, textInput_declared_value5;
   // private EditText editText_weight1, editText_weight2, editText_weight3, editText_weight4, editText_weight5;
   // private EditText editText_declared_value1, editText_declared_value2, editText_declared_value3, editText_declared_value4, editText_declared_value5;
    private LinearLayout linear_package1, linear_package2, linear_package3, linear_package4, linear_package5;
    int count = 1;
    private ConnectionDetector cd;
    private ProgressDialog progressBar;
    private int position_of_spinner;
    private TextView tv_first,tv_second,tv_third,tv_fourth,tv_fifth;
    private TextView tv_package1,tv_package2,tv_package3,tv_package4,tv_package5;
    private TextView tv_package_details1,tv_package_details2,tv_package_details3,tv_package_details4,tv_package_details5;
    private CheckBox cb_identical;
    private String strTextViewNo = "1";
    PackageDetails mPackageDetails;
    HashMap<String,String> hashMapPackage1 = new HashMap<>();
    HashMap<String,String> hashMapPackage2 = new HashMap<>();
    HashMap<String,String> hashMapPackage3 = new HashMap<>();
    HashMap<String,String> hashMapPackage4 = new HashMap<>();
    HashMap<String,String> hashMapPackage5 = new HashMap<>();
    ScrollView scrollView;

    //private Spinner spinner_carrier,spinner_service_type,spinner_pickup_drop,spinner_package_type;
    private SearchableSpinner spinner_carrier,spinner_service_type,spinner_pickup_drop,spinner_package_type;

    private TextView tv_select_carrier,tv_service_type;//,tv_weight_unit;
    private ArrayList<Carriers>  arrayListCarriers;
    private ArrayList<DropOff> arrayListDropOffs;
    private ArrayList<ServiceTypes> arrayListServiceTypes;
    private ArrayList<String> carrierList;
    private ArrayList<String> seviceTypeList;
    private ArrayList<String> dropOffList;
    private ArrayList<String> packageTypeList;
    private ArrayList<String> packageNameList;
    ArrayList<HashMap<String, String>> dataMapOfPackages;
    ArrayList<Rates> ratesArrayList;
    String strPackageType;
    String strPackageName;
    String strCarrierCode;
    String strDropoffType;
    String strDropOffDescription;
    String strServiceType="";
    String strServiceTypeDesc="";

    private ImageView img_info;
    String strDimensionalUnit = "IN";
    String strWeightUnit ="LB";
    String strCurrencyUnit = "USD";
    private SessionManager mSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_details);
        init();
    }

    public void init() {
        cd = new ConnectionDetector(this);
        mSessionManager = new SessionManager(this);

        arrayListCarriers =  new ArrayList<>();
        arrayListDropOffs = new ArrayList<>();
        arrayListServiceTypes = new ArrayList<>();

        carrierList = new ArrayList<>();
        seviceTypeList = new ArrayList<>();
        dropOffList = new ArrayList<>();
        packageTypeList = new ArrayList<>();
        packageNameList = new ArrayList<>();
        ratesArrayList = new ArrayList<>();

        dataMapOfPackages = new ArrayList<HashMap<String, String>>();

        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        btn_get_quick_quote = (Button) findViewById(R.id.btn_get_quick_quote);
        btn_get_quick_quote.setOnClickListener(this);

        btn_get_detailed_quote = (Button) findViewById(R.id.btn_get_detailed_quote);
        btn_get_detailed_quote.setOnClickListener(this);

        editText_ship_date = (EditText) findViewById(R.id.editText_ship_date);
        editText_ship_date.setOnClickListener(this);

       /* editText_no_of_packages = (EditText) findViewById(R.id.editText_no_of_packages);
        editText_no_of_packages.setOnClickListener(this);
*/
      /*  editText_weight1 = (EditText) findViewById(R.idid.editText_weight1);
        editText_weight2 = (EditText) findViewById(R.id.editText_weight2);
        editText_weight3 = (EditText) findViewById(R.id.editText_weight3);
        editText_weight4 = (EditText) findViewById(R.id.editText_weight4);
        editText_weight5 = (EditText) findViewById(R.id.editText_weight5);

        editText_declared_value1 = (EditText) findViewById(R.id.editText_declared_value1);
        editText_declared_value2 = (EditText) findViewById(R.id.editText_declared_value2);
        editText_declared_value3 = (EditText) findViewById(R.id.editText_declared_value3);
        editText_declared_value4 = (EditText) findViewById(R.id.editText_declared_value4);
        editText_declared_value5 = (EditText) findViewById(R.id.editText_declared_value5);
*/
        linear_package1 = (LinearLayout) findViewById(R.id.linear_package1);
        linear_package1.setOnClickListener(this);

        linear_package2 = (LinearLayout) findViewById(R.id.linear_package2);
        linear_package2.setOnClickListener(this);

        linear_package3 = (LinearLayout) findViewById(R.id.linear_package3);
        linear_package3.setOnClickListener(this);

        linear_package4 = (LinearLayout) findViewById(R.id.linear_package4);
        linear_package4.setOnClickListener(this);

        linear_package5 = (LinearLayout) findViewById(R.id.linear_package5);
        linear_package5.setOnClickListener(this);

        textInput_ship_date = (TextInputLayout) findViewById(R.id.textInput_ship_date);

        tv_package1 = (TextView) findViewById(R.id.tv_package1);
        tv_package2 = (TextView) findViewById(R.id.tv_package2);
        tv_package3 = (TextView) findViewById(R.id.tv_package3);
        tv_package4 = (TextView) findViewById(R.id.tv_package4);
        tv_package5 = (TextView) findViewById(R.id.tv_package5);

        tv_package_details1 = (TextView) findViewById(R.id.tv_package_details1);
        tv_package_details2 = (TextView) findViewById(R.id.tv_package_details2);
        tv_package_details3 = (TextView) findViewById(R.id.tv_package_details3);
        tv_package_details4 = (TextView) findViewById(R.id.tv_package_details4);
        tv_package_details5 = (TextView) findViewById(R.id.tv_package_details5);


        cb_identical = (CheckBox) findViewById(R.id.cb_identical);
        cb_identical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv_package1.setText("Packages");
                    linear_package1.setVisibility(View.VISIBLE);
                    hideLayout(linear_package2);
                    hideLayout(linear_package3);
                    hideLayout(linear_package4);
                    hideLayout(linear_package5);
                    //showPackageDetails(tv_package_details1,hashMapPackage1);
                    hashMapPackage2.clear();
                    hashMapPackage3.clear();
                    hashMapPackage4.clear();
                    hashMapPackage5.clear();
                    dataMapOfPackages.remove(hashMapPackage2);
                    dataMapOfPackages.remove(hashMapPackage3);
                    dataMapOfPackages.remove(hashMapPackage4);
                    dataMapOfPackages.remove(hashMapPackage5);
                }
                else {
                    tv_package1.setText(getResources().getString(R.string.lbl_package_1));
                    if(strTextViewNo.equalsIgnoreCase("2")){
                        tv_second.performClick();
                    }else if(strTextViewNo.equalsIgnoreCase("3")){
                        tv_third.performClick();
                    }else if(strTextViewNo.equalsIgnoreCase("4")){
                        tv_fourth.performClick();
                    }else if(strTextViewNo.equalsIgnoreCase("5")){
                        tv_fifth.performClick();
                    }
                }
            }
        });

        tv_first = (TextView) findViewById(R.id.tv_first);
        tv_first.setOnClickListener(this);

        tv_second = (TextView) findViewById(R.id.tv_second);
        tv_second.setOnClickListener(this);

        tv_third = (TextView) findViewById(R.id.tv_third);
        tv_third.setOnClickListener(this);

        tv_fourth = (TextView) findViewById(R.id.tv_fourth);
        tv_fourth.setOnClickListener(this);

        tv_fifth = (TextView) findViewById(R.id.tv_fifth);
        tv_fifth.setOnClickListener(this);

        tv_select_carrier = (TextView) findViewById(R.id.tv_select_carrier);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);

        //spinner_carrier = (Spinner) findViewById(R.id.spinner_carrier);
        spinner_carrier = (SearchableSpinner) findViewById(R.id.spinner_carrier);
        spinner_carrier.setTitle(getResources().getString(R.string.lbl_select_carrier));
        spinner_carrier.setOnItemSelectedListener(this);

        //spinner_service_type = (Spinner) findViewById(R.id.spinner_service_type);
        spinner_service_type = (SearchableSpinner) findViewById(R.id.spinner_service_type);
        spinner_service_type.setTitle(getResources().getString(R.string.lbl_service_type));
        spinner_service_type.setOnItemSelectedListener(this);
      /*  spinner_service_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShipmentDetailsCustomerActivity.this,spinner_carrier.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();

            }
        });*/

        //spinner_pickup_drop = (Spinner) findViewById(R.id.spinner_pickup_drop);
        spinner_pickup_drop = (SearchableSpinner) findViewById(R.id.spinner_pickup_drop);
        spinner_pickup_drop.setTitle(getResources().getString(R.string.lbl_select_pickup_drop));
        spinner_pickup_drop.setOnItemSelectedListener(this);

        //spinner_package_type = (Spinner) findViewById(R.id.spinner_package_type);
        spinner_package_type = (SearchableSpinner) findViewById(R.id.spinner_package_type);
        spinner_package_type.setTitle(getResources().getString(R.string.lbl_select_package_type));
        spinner_package_type.setOnItemSelectedListener(this);


        if(AppConstant.T_C_COUNTRY_CODE.equalsIgnoreCase("US")&&AppConstant.F_C_COUNTRY_CODE.equalsIgnoreCase("US")){
            tv_select_carrier.setVisibility(View.VISIBLE);
            spinner_carrier.setVisibility(View.VISIBLE);
            if(cd.isConnectingToInternet()){
                showProgressDialog();
                getCarriers();
            }else {
                Snackbar.make(tv_first,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
            }

        }else {
            if(cd.isConnectingToInternet()){
                strCarrierCode = "FedEX";
                getServiceTypes();
            }else {
                Snackbar.make(tv_first,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
            }
        }

        tv_first.performClick();

        scrollView = (ScrollView)findViewById(R.id.scrollView);

        if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

            Log.e("Harshada=>",mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
            editText_ship_date.setText(mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
            if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
                cb_identical.setChecked(true);
               // tv_first.performClick();
                if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    hashMapPackage1.putAll(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage1);
                    tv_first.performClick();
                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    hashMapPackage2.putAll(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage2);
                    tv_second.performClick();

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                    hashMapPackage3.putAll(AppConstant.hashMapPackage3);
                    dataMapOfPackages.add(hashMapPackage3);
                    tv_third.performClick();

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                    hashMapPackage4.putAll(AppConstant.hashMapPackage4);
                    dataMapOfPackages.add(hashMapPackage4);
                    tv_fourth.performClick();
                }else {
                    hashMapPackage5.putAll(AppConstant.hashMapPackage5);
                    dataMapOfPackages.add(hashMapPackage5);
                    tv_fifth.performClick();
                }

                hashMapPackage1.putAll(AppConstant.hashMapPackage1);
                dataMapOfPackages.add(hashMapPackage1);
                tv_package1.setText("Packages");
                linear_package1.setVisibility(View.VISIBLE);
                hideLayout(linear_package2);
                hideLayout(linear_package3);
                hideLayout(linear_package4);
                hideLayout(linear_package5);
                showPackageDetails(tv_package_details1,hashMapPackage1);
                hashMapPackage2.clear();
                hashMapPackage3.clear();
                hashMapPackage4.clear();
                hashMapPackage5.clear();


            }else {
               cb_identical.setChecked(false);
                if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    hashMapPackage1.putAll(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage1);
                    tv_first.performClick();
                    showPackageDetails(tv_package_details1,hashMapPackage1);


                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    hashMapPackage1.putAll(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage1);
                    tv_first.performClick();
                    showPackageDetails(tv_package_details1,hashMapPackage1);

                    hashMapPackage2.putAll(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage2);
                    tv_second.performClick();
                    showPackageDetails(tv_package_details2,hashMapPackage2);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){

                    hashMapPackage1.putAll(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage1);
                    tv_first.performClick();
                    showPackageDetails(tv_package_details1,hashMapPackage1);

                    hashMapPackage2.putAll(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage2);
                    tv_second.performClick();
                    showPackageDetails(tv_package_details2,hashMapPackage2);

                    hashMapPackage3.putAll(AppConstant.hashMapPackage3);
                    dataMapOfPackages.add(hashMapPackage3);
                    tv_third.performClick();
                    showPackageDetails(tv_package_details3,hashMapPackage3);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){

                    hashMapPackage1.putAll(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage1);
                    tv_first.performClick();
                    showPackageDetails(tv_package_details1,hashMapPackage1);

                    hashMapPackage2.putAll(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage2);
                    tv_second.performClick();
                    showPackageDetails(tv_package_details2,hashMapPackage2);

                    hashMapPackage3.putAll(AppConstant.hashMapPackage3);
                    dataMapOfPackages.add(hashMapPackage3);
                    tv_third.performClick();
                    showPackageDetails(tv_package_details3,hashMapPackage3);

                    hashMapPackage4.putAll(AppConstant.hashMapPackage4);
                    dataMapOfPackages.add(hashMapPackage4);
                    tv_fourth.performClick();
                    showPackageDetails(tv_package_details4,hashMapPackage4);

                }else {
                    hashMapPackage1.putAll(AppConstant.hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage1);
                    tv_first.performClick();
                    showPackageDetails(tv_package_details1,hashMapPackage1);

                    hashMapPackage2.putAll(AppConstant.hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage2);
                    tv_second.performClick();
                    showPackageDetails(tv_package_details2,hashMapPackage2);

                    hashMapPackage3.putAll(AppConstant.hashMapPackage3);
                    dataMapOfPackages.add(hashMapPackage3);
                    tv_third.performClick();
                    showPackageDetails(tv_package_details3,hashMapPackage3);

                    hashMapPackage4.putAll(AppConstant.hashMapPackage4);
                    dataMapOfPackages.add(hashMapPackage4);
                    tv_fourth.performClick();
                    showPackageDetails(tv_package_details4,hashMapPackage4);


                    hashMapPackage5.putAll(AppConstant.hashMapPackage5);
                    dataMapOfPackages.add(hashMapPackage5);
                    tv_fifth.performClick();
                    showPackageDetails(tv_package_details5,hashMapPackage5);

                }
            }
        }


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear+1;
        String date = year + "-" + month + "-" + dayOfMonth;
        editText_ship_date.setText(date);
    }
/*  spinner_service_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShipmentDetailsCustomerActivity.this,spinner_carrier.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();

            }
        });*/
    private String getTextViewNumber(TextView textView){
        return  textView.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editText_ship_date:
                hideKeyboard();
                datePickerDialog = DatePickerDialog.newInstance(ShipmentDetailsCustomerActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#009688"));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                break;

            case R.id.btn_get_quick_quote:
                hideKeyboard();

                // Toast.makeText(ShipmentDetailsCustomerActivity.this,"->"+tv_package_details1.getText().toString()+","+tv_package_details2.getText().toString()+","+tv_package_details3.getText().toString()+","+tv_package_details4.getText().toString()+","+tv_package_details5.getText().toString(),Toast.LENGTH_SHORT).show();
                if(cd.isConnectingToInternet()){
                    validateAndNext();
                }else {
                    Snackbar.make(btn_get_quick_quote,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_get_detailed_quote:
                hideKeyboard();

                //Toast.makeText(ShipmentDetailsCustomerActivity.this,"size->"+dataMapOfPackages.size(),Toast.LENGTH_SHORT).show();

               /* if(cd.isConnectingToInternet()){
                    validateAndNext();
                }else {
                    Snackbar.make(btn_get_quick_quote,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }*/
                break;

            case R.id.img_info:
                hideKeyboard();
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsCustomerActivity.this,"Help",getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.tv_first:
                hideKeyboard();

                strTextViewNo = getTextViewNumber(tv_first);
                cb_identical.setChecked(false);
                cb_identical.setEnabled(false);
                linear_package1.setVisibility(View.VISIBLE);
                hideLayout(linear_package2);
                hideLayout(linear_package3);
                hideLayout(linear_package4);
                hideLayout(linear_package5);
                highLightText(tv_first,tv_second,tv_third,tv_fourth,tv_fifth);
                hashMapPackage2.clear();
                hashMapPackage3.clear();
                hashMapPackage4.clear();
                hashMapPackage5.clear();
                dataMapOfPackages.remove(hashMapPackage2);
                dataMapOfPackages.remove(hashMapPackage3);
                dataMapOfPackages.remove(hashMapPackage4);
                dataMapOfPackages.remove(hashMapPackage5);

                break;

            case R.id.tv_second:
                hideKeyboard();
                strTextViewNo = getTextViewNumber(tv_second);
                cb_identical.setEnabled(true);
                if(cb_identical.isChecked()){
                    linear_package2.setVisibility(View.GONE);
                }else {
                    linear_package1.setVisibility(View.VISIBLE);
                    linear_package2.setVisibility(View.VISIBLE);
                }

                hideLayout(linear_package3);
                hideLayout(linear_package4);
                hideLayout(linear_package5);
                highLightText(tv_second,tv_first,tv_third,tv_fourth,tv_fifth);
                hashMapPackage3.clear();
                hashMapPackage4.clear();
                hashMapPackage5.clear();
                dataMapOfPackages.remove(hashMapPackage3);
                dataMapOfPackages.remove(hashMapPackage4);
                dataMapOfPackages.remove(hashMapPackage5);
                scrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case R.id.tv_third:
                hideKeyboard();
                strTextViewNo = getTextViewNumber(tv_third);
                cb_identical.setEnabled(true);
                if(cb_identical.isChecked()){
                    linear_package2.setVisibility(View.GONE);
                    linear_package3.setVisibility(View.GONE);
                }else {
                    linear_package1.setVisibility(View.VISIBLE);
                    linear_package2.setVisibility(View.VISIBLE);
                    linear_package3.setVisibility(View.VISIBLE);
                }

                hideLayout(linear_package4);
                hideLayout(linear_package5);
                highLightText(tv_third,tv_second,tv_first,tv_fourth,tv_fifth);
                hashMapPackage4.clear();
                hashMapPackage5.clear();

                dataMapOfPackages.remove(hashMapPackage4);
                dataMapOfPackages.remove(hashMapPackage5);

                scrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case R.id.tv_fourth:
                hideKeyboard();
                strTextViewNo = getTextViewNumber(tv_fourth);
                cb_identical.setEnabled(true);

                if(cb_identical.isChecked()){
                    linear_package2.setVisibility(View.GONE);
                    linear_package3.setVisibility(View.GONE);
                    linear_package4.setVisibility(View.GONE);
                }else {
                    linear_package1.setVisibility(View.VISIBLE);
                    linear_package2.setVisibility(View.VISIBLE);
                    linear_package3.setVisibility(View.VISIBLE);
                    linear_package4.setVisibility(View.VISIBLE);
                }

                hideLayout(linear_package5);
                highLightText(tv_fourth,tv_second,tv_third,tv_first,tv_fifth);
                hashMapPackage5.clear();
                dataMapOfPackages.remove(hashMapPackage5);

                scrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case R.id.tv_fifth:
                hideKeyboard();
                strTextViewNo = getTextViewNumber(tv_fifth);
                cb_identical.setEnabled(true);

                if(cb_identical.isChecked()){
                    linear_package2.setVisibility(View.GONE);
                    linear_package3.setVisibility(View.GONE);
                    linear_package4.setVisibility(View.GONE);
                    linear_package5.setVisibility(View.GONE);
                }else {
                    linear_package1.setVisibility(View.VISIBLE);
                    linear_package2.setVisibility(View.VISIBLE);
                    linear_package3.setVisibility(View.VISIBLE);
                    linear_package4.setVisibility(View.VISIBLE);
                    linear_package5.setVisibility(View.VISIBLE);
                }

                highLightText(tv_fifth,tv_second,tv_third,tv_fourth,tv_first);
                scrollView.fullScroll(View.FOCUS_DOWN);
                break;
/*  spinner_service_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShipmentDetailsCustomerActivity.this,spinner_carrier.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();

            }
        });*/
            case R.id.linear_package1:
                hideKeyboard();
                ShowCustomDialog(getResources().getString(R.string.lbl_package_1),tv_package_details1,hashMapPackage1);
                 break;
            case R.id.linear_package2:
                hideKeyboard();
                ShowCustomDialog(getResources().getString(R.string.lbl_package_2),tv_package_details2,hashMapPackage2);
                break;
            case R.id.linear_package3:
                hideKeyboard();
                ShowCustomDialog(getResources().getString(R.string.lbl_package_3),tv_package_details3,hashMapPackage3);
                break;
            case R.id.linear_package4:
                hideKeyboard();
                ShowCustomDialog(getResources().getString(R.string.lbl_package_4),tv_package_details4,hashMapPackage4);
                break;
            case R.id.linear_package5:
                hideKeyboard();
                ShowCustomDialog(getResources().getString(R.string.lbl_package_5),tv_package_details5,hashMapPackage5);
                break;
        }
    }

    private void hideLayout(LinearLayout linearLayout){
        linearLayout.setVisibility(View.GONE);
    }

    private void highLightText(TextView textView1,TextView textView2,TextView textView3, TextView textView4, TextView textView5){
        textView1.setBackgroundColor(getResources().getColor(R.color.colorRed));
        textView1.setTextColor(getResources().getColor(R.color.colorWhite));

        textView2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        textView2.setTextColor(getResources().getColor(android.R.color.black));

        textView3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        textView3.setTextColor(getResources().getColor(android.R.color.black));

        textView4.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        textView4.setTextColor(getResources().getColor(android.R.color.black));

        textView5.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        textView5.setTextColor(getResources().getColor(android.R.color.black));
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void validateAndNext(){
        if(editText_ship_date.getText().toString().isEmpty()){
            textInput_ship_date.setError(getResources().getString(R.string.err_msg_shipment_date));
            requestFocus(editText_ship_date);
        }else if(AppConstant.T_C_COUNTRY_CODE.equalsIgnoreCase("US")&&AppConstant.F_C_COUNTRY_CODE.equalsIgnoreCase("US")&&spinner_carrier.getSelectedItemPosition()==-1) {
            Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_carrier),Snackbar.LENGTH_SHORT).show();
        }else if(spinner_pickup_drop.getSelectedItemPosition()==-1){
            Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_pickup_drop),Snackbar.LENGTH_SHORT).show();
        }else if(spinner_package_type.getSelectedItemPosition()==-1){
            Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_package_type),Snackbar.LENGTH_SHORT).show();
        }else if (strTextViewNo.equalsIgnoreCase("1")&&tv_package_details1.getText().toString().isEmpty()){
            Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
        } else if(!cb_identical.isChecked()){
            Log.e("Harshada"," 2 => "+tv_package_details2.getText().toString().isEmpty()+" 3 => "+tv_package_details3.getText().toString().isEmpty()+" 4 => "+tv_package_details4.getText().toString().isEmpty()+"  5 => "+tv_package_details5.getText().toString().isEmpty());
            if(strTextViewNo.equalsIgnoreCase("2")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty())){
                Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
            } else if(strTextViewNo.equalsIgnoreCase("3")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty()||tv_package_details3.getText().toString().isEmpty())){
                Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
            } else if(strTextViewNo.equalsIgnoreCase("4")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty()||tv_package_details3.getText().toString().isEmpty()||tv_package_details4.getText().toString().isEmpty())){
                Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
            } else if(strTextViewNo.equalsIgnoreCase("5")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty()||tv_package_details3.getText().toString().isEmpty()||tv_package_details4.getText().toString().isEmpty()||tv_package_details5.getText().toString().isEmpty())){
                Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
            } else {
                textInput_ship_date.setErrorEnabled(false);
                AppConstant.SHIPMENT_DATE = editText_ship_date.getText().toString().trim();

                mSessionManager.putStringData(SessionManager.KEY_SHIP_DATE,editText_ship_date.getText().toString().trim());
                mSessionManager.putStringData(SessionManager.KEY_CARRIER_CODE,strCarrierCode);
                mSessionManager.putStringData(SessionManager.KEY_SERVICE_TYPE,strServiceType);
                mSessionManager.putStringData(SessionManager.KEY_SERVICE_TYPE_NAME,strServiceTypeDesc);

                mSessionManager.putStringData(SessionManager.KEY_PICKUP_DROP,strDropoffType);
                mSessionManager.putStringData(SessionManager.KEY_PICKUP_DROP_NAME,strDropOffDescription);

                mSessionManager.putStringData(SessionManager.KEY_PACKAGE_TYPE,strPackageType);
                mSessionManager.putStringData(SessionManager.KEY_PACKAGE_TYPE_NAME,strPackageName);

                mSessionManager.putStringData(SessionManager.KEY_PACKAGE_COUNT,strTextViewNo);
                if(cb_identical.isChecked()){
                    mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,true);
                }else {
                    mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,false);
                }

                getCheckRatesDetails();
            }
        }else {
            textInput_ship_date.setErrorEnabled(false);
            AppConstant.SHIPMENT_DATE = editText_ship_date.getText().toString().trim();

            mSessionManager.putStringData(SessionManager.KEY_SHIP_DATE,editText_ship_date.getText().toString().trim());
            mSessionManager.putStringData(SessionManager.KEY_CARRIER_CODE,strCarrierCode);
            mSessionManager.putStringData(SessionManager.KEY_SERVICE_TYPE,strServiceType);
            mSessionManager.putStringData(SessionManager.KEY_SERVICE_TYPE_NAME,strServiceTypeDesc);

            mSessionManager.putStringData(SessionManager.KEY_PICKUP_DROP,strDropoffType);
            mSessionManager.putStringData(SessionManager.KEY_PICKUP_DROP_NAME,strDropOffDescription);

            mSessionManager.putStringData(SessionManager.KEY_PACKAGE_TYPE,strPackageType);
            mSessionManager.putStringData(SessionManager.KEY_PACKAGE_TYPE_NAME,strPackageName);

            mSessionManager.putStringData(SessionManager.KEY_PACKAGE_COUNT,strTextViewNo);
            if(cb_identical.isChecked()){
                mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,true);
            }else {
                mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,false);
            }

            getCheckRatesDetails();
        }


    }

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void getCheckRatesDetails(){
        try {
            progressBar = new ProgressDialog(ShipmentDetailsCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_check_rates);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    JSONArray jsonArray = Json_response.getJSONArray("rates");
                                    //JSONArray jsonArray = (JSONArray) Json_response.get("rates");
                                    Log.e("Hi","->"+jsonArray.length());

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
                                                                    jsonObjectRate.getString("cs_surcharge_title")
                                        ));
                                    }
                                        progressBar.dismiss();
                                    startActivity(new Intent(ShipmentDetailsCustomerActivity.this,CheckRateScreenActivity.class).putParcelableArrayListExtra("RateList",ratesArrayList));
                                    ratesArrayList.clear();

                                }else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                                    Snackbar.make(spinner_carrier,jsonObject.getString(JSONConstant.MESSAGE),Snackbar.LENGTH_SHORT).show();
                                }
                                progressBar.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBar.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            progressBar.dismiss();}
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(JSONConstant.FROM_C_COUNTRY_CODE,AppConstant.F_C_COUNTRY_CODE);
                    params.put(JSONConstant.FROM_C_ZIPCODE,AppConstant.F_C_ZIPCODE);
                    params.put(JSONConstant.TO_C_COUNTRY_CODE,AppConstant.T_C_COUNTRY_CODE);
                    params.put(JSONConstant.TO_C_ZIPCODE,AppConstant.T_C_ZIPCODE);
                    params.put(JSONConstant.PACKAGECOUNT,strTextViewNo);
                    params.put(JSONConstant.SERVICESHIPDATE,editText_ship_date.getText().toString());
                    params.put(JSONConstant.PACKAGING_TYPE,strPackageType);

                    if (strCarrierCode != null &&!strCarrierCode.equalsIgnoreCase("UPS")){
                        params.put(JSONConstant.SERVICE_TYPE,strServiceType);
                    }

                    params.put(JSONConstant.DROP_OFF_TYPE,strDropoffType);
                    params.put(JSONConstant.PACKAGING_TYPE_DESCRIPTION,strPackageName);
                    params.put(JSONConstant.DROPOFF_TYPE_DESCRIPTION,strDropOffDescription);

                    if(cb_identical.isChecked()){
                        params.put(JSONConstant.IS_IDENTICAL,"1");
                    }else {
                        params.put(JSONConstant.IS_IDENTICAL,"0");
                    }
                    //
                    //params.put("cr_carrier_code","FDXE");
                    params.put(JSONConstant.CR_CARRIER_CODE,strCarrierCode);

                    AppConstant.noOfPackages.addAll(dataMapOfPackages);
                    //AppConstant.noOfPackages.removeAll(dataMapOfPackages);

                    try {
                        List<JSONObject> jsonObj = new ArrayList<JSONObject>();

                        for(HashMap<String, String> data : dataMapOfPackages) {
                            JSONObject obj = new JSONObject(data);
                            jsonObj.add(obj);
                        }

                        JSONArray test = new JSONArray(jsonObj);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Packages",test);

                        System.out.println(jsonObject.toString());
                        params.put(JSONConstant.PACKAGES,test.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    params.put(JSONConstant.FROM_C_STATE,"");
                    params.put(JSONConstant.FROM_C_ADDRESS_LINE1,"");
                    params.put(JSONConstant.FROM_C_CITY,"");
                    params.put(JSONConstant.TO_C_STATE,"");
                    params.put(JSONConstant.TO_C_ADDRESS_LINE1,"");
                    params.put(JSONConstant.TO_C_CITY,"");

                    System.out.println("hi harshada"+params.toString());

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    // TODO Auto-generated method stub
                    return "application/x-www-form-urlencoded";
                }

                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response.headers == null)
                    {
                        // cant just set a new empty map because the member is final.
                        response = new NetworkResponse(
                                response.statusCode,
                                response.data,
                                Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                response.notModified,
                                response.networkTimeMs);


                    }

                    return super.parseNetworkResponse(response);
                }
            };

            // Adding JsonObject request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showProgressDialog(){
        progressBar = new ProgressDialog(ShipmentDetailsCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
    }

    public void getCarriers(){
        try {
           /* progressBar = new ProgressDialog(ShipmentDetailsCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();*/

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_fetch_carriers);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                               if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                   JSONArray jsonArray = Json_response.getJSONArray("carriers");

                                   for(int i = 0; i<jsonArray.length();i++) {
                                       JSONObject jsonObjectCarrier = jsonArray.getJSONObject(i);
                                       Carriers carriers = new Carriers();
                                       carriers.setCr_code(jsonObjectCarrier.getString("cr_carrier_code"));
                                       carriers.setCr_name(jsonObjectCarrier.getString("cr_carrier_name"));
                                       arrayListCarriers.add(carriers);
                                       carrierList.add(jsonObjectCarrier.getString("cr_carrier_name"));
                                   }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsCustomerActivity.this,
                                        android.R.layout.simple_spinner_item, carrierList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_carrier.setAdapter(dataAdapter);

                                progressBar.dismiss();

                                if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
                                    for (int i = 0;i<arrayListCarriers.size();i++) {
                                        System.out.println(arrayListCarriers.get(i).getCr_code()+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
                                        if (arrayListCarriers.get(i).getCr_code().equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE))) {
                                            spinner_carrier.setItemOnPosition(i);

                                        }
                                    }
                                }




                                //getServiceTypes();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBar.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            progressBar.dismiss();
                        }
                    }) {

                @Override
                public String getBodyContentType() {
                    // TODO Auto-generated method stub
                    return "application/x-www-form-urlencoded";
                }
            };

            // Adding JsonObject request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getServiceTypes(){
        try {
           progressBar = new ProgressDialog(ShipmentDetailsCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_fetch_service_types);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {

                                JSONObject Json_response = new JSONObject(response);
                                //seviceTypeList.add(0,"Select Service Types");
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                   JSONArray jsonArray = Json_response.getJSONArray("service_types");

                                    for(int i = 0; i<jsonArray.length();i++) {
                                        JSONObject jsonObjectService = jsonArray.getJSONObject(i);

                                        seviceTypeList.add(jsonObjectService.getString("ServiceDescription"));
                                        ServiceTypes serviceTypes = new ServiceTypes();
                                        serviceTypes.setServiceType(jsonObjectService.getString("Service"));
                                        serviceTypes.setServiceText(jsonObjectService.getString("ServiceDescription"));
                                        arrayListServiceTypes.add(serviceTypes);
                                    }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsCustomerActivity.this,
                                        android.R.layout.simple_spinner_item, seviceTypeList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_service_type.setAdapter(dataAdapter);

                               // progressBar.dismiss();

                                dropOffList = new ArrayList<>();
                                arrayListDropOffs = new ArrayList<>();
                                packageTypeList = new ArrayList<>();
                                packageNameList = new ArrayList<>();

                                if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE));
                                    for (int i = 0;i<arrayListServiceTypes.size();i++) {
                                        System.out.println(arrayListServiceTypes.get(i).getServiceType()+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE));
                                        if (arrayListServiceTypes.get(i).getServiceType().equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE))) {
                                            spinner_service_type.setItemOnPosition(i);

                                        }
                                    }
                                }


                                getDropOff();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //progressBar.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            progressBar.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    params.put("OriginCountryCode",AppConstant.F_C_COUNTRY_CODE);
                    params.put("OriginPostalCode",AppConstant.F_C_ZIPCODE);
                    params.put("DestinationCountryCode",AppConstant.T_C_COUNTRY_CODE);
                    params.put("DestinationPostalCode",AppConstant.T_C_ZIPCODE);

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    // TODO Auto-generated method stub
                    return "application/x-www-form-urlencoded";
                }
            };

            // Adding JsonObject request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getDropOff(){
        try {
         /*   progressBar = new ProgressDialog(ShipmentDetailsCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();*/

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_drop_off_type);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                //JSONObject Json_response = new JSONObject(response);
                                JSONArray jsonArray = new JSONArray(response);
                               // seviceTypeList.add(0,"Select Drop Off");
                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    dropOffList.add(jsonObject.getString("drop_off_text"));
                                    DropOff dropOff = new DropOff();
                                    dropOff.setDropOffText(jsonObject.getString("drop_off_text"));
                                    dropOff.setDropOffType(jsonObject.getString("drop_off_type"));
                                    arrayListDropOffs.add(dropOff);

                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsCustomerActivity.this,
                                        android.R.layout.simple_spinner_item, dropOffList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_pickup_drop.setAdapter(dataAdapter);

                                if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));
                                    for (int i = 0;i<arrayListDropOffs.size();i++) {
                                        System.out.println(arrayListDropOffs.get(i).getDropOffType()+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));
                                        if (arrayListDropOffs.get(i).getDropOffType().equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP))) {
                                            spinner_pickup_drop.setItemOnPosition(i);

                                        }
                                    }
                                }


                                //Log.e("harshada",strCarrierCode+"="+carrierCode);
                                //progressBar.dismiss();

                                getPackageType();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //progressBar.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            progressBar.dismiss();}
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    // TODO Auto-generated method stub
                    return "application/x-www-form-urlencoded";
                }
            };

            // Adding JsonObject request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getPackageType(){
        try {
          /*  progressBar = new ProgressDialog(ShipmentDetailsCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();*/



            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_fetch_package_types);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    JSONArray jsonArray = Json_response.getJSONArray("package_types");

                                    for(int i = 0; i<jsonArray.length();i++) {
                                        JSONObject jsonObjectService = jsonArray.getJSONObject(i);
                                        packageTypeList.add(jsonObjectService.getString("fpt_package_type"));
                                        packageNameList.add(jsonObjectService.getString("fpt_package_name"));
                                    }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsCustomerActivity.this,
                                        android.R.layout.simple_spinner_item, packageNameList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_package_type.setAdapter(dataAdapter);

                                if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));
                                    for (int i = 0;i<packageTypeList.size();i++) {
                                        System.out.println(packageTypeList.get(i)+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));
                                        if (packageTypeList.get(i).equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE))) {
                                            spinner_package_type.setItemOnPosition(i);

                                        }
                                    }
                                }

                                progressBar.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBar.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            progressBar.dismiss();}
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    // TODO Auto-generated method stub
                    return "application/x-www-form-urlencoded";
                }
            };

            // Adding JsonObject request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Custom Dialog
    private void ShowCustomDialog(final String titleShow,final TextView textView, final HashMap hashMap){
        Button btn_save,btn_cancel;
        final TextView tv_title,tv_weight_unit,tv_package_dimension,tv_currency_unit;
        final  EditText editText_weight,editText_declared_value,editText_length,editText_width,editText_height,editText_package_description;
        final TextInputLayout textInput_weight,textInput_declared_value,textInput_length,textInput_width,textInput_height;
        final LinearLayout ll_package_dimension;
        final Spinner spinner_unit,spinner_currency;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShipmentDetailsCustomerActivity.this);
        LayoutInflater inflater = ShipmentDetailsCustomerActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_for_package, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        Window window = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        ll_package_dimension = (LinearLayout) dialogView.findViewById(R.id.ll_package_dimension);

        btn_save = (Button) dialogView. findViewById(R.id.btn_save);

        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        tv_title.setText(titleShow);

        tv_package_dimension = (TextView)dialogView.findViewById(R.id.tv_package_dimension);

        editText_weight = (EditText)dialogView. findViewById(R.id.editText_weight);
        editText_declared_value = (EditText)dialogView. findViewById(R.id.editText_declared_value);

        editText_length = (EditText)dialogView. findViewById(R.id.editText_length);
        editText_width = (EditText)dialogView. findViewById(R.id.editText_width);
        editText_height = (EditText)dialogView. findViewById(R.id.editText_height);
        editText_package_description = (EditText)dialogView. findViewById(R.id.editText_package_description);

        tv_weight_unit = (TextView)dialogView. findViewById(R.id.tv_weight_unit);
        tv_currency_unit = (TextView) dialogView.findViewById(R.id.tv_currency_unit);

        textInput_weight = (TextInputLayout)dialogView. findViewById(R.id.textInput_weight);
        textInput_declared_value = (TextInputLayout)dialogView. findViewById(R.id.textInput_declared_value);
        textInput_length = (TextInputLayout)dialogView. findViewById(R.id.textInput_length);
        textInput_height = (TextInputLayout)dialogView. findViewById(R.id.textInput_height);
        textInput_width = (TextInputLayout)dialogView. findViewById(R.id.textInput_width);

        spinner_unit = (Spinner)dialogView. findViewById(R.id.spinner_unit);
        spinner_currency = (Spinner)dialogView. findViewById(R.id.spinner_currency);


        String[] unit=getResources().getStringArray(R.array.array_unit);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsCustomerActivity.this,
                android.R.layout.simple_spinner_item, unit);
        // set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_unit.setAdapter(dataAdapter);

        String[] currency_unit=getResources().getStringArray(R.array.array_currency_unit);

        ArrayAdapter<String> currencyUnitAdapter = new ArrayAdapter<String>(ShipmentDetailsCustomerActivity.this,
                android.R.layout.simple_spinner_item, currency_unit);
        // set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_currency.setAdapter(currencyUnitAdapter);


        if(tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
            spinner_currency.setVisibility(View.VISIBLE);
            tv_currency_unit.setVisibility(View.GONE);
        }else {
            spinner_currency.setVisibility(View.GONE);
            tv_currency_unit.setVisibility(View.VISIBLE);
        }


        if(tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))&&strCarrierCode != null &&!strCarrierCode.equalsIgnoreCase("UPS")){
            spinner_unit.setVisibility(View.VISIBLE);
            tv_weight_unit.setVisibility(View.GONE);
        }else {
            spinner_unit.setVisibility(View.GONE);
            tv_weight_unit.setVisibility(View.VISIBLE);
        }




        /*if(dataMapOfPackages.size()>1){
            spinner_unit.setVisibility(View.GONE);
            tv_weight_unit.setVisibility(View.VISIBLE);
         *//*   spinner_unit.setClickable(false);
            spinner_unit.setFocusable(false);
            spinner_unit.setEnabled(false);
            spinner_unit.setto*//*
        }*/

      spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                    strWeightUnit="LB";
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                    strDimensionalUnit = "IN";
                }else {
                    tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_cms));
                    strWeightUnit="KG";
                    strDimensionalUnit = "CM";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_usd));
                    strCurrencyUnit="USD";
                }else if(position==1){
                    tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_inr));
                    strCurrencyUnit="INR";
                }else {
                    tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_cad));
                    strCurrencyUnit="CAD";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(strWeightUnit.equalsIgnoreCase("KG")){
            spinner_unit.setSelection(1);
            if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                tv_weight_unit.setVisibility(View.VISIBLE);
                tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
            }
            strWeightUnit = "KG";
            tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_cms));
        }else {
            spinner_unit.setSelection(0);
            if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                tv_weight_unit.setVisibility(View.VISIBLE);
                tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
            }
            //tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
            strWeightUnit = "LB";
            tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
        }


        if(strCurrencyUnit.equalsIgnoreCase("USD")){
            spinner_currency.setSelection(0);
            if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                tv_currency_unit.setVisibility(View.VISIBLE);
                tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_usd));
            }
            strCurrencyUnit = "USD";
        }else  if(strCurrencyUnit.equalsIgnoreCase("INR")){
            spinner_currency.setSelection(1);
            if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                tv_currency_unit.setVisibility(View.VISIBLE);
                tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_inr));
            }
            strCurrencyUnit = "INR";
        } else {
            spinner_currency.setSelection(2);
            if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                tv_currency_unit.setVisibility(View.VISIBLE);
                tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_cad));
            }
            strCurrencyUnit = "CAD";
        }


        if((strPackageType != null && strPackageType.equalsIgnoreCase("YOUR_PACKAGING"))||(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("UPS"))) {
            ll_package_dimension.setVisibility(View.VISIBLE);
            tv_package_dimension.setVisibility(View.VISIBLE);
        }else {
            ll_package_dimension.setVisibility(View.GONE);
            tv_package_dimension.setVisibility(View.GONE);
        }

        if(!hashMap.isEmpty()){

            if(strPackageType != null &&strPackageType.equalsIgnoreCase("YOUR_PACKAGING")||(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("UPS"))) {
                tv_title.setText(titleShow);
                editText_weight.setText(hashMap.get(AppConstant.HM_WEIGHT).toString());
                editText_weight.setSelection(hashMap.get(AppConstant.HM_WEIGHT).toString().length());
                tv_weight_unit.setText(hashMap.get(AppConstant.HM_WEIGHT_UNIT).toString());
                tv_currency_unit.setText(hashMap.get(AppConstant.HM_CURRENCY_UNIT).toString());
                editText_declared_value.setText(hashMap.get(AppConstant.HM_DECLARED_VALUE).toString());
                editText_length.setText(hashMap.get(AppConstant.HM_LENGTH).toString());
                editText_width.setText(hashMap.get(AppConstant.HM_WIDTH).toString());
                editText_height.setText(hashMap.get(AppConstant.HM_HEIGHT).toString());
                editText_package_description.setText(hashMap.get(AppConstant.HM_PACKAGE_DESC).toString());
                if(hashMap.get(AppConstant.HM_WEIGHT_UNIT).toString().equalsIgnoreCase(getResources().getString(R.string.lbl_weight_unit_kg))){
                   spinner_unit.setSelection(1);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_weight_unit.setVisibility(View.VISIBLE);
                        tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
                    }
                    strWeightUnit = "KG";
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_cms));
                }else {
                    spinner_unit.setSelection(0);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_weight_unit.setVisibility(View.VISIBLE);
                        tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                    }
                    //tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                    strWeightUnit = "LB";
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                }

                if(hashMap.get(AppConstant.HM_CURRENCY_UNIT).toString().equalsIgnoreCase(getResources().getString(R.string.lbl_currency_unit_usd))){
                    spinner_currency.setSelection(0);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_currency_unit.setVisibility(View.VISIBLE);
                        tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_usd));
                    }
                    strCurrencyUnit = "USD";
                }else  if(hashMap.get(AppConstant.HM_CURRENCY_UNIT).toString().equalsIgnoreCase(getResources().getString(R.string.lbl_currency_unit_inr))){
                    spinner_currency.setSelection(1);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_currency_unit.setVisibility(View.VISIBLE);
                        tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_inr));
                    }
                    strCurrencyUnit = "INR";
                } else {
                    spinner_currency.setSelection(2);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_currency_unit.setVisibility(View.VISIBLE);
                        tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_cad));
                    }
                    strCurrencyUnit = "CAD";
                }

            }else {
                tv_title.setText(titleShow);
                editText_weight.setText(hashMap.get(AppConstant.HM_WEIGHT).toString());
                editText_weight.setSelection(hashMap.get(AppConstant.HM_WEIGHT).toString().length());
                tv_weight_unit.setText(hashMap.get(AppConstant.HM_WEIGHT_UNIT).toString());
                editText_declared_value.setText(hashMap.get(AppConstant.HM_DECLARED_VALUE).toString());
                editText_package_description.setText(hashMap.get(AppConstant.HM_PACKAGE_DESC).toString());

                if(hashMap.get(AppConstant.HM_WEIGHT_UNIT).toString().equalsIgnoreCase(getResources().getString(R.string.lbl_weight_unit_kg))){
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_weight_unit.setVisibility(View.VISIBLE);
                        tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
                    }else {
                        spinner_unit.setSelection(1);
                        tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
                        tv_weight_unit.setVisibility(View.GONE);
                    }
                    strWeightUnit = "KG";
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_cms));
                }else {
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_weight_unit.setVisibility(View.VISIBLE);
                        tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                    }else {
                        spinner_unit.setSelection(0);
                        tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                        tv_weight_unit.setVisibility(View.GONE);

                    }
                    //tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                    strWeightUnit = "LB";
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                }


                if(hashMap.get(AppConstant.HM_CURRENCY_UNIT).toString().equalsIgnoreCase(getResources().getString(R.string.lbl_currency_unit_usd))){
                    spinner_currency.setSelection(0);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_currency_unit.setVisibility(View.VISIBLE);
                        tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_usd));
                    }
                    strCurrencyUnit = "USD";
                }else  if(hashMap.get(AppConstant.HM_CURRENCY_UNIT).toString().equalsIgnoreCase(getResources().getString(R.string.lbl_currency_unit_inr))){
                    spinner_currency.setSelection(1);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_currency_unit.setVisibility(View.VISIBLE);
                        tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_inr));
                    }
                    strCurrencyUnit = "INR";
                } else {
                    spinner_currency.setSelection(2);
                    if(!tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                        tv_currency_unit.setVisibility(View.VISIBLE);
                        tv_currency_unit.setText(getResources().getString(R.string.lbl_currency_unit_cad));
                    }
                    strCurrencyUnit = "CAD";
                }

            }

        }


        btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(strPackageType != null &&strPackageType.equalsIgnoreCase("YOUR_PACKAGING")||(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("UPS"))) {
                    if(editText_weight.getText().toString().isEmpty()){
                        textInput_weight.setError(getResources().getString(R.string.err_msg_weight));
                        requestFocus(editText_weight);
                    }else if(editText_declared_value.getText().toString().isEmpty()){
                        textInput_weight.setErrorEnabled(false);
                        textInput_declared_value.setError(getResources().getString(R.string.err_msg_declared_value));
                        requestFocus(editText_declared_value);
                    }else if(editText_length.getText().toString().isEmpty()){
                        textInput_declared_value.setErrorEnabled(false);
                        textInput_length.setError(getResources().getString(R.string.err_msg_length));
                        requestFocus(editText_length);
                    }else if(editText_width.getText().toString().isEmpty()){
                        textInput_length.setErrorEnabled(false);
                        textInput_width.setError(getResources().getString(R.string.err_msg_width));
                        requestFocus(editText_width);
                    }else if(editText_height.getText().toString().isEmpty()){
                        textInput_width.setErrorEnabled(false);
                        editText_height.setError(getResources().getString(R.string.err_msg_height));
                        requestFocus(editText_weight);
                    }else {
                        textInput_declared_value.setErrorEnabled(false);
                        textInput_weight.setErrorEnabled(false);
                        textInput_length.setErrorEnabled(false);
                        textInput_width.setErrorEnabled(false);
                        textInput_height.setErrorEnabled(false);


                        if(spinner_unit.getSelectedItemPosition()==0){
                            tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                            tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                            strWeightUnit="LB";
                            tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                            strDimensionalUnit = "IN";
                        }else {
                            tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_cms));
                            tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
                            strWeightUnit="KG";
                            strDimensionalUnit = "CM";
                        }

                        hashMap.put(AppConstant.HM_PACKAGE_NO,titleShow);
                        hashMap.put(AppConstant.HM_WEIGHT,editText_weight.getText().toString().trim());
                        hashMap.put(AppConstant.HM_CURRENCY_UNIT,strCurrencyUnit);
                        hashMap.put(AppConstant.HM_WEIGHT_UNIT,strWeightUnit);
                        hashMap.put(AppConstant.HM_DECLARED_VALUE,editText_declared_value.getText().toString().trim());
                        hashMap.put(AppConstant.HM_LENGTH,editText_length.getText().toString().trim());
                        hashMap.put(AppConstant.HM_WIDTH,editText_width.getText().toString().trim());
                        hashMap.put(AppConstant.HM_HEIGHT,editText_height.getText().toString().trim());
                        hashMap.put(AppConstant.HM_PACKAGE_DESC,editText_package_description.getText().toString().trim());
                       //11 dec added below line
                        hashMap.put(AppConstant.HM_DIMENSIONAL_UNIT, strDimensionalUnit);
                        alertDialog.hide();

                        hideKeyboard();
                        showPackageDetails(textView,hashMap);
                    }
                } else {
                     if (editText_weight.getText().toString().isEmpty()) {
                        textInput_weight.setError(getResources().getString(R.string.err_msg_weight));
                        requestFocus(editText_weight);
                    } else if (editText_declared_value.getText().toString().isEmpty()) {
                        textInput_weight.setErrorEnabled(false);
                        textInput_declared_value.setError(getResources().getString(R.string.err_msg_declared_value));
                        requestFocus(editText_declared_value);
                    } else {
                        textInput_declared_value.setErrorEnabled(false);
                        textInput_weight.setErrorEnabled(false);

                         if(spinner_unit.getSelectedItemPosition()==0){
                             tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                             tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                             strWeightUnit="LB";
                             tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                             strDimensionalUnit = "IN";
                         }else {
                             tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_cms));
                             tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_kg));
                             strWeightUnit="KG";
                             strDimensionalUnit = "CM";
                         }

                        hashMap.put(AppConstant.HM_PACKAGE_NO, titleShow);
                        hashMap.put(AppConstant.HM_WEIGHT, editText_weight.getText().toString().trim());
                        hashMap.put(AppConstant.HM_WEIGHT_UNIT, strWeightUnit);
                        hashMap.put(AppConstant.HM_CURRENCY_UNIT,strCurrencyUnit);
                        hashMap.put(AppConstant.HM_DECLARED_VALUE, editText_declared_value.getText().toString().trim());
                        hashMap.put(AppConstant.HM_PACKAGE_DESC, editText_package_description.getText().toString().trim());
                        //11Dec  hashMap.put(AppConstant.HM_DIMENSIONAL_UNIT, "IN");
                        hashMap.put(AppConstant.HM_DIMENSIONAL_UNIT, strDimensionalUnit);
                        hashMap.put(AppConstant.HM_LENGTH, "");
                        hashMap.put(AppConstant.HM_WIDTH, "");
                        hashMap.put(AppConstant.HM_HEIGHT, "");

                        alertDialog.hide();

                        hideKeyboard();
                        showPackageDetails(textView, hashMap);
                    }
                }

                if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                    hashMapPackage1.putAll(hashMap);
                    AppConstant.hashMapPackage1.putAll(hashMap);
                    if(dataMapOfPackages.contains(hashMapPackage1)){
                        dataMapOfPackages.set(0,hashMapPackage1);
                    }else {
                        dataMapOfPackages.add(0,hashMapPackage1);
                    }
                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_2))){
                    hashMapPackage2.putAll(hashMap);
                    AppConstant.hashMapPackage2.putAll(hashMap);

                    if(dataMapOfPackages.contains(hashMapPackage2)){
                        dataMapOfPackages.set(1,hashMapPackage2);
                    }else {
                        dataMapOfPackages.add(1,hashMapPackage2);
                    }

                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_3))){
                    hashMapPackage3.putAll(hashMap);
                    AppConstant.hashMapPackage3.putAll(hashMap);

                    if(dataMapOfPackages.contains(hashMapPackage3)){
                        dataMapOfPackages.set(2,hashMapPackage3);
                    }else {
                        dataMapOfPackages.add(2,hashMapPackage3);
                    }

                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_4))){
                    hashMapPackage4.putAll(hashMap);
                    AppConstant.hashMapPackage4.putAll(hashMap);

                    if(dataMapOfPackages.contains(hashMapPackage4)){
                        dataMapOfPackages.set(3,hashMapPackage4);
                    }else {
                        dataMapOfPackages.add(3,hashMapPackage4);
                    }

                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_5))){
                    hashMapPackage5.putAll(hashMap);
                    AppConstant.hashMapPackage5.putAll(hashMap);

                    if(dataMapOfPackages.contains(hashMapPackage5)){
                        dataMapOfPackages.set(4,hashMapPackage5);
                    }else {
                        dataMapOfPackages.add(4,hashMapPackage5);
                    }
                }
            }
        });

        alertDialog.show();
    }

    private void showPackageDetails(TextView textView,HashMap hashMap){
        if(strPackageType != null &&strPackageType.equalsIgnoreCase("YOUR_PACKAGING")||(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("UPS"))){
            textView.setText("Weight is "+hashMap.get(AppConstant.HM_WEIGHT)+" "+hashMap.get(AppConstant.HM_WEIGHT_UNIT)+" & Declared Value (Per package) is "+hashMap.get(AppConstant.HM_CURRENCY_UNIT)+" "+hashMap.get(AppConstant.HM_DECLARED_VALUE));
        }else {
            textView.setText("Weight is "+hashMap.get(AppConstant.HM_WEIGHT)+" "+hashMap.get(AppConstant.HM_WEIGHT_UNIT)+" & Declared Value (Per package) is "+hashMap.get(AppConstant.HM_CURRENCY_UNIT)+" "+hashMap.get(AppConstant.HM_DECLARED_VALUE));
        }
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("position","position -"+position + " id -"+id);
        switch (parent.getId()){

            case R.id.spinner_carrier:
                strCarrierCode = arrayListCarriers.get(position).getCr_code();
                if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")){
                    spinner_service_type.setVisibility(View.GONE);
                    tv_service_type.setVisibility(View.GONE);

                    dropOffList = new ArrayList<>();
                    arrayListDropOffs = new ArrayList<>();
                    arrayListServiceTypes = new ArrayList<>();
                    packageTypeList = new ArrayList<>();
                    packageNameList = new ArrayList<>();
                    showProgressDialog();
                    getDropOff();

                }else {
                    spinner_service_type.setVisibility(View.VISIBLE);
                    tv_service_type.setVisibility(View.VISIBLE);

                    dropOffList = new ArrayList<>();
                    arrayListDropOffs = new ArrayList<>();
                    arrayListServiceTypes = new ArrayList<>();
                    packageTypeList = new ArrayList<>();
                    packageNameList = new ArrayList<>();

                    getServiceTypes();
                }

                break;

            case R.id.spinner_service_type:
                //11Dec strServiceType = arrayListServiceTypes.get(position).getServiceType();
                //Toast.makeText(ShipmentDetailsCustomerActivity.this,spinner_carrier.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();
                strServiceType = arrayListServiceTypes.get(position).getServiceType();
                strServiceTypeDesc = arrayListServiceTypes.get(position).getServiceText();

                     /* if(position==0){
                     strServiceType="";In Parcel details popup can you add a currency symbol based on source country. Say USA - $ , India - INR.
                      }else {
                     strServiceType = arrayListServiceTypes.get(position-1).getServiceType();
                    }*/
                break;

            case R.id.spinner_pickup_drop:
                strDropoffType = arrayListDropOffs.get(position).getDropOffType();
                strDropOffDescription = arrayListDropOffs.get(position).getDropOffText();
                break;

            case R.id.spinner_package_type:
                //strPackageType = spinner_package_type.getItemAtPosition(position).toString();
                //strPackageType = packageTypeList.get(position).toLowerCase();

                strPackageType = packageTypeList.get(position);
                strPackageName = packageNameList.get(position);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

package com.example.aspl.fortunecourier.activity.associate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;

import com.example.aspl.fortunecourier.activity.customer.ShipmentDetailsCustomerActivity;
import com.example.aspl.fortunecourier.customspinner.SearchableSpinner;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Carriers;
import com.example.aspl.fortunecourier.model.DropOff;
import com.example.aspl.fortunecourier.model.PackageDetails;
import com.example.aspl.fortunecourier.model.Rates;
import com.example.aspl.fortunecourier.model.ServiceTypes;
import com.example.aspl.fortunecourier.model.ShipmentPurpose;
import com.example.aspl.fortunecourier.model.SpecialService;
import com.example.aspl.fortunecourier.model.SpecialServicesOptions;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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

public class ShipmentDetailsAssociateActivity extends Activity implements DatePickerDialog.OnDateSetListener, View.OnClickListener,AdapterView.OnItemSelectedListener{

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    private TextInputLayout textInput_ship_date,textInput_customs_value,textInput_no_of_packages;//,textInput_customs_value,textInput_customs_description;
    private EditText editText_ship_date,editText_customs_value,editText_no_of_packages;//,editText_customs_value,editText_customs_description;
    static List<String> arrayListOfPackages = new ArrayList<>();
    private Button btn_get_quick_quote;
    private LayoutInflater layoutInflater;
    private TextInputLayout textInput_weight1, textInput_weight2, textInput_weight3, textInput_weight4, textInput_weight5;
    private TextInputLayout textInput_declared_value1, textInput_declared_value2, textInput_declared_value3, textInput_declared_value4, textInput_declared_value5;
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

    private SearchableSpinner spinner_carrier,spinner_service_type,spinner_pickup_drop,spinner_package_type,spinner_container_type,spinner_sizes;

    private TextView tv_select_carrier,tv_service_type,tv_container_type,tv_sizes,tv_pickup_drop,tv_no_of_packages,tv_get_detailed_quote;//,tv_weight_unit;
    private CheckBox checkbox_sizes,checkbox_shapes;
    private LinearLayout ll_no_of_packages,ll_sizes,ll_shapes;
    private ArrayList<Carriers>  arrayListCarriers;
    private ArrayList<DropOff> arrayListDropOffs;
    private ArrayList<ServiceTypes> arrayListServiceTypes;
    private ArrayList<String> carrierList;
    private ArrayList<String> seviceTypeList;
    private ArrayList<String> dropOffList;
    private ArrayList<String> packageTypeList;
    private ArrayList<String> packageNameList;
    private ArrayList<String> containerTypeList;
    private ArrayList<String> sizesList;
    ArrayList<HashMap<String, String>> dataMapOfPackages;
    private ArrayList<ShipmentPurpose> arrayListOfShipmentPurposes;
    private ArrayList<String> shipmentPurposesList;

    ArrayList<Rates> ratesArrayList;
    String strPackageType;
    String strPackageName;
    String strCarrierCode="";
    String strDropoffType="";
    String strDropOffDescription="";
    String strServiceType="";
    String strServiceTypeDesc="";
    String strShape="",strShipmentPurpose="",strPackageContents="";
    String strSize="REGULAR";

    private ImageView img_info,img_info_special_sevices;
    String strDimensionalUnit = "IN";
    String strWeightUnit ="LB";
    String strCurrencyUnit = "USD";
    private SessionManager mSessionManager;
    private AlertDialog alertDialog;

    List<View> allViewInstance = new ArrayList<View>();
    JSONObject jsonObject = new JSONObject();
    private JSONObject optionsObj;
    private ArrayList<String> arrayListFalse;
    private ArrayList<String> arrayListTrue;
    RadioGroup rg;
    RadioButton rb;
    LinearLayout.LayoutParams params;
    String strCheckbox ;

    private ArrayList<String> arrayListCheckedServices;
    private ArrayList<SpecialService> arrayListSpecialServices;
    private HashMap<String,String> hashMapForSpecialServices;
    private ArrayList<HashMap<String,String>> dataMapOfSpecialServices;

    private String strSelectedRadioButton;
    private HashMap<String,Boolean>  hashMapForSavingState = new HashMap<>();
    private String strSpecialServicesResponse="";
    private ArrayList<String> documentsList;

    private LinearLayout ll_package_contents,ll_document_desc,ll_shipment_purpose,ll_customs_details;
    private RadioGroup rg_package_contents;
    private RadioButton rb_products_commodities;
    private SearchableSpinner spinner_document_desc,spinner_shipment_purpose,spinner_customs_currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_details);
        init();
    }

    public void init() {
        cd = new ConnectionDetector(this);
        mSessionManager = new SessionManager(this);

        dataMapOfPackages = new ArrayList<HashMap<String, String>>();
        dataMapOfSpecialServices = new ArrayList<>();
        arrayListSpecialServices = new ArrayList<>();
        arrayListCheckedServices = new ArrayList<>();

        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        img_info_special_sevices = (ImageView) findViewById(R.id.img_info_special_sevices);
        img_info_special_sevices.setOnClickListener(this);

        btn_get_quick_quote = (Button) findViewById(R.id.btn_get_quick_quote);
        btn_get_quick_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                // showDesclaimer(ShipmentDetailsAssociateActivity.this);
                if(cd.isConnectingToInternet()){
                    validateAndNext();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
            }
        });

        tv_get_detailed_quote = (TextView) findViewById(R.id.tv_get_detailed_quote);
        tv_get_detailed_quote.setOnClickListener(this);

        String udata=getResources().getString(R.string.btn_special_services);
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        tv_get_detailed_quote.setText(content);

        editText_ship_date = (EditText) findViewById(R.id.editText_ship_date);
        editText_ship_date.setOnClickListener(this);

        editText_customs_value = (EditText) findViewById(R.id.editText_customs_value);

        ll_shapes = (LinearLayout) findViewById(R.id.ll_shapes);
        ll_sizes = (LinearLayout) findViewById(R.id.ll_sizes);

        ll_package_contents = (LinearLayout) findViewById(R.id.ll_package_contents);
        ll_document_desc = (LinearLayout) findViewById(R.id.ll_document_desc);
        ll_shipment_purpose = (LinearLayout) findViewById(R.id.ll_shipment_purpose);
        ll_customs_details = (LinearLayout) findViewById(R.id.ll_customs_details);

        rb_products_commodities = (RadioButton) findViewById(R.id.rb_products_commodities);

        rg_package_contents = (RadioGroup) findViewById(R.id.rg_package_contents);
        rg_package_contents.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // int selectedId=rg_package_contents.getCheckedRadioButtonId();
                //Toast.makeText(ShipmentDetailsAssociateActivity.this,"=>"+selectedId,Toast.LENGTH_SHORT).show();
                if(rb_products_commodities.isChecked()){
                    ll_shipment_purpose.setVisibility(View.VISIBLE);
                    ll_customs_details.setVisibility(View.VISIBLE);
                    ll_document_desc.setVisibility(View.GONE);
                    strPackageContents="NON_DOCUMENTS";
                }else {
                    ll_document_desc.setVisibility(View.VISIBLE);
                    ll_shipment_purpose.setVisibility(View.VISIBLE);
                    ll_customs_details.setVisibility(View.GONE);
                    strPackageContents="DOCUMENTS_ONLY";
                }

            }
        });

        spinner_document_desc = (SearchableSpinner) findViewById(R.id.spinner_document_desc);
        spinner_shipment_purpose = (SearchableSpinner) findViewById(R.id.spinner_shipment_purpose);
        spinner_customs_currency = (SearchableSpinner) findViewById(R.id.spinner_customs_currency);

        String[] unit=getResources().getStringArray(R.array.array_currency_unit);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this, android.R.layout.simple_spinner_item, unit);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_customs_currency.setAdapter(dataAdapter);

        spinner_customs_currency.setItemOnPosition(0);

        checkbox_sizes = (CheckBox) findViewById(R.id.checkbox_sizes);
        checkbox_shapes = (CheckBox) findViewById(R.id.checkbox_shapes);

        checkbox_sizes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_shapes.setVisibility(View.VISIBLE);
                    strSize = "LARGE";
                    strShape="RECTANGULAR";

                }else {
                    ll_shapes.setVisibility(View.GONE);
                    checkbox_shapes.setChecked(false);
                    strSize = "REGULAR";
                    strShape = "";
                }
            }
        });

        checkbox_shapes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    strShape = "NONRECTANGULAR";
                }else {
                    //ll_shapes.setVisibility(View.GONE);
                    strShape = "RECTANGULAR";
                }
            }
        });

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
        textInput_customs_value = (TextInputLayout) findViewById(R.id.textInput_customs_value);

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

        tv_no_of_packages = (TextView) findViewById(R.id.tv_no_of_packages);

        ll_no_of_packages = (LinearLayout) findViewById(R.id.ll_no_of_packages);

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

                    hashMapPackage2.clear();
                    hashMapPackage3.clear();
                    hashMapPackage4.clear();
                    hashMapPackage5.clear();

                    tv_package_details2.setText("");
                    tv_package_details3.setText("");
                    tv_package_details4.setText("");
                    tv_package_details5.setText("");

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
        tv_pickup_drop = (TextView) findViewById(R.id.tv_pickup_drop);
        tv_container_type = (TextView) findViewById(R.id.tv_container_type);
        tv_sizes = (TextView) findViewById(R.id.tv_sizes);

        if(AppConstant.IS_FROM_CREATE_SHIPMENT){
            tv_service_type.setHint(getResources().getString(R.string.hint_service_type));
        }else {
            tv_service_type.setHint(getResources().getString(R.string.lbl_service_type));

        }

        spinner_carrier = (SearchableSpinner) findViewById(R.id.spinner_carrier);
        spinner_carrier.setTitle(getResources().getString(R.string.lbl_select_carrier));
        spinner_carrier.setOnItemSelectedListener(this);

        spinner_service_type = (SearchableSpinner) findViewById(R.id.spinner_service_type);
        spinner_service_type.setTitle(getResources().getString(R.string.lbl_service_type));
        spinner_service_type.setOnItemSelectedListener(this);

        spinner_pickup_drop = (SearchableSpinner) findViewById(R.id.spinner_pickup_drop);
        spinner_pickup_drop.setTitle(getResources().getString(R.string.lbl_select_pickup_drop));
        spinner_pickup_drop.setOnItemSelectedListener(this);

        spinner_package_type = (SearchableSpinner) findViewById(R.id.spinner_package_type);
        spinner_package_type.setTitle(getResources().getString(R.string.lbl_select_package_type));
        spinner_package_type.setOnItemSelectedListener(this);

        spinner_container_type = (SearchableSpinner) findViewById(R.id.spinner_container_type);
        spinner_container_type.setTitle(getResources().getString(R.string.lbl_container_type));
        spinner_container_type.setOnItemSelectedListener(this);

        spinner_sizes = (SearchableSpinner) findViewById(R.id.spinner_sizes);
        spinner_sizes.setTitle(getResources().getString(R.string.lbl_sizes));
        spinner_sizes.setOnItemSelectedListener(this);

        if(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US")){
            tv_select_carrier.setVisibility(View.VISIBLE);
            spinner_carrier.setVisibility(View.VISIBLE);
            ll_package_contents.setVisibility(View.GONE);
            if(cd.isConnectingToInternet()){
                showProgressDialog();
                getCarriers();
            }else {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                customDialogForHelp.show();
            }
        }else {
            ll_package_contents.setVisibility(View.VISIBLE);
            if(cd.isConnectingToInternet()){
                strCarrierCode = "FedEX";
                getServiceTypes();
            }else {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                customDialogForHelp.show();
            }
        }

        tv_first.performClick();

        scrollView = (ScrollView)findViewById(R.id.scrollView);


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear+1;
        String date = year + "-" + month + "-" + dayOfMonth;
        editText_ship_date.setText(date);
    }

    private String getTextViewNumber(TextView textView){
        return  textView.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        CustomDialogForHelp customDialogForHelp;
        DBInfo dbInfo = new DBInfo(ShipmentDetailsAssociateActivity.this);

        switch (v.getId()) {
            case R.id.editText_ship_date:
                Calendar c = Calendar.getInstance();
                hideKeyboard();
                datePickerDialog = DatePickerDialog.newInstance(ShipmentDetailsAssociateActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#009688"));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.setMinDate(c);
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                break;

           /* case R.id.btn_get_quick_quote:
                hideKeyboard();

                if(cd.isConnectingToInternet()){
                    validateAndNext();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                   // Snackbar.make(btn_get_quick_quote,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;
*/
            case R.id.tv_get_detailed_quote:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    if(strCarrierCode.equalsIgnoreCase("")){
                        customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_carrier));
                        customDialogForHelp.show();
                    } else if(strCarrierCode.equalsIgnoreCase("UPS")&&spinner_service_type.getSelectedItemPosition()==-1){
                        customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_service_type));
                        customDialogForHelp.show();
                    }
                    else {
                        //getSpecialServices();
                        if(arrayListSpecialServices.size()>0){
                            showSpecialServices("Special Services",strSpecialServicesResponse);
                        }else {
                            getSpecialServices();
                        }
                    }
                }else {
                    customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;

            case R.id.img_info:
                hideKeyboard();
                customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,"Help",dbInfo.getDescription("Calculate Rates Screen"));
                customDialogForHelp.show();
                break;

            case R.id.img_info_special_sevices:
                hideKeyboard();
                customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,"Help",dbInfo.getDescription("Calculate Rates Special Services"));
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

                tv_package_details2.setText("");
                tv_package_details3.setText("");
                tv_package_details4.setText("");
                tv_package_details5.setText("");

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
        textView1.setBackgroundColor( ContextCompat.getColor(this,R.color.colorRed));
        textView1.setTextColor(getResources().getColor(R.color.colorWhite));

        textView2.setBackgroundColor( ContextCompat.getColor(this,android.R.color.transparent));
        textView2.setTextColor( ContextCompat.getColor(this,android.R.color.black));

        textView3.setBackgroundColor( ContextCompat.getColor(this,android.R.color.transparent));
        textView3.setTextColor( ContextCompat.getColor(this,android.R.color.black));

        textView4.setBackgroundColor( ContextCompat.getColor(this,android.R.color.transparent));
        textView4.setTextColor( ContextCompat.getColor(this,android.R.color.black));

        textView5.setBackgroundColor( ContextCompat.getColor(this,android.R.color.transparent));
        textView5.setTextColor( ContextCompat.getColor(this,android.R.color.black));
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void validateAndNext(){

        dataMapOfPackages = new ArrayList<HashMap<String, String>>();

        if(editText_ship_date.getText().toString().isEmpty()){
            textInput_ship_date.setError(getResources().getString(R.string.err_msg_shipment_date));
            requestFocus(editText_ship_date);
        }else if(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US")&&spinner_carrier.getSelectedItemPosition()==-1) {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_carrier));
            customDialogForHelp.show();
            //Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_carrier),Snackbar.LENGTH_SHORT).show();
        } else if(AppConstant.IS_FROM_CREATE_SHIPMENT && strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("FedEX")&&spinner_service_type.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_service_type));
            customDialogForHelp.show();
            // Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_service_type),Snackbar.LENGTH_SHORT).show();
        } else if(!(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US"))&&spinner_service_type.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_service_type));
            customDialogForHelp.show();
            // Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_service_type),Snackbar.LENGTH_SHORT).show();
        }
        else if(strCarrierCode != null &&!strCarrierCode.equalsIgnoreCase("USPS")&&spinner_pickup_drop.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_pickup_drop));
            customDialogForHelp.show();
            // Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_pickup_drop),Snackbar.LENGTH_SHORT).show();
        }else if(spinner_package_type.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_package_type));
            customDialogForHelp.show();
            // Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_package_type),Snackbar.LENGTH_SHORT).show();
        }else if(!(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US"))&&strPackageContents.equalsIgnoreCase("")){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_package_contents));
            customDialogForHelp.show();
        }else if(!(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US"))&&rb_products_commodities.isChecked()&&editText_customs_value.getText().toString().trim().isEmpty()){
          /*  CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_package_contents));
            customDialogForHelp.show();*/
            textInput_customs_value.setError(getResources().getString(R.string.msg_customs_value));
            requestFocus(editText_customs_value);
            textInput_ship_date.setErrorEnabled(false);
        }
       /* else if(strCarrierCode != null&&strCarrierCode.equalsIgnoreCase("USPS")&&spinner_container_type.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_container_type));
            customDialogForHelp.show();
            //Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_container_type),Snackbar.LENGTH_SHORT).show();
        }*/
       /* else if(strCarrierCode != null&&strCarrierCode.equalsIgnoreCase("USPS")&&spinner_sizes.getSelectedItemPosition()==-1){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.lbl_select_sizes));
            customDialogForHelp.show();
           // Snackbar.make(tv_package1,getResources().getString(R.string.lbl_select_sizes),Snackbar.LENGTH_SHORT).show();
        } */

       /* else if(!(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US"))&&editText_customs_value.getText().toString().trim().isEmpty()) {

           textInput_customs_value.setError(getResources().getString(R.string.msg_customs_value));
            requestFocus(editText_customs_value);
            textInput_ship_date.setErrorEnabled(false);

        }
        else if(!(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US"))&&editText_customs_description.getText().toString().trim().isEmpty()) {
          textInput_customs_description.setError(getResources().getString(R.string.msg_customs_desc));
            requestFocus(editText_customs_description);
            textInput_customs_value.setErrorEnabled(false);
            textInput_ship_date.setErrorEnabled(false);

        }*/
        else if (strTextViewNo.equalsIgnoreCase("1")&&tv_package_details1.getText().toString().isEmpty()){
            textInput_customs_value.setErrorEnabled(false);

            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_package_info));
            customDialogForHelp.show();
            //Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
        } else if(!cb_identical.isChecked()){
            Log.e("Harshada"," 2 => "+tv_package_details2.getText().toString().isEmpty()+" 3 => "+tv_package_details3.getText().toString().isEmpty()+" 4 => "+tv_package_details4.getText().toString().isEmpty()+"  5 => "+tv_package_details5.getText().toString().isEmpty());
            if(strTextViewNo.equalsIgnoreCase("2")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty())){
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_package_info));
                customDialogForHelp.show();
                //Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
            } else if(strTextViewNo.equalsIgnoreCase("3")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty()||tv_package_details3.getText().toString().isEmpty())){
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_package_info));
                customDialogForHelp.show();
                //Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
            } else if(strTextViewNo.equalsIgnoreCase("4")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty()||tv_package_details3.getText().toString().isEmpty()||tv_package_details4.getText().toString().isEmpty())){
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_package_info));
                customDialogForHelp.show();
                //Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
            } else if(strTextViewNo.equalsIgnoreCase("5")&&(tv_package_details1.getText().toString().isEmpty()||tv_package_details2.getText().toString().isEmpty()||tv_package_details3.getText().toString().isEmpty()||tv_package_details4.getText().toString().isEmpty()||tv_package_details5.getText().toString().isEmpty())){
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_package_info));
                customDialogForHelp.show();
                //Snackbar.make(tv_package1,getResources().getString(R.string.err_msg_package_info),Snackbar.LENGTH_SHORT).show();
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

                mSessionManager.putStringData(SessionManager.KEY_SHAPE,strShape);
                mSessionManager.putStringData(SessionManager.KEY_SIZES,strSize);

                mSessionManager.putStringData(SessionManager.KEY_PACKAGE_COUNT,strTextViewNo);

                mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_PURPOSE,strShipmentPurpose);
                Log.e("package",strPackageContents);
                mSessionManager.putStringData(SessionManager.KEY_PACKAGE_CONTENTS,strPackageContents);


                if(cb_identical.isChecked()){
                    mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,true);
                }else {
                    mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,false);
                }

                if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
                    if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                        dataMapOfPackages.add(hashMapPackage1);
                    }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                        dataMapOfPackages.add(hashMapPackage2);
                    }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                        dataMapOfPackages.add(hashMapPackage3);
                    }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                        dataMapOfPackages.add(hashMapPackage4);
                    }else {
                        dataMapOfPackages.add(hashMapPackage5);
                    }

                    dataMapOfPackages.add(hashMapPackage1);
                    tv_package1.setText("Packages");

                    hashMapPackage2.clear();
                    hashMapPackage3.clear();
                    hashMapPackage4.clear();
                    hashMapPackage5.clear();

                    tv_package_details2.setText("");
                    tv_package_details3.setText("");
                    tv_package_details4.setText("");
                    tv_package_details5.setText("");

                    dataMapOfPackages.remove(hashMapPackage2);
                    dataMapOfPackages.remove(hashMapPackage3);
                    dataMapOfPackages.remove(hashMapPackage4);
                    dataMapOfPackages.remove(hashMapPackage5);

                }else {
                    if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                        dataMapOfPackages.add(hashMapPackage1);

                    }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                        dataMapOfPackages.add(hashMapPackage1);
                        dataMapOfPackages.add(hashMapPackage2);

                    }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                        dataMapOfPackages.add(hashMapPackage1);
                        dataMapOfPackages.add(hashMapPackage2);
                        dataMapOfPackages.add(hashMapPackage3);

                    }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                        dataMapOfPackages.add(hashMapPackage1);
                        dataMapOfPackages.add(hashMapPackage2);
                        dataMapOfPackages.add(hashMapPackage3);
                        dataMapOfPackages.add(hashMapPackage4);

                    }else {
                        dataMapOfPackages.add(hashMapPackage1);
                        dataMapOfPackages.add(hashMapPackage2);
                        dataMapOfPackages.add(hashMapPackage3);
                        dataMapOfPackages.add(hashMapPackage4);
                        dataMapOfPackages.add(hashMapPackage5);
                    }
                }

                showDesclaimer(ShipmentDetailsAssociateActivity.this);
               /* if(rb_products_commodities.isChecked()){
                    startActivity(new Intent(ShipmentDetailsAssociateActivity.this,CommodityDetailsCustomerActivity.class));
                }else {
                    getCheckRatesDetails();
                }*/
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

            mSessionManager.putStringData(SessionManager.KEY_SHAPE,strShape);
            mSessionManager.putStringData(SessionManager.KEY_SIZES,strSize);

            mSessionManager.putStringData(SessionManager.KEY_PACKAGE_COUNT,strTextViewNo);
            mSessionManager.putStringData(SessionManager.KEY_SHIPMENT_PURPOSE,strShipmentPurpose);
            Log.e("package",strPackageContents);

            mSessionManager.putStringData(SessionManager.KEY_PACKAGE_CONTENTS,strPackageContents);

            if(cb_identical.isChecked()){
                mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,true);
            }else {
                mSessionManager.putBooleanData(SessionManager.KEY_IS_IDENTICAL,false);
            }

            if(mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)){
                cb_identical.setChecked(true);
                // tv_first.performClick();
                if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    dataMapOfPackages.add(hashMapPackage1);
                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    dataMapOfPackages.add(hashMapPackage2);
                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                    dataMapOfPackages.add(hashMapPackage3);
                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                    dataMapOfPackages.add(hashMapPackage4);
                }else {
                    dataMapOfPackages.add(hashMapPackage5);
                }

                dataMapOfPackages.add(hashMapPackage1);
                tv_package1.setText("Packages");
                hashMapPackage2.clear();
                hashMapPackage3.clear();
                hashMapPackage4.clear();
                hashMapPackage5.clear();

                tv_package_details2.setText("");
                tv_package_details3.setText("");
                tv_package_details4.setText("");
                tv_package_details5.setText("");

                dataMapOfPackages.remove(hashMapPackage2);
                dataMapOfPackages.remove(hashMapPackage3);
                dataMapOfPackages.remove(hashMapPackage4);
                dataMapOfPackages.remove(hashMapPackage5);

            }else{
                cb_identical.setChecked(false);
                if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")){
                    dataMapOfPackages.add(hashMapPackage1);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")){
                    dataMapOfPackages.add(hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage2);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")){
                    dataMapOfPackages.add(hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage3);

                }else if(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")){
                    dataMapOfPackages.add(hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage3);
                    dataMapOfPackages.add(hashMapPackage4);

                }else{
                    dataMapOfPackages.add(hashMapPackage1);
                    dataMapOfPackages.add(hashMapPackage2);
                    dataMapOfPackages.add(hashMapPackage3);
                    dataMapOfPackages.add(hashMapPackage4);
                    dataMapOfPackages.add(hashMapPackage5);
                }
            }

            showDesclaimer(ShipmentDetailsAssociateActivity.this);
           /* if(rb_products_commodities.isChecked()){
                startActivity(new Intent(ShipmentDetailsAssociateActivity.this,CommodityDetailsCustomerActivity.class));
            }else{
                getCheckRatesDetails();
            }*/
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

            showProgressDialog();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_check_rates);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    mSessionManager.putStringData(SessionManager.KEY_CURRENCY_UNIT,strCurrencyUnit);
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
                                    //progressBar.dismiss();
                                    closeProgressBar();
                                    startActivity(new Intent(ShipmentDetailsAssociateActivity.this,CheckRateScreenAssociateActivity.class).putParcelableArrayListExtra("RateList",ratesArrayList));
                                    //ratesArrayList.clear();

                                }else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                                    customDialogForHelp.show();
                                    // Snackbar.make(spinner_carrier,jsonObject.getString(JSONConstant.MESSAGE),Snackbar.LENGTH_LONG).setDuration(5000).show();
                                }
                                // progressBar.dismiss();
                                closeProgressBar();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //progressBar.dismiss();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            // progressBar.dismiss();
                            closeProgressBar();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(JSONConstant.FROM_A_COUNTRY_CODE, AppConstant.F_A_COUNTRY_CODE);
                    params.put(JSONConstant.FROM_A_ZIPCODE, AppConstant.F_A_ZIPCODE);
                    params.put(JSONConstant.TO_A_COUNTRY_CODE, AppConstant.T_A_COUNTRY_CODE);
                    params.put(JSONConstant.TO_A_ZIPCODE, AppConstant.T_A_ZIPCODE);
                    params.put(JSONConstant.PACKAGECOUNT,strTextViewNo);
                    params.put(JSONConstant.SERVICESHIPDATE,editText_ship_date.getText().toString());
                    params.put(JSONConstant.PACKAGING_TYPE,strPackageType);
                    params.put(JSONConstant.SERVICE_TYPE,strServiceType);


                   /* if (strCarrierCode != null &&!strCarrierCode.equalsIgnoreCase("UPS")){
                        params.put(JSONConstant.SERVICE_TYPE,strServiceType);
                    }*/

                    if (strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("USPS")){
                        params.put(JSONConstant.CONTAINER,"");
                        params.put("Shape",strShape);

                        params.put(JSONConstant.SIZE,strSize);
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

                    //AppConstant.noOfPackages.addAll(dataMapOfPackages);
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

                    AppConstant.noOfSpecialServices = dataMapOfSpecialServices;
                    //AppConstant.noOfSpecialServices.addAll(dataMapOfPackages);
                    // AppConstant.noOfSpecialServices.replaceAll(dataMapOfPackages);
                    try {
                        List<JSONObject> jsonObj = new ArrayList<JSONObject>();

                        for(HashMap<String, String> data : dataMapOfSpecialServices) {
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

                    params.put(JSONConstant.FROM_A_STATE,"");
                    params.put(JSONConstant.FROM_A_ADDRESS_LINE1,"");
                    params.put(JSONConstant.FROM_A_CITY,"");
                    params.put(JSONConstant.TO_A_STATE,"");
                    params.put(JSONConstant.TO_A_ADDRESS_LINE1,"");
                    params.put(JSONConstant.TO_A_CITY,"");


                    /*if(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US")) {
                        params.put("customs_currency","");
                        params.put("customs_value","");
                        params.put("customs_description","");

                        mSessionManager.putStringData(SessionManager.KEY_CUSTOM_VALUE,"");
                        mSessionManager.putStringData(SessionManager.KEY_CUSTOM_DESC,"");

                    }else {
                        params.put("customs_currency",strCurrencyUnit);
                        params.put("customs_value",editText_customs_value.getText().toString().trim());
                        params.put("customs_description",editText_customs_description.getText().toString().trim());

                        mSessionManager.putStringData(SessionManager.KEY_CUSTOM_VALUE,editText_customs_value.getText().toString().trim());
                        mSessionManager.putStringData(SessionManager.KEY_CUSTOM_DESC,editText_customs_value.getText().toString().trim());

                    }*/


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
        progressBar = new ProgressDialog(ShipmentDetailsAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }

    public void getCarriers(){
        try {

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_carriers);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    JSONArray jsonArray = Json_response.getJSONArray("carriers");

                                    arrayListCarriers = new ArrayList<>();
                                    carrierList = new ArrayList<>();

                                    for(int i = 0; i<jsonArray.length();i++) {
                                        JSONObject jsonObjectCarrier = jsonArray.getJSONObject(i);
                                        Carriers carriers = new Carriers();
                                        carriers.setCr_code(jsonObjectCarrier.getString("cr_carrier_code"));
                                        carriers.setCr_name(jsonObjectCarrier.getString("cr_carrier_name"));
                                        arrayListCarriers.add(carriers);
                                        carrierList.add(jsonObjectCarrier.getString("cr_carrier_name"));
                                    }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this,
                                        android.R.layout.simple_spinner_item, carrierList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_carrier.setAdapter(dataAdapter);

                                // progressBar.dismiss();
                                closeProgressBar();

                                /*if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
                                    for (int i = 0;i<arrayListCarriers.size();i++) {
                                        System.out.println(arrayListCarriers.get(i).getCr_code()+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
                                        if (arrayListCarriers.get(i).getCr_code().equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE))) {
                                            spinner_carrier.setItemOnPosition(i);

                                        }
                                    }
                                }*/

                                //getServiceTypes();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //progressBar.dismiss();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            //progressBar.dismiss();
                            closeProgressBar();
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
            showProgressDialog();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_service_types);

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
                                    arrayListServiceTypes = new ArrayList<>();
                                    seviceTypeList = new ArrayList<>();

                                    for(int i = 0; i<jsonArray.length();i++) {
                                        JSONObject jsonObjectService = jsonArray.getJSONObject(i);

                                        seviceTypeList.add(jsonObjectService.getString("ServiceDescription"));
                                        ServiceTypes serviceTypes = new ServiceTypes();
                                        serviceTypes.setServiceType(jsonObjectService.getString("Service"));
                                        serviceTypes.setServiceText(jsonObjectService.getString("ServiceDescription"));
                                        arrayListServiceTypes.add(serviceTypes);
                                    }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this,
                                        android.R.layout.simple_spinner_item, seviceTypeList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_service_type.setAdapter(dataAdapter);

                                // progressBar.dismiss();


                               /* if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE));
                                    for (int i = 0;i<arrayListServiceTypes.size();i++) {
                                        System.out.println(arrayListServiceTypes.get(i).getServiceType()+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE));
                                        if (arrayListServiceTypes.get(i).getServiceType().equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_SERVICE_TYPE))) {
                                            spinner_service_type.setItemOnPosition(i);

                                        }
                                    }
                                }*/

                                //added 2 jan for USPS
                                if(strCarrierCode.equalsIgnoreCase("USPS")){
                                    /*tv_container_type.setVisibility(View.VISIBLE);
                                    tv_sizes.setVisibility(View.VISIBLE);
                                    spinner_container_type.setVisibility(View.VISIBLE);
                                    spinner_sizes.setVisibility(View.VISIBLE);
                                    getContainerType();
                                    */
                                    closeProgressBar();

                                }else {
                                  /*  tv_container_type.setVisibility(View.GONE);
                                    tv_sizes.setVisibility(View.GONE);
                                    spinner_container_type.setVisibility(View.GONE);
                                    spinner_sizes.setVisibility(View.GONE);*/

                                    tv_pickup_drop.setVisibility(View.VISIBLE);
                                    spinner_pickup_drop.setVisibility(View.VISIBLE);

                                    getDropOff();
                                }

                                //getDropOff();

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
                            // progressBar.dismiss();
                            closeProgressBar();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    params.put("OriginCountryCode", AppConstant.F_A_COUNTRY_CODE);
                    params.put("OriginPostalCode", AppConstant.F_A_ZIPCODE);
                    params.put("DestinationCountryCode", AppConstant.T_A_COUNTRY_CODE);
                    params.put("DestinationPostalCode", AppConstant.T_A_ZIPCODE);

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

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_drop_off_type);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                //JSONObject Json_response = new JSONObject(response);
                                JSONArray jsonArray = new JSONArray(response);
                                dropOffList = new ArrayList<>();
                                arrayListDropOffs = new ArrayList<>();
                                // seviceTypeList.add(0,"Select Drop Off");
                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    dropOffList.add(jsonObject.getString("drop_off_text"));
                                    DropOff dropOff = new DropOff();
                                    dropOff.setDropOffText(jsonObject.getString("drop_off_text"));
                                    dropOff.setDropOffType(jsonObject.getString("drop_off_type"));
                                    arrayListDropOffs.add(dropOff);

                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this,
                                        android.R.layout.simple_spinner_item, dropOffList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_pickup_drop.setAdapter(dataAdapter);

                               /* if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));
                                    for (int i = 0;i<arrayListDropOffs.size();i++) {
                                        System.out.println(arrayListDropOffs.get(i).getDropOffType()+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));
                                        if (arrayListDropOffs.get(i).getDropOffType().equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP))) {
                                            spinner_pickup_drop.setItemOnPosition(i);

                                        }
                                    }
                                }
*/

                                //Log.e("harshada",strCarrierCode+"="+carrierCode);
                                // progressBar.dismiss();

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
                            //progressBar.dismiss();
                            closeProgressBar();
                        }
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

    public void getContainerType(){
        try {


            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_contianer_type);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {

                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    JSONArray jsonArray = Json_response.getJSONArray("container_types");
                                    containerTypeList = new ArrayList<>();

                                    for (int i = 0; i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        containerTypeList.add(jsonObject.getString("upc_container_type"));
                                    }
                                }



                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this, android.R.layout.simple_spinner_item, containerTypeList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_container_type.setAdapter(dataAdapter);

                               /* if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_CONTAINER_TYPE));
                                    for (int i = 0;i<containerTypeList.size();i++) {
                                        System.out.println(containerTypeList.get(i)+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_CONTAINER_TYPE));
                                        if (containerTypeList.get(i).equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_CONTAINER_TYPE))) {
                                            spinner_container_type.setItemOnPosition(i);

                                        }
                                    }
                                }*/


                                //getSizes();

                                //Log.e("harshada",strCarrierCode+"="+carrierCode);
                                //progressBar.dismiss();

                                //getPackageType();

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
                            // progressBar.dismiss();
                            closeProgressBar();
                        }
                    }) {

              /*  @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    return params;
                }*/

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

    public void getSizes(){
        try {

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_package_size);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {

                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    JSONArray jsonArray = Json_response.getJSONArray("package_sizes");
                                    sizesList = new ArrayList<>();

                                    for (int i = 0; i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        sizesList.add(jsonObject.getString("size"));

                                    }
                                }


                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this,
                                        android.R.layout.simple_spinner_item, sizesList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_sizes.setAdapter(dataAdapter);

                                /*if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_SIZES));
                                    for (int i = 0;i<sizesList.size();i++) {
                                        System.out.println(sizesList.get(i)+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_SIZES));
                                        if (sizesList.get(i).equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_SIZES))) {
                                            spinner_sizes.setItemOnPosition(i);

                                        }
                                    }
                                }*/

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
                            closeProgressBar();
                            //progressBar.dismiss();
                        }
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

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_package_types);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    JSONArray jsonArray = Json_response.getJSONArray("package_types");
                                    packageNameList = new ArrayList<>();
                                    packageTypeList = new ArrayList<>();

                                    for(int i = 0; i<jsonArray.length();i++) {
                                        JSONObject jsonObjectService = jsonArray.getJSONObject(i);
                                        packageTypeList.add(jsonObjectService.getString("fpt_package_type"));
                                        packageNameList.add(jsonObjectService.getString("fpt_package_name"));
                                    }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this, android.R.layout.simple_spinner_item, packageNameList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_package_type.setAdapter(dataAdapter);

                                /*if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT){

                                    Log.e("Harshada","->"+mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));
                                    for (int i = 0;i<packageTypeList.size();i++) {
                                        System.out.println(packageTypeList.get(i)+"satte name ->"+ mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));
                                        if (packageTypeList.get(i).equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE))) {
                                            spinner_package_type.setItemOnPosition(i);

                                        }
                                    }
                                }*/

                                if(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US")){
                                    closeProgressBar();
                                }else{
                                    getShipmentPurpose();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            closeProgressBar();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    params.put("service_type",strServiceType);
                    params.put(JSONConstant.FROM_A_COUNTRY_CODE, AppConstant.F_A_COUNTRY_CODE);
                    params.put(JSONConstant.TO_A_COUNTRY_CODE, AppConstant.T_A_COUNTRY_CODE);
                    params.put(JSONConstant.FROM_A_ZIPCODE, AppConstant.F_A_ZIPCODE);
                    params.put(JSONConstant.TO_A_ZIPCODE, AppConstant.T_A_ZIPCODE);
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


    public void getShipmentPurpose(){
        try {

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_fedex_shipment_purpose);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    JSONArray jsonArray = Json_response.getJSONArray("shipping_purpose");
                                    arrayListOfShipmentPurposes = new ArrayList<>();
                                    shipmentPurposesList = new ArrayList<>();

                                    for(int i = 0; i<jsonArray.length();i++) {
                                        JSONObject jsonObjectShipmentPurposes = jsonArray.getJSONObject(i);
                                        ShipmentPurpose shipmentPurpose = new ShipmentPurpose();
                                        shipmentPurpose.setShipping_purpose(jsonObjectShipmentPurposes.getString("shipping_purpose"));
                                        shipmentPurpose.setShipping_purpose_description(jsonObjectShipmentPurposes.getString("shipping_purpose_description"));
                                        shipmentPurposesList.add(jsonObjectShipmentPurposes.getString("shipping_purpose_description"));
                                        arrayListOfShipmentPurposes.add(shipmentPurpose);
                                    }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this, android.R.layout.simple_spinner_item, shipmentPurposesList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_shipment_purpose.setAdapter(dataAdapter);
                                //closeProgressBar();
                                getDocuments();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            closeProgressBar();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    params.put("service_type",strServiceType);
                    params.put(JSONConstant.FROM_A_COUNTRY_CODE, AppConstant.F_A_COUNTRY_CODE);
                    params.put(JSONConstant.TO_A_COUNTRY_CODE, AppConstant.T_A_COUNTRY_CODE);
                    params.put(JSONConstant.FROM_A_ZIPCODE, AppConstant.F_A_ZIPCODE);
                    params.put(JSONConstant.TO_A_ZIPCODE, AppConstant.T_A_ZIPCODE);
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


    public void getDocuments(){
        try {

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_fetch_fedex_document_types);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Send Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){

                                    JSONArray jsonArray = Json_response.getJSONArray("document_types");
                                    documentsList = new ArrayList<>();

                                    for(int i = 0; i<jsonArray.length();i++) {
                                        JSONObject jsonObjectShipmentPurposes = jsonArray.getJSONObject(i);
                                        documentsList.add(jsonObjectShipmentPurposes.getString("document_type"));
                                    }
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this, android.R.layout.simple_spinner_item, documentsList);
                                // set the view for the Drop down list
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // set the ArrayAdapter to the spinner
                                spinner_document_desc.setAdapter(dataAdapter);
                                closeProgressBar();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeProgressBar();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("==Volley Error=" + error);
                            closeProgressBar();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cr_carrier_code",strCarrierCode);
                    params.put("service_type",strServiceType);
                    params.put(JSONConstant.FROM_C_COUNTRY_CODE, AppConstant.F_C_COUNTRY_CODE);
                    params.put(JSONConstant.TO_C_COUNTRY_CODE, AppConstant.T_C_COUNTRY_CODE);
                    params.put(JSONConstant.FROM_C_ZIPCODE, AppConstant.F_C_ZIPCODE);
                    params.put(JSONConstant.TO_C_ZIPCODE, AppConstant.T_C_ZIPCODE);
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
        final EditText editText_weight,editText_declared_value,editText_length,editText_width,editText_height,editText_package_description,editText_girth,editText_no_of_skid;
        final TextInputLayout textInput_weight,textInput_declared_value,textInput_length,textInput_width,textInput_height,textInput_girth, textInput_no_of_skid;
        final LinearLayout ll_package_dimension;
        final Spinner spinner_unit,spinner_currency;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShipmentDetailsAssociateActivity.this);
        LayoutInflater inflater = ShipmentDetailsAssociateActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_for_package, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
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
        editText_girth = (EditText) dialogView.findViewById(R.id.editText_girth);
        editText_package_description = (EditText)dialogView.findViewById(R.id.editText_package_description);
        editText_no_of_skid = (EditText) dialogView.findViewById(R.id.editText_no_of_skid);

        tv_weight_unit = (TextView)dialogView. findViewById(R.id.tv_weight_unit);
        tv_currency_unit = (TextView) dialogView.findViewById(R.id.tv_currency_unit);

        textInput_weight = (TextInputLayout)dialogView. findViewById(R.id.textInput_weight);
        textInput_declared_value = (TextInputLayout)dialogView. findViewById(R.id.textInput_declared_value);
        textInput_length = (TextInputLayout)dialogView. findViewById(R.id.textInput_length);
        textInput_height = (TextInputLayout)dialogView. findViewById(R.id.textInput_height);
        textInput_width = (TextInputLayout)dialogView. findViewById(R.id.textInput_width);
        textInput_girth = (TextInputLayout) dialogView.findViewById(R.id.textInput_girth);
        textInput_no_of_skid = (TextInputLayout) dialogView.findViewById(R.id.textInput_no_of_skid);

        spinner_unit = (Spinner)dialogView. findViewById(R.id.spinner_unit);
        spinner_currency = (Spinner)dialogView. findViewById(R.id.spinner_currency);


        String[] unit=getResources().getStringArray(R.array.array_unit);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this,
                android.R.layout.simple_spinner_item, unit);
        // set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_unit.setAdapter(dataAdapter);

        String[] currency_unit=getResources().getStringArray(R.array.array_currency_unit);

        ArrayAdapter<String> currencyUnitAdapter = new ArrayAdapter<String>(ShipmentDetailsAssociateActivity.this,
                android.R.layout.simple_spinner_item, currency_unit);
        // set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_currency.setAdapter(currencyUnitAdapter);


        if(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("FedEX")){
            if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                textInput_no_of_skid.setVisibility(View.VISIBLE);
                cb_identical.setVisibility(View.GONE);
            }else {
                /*editText_no_of_skid.setHint(getResources().getString(R.string.hint_no_of_quantity));
                textInput_no_of_skid.setVisibility(View.VISIBLE);
                cb_identical.setVisibility(View.GONE);*/

                if(!cb_identical.isChecked()){
                    editText_no_of_skid.setHint(getResources().getString(R.string.hint_no_of_quantity));
                    textInput_no_of_skid.setVisibility(View.VISIBLE);
                    cb_identical.setVisibility(View.VISIBLE);
                }else {
                    textInput_no_of_skid.setVisibility(View.GONE);
                    cb_identical.setVisibility(View.VISIBLE);
                }
            }
        }else {
            textInput_no_of_skid.setVisibility(View.GONE);
            cb_identical.setVisibility(View.VISIBLE);
        }



        if(tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
            spinner_currency.setVisibility(View.VISIBLE);
            tv_currency_unit.setVisibility(View.GONE);
        }else {
            spinner_currency.setVisibility(View.GONE);
            tv_currency_unit.setVisibility(View.VISIBLE);
        }

       /* if(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&&AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US")){
            spinner_unit.setVisibility(View.GONE);
            tv_weight_unit.setVisibility(View.VISIBLE);
        }else {
            spinner_unit.setVisibility(View.VISIBLE);
            tv_weight_unit.setVisibility(View.GONE);
        }*/

        if(tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))&&(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("FedEX"))){
            spinner_unit.setVisibility(View.VISIBLE);
            tv_weight_unit.setVisibility(View.GONE);

            if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                spinner_unit.setVisibility(View.GONE);
                tv_weight_unit.setVisibility(View.VISIBLE);
            }else {
                spinner_unit.setVisibility(View.VISIBLE);
                tv_weight_unit.setVisibility(View.GONE);
            }
        }else {
            spinner_unit.setVisibility(View.GONE);
            tv_weight_unit.setVisibility(View.VISIBLE);
        }

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

        if((strPackageType != null && strPackageType.equalsIgnoreCase("YOUR_PACKAGING"))||(strSize != null && strSize.equalsIgnoreCase("LARGE"))||(strPackageType != null && strPackageType.equalsIgnoreCase("30"))||(strPackageType != null && strPackageType.equalsIgnoreCase("02"))) {
            ll_package_dimension.setVisibility(View.VISIBLE);
            tv_package_dimension.setVisibility(View.VISIBLE);
            if(strShape != null && strShape.equalsIgnoreCase("NONRECTANGULAR")){
                textInput_girth.setVisibility(View.VISIBLE);
            }else {
                textInput_girth.setVisibility(View.GONE);
            }
        }else {
            ll_package_dimension.setVisibility(View.GONE);
            tv_package_dimension.setVisibility(View.GONE);
            textInput_girth.setVisibility(View.GONE);
        }

        if(!hashMap.isEmpty()){

            if(strPackageType != null &&strPackageType.equalsIgnoreCase("YOUR_PACKAGING")||(strSize != null && strSize.equalsIgnoreCase("LARGE"))||(strPackageType != null && strPackageType.equalsIgnoreCase("30"))||(strPackageType != null && strPackageType.equalsIgnoreCase("02"))) {
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

                if(strShape != null && strShape.equalsIgnoreCase("NONRECTANGULAR")){
                    textInput_girth.setVisibility(View.VISIBLE);
                    editText_girth.setText(hashMap.get(AppConstant.HM_GIRTH).toString());
                }else {
                    textInput_girth.setVisibility(View.GONE);
                }

              /* if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                    textInput_no_of_skid.setVisibility(View.VISIBLE);
                    editText_no_of_skid.setText(hashMap.get(AppConstant.HM_HANDLING_UNITS).toString());
                }else {
                    textInput_no_of_skid.setVisibility(View.GONE);
                }*/

                if(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("FedEX")){
                    if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                        textInput_no_of_skid.setVisibility(View.VISIBLE);
                        cb_identical.setVisibility(View.GONE);

                        editText_no_of_skid.setText(hashMap.get(AppConstant.HM_HANDLING_UNITS).toString());
                    }else {
                        if(!cb_identical.isChecked()){
                            editText_no_of_skid.setHint(getResources().getString(R.string.hint_no_of_quantity));
                            textInput_no_of_skid.setVisibility(View.VISIBLE);
                            cb_identical.setVisibility(View.VISIBLE);
                            editText_no_of_skid.setText(hashMap.get(AppConstant.HM_HANDLING_UNITS).toString());
                        }else {
                            textInput_no_of_skid.setVisibility(View.GONE);
                            cb_identical.setVisibility(View.VISIBLE);
                        }

                    }
                }else {
                    textInput_no_of_skid.setVisibility(View.GONE);
                    cb_identical.setVisibility(View.VISIBLE);
                }



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


                if(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&&AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US")){
                    //if(tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                    tv_weight_unit.setVisibility(View.VISIBLE);
                    tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                    // }
                    strWeightUnit = "LB";
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                }else {
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

               /* if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                    textInput_no_of_skid.setVisibility(View.VISIBLE);
                    editText_no_of_skid.setText(hashMap.get(AppConstant.HM_HANDLING_UNITS).toString());
                }else {
                    textInput_no_of_skid.setVisibility(View.GONE);
                }*/


                if(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("FedEX")){
                    if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                        textInput_no_of_skid.setVisibility(View.VISIBLE);
                        cb_identical.setVisibility(View.GONE);

                        textInput_no_of_skid.setVisibility(View.VISIBLE);
                        editText_no_of_skid.setText(hashMap.get(AppConstant.HM_HANDLING_UNITS).toString());
                    }else {
                        editText_no_of_skid.setHint(getResources().getString(R.string.hint_no_of_quantity));
                        textInput_no_of_skid.setVisibility(View.VISIBLE);
                       // cb_identical.setVisibility(View.GONE);

                        textInput_no_of_skid.setVisibility(View.VISIBLE);
                        editText_no_of_skid.setText(hashMap.get(AppConstant.HM_HANDLING_UNITS).toString());
                    }
                }else {
                    textInput_no_of_skid.setVisibility(View.GONE);
                    cb_identical.setVisibility(View.VISIBLE);
                }

               /* if(hashMap.get(AppConstant.HM_WEIGHT_UNIT).toString().equalsIgnoreCase(getResources().getString(R.string.lbl_weight_unit_kg))){
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
                }*/

                if(AppConstant.T_A_COUNTRY_CODE.equalsIgnoreCase("US")&& AppConstant.F_A_COUNTRY_CODE.equalsIgnoreCase("US")){
                    //if(tv_title.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                    tv_weight_unit.setVisibility(View.VISIBLE);
                    tv_weight_unit.setText(getResources().getString(R.string.lbl_weight_unit_lbs));
                    // }
                    strWeightUnit = "LB";
                    tv_package_dimension.setText(getResources().getString(R.string.lbl_package_dimension_in));
                }else {
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


                if(strPackageType != null &&strPackageType.equalsIgnoreCase("YOUR_PACKAGING")||(strSize != null && strSize.equalsIgnoreCase("LARGE"))||(strPackageType != null && strPackageType.equalsIgnoreCase("30"))||(strPackageType != null && strPackageType.equalsIgnoreCase("02"))) {
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
                        textInput_height.setError(getResources().getString(R.string.err_msg_height));
                        requestFocus(editText_weight);
                    }else if(strShape != null && strShape.equalsIgnoreCase("NONRECTANGULAR")&&editText_girth.getText().toString().isEmpty()) {
                        textInput_height.setErrorEnabled(false);
                        textInput_girth.setError(getResources().getString(R.string.err_msg_girth));
                        requestFocus(editText_girth);
                    } else if(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("FedEX")&&strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")&&editText_no_of_skid.getText().toString().isEmpty()){
                        textInput_girth.setErrorEnabled(false);
                        textInput_no_of_skid.setError(getResources().getString(R.string.err_msg_enter_no_of_skid));
                        requestFocus(editText_no_of_skid);
                    } else if(strCarrierCode != null &&strCarrierCode.equalsIgnoreCase("FedEX")&&strServiceTypeDesc != null && !strServiceTypeDesc.contains("Freight")&&editText_no_of_skid.getText().toString().isEmpty()){
                        textInput_girth.setErrorEnabled(false);
                        textInput_no_of_skid.setError(getResources().getString(R.string.err_msg_enter_no_of_quantity));
                        requestFocus(editText_no_of_skid);
                    } else {
                        textInput_declared_value.setErrorEnabled(false);
                        textInput_weight.setErrorEnabled(false);
                        textInput_length.setErrorEnabled(false);
                        textInput_width.setErrorEnabled(false);
                        textInput_height.setErrorEnabled(false);
                        textInput_girth.setErrorEnabled(false);
                        textInput_no_of_skid.setErrorEnabled(false);

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

                        if(strShape != null && strShape.equalsIgnoreCase("NONRECTANGULAR")){
                            hashMap.put(AppConstant.HM_GIRTH,editText_girth.getText().toString().trim());
                        }else {
                            hashMap.put(AppConstant.HM_GIRTH,"");
                        }

                        if((strCarrierCode!=null&&strCarrierCode.equalsIgnoreCase("FedEX"))||(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight"))){
                            // hashMap.put(AppConstant.HM_HANDLING_UNITS,editText_no_of_skid.getText().toString().trim());
                            if(cb_identical.isChecked()){
                                hashMap.put(AppConstant.HM_HANDLING_UNITS,"1");
                            }else {
                                hashMap.put(AppConstant.HM_HANDLING_UNITS,editText_no_of_skid.getText().toString().trim());
                            }
                        }else{
                            hashMap.put(AppConstant.HM_HANDLING_UNITS,"");
                        }

                       /* if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                            hashMap.put(AppConstant.HM_HANDLING_UNITS,editText_no_of_skid.getText().toString().trim());
                        }else{
                            hashMap.put(AppConstant.HM_HANDLING_UNITS,"");
                        }
*/
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
                        hashMap.put(AppConstant.HM_GIRTH,"");

                        if((strCarrierCode!=null&&strCarrierCode.equalsIgnoreCase("FedEX"))||(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight"))){
                            if(cb_identical.isChecked()){
                                hashMap.put(AppConstant.HM_HANDLING_UNITS,"1");
                            }else {
                                hashMap.put(AppConstant.HM_HANDLING_UNITS,editText_no_of_skid.getText().toString().trim());
                            }
                        }else{
                            hashMap.put(AppConstant.HM_HANDLING_UNITS,"");
                        }

                        alertDialog.hide();

                        hideKeyboard();
                        showPackageDetails(textView, hashMap);
                    }
                }

                if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_1))){
                    hashMapPackage1.putAll(hashMap);
                    AppConstant.hashMapPackage1.putAll(hashMap);

                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_2))){
                    hashMapPackage2.putAll(hashMap);
                    AppConstant.hashMapPackage2.putAll(hashMap);

                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_3))){
                    hashMapPackage3.putAll(hashMap);
                    AppConstant.hashMapPackage3.putAll(hashMap);

                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_4))){
                    hashMapPackage4.putAll(hashMap);
                    AppConstant.hashMapPackage4.putAll(hashMap);

                }else if(titleShow.equalsIgnoreCase(getResources().getString(R.string.lbl_package_5))){
                    hashMapPackage5.putAll(hashMap);
                    AppConstant.hashMapPackage5.putAll(hashMap);

                }
            }
        });

        alertDialog.show();
    }

    private void showPackageDetails(TextView textView,HashMap hashMap){
        if(strPackageType != null &&strPackageType.equalsIgnoreCase("YOUR_PACKAGING")||(strSize != null && strSize.equalsIgnoreCase("LARGE"))||(strPackageType != null && strPackageType.equalsIgnoreCase("30"))||(strPackageType != null && strPackageType.equalsIgnoreCase("02"))){
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

                dataMapOfSpecialServices = new ArrayList<>();
                arrayListCheckedServices = new ArrayList<>();
                arrayListSpecialServices = new ArrayList<>();

                strPackageType="";
                //strContainerType="";
                //strSize="";
                strServiceType = "";
                strServiceTypeDesc = "";
                strWeightUnit = "";
                strDimensionalUnit ="";
                //strCurrencyUnit ="";

                checkbox_shapes.setChecked(false);
                checkbox_sizes.setChecked(false);

                strCarrierCode = arrayListCarriers.get(position).getCr_code();
                if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")){
                    // spinner_service_type.setVisibility(View.GONE);
                    // tv_service_type.setVisibility(View.GONE);

                    /*tv_container_type.setVisibility(View.GONE);
                    spinner_container_type.setVisibility(View.GONE);

                    tv_sizes.setVisibility(View.GONE);
                    spinner_sizes.setVisibility(View.GONE);*/

                    tv_pickup_drop.setVisibility(View.VISIBLE);
                    spinner_pickup_drop.setVisibility(View.VISIBLE);

                    tv_no_of_packages.setVisibility(View.VISIBLE);
                    ll_no_of_packages.setVisibility(View.VISIBLE);

                    //showProgressDialog();
                    getServiceTypes();
                    //getDropOff();

                }else if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("USPS")){

                    tv_pickup_drop.setVisibility(View.GONE);
                    spinner_pickup_drop.setVisibility(View.GONE);

                  /*  tv_container_type.setVisibility(View.VISIBLE);
                    spinner_container_type.setVisibility(View.VISIBLE);

                    tv_sizes.setVisibility(View.VISIBLE);
                    spinner_sizes.setVisibility(View.VISIBLE);*/

                   /* ll_sizes.setVisibility(View.VISIBLE);
                    ll_shapes.setVisibility(View.GONE);*/

                    spinner_service_type.setVisibility(View.VISIBLE);
                    tv_service_type.setVisibility(View.VISIBLE);

                    tv_no_of_packages.setVisibility(View.GONE);
                    ll_no_of_packages.setVisibility(View.GONE);

                    strTextViewNo = "1";

                    getServiceTypes();

                }else {
                    spinner_service_type.setVisibility(View.VISIBLE);
                    tv_service_type.setVisibility(View.VISIBLE);

                    tv_pickup_drop.setVisibility(View.VISIBLE);
                    spinner_pickup_drop.setVisibility(View.VISIBLE);

                    ll_sizes.setVisibility(View.GONE);
                    ll_shapes.setVisibility(View.GONE);


                   /* tv_container_type.setVisibility(View.GONE);
                    spinner_container_type.setVisibility(View.GONE);

                    tv_sizes.setVisibility(View.GONE);
                    spinner_sizes.setVisibility(View.GONE);*/

                    tv_no_of_packages.setVisibility(View.VISIBLE);
                    ll_no_of_packages.setVisibility(View.VISIBLE);

                    getServiceTypes();
                }

                break;

            case R.id.spinner_service_type:
                //11Dec strServiceType = arrayListServiceTypes.get(position).getServiceType();
                //Toast.makeText(ShipmentDetailsAssociateActivity.this,spinner_carrier.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();
                strServiceType = arrayListServiceTypes.get(position).getServiceType();
                strServiceTypeDesc = arrayListServiceTypes.get(position).getServiceText();
                Log.e("Harshada#","=>"+strServiceTypeDesc);


                if(strServiceTypeDesc != null && strServiceTypeDesc.contains("Freight")){
                    tv_no_of_packages.setText(getResources().getString(R.string.hint_no_of_skid));
                    cb_identical.setVisibility(View.GONE);
                }else {
                    tv_no_of_packages.setText(getResources().getString(R.string.lbl_no_of_packages));
                    cb_identical.setVisibility(View.VISIBLE);
                }

                if(progressBar!=null&&progressBar.isShowing()){
                    // progressBar.dismiss();
                    closeProgressBar();
                }

                showProgressDialog();
                getPackageType();

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

                if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("USPS") && strPackageType!=null&&strPackageType.equalsIgnoreCase("PACKAGE SERVICE")){

                    ll_sizes.setVisibility(View.VISIBLE);
                    ll_shapes.setVisibility(View.GONE);
                    strTextViewNo = "1";


                }else {

                    ll_sizes.setVisibility(View.GONE);
                    ll_shapes.setVisibility(View.GONE);
                }

                break;

            case R.id.spinner_container_type:
                //strPackageType = spinner_package_type.getItemAtPosition(position).toString();
                //strPackageType = packageTypeList.get(position).toLowerCase();

                strShape = containerTypeList.get(position);
                break;

            case R.id.spinner_sizes:
                //strPackageType = spinner_package_type.getItemAtPosition(position).toString();
                //strPackageType = packageTypeList.get(position).toLowerCase();
                strSize = sizesList.get(position);
                break;

            case R.id.spinner_shipment_purpose:
                strShipmentPurpose = arrayListOfShipmentPurposes.get(position).getShipping_purpose();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override public void onStop() {
        super.onStop();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null; }
    }

    private void getSpecialServices(){
        progressBar = new ProgressDialog(ShipmentDetailsAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_get_special_services);

        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.CR_CARRIER_CODE,strCarrierCode);
        params.put(JSONConstant.TO_A_COUNTRY_CODE, AppConstant.T_A_COUNTRY_CODE);
        params.put(JSONConstant.FROM_A_COUNTRY_CODE, AppConstant.F_A_COUNTRY_CODE);
        params.put("request_type","check_rates");
        params.put(JSONConstant.SERVICE_TYPE,strServiceType);
        if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("FedEX")) {
            params.put("hold_at_fedex_location", "true");
        }

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(ShipmentDetailsAssociateActivity.this, URL,params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                //progressBar.dismiss();
                closeProgressBar();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());
                strSpecialServicesResponse = response.toString();
                showSpecialServices("Special Services",response.toString());
                // progressBar.dismiss();
                closeProgressBar();
            }
        });
    }




    // Custom Dialog
    private void showSpecialServices(final String titleShow,final String response){
        Button btn_ok,btn_cancel;
        final TextView tv_title;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShipmentDetailsAssociateActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_for_services, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        Window window = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        btn_ok = (Button) dialogView. findViewById(R.id.btn_ok);

        final LinearLayout viewProductLayout = (LinearLayout)dialogView. findViewById(R.id.customOptionLL);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        tv_title.setText(titleShow);

        rg = new RadioGroup(ShipmentDetailsAssociateActivity.this); //create the RadioGroup
        rb = new RadioButton(ShipmentDetailsAssociateActivity.this);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                strSelectedRadioButton = radioButton.getTag().toString();
            }
        });

        params1.topMargin = 3;
        params1.bottomMargin = 3;

        params.topMargin = 3;
        params.bottomMargin = 3;
        params.leftMargin = 20;

        arrayListSpecialServices = new ArrayList<>();


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataMapOfSpecialServices = new ArrayList<HashMap<String, String>>();
                System.out.println("checked==>"+arrayListCheckedServices.toString());
                System.out.println("special==>"+arrayListSpecialServices.toString());

                System.out.println("Hashmap data==>"+hashMapForSavingState.toString());
                try {


                    for (int k=0 ; k<arrayListCheckedServices.size();k++){
                        for (int i = 0 ; i < arrayListSpecialServices.size();i++){

                            /*if(arrayListCheckedServices.get(k).equalsIgnoreCase(arrayListSpecialServices.get(i).getSpecial_service_description())) {
                                ArrayList<SpecialServicesOptions> optionses = arrayListSpecialServices.get(i).getSpecialServicesOptionses();

                                if (optionses.size() > 0) {
                                    for (int j = 0; j < optionses.size(); j++) {
                                        if (strSelectedRadioButton != null && strSelectedRadioButton.equalsIgnoreCase(optionses.get(j).getSignature_option_description())) {
                                            hashMapForSpecialServices = new HashMap<String, String>();
                                            hashMapForSpecialServices.put("special_service", arrayListSpecialServices.get(i).getSpecial_service());
                                            hashMapForSpecialServices.put("special_service_description", arrayListSpecialServices.get(i).getSpecial_service_description());
                                            hashMapForSpecialServices.put("selected_option", optionses.get(j).getSignature_option());
                                            hashMapForSpecialServices.put("selected_option_description", optionses.get(j).getSignature_option_description());
                                            dataMapOfSpecialServices.add(hashMapForSpecialServices);
                                        }
                                    }
                                } else {
                                    hashMapForSpecialServices = new HashMap<String, String>();
                                    hashMapForSpecialServices.put("special_service", arrayListSpecialServices.get(i).getSpecial_service());
                                    hashMapForSpecialServices.put("special_service_description", arrayListSpecialServices.get(i).getSpecial_service_description());
                                    hashMapForSpecialServices.put("selected_option", " ");
                                    hashMapForSpecialServices.put("selected_option_description", " ");
                                    dataMapOfSpecialServices.add(hashMapForSpecialServices);

                                }
                            }*/

                          /* if(arrayListCheckedServices.get(k).equalsIgnoreCase(arrayListSpecialServices.get(i).getSpecial_service_description())&&arrayListSpecialServices.get(i).getType().equalsIgnoreCase("radio")){


                                ArrayList<SpecialServicesOptions> optionses = arrayListSpecialServices.get(i).getSpecialServicesOptionses();
                                for (int j = 0; j < optionses.size(); j++) {
                                    if (strSelectedRadioButton != null && strSelectedRadioButton.equalsIgnoreCase(optionses.get(j).getSignature_option_description())) {
                                        hashMapForSpecialServices = new HashMap<String, String>();
                                        hashMapForSpecialServices.put("special_service",arrayListSpecialServices.get(i).getSpecial_service());
                                        hashMapForSpecialServices.put("special_service_description",arrayListSpecialServices.get(i).getSpecial_service_description());
                                        hashMapForSpecialServices.put("selected_option",optionses.get(j).getSignature_option());
                                        hashMapForSpecialServices.put("selected_option_description",optionses.get(j).getSignature_option_description());
                                        dataMapOfSpecialServices.add(hashMapForSpecialServices);
                                    }
                                }
                            }else if(arrayListCheckedServices.get(k).equalsIgnoreCase(arrayListSpecialServices.get(i).getSpecial_service_description())&&!arrayListSpecialServices.get(i).getType().equalsIgnoreCase("radio")){
                                hashMapForSpecialServices = new HashMap<String, String>();
                                hashMapForSpecialServices.put("special_service",arrayListSpecialServices.get(i).getSpecial_service());
                                hashMapForSpecialServices.put("special_service_description",arrayListSpecialServices.get(i).getSpecial_service_description());
                                hashMapForSpecialServices.put("selected_option"," ");
                                hashMapForSpecialServices.put("selected_option_description"," ");
                                dataMapOfSpecialServices.add(hashMapForSpecialServices);

                            }*/


                            if(arrayListCheckedServices.get(k).equalsIgnoreCase(arrayListSpecialServices.get(i).getSpecial_service_description())){


                                ArrayList<SpecialServicesOptions> optionses = arrayListSpecialServices.get(i).getSpecialServicesOptionses();
                                System.out.println("Harshada : "+optionses.toString());
                                if(optionses.size()>0) {
                                    for (int j = 0; j < optionses.size(); j++) {
                                        if (strSelectedRadioButton != null && strSelectedRadioButton.equalsIgnoreCase(optionses.get(j).getSignature_option_description())) {
                                            hashMapForSpecialServices = new HashMap<String, String>();
                                            hashMapForSpecialServices.put("special_service", arrayListSpecialServices.get(i).getSpecial_service());
                                            hashMapForSpecialServices.put("special_service_description", arrayListSpecialServices.get(i).getSpecial_service_description());
                                            hashMapForSpecialServices.put("selected_option", optionses.get(j).getSignature_option());
                                            hashMapForSpecialServices.put("selected_option_description", optionses.get(j).getSignature_option_description());
                                            dataMapOfSpecialServices.add(hashMapForSpecialServices);
                                        }
                                    }
                                }else {
                                    hashMapForSpecialServices = new HashMap<String, String>();
                                    hashMapForSpecialServices.put("special_service",arrayListSpecialServices.get(i).getSpecial_service());
                                    hashMapForSpecialServices.put("special_service_description",arrayListSpecialServices.get(i).getSpecial_service_description());
                                    hashMapForSpecialServices.put("selected_option"," ");
                                    hashMapForSpecialServices.put("selected_option_description"," ");
                                    dataMapOfSpecialServices.add(hashMapForSpecialServices);
                                }
                            }
                           /* else if(arrayListCheckedServices.get(k).equalsIgnoreCase(arrayListSpecialServices.get(i).getSpecial_service_description())&&!arrayListSpecialServices.get(i).getType().equalsIgnoreCase("radio")){
                                hashMapForSpecialServices = new HashMap<String, String>();
                                hashMapForSpecialServices.put("special_service",arrayListSpecialServices.get(i).getSpecial_service());
                                hashMapForSpecialServices.put("special_service_description",arrayListSpecialServices.get(i).getSpecial_service_description());
                                hashMapForSpecialServices.put("selected_option"," ");
                                hashMapForSpecialServices.put("selected_option_description"," ");
                                dataMapOfSpecialServices.add(hashMapForSpecialServices);

                            }*/
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                alertDialog.dismiss();
            }
        });


        try {
            jsonObject = new JSONObject(response);
            JSONArray customOptnList = jsonObject.getJSONArray("special_services");

            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);

                SpecialService specialService = new SpecialService();
                specialService.setType(eachData.getString("type"));
                specialService.setSpecial_service(eachData.getString("special_service"));
                specialService.setSpecial_service_description(eachData.getString("special_service_description"));
                ArrayList<SpecialServicesOptions> optionses = new ArrayList<>();

                specialService.setSpecialServicesOptionses(optionses);

                final JSONArray radioButtonJSONOpt = eachData.getJSONArray("options");

                // if (eachData.getString("type").equals("checkbox")) {
                if (radioButtonJSONOpt.length()<=0) {
                    arrayListSpecialServices.add(specialService);

                } else{
                    //final JSONArray radioButtonJSONOpt = eachData.getJSONArray("options");
                    arrayListFalse = new ArrayList<>();
                    arrayListTrue = new ArrayList<>();
                    optionses = new ArrayList<>();

                    specialService = new SpecialService();
                    specialService.setType(eachData.getString("type"));
                    specialService.setSpecial_service(eachData.getString("special_service"));
                    specialService.setSpecial_service_description(eachData.getString("special_service_description"));

                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {
                        SpecialServicesOptions specialServicesOptions = new SpecialServicesOptions();
                        specialServicesOptions.setIs_selected(false);
                        specialServicesOptions.setSignature_option(radioButtonJSONOpt.getJSONObject(j).getString("signature_option"));
                        specialServicesOptions.setSignature_option_description(radioButtonJSONOpt.getJSONObject(j).getString("signature_option_description"));
                        if (strCarrierCode != null && strCarrierCode.equalsIgnoreCase("FedEX")) {
                            if (radioButtonJSONOpt.getJSONObject(j).getString("hold_at_fedex_location").equalsIgnoreCase("true")) {
                                arrayListTrue.add(radioButtonJSONOpt.getJSONObject(j).getString("signature_option_description"));
                            }
                        }

                        arrayListFalse.add(radioButtonJSONOpt.getJSONObject(j).getString("signature_option_description"));
                        optionses.add(specialServicesOptions);
                    }

                    specialService.setSpecialServicesOptionses(optionses);
                    arrayListSpecialServices.add(specialService);
                }

                CheckBox chk = new CheckBox(ShipmentDetailsAssociateActivity.this);
                chk.setTag(eachData.getString("special_service_description"));
                String optionString1 = eachData.getString("special_service_description");
                chk.setText(optionString1);
                viewProductLayout.addView(chk, params1);

                chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String variant_name = buttonView.getTag().toString();

                        switch (buttonView.getTag().toString()){
                            case "Hold at Fedex Location":

                                if(isChecked){
                                    if (!arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.add(variant_name);
                                    }
                                    hashMapForSavingState.put(variant_name,true);

                                    if(hashMapForSavingState.containsKey("Signature Option")&&hashMapForSavingState.get("Signature Option")){
                                        rg.removeAllViews();
                                        for (int j = 0; j < arrayListTrue.size(); j++) {
                                            rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                            rg.addView(rb, params);
                                            rb.setLayoutParams(params);
                                            rb.setTag(arrayListTrue.get(j).toString());
                                            rb.setText(arrayListTrue.get(j).toString());

                                        }
                                        viewProductLayout.removeView(rg);
                                        viewProductLayout.addView(rg, params);
                                    }
                                }else {

                                    if (arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.remove(variant_name);
                                    }
                                    hashMapForSavingState.put(variant_name,false);

                                    if(hashMapForSavingState.containsKey("Signature Option")&&hashMapForSavingState.get("Signature Option")){

                                        rg.removeAllViews();
                                        for (int j = 0; j < arrayListFalse.size(); j++) {
                                            rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                            rg.addView(rb, params);
                                            rb.setLayoutParams(params);
                                            rb.setTag(arrayListFalse.get(j).toString());
                                            rb.setText(arrayListFalse.get(j).toString());
                                        }
                                        viewProductLayout.removeView(rg);
                                        viewProductLayout.addView(rg, params);
                                    }
                                }

                                break;

                            case "Signature Option":
                                if(isChecked){
                                    rg.removeAllViews();

                                    if (!arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.add(variant_name);
                                    }

                                    hashMapForSavingState.put(variant_name,true);
                                    if(hashMapForSavingState.containsKey("Hold at Fedex Location")&&hashMapForSavingState.get("Hold at Fedex Location")){
                                        rg.removeAllViews();

                                        for (int j = 0; j < arrayListTrue.size(); j++) {
                                            rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                            rg.addView(rb, params);
                                            rb.setLayoutParams(params);
                                            rb.setTag(arrayListTrue.get(j).toString());
                                            rb.setText(arrayListTrue.get(j).toString());

                                        }
                                        viewProductLayout.removeView(rg);
                                        viewProductLayout.addView(rg, params);
                                    } else {
                                        rg.removeAllViews();
                                        for (int j = 0; j < arrayListFalse.size(); j++) {
                                            rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                            rg.addView(rb, params);
                                            rb.setLayoutParams(params);
                                            rb.setTag(arrayListFalse.get(j).toString());
                                            rb.setText(arrayListFalse.get(j).toString());
                                        }
                                        viewProductLayout.removeView(rg);
                                        viewProductLayout.addView(rg, params);
                                    }
                                }else {
                                    strSelectedRadioButton = "";

                                    hashMapForSavingState.put(variant_name,false);
                                    if (arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.remove(variant_name);
                                    }
                                    rg.removeAllViews();
                                    viewProductLayout.removeView(rg);
                                    //viewProductLayout.addView(rg, params);
                                }
                                break;

                            case "Signature Options":
                                if(isChecked) {
                                    // rg.removeAllViews();

                                    if (!arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.add(variant_name);
                                    }

                                    hashMapForSavingState.put(variant_name, true);
                                    rg.removeAllViews();
                                    for (int j = 0; j < arrayListFalse.size(); j++) {
                                        rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                        rg.addView(rb, params);
                                        rb.setLayoutParams(params);
                                        rb.setTag(arrayListFalse.get(j).toString());
                                        rb.setText(arrayListFalse.get(j).toString());
                                    }
                                    viewProductLayout.removeView(rg);
                                    viewProductLayout.addView(rg, params);
                                }else {
                                    strSelectedRadioButton = "";
                                    hashMapForSavingState.put(variant_name,false);
                                    if (arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.remove(variant_name);
                                    }
                                    rg.removeAllViews();
                                    viewProductLayout.removeView(rg);
                                    viewProductLayout.addView(rg, params);
                                }
                                break;

                            case "Signature Services":
                                if(isChecked) {
                                    //rg.removeAllViews();

                                    if (!arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.add(variant_name);
                                    }

                                    hashMapForSavingState.put(variant_name, true);
                                    rg.removeAllViews();
                                    for (int j = 0; j < arrayListFalse.size(); j++) {
                                        rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                        rg.addView(rb, params);
                                        rb.setLayoutParams(params);
                                        rb.setTag(arrayListFalse.get(j).toString());
                                        rb.setText(arrayListFalse.get(j).toString());
                                    }
                                    viewProductLayout.removeView(rg);
                                    viewProductLayout.addView(rg, params);
                                }else {
                                    strSelectedRadioButton = "";
                                    hashMapForSavingState.put(variant_name,false);
                                    if (arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.remove(variant_name);
                                    }

                                    rg.removeAllViews();
                                    viewProductLayout.removeView(rg);
                                    //viewProductLayout.addView(rg, params);
                                }
                                break;

                            default:
                                if (isChecked) {
                                    if (!arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.add(variant_name);
                                    }
                                    hashMapForSavingState.put(variant_name,true);
                                } else {
                                    if (arrayListCheckedServices.contains(variant_name)) {
                                        arrayListCheckedServices.remove(variant_name);
                                    }
                                    hashMapForSavingState.put(variant_name,false);
                                }
                        }
                    }
                });
            }

           /* if(arrayListCheckedServices.size()<=0){
                rg.removeAllViews();
                for (int j = 0; j < arrayListFalse.size(); j++) {
                    rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                    rg.addView(rb, params);
                    rb.setLayoutParams(params);
                    rb.setTag(arrayListFalse.get(j).toString());
                    rb.setText(arrayListFalse.get(j).toString());
                }
                viewProductLayout.addView(rg, params);
                rg.removeAllViews();
            }*/

            if(dataMapOfSpecialServices.size()>0){
                viewProductLayout.removeAllViewsInLayout();
                //final ArrayList<String> arrayListTemp = new ArrayList<>();


                for(int i = 0 ; i<dataMapOfPackages.size();i++){
                    HashMap<String, String> hashMap = dataMapOfPackages.get(i);
                    System.out.println("Hashmap "+ hashMap.toString());
                   /* arrayListCheckedServices = new ArrayList<>();
                    arrayListCheckedServices.add(hashMap.get("special_service_description"));
*/
                    if(hashMap.get("special_service_description") != null&&!arrayListCheckedServices.contains(hashMap.get("special_service_description"))) {
                        arrayListCheckedServices.add(hashMap.get("special_service_description"));
                    }

                    //arrayListTemp.add(hashMap.get(hashMap.get("special_service_description")));
                }

                for(int i = 0; i<arrayListSpecialServices.size();i++){
                    CheckBox chk = new CheckBox(ShipmentDetailsAssociateActivity.this);
                    chk.setTag(arrayListSpecialServices.get(i).getSpecial_service_description());
                    String optionString1 = arrayListSpecialServices.get(i).getSpecial_service_description();
                    chk.setText(optionString1);

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            String variant_name = buttonView.getTag().toString();

                            switch (buttonView.getTag().toString()){
                                case "Hold at Fedex Location":

                                    if(isChecked){
                                        if (!arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.add(variant_name);
                                        }


                                        hashMapForSavingState.put(variant_name,true);

                                        if(hashMapForSavingState.containsKey("Signature Option")&&hashMapForSavingState.get("Signature Option")){
                                            rg.removeAllViews();
                                            for (int j = 0; j < arrayListTrue.size(); j++) {
                                                rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                                rg.addView(rb, params);
                                                rb.setLayoutParams(params);
                                                rb.setTag(arrayListTrue.get(j).toString());
                                                rb.setText(arrayListTrue.get(j).toString());

                                            }
                                            viewProductLayout.removeView(rg);
                                            viewProductLayout.addView(rg, params);
                                        }
                                        // added at 8th Feb 2018
                                        else {
                                            rg.removeAllViews();
                                            viewProductLayout.removeView(rg);
                                            //viewProductLayout.addView(rg, params);
                                            //viewProductLayout.updateViewLayout(rg, params);
                                        }
                                    }else {

                                        if (arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.remove(variant_name);
                                        }

                                        hashMapForSavingState.put(variant_name,false);

                                        if(hashMapForSavingState.containsKey("Signature Option")&&hashMapForSavingState.get("Signature Option")){

                                            rg.removeAllViews();
                                            for (int j = 0; j < arrayListFalse.size(); j++) {
                                                rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                                rg.addView(rb, params);
                                                rb.setLayoutParams(params);
                                                rb.setTag(arrayListFalse.get(j).toString());
                                                rb.setText(arrayListFalse.get(j).toString());
                                            }
                                            viewProductLayout.removeView(rg);
                                            viewProductLayout.addView(rg, params);
                                        }
                                    }

                                    break;

                                case "Signature Option":
                                    if(isChecked){
                                        rg.removeAllViews();

                                        if (!arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.add(variant_name);
                                        }

                                        hashMapForSavingState.put(variant_name,true);
                                        if(hashMapForSavingState.containsKey("Hold at Fedex Location")&&hashMapForSavingState.get("Hold at Fedex Location")){
                                            rg.removeAllViews();

                                            for (int j = 0; j < arrayListTrue.size(); j++) {
                                                rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                                rg.addView(rb, params);
                                                rb.setLayoutParams(params);
                                                rb.setTag(arrayListTrue.get(j).toString());
                                                rb.setText(arrayListTrue.get(j).toString());

                                            }
                                            viewProductLayout.removeView(rg);
                                            viewProductLayout.addView(rg, params);
                                        } else {
                                            rg.removeAllViews();
                                            for (int j = 0; j < arrayListFalse.size(); j++) {
                                                rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                                rg.addView(rb, params);
                                                rb.setLayoutParams(params);
                                                rb.setTag(arrayListFalse.get(j).toString());
                                                rb.setText(arrayListFalse.get(j).toString());
                                            }
                                            viewProductLayout.removeView(rg);
                                            viewProductLayout.addView(rg, params);
                                        }
                                    }else {
                                        strSelectedRadioButton = "";
                                        hashMapForSavingState.put(variant_name,false);
                                        if (arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.remove(variant_name);
                                        }

                                        rg.removeAllViews();
                                        viewProductLayout.removeView(rg);
                                        // viewProductLayout.addView(rg, params);
                                    }
                                    break;

                                case "Signature Options":
                                    if(isChecked) {
                                        //rg.removeAllViews();

                                        if (!arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.add(variant_name);
                                        }

                                        hashMapForSavingState.put(variant_name, true);
                                        rg.removeAllViews();
                                        for (int j = 0; j < arrayListFalse.size(); j++) {
                                            rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                            rg.addView(rb, params);
                                            rb.setLayoutParams(params);
                                            rb.setTag(arrayListFalse.get(j).toString());
                                            rb.setText(arrayListFalse.get(j).toString());
                                        }
                                        viewProductLayout.removeView(rg);
                                        viewProductLayout.addView(rg, params);
                                    }else {
                                        strSelectedRadioButton = "";
                                        hashMapForSavingState.put(variant_name,false);
                                        if (arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.remove(variant_name);
                                        }

                                        rg.removeAllViews();
                                        viewProductLayout.removeView(rg);
                                        //viewProductLayout.addView(rg, params);
                                    }
                                    break;


                                case "Signature Services":
                                    if(isChecked) {
                                        //rg.removeAllViews();

                                        if (!arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.add(variant_name);
                                        }

                                        hashMapForSavingState.put(variant_name, true);
                                        rg.removeAllViews();
                                        for (int j = 0; j < arrayListFalse.size(); j++) {
                                            rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                            rg.addView(rb, params);
                                            rb.setLayoutParams(params);
                                            rb.setTag(arrayListFalse.get(j).toString());
                                            rb.setText(arrayListFalse.get(j).toString());
                                        }
                                        viewProductLayout.removeView(rg);
                                        viewProductLayout.addView(rg, params);
                                    }else {
                                        strSelectedRadioButton = "";
                                        hashMapForSavingState.put(variant_name,false);
                                        if (arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.remove(variant_name);
                                        }

                                        rg.removeAllViews();
                                        viewProductLayout.removeView(rg);
                                        //viewProductLayout.addView(rg, params);
                                    }
                                    break;

                                default:
                                    if (isChecked) {
                                        if (!arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.add(variant_name);
                                        }
                                        hashMapForSavingState.put(variant_name,true);
                                    } else {
                                        if (arrayListCheckedServices.contains(variant_name)) {
                                            arrayListCheckedServices.remove(variant_name);
                                        }
                                        hashMapForSavingState.put(variant_name,false);
                                    }
                            }

                        }
                    });
                    // arrayListTemp

                    if(arrayListCheckedServices.contains(arrayListSpecialServices.get(i).getSpecial_service_description())){
                        //if(arrayListTemp.contains(arrayListSpecialServices.get(i).getSpecial_service_description())){
                        chk.setChecked(true);
                        if(arrayListSpecialServices.get(i).getSpecial_service_description().equalsIgnoreCase("Signature Option")){
                            // true

                            if(arrayListCheckedServices.contains("Hold at Fedex Location")) {
                                //if(arrayListTemp.contains("Hold at Fedex Location")) {

                                rg.removeAllViews();
                                for (int j = 0; j < arrayListTrue.size(); j++) {
                                    rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                    rg.addView(rb, params);
                                    rb.setLayoutParams(params);
                                    rb.setTag(arrayListTrue.get(j).toString());
                                    rb.setText(arrayListTrue.get(j).toString());
                                    if (arrayListTrue.get(j).equalsIgnoreCase(strSelectedRadioButton)) {
                                        rb.setChecked(true);
                                    }
                                }
                            }else {

                                //false
                                rg.removeAllViews();
                                for (int j = 0; j < arrayListFalse.size(); j++) {
                                    rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                    rg.addView(rb, params);
                                    rb.setLayoutParams(params);
                                    rb.setTag(arrayListFalse.get(j).toString());
                                    rb.setText(arrayListFalse.get(j).toString());
                                    if(arrayListFalse.get(j).equalsIgnoreCase(strSelectedRadioButton)){
                                        rb.setChecked(true);
                                    }
                                }
                            }
                        } else if(arrayListSpecialServices.get(i).getSpecial_service_description().equalsIgnoreCase("Hold at Fedex Location")&&!arrayListCheckedServices.contains("Signature Option")){
                            //else if(arrayListSpecialServices.get(i).getSpecial_service_description().equalsIgnoreCase("Hold at Fedex Location")&&!arrayListTemp.contains("Signature Option")){
                            rg.removeAllViews();
                            for (int j = 0; j < arrayListTrue.size(); j++) {
                                rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                rg.addView(rb, params);
                                rb.setLayoutParams(params);
                                rb.setTag(arrayListTrue.get(j).toString());
                                rb.setText(arrayListTrue.get(j).toString());
                                if(arrayListTrue.get(j).equalsIgnoreCase(strSelectedRadioButton)){
                                    rb.setChecked(true);
                                }

                            }
                            //rg.removeAllViews();
                        } else if(arrayListSpecialServices.get(i).getSpecial_service_description().equalsIgnoreCase("Signature Options")){
                            rg.removeAllViews();
                            for (int j = 0; j < arrayListFalse.size(); j++) {
                                rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                rg.addView(rb, params);
                                rb.setLayoutParams(params);
                                rb.setTag(arrayListFalse.get(j).toString());
                                rb.setText(arrayListFalse.get(j).toString());
                                if(arrayListFalse.get(j).equalsIgnoreCase(strSelectedRadioButton)){
                                    rb.setChecked(true);
                                }
                            }
                            //rg.removeAllViews();
                        }else {
                            rg.removeAllViews();
                            for (int j = 0; j < arrayListFalse.size(); j++) {
                                rb = new RadioButton(ShipmentDetailsAssociateActivity.this);
                                rg.addView(rb, params);
                                rb.setLayoutParams(params);
                                rb.setTag(arrayListFalse.get(j).toString());
                                rb.setText(arrayListFalse.get(j).toString());
                                if(arrayListFalse.get(j).equalsIgnoreCase(strSelectedRadioButton)){
                                    rb.setChecked(true);
                                }
                            }
                        }
                    }else {
                        chk.setChecked(false);
                    }

                    viewProductLayout.addView(chk, params1);
                    viewProductLayout.removeView(rg);
                    viewProductLayout.addView(rg,params);
                }
            }
            Log.e("Harshada","=>"+arrayListSpecialServices.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        alertDialog.show();
    }

    private void closeProgressBar(){
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
    }

    private void showDesclaimer(Context context){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.custom_dialog_logout, null);
        alertDialog.setView(convertView);

        TextView popup_title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView popup_message = (TextView) convertView.findViewById(R.id.tv_message);

        Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);

        final android.app.AlertDialog dialog = alertDialog.create();
        popup_title.setVisibility(View.VISIBLE);
        popup_title.setText(getResources().getString(R.string.lbl_disclaimer));

        popup_message.setText(getResources().getString(R.string.info));
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
                if(rb_products_commodities.isChecked()){
                    String strWeightUnit="";
                    String strValueUnit= "";
                    double totalWeight = 0;;
                    double totalCustomValue = 0;
                    for(int i=0;i<dataMapOfPackages.size();i++){
                        HashMap<String, String> hashMap = dataMapOfPackages.get(i);
                        if(cb_identical.isChecked()){
                            totalWeight = totalWeight+(Double.valueOf(hashMap.get(AppConstant.HM_WEIGHT))*(Double.valueOf(strTextViewNo)));
                            totalCustomValue = totalCustomValue+(Double.valueOf(hashMap.get(AppConstant.HM_DECLARED_VALUE))*(Double.valueOf(strTextViewNo)));
                        }else {
                            if(hashMap.get(AppConstant.HM_HANDLING_UNITS).equalsIgnoreCase("")){
                                totalWeight = totalWeight+Double.valueOf(hashMap.get(AppConstant.HM_WEIGHT));
                                totalCustomValue = totalCustomValue+Double.valueOf(hashMap.get(AppConstant.HM_DECLARED_VALUE));
                            }else {
                                totalWeight = totalWeight+(Double.valueOf(hashMap.get(AppConstant.HM_WEIGHT))*(Double.valueOf(hashMap.get(AppConstant.HM_HANDLING_UNITS))));
                                totalCustomValue = totalCustomValue+(Double.valueOf(hashMap.get(AppConstant.HM_DECLARED_VALUE))*(Double.valueOf(hashMap.get(AppConstant.HM_HANDLING_UNITS))));
                            }

                        }
                        strValueUnit = hashMap.get(AppConstant.HM_CURRENCY_UNIT);
                        strWeightUnit = hashMap.get(AppConstant.HM_WEIGHT_UNIT);

                    }

                    mSessionManager.putStringData(SessionManager.KEY_TOTAL_SHIPMENT_WEIGHT,String.valueOf(totalWeight));
                    mSessionManager.putStringData(SessionManager.KEY_TOTAL_SHIPMENT_WEIGHT_UNIT,strWeightUnit);
                    mSessionManager.putStringData(SessionManager.KEY_TOTAL_CARRIAGE_VALUE,String.valueOf(totalCustomValue));
                    mSessionManager.putStringData(SessionManager.KEY_TOTAL_CARRIAGE_VALUE_UNIT,strValueUnit);

                    startActivity(new Intent(ShipmentDetailsAssociateActivity.this,CommodityDetailsAssociateActivity.class));


                }else{
                    getCheckRatesDetails();
                }
                /*if(cd.isConnectingToInternet()){
                    validateAndNext();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentDetailsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }*/

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

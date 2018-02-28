package com.example.aspl.fortunecourier.utility;

import com.example.aspl.fortunecourier.model.SpecialService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aspl on 10/11/17.
 */

public class AppConstant {

    public static final String SIGNUP_OR_FORGOT_OTP = "SIGNUP_OR_FORGOT_OTP";
    public static final String SIGNUP_OTP = "SIGNUP_OTP";
    public static final String FORGOT_OTP = "FORGOT_OTP";
    public static final String CHANGE_PHONE_OTP = "CHANGE_PHONE_OTP";
    public static final String VERIFY_PINCODE_OTP = "VERIFY_PINCODE_OTP";


    public static boolean IS_USER_FROM_FACEBOOK_SIGNUP = false;

    // Constant For Shipment Details

    public static  String SHIPMENT_DATE = "SHIPMENT_DATE";

    public static String F_C_COUNTRY_CODE = "from_c_country_code";
    public static  String F_C_STATE = "from_c_state";
    public static  String F_C_ZIPCODE = "from_c_zipcode";
    public static final String F_C_ADDRESS_LINE1 = "from_c_address_line1";
    public static  String F_C_CITY = "from_c_city";
    public static  String T_C_COUNTRY_CODE = "to_c_country_code";
    public static  String T_C_STATE = "to_c_state";
    public static  String T_C_ZIPCODE = "to_c_zipcode";
    public static final String T_C_ADDRESS_LINE1 = "to_c_address_line1";
    public static  String T_C_CITY = "to_c_city";
    public static  String TOTALINSUREDVALUE = "TotalInsuredValue";
    public static  String PACKAGECOUNT = "PackageCount";
    public static  String PACKAGEWEIGHT = "PackageWeight";
    public static  String SERVICESHIPDATE = "serviceshipdate";

    public static String F_A_COUNTRY_CODE = "from_a_country_code";
    public static  String F_A_ZIPCODE = "from_a_zipcode";
    public static String F_A_CITY = "from_a_city";
    public static  String F_A_STATE = "from_a_state";

    public static  String T_A_COUNTRY_CODE = "to_a_country_code";
    public static  String T_A_ZIPCODE = "to_a_zipcode";

    public static  String T_A_CITY = "to_a_city";
    public static  String T_A_STATE = "to_a_state";

    public static  String HM_PACKAGE_NO = "HM_PACKAGE_NO";
    public static  String HM_WEIGHT = "PackageWeight";
    public static  String HM_WEIGHT_UNIT = "WeightUnit";
    public static  String HM_CURRENCY_UNIT = "CurrencyUnit";
    public static  String HM_DECLARED_VALUE = "DesiredValue";
    public static  String HM_LENGTH = "Length";
    public static  String HM_WIDTH = "Width";
    public static  String HM_HEIGHT = "Height";
    public static  String HM_DIMENSIONAL_UNIT = "DimensionUnit";
    public static  String HM_PACKAGE_DESC = "PackageDetails";
    public static  String HM_GIRTH = "Girth";
    public static  String HM_HANDLING_UNITS = "HandlingUnits";
    public static  String APPLIED_COUPON_CODE="";


    public static String SPINNER_TITLE = "Select Country";
    //public static boolean IS_FROM_CALCULATE_RATES = false;
    public static boolean IS_FROM_CREATE_SHIPMENT = false;
    public static boolean IS_CUSTOMER_LOG_IN = false;
    public static boolean IS_FROM_OUTSIDE = false;

    public static HashMap<String,String> hashMapPackage1 = new HashMap<>();
    public static HashMap<String,String> hashMapPackage2 = new HashMap<>();
    public static HashMap<String,String> hashMapPackage3 = new HashMap<>();
    public static HashMap<String,String> hashMapPackage4 = new HashMap<>();
    public static HashMap<String,String> hashMapPackage5 = new HashMap<>();

    public static String SERVICE_TYPE_FOR_UPS = "SERVICE_TYPE_FOR_UPS";
    public static String EXPECTED_DELIVERY_TIMESTAMP_FOR_UPS = "EXPECTED_DELIVERY_DATE_FOR_UPS";
    public static String SERVICE_TYPE_DESCR_FOR_UPS = "SERVICE_TYPE_DESCR_FOR_UPS";

    //public static ArrayList<HashMap<String, String>> noOfPackages = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> noOfSpecialServices = new ArrayList<>();

}

package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.CardDetails;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.android.view.CardNumberEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 26/12/17.
 */

public class PaymentCustomerActivity extends Activity implements View.OnClickListener{

    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    //private CardInputWidget card_input_widget;
    private CardMultilineWidget card_multiline_widget;
    private TextView tv_total_amount,tv_total_discount,tv_grand_total,tv_remove_coupon;
    private Button btn_pay,btn_apply;
    private TextInputLayout textInput_coupon_code;
    private EditText editText_coupon_code,editText_cvv;
    private TextInputLayout textInput_cvv;
    private ProgressDialog pDialog;
    private Stripe stripe;
    private Card card;
    private String strCouponCode="",strDiscountAmount="",strGrandTotal="",strCardStatus="0",strLastDigits="0",strCustomerId="",strCVV="";
    private CheckBox checkbox_save_card;
    private LinearLayout ll_card_view,ll_cvv;
    private RadioGroup rg_saved_cards;

    private ArrayList<CardDetails> arrayListCardDetails;
    private ImageView img_info;
    //private CardNumberEditText mCardNumberEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(this);
        cd = new ConnectionDetector(this);

        ll_card_view = (LinearLayout) findViewById(R.id.ll_card_view);
        ll_cvv = (LinearLayout) findViewById(R.id.ll_cvv);
        rg_saved_cards = (RadioGroup) findViewById(R.id.rg_saved_cards);
        //card_input_widget = (CardInputWidget) findViewById(R.id.card_input_widget);
        //card = card_input_widget.getCard();
        card_multiline_widget = (CardMultilineWidget) findViewById(R.id.card_multiline_widget);
       // card = card_multiline_widget.getCard();
        arrayListCardDetails = new ArrayList<>();

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);


        //mCardNumberEditText = (CardNumberEditText) findViewById(R.id.et_card_number);

        stripe = new Stripe(PaymentCustomerActivity.this,getResources().getString(R.string.publishable_key));

        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        tv_total_discount = (TextView) findViewById(R.id.tv_total_discount);
        tv_grand_total = (TextView) findViewById(R.id.tv_grand_total);

        tv_remove_coupon = (TextView) findViewById(R.id.tv_remove_coupon);
        tv_remove_coupon.setOnClickListener(this);

        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(this);

        editText_coupon_code = (EditText) findViewById(R.id.editText_coupon_code);
        editText_cvv = (EditText) findViewById(R.id.editText_cvv);

        textInput_cvv = (TextInputLayout) findViewById(R.id.textInput_cvv);

        textInput_coupon_code = (TextInputLayout) findViewById(R.id.textInput_coupon_code);
        checkbox_save_card = (CheckBox) findViewById(R.id.checkbox_save_card);

        tv_total_amount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        tv_total_discount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+"0.00");
        tv_grand_total.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        strGrandTotal = mSessionManager.getStringData(SessionManager.KEY_AMOUNT);

        if (cd.isConnectingToInternet()){
            getSavedCards();
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
        }


        rg_saved_cards.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                //Toast.makeText(getApplicationContext(),""+checkedId,Toast.LENGTH_LONG).show();
                if(radioButton.getText().toString().equalsIgnoreCase("Use new card")){
                    ll_card_view.setVisibility(View.VISIBLE);
                    ll_cvv.setVisibility(View.GONE);
                    strCustomerId = "";
                }else {
                    ll_card_view.setVisibility(View.GONE);
                    ll_cvv.setVisibility(View.VISIBLE);

                    for (int i=0;i<arrayListCardDetails.size();i++){
                      if(arrayListCardDetails.get(i).getCard_no_last_digits().equalsIgnoreCase(radioButton.getText().toString())){
                            strCustomerId = arrayListCardDetails.get(i).getCustomer_id();
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_apply:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    if(editText_coupon_code.getText().toString().isEmpty()){
                        textInput_coupon_code.setError(getResources().getString(R.string.err_msg_name));
                        requestFocus(editText_coupon_code);
                    }else {
                        applyCoupon(editText_coupon_code.getText().toString());
                    }
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;

            case R.id.btn_pay:
                hideKeyboard();
                if(strCustomerId.equalsIgnoreCase("")){
                    if(card_multiline_widget.getCard()==null){
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_payment));
                        customDialogForHelp.show();
                    }else {
                       getToken();
                    }
                }else {

                    if (!editText_cvv.getText().toString().isEmpty()) {
                        textInput_cvv.setErrorEnabled(false);
                        verifyCard(editText_cvv.getText().toString().trim());
                    }else {
                        textInput_cvv.setError(getResources().getString(R.string.hint_enter_cvv));

                      /*  CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_enter_cvv));
                        customDialogForHelp.show();*/
                    }

                   /* if (!editText_cvv.getText().toString().isEmpty()) {
                       if (checkbox_save_card.isChecked()) {
                         startActivity(new Intent(PaymentCustomerActivity.this, VerifyPincodeCustomerActivity.class).putExtra("token", "").putExtra("customer_id", strCustomerId).putExtra("save_card_details", "1").putExtra("card_no_last_digits", "0").putExtra("cvv",editText_cvv.getText().toString().trim()));
                         finish();
                       } else {
                         startActivity(new Intent(PaymentCustomerActivity.this, VerifyPincodeCustomerActivity.class).putExtra("token", "").putExtra("customer_id", strCustomerId).putExtra("save_card_details", "0").putExtra("card_no_last_digits", strLastDigits).putExtra("cvv",editText_cvv.getText().toString().trim()));
                         finish();
                       }
                      }else {
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_enter_cvv));
                        customDialogForHelp.show();
                    }*/

                   // startActivity(new Intent(PaymentCustomerActivity.this,VerifyPincodeCustomerActivity.class).putExtra("token","").putExtra("customer_id",strCustomerId).putExtra("save_card_details","0").putExtra("card_no_last_digits","0"));


                   /* if (cd.isConnectingToInternet()) {
                        showProgressDialog();
                        strLastDigits = "0";
                        sendToken("");
                    } else {
                        if (PaymentCustomerActivity.this != null) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                            customDialogForHelp.show();

                        }
                    }*/
                }

                /*if(strCustomerId.equalsIgnoreCase("")){
                    getToken();
                }else {

                    if (cd.isConnectingToInternet()) {
                        showProgressDialog();
                        strLastDigits = "0";
                        sendToken("");
                    } else {
                        if (PaymentCustomerActivity.this != null) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                            customDialogForHelp.show();

                        }
                    }
                }*/
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.img_info:
                hideKeyboard();
                DBInfo dbInfo = new DBInfo(PaymentCustomerActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,"Help",dbInfo.getDescription("Payment Screen"));
                customDialogForHelp.show();
                break;

            case R.id.tv_remove_coupon:
                removeAppliedCoupon();
                break;
        }
    }

    private void verifyCard(final String strCvv) {
        showProgressDialog();

        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_verify_card);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put("customer_id",strCustomerId);
        params.put("cvv",strCvv);


        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(PaymentCustomerActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                closeProgressDialog();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        if (checkbox_save_card.isChecked()) {
                            startActivity(new Intent(PaymentCustomerActivity.this, VerifyPincodeCustomerActivity.class).putExtra("token", "").putExtra("customer_id", strCustomerId).putExtra("save_card_details", "1").putExtra("card_no_last_digits", strLastDigits).putExtra("cvv",strCvv));
                            finish();
                        } else {
                            startActivity(new Intent(PaymentCustomerActivity.this, VerifyPincodeCustomerActivity.class).putExtra("token", "").putExtra("customer_id", strCustomerId).putExtra("save_card_details", "0").putExtra("card_no_last_digits", "0").putExtra("cvv",strCvv));
                            finish();
                        }
                    }else {
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();
                       /* JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                    *//*    startActivity(new Intent(PaymentCustomerActivity.this,LabelCustomerActivity.class).putExtra(JSONConstant.STATUS,"Failure").putExtra(JSONConstant.MESSAGE,jsonObject.has(JSONConstant.MESSAGE)));
                        finish();*//*
                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(btn_get_details,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }*/
                    }
                    closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressDialog();
                }
            }
        });
    }

    private void getToken(){
        if (cd.isConnectingToInternet()) {
            card = card_multiline_widget.getCard();
            //card = card_input_widget.getCard();
            //card.setName("Customer Name");
            //card.setAddressZip("12345");
            try {
                if (!card.getNumber().isEmpty()) {
                     strLastDigits = card.getLast4();
                     strCVV=card.getCVC();
                    //card.setAddressZip("1000");
                    showProgressDialog();

                    //stripe.createToken(  new Card("4242424242424242", 12, 2013, "123"),getResources().getString(R.string.publishable_key), new TokenCallback(){
                   stripe.createToken(card, getResources().getString(R.string.publishable_key), new TokenCallback() {


                        public void onSuccess(Token token) {
                            // TODO: Send Token information to your backend to initiate a charge
                            Log.e("Token","->"+token.getId());
                            closeProgressDialog();

                            if(checkbox_save_card.isChecked()){
                                startActivity(new Intent(PaymentCustomerActivity.this,VerifyPincodeCustomerActivity.class).putExtra("token",token.getId()).putExtra("customer_id","").putExtra("save_card_details","1").putExtra("card_no_last_digits",strLastDigits).putExtra("cvv",strCVV));

                                finish();

                            }else {
                                startActivity(new Intent(PaymentCustomerActivity.this,VerifyPincodeCustomerActivity.class).putExtra("token",token.getId()).putExtra("customer_id","").putExtra("save_card_details","0").putExtra("card_no_last_digits","0").putExtra("cvv",strCVV));

                                finish();

                            }


                           /* if (cd.isConnectingToInternet()) {
                                     sendToken(token.getId());
                            } else {
                                if (PaymentCustomerActivity.this != null) {
                                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                                    customDialogForHelp.show();

                                }
                            }*/

                        }
                        public void onError(Exception error) {
                            closeProgressDialog();
                            Log.d("Stripe", error.getLocalizedMessage());
                        }
                    });
                } else {
                    Toast.makeText(this, "Please Enter card detail", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Please check card detail", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        } else {
            if (PaymentCustomerActivity.this != null) {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                customDialogForHelp.show();
            }
        }

    }

    private void showProgressDialog(){
        pDialog = new ProgressDialog(PaymentCustomerActivity.this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Authenticating ...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.show();
    }

    private void closeProgressDialog(){
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void sendToken(String strToken){
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_pay);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put(JSONConstant.SHIPMENT_ID,mSessionManager.getStringData(SessionManager.KEY_SHIPMENT_ID));
        params.put("currency",mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT));
        //params.put("Amount",strGrandTotal);
        params.put("token",strToken);
        //params.put("total_amount",mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        //params.put("discount",strDiscountAmount);
        params.put("coupon_code",strCouponCode);
        params.put("customer_id",strCustomerId);

        if(checkbox_save_card.isChecked()){
            params.put("save_card_details","1");
            params.put("card_no_last_digits",strLastDigits);
        }else {
            params.put("save_card_details","0");
            params.put("card_no_last_digits","0");
        }


        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(PaymentCustomerActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                closeProgressDialog();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        if(checkbox_save_card.isChecked()) {

                            startActivity(new Intent(PaymentCustomerActivity.this, LabelCustomerActivity.class).putExtra(JSONConstant.STATUS, JSONConstant.SUCCESS).putExtra(JSONConstant.MESSAGE, Json_response.getString(JSONConstant.MESSAGE)).putExtra("pdf", Json_response.getString("pdf_label")).putExtra(JSONConstant.MASTER_TRACKING_NO, Json_response.getString(JSONConstant.MASTER_TRACKING_NO)).putExtra("save_card_details", "1").putExtra("card_no_last_digits",strLastDigits));
                            finish();
                        }else{
                            startActivity(new Intent(PaymentCustomerActivity.this,LabelCustomerActivity.class).putExtra(JSONConstant.STATUS,JSONConstant.SUCCESS).putExtra(JSONConstant.MESSAGE,Json_response.getString(JSONConstant.MESSAGE)).putExtra("pdf",Json_response.getString("pdf_label")).putExtra(JSONConstant.MASTER_TRACKING_NO,Json_response.getString(JSONConstant.MASTER_TRACKING_NO)).putExtra("save_card_details","0").putExtra("card_no_last_digits","0"));
                            finish();

                        }




                        //startActivity(new Intent(PaymentCustomerActivity.this,));
                       /* CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();*/
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                        startActivity(new Intent(PaymentCustomerActivity.this,LabelCustomerActivity.class).putExtra(JSONConstant.STATUS,"Failure").putExtra(JSONConstant.MESSAGE,jsonObject.has(JSONConstant.MESSAGE)));
                        finish();
                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                          /*  CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();*/
                            //Snackbar.make(btn_get_details,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                   closeProgressDialog();
                }
            }
        });
    }


    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void applyCoupon(final String strCode){
        showProgressDialog();
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_apply_coupon_code);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put(JSONConstant.SHIPMENT_ID,mSessionManager.getStringData(SessionManager.KEY_SHIPMENT_ID));
        //params.put("currency",mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT));
        params.put("Amount",mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        params.put("coupon_code",strCode);

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(PaymentCustomerActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                closeProgressDialog();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();
                        editText_coupon_code.setText("");
                        AppConstant.APPLIED_COUPON_CODE=strCode;
                        strGrandTotal = Json_response.getString("discounted_amount");
                        strDiscountAmount = Json_response.getString("discount");
                        strCouponCode = strCode;
                        tv_total_amount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+Json_response.getString("amount"));
                        tv_total_discount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+Json_response.getString("discount"));
                        tv_grand_total.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+Json_response.getString("discounted_amount"));

                        tv_remove_coupon.setVisibility(View.VISIBLE);
                        tv_remove_coupon.setText(getResources().getString(R.string.lbl_remove_coupon)+" "+strCode);

                        // 02-02 11:22:32.196 27599-27599/com.example.aspl.fortunecourier I/System.out: ==Volley Response===>>{"status":"success","message":"Coupon code applied successfully!","coupon_code":"FLAT10","amount":"321.0","discount":38.52,"discounted_amount":282.48}

                    }else {
                        strGrandTotal = "";
                        strDiscountAmount = "";
                        strCouponCode = "";
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(btn_get_details,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressDialog();
                }
            }
        });
    }

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void getSavedCards(){
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_get_saved_cards);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));

        //params.put(JSONConstant.C_ID,"cust_1515507200F9FC20");

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(PaymentCustomerActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                closeProgressDialog();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        JSONArray jsonArray = Json_response.getJSONArray("card_details");
                        if(jsonArray.length()>0){
                            ll_card_view.setVisibility(View.GONE);
                            for (int i = 0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                CardDetails cardDetails = new CardDetails();
                                cardDetails.setCard_no_last_digits(jsonObject.getString("card_no_last_digits"));
                                cardDetails.setCustomer_id(jsonObject.getString("customer_id"));
                                arrayListCardDetails.add(cardDetails);

                                RadioButton radiobutton = new RadioButton(PaymentCustomerActivity.this);
                                radiobutton.setTextSize(18.0f);
                                radiobutton.setText(jsonObject.getString("card_no_last_digits"));
                                rg_saved_cards.addView(radiobutton);

                            }

                            RadioButton radiobutton = new RadioButton(PaymentCustomerActivity.this);
                            radiobutton.setTextSize(18.0f);
                            radiobutton.setText("Use new card");
                            rg_saved_cards.addView(radiobutton);
                        }else {
                            ll_card_view.setVisibility(View.VISIBLE);

                        }

                        /*CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();*/
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(PaymentCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(btn_get_details,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressDialog();
                }
            }
        });
    }

    private void removeAppliedCoupon(){
        AppConstant.APPLIED_COUPON_CODE="";
        tv_remove_coupon.setVisibility(View.GONE);
        textInput_coupon_code.setErrorEnabled(false);
        tv_total_amount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        tv_total_discount.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+"0.00");
        tv_grand_total.setText(mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT)+" "+mSessionManager.getStringData(SessionManager.KEY_AMOUNT));
        strGrandTotal = mSessionManager.getStringData(SessionManager.KEY_AMOUNT);
    }
}

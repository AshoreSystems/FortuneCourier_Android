package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.chaos.view.PinView;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class RegisterPincodeCustomerActivity extends Activity implements View.OnClickListener{

    private PinView pinView_pincode,pinView_re_enter_pincode;
    private Button btn_register;
    private ConnectionDetector cd;
    private SessionManager mSessionManager;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pincode);
        init();
    }

    private void init(){
        cd = new ConnectionDetector(this);
        mSessionManager = new SessionManager(this);


        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        pinView_pincode = (PinView) findViewById(R.id.pinView_pincode);
        pinView_re_enter_pincode = (PinView) findViewById(R.id.pinView_re_enter_pincode);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

    }

    private void validateAndRegister(){
        if(pinView_pincode.getText().toString().isEmpty()||pinView_pincode.getText().toString().length()<6){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(RegisterPincodeCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_pincode));
            customDialogForHelp.show();
        }else if(pinView_re_enter_pincode.getText().toString().isEmpty()||pinView_re_enter_pincode.getText().toString().length()<6){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(RegisterPincodeCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_pincode));
            customDialogForHelp.show();
        }else if(!pinView_pincode.getText().toString().equalsIgnoreCase(pinView_re_enter_pincode.getText().toString())){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(RegisterPincodeCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_pincode_not_matched));
            customDialogForHelp.show();
        }else {
            registerPincode();
        }

    }


    private void registerPincode(){
        progressBar = new ProgressDialog(RegisterPincodeCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();


        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_set_pin);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put("c_security_pin",pinView_pincode.getText().toString().trim());
        params.put("c_confirm_security_pin",pinView_re_enter_pincode.getText().toString().trim());
        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(RegisterPincodeCustomerActivity.this, URL, params, new VolleyResponseListener() {
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
                        Intent i = new Intent(RegisterPincodeCustomerActivity.this, DashboardCustomerActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        //startActivity(new Intent(OTPVerificationCustomerActivity.this, DashboardCustomerActivity.class));
                        finish();

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(RegisterPincodeCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;
            case R.id.btn_register:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    validateAndRegister();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(RegisterPincodeCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

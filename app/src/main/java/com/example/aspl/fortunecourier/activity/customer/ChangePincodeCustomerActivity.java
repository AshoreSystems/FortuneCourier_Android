package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangePincodeCustomerActivity extends Activity implements View.OnClickListener {
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    private Button btn_submit;
    private PinView pinView_old_pincode,pinView_new_pincode,pinView_re_enter_pincode;
    private ProgressDialog progressBar;
    private TextView tv_forgot_pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pincode);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(this);
        cd = new ConnectionDetector(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        tv_forgot_pincode = (TextView) findViewById(R.id.tv_forgot_pincode);
        tv_forgot_pincode.setOnClickListener(this);

        pinView_old_pincode = (PinView) findViewById(R.id.pinView_old_pincode);
        pinView_new_pincode = (PinView) findViewById(R.id.pinView_new_pincode);
        pinView_re_enter_pincode = (PinView) findViewById(R.id.pinView_re_enter_pincode);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(pinView_old_pincode, InputMethodManager.SHOW_IMPLICIT);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.btn_submit:
                hideKeyboard();
                validate();
                break;

            case R.id.tv_forgot_pincode:
                hideKeyboard();
                startActivity(new Intent(ChangePincodeCustomerActivity.this,ForgotPincodeCustomerActivity.class));
                break;
        }
    }

    private void validate(){
        if(cd.isConnectingToInternet()) {
            if (pinView_old_pincode.getText().toString().isEmpty() || pinView_old_pincode.getText().toString().length() < 6) {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePincodeCustomerActivity.this, getResources().getString(R.string.app_name), getResources().getString(R.string.err_msg_pincode));
                customDialogForHelp.show();
            } else if (pinView_new_pincode.getText().toString().isEmpty() || pinView_new_pincode.getText().toString().length() < 6) {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePincodeCustomerActivity.this, getResources().getString(R.string.app_name), getResources().getString(R.string.err_msg_pincode));
                customDialogForHelp.show();
            } else if (pinView_re_enter_pincode.getText().toString().isEmpty() || pinView_re_enter_pincode.getText().toString().length() < 6) {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePincodeCustomerActivity.this, getResources().getString(R.string.app_name), getResources().getString(R.string.err_msg_pincode));
                customDialogForHelp.show();
            } else if (!pinView_new_pincode.getText().toString().equalsIgnoreCase(pinView_re_enter_pincode.getText().toString())) {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePincodeCustomerActivity.this, getResources().getString(R.string.app_name), getResources().getString(R.string.err_msg_pincode_not_matched));
                customDialogForHelp.show();
            } else {
                changePincode();
            }
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePincodeCustomerActivity.this, getResources().getString(R.string.app_name), getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
        }
    }

    private void changePincode(){
        progressBar = new ProgressDialog(ChangePincodeCustomerActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();

        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_change_pin);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
        params.put("c_old_pin",pinView_old_pincode.getText().toString().trim());
        params.put("c_security_pin",pinView_new_pincode.getText().toString().trim());
        params.put("c_confirm_security_pin",pinView_re_enter_pincode.getText().toString().trim());

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(ChangePincodeCustomerActivity.this, URL, params, new VolleyResponseListener() {
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
                        Toast.makeText(ChangePincodeCustomerActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePincodeCustomerActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
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

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

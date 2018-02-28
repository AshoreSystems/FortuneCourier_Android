package com.example.aspl.fortunecourier.activity.associate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.AppConstant;
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

public class ForgotPincodeAssociateActivity extends Activity implements View.OnClickListener{
    private PinView pinView_pincode,pinView_re_enter_pincode;
    private Button btn_register;
    private ConnectionDetector cd;
    private SessionManager mSessionManager;
    private ProgressDialog progressBar;
    private TextView tv_enter_new_pincode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pincode);
        init();
    }

    private void init(){
        cd = new ConnectionDetector(this);
        mSessionManager = new SessionManager(this);

        tv_enter_new_pincode = (TextView) findViewById(R.id.tv_enter_new_pincode);
        tv_enter_new_pincode.setText(getResources().getString(R.string.lbl_enter_new_pincode));

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        pinView_pincode = (PinView) findViewById(R.id.pinView_pincode);
        pinView_re_enter_pincode = (PinView) findViewById(R.id.pinView_re_enter_pincode);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setText(getResources().getString(R.string.btn_submit));
        btn_register.setOnClickListener(this);


    }

    private void validateAndRegister(){
        if(pinView_pincode.getText().toString().isEmpty()||pinView_pincode.getText().toString().length()<6){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ForgotPincodeAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_pincode));
            customDialogForHelp.show();
        }else if(pinView_re_enter_pincode.getText().toString().isEmpty()||pinView_re_enter_pincode.getText().toString().length()<6){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ForgotPincodeAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_pincode));
            customDialogForHelp.show();
        }else if(!pinView_pincode.getText().toString().equalsIgnoreCase(pinView_re_enter_pincode.getText().toString())){
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ForgotPincodeAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_pincode_not_matched));
            customDialogForHelp.show();
        }else {
            forgotPincode();
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                if(cd.isConnectingToInternet()){
                    validateAndRegister();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ForgotPincodeAssociateActivity.this, getResources().getString(R.string.app_name), getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;

            case R.id.header_layout_back:
                finish();
                break;
        }
    }



    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void forgotPincode() {
        progressBar = new ProgressDialog(ForgotPincodeAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();

        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_forgot_pin);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
        // params.put("c_security_pin", pinView_pincode.getText().toString().trim());
        System.out.println("=>" + params.toString());

        VolleyUtils.POST_METHOD(ForgotPincodeAssociateActivity.this, URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.dismiss();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());

                    if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                        Toast.makeText(ForgotPincodeAssociateActivity.this, Json_response.getString(JSONConstant.MESSAGE), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPincodeAssociateActivity.this, OTPVerificationAssociateActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.VERIFY_PINCODE_OTP).putExtra("pincode",pinView_pincode.getText().toString()));
                    } else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ForgotPincodeAssociateActivity.this, getResources().getString(R.string.app_name), jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                        }
                    }
                    progressBar.dismiss();
                } catch (JSONException e) {
                    progressBar.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }



}

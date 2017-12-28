package com.example.aspl.fortunecourier.activity.associate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.OTPVerificationCustomerActivity;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 3/11/17.
 */

public class ForgotPasswordAssociateActivity extends AppCompatActivity implements View.OnClickListener{
    private ConnectionDetector cd;
    private SessionManager mSessionManager;
    private EditText editText_phoneNumber;
    private Button btn_submit;
    private ProgressDialog progressBar;
    private CountryCodePicker ccp;
    private TextInputLayout textInput_phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
    }

    private void init(){
        cd = new ConnectionDetector(ForgotPasswordAssociateActivity.this);
        mSessionManager = new SessionManager(ForgotPasswordAssociateActivity.this);


        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        textInput_phoneNumber = (TextInputLayout) findViewById(R.id.textInput_phoneNumber);

        editText_phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    Snackbar.make(btn_submit,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;


        }

    }

    public void validateAndSubmit() {

        if (editText_phoneNumber.getText().toString().trim().isEmpty() || editText_phoneNumber.getText().toString().trim().length() != 10) {
            textInput_phoneNumber.setError(getResources().getString(R.string.err_msg_phonenumber));
            requestFocus(editText_phoneNumber);
        } else  {
            textInput_phoneNumber.setErrorEnabled(false);
            forgotPassword();
        }
    }


    public void forgotPassword() {

        try {
            progressBar = new ProgressDialog(ForgotPasswordAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_forgot_password);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    mSessionManager.putStringData(SessionManager.KEY_A_ID,Json_response.getString(JSONConstant.A_ID));
                                    mSessionManager.putStringData(SessionManager.KEY_A_DIALLING_CODE,"+"+ccp.getFullNumber());
                                    mSessionManager.putStringData(SessionManager.KEY_A_PHONE_NO,editText_phoneNumber.getText().toString().trim());
                                    startActivity(new Intent(ForgotPasswordAssociateActivity.this, OTPVerificationAssociateActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.FORGOT_OTP));
                                } else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                                    if (jsonObject.has(JSONConstant.A_PHONE_NO)) {
                                        textInput_phoneNumber.setError(jsonObject.getString(JSONConstant.A_PHONE_NO));
                                        requestFocus(editText_phoneNumber);
                                    }
                                    if (jsonObject.has(JSONConstant.MESSAGE)) {
                                        Snackbar.make(textInput_phoneNumber,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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
                            progressBar.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.A_PHONE_NO, editText_phoneNumber.getText().toString().trim());
                    params.put(JSONConstant.A_DIALLING_CODE, "+"+ccp.getFullNumber());

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
}

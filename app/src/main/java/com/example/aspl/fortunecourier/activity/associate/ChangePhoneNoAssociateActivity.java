package com.example.aspl.fortunecourier.activity.associate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
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
 * Created by aspl on 17/11/17.
 */

public class ChangePhoneNoAssociateActivity extends Activity implements View.OnClickListener{
    private ConnectionDetector cd;
    private SessionManager mSessionManager;
    private Button btn_submit;
    private ProgressDialog progressBar;
    private CountryCodePicker ccp_old_phonenumber,ccp_new_phonenumber;
    private TextInputLayout textInput_old_phonenumber,textInput_new_phonenumber;
    private EditText editText_old_phonenumber,editText_new_phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phonenumber);
        init();
    }

    private void init(){
        cd = new ConnectionDetector(ChangePhoneNoAssociateActivity.this);
        mSessionManager = new SessionManager(ChangePhoneNoAssociateActivity.this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        ccp_old_phonenumber = (CountryCodePicker) findViewById(R.id.ccp_old_phonenumber);
        ccp_new_phonenumber = (CountryCodePicker) findViewById(R.id.ccp_new_phonenumber);

        textInput_old_phonenumber = (TextInputLayout) findViewById(R.id.textInput_old_phonenumber);
        textInput_new_phonenumber = (TextInputLayout) findViewById(R.id.textInput_new_phonenumber);

        editText_old_phonenumber = (EditText) findViewById(R.id.editText_old_phonenumber);
        editText_new_phonenumber = (EditText) findViewById(R.id.editText_new_phonenumber);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);


    }

    private void validateAndSubmit() {
        hideKeyboard();
        if (editText_old_phonenumber.getText().toString().trim().isEmpty() || editText_old_phonenumber.getText().toString().trim().length() != 10) {
            textInput_old_phonenumber.setError(getResources().getString(R.string.err_msg_phonenumber));
            requestFocus(editText_old_phonenumber);
        }  else if (editText_new_phonenumber.getText().toString().trim().isEmpty() || editText_new_phonenumber.getText().toString().trim().length() != 10) {
            textInput_new_phonenumber.setError(getResources().getString(R.string.err_msg_phonenumber));
            textInput_old_phonenumber.setErrorEnabled(false);
            requestFocus(editText_new_phonenumber);
        }  else  {
            textInput_new_phonenumber.setErrorEnabled(false);
            changePhoneNumber();
        }
    }


    public void changePhoneNumber() {

        try {
            progressBar = new ProgressDialog(ChangePhoneNoAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_change_phone_no);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                   // Snackbar.make(btn_submit,Json_response.getString(JSONConstant.MESSAGE),Snackbar.LENGTH_LONG).show();
                                    Toast.makeText(ChangePhoneNoAssociateActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                                    mSessionManager.putStringData(SessionManager.KEY_A_ID,Json_response.getString(JSONConstant.A_ID));
                                    mSessionManager.putStringData(SessionManager.KEY_A_PHONE_NO, editText_new_phonenumber.getText().toString().trim());
                                    mSessionManager.putStringData(SessionManager.KEY_A_DIALLING_CODE, "+"+ccp_new_phonenumber.getFullNumber());
                                    startActivity(new Intent(ChangePhoneNoAssociateActivity.this, OTPVerificationAssociateActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.CHANGE_PHONE_OTP));
                                    hideKeyboard();
                                    finish();
                                } else {
                                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                                    if (jsonObject.has(JSONConstant.A_PHONE_NO)) {
                                        textInput_new_phonenumber.setError(jsonObject.getString(JSONConstant.A_PHONE_NO));
                                        requestFocus(editText_new_phonenumber);
                                    }

                                    if (jsonObject.has(JSONConstant.A_OLD_PHONE_NO)) {
                                        textInput_old_phonenumber.setError(jsonObject.getString(JSONConstant.A_PHONE_NO));
                                        requestFocus(editText_old_phonenumber);
                                    }

                                    if (jsonObject.has(JSONConstant.MESSAGE)) {
                                        Toast.makeText(ChangePhoneNoAssociateActivity.this,jsonObject.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();

                                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePhoneNoAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                                        customDialogForHelp.show();
                                        //Snackbar.make(textInput_new_phonenumber,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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
                    params.put(JSONConstant.A_ID, mSessionManager.getStringData(SessionManager.KEY_A_ID));
                    params.put(JSONConstant.A_DIALLING_CODE, "+"+ccp_new_phonenumber.getFullNumber());
                    params.put(JSONConstant.A_PHONE_NO, editText_new_phonenumber.getText().toString().trim());
                    params.put(JSONConstant.A_OLD_DIALLING_CODE, "+"+ccp_old_phonenumber.getFullNumber());
                    params.put(JSONConstant.A_OLD_PHONE_NO, editText_old_phonenumber.getText().toString().trim());
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if(cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePhoneNoAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    //Snackbar.make(btn_submit,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;


        }

    }
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

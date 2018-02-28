package com.example.aspl.fortunecourier.activity.customer;

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
import com.example.aspl.fortunecourier.activity.associate.ResetPasswordAssociateActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 10/11/17.
 */

public class ResetPasswordCustomerActivity extends Activity implements View.OnClickListener{

    private SessionManager mSessionManager;
    private ConnectionDetector cd;

    private EditText editText_new_password,editText_confirm_password;
    private Button btn_submit;
    private ProgressDialog progressBar;
    private TextInputLayout textInput_new_password,textInput_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }

    private void init(){
        cd = new ConnectionDetector(ResetPasswordCustomerActivity.this);
        mSessionManager = new SessionManager(ResetPasswordCustomerActivity.this);

        textInput_new_password = (TextInputLayout) findViewById(R.id.textInput_new_password);
        textInput_confirm_password = (TextInputLayout) findViewById(R.id.textInput_confirm_password);

        editText_new_password = (EditText) findViewById(R.id.editText_new_password);
        editText_confirm_password = (EditText) findViewById(R.id.editText_confirm_password);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

    }

    private void validateAndSubmit(){
        if (editText_new_password.getText().toString().trim().isEmpty() || editText_new_password.getText().toString().trim().length() < 7 || editText_new_password.getText().toString().trim().length() > 10) {
            textInput_new_password.setError(getResources().getString(R.string.err_msg_password));
            requestFocus(editText_new_password);
        } else if (editText_confirm_password.getText().toString().trim().isEmpty() || editText_confirm_password.getText().toString().trim().length() < 7 || editText_confirm_password.getText().toString().trim().length() > 10 || !(editText_confirm_password.getText().toString().trim().equals(editText_new_password.getText().toString().trim()))) {
            textInput_confirm_password.setError(getResources().getString(R.string.err_msg_confirm_password));
            textInput_new_password.setErrorEnabled(false);
            requestFocus(editText_confirm_password);
        } else {
            textInput_confirm_password.setErrorEnabled(false);
            resetPassword();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                hideKeyboard();
                if(cd.isConnectingToInternet()){
                    validateAndSubmit();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ResetPasswordCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
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

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void resetPassword() {

        try {
            progressBar = new ProgressDialog(ResetPasswordCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_reset_password);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Respontexse===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                String status = Json_response.getString(JSONConstant.STATUS);
                                if (status.equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                    Toast.makeText(ResetPasswordCustomerActivity.this,getResources().getString(R.string.msg_password_change),Toast.LENGTH_SHORT).show();
                                    finish();
                                  /*  Snackbar.make(btn_submit, getResources().getString(R.string.msg_password_change), Snackbar.LENGTH_SHORT).show();
                                    thread.start();*/
                                } else {

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
                    params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));
                    params.put(JSONConstant.C_PASSWORD, editText_new_password.getText().toString().trim());
                    params.put(JSONConstant.C_CONFIRM_PASSWORD, editText_confirm_password.getText().toString().trim());

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

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                startActivity(new Intent(ResetPasswordCustomerActivity.this, LoginCustomerActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.FORGOT_OTP));
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}


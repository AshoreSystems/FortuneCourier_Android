package com.example.aspl.fortunecourier.activity.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 16/11/17.
 */

public class ChangePasswordCustomerActivity extends AppCompatActivity implements View.OnClickListener{

    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    private EditText editText_old_password,editText_new_password,editText_confirm_password;
    private Button btn_submit;
    private ProgressDialog progressBar;
    private TextInputLayout textInput_old_password,textInput_new_password,textInput_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(ChangePasswordCustomerActivity.this);
        cd = new ConnectionDetector(ChangePasswordCustomerActivity.this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        textInput_old_password = (TextInputLayout) findViewById(R.id.textInput_old_password);
        textInput_old_password.setVisibility(View.VISIBLE);
        textInput_new_password = (TextInputLayout) findViewById(R.id.textInput_new_password);
        textInput_confirm_password = (TextInputLayout) findViewById(R.id.textInput_confirm_password);
        editText_old_password = (EditText) findViewById(R.id.editText_old_password);
        editText_new_password = (EditText) findViewById(R.id.editText_new_password);
        editText_confirm_password = (EditText) findViewById(R.id.editText_confirm_password);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if(cd.isConnectingToInternet()){
                    hideKeyboard();
                    validateAndSubmit();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePasswordCustomerActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                    // Snackbar.make(btn_submit,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.header_layout_back:
                finish();
                break;
        }
    }



    private void validateAndSubmit(){
        if(editText_old_password.getText().toString().trim().isEmpty() || editText_old_password.getText().toString().trim().length() < 7 || editText_old_password.getText().toString().trim().length() > 10){
            textInput_old_password.setError(getResources().getString(R.string.err_msg_password));
            requestFocus(editText_old_password);
        }else if (editText_new_password.getText().toString().trim().isEmpty() || editText_new_password.getText().toString().trim().length() < 7 || editText_new_password.getText().toString().trim().length() > 10) {
            textInput_new_password.setError(getResources().getString(R.string.err_msg_password));
            textInput_old_password.setErrorEnabled(false);
            requestFocus(editText_new_password);
        } else if (editText_confirm_password.getText().toString().trim().isEmpty() || editText_confirm_password.getText().toString().trim().length() < 7 || editText_confirm_password.getText().toString().trim().length() > 10 || !(editText_confirm_password.getText().toString().trim().equals(editText_new_password.getText().toString().trim()))) {
            textInput_confirm_password.setError(getResources().getString(R.string.err_msg_confirm_password));
            textInput_new_password.setErrorEnabled(false);
            requestFocus(editText_confirm_password);
        } else {
            textInput_confirm_password.setErrorEnabled(false);

            changePassword();
        }
    }

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void changePassword() {

        try {
            progressBar = new ProgressDialog(ChangePasswordCustomerActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_change_password);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                               if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                                   /* mSessionManager.putStringData(SessionManager.KEY_C_ID,Json_response.getString(JSONConstant.C_ID));
                                    startActivity(new Intent(ChangePasswordCustomerActivity.this, OTPVerificationCustomerActivity.class).putExtra(AppConstant.SIGNUP_OR_FORGOT_OTP,AppConstant.FORGOT_OTP));*/
                                  /* Snackbar.make(btn_submit,Json_response.getString(JSONConstant.MESSAGE),Snackbar.LENGTH_LONG).show();
                                   thread.start();*/
                                   Toast.makeText(ChangePasswordCustomerActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                                   finish();
                               } else {
                                   CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ChangePasswordCustomerActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                                   customDialogForHelp.show();
                                    //Snackbar.make(btn_submit,Json_response.getString(JSONConstant.MESSAGE),Snackbar.LENGTH_LONG).show();
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

                    params.put(JSONConstant.C_ID, mSessionManager.getStringData(SessionManager.KEY_C_ID));
                    params.put(JSONConstant.C_OLD_PASSWORD, editText_old_password.getText().toString().trim());
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

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}

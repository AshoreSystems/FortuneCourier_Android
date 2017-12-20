package com.example.aspl.fortunecourier.activity.associate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aspl.fortunecourier.activity.customer.LandingCustomerActivity;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.utility.AppSingleton;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aspl on 2/11/17.
 */



public class LandingAssociateActivity extends AppCompatActivity implements View.OnClickListener {

        private TextView tv_become_user, tv_info, tv_existing_user, tv_portal, tv_check_rates;
        private Button btn_register_now;
        private ConnectionDetector cd;
        private ProgressDialog progressBar;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_landing);
            init();
        }


        private void init() {

            cd = new ConnectionDetector(LandingAssociateActivity.this);

            tv_become_user = (TextView) findViewById(R.id.tv_become_user);
            tv_become_user.setOnClickListener(this);

            tv_info = (TextView) findViewById(R.id.tv_info);
            tv_info.setOnClickListener(this);
            tv_info.setMovementMethod(new ScrollingMovementMethod());

            tv_existing_user = (TextView) findViewById(R.id.tv_existing_user);
            tv_existing_user.setOnClickListener(this);

            tv_portal = (TextView) findViewById(R.id.tv_portal);
            tv_portal.setOnClickListener(this);

            tv_check_rates = (TextView) findViewById(R.id.tv_check_rates);
            tv_check_rates.setOnClickListener(this);
            tv_check_rates.setVisibility(View.INVISIBLE);

            btn_register_now = (Button) findViewById(R.id.btn_register_now);
            btn_register_now.setOnClickListener(this);

            tv_become_user.setText(getResources().getString(R.string.lbl_become_associate));
            tv_existing_user.setText(getResources().getString(R.string.lbl_existing_associate));
            tv_portal.setText(getResources().getString(R.string.lbl_customer_portal));

            String first = "Existing Associate? ";
            String next = "<font color='#EE0000'>Login</font>";
            tv_existing_user.setText(Html.fromHtml(first + next));

            if(cd.isConnectingToInternet()){
                getCustomerInfo();
            }else {
                Snackbar.make(tv_portal,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tv_become_user:
                    break;

                case R.id.tv_info:
                    break;

                case R.id.tv_existing_user:
                    startActivity(new Intent(LandingAssociateActivity.this,LoginAssociateActivity.class));
                    break;

                case R.id.tv_portal:
                    startActivity(new Intent(LandingAssociateActivity.this,LandingCustomerActivity.class));
                    break;

                case R.id.tv_check_rates:
                    break;

                case R.id.btn_register_now:
                    startActivity(new Intent(LandingAssociateActivity.this,SignUpAssociateActivity.class));
                    break;

            }

        }

    public void getCustomerInfo() {

        try {
            progressBar = new ProgressDialog(LandingAssociateActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Authenticating ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();

            String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_dashboard_content);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("==Volley Response===>>" + response);
                            try {
                                JSONObject Json_response = new JSONObject(response);
                                if( Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                                    JSONObject jsonResponsePageContent = Json_response.getJSONObject(JSONConstant.PAGE_CONTENT);
                                    tv_info.setText(Html.fromHtml(jsonResponsePageContent.getString("pageContent")));
                                   /* CustomDialogClass customDialogClass = new CustomDialogClass(LandingCustomerActivity.this,jsonResponsePageContent.getString(JSONConstant.PAGENAME),jsonResponsePageContent.getString(JSONConstant.PAGECONTENT));
                                    customDialogClass.show();*/
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

              /*  @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(JSONConstant.C_FACEBOOK_ID,fb_id);
                    params.put(JSONConstant.C_FIRST_NAME, f_name);
                    params.put(JSONConstant.C_LAST_NAME, l_name);
                    params.put(JSONConstant.C_EMAIL_ADDRESS, email);
                    params.put(JSONConstant.C_PROFILE_PIC,profile_url);

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
    }



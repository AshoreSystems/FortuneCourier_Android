package com.example.aspl.fortunecourier.activity.associate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.adapter.CommissionAdapter;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Commission;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 19/1/18.
 */

public class MyCommissionActivity extends Activity implements View.OnClickListener{

    private TextView tv_total_commission,tv_current_month_commission,tv_amount_received;
    private RecyclerView list_commission;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ArrayList<Commission> arrayListOfCommissionDetails;
    private CommissionAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commission);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(MyCommissionActivity.this);
        ConnectionDetector cd = new ConnectionDetector(MyCommissionActivity.this);
        arrayListOfCommissionDetails = new ArrayList<>();

        list_commission = (RecyclerView) findViewById(R.id.list_commission);
        tv_total_commission = (TextView) findViewById(R.id.tv_total_commission);
        tv_current_month_commission = (TextView) findViewById(R.id.tv_current_month_commission);
        tv_amount_received = (TextView) findViewById(R.id.tv_amount_received);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        ImageView img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        list_commission.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(list_commission.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(MyCommissionActivity.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        list_commission.addItemDecoration(horizontalDecoration);

        if(cd.isConnectingToInternet()){
           getCommisionDetails();

       }else {
           CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(MyCommissionActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
           customDialogForHelp.show();
       }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_info:
                DBInfo dbInfo = new DBInfo(MyCommissionActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(MyCommissionActivity.this,"Help",dbInfo.getDescription("My Commission Screen"));
                customDialogForHelp.show();
                break;

            case R.id.header_layout_back:
                finish();
                break;
        }
    }

    private void getCommisionDetails(){

        progressBar = new ProgressDialog(MyCommissionActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_get_associate_commissions);

        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(MyCommissionActivity.this, URL, params, new VolleyResponseListener() {
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

                        tv_total_commission.setText("Total Earned : "+Json_response.getString("currency")+" "+Json_response.getString("total_commission"));
                        tv_current_month_commission.setText("Current Month : "+Json_response.getString("currency")+" "+Json_response.getString("current_month_commission"));
                        tv_amount_received.setText("Amount Received : USD 0.0");

                        JSONArray jsonArray = (JSONArray) Json_response.get("commissions");
                        for(int i = 0; i< jsonArray.length() ; i++ ){
                            JSONObject jsonObjectRate = jsonArray.getJSONObject(i);
                            Commission commission = new Commission();
                            commission.setCommission(jsonObjectRate.getString("commission"));
                            commission.setYear(jsonObjectRate.getString("year"));
                            commission.setMonth(jsonObjectRate.getString("month"));
                            commission.setCurrency(jsonObjectRate.getString("currency"));
                            arrayListOfCommissionDetails.add(commission);
                        }

                        recyclerViewAdapter = new CommissionAdapter(arrayListOfCommissionDetails,MyCommissionActivity.this);
                        list_commission.setAdapter(recyclerViewAdapter);
                        progressBar.dismiss();

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(MyCommissionActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
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

}

package com.example.aspl.fortunecourier.activity.associate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.adapter.ShipmentHistoryAssociateAdapter;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.HistoryDetails;
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
 * Created by aspl on 18/12/17.
 */

public class ShipmentHistoryAssociateActivity extends Activity implements View.OnClickListener{

    private RecyclerView recycler_history_list;
    private ImageView img_info ;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    ArrayList<HistoryDetails> listOfHistoryDeatils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
    }

    private void init(){
        mSessionManager = new SessionManager(this);
        cd = new ConnectionDetector(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        recycler_history_list = (RecyclerView) findViewById(R.id.recycler_history_list);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recycler_history_list.setLayoutManager(recyclerLayoutManager);

      /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_history_list.getContext(), recyclerLayoutManager.getOrientation());
        recycler_history_list.addItemDecoration(dividerItemDecoration);

*/

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_history_list.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(ShipmentHistoryAssociateActivity.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recycler_history_list.addItemDecoration(horizontalDecoration);

        if(cd.isConnectingToInternet()){
            viewHistoryDetails();
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentHistoryAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
            //Snackbar.make(img_info,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                finish();
                break;
            case R.id.img_info:
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentHistoryAssociateActivity.this,"Help",getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;
        }
    }


    private void viewHistoryDetails(){
        listOfHistoryDeatils = new ArrayList<HistoryDetails>();

        progressBar = new ProgressDialog(ShipmentHistoryAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_view_shipment_history);

        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(ShipmentHistoryAssociateActivity.this, URL, params, new VolleyResponseListener() {
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

                        //JSONArray jsonArray = new JSONArray(response.toString());
                        JSONArray jsonArray = (JSONArray) Json_response.get("shipment_history");
                        Log.e("Hi","->"+jsonArray.length());

                        for(int i = 0; i< jsonArray.length() ; i++ ){
                            JSONObject jsonObjectRate = jsonArray.getJSONObject(i);
                            HistoryDetails historyDetails = new HistoryDetails();
                            historyDetails.setDeliveryDate(jsonObjectRate.getString("expected_deliver_date"));
                            historyDetails.setDeliveryDay(jsonObjectRate.getString("expected_deliver_day"));
                            historyDetails.setDeliveryStatus(jsonObjectRate.getString("status"));
                            historyDetails.setShipDate(jsonObjectRate.getString("shipmentdate"));
                            historyDetails.setShipDay(jsonObjectRate.getString("shipmentday"));
                            historyDetails.setToName(jsonObjectRate.getString("to_contact_name"));
                            historyDetails.setMasterTrackingNo(jsonObjectRate.getString("master_tracking_no"));
                            historyDetails.setShipmentId(jsonObjectRate.getString("shipment_id"));
                            historyDetails.setFromCountry(jsonObjectRate.getString(JSONConstant.FROM_A_COUNTRY_CODE));
                            historyDetails.setFromState(jsonObjectRate.getString(JSONConstant.FROM_A_STATE));
                            historyDetails.setToCountry(jsonObjectRate.getString(JSONConstant.TO_A_COUNTRY_CODE));
                            historyDetails.setToState(jsonObjectRate.getString(JSONConstant.TO_A_STATE));
                            historyDetails.setToCity(jsonObjectRate.getString(JSONConstant.TO_A_CITY));
                            historyDetails.setFromCity(jsonObjectRate.getString(JSONConstant.FROM_A_CITY));
                            listOfHistoryDeatils.add(historyDetails);

                           /* ratesArrayList.add(new Rates(jsonObjectRate.getString("ServiceType"),
                                    jsonObjectRate.getString("Amount"),
                                    jsonObjectRate.getString("ServiceDescription"),
                                    jsonObjectRate.getString("DeliveryDate"),
                                    jsonObjectRate.getString("DeliveryTimestamp"),
                                    jsonObjectRate.getString("BaseAmount"),
                                    jsonObjectRate.getString("cs_tax1"),
                                    jsonObjectRate.getString("cs_tax1_title"),
                                    jsonObjectRate.getString("cs_tax2"),
                                    jsonObjectRate.getString("cs_tax2_title"),
                                    jsonObjectRate.getString("cs_surcharge"),
                                    jsonObjectRate.getString("cs_surcharge_title")*/

                        }

                        ShipmentHistoryAssociateAdapter recyclerViewAdapter = new ShipmentHistoryAssociateAdapter(listOfHistoryDeatils,ShipmentHistoryAssociateActivity.this);
                        recycler_history_list.setAdapter(recyclerViewAdapter);

                        progressBar.dismiss();



                        //Snackbar.make(img_info,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentHistoryAssociateActivity.this,getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                            //Snackbar.make(img_info,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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

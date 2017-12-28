package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.adapter.ShipmentHistoryAdapter;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.HistoryDetails;
import com.example.aspl.fortunecourier.model.Rates;
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
import java.util.List;
import java.util.Map;

/**
 * Created by aspl on 18/12/17.
 */

public class ShipmentHistoryActivity extends Activity implements View.OnClickListener{

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
        Drawable horizontalDivider = ContextCompat.getDrawable(ShipmentHistoryActivity.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recycler_history_list.addItemDecoration(horizontalDecoration);

        if(cd.isConnectingToInternet()){
            viewHistoryDetails();
        }else {
            Snackbar.make(img_info,getResources().getString(R.string.err_msg_internet),Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                finish();
                break;
            case R.id.img_info:
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ShipmentHistoryActivity.this,"Help",getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;
        }
    }


    private void viewHistoryDetails(){
        listOfHistoryDeatils = new ArrayList<HistoryDetails>();

        progressBar = new ProgressDialog(ShipmentHistoryActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = getResources().getString(R.string.url_domain_customer) + getResources().getString(R.string.url_view_shipment_history);

        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID,mSessionManager.getStringData(SessionManager.KEY_C_ID));

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(ShipmentHistoryActivity.this, URL, params, new VolleyResponseListener() {
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

                        ShipmentHistoryAdapter recyclerViewAdapter = new ShipmentHistoryAdapter(listOfHistoryDeatils,ShipmentHistoryActivity.this);
                        recycler_history_list.setAdapter(recyclerViewAdapter);

                        progressBar.dismiss();



                        //Snackbar.make(img_info,Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            Snackbar.make(img_info,jsonObject.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_SHORT).show();
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

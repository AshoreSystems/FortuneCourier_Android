package com.example.aspl.fortunecourier.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.associate.MyCommissionActivity;
import com.example.aspl.fortunecourier.activity.customer.ShipmentDetailsCustomerActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Commission;
import com.example.aspl.fortunecourier.model.CommissionDetails;
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
 * Created by aspl on 15/1/18.
 */

public class CommissionAdapter extends RecyclerView.Adapter<CommissionAdapter.ViewHolder> {


    private List<Commission> commissionList;
    private Activity mActivity;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ArrayList<CommissionDetails> arrayListOfCommissionDetails = new ArrayList<>();


    public CommissionAdapter(List<Commission> offersListIn, Activity activity) {
        commissionList = offersListIn;
        mActivity = activity;
        mSessionManager = new SessionManager(activity);
    }


    @Override
    public CommissionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_commission, parent, false);

        CommissionAdapter.ViewHolder viewHolder = new CommissionAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CommissionAdapter.ViewHolder holder, final int position) {

        Commission commission = commissionList.get(position);

        holder.tv_month_year.setText(commission.getMonth()+":"+ commission.getYear());
        holder.tv_amount.setText(commission.getCurrency()+" "+ commission.getCommission());

        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCommisionDetails(commissionList.get(position).getMonth(),commissionList.get(position).getYear());
              /*  Intent intent = new Intent(mActivity, HistoryDetailsAssociateActivity.class).putExtra("ShipmentId",historyList.get(position).getShipmentId());
                mActivity.startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return commissionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout ll_row;
        public TextView tv_month_year,tv_amount;

        public ViewHolder(View view) {
            super(view);
            ll_row = (LinearLayout) view.findViewById(R.id.ll_row);

            tv_month_year = (TextView) view.findViewById(R.id.tv_month_year);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);

        }
    }

    private void getCommisionDetails(String strMonth,String strYear){

        progressBar = new ProgressDialog(mActivity);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = mActivity.getResources().getString(R.string.url_domain_associate) + mActivity.getResources().getString(R.string.url_get_associate_monthly_commission);

        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.A_ID,mSessionManager.getStringData(SessionManager.KEY_A_ID));
        params.put("month",strMonth);
        params.put("year",strYear);

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(mActivity, URL, params, new VolleyResponseListener() {
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
                     //   02-07 12:32:24.974 32372-32372/com.example.aspl.fortunecourier I/System.out: ==Volley Response===>>{"status":"success","commissions":[{"commission":"2.43","master_tracking_no":"1Z94A6651522585448","shipmentdate":"02\/01\/2018","shipment_id":"ship_151748560924D507"},{"commission":"4.52","master_tracking_no":"1Z94A6650118984041","shipmentdate":"02\/02\/2018","shipment_id":"ship_15175843323DA8BF"},{"commission":"4.52","master_tracking_no":"1Z94A6650115476257","shipmentdate":"02\/02\/2018","shipment_id":"ship_1517584376ADF596"},{"commission":"1.99","master_tracking_no":"794672025583","shipmentdate":"02\/06\/2018","shipment_id":"ship_1517809122802A61"},{"commission":"4.90","master_tracking_no":"1Z94A6651516283277","shipmentdate":"02\/06\/2018","shipment_id":"ship_1517809606EFDD44"},{"commission":"4.12","master_tracking_no":"1Z94A6651318606089","shipmentdate":"02\/05\/2018","shipment_id":"ship_151781004350558F"},{"commission":"4.12","master_tracking_no":"1Z94A6651311947090","shipmentdate":"02\/05\/2018","shipment_id":"ship_1517810076090448"},{"commission":"161.36","master_tracking_no":"794672096542","shipmentdate":"02\/06\/2018","shipment_id":"ship_1517830490A0FE72"},{"commission":"4.12","master_tracking_no":"1Z94A6651325745602","shipmentdate":"02\/05\/2018","shipment_id":"ship_1517831736641473"},{"commission":"4.43","master_tracking_no":"1Z94A6651308579717","shipmentdate":"02\/05\/2018","shipment_id":"ship_1517831957098E88"},{"commission":"4.43","master_tracking_no":"1Z94A6651316119323","shipmentdate":"02\/05\/2018","shipment_id":"ship_1517832416084F72"},{"commission":"4.43","master_tracking_no":"1Z94A6651302373137","shipmentdate":"02\/05\/2018","shipment_id":"ship_151783249585BD3C"},{"commission":"4.43","master_tracking_no":"1Z94A6651304465141","shipmentdate":"02\/05\/2018","shipment_id":"ship_15178325574091B4"},{"commission":"4.43","master_tracking_no":"1Z94A6651306799359","shipmentdate":"02\/05\/2018","shipment_id":"ship_1517832574EDE57A"},{"commission":"4.43","master_tracking_no":"1Z94A6651338202414","shipmentdate":"02\/05\/2018","shipment_id":"ship_1517832604B6DFB5"},{"commission":"4.43","master_tracking_no":"1Z94A6651335609026","shipmentdate":"02\/05\/2018","shipment_id":"ship_151783271059B5B8"},{"commission":"2.76","master_tracking_no":"1Z94A6651302659767","shipmentdate":"02\/05\/2018","shipment_id":"ship_15178331703B3377"},{"commission":"0.15","master_tracking_no":"420900459405501699320898149310","shipmentdate":"02\/05\/2018","shipment_id":"ship_151783390586B544"},{"commission":"4.90","master_tracking_no":"1Z94A6651507810375","shipmentdate":"02\/07\/2018","shipment_id":"ship_1517907513ED1E21"},{"commission":"2.05","master_tracking_no":"794672397488","shipmentdate":"02\/07\/2018","shipment_id":"ship_151790802405C1B9"},{"commission":"2.41","master_tracking_no":"1Z94A6651321527640","shipmentdate":"02\/06\/2018","shipment_id":"ship_151790823002760D"},{"commission":"2.41","master_tracking_no":"1Z94A6651327567659","shipmentdate":"02\/06\/2018","shipment_id":"ship_151790825105F9B4"},{"commission":"2.41","master_tracking_no":"1Z94A6651328629072","shipmentdate":"02\/06\/2018","shipment_id":"ship_15179090320EDE44"},{"commission":"4.90","master_tracking_no":"1Z94A6651533498487","shipmentdate":"02\/07\/2018","shipment_id":"ship_15179091654547A4"},{"commission":"0.20","master_tracking_no":"420900459405501699320898629799","shipmentdate":"02\/08\/2018","shipment_id":"ship_1517924878572F73"},{"commission":"0.10","master_tracking_no":"420900129405501699320898657198","shipmentdate":"02\/06\/2018","shipment_id":"ship_1517930032C14C0F"},{"commission":"2.43","master_tracking_no":"1Z94A6651522585448","shipmentdate":"02\/01\/2018","shipment_id":"ship_151748560924D507"},{"commission":"4.52","master_tracking_no":"1Z94A6650118984041","shipmentdate":"02\/02\/2018","shipment_id":"ship_15175843323DA8BF"},{"commission":"4.52","master_tracking_no":"1Z94A6650115476257","shipmentdate":"02\/02\/2018","shipment_id":"ship_1517584376ADF596"},{"commission":"1.99","master_tracking_no":"794672025583","shipmentdate":"02\/06\/2018","shipment_id":"ship_1517809122802A61"},{"commission":"4.90","master_tra

                        arrayListOfCommissionDetails = new ArrayList<>();

                        JSONArray jsonArray = (JSONArray) Json_response.get("commissions");
                        for(int i = 0; i< jsonArray.length() ; i++ ){
                            JSONObject jsonObjectRate = jsonArray.getJSONObject(i);
                            CommissionDetails commissionDetails = new CommissionDetails();
                            commissionDetails.setCommission(jsonObjectRate.getString("commission"));
                            commissionDetails.setMaster_tracking_no(jsonObjectRate.getString("master_tracking_no"));
                            commissionDetails.setShipment_id(jsonObjectRate.getString("shipment_id"));
                            commissionDetails.setShipmentdate(jsonObjectRate.getString("shipmentdate"));
                            commissionDetails.setCurrency(jsonObjectRate.getString("currency"));
                            arrayListOfCommissionDetails.add(commissionDetails);
                        }
                        progressBar.dismiss();

                        CommissionDetailsAdapter commissionDetailsAdapter = new CommissionDetailsAdapter(arrayListOfCommissionDetails,mActivity);

                        showDetailsListDialog(commissionDetailsAdapter);

                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(mActivity,mActivity.getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
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

    private void showDetailsListDialog(CommissionDetailsAdapter commissionDetailsAdapter){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_for_view_commission, null);
        dialogBuilder.setView(dialogView);
        RecyclerView  list_commission = (RecyclerView) dialogView.findViewById(R.id.list_commission);
        Button btn_close = (Button) dialogView.findViewById(R.id.btn_close);


        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(mActivity);
        list_commission.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(list_commission.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(mActivity, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        list_commission.addItemDecoration(horizontalDecoration);

        list_commission.setAdapter(commissionDetailsAdapter);


        final AlertDialog alertDialog;
        alertDialog = dialogBuilder.create();
        Window window = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}

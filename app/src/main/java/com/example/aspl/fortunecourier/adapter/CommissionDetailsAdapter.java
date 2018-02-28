package com.example.aspl.fortunecourier.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
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

public class CommissionDetailsAdapter extends RecyclerView.Adapter<CommissionDetailsAdapter.ViewHolder> {


    private List<CommissionDetails> commissionList;
    private Activity mActivity;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private ArrayList<CommissionDetails> arrayListOfCommissionDetails = new ArrayList<>();


    public CommissionDetailsAdapter(List<CommissionDetails> offersListIn, Activity activity) {
        commissionList = offersListIn;
        mActivity = activity;
        mSessionManager = new SessionManager(activity);
    }


    @Override
    public CommissionDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_commission_deatils, parent, false);

        CommissionDetailsAdapter.ViewHolder viewHolder = new CommissionDetailsAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CommissionDetailsAdapter.ViewHolder holder, final int position) {

        CommissionDetails commissionDetails = commissionList.get(position);

        holder.tv_tracking_number.setText(commissionDetails.getMaster_tracking_no());
        holder.tv_amount.setText(commissionDetails.getCurrency()+" "+ commissionDetails.getCommission());
        holder.tv_date.setText(commissionDetails.getShipmentdate());

        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        public TextView tv_tracking_number,tv_date,tv_amount;

        public ViewHolder(View view) {
            super(view);
            ll_row = (LinearLayout) view.findViewById(R.id.ll_row);

            tv_tracking_number = (TextView) view.findViewById(R.id.tv_tracking_number);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            tv_date = (TextView) view.findViewById(R.id.tv_date);


        }
    }


}

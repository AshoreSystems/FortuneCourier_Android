package com.example.aspl.fortunecourier.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.associate.HistoryDetailsAssociateActivity;
import com.example.aspl.fortunecourier.activity.customer.HistoryDetailsCustomerActivity;
import com.example.aspl.fortunecourier.model.HistoryDetails;

import java.util.List;

/**
 * Created by aspl on 18/12/17.
 */

public class ShipmentHistoryAssociateAdapter extends RecyclerView.Adapter<ShipmentHistoryAssociateAdapter.ViewHolder> {


    private List<HistoryDetails> historyList;
    private Activity mActivity;
    private int lastSelectedPosition = -1;

    public ShipmentHistoryAssociateAdapter(List<HistoryDetails> offersListIn, Activity activity) {
        historyList = offersListIn;
        mActivity = activity;
    }


    @Override
    public ShipmentHistoryAssociateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_shipment_history, parent, false);

        ShipmentHistoryAssociateAdapter.ViewHolder viewHolder = new ShipmentHistoryAssociateAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ShipmentHistoryAssociateAdapter.ViewHolder holder, final int position) {

        HistoryDetails historyDetails = historyList.get(position);

        holder.tv_transaction_no.setText(historyDetails.getMasterTrackingNo());
        holder.tv_ship_day.setText(historyDetails.getShipDay());
        holder.tv_ship_date.setText(historyDetails.getShipDate());
        holder.tv_to_name.setText("To : "+historyDetails.getToName());
        holder.tv_delivery_status.setText(historyDetails.getDeliveryStatus());
        holder.tv_delivery_day.setText(historyDetails.getDeliveryDay());
        holder.tv_delivery_date.setText(historyDetails.getDeliveryDate());
        holder.tv_to_city.setText(historyDetails.getToCity());
        holder.tv_from_city.setText(historyDetails.getFromCity());

        holder.tv_from_address.setText(historyDetails.getFromState()+" , "+historyDetails.getFromCountry());
        holder.tv_to_address.setText(historyDetails.getToState()+" , "+historyDetails.getToCountry());

        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, HistoryDetailsAssociateActivity.class).putExtra("ShipmentId",historyList.get(position).getShipmentId());
                mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_row;
        public ImageView img_delet,img_delivery_status;
        public TextView tv_transaction_no,tv_ship_day,tv_ship_date,tv_to_name,tv_delivery_status,tv_delivery_day,tv_delivery_date,tv_from_address,tv_to_address;
        public  TextView tv_from_city,tv_to_city;

        public ViewHolder(View view) {
            super(view);

            ll_row = (LinearLayout) view.findViewById(R.id.ll_row);

            img_delet = (ImageView) view.findViewById(R.id.img_delet);
            img_delivery_status = (ImageView) view.findViewById(R.id.img_delivery_status);

            tv_transaction_no = (TextView) view.findViewById(R.id.tv_transaction_no);
            tv_ship_day = (TextView) view.findViewById(R.id.tv_ship_day);
            tv_ship_date = (TextView) view.findViewById(R.id.tv_ship_date);
            tv_to_name = (TextView) view.findViewById(R.id.tv_to_name);
            tv_delivery_status = (TextView) view.findViewById(R.id.tv_delivery_status);
            tv_delivery_day = (TextView) view.findViewById(R.id.tv_delivery_day);
            tv_delivery_date = (TextView) view.findViewById(R.id.tv_delivery_date);
            tv_to_address = (TextView) view.findViewById(R.id.tv_to_address);
            tv_from_address = (TextView) view.findViewById(R.id.tv_from_address);

            tv_from_city = (TextView) view.findViewById(R.id.tv_from_city);
            tv_to_city = (TextView) view.findViewById(R.id.tv_to_city);
           /* ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, HistoryDetailsCustomerActivity.class);
                    mActivity.startActivity(intent);
                }
            });*/

           /* rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    Toast.makeText(CheckRateAssociateAdapter.this.mActivity,
                            "selected offer is " + tv_service_desc.getText(),
                            Toast.LENGTH_LONG).show();
                }
            });*/

        }
    }
}

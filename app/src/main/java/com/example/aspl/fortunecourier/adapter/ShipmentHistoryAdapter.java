package com.example.aspl.fortunecourier.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.HistoryDetailsActivity;
import com.example.aspl.fortunecourier.model.HistoryDetails;
import com.example.aspl.fortunecourier.model.Rates;

import java.util.List;

import static com.example.aspl.fortunecourier.R.id.tv_amount;
import static com.example.aspl.fortunecourier.R.id.tv_service_desc;

/**
 * Created by aspl on 18/12/17.
 */

public class ShipmentHistoryAdapter  extends RecyclerView.Adapter<ShipmentHistoryAdapter.ViewHolder> {


    private List<HistoryDetails> historyList;
    private Activity mActivity;
    private int lastSelectedPosition = -1;

    public ShipmentHistoryAdapter(List<HistoryDetails> offersListIn, Activity activity) {
        historyList = offersListIn;
        mActivity = activity;
    }


    @Override
    public ShipmentHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_shipment_history, parent, false);

        ShipmentHistoryAdapter.ViewHolder viewHolder = new ShipmentHistoryAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ShipmentHistoryAdapter.ViewHolder holder, final int position) {

        HistoryDetails historyDetails = historyList.get(position);

        holder.tv_transaction_no.setText(historyDetails.getMasterTrackingNo());
        holder.tv_ship_day.setText(historyDetails.getShipDay());
        holder.tv_ship_date.setText(historyDetails.getShipDate());
        holder.tv_to_name.setText(historyDetails.getToName());
        holder.tv_delivery_status.setText(historyDetails.getDeliveryStatus());
        holder.tv_delivery_day.setText(historyDetails.getDeliveryDay());
        holder.tv_delivery_date.setText(historyDetails.getDeliveryDate());

        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, HistoryDetailsActivity.class).putExtra("ShipmentId",historyList.get(position).getShipmentId());
                mActivity.startActivity(intent);
            }
        });

       /* holder.tv_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strForTooltip = "BaseAmount : $"+checkRateList.get(position).getBaseAmount();
                strForTooltip1 = checkRateList.get(position).getCs_surcharge_title()+": $"+checkRateList.get(position).getCs_surcharge();
                strForTooltip2 = checkRateList.get(position).getCs_tax1_title()+": $"+checkRateList.get(position).getCs_tax1();
                strForTooltip3 = checkRateList.get(position).getCs_tax2_title()+": $"+checkRateList.get(position).getCs_tax2();
                openToolTip(mActivity,holder.tv_amount,"bottom",strForTooltip,strForTooltip1,strForTooltip2,strForTooltip3);

            }
        });*/

        //holder.rb_select.setChecked(lastSelectedPosition == position);

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_row;
        public ImageView img_delet,img_delivery_status;
        public TextView tv_transaction_no,tv_ship_day,tv_ship_date,tv_to_name,tv_delivery_status,tv_delivery_day,tv_delivery_date;


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

            ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, HistoryDetailsActivity.class);
                    mActivity.startActivity(intent);
                }
            });

           /* rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    Toast.makeText(CheckRateAdapter.this.mActivity,
                            "selected offer is " + tv_service_desc.getText(),
                            Toast.LENGTH_LONG).show();
                }
            });*/

        }
    }
}

package com.example.aspl.fortunecourier.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.model.Rates;

import java.util.List;

/**
 * Created by aspl on 18/12/17.
 */

public class ShipmentHistoryAdapter  extends RecyclerView.Adapter<ShipmentHistoryAdapter.ViewHolder> {


    private List<Rates> checkRateList;
    private Activity mActivity;
    private String strForTooltip,strForTooltip1,strForTooltip2,strForTooltip3;
    private int mPosition;
    // private Context context;


    private int lastSelectedPosition = -1;

    public ShipmentHistoryAdapter(List<Rates> offersListIn, Activity activity) {
        checkRateList = offersListIn;
        mActivity = activity;
    }


    @Override
    public ShipmentHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_check_rate, parent, false);

        ShipmentHistoryAdapter.ViewHolder viewHolder = new ShipmentHistoryAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ShipmentHistoryAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_service_desc,tv_amount,tv_delivery_timestamp;
        public RadioButton rb_select;
        public LinearLayout ll_row;


        public ViewHolder(View view) {
            super(view);

            ll_row = (LinearLayout) view.findViewById(R.id.ll_row);
            tv_service_desc = (TextView) view.findViewById(R.id.tv_service_desc);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            // Rates rates =  checkRateList.get(mPosition);

           /* tv_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openToolTip(mActivity,tv_amount,"bottom",strForTooltip,strForTooltip1,strForTooltip2,strForTooltip3);
                }
            });*/
            tv_delivery_timestamp = (TextView) view.findViewById(R.id.tv_delivery_timestamp);

            rb_select = (RadioButton) view.findViewById(R.id.rb_select);
            rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    Toast.makeText(ShipmentHistoryAdapter.this.mActivity,
                            "selected offer is " + tv_service_desc.getText(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

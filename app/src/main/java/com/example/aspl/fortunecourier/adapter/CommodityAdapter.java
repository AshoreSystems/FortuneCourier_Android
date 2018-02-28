package com.example.aspl.fortunecourier.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.DBHelper.DBCommodity;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.CommodityCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.CommodityDetailsCustomerActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.CommissionDetails;
import com.example.aspl.fortunecourier.model.Commodity;
import com.example.aspl.fortunecourier.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class CommodityAdapter  extends RecyclerView.Adapter<CommodityAdapter.ViewHolder> {


    private List<Commodity> commodityList;
    private Activity mActivity;
    private ProgressDialog progressBar;
    private SessionManager mSessionManager;
    private CommodityAdapter commodityAdapter;


    public CommodityAdapter(List<Commodity> offersListIn, Activity activity) {
        commodityList = offersListIn;
        mActivity = activity;
        mSessionManager = new SessionManager(activity);
    }


    @Override
    public CommodityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_commodity, parent, false);

        CommodityAdapter.ViewHolder viewHolder = new CommodityAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CommodityAdapter.ViewHolder holder, final int position) {

        Commodity commodity = commodityList.get(position);

        holder.tv_commodity_description.setText(mActivity.getResources().getString(R.string.hint_commodity_description)+" : "+commodity.getCommodity_desc());
        holder.tv_customs_value.setText(mActivity.getResources().getString(R.string.lbl_customs_value)+commodity.getCustoms_value()+" "+ commodity.getCustoms_value_unit());
        holder.tv_quantity.setText(mActivity.getResources().getString(R.string.hint_quantity)+commodity.getQuantity());
        holder.tv_weight.setText(mActivity.getResources().getString(R.string.lbl_weight)+commodity.getCommodity_weight()+" "+commodity.getCommodity_weight_unit());
        final String strId = commodity.getCommodity_id();
        final int mPosition = position;

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity,CommodityCustomerActivity.class).putExtra("action","edit").putExtra("id",strId));
               // mActivity.finish();
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOnDelet(mActivity,strId,mPosition);

            }
        });


       /* holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              *//*  Intent intent = new Intent(mActivity, HistoryDetailsAssociateActivity.class).putExtra("ShipmentId",historyList.get(position).getShipmentId());
                mActivity.startActivity(intent);*//*
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return commodityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_commodity_description,tv_customs_value,tv_quantity,tv_weight;
        public ImageView img_edit,img_delete;

        public ViewHolder(View view) {
            super(view);

            tv_commodity_description = (TextView) view.findViewById(R.id.tv_commodity_description);
            tv_customs_value = (TextView) view.findViewById(R.id.tv_customs_value);
            tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
            tv_weight = (TextView) view.findViewById(R.id.tv_weight);

            img_edit = (ImageView) view.findViewById(R.id.img_edit);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);

        }
    }

    private void showDialogOnDelet(Context context,final String strId,final int mPosition){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.custom_dialog_logout, null);
        alertDialog.setView(convertView);

        TextView popup_title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView popup_message = (TextView) convertView.findViewById(R.id.tv_message);

        Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = alertDialog.create();
        popup_title.setText(mActivity.getResources().getString(R.string.logout));
        popup_message.setText(mActivity.getResources().getString(R.string.delete_commodity_text));
        dialog.setCanceledOnTouchOutside(false);

        // for back button
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DBCommodity dbCommodity = new DBCommodity(mActivity);
                dbCommodity.deleteCommodity(strId);

                commodityList.remove(mPosition);
                notifyDataSetChanged();
                updateCommodityDetails();
                dialog.dismiss();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public void updateCommodityDetails(){
        DBCommodity dbCommodity = new DBCommodity(mActivity);
        LinearLayout ll_commodity_details = (LinearLayout) (mActivity).findViewById(R.id.ll_commodity_details);
        TextView tv_total_quantity =  (TextView) (mActivity).findViewById(R.id.tv_total_quantity);
        TextView tv_total_weight =  (TextView) (mActivity).findViewById(R.id.tv_total_weight);
        TextView tv_total_custom_value =  (TextView) (mActivity).findViewById(R.id.tv_total_custom_value);

        if(dbCommodity.getCommodityCount()>0){
            ll_commodity_details.setVisibility(View.VISIBLE);
            Commodity commodity = dbCommodity.getSumOfShipment();

            tv_total_quantity.setText("Total Quantity : "+commodity.getQuantity());
            tv_total_weight.setText("Total Weight : "+commodity.getCommodity_weight()+" "+commodity.getCommodity_weight_unit());
            tv_total_custom_value.setText("Total Custom Value : "+commodity.getCustoms_value_unit()+" "+commodity.getCustoms_value());
           /* if(((CommodityDetailsCustomerActivity) mActivity).getTotalShipmentWeight()>Double.valueOf(commodity.getCommodity_weight())||((CommodityDetailsCustomerActivity) mActivity).getTotalCarriageValue()>Double.valueOf(commodity.getCustoms_value())){
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(mActivity,mActivity.getResources().getString(R.string.app_name),mActivity.getResources().getString(R.string.err_msg_commodity));
                customDialogForHelp.show();
            }*/
        }else {
            ll_commodity_details.setVisibility(View.GONE);
        }
    }


}


package com.example.aspl.fortunecourier.adapter;

/**
 * Created by aspl on 28/11/17.
 */

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.model.Rates;
import com.michael.easydialog.EasyDialog;

import java.util.List;


public class CheckRateAdapter extends RecyclerView.Adapter<CheckRateAdapter.ViewHolder> {

    private List<Rates> checkRateList;
    private Activity mActivity;
    private String strForTooltip,strForTooltip1,strForTooltip2,strForTooltip3;
    private int mPosition;
   // private Context context;


    public int lastSelectedPosition = -1;

    public CheckRateAdapter(List<Rates> offersListIn, Activity activity) {
        checkRateList = offersListIn;
        mActivity = activity;
    }


    @Override
    public CheckRateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_check_rate, parent, false);

        CheckRateAdapter.ViewHolder viewHolder = new CheckRateAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final  CheckRateAdapter.ViewHolder holder,
                                 final int position) {
        Rates rates =  checkRateList.get(position);
        mPosition = position;
        holder.tv_service_desc.setText(rates.getServiceDescription());
        //holder.tv_amount.setText(rates.getAmount());
        holder.tv_delivery_timestamp.setText(rates.getDeliveryDate());

        SpannableString content = new SpannableString("$"+rates.getAmount());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.tv_amount.setText(content);
        //mPosition = position;

        holder.tv_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strForTooltip = "BaseAmount : $"+checkRateList.get(position).getBaseAmount();
                strForTooltip1 = checkRateList.get(position).getCs_surcharge_title()+": $"+checkRateList.get(position).getCs_surcharge();
                strForTooltip2 = checkRateList.get(position).getCs_tax1_title()+": $"+checkRateList.get(position).getCs_tax1();
                strForTooltip3 = checkRateList.get(position).getCs_tax2_title()+": $"+checkRateList.get(position).getCs_tax2();
                openToolTip(mActivity,holder.tv_amount,"bottom",strForTooltip,strForTooltip1,strForTooltip2,strForTooltip3);

            }
        });

        holder.rb_select.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return  checkRateList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_service_desc,tv_amount,tv_delivery_timestamp;
        public RadioButton rb_select;
        public LinearLayout ll_row;


        public ViewHolder(View view) {
            super(view);

            ll_row = (LinearLayout) view.findViewById(R.id.ll_row);
            tv_service_desc = (TextView) view.findViewById(R.id.tv_service_desc);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);

            tv_delivery_timestamp = (TextView) view.findViewById(R.id.tv_delivery_timestamp);

            rb_select = (RadioButton) view.findViewById(R.id.rb_select);
            rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                   /* Toast.makeText(CheckRateAdapter.this.mActivity,
                            "selected offer is " + tv_service_desc.getText(),
                            Toast.LENGTH_LONG).show();*/
                }
            });
        }
    }

    public void openToolTip(Activity activity, View viewToolTip,String topOrBottom,String strForTooltip,String strForTooltip1,String strForTooltip2,String strForTooltip3 ){

        View view = activity.getLayoutInflater().inflate(R.layout.layout_tooltip, null);
        TextView tv_mesg=(TextView)view.findViewById(R.id.tv_mesg);
        tv_mesg.setText(strForTooltip);

        TextView tv_mesg1=(TextView)view.findViewById(R.id.tv_mesg1);
      /*  tv_mesg1.setSelected(true);
        tv_mesg1.setMovementMethod(new ScrollingMovementMethod());*/
        tv_mesg1.setText(Html.fromHtml(strForTooltip1));

        TextView tv_mesg2=(TextView)view.findViewById(R.id.tv_mesg2);
        tv_mesg2.setText(Html.fromHtml(strForTooltip2));

        TextView tv_mesg3=(TextView)view.findViewById(R.id.tv_mesg3);
        tv_mesg3.setText(Html.fromHtml(strForTooltip3));

        int gravity;
        if(topOrBottom.equalsIgnoreCase("top")){
            gravity= EasyDialog.GRAVITY_TOP;
        }else{
            gravity=EasyDialog.GRAVITY_BOTTOM;
        }
        new EasyDialog(activity)
                .setLayout(view)
                .setBackgroundColor(Color.BLACK)
                .setLocationByAttachedView(viewToolTip)
                .setGravity(gravity)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMarginLeftAndRight(0, 0)
                .setOutsideColor(Color.TRANSPARENT)
                .show();

       /* Tooltip tooltip = new Tooltip.Builder(viewToolTip)
                .setText(strForTooltip1+"\n"+strForTooltip2+"\n"+strForTooltip3)
                .show();*/

    }


}
package com.example.aspl.fortunecourier.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.aspl.fortunecourier.DBHelper.DBCommodity;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.CommodityDetailsCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.PaymentCustomerActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.CardDetails;
import com.example.aspl.fortunecourier.model.Commodity;
import com.example.aspl.fortunecourier.model.Rates;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;
import com.michael.easydialog.EasyDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class SaveCardsAdapter extends RecyclerView.Adapter<SaveCardsAdapter.ViewHolder> {

    private List<CardDetails> checkRateList;
    private Activity mActivity;
    private String strForTooltip,strForTooltip1,strForTooltip2,strForTooltip3;
    private int mPosition;
    private SessionManager mSessionManager;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;
    private ArrayList<Rates> ratesArrayList;
    private ArrayList<HashMap<String, String>> dataMapOfPackages;
    private ArrayList<HashMap<String,String>> dataMapOfSpecialServices;



    public int lastSelectedPosition = -1;

    public SaveCardsAdapter(List<CardDetails> offersListIn, Activity activity) {
        checkRateList = offersListIn;
        mActivity = activity;
        mSessionManager = new SessionManager(mActivity);
        cd = new ConnectionDetector(mActivity);
        dataMapOfPackages = new ArrayList<HashMap<String, String>>();
    }


    @Override
    public SaveCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_save_cards, parent, false);

        SaveCardsAdapter.ViewHolder viewHolder = new SaveCardsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( SaveCardsAdapter.ViewHolder holder,
                                  int position) {
        //Rates rates =  checkRateList.get(position);
        CardDetails cardDetails = checkRateList.get(position);
        mPosition = position;
        holder.tv_card_number.setText(Html.fromHtml(cardDetails.getCard_no_last_digits()));

        holder.rb_select.setChecked(lastSelectedPosition == position);

       if(position==lastSelectedPosition){
           if(cardDetails.getCard_no_last_digits().equalsIgnoreCase("Use new card")){
               holder.ll_cvv.setVisibility(View.GONE);
               layoutHideShow(cardDetails.getCard_no_last_digits());
           }else {
               holder.ll_cvv.setVisibility(View.VISIBLE);
               layoutHideShow(cardDetails.getCard_no_last_digits());
           }

        }else {
           holder.ll_cvv.setVisibility(View.GONE);
          // layoutHideShow(cardDetails.getCard_no_last_digits());
       }

    }

    @Override
    public int getItemCount() {
        return  checkRateList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_card_number;
        public RadioButton rb_select;
        public LinearLayout ll_cvv;
        public EditText editText_cvv;


        public ViewHolder(View view) {
            super(view);

            ll_cvv = (LinearLayout) view.findViewById(R.id.ll_cvv);
            tv_card_number = (TextView) view.findViewById(R.id.tv_card_number);
            editText_cvv = (EditText) view.findViewById(R.id.editText_cvv);

            rb_select = (RadioButton) view.findViewById(R.id.rb_select);
            rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                   /* if(rb_select.isChecked()){
                        ll_cvv.setVisibility(View.VISIBLE);
                    }else {
                        ll_cvv.setVisibility(View.GONE);

                    }*/

                    notifyDataSetChanged();


                    /*if(getAdapterPosition()==lastSelectedPosition){
                       ll_cvv.setVisibility(View.VISIBLE);
                    }else {
                        ll_cvv.setVisibility(View.GONE);
                    }*/
                   // ll_cvv.setVisibility(View.VISIBLE);



                }
            });
        }
    }

    public void layoutHideShow(String str){
        LinearLayout ll_card_view = (LinearLayout) ((PaymentCustomerActivity)mActivity).findViewById(R.id.ll_card_view);
       if(str.equalsIgnoreCase("Use new card")){
           ll_card_view.setVisibility(View.VISIBLE);
       } else {
           ll_card_view.setVisibility(View.GONE);

       }
    }

}

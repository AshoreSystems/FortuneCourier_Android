package com.example.aspl.fortunecourier.adapter;

/**
 * Created by aspl on 28/11/17.
 */

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
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


public class CheckRateAssociateAdapter extends RecyclerView.Adapter<CheckRateAssociateAdapter.ViewHolder> {

    private List<Rates> checkRateList;
    private Activity mActivity;
    private String strForTooltip,strForTooltip1,strForTooltip2,strForTooltip3;
    private int mPosition;
    private SessionManager mSessionManager;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;
    private ArrayList<HashMap<String, String>> dataMapOfPackages;
    private ArrayList<HashMap<String,String>> dataMapOfSpecialServices;



    // private Context context;


    public int lastSelectedPosition = -1;

    public CheckRateAssociateAdapter(List<Rates> offersListIn, Activity activity) {
        checkRateList = offersListIn;
        mActivity = activity;
        mSessionManager = new SessionManager(mActivity);
        cd = new ConnectionDetector(mActivity);
        dataMapOfPackages = new ArrayList<HashMap<String, String>>();

    }


    @Override
    public CheckRateAssociateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_check_rate, parent, false);

        CheckRateAssociateAdapter.ViewHolder viewHolder = new CheckRateAssociateAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final  CheckRateAssociateAdapter.ViewHolder holder,
                                 final int position) {
        Rates rates =  checkRateList.get(position);
        mPosition = position;
        holder.tv_service_desc.setText(Html.fromHtml(rates.getServiceDescription()));

        //holder.tv_service_desc.setText(rates.getServiceDescription());
        //holder.tv_amount.setText(rates.getAmount());
        if(!rates.getDeliveryDate().isEmpty()){
            holder.tv_delivery_timestamp.setText(rates.getDeliveryDate());
        }else {
            holder.tv_delivery_timestamp.setText("Delivery Date Time Not Available.");
        }

        SpannableString content = new SpannableString("$"+rates.getAmount());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.tv_amount.setText(content);
        //mPosition = position;

       /* if(AppConstant.IS_FROM_CALCULATE_RATES && AppConstant.IS_FROM_CREATE_SHIPMENT) {
            if (checkRateList.get(position).getServiceDescription().equalsIgnoreCase(mSessionManager.getStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION))) {
                lastSelectedPosition = position;
                //notifyDataSetChanged();
            }
        }*/
       /* else {
            lastSelectedPosition = position;
            notifyDataSetChanged();
            //holder.rb_select.setChecked(false);
           // notifyDataSetChanged();
            Toast.makeText(mActivity,"else "+checkRateList.get(position).getServiceDescription()+"-"+mSessionManager.getStringData(SessionManager.KEY_SELECTED_SERVICE_DESCRIPTION),Toast.LENGTH_LONG).show();

        }*/

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
                    if(AppConstant.noOfSpecialServices.size()>0) {

                        if (cd.isConnectingToInternet()) {
                            getSpecialServices(checkRateList.get(lastSelectedPosition).getServiceDescription());

                        } else {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(mActivity, mActivity.getResources().getString(R.string.app_name), mActivity.getResources().getString(R.string.err_msg_internet));
                            customDialogForHelp.show();
                        }
                    }
                   // getSpecialServices(checkRateList.get(lastSelectedPosition).getServiceDescription());
                   /* Toast.makeText(CheckRateAssociateAdapter.this.mActivity,
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


    }

    private void getSpecialServices(final String strServiceType){
        progressBar = new ProgressDialog(mActivity);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = mActivity.getResources().getString(R.string.url_domain_associate) + mActivity.getResources().getString(R.string.url_check_special_services);

        Map<String, String> params = new HashMap<String, String>();

        if(!mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")) {
            params.put(JSONConstant.CR_CARRIER_CODE,mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
            params.put(JSONConstant.TO_A_COUNTRY_CODE, mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE));
            params.put(JSONConstant.FROM_A_COUNTRY_CODE, mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE));
            params.put("request_type","check_rates");
            params.put(JSONConstant.SERVICE_TYPE,strServiceType);
        }else {
            params.put(JSONConstant.CR_CARRIER_CODE, mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
            params.put("request_type", "check_rates");
            params.put(JSONConstant.SERVICE_TYPE, strServiceType);
            params.put(JSONConstant.FROM_A_COUNTRY_CODE, mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE));
            params.put(JSONConstant.FROM_A_ZIPCODE, mSessionManager.getStringData(SessionManager.KEY_F_ZIP_CODE));
            params.put(JSONConstant.TO_A_COUNTRY_CODE, mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE));
            params.put(JSONConstant.TO_A_ZIPCODE, mSessionManager.getStringData(SessionManager.KEY_T_ZIP_CODE));
            params.put(JSONConstant.PACKAGECOUNT, mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT));
            params.put(JSONConstant.SERVICESHIPDATE, mSessionManager.getStringData(SessionManager.KEY_SHIP_DATE));
            params.put(JSONConstant.PACKAGING_TYPE, mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE));
            params.put(JSONConstant.SERVICE_TYPE,strServiceType);

            if (mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT) != null && mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")) {
                params.put(JSONConstant.CONTAINER, "");
                params.put("Shape", mSessionManager.getStringData(SessionManager.KEY_SHAPE));

                params.put(JSONConstant.SIZE, mSessionManager.getStringData(SessionManager.KEY_SIZES));
            }

            params.put(JSONConstant.DROP_OFF_TYPE, mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP));
            params.put(JSONConstant.PACKAGING_TYPE_DESCRIPTION, mSessionManager.getStringData(SessionManager.KEY_PACKAGE_TYPE_NAME));
            params.put(JSONConstant.DROPOFF_TYPE_DESCRIPTION, mSessionManager.getStringData(SessionManager.KEY_PICKUP_DROP_NAME));

            if (mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)) {
                params.put(JSONConstant.IS_IDENTICAL, "1");
            } else {
                params.put(JSONConstant.IS_IDENTICAL, "0");
            }


            params.put(JSONConstant.CR_CARRIER_CODE, mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));


            if (mSessionManager.getBooleanData(SessionManager.KEY_IS_IDENTICAL)) {
                if (!AppConstant.hashMapPackage1.isEmpty()) {
                    dataMapOfPackages.add(AppConstant.hashMapPackage1);
                }
            } else {
                if (!AppConstant.hashMapPackage1.isEmpty()) {
                    if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("1")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);

                    } else if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("2")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);

                    } else if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("3")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);
                        dataMapOfPackages.add(AppConstant.hashMapPackage3);

                    } else if (mSessionManager.getStringData(SessionManager.KEY_PACKAGE_COUNT).equalsIgnoreCase("4")) {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);
                        dataMapOfPackages.add(AppConstant.hashMapPackage3);
                        dataMapOfPackages.add(AppConstant.hashMapPackage4);

                    } else {
                        dataMapOfPackages.add(AppConstant.hashMapPackage1);
                        dataMapOfPackages.add(AppConstant.hashMapPackage2);
                        dataMapOfPackages.add(AppConstant.hashMapPackage3);
                        dataMapOfPackages.add(AppConstant.hashMapPackage4);
                        dataMapOfPackages.add(AppConstant.hashMapPackage5);

                    }
                }
            }

            try {
                List<JSONObject> jsonObj = new ArrayList<JSONObject>();

                for (HashMap<String, String> data : dataMapOfPackages) {
                    JSONObject obj = new JSONObject(data);
                    jsonObj.add(obj);
                }

                JSONArray test = new JSONArray(jsonObj);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Packages", test);

                System.out.println(jsonObject.toString());
                params.put(JSONConstant.PACKAGES, test.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                List<JSONObject> jsonObj = new ArrayList<JSONObject>();

                for (HashMap<String, String> data : AppConstant.noOfSpecialServices) {
                    JSONObject obj = new JSONObject(data);
                    jsonObj.add(obj);
                }

                JSONArray test = new JSONArray(jsonObj);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("special_services", test);

                params.put("special_services", test.toString());


                System.out.println(jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            params.put(JSONConstant.FROM_A_STATE, "");
            params.put(JSONConstant.FROM_A_ADDRESS_LINE1, "");
            params.put(JSONConstant.FROM_A_CITY, "");
            params.put(JSONConstant.TO_A_STATE, "");
            params.put(JSONConstant.TO_A_ADDRESS_LINE1, "");
            params.put(JSONConstant.TO_A_CITY, "");


            if (mSessionManager.getStringData(SessionManager.KEY_T_COUNTRY_CODE).equalsIgnoreCase("US") && mSessionManager.getStringData(SessionManager.KEY_F_COUNTRY_CODE).equalsIgnoreCase("US")) {
                params.put("customs_currency", "");
                params.put("customs_value", "");
                params.put("customs_description", "");

            } else {
                params.put("customs_currency", mSessionManager.getStringData(SessionManager.KEY_CURRENCY_UNIT));
                params.put("customs_value", mSessionManager.getStringData(SessionManager.KEY_CUSTOM_VALUE));
                params.put("customs_description", mSessionManager.getStringData(SessionManager.KEY_CUSTOM_DESC));

            }
        }

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(mActivity, URL,params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.dismiss();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());
                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                    if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                        progressBar.dismiss();
                        if(mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE).equalsIgnoreCase("USPS")) {
                            JSONArray customOptnList = Json_response.getJSONArray("special_services");

                            dataMapOfSpecialServices = new ArrayList<>();

                            for (int i = 0; i < customOptnList.length(); i++) {
                                JSONObject eachData = customOptnList.getJSONObject(i);

                                HashMap<String, String> hashMapForSpecialServices = new HashMap<String, String>();
                                hashMapForSpecialServices.put("special_service", eachData.getString("special_service"));
                                hashMapForSpecialServices.put("special_service_description", eachData.getString("special_service_description"));

                                if (eachData.has("selected_option")) {
                                    hashMapForSpecialServices.put("selected_option", eachData.getString("selected_option"));
                                    hashMapForSpecialServices.put("selected_option_description", eachData.getString("selected_option_description"));

                                } else {
                                    hashMapForSpecialServices.put("selected_option", " ");
                                    hashMapForSpecialServices.put("selected_option_description", " ");

                                }
                                hashMapForSpecialServices.put("special_service_price", eachData.getString("special_service_price"));
                                hashMapForSpecialServices.put("CurrencyUnit", eachData.getString("CurrencyUnit"));
                                dataMapOfSpecialServices.add(hashMapForSpecialServices);
                            }
                            AppConstant.noOfSpecialServices = dataMapOfSpecialServices;

                            System.out.println("##" + dataMapOfSpecialServices.toString());
                        }
                    }else {
                        JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                        progressBar.dismiss();
                        CustomDialogForHelp customDialogForHelp =new CustomDialogForHelp(mActivity,mActivity.getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.dismiss();
                }
            }
        });
    }


    /*private void getSpecialServices(final String strServiceType){
        progressBar = new ProgressDialog(mActivity);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        String URL = mActivity.getResources().getString(R.string.url_domain_associate) + mActivity.getResources().getString(R.string.url_check_special_services);

        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.CR_CARRIER_CODE,mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE));
        params.put(JSONConstant.TO_A_COUNTRY_CODE,AppConstant.T_A_COUNTRY_CODE);
        params.put(JSONConstant.FROM_A_COUNTRY_CODE,AppConstant.F_A_COUNTRY_CODE);
        params.put("request_type","check_rates");
        params.put(JSONConstant.SERVICE_TYPE,strServiceType);


        try {
            List<JSONObject> jsonObj = new ArrayList<JSONObject>();

            for(HashMap<String, String> data :  AppConstant.noOfSpecialServices) {
                JSONObject obj = new JSONObject(data);
                jsonObj.add(obj);
            }

            JSONArray test = new JSONArray(jsonObj);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("special_services",test);

            params.put("special_services",test.toString());
            System.out.println(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("=>"+params.toString());

        VolleyUtils.POST_METHOD(mActivity, URL,params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.dismiss();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());
                try {
                    JSONObject Json_response = new JSONObject(response.toString());
                if(Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)){
                    progressBar.dismiss();
                  *//*  CustomDialogForHelp customDialogForHelp =new CustomDialogForHelp(mActivity,mActivity.getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                    customDialogForHelp.show();*//*
                    //showSpecialServices("Special Services",response.toString());
                    //progressBar.dismiss();
                }else {
                    JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);
                    progressBar.dismiss();
                    CustomDialogForHelp customDialogForHelp =new CustomDialogForHelp(mActivity,mActivity.getResources().getString(R.string.app_name),jsonObject.getString(JSONConstant.MESSAGE));
                    customDialogForHelp.show();
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.dismiss();
                }


            }
        });
    }*/


}
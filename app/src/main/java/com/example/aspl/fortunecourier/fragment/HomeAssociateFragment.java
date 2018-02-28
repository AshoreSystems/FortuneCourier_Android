package com.example.aspl.fortunecourier.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.associate.CheckRatesFromAssociateActivity;
import com.example.aspl.fortunecourier.activity.associate.CreateShipmentFromAssociateActivity;
import com.example.aspl.fortunecourier.activity.associate.ShipmentHistoryAssociateActivity;
import com.example.aspl.fortunecourier.activity.associate.TrackShipmentAssociateActivity;
import com.example.aspl.fortunecourier.activity.customer.ShipmentHistoryCustomerActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.example.aspl.fortunecourier.utility.VolleyResponseListener;
import com.example.aspl.fortunecourier.utility.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aspl on 5/1/18.
 */

public class HomeAssociateFragment extends Fragment implements View.OnClickListener{

    private LinearLayout ll_calculate_rates,ll_generate_shipment,ll_view_shipment_history,ll_track_shipment,ll_flash_view;
    private TextView tv_flash_message;
    private SessionManager mSessionManager;
    private ProgressDialog progressBar;
    private ConnectionDetector cd;



    public HomeAssociateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mSessionManager = new SessionManager(getActivity());
        cd = new ConnectionDetector(getActivity());

        ll_calculate_rates = (LinearLayout) rootView.findViewById(R.id.ll_calculate_rates);
        ll_calculate_rates.setOnClickListener(this);

        ll_generate_shipment = (LinearLayout) rootView.findViewById(R.id.ll_generate_shipment);
        ll_generate_shipment.setOnClickListener(this);

        ll_view_shipment_history = (LinearLayout) rootView.findViewById(R.id.ll_view_shipment_history);
        ll_view_shipment_history.setOnClickListener(this);

        ll_track_shipment = (LinearLayout) rootView.findViewById(R.id.ll_track_shipment);
        ll_track_shipment.setOnClickListener(this);

        ll_flash_view = (LinearLayout) rootView.findViewById(R.id.ll_flash_view);

        tv_flash_message = (TextView) rootView.findViewById(R.id.tv_flash_message);

        ll_flash_view.setVisibility(View.INVISIBLE);

        if(cd.isConnectingToInternet()){
            getFlashText();
        }else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
        }

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_calculate_rates:

                //AppConstant.IS_FROM_CALCULATE_RATES = true;
                mSessionManager.putBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES,true);

                AppConstant.IS_FROM_CREATE_SHIPMENT = false;
                startActivity(new Intent(getActivity(), CheckRatesFromAssociateActivity.class));
                break;
            case R.id.ll_generate_shipment:
                //AppConstant.IS_FROM_CALCULATE_RATES =false;
                mSessionManager.putBooleanData(SessionManager.KEY_IS_FROM_CALCULATE_RATES,false);

                // uncomment below line on 5th Jan 2018
                AppConstant.IS_FROM_CREATE_SHIPMENT = true;
                startActivity(new Intent(getActivity(),CreateShipmentFromAssociateActivity.class));

                break;
            case R.id.ll_view_shipment_history:
                startActivity(new Intent(getActivity(), ShipmentHistoryAssociateActivity.class));
                break;

            case R.id.ll_track_shipment:
                startActivity(new Intent(getActivity(), TrackShipmentAssociateActivity.class));
                break;
        }
    }

    private void getFlashText() {
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();

        String URL = getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_dashboard);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConstant.C_ID, mSessionManager.getStringData(SessionManager.KEY_C_ID));
        System.out.println("=>" + params.toString());

        VolleyUtils.POST_METHOD(getActivity(), URL, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.dismiss();
            }

            @Override
            public void onResponse(Object response) {
                System.out.println("==Volley Response===>>" + response.toString());

                try {
                    JSONObject Json_response = new JSONObject(response.toString());

                    if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                       if(Json_response.getString("flash_text").isEmpty()){
                           ll_flash_view.setVisibility(View.INVISIBLE);
                       }else {
                           ll_flash_view.setVisibility(View.VISIBLE);

                           tv_flash_message.setText(Json_response.getString("flash_text"));
                       }
                        // Toast.makeText(getActivity(), Json_response.getString(JSONConstant.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(getActivity(), getResources().getString(R.string.app_name), Json_response.getString(JSONConstant.MESSAGE));
                        customDialogForHelp.show();
                      /*  JSONObject jsonObject = Json_response.getJSONObject(JSONConstant.ERROR_MESSAGES);

                        if (jsonObject.has(JSONConstant.MESSAGE)) {
                            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(getActivity(), getResources().getString(R.string.app_name), jsonObject.getString(JSONConstant.MESSAGE));
                            customDialogForHelp.show();
                        }*/
                    }
                    progressBar.dismiss();
                } catch (JSONException e) {
                    progressBar.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }
}


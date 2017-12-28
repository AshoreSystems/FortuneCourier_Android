package com.example.aspl.fortunecourier.fragment;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.CheckRatesFromActivity;
import com.example.aspl.fortunecourier.activity.customer.CreateShipmentFromActivity;
import com.example.aspl.fortunecourier.activity.customer.ShipmentHistoryActivity;
import com.example.aspl.fortunecourier.utility.AppConstant;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private LinearLayout ll_calculate_rates,ll_generate_shipment,ll_view_shipment_history;

    public HomeFragment() {
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

        ll_calculate_rates = (LinearLayout) rootView.findViewById(R.id.ll_calculate_rates);
        ll_calculate_rates.setOnClickListener(this);

        ll_generate_shipment = (LinearLayout) rootView.findViewById(R.id.ll_generate_shipment);
        ll_generate_shipment.setOnClickListener(this);

        ll_view_shipment_history = (LinearLayout) rootView.findViewById(R.id.ll_view_shipment_history);
        ll_view_shipment_history.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_calculate_rates:
                AppConstant.IS_FROM_CALCULATE_RATES = true;
                AppConstant.IS_FROM_CREATE_SHIPMENT = false;
                startActivity(new Intent(getActivity(), CheckRatesFromActivity.class));
                break;
            case R.id.ll_generate_shipment:
                AppConstant.IS_FROM_CALCULATE_RATES =false;

                startActivity(new Intent(getActivity(), CreateShipmentFromActivity.class));
                break;
            case R.id.ll_view_shipment_history:
                startActivity(new Intent(getActivity(), ShipmentHistoryActivity.class));
                break;
        }
    }
}

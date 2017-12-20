package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.aspl.fortunecourier.R;

/**
 * Created by aspl on 18/12/17.
 */

public class ShipmentHistoryActivity extends Activity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_history);
    }

    private void init(){
        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                finish();
                break;
        }
    }
}

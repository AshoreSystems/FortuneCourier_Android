package com.example.aspl.fortunecourier.activity.customer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.adapter.CheckRateAdapter;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.model.Rates;

import java.util.ArrayList;

/**
 * Created by aspl on 28/11/17.
 */

public class CheckRateScreenActivity extends Activity implements View.OnClickListener{
    private RecyclerView recycler_checklist;
    private ImageView img_info ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rate_screen);
        init();

    }

    private void init(){

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        recycler_checklist = (RecyclerView) findViewById(R.id.recycler_checklist);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recycler_checklist.setLayoutManager(recyclerLayoutManager);

       DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_checklist.getContext(), recyclerLayoutManager.getOrientation());
        recycler_checklist.addItemDecoration(dividerItemDecoration);

        ArrayList<Rates> ciArr = (ArrayList) getIntent().getExtras().getParcelableArrayList("RateList");

        CheckRateAdapter recyclerViewAdapter = new CheckRateAdapter(ciArr,CheckRateScreenActivity.this);
        recycler_checklist.setAdapter(recyclerViewAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_layout_back:
                finish();
                break;

            case R.id.img_info:
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(CheckRateScreenActivity.this,"Help",getResources().getString(R.string.info));
                customDialogForHelp.show();
                break;
        }
    }
}

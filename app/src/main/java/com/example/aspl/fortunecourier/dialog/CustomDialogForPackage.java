package com.example.aspl.fortunecourier.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.model.PackageDetails;

/**
 * Created by aspl on 30/11/17.
 */

public class CustomDialogForPackage extends Dialog implements View.OnClickListener {

    public Activity c;
    public Button btn_submit;
    public TextView tv_title;
    private String titleShow;
    private EditText editText_weight,editText_declared_value;
    private TextInputLayout textInput_weight,textInput_declared_value;

    public CustomDialogForPackage(Activity a, String title) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        titleShow=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_for_package);
        setCancelable(false);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText(titleShow);

        editText_weight = (EditText) findViewById(R.id.editText_weight);
        editText_declared_value = (EditText) findViewById(R.id.editText_declared_value);

        textInput_weight = (TextInputLayout) findViewById(R.id.textInput_weight);
        textInput_declared_value = (TextInputLayout) findViewById(R.id.textInput_declared_value);


        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {

            case R.id.btn_submit:
                validateData();
                break;

            default:
                break;
        }
        //dismiss();
    }

    public PackageDetails validateData(){
        PackageDetails packageDetails = new PackageDetails(titleShow);
        if(editText_weight.getText().toString().isEmpty()){

        }else if(editText_declared_value.getText().toString().isEmpty()){

        }else {
            packageDetails.setWeight(editText_weight.getText().toString().trim());
            packageDetails.setWeightUnit("kg");
            packageDetails.setDeclaredValue(editText_declared_value.getText().toString().trim());
            dismiss();
        }
        return packageDetails;
    }
}

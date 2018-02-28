package com.example.aspl.fortunecourier.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;

/**
 * Created by aspl on 1/12/17.
 */

public class CustomDialogForHelp  extends Dialog implements View.OnClickListener {

    public Activity c;
    public Button btn_ok;
    public TextView tv_title,tv_message;
    private String titleShow,messageShow;
    public CustomDialogForHelp(Activity a, String title, String message) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        titleShow=title;
        messageShow=message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_help);
        setCancelable(false);

        btn_ok = (Button) findViewById(R.id.btn_ok);

        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText(titleShow);

        tv_message=(TextView)findViewById(R.id.tv_message);
        tv_message.setText(messageShow);
        //tv_message.setGravity(Gravity.FILL_HORIZONTAL);

        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {

            case R.id.btn_ok:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }
}

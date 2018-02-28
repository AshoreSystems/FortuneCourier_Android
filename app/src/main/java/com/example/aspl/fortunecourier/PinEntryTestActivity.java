package com.example.aspl.fortunecourier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.aspl.fortunecourier.utility.PinEntryEditText;

public class PinEntryTestActivity extends AppCompatActivity {
    PinView pinView ;
    Button btn_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pin_entry);
        pinView = (PinView) findViewById(R.id.pinView);
        btn_show = (Button) findViewById(R.id.btn_show_toast);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PinEntryTestActivity.this,pinView.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });




      /* final PinEntryEditText txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry1);
        //txtPinEntry.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        txtPinEntry.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("123456")) {
                    Toast.makeText(PinEntryTestActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else if (s.length() == "123456".length()) {
                    Toast.makeText(PinEntryTestActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                    txtPinEntry.setText(null);
                }
            }
        });*/
    }
}

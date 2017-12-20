package com.example.aspl.fortunecourier.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.SessionManager;

/**
 * Created by aspl on 16/11/17.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {
    View view;

    private SessionManager mSessionManager;
    private ConnectionDetector cd;
    private EditText editText_old_password,editText_new_password,editText_confirm_password;
    private Button btn_submit;
    private ProgressDialog progressBar;
    private TextInputLayout textInput_old_password,textInput_new_password,textInput_confirm_password;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_reset_password,null);
            init();
        return view;

    }

    private void init(){
        mSessionManager = new SessionManager(getActivity());
        cd = new ConnectionDetector(getActivity());

        textInput_old_password = (TextInputLayout)view. findViewById(R.id.textInput_old_password);
        textInput_old_password.setVisibility(View.VISIBLE);
        textInput_new_password = (TextInputLayout)view. findViewById(R.id.textInput_new_password);
        textInput_confirm_password = (TextInputLayout)view. findViewById(R.id.textInput_confirm_password);
        editText_old_password = (EditText)view. findViewById(R.id.editText_old_password);
        editText_new_password = (EditText)view. findViewById(R.id.editText_new_password);
        editText_confirm_password = (EditText)view. findViewById(R.id.editText_confirm_password);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                validateAndSubmit();
                break;

        }
    }

    private void validateAndSubmit(){
        if(editText_old_password.getText().toString().trim().isEmpty() || editText_old_password.getText().toString().trim().length() < 7 || editText_old_password.getText().toString().trim().length() > 10){
            textInput_old_password.setError(getResources().getString(R.string.err_msg_password));
            requestFocus(editText_new_password);
        }else if (editText_new_password.getText().toString().trim().isEmpty() || editText_new_password.getText().toString().trim().length() < 7 || editText_new_password.getText().toString().trim().length() > 10) {
            textInput_new_password.setError(getResources().getString(R.string.err_msg_password));
            textInput_old_password.setErrorEnabled(false);
            requestFocus(editText_new_password);
        } else if (editText_confirm_password.getText().toString().trim().isEmpty() || editText_confirm_password.getText().toString().trim().length() < 7 || editText_confirm_password.getText().toString().trim().length() > 10 || !(editText_confirm_password.getText().toString().trim().equals(editText_new_password.getText().toString().trim()))) {
            textInput_confirm_password.setError(getResources().getString(R.string.err_msg_confirm_password));
            textInput_new_password.setErrorEnabled(false);
            requestFocus(editText_confirm_password);
        } else {
            textInput_confirm_password.setErrorEnabled(false);
        }
    }

    //request after error
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}

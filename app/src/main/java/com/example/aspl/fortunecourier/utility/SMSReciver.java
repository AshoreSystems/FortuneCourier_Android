package com.example.aspl.fortunecourier.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.aspl.fortunecourier.activity.customer.OTPVerificationCustomerActivity;

/**
 * Created by aspl on 9/11/17.
 */

public class SMSReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    try {
                        if (senderNum.equals("044152")) {
                            OTPVerificationCustomerActivity Sms = new OTPVerificationCustomerActivity();
                            Sms.recivedSms(message );
                        }
                        /*if (senderNum.equals("TA-DOCOMO")) {
                            Otp Sms = new Otp();
                            Sms.recivedSms(message );
                        }*/
                    } catch(Exception e){}
                }
            }
        } catch (Exception e) {}
    }
}

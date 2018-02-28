package com.example.aspl.fortunecourier;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.aspl.fortunecourier.activity.associate.DashboardAssociateActivity;
import com.example.aspl.fortunecourier.activity.associate.LoginAssociateActivity;
import com.example.aspl.fortunecourier.activity.customer.DashboardCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.LandingCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.LoginCustomerActivity;
import com.example.aspl.fortunecourier.utility.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aspl on 27/10/17.
 */

public class SplashActivity extends Activity {

    private SessionManager mSessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        init();
        //printKeyHash(this);
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    private void init(){
        mSessionManager =  new SessionManager(SplashActivity.this);
        nextScreen();
    }

    private void nextScreen(){
        // Splash screen timer
        int splash_time_out = 3000;

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if (mSessionManager.getIntData(SessionManager.KEY_WHICH_USER) == 1) {

                    if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)) {
                        if (mSessionManager.getBooleanData(SessionManager.KEY_IS_C_LOGOUT)) {
                            Intent i = new Intent(SplashActivity.this, LoginCustomerActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashActivity.this, DashboardCustomerActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }else {
                        Intent i = new Intent(SplashActivity.this, LandingCustomerActivity.class);
                        startActivity(i);
                    }
                } else if (mSessionManager.getIntData(SessionManager.KEY_WHICH_USER) == 2) {
                    if (mSessionManager.getBooleanData(SessionManager.KEY_IS_A_REMEMBERED)) {
                        if (mSessionManager.getBooleanData(SessionManager.KEY_IS_A_LOGOUT)) {
                            Intent i = new Intent(SplashActivity.this, LoginAssociateActivity.class);
                            startActivity(i);
                        } else {

                            Intent i = new Intent(SplashActivity.this, DashboardAssociateActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                           /* Intent i = new Intent(SplashActivity.this, DashboardAssociateActivity.class);
                            startActivity(i);*/
                        }
                    }else {
                        Intent i = new Intent(SplashActivity.this, LandingCustomerActivity.class);
                        startActivity(i);
                    }
                } else {
                    Intent i = new Intent(SplashActivity.this, LandingCustomerActivity.class);
                    startActivity(i);
                }

               /* if(mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)){
                    if(mSessionManager.getBooleanData(SessionManager.KEY_IS_C_LOGOUT)){
                        Intent i = new Intent(SplashActivity.this, LoginCustomerActivity.class);
                        startActivity(i);
                    }else {
                        Intent i = new Intent(SplashActivity.this, DashboardCustomerActivity.class);
                        startActivity(i);
                    }
                }else if(mSessionManager.getBooleanData(SessionManager.KEY_IS_A_REMEMBERED)){
                    if(mSessionManager.getBooleanData(SessionManager.KEY_IS_A_LOGOUT)){
                        Intent i = new Intent(SplashActivity.this, LoginAssociateActivity.class);
                        startActivity(i);
                    }else {
                        Intent i = new Intent(SplashActivity.this, DashboardAssociateActivity.class);
                        startActivity(i);
                    }
                }else {
                    Intent i = new Intent(SplashActivity.this, LandingCustomerActivity.class);
                    startActivity(i);
                }*/

                // This method will be executed once the timer is over
                // Start your app main activity
              /*  if(mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)&&mSessionManager.getBooleanData(SessionManager.KEY_IS_C_LOGOUT)){
                    Intent i = new Intent(SplashActivity.this, LoginCustomerActivity.class);
                    startActivity(i);
                }else if(mSessionManager.getBooleanData(SessionManager.KEY_IS_C_REMEMBERED)&&!mSessionManager.getBooleanData(SessionManager.KEY_IS_C_LOGOUT)){
                    Intent i = new Intent(SplashActivity.this, DashboardCustomerActivity.class);
                    startActivity(i);
                }if(mSessionManager.getBooleanData(SessionManager.KEY_IS_A_REMEMBERED)&&mSessionManager.getBooleanData(SessionManager.KEY_IS_A_LOGOUT)){
                    Intent i = new Intent(SplashActivity.this, LoginCustomerActivity.class);
                    startActivity(i);
                }else if(mSessionManager.getBooleanData(SessionManager.KEY_IS_A_REMEMBERED)&&!mSessionManager.getBooleanData(SessionManager.KEY_IS_A_LOGOUT)){
                    Intent i = new Intent(SplashActivity.this, DashboardAssociateActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(SplashActivity.this, LandingCustomerActivity.class);
                    startActivity(i);
                }*/
                // close this activity
                finish();
            }
        }, splash_time_out);
    }
}

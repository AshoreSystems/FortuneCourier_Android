package com.example.aspl.fortunecourier.activity.associate;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.BuildConfig;
import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.CheckRatesFromCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.DashboardCustomerActivity;
import com.example.aspl.fortunecourier.activity.customer.HistoryDetailsCustomerActivity;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by aspl on 5/2/18.
 */

public class LabelAssociateActivity extends Activity implements View.OnClickListener {

    private TextView tv_message,tv_download_label;
    private String strStatus="",strMessage="",strPdf="",strTrackingId="";
    private ProgressDialog progressBar;
    private ConnectionDetector cd;
    private static final int  MEGABYTE = 1024 * 1024;
    private View header_layout_back;
    private Button btn_back_home;
    private ImageView img_info;
    private SessionManager mSessionManager;
    private String strCarrierCode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        init();

    }

    private void init(){
        cd= new ConnectionDetector(LabelAssociateActivity.this);
        mSessionManager = new SessionManager(LabelAssociateActivity.this);

        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_download_label = (TextView) findViewById(R.id.tv_download_label);
        tv_download_label.setOnClickListener(this);

        btn_back_home = (Button) findViewById(R.id.btn_back_home);
        btn_back_home.setOnClickListener(this);

        header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);


        String udata=getResources().getString(R.string.btn_download_label_here);
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        tv_download_label.setText(content);

        strCarrierCode= mSessionManager.getStringData(SessionManager.KEY_CARRIER_CODE);

        strStatus = getIntent().getStringExtra(JSONConstant.STATUS);

        if(strStatus.equalsIgnoreCase(JSONConstant.SUCCESS))
        {
            strTrackingId = getIntent().getStringExtra(JSONConstant.MASTER_TRACKING_NO);
            strMessage = getIntent().getStringExtra(JSONConstant.MESSAGE);
            strPdf = getIntent().getStringExtra("pdf");
            tv_message.setText(strMessage);
            tv_download_label.setVisibility(View.VISIBLE);

        }else {
            strMessage = getIntent().getStringExtra(JSONConstant.MESSAGE);
            tv_download_label.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.tv_download_label:
                if(cd.isConnectingToInternet()){
                    try{
                        if(Build.VERSION.SDK_INT>=23){
                            int permissionCheckCamera1 = ContextCompat.checkSelfPermission(LabelAssociateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionCheckCamera1 != PackageManager.PERMISSION_GRANTED ){
                                ActivityCompat.requestPermissions(LabelAssociateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }else{
                                showProgressBar();
                                if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                                    new DownloadFile().execute(strPdf, strTrackingId + ".gif");
                                }else {
                                    new DownloadFile().execute(strPdf, strTrackingId+".pdf");
                                }                            }
                        }else{
                            showProgressBar();
                            if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                                new DownloadFile().execute(strPdf, strTrackingId + ".gif");
                            }else {
                                new DownloadFile().execute(strPdf, strTrackingId+".pdf");
                            }                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(LabelAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
                    customDialogForHelp.show();
                }
                break;

            case R.id.header_layout_back:
                i = new Intent(LabelAssociateActivity.this, DashboardAssociateActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;

            case R.id.btn_back_home:
                i = new Intent(LabelAssociateActivity.this, DashboardAssociateActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;

            case R.id.img_info:
                DBInfo dbInfo = new DBInfo(LabelAssociateActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(LabelAssociateActivity.this,"Help",dbInfo.getDescription("Payment Success Screen"));
                customDialogForHelp.show();
                break;
        }

    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FortuneCourier");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            //FileDownloader.downloadFile(fileUrl, pdfFile);
            downloadFile(fileUrl, pdfFile);
            return null;
        }
    }


    public  void downloadFile(String fileUrl, File directory){
        try {

            java.net.URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();

            closeProgressBar();

           /* File pdfFile = new File(Environment.getExternalStorageDirectory() + "/FortuneCourier/" + strTrackingId+".pdf");  // -> filename = maven.pdf
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri apkURI = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);
            pdfIntent.setDataAndType(apkURI, "application/pdf");
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(HistoryDetailsAssociateActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }*/

            File pdfFile;
            if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                pdfFile = new File(Environment.getExternalStorageDirectory() + "/FortuneCourier/" + strTrackingId + ".gif");  // -> filename = maven.pdf
            }else {
                pdfFile = new File(Environment.getExternalStorageDirectory() + "/FortuneCourier/" + strTrackingId + ".pdf");  // -> filename = maven.pdf
            }

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri apkURI = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);

            if(strCarrierCode != null && strCarrierCode.equalsIgnoreCase("UPS")) {
                pdfIntent.setDataAndType(apkURI, "image/*");
            }else {
                pdfIntent.setDataAndType(apkURI, "application/pdf");
            }

            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(LabelAssociateActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showProgressBar(){
        progressBar = new ProgressDialog(LabelAssociateActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
    }

    private void closeProgressBar(){
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LabelAssociateActivity.this, DashboardAssociateActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}

package com.example.aspl.fortunecourier.activity.associate;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.DBHelper.DBInfo;
import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.dialog.CustomDialogForHelp;
import com.example.aspl.fortunecourier.utility.CameraUtility;
import com.example.aspl.fortunecourier.utility.ConnectionDetector;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;


public class ContactUsAssociateActivity extends AppCompatActivity implements View.OnClickListener {


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private static int img = 0;

    ProgressDialog pDialog;
    Uri img1 = null, img2 = null, img3 = null;
    Bitmap bitmapEmpImage, myimage;

    SessionManager mSessionManager;
    ConnectionDetector cd;

    EditText editText_enter_message;
    TextView tv_add_screenshots;
    ImageView img_first, img_close_first, img_second, img_close_second, img_third, img_close_third, img_info;
    Button btn_send_message;
    String strMessage;
    String strResponse ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        init();
    }

    private void init() {
        mSessionManager = new SessionManager(ContactUsAssociateActivity.this);
        cd = new ConnectionDetector(ContactUsAssociateActivity.this);

        editText_enter_message = (EditText) findViewById(R.id.editText_enter_message);

        img_first = (ImageView) findViewById(R.id.img_first);
        img_first.setOnClickListener(this);

        img_close_first = (ImageView) findViewById(R.id.img_close_first);
        img_close_first.setOnClickListener(this);

        img_second = (ImageView) findViewById(R.id.img_second);
        img_second.setOnClickListener(this);

        img_close_second = (ImageView) findViewById(R.id.img_close_second);
        img_close_second.setOnClickListener(this);

        img_third = (ImageView) findViewById(R.id.img_third);
        img_third.setOnClickListener(this);

        img_close_third = (ImageView) findViewById(R.id.img_close_third);
        img_close_third.setOnClickListener(this);

        btn_send_message = (Button) findViewById(R.id.btn_send_message);
        btn_send_message.setOnClickListener(this);

        View header_layout_back = findViewById(R.id.header_layout_back);
        header_layout_back.setOnClickListener(this);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_send_message:
                hideKeyboard();
                validateAndSubmit();
                break;
            case R.id.img_first:
                hideKeyboard();
                img = 1;
                selectImage();
                break;
            case R.id.img_close_first:
                hideKeyboard();
                img_first.setImageResource(R.drawable.add);
                img_close_first.setVisibility(View.GONE);
                img1 = null;
                break;
            case R.id.img_second:
                hideKeyboard();
                img = 2;
                selectImage();
                break;
            case R.id.img_close_second:
                hideKeyboard();
                img_second.setImageResource(R.drawable.add);
                img_close_second.setVisibility(View.GONE);
                img1 = null;
                break;
            case R.id.img_third:
                hideKeyboard();
                img = 3;
                selectImage();
                break;
            case R.id.img_close_third:
                hideKeyboard();
                img_third.setImageResource(R.drawable.add);
                img_close_third.setVisibility(View.GONE);
                img1 = null;
                break;

            case R.id.header_layout_back:
                hideKeyboard();
                finish();
                break;

            case R.id.img_info:
                hideKeyboard();
                DBInfo dbInfo = new DBInfo(ContactUsAssociateActivity.this);
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ContactUsAssociateActivity.this, "Help", dbInfo.getDescription("Contact US Screen"));
                customDialogForHelp.show();
                break;

        }

    }

    private void validateAndSubmit() {
        if (cd.isConnectingToInternet()) {
            if (editText_enter_message.getText().toString().trim().isEmpty()) {
                CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ContactUsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_enter_message));
                customDialogForHelp.show();
               // Snackbar.make(btn_send_message, getResources().getString(R.string.err_msg_enter_message), Snackbar.LENGTH_SHORT).show();
            } else {
                strMessage = editText_enter_message.getText().toString().trim();
                JSONParser mJSONParser = new JSONParser();
                mJSONParser.execute();
            }
        } else {
            CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ContactUsAssociateActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.err_msg_internet));
            customDialogForHelp.show();
            //Snackbar.make(btn_send_message, getResources().getString(R.string.err_msg_internet), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactUsAssociateActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CameraUtility.checkPermission(ContactUsAssociateActivity.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public class JSONParser extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ContactUsAssociateActivity.this);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(getResources().getString(R.string.url_domain_associate) + getResources().getString(R.string.url_contact_us));
            FileBody first = null, second = null, third = null;


            MultipartEntity reqEntity = new MultipartEntity();
            StringBody message = null;
            StringBody user_id = null;
           /* StringBody device_id = null;
            StringBody lang = null;*/

            String firstString = "";
            try {
                if (!img1.equals(null)) {
                    firstString = getFilePath(ContactUsAssociateActivity.this, img1);
                    first = new FileBody(new File(firstString));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String secondString = "";

            try {
                if (!img2.equals(null)) {
                    secondString = getFilePath(ContactUsAssociateActivity.this, img2);
                    second = new FileBody(new File(secondString));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String thirdString = "";

            try {
                if (!img3.equals(null)) {
                    thirdString = getFilePath(ContactUsAssociateActivity.this, img3);
                    third = new FileBody(new File(thirdString));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                message = new StringBody(strMessage);
                user_id = new StringBody(mSessionManager.getStringData(SessionManager.KEY_A_ID));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!firstString.equals("") || img1 != null) {
                try {
                    reqEntity.addPart("attachment1", first);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    reqEntity.addPart("attachment2", second);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    reqEntity.addPart("attachment3", third);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reqEntity.addPart("cu_message", message);
                reqEntity.addPart("a_id", user_id);

            } else {
                reqEntity.addPart("cu_message", message);
                reqEntity.addPart("a_id", user_id);

            }

            httppost.setEntity(reqEntity);

            // DEBUG
            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = null;
            HttpEntity resEntity = null;
            try {
                response = httpclient.execute(httppost);
                resEntity = response.getEntity();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // DEBUG
            if (response != null) {
                System.out.println(response.getStatusLine());
            }
            if (resEntity != null) {
                try {
                    strResponse = EntityUtils.toString(resEntity);

                   // System.out.println(EntityUtils.toString(resEntity));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } // end if

            if (resEntity != null) {
                try {
                    resEntity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } // end if

            httpclient.getConnectionManager().shutdown();
            return strResponse;
        }

        protected void onPostExecute(String page) {
            try {
                if (pDialog != null) {
                    pDialog.dismiss();
                    pDialog = null;
                }
                //pDialog.dismiss();
                JSONObject Json_response = new JSONObject(page);
                if (Json_response.getString(JSONConstant.STATUS).equalsIgnoreCase(JSONConstant.SUCCESS)) {
                   /* Snackbar.make(btn_send_message, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
                    thread.start();*/
                    Toast.makeText(ContactUsAssociateActivity.this,Json_response.getString(JSONConstant.MESSAGE),Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    CustomDialogForHelp customDialogForHelp = new CustomDialogForHelp(ContactUsAssociateActivity.this,getResources().getString(R.string.app_name),Json_response.getString(JSONConstant.MESSAGE));
                    customDialogForHelp.show();
                   // Snackbar.make(btn_send_message, Json_response.getString(JSONConstant.MESSAGE), Snackbar.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //pDialog.dismiss();

            //DashboardActivity.start(ContactUsActivity.this);
        }
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CameraUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Uri image = data.getData();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
                //img_profile_picture.setImageURI(image);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            //img_profile_picture.setImageURI(image);

        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // bitmapEmpImage = getRoundedShape(thumbnail);
        myimage = thumbnail;
        // imgProfilePicture.setImageBitmap(myimage);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = data.getData();


        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(ContactUsAssociateActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // bitmapEmpImage = getRoundedShape(bm);
        myimage = bm;
        if (img == 1) {
            img_first.setImageBitmap(myimage);
            img_close_first.setVisibility(View.VISIBLE);
            //Add image uri in variable to send to service
            img1 = selectedImage;
        } else if (img == 2) {
            img_second.setImageBitmap(myimage);
            img_close_second.setVisibility(View.VISIBLE);

            //Add image uri in variable to send to service
            img2 = selectedImage;
        } else if (img == 3) {
            img_third.setImageBitmap(myimage);
            img_close_third.setVisibility(View.VISIBLE);

            //Add image uri in variable to send to service
            img3 = selectedImage;
        }

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkProvidedPermission() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            //OR   if (MyVersion > 22)
            boolean hasFilePermission = (ContextCompat.checkSelfPermission(ContactUsAssociateActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);


            if (!hasFilePermission) {
                ActivityCompat.requestPermissions(ContactUsAssociateActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET
                        },
                        10);
            }
        }
    }
}
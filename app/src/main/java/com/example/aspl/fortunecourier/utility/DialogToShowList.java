package com.example.aspl.fortunecourier.utility;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspl on 29/11/17.
 */

public class DialogToShowList {

    private Activity mActivity;
    private String strItem,strTitle;
    private List<String> mArrayList;
    private int position;
    public DialogToShowList(Activity activity,List<String> arrayList,String title) {

        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.strTitle = title;
    }

    public DialogToShowList(Activity activity) {

        this.mActivity = activity;
    }

    // Custom Dialog with List
    public String ShowCustomDialogwithList() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_customdialogwithlist, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        Window window = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        final TextView textView = (TextView) dialogView.findViewById(R.id.tv_title);
        textView.setText(strTitle);

        final ListView listView = (ListView) dialogView.findViewById(R.id.listview);

        /*  ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Donut");
        arrayList.add("Eclair");
        arrayList.add("Froyo");
        arrayList.add("Gingerbread");*/



       /* // Defined Array values to show in ListView
        String[] values = new String[] { "Cupcake",
                "Donut",
                "Eclair",
                "Froyo",
                "Gingerbread"ArrayList
        };*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_list_item_1, android.R.id.text1, mArrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                strItem    = (String) listView.getItemAtPosition(position);
                //position  = position;

               /* // Show Alert
                Toast.makeText(mActivity,
                        "Position :"+itemPosition+"  ListItem : " +strItem , Toast.LENGTH_LONG)
                        .show();*/
                alertDialog.dismiss();
                setItemSelectionPosition(itemPosition);


                //return itemValue;

            }

        });

       // set alert dialog in center
        // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom

       /* // Cancel Button
        Button cancel_btn = (Button) dialogView.findViewById(R.id.buttoncancellist);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });*/

        alertDialog.show();
        return strItem;
    }

    public void setItemSelectionPosition(int itemPosition){
        position = itemPosition;
    }

    public int getItemSelectedPosition(){
        return position;
    }

}

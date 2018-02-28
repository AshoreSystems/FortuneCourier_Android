package com.example.aspl.fortunecourier.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.model.SpecialServicesOptions;

import java.util.List;

/**
 * Created by aspl on 24/1/18.
 */

public class SpecialServicesRadioOptionsAdapter extends
        RecyclerView.Adapter<SpecialServicesRadioOptionsAdapter.ViewHolder> {

    private List<SpecialServicesOptions> filterList;
    private Context context;
    private int selected;

    public SpecialServicesRadioOptionsAdapter(List<SpecialServicesOptions> filterModelList
            , Context ctx,int selected) {
        filterList = filterModelList;
        context = ctx;
        selected = selected;
    }

    @Override
    public SpecialServicesRadioOptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_radiobutton_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (selected ==1){
            if(filterList.get(position).is_selected()){
                holder.brandName.setText(filterList.get(position).getSignature_option_description());
                if(filterList.get(position).is_selected()){
                    holder.selectionState.setChecked(true);
                }
            }

        }else if (selected ==2){
            holder.brandName.setText(filterList.get(position).getSignature_option_description());
            if(filterList.get(position).is_selected()){
                holder.selectionState.setChecked(true);
            }
        }

/*
        holder.brandName.setText(filterList.get(position).getSignature_option_description());
       *//* if (filterList.get(position).isSelected()) {
            holder.selectionState.setChecked(true);
        } else {
            holder.selectionState.setChecked(false);
        }*//*

        if(filterList.get(position).is_selected()){
            holder.selectionState.setChecked(true);
        }*/
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView brandName;
        public RadioButton selectionState;

        public ViewHolder(View view) {
            super(view);


            brandName = (TextView) view.findViewById(R.id.tv_name);
            selectionState = (RadioButton) view.findViewById(R.id.radio);

            //item click event listener
            view.setOnClickListener(this);

            //checkbox click event handling
            selectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(SpecialServicesRadioOptionsAdapter.this.context,
                                "selected brand is " + brandName.getText(),
                                Toast.LENGTH_LONG).show();
                    } else {

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            TextView brndName = (TextView) v.findViewById(R.id.tv_name);
            //show more information about brand
        }
    }
}
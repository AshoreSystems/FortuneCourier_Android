package com.example.aspl.fortunecourier.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.model.SpecialService;

import java.util.List;

/**
 * Created by aspl on 24/1/18.
 */

public class SpecialServicesCBAdapter extends
        RecyclerView.Adapter<SpecialServicesCBAdapter.ViewHolder> {

    private List<SpecialService> filterList;
    private Context context;

    private final OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemClick(SpecialService item);
    }


    public SpecialServicesCBAdapter(List<SpecialService> filterModelList
            , Context ctx, OnItemClickListener listener) {
        filterList = filterModelList;
        context = ctx;
        this.listener = listener;

    }

    @Override
    public SpecialServicesCBAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_checkbox, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       /* holder.brandName.setText(filterList.get(position).getSpecial_service_description());
       *//* if (filterList.get(position).isSelected()) {
            holder.selectionState.setChecked(true);
        } else {
            holder.selectionState.setChecked(false);
        }*//*

        if(filterList.get(position).isChecked()){
            holder.selectionState.setChecked(true);
        }*/

        holder.bind(filterList.get(position), listener);


    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView brandName;
        public CheckBox selectionState;

        public ViewHolder(View view) {
            super(view);
            brandName = (TextView) view.findViewById(R.id.tv_name);
            selectionState = (CheckBox) view.findViewById(R.id.cb);

            //item click event listener
            view.setOnClickListener(this);

            //checkbox click event handling
            selectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(SpecialServicesCBAdapter.this.context,
                                "selected brand is " + brandName.getText(),
                                Toast.LENGTH_LONG).show();
                    }else{

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            TextView brndName = (TextView) v.findViewById(R.id.tv_name);
            //show more information about brand
        }

        public void bind(final SpecialService item, final OnItemClickListener listener) {

            brandName.setText(item.getSpecial_service_description());

            if(item.isChecked()){
                selectionState.setChecked(true);
            }

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    listener.onItemClick(item);
                }
            });

        }
    }
}
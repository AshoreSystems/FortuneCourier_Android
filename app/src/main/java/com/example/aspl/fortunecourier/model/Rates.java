package com.example.aspl.fortunecourier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by aspl on 28/11/17.
 */

public class Rates implements Parcelable{
    private String serviceType, amount, serviceDescription, deliveryTimestamp;
    private String cs_tax1,cs_tax2,cs_surcharge,cs_tax1_title,cs_tax2_title,cs_surcharge_title;
    private String deliveryDate, baseAmount;

    private ArrayList<Rates> ratesArrayList;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getCs_tax1() {
        return cs_tax1;
    }

    public String getCs_surcharge_title() {
        return cs_surcharge_title;
    }

    public void setCs_surcharge_title(String cs_surcharge_title) {
        this.cs_surcharge_title = cs_surcharge_title;
    }

    public void setCs_tax1(String cs_tax1) {
        this.cs_tax1 = cs_tax1;
    }

    public String getCs_tax2() {
        return cs_tax2;
    }

    public void setCs_tax2(String cs_tax2) {
        this.cs_tax2 = cs_tax2;
    }

    public String getCs_surcharge() {
        return cs_surcharge;
    }

    public void setCs_surcharge(String surcharge) {
        this.cs_surcharge = cs_surcharge;
    }

    public String getCs_tax1_title() {
        return cs_tax1_title;
    }

    public void setCs_tax1_title(String cs_tax1_title) {
        this.cs_tax1_title = cs_tax1_title;
    }

    public String getCs_tax2_title() {
        return cs_tax2_title;
    }

    public void setCs_tax2_title(String cs_tax2_title) {
        this.cs_tax2_title = cs_tax2_title;
    }

    protected Rates(Parcel in) {
        serviceType = in.readString();
        amount = in.readString();
        serviceDescription = in.readString();
        deliveryDate = in.readString();
        deliveryTimestamp = in.readString();
        baseAmount = in.readString();
        cs_tax1 = in.readString();
        cs_tax1_title = in.readString();
        cs_tax2 = in.readString();
        cs_tax2_title = in.readString();
        cs_surcharge = in.readString();
        cs_surcharge_title = in.readString();
        ratesArrayList = in.createTypedArrayList(Rates.CREATOR);
    }

    public static final Creator<Rates> CREATOR = new Creator<Rates>() {
        @Override
        public Rates createFromParcel(Parcel in) {
            return new Rates(in);
        }

        @Override
        public Rates[] newArray(int size) {
            return new Rates[size];
        }
    };

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getDeliveryTimestamp() {
        return deliveryTimestamp;
    }

    public void setDeliveryTimestamp(String deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    public Rates(String serviceType, String amount, String serviceDescription, String deliveryDate,String deliveryTimestamp,String baseAmount,String cs_tax1,String cs_tax1_title,String cs_tax2, String cs_tax2_title,String cs_surcharge,String cs_surcharge_title) {
        this.serviceType = serviceType;
        this.amount = amount;
        this.serviceDescription = serviceDescription;
        this.deliveryDate = deliveryDate;
        this.deliveryTimestamp = deliveryTimestamp;
        this.baseAmount = baseAmount;
        this.cs_tax1 = cs_tax1;
        this.cs_tax1_title = cs_tax1_title ;
        this.cs_tax2 = cs_tax2;
        this.cs_tax2_title = cs_tax2_title;
        this.cs_surcharge = cs_surcharge;
        this.cs_surcharge_title = cs_surcharge_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceType);
        dest.writeString(amount);
        dest.writeString(serviceDescription);
        dest.writeString(deliveryDate);
        dest.writeString(deliveryTimestamp);
        dest.writeString(baseAmount);
        dest.writeString(cs_tax1);
        dest.writeString(cs_tax1_title);
        dest.writeString(cs_tax2);
        dest.writeString(cs_tax2_title);
        dest.writeString(cs_surcharge);
        dest.writeString(cs_surcharge_title);
        dest.writeTypedList(ratesArrayList);
    }
}

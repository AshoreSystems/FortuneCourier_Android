package com.example.aspl.fortunecourier.model;

import java.util.ArrayList;

/**
 * Created by aspl on 15/1/18.
 */

public class SpecialService {
    private String special_service;
    private String special_service_description;
    private String type;
    private String options;
    private String status_checkbox;
    private String signature_option_description;

    public String getSpecial_service_price() {
        return special_service_price;
    }

    public void setSpecial_service_price(String special_service_price) {
        this.special_service_price = special_service_price;
    }

    public String getSpecial_service_currency() {
        return special_service_currency;
    }

    public void setSpecial_service_currency(String special_service_currency) {
        this.special_service_currency = special_service_currency;
    }

    private String signature_option;
    private String special_service_price;
    private String special_service_currency;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSignature_option_description() {
        return signature_option_description;
    }

    public void setSignature_option_description(String signature_option_description) {
        this.signature_option_description = signature_option_description;
    }

    public String getSignature_option() {
        return signature_option;
    }

    public void setSignature_option(String signature_option) {
        this.signature_option = signature_option;
    }

    @Override
    public String toString() {
        return "SpecialService{" +
                "special_service='" + special_service + '\'' +
                ", special_service_description='" + special_service_description + '\'' +
                ", type='" + type + '\'' +
                ", options='" + options + '\'' +
                ", status_checkbox='" + status_checkbox + '\'' +
                ", status_radio='" + status_radio + '\'' +
                ", specialServicesOptionses=" + specialServicesOptionses +
                '}';
    }

    private String status_radio;

    public ArrayList<SpecialServicesOptions> getSpecialServicesOptionses() {
        return specialServicesOptionses;
    }

    public void setSpecialServicesOptionses(ArrayList<SpecialServicesOptions> specialServicesOptionses) {
        this.specialServicesOptionses = specialServicesOptionses;
    }

    private ArrayList<SpecialServicesOptions> specialServicesOptionses;



    public String getSpecial_service() {
        return special_service;
    }

    public void setSpecial_service(String special_service) {
        this.special_service = special_service;
    }

    public String getSpecial_service_description() {
        return special_service_description;
    }

    public void setSpecial_service_description(String special_service_description) {
        this.special_service_description = special_service_description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getStatus_checkbox() {
        return status_checkbox;
    }

    public void setStatus_checkbox(String status_checkbox) {
        this.status_checkbox = status_checkbox;
    }

    public String getStatus_radio() {
        return status_radio;
    }

    public void setStatus_radio(String status_radio) {
        this.status_radio = status_radio;
    }


//private String

}

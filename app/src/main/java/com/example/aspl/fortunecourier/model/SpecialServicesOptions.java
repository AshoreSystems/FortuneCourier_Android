package com.example.aspl.fortunecourier.model;

/**
 * Created by aspl on 16/1/18.
 */

public class SpecialServicesOptions {

    private String signature_option_description;
    private String signature_option;
    private boolean is_selected;

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

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}

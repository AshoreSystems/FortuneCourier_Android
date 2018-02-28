package com.example.aspl.fortunecourier.model;

/**
 * Created by aspl on 2/2/18.
 */

public class CardDetails {
    private String customer_id,card_no_last_digits;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCard_no_last_digits() {
        return card_no_last_digits;
    }

    public void setCard_no_last_digits(String card_no_last_digits) {
        this.card_no_last_digits = card_no_last_digits;
    }
}

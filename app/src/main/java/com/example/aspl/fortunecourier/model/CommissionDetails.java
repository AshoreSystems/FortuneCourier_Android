package com.example.aspl.fortunecourier.model;

/**
 * Created by aspl on 7/2/18.
 */

public class CommissionDetails {
    private String commission,master_tracking_no,shipmentdate,shipment_id,currency;

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getMaster_tracking_no() {
        return master_tracking_no;
    }

    public void setMaster_tracking_no(String master_tracking_no) {
        this.master_tracking_no = master_tracking_no;
    }

    public String getShipmentdate() {
        return shipmentdate;
    }

    public void setShipmentdate(String shipmentdate) {
        this.shipmentdate = shipmentdate;
    }

    public String getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(String shipment_id) {
        this.shipment_id = shipment_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

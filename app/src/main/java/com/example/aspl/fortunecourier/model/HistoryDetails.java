package com.example.aspl.fortunecourier.model;

/**
 * Created by aspl on 20/12/17.
 */

public class HistoryDetails {

    private String shipmentId,masterTrackingNo,shipDay,shipDate,deliveryDay,deliveryDate,toName,deliveryStatus;

    private String fromCountry,fromState,toCountry,toState;
    private String fromCity,toCity;

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getMasterTrackingNo() {
        return masterTrackingNo;
    }

    public void setMasterTrackingNo(String transactionNo) {
        this.masterTrackingNo = transactionNo;
    }

    public String getShipDay() {
        return shipDay;
    }

    public void setShipDay(String shipDay) {
        this.shipDay = shipDay;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getDeliveryDay() {
        return deliveryDay;
    }

    public void setDeliveryDay(String deliveryDay) {
        this.deliveryDay = deliveryDay;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public HistoryDetails() {
    }

    /*public HistoryDetails(String transactionNo, String shipDay, String shipDate, String deliveryDay, String deliveryDate, String toName, String deliveryStatus) {
        this.masterTrackingNo = transactionNo;
        this.shipDay = shipDay;
        this.shipDate = shipDate;
        this.deliveryDay = deliveryDay;
        this.deliveryDate = deliveryDate;
        this.toName = toName;
        this.deliveryStatus = deliveryStatus;
    }*/

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}

package com.example.aspl.fortunecourier.model;

/**
 * Created by ${Harshada} on ${08Feb2018}.
 */

public class Commodity {
    private String commodity_desc,unit_of_measures,quantity,commodity_weight,commodity_weight_unit,
    customs_value,customs_value_unit,country_of_manufacture,harmonized_code,export_license_no,expiration_date,isTotals;

    private String commodity_id;

    public String getIsTotals() {
        return isTotals;
    }

    public void setIsTotals(String isTotals) {
        this.isTotals = isTotals;
    }

    public String getCommodity_id() {
        return commodity_id;
    }



    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String getCommodity_desc() {
        return commodity_desc;
    }

    public void setCommodity_desc(String commodity_desc) {
        this.commodity_desc = commodity_desc;
    }

    public String getUnit_of_measures() {
        return unit_of_measures;
    }

    public void setUnit_of_measures(String unit_of_measures) {
        this.unit_of_measures = unit_of_measures;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCommodity_weight() {
        return commodity_weight;
    }

    public void setCommodity_weight(String commodity_weight) {
        this.commodity_weight = commodity_weight;
    }

    public String getCommodity_weight_unit() {
        return commodity_weight_unit;
    }

    public void setCommodity_weight_unit(String commodity_weight_unit) {
        this.commodity_weight_unit = commodity_weight_unit;
    }

    public String getCustoms_value() {
        return customs_value;
    }

    public void setCustoms_value(String customs_value) {
        this.customs_value = customs_value;
    }

    public String getCustoms_value_unit() {
        return customs_value_unit;
    }

    public void setCustoms_value_unit(String customs_value_unit) {
        this.customs_value_unit = customs_value_unit;
    }

    public String getCountry_of_manufacture() {
        return country_of_manufacture;
    }

    public void setCountry_of_manufacture(String country_of_manufacture) {
        this.country_of_manufacture = country_of_manufacture;
    }

    public String getHarmonized_code() {
        return harmonized_code;
    }

    public void setHarmonized_code(String harmonized_code) {
        this.harmonized_code = harmonized_code;
    }

    public String getExport_license_no() {
        return export_license_no;
    }

    public void setExport_license_no(String export_license_no) {
        this.export_license_no = export_license_no;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }
}

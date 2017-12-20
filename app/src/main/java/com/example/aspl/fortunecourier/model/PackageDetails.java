package com.example.aspl.fortunecourier.model;

/**
 * Created by aspl on 30/11/17.
 */

public class PackageDetails {

    private String packageNo;
    private String weight;
    private String weightUnit;
    private String declaredValue;


    public PackageDetails(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getPackageNo() {
        return packageNo;

    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(String declaredValue) {
        this.declaredValue = declaredValue;
    }
}

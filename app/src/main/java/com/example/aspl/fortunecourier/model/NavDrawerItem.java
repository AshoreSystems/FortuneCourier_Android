package com.example.aspl.fortunecourier.model;

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int images;

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

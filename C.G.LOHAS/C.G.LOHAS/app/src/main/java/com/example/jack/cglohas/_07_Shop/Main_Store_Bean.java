package com.example.jack.cglohas._07_Shop;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Main_Store_Bean implements Serializable {

    private int storeid;
    private String storename;
    private String shortdesc;
    private String longdesc;

    public Main_Store_Bean() {
    }

    public Main_Store_Bean(int storeid, String storename, String shortdesc, String longdesc) {
        this.storeid = storeid;
        this.storename = storename;
        this.shortdesc = shortdesc;
        this.longdesc = longdesc;
    }

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getLongdesc() {
        return longdesc;
    }

    public void setLongdesc(String longdesc) {
        this.longdesc = longdesc;
    }





}

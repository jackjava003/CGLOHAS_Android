package com.example.jack.cglohas._07_Shop;

import java.io.Serializable;
import java.sql.Blob;

@SuppressWarnings("serial")
public class Shop implements Serializable {

    private int locationid;
    private int storeid;
    private String s_name;
    private Double lat;
    private Double longi;
    private Blob image;
    private String address;
    private String phone;

    public Shop() {

    }

    public Shop(int locationid, int storeid, String s_name, Double lat, Double longi, String address, String phone) {
        this.locationid = locationid;
        this.storeid = storeid;
        this.s_name = s_name;
        this.lat = lat;
        this.longi = longi;
        this.address = address;
        this.phone = phone;
    }



    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




}

package com.example.ggmap;

public class GwangjinStreetLight {
    private String name;
    private double latitude;
    private double longitude;

    public GwangjinStreetLight(){

    }

    public GwangjinStreetLight(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // set function
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public void setLongitude(double longitude){ this.longitude = longitude; }

    // get function
    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }
}

package com.google.firebase.ml.mddd.java.settings;

public class Taxibooking_data {
    String uid;
    String destination;
    String location;


    public Taxibooking_data() {
    }

    public Taxibooking_data(String uid, String destination, String location) {
        this.uid = uid;
        this.destination = destination;
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public String getDestination() {
        return destination;
    }

    public String getLocation() {
        return location;
    }

}

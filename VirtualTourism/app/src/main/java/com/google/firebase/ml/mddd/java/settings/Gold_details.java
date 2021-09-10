package com.google.firebase.ml.mddd.java.settings;

public class Gold_details {
    String rate;
    String availability;
    String Description;

    public Gold_details(String rate, String availability, String description) {
        this.rate = rate;
        this.availability = availability;
        Description = description;
    }

    public Gold_details() {
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}

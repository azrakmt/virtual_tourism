package com.google.firebase.ml.mddd.java;

public class monument_data {
    String name;
    String id;
    String description;
    String disp_image;
    String visitors;
    String approval;

    public monument_data(){

    }
    public monument_data(String name, String id, String description, String disp_image, String visitors,String approval) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.disp_image = disp_image;
        this.visitors = visitors;
        this.approval = approval;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDisp_image() {
        return disp_image;
    }

    public String getVisitors() {
        return visitors;
    }
}

package com.google.firebase.ml.mddd;
public class user_data {
 public    String name;
  public   String email;
  public   String phone;
  public   String sex;
  public  String country;
  public String propic_url;

    public user_data()
    {

    }

    public user_data(String name, String email, String phone,String sex,String country,String propic_url) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.sex=sex;
        this.country = country;
        this.propic_url = propic_url;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPropic_url() {
        return propic_url;
    }

    public String getPhone() {
        return phone;
    }
    public String getSex(){
        return sex;
    }
}

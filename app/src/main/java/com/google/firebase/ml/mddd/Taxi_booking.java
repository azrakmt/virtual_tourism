package com.google.firebase.ml.mddd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.ml.mddd.java.Taxi_list;

import java.util.ArrayList;

public class Taxi_booking extends AppCompatActivity {

    ArrayList<String> dp = new ArrayList<>();
    ArrayList<String> t_model = new ArrayList<>();
    ArrayList<String> t_name = new ArrayList<>();
    ArrayList<String> t_number = new ArrayList<>();
    ArrayList<Integer> t_rating = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_booking);
        added();
    }
    public void added()
    {
        t_model.add("car1");
        t_model.add("car3");
        t_model.add("car2");
        t_name.add("name1");
        t_name.add("name3");
        t_name.add("name2");
        t_number.add("kl_D_123");
        t_number.add("kl_D_124");
        t_number.add("kl_D_125");
        t_rating.add(5);
        t_rating.add(3);
        t_rating.add(4);
        dp.add("https://i.imgur.com/ZcLLrkY.jpg");
        dp.add("https://i.imgur.com/ZcLLrkY.jpg");
        dp.add("https://i.imgur.com/ZcLLrkY.jpg");
        recycler();
    }
    public void recycler()
    {
        RecyclerView recyclerView = findViewById(R.id.taxi_list);
        Taxi_list list = new Taxi_list(this,t_name,dp,t_number,t_model,t_rating);
        recyclerView.setAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goToHome(View view) {
        startActivity(new Intent(this,home.class));
        finish();
    }
}

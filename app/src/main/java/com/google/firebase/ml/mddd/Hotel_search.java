package com.google.firebase.ml.mddd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.ml.mddd.java.hotel_list;

import java.util.ArrayList;

public class Hotel_search extends AppCompatActivity {
    ArrayList<String> dp = new ArrayList<>();
    ArrayList<String> t_model = new ArrayList<>();
    ArrayList<String> t_name = new ArrayList<>();
    ArrayList<String> t_number = new ArrayList<>();
    ArrayList<Integer> t_rating = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search);
        added();
    }
    public void added()
    {
        t_model.add("hotel1");
        t_model.add("hotel3");
        t_model.add("hotel2");
        t_name.add("hayat");
        t_name.add("mayat");
        t_name.add("sataa");
        t_number.add("9544dkfh");
        t_number.add("z,dmfn");
        t_number.add("dnfjnv");
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
        hotel_list list = new hotel_list(this,t_name,dp,t_number,t_model,t_rating);
        recyclerView.setAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void goToHome(View view) {
        startActivity(new Intent(this,home.class));
        finish();
    }
}

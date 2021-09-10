package com.google.firebase.ml.mddd.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.mddd.R;

import java.util.ArrayList;

public class MonumentImages extends AppCompatActivity {

    ArrayList<String> images;
    String monument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_images);
        Bundle extra = getIntent().getExtras();
        monument = extra.getString("mname");
        get(monument);
    }

    void get(String id)
    {
        ArrayList<String> data = new ArrayList<>();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("MonumentsImages");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.child(id).getChildren())
                {
                   // Toast.makeText(MonumentImages.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    data.add(dataSnapshot.getValue().toString());
                }
                recycle(data);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void recycle(ArrayList<String> data)
    {
        RecyclerView recyclerView = findViewById(R.id.monumentImages);
        MimageInflator list = new MimageInflator(this,data);
        recyclerView.setAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }
}
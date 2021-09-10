package com.google.firebase.ml.mddd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.mddd.java.FirebaseMethods;
import com.google.firebase.ml.mddd.java.MonumentImages;
import com.google.firebase.ml.mddd.java.monument_data;

import java.util.ArrayList;

public class Search_result extends AppCompatActivity {
    TextView t1,t2;
    ImageView mimage;
    String monument;
    monument_data data;
    DatabaseReference databaseReference;
    Button b,b2;
    CollapsingToolbarLayout clayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        t1=findViewById(R.id.mon_name);
        t2=findViewById(R.id.mon_details);
        mimage = findViewById(R.id.disp_image);

       clayout = findViewById(R.id.ctoolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference("Monuments");
        Bundle extra = getIntent().getExtras();
        monument = extra.getString("mname");

        getData(monument);
        b = findViewById(R.id.imageresult);
        b2 = findViewById(R.id.direction);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data="";
                if(monument=="eiffletower")
                {
                    data="effiletower";
                }
                else
                {
                    data=monument;
                }
                Intent  i= new Intent(Search_result.this, MonumentImages.class) ;
                i.putExtra("mname", data);
                startActivity(i);
             // DatabaseReference  newdatabaseReference = FirebaseDatabase.getInstance().getReference("location");

//                newdatabaseReference.child(monument).child("lat").setValue("123");
//                newdatabaseReference.child(monument).child("lon").setValue("1234");

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data="";
                if(monument=="eiffeltower")
                {
                    data="effiletower";
                }
                else
                {
                    data=monument;
                }

                location(data);
            }
        });
    }
    public void getData(String mname){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = dataSnapshot.child(mname).getValue(monument_data.class);
                if(data!=null)
                {
                   t1.setText(data.getName());
                    clayout.setTitle(data.getName());
                   t2.setText(data.getDescription());
                  //  UniversalImageLoader.setImage(data.getDisp_image(),mimage,null,"");
                    Toast.makeText(getApplicationContext(),data.getDisp_image(),Toast.LENGTH_SHORT);
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(data.getDisp_image())
                            .into(mimage);
                }
                else {
                    add_monument(mname);
                    Toast.makeText(Search_result.this, "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void add_monument(String name1)
    {

        monument_data data;
        String name = name1;
        String id = "";
        String visitor = "0";
        String desc ="";
        data = new monument_data(name,id,desc,"",visitor,"0");
        FirebaseMethods firebaseMethods = new FirebaseMethods(this);
        firebaseMethods.addmonumenttofb(data);

    }

    void location(String id)
    {
        ArrayList<String> loca = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("location");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.child(id).getChildren())
                {
                    Toast.makeText(Search_result.this,dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    loca.add(dataSnapshot.getValue().toString());
                }
                String destinationLatitude = "27.1751";
                String destinationLongitude = "78.0421";
                String uri = "http://maps.google.com/maps?daddr=" + loca.get(0) + "," + loca.get(1) + " (" + monument + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

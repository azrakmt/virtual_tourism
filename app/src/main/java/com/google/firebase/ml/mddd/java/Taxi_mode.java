package com.google.firebase.ml.mddd.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.mddd.R;
import com.google.firebase.ml.mddd.java.settings.Taxibooking_data;
import com.google.firebase.ml.mddd.user_data;

import java.util.ArrayList;

public class Taxi_mode extends AppCompatActivity {

    ArrayList<String> dp = new ArrayList<>();
    final ArrayList<String> uid = new ArrayList<>();
    ArrayList<String> user_name = new ArrayList<>();
    ArrayList<String> phone_number = new ArrayList<>();
   final ArrayList<String> location = new ArrayList<>();
    ArrayList<String> destination = new ArrayList<>();
    RecyclerView rview;
    User_list list;
    DatabaseReference tbooking = FirebaseDatabase.getInstance().getReference("Taxi_Booking");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_mode);
        rview = findViewById(R.id.rlayout);
        add();
    }

    public void addvalue(Taxibooking_data data)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Userdata");
        databaseReference.addValueEventListener(new ValueEventListener()
                                                {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        user_data userData = dataSnapshot.child(data.getUid()).getValue(user_data.class);
                                                        add_to_taxi(userData);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                }
        );
    }
    public void add_to_taxi(user_data userData)
    {
        dp.add(userData.getPropic_url());
        user_name.add(userData.getName());
        phone_number.add(userData.getPhone());
        LayoutInflater();
    }
    public void add()
    {
        tbooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot tsanpshot : dataSnapshot.getChildren())
                {
                    Taxibooking_data taxibooking_data = tsanpshot.getValue(Taxibooking_data.class);
                    getbooked_uid(taxibooking_data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        LayoutInflater();

    }
    void getbooked_uid(Taxibooking_data uid_sample)
    {
     uid.add(uid_sample.getUid());
     destination.add(uid_sample.getDestination());
     location.add(uid_sample.getLocation());
     addvalue(uid_sample);

    }

    public void LayoutInflater()
    {
         list = new User_list(this,user_name,dp,phone_number,location,destination,uid);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rview);
        rview.setAdapter(list);
        rview.setLayoutManager(new LinearLayoutManager(this));

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            user_name.remove(viewHolder.getAdapterPosition());
            list.notifyItemRemoved(viewHolder.getAdapterPosition());

        }
    };
}

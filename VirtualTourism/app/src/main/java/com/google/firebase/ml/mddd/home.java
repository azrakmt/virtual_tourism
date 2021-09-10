package com.google.firebase.ml.mddd;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.mddd.java.NewRecycler;
import com.google.firebase.ml.mddd.java.monument_data;
import com.google.firebase.ml.mddd.java.settings.Taxibooking_data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class home extends AppCompatActivity {
    int arraysize;
    int count = 0;
    int largest = 0;
    ListView lv;
    RecyclerView recyclerView;
    //int[] visited ;

    DatabaseReference databaseReference;
    DatabaseReference newdatabaseReference;
    ProgressBar pbar;
    ImageButton side_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // lv = findViewById(R.id.listView);

        recyclerView  = findViewById(R.id.HomeRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Monuments");
        newdatabaseReference = FirebaseDatabase.getInstance().getReference("MonumentsImages");
        Bundle extra = getIntent().getExtras();
        arraysize =1;
        side_button = findViewById(R.id.sidebutton);
        test();
        bottomnav();
       // ListViewClicked(lv);
      //  ayo();

        side_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference.child("tajmahal").setValue(new monument_data("Taj Mahal","1","Statue in USA","","200000","2"));
//                databaseReference.child("leaningtowerofpisa").setValue(new monument_data("Leaning Tower of Pisa","2","Statue in USA","","200000","2"));
//                databaseReference.child("greatwallofchina").setValue(new monument_data("Great Wall of China","3","Statue in USA","","200000","2"));

                PopupMenu popupMenu = new PopupMenu(home.this,v);
                popupMenu.inflate(R.menu.topmenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.taxi_click :
//                                Intent  i= new Intent(home.this, Taxi_booking.class) ;
//                                startActivity(i);
                                taxibooking();
                                return true;
                            case R.id.hotel_click:
                                startActivity(new Intent(home.this,Hotel_search.class));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });

    }

    void  RecylcerFunction (ArrayList<String> image,ArrayList<String> name,ArrayList<String> visitor)
    {
        ArrayList<String> n = new ArrayList<>();
        ArrayList<String> i = new ArrayList<>();
        ArrayList<String> v = new ArrayList<>();

        n.add("1");
        n.add("2");
        n.add("3");

        i.add("");
        i.add("");
        i.add("");

        v.add("200");
        v.add("500");
        v.add("400");

        RecyclerView recyclerView = findViewById(R.id.HomeRecyclerView);
        NewRecycler list = new NewRecycler(this,name,image,visitor);
        recyclerView.setAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }
    public void taxibooking()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_seclect_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button book = dialog.findViewById(R.id.book);
        EditText destination = dialog.findViewById(R.id.destination);
        String uid = FirebaseAuth.getInstance().getUid();
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Taxibooking_data data = new Taxibooking_data(uid,destination.getText().toString(),"New");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Taxi_Booking");
                databaseReference.child(uid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(home.this, "Successfully Booked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void bottomnav()
    {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_profile :

                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        break;

                    case R.id.navigation_home :
                        startActivity(new Intent(getApplicationContext(),home.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navigation_scan :
                        startActivity(new Intent(getApplicationContext(), cameraActivity.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });
    }
//    public void setAdapterdetails()
//    {
//
//       HomeScreenAdapter hsa = new HomeScreenAdapter(this,mname,image,visited);
//       lv.setAdapter(hsa);
//    }

    ArrayList<String> mname = new ArrayList<>();
    ArrayList<String> image = new ArrayList<>() ;
    ArrayList<String> visited = new ArrayList<>() ;

    public void test()
    {
        mname.clear();
        image.clear();
        visited.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot monSnapshot : dataSnapshot.getChildren())
                {
                    monument_data data = monSnapshot.getValue(monument_data.class);
                    mname.add(data.getName()) ;
                    visited.add(data.getVisitors());
                    image.add(data.getDisp_image());
                }
                for (int i=0;i<mname.size();i++)
                {
                 //   Toast.makeText(home.this,mname.get(i), Toast.LENGTH_SHORT).show();
                }

                RecylcerFunction(image,mname,visited);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void ListViewClicked(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String m = list.getItemAtPosition(position).toString();
                String mname1 = m.replace(" ", "").toLowerCase();
                Intent i = new Intent(home.this, Search_result.class);
                i.putExtra("mname", mname1);
                startActivity(i);
                Toast.makeText(home.this, m, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package com.google.firebase.ml.mddd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.mddd.java.FirebaseMethods;
import com.google.firebase.ml.mddd.java.monument_data;

public class new_search extends AppCompatActivity {

    TextView name;
    Button b;
    String mname;
    DatabaseReference reference;
    monument_data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search2);
        name = findViewById(R.id.textView9);
        b = findViewById(R.id.b1);

        reference = FirebaseDatabase.getInstance().getReference("Monuments");
        bottomnav();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mname = name.getText().toString().trim();
                mname = mname.replace(" ","");
                mname = mname.toLowerCase();
                Toast.makeText(new_search.this, mname, Toast.LENGTH_SHORT).show();
               reference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       data = dataSnapshot.child(mname).getValue(monument_data.class);
                       if(data!=null)
                       {
                           Intent i = new Intent(new_search.this,Search_result.class);
                           i.putExtra("mname",mname);
                           startActivity(i);
                       }
                       else {
                         //  Toast.makeText(new_search.this, "no shuch data", Toast.LENGTH_SHORT).show();
                           reference.child(mname).setValue(new monument_data(name.getText().toString(),"","Curently no description","","",""));
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

            }
        });
    }
    public void bottomnav()
    {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_scan);

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
                        Intent i = (new Intent(getApplicationContext(),home.class));
                        i.putExtra("Size",new FirebaseMethods(new_search.this).getSize("Monuments"));
                        startActivity(i);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navigation_scan :
                        startActivity(new Intent(getApplicationContext(), new_search.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });
    }
}

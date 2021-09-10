package com.google.firebase.ml.mddd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.mddd.java.FirebaseMethods;
import com.google.firebase.ml.mddd.java.Taxi_mode;
import com.google.firebase.ml.mddd.java.UniversalImageLoader;
import com.google.firebase.ml.mddd.java.hotel_mode;
import com.google.firebase.ml.mddd.java.settings.profile_ImageAddapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class profile extends AppCompatActivity {
    FirebaseAuth mAuth;
    user_data gdata;
    int size;
    DatabaseReference dbreference;
    String uid;
    TextView uname,uemail,ucountry,uphone,t_hotel,t_taxi,t_logout,t_cprofile;
    ImageView profilephoto,i_hotel,i_taxi,i_logout,i_cprofile;
    LinearLayout mBottomSheet,Testing;
    BottomSheetBehavior mBottombehavior;
    FirebaseMethods fmethod = new FirebaseMethods(this);

    Toolbar toolbar;
    Dialog dialog  ;
    Dialog dialog2  ;
    ArrayList<String> taxi_data;
    String hotel_data;



String pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        uname = findViewById(R.id.username);
        uemail = findViewById(R.id.useremail);
        uphone = findViewById(R.id.userphone);
        ucountry = findViewById(R.id.usercountry);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        profilephoto = findViewById(R.id.pro_pic);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new Dialog(this) ;
        dialog2 = new Dialog(this) ;
        taxi_data = new ArrayList<>();



        dbreference = FirebaseDatabase.getInstance().getReference("Userdata");
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gdata= dataSnapshot.child(uid).getValue(user_data.class);
                setTextveiws(gdata);
               // pic=dataSnapshot.child(uid).getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_profile);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_home:
                        Intent i = (new Intent(getApplicationContext(), home.class));
                      //  i.putExtra("Size",size);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_scan:
                         startActivity(new Intent(getApplicationContext(), cameraActivity.class));
                       // startActivity(new Intent(getApplicationContext(), new_search.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
        initImageLoader();
        tempGridSetup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.hotel_frag:
            {
                Toast.makeText(this, "hotel", Toast.LENGTH_SHORT).show();
                hotel_click();
              break;
            }
            case R.id.taxi_frag:
            {
                Toast.makeText(this, "taxi", Toast.LENGTH_SHORT).show();
                taxi_click();
                break;
            }
            case R.id.edit_frag:
            {
                Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.logout:
            {
                logout1();
                break;
            }
        }
           return true;
    }

    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        names.add("Taj Mahal");
        names.add("Effile Tower");
        names.add("Leaning Tower");
        imgURLs.add("https://firebasestorage.googleapis.com/v0/b/icheck-262407.appspot.com/o/testing%2Ftajmahal.jpg?alt=media&token=95b9bb9f-268c-48b3-8af2-4145c9231984");
         imgURLs.add("https://firebasestorage.googleapis.com/v0/b/icheck-262407.appspot.com/o/testing%2FEffile.jpg?alt=media&token=95097588-30d1-4810-85df-2ace9c2c372b");
        imgURLs.add("https://firebasestorage.googleapis.com/v0/b/icheck-262407.appspot.com/o/testing%2FLeaning-Tower-of-Pisa-Italy%20(1).jpg?alt=media&token=a6827862-3f63-41a3-99ea-01f8b9911562");
        recycler(names,imgURLs);
    }
    public void recycler(ArrayList<String> names,ArrayList<String> imgURLs)
    {
        RecyclerView recyclerView = findViewById(R.id.image_disp);
        profile_ImageAddapter list = new profile_ImageAddapter(this,names,imgURLs);
        recyclerView.setAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    public void setProfileImage(String imgURL){

        UniversalImageLoader.setImage(imgURL, profilephoto, null, "");
    }

    public void logout1() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void setTextveiws(@NotNull user_data data)
    {
        uname.setText(data.name);
        ucountry.setText(data.country);
        uphone.setText(data.phone);
        uemail.setText(data.email);
        uemail=findViewById(R.id.useremail1);
        uemail.setText(data.email);
        uname=findViewById(R.id.username1);
        uname.setText(data.name);
        setProfileImage(data.getPropic_url());
        gdata=data;
        toolbar.setTitle(data.name);

    }

    public void change_pro_pic(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String imgURL = selectedImage.toString();

            FirebaseMethods fm = new FirebaseMethods(this);
            fm.uploadNewPhoto("pro_image",0,imgURL,selectedImage);
            setProfileImage(gdata.getPropic_url());
        }
        if (requestCode ==2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String imgURL = selectedImage.toString();
            taxi_data.add(0,imgURL);
            //Toast.makeText(this, imgURL, Toast.LENGTH_SHORT).show();
            TextView tv = dialog.findViewById(R.id.tv1);
            tv.setText("added");
        }
        if (requestCode ==3 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String imgURL = selectedImage.toString();
            taxi_data.add(1,imgURL);
            TextView tv = dialog.findViewById(R.id.tv2);
            tv.setText("added");
        }
        if (requestCode ==4 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String imgURL = selectedImage.toString();
            hotel_data = imgURL;
            TextView tv = dialog2.findViewById(R.id.tv1);
            tv.setText("added");
        }
    }
    public void taxi_click()
    {
        dialog.setContentView(R.layout.dialog_taxi);
        EditText cmodel = dialog.findViewById(R.id.car_model);
        EditText cnumber = dialog.findViewById(R.id.car_number);
        Button c1 = dialog.findViewById(R.id.b1);
        Button c2 = dialog.findViewById(R.id.b2);
        Button save = dialog.findViewById(R.id.save);
        ImageButton close = dialog.findViewById(R.id.close);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                taxi_data = null;
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 3);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(cnumber.getText().toString().isEmpty())
//                {
//                    Toast.makeText(profile.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
//                    cnumber.setError("Number required");
//                    return;
//                }
//                if(cmodel.getText().toString().isEmpty())
//                {
//                    Toast.makeText(profile.this, "Car Model Empty", Toast.LENGTH_SHORT).show();
//                    cmodel.setError("Car Model Required");
//                    return;
//                }
//                if(taxi_data.get(0).isEmpty() || taxi_data.get(1).isEmpty())
//                {
//                    Toast.makeText(profile.this, "Data required", Toast.LENGTH_SHORT).show();
//                }
                startActivity(new Intent(profile.this, Taxi_mode.class));


            }
        });
    }
    public void hotel_click()
    {
//        dialog2.setContentView(R.layout.dialog_hotel);
//        EditText hname = dialog2.findViewById(R.id.car_model);
//        EditText hlocation = dialog2.findViewById(R.id.car_number);
//        Button c1 = dialog2.findViewById(R.id.b1);
//        Button save = dialog2.findViewById(R.id.save);
//        ImageButton close = dialog2.findViewById(R.id.close);
//        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog2.show();
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog2.dismiss();
//                hotel_data = null;
//            }
//        });
//        c1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, 4);
//            }
//        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(hname.getText().toString().isEmpty())
//                {
//                    hname.setError("name required");
//                    hname.requestFocus();
//                    return;
//                }
//                if(hlocation.getText().toString().isEmpty())
//                {
//                    hlocation.setError("location Required");
//                    hlocation.requestFocus();
//                    return;
//                }
////                if(hotel_data.isEmpty())
////                {
////                    Toast.makeText(profile.this, "Data required", Toast.LENGTH_SHORT).show();
////                }
//
                Intent i = new Intent(profile.this, hotel_mode.class);
                i.putExtra("mode","E");
                i.putExtra("hname","xxxx");
                i.putExtra("hlocation","loc");
                startActivity(i);
//            }
//        });
    }

}
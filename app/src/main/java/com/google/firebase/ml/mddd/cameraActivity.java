package com.google.firebase.ml.mddd;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.mddd.java.monument_data;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.util.List;

public class cameraActivity extends AppCompatActivity {

    private android.R.attr Manifest;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.imageView123);
        botnav();
        databaseReference = FirebaseDatabase.getInstance().getReference("Monuments");


            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);

            FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance()
                    .getVisionCloudLandmarkDetector();
            Task<List<FirebaseVisionCloudLandmark>> result = detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionCloudLandmark>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionCloudLandmark> firebaseVisionCloudLandmarks) {

                            imageView.setImageBitmap(photo);
                            String landmark1="";
                            Toast.makeText(cameraActivity.this, firebaseVisionCloudLandmarks.get(0).getLandmark(), Toast.LENGTH_SHORT).show();
                            landmark1 = firebaseVisionCloudLandmarks.get(0).getLandmark();
                            String sdata = landmark1.replace(" ","").toLowerCase();

                            if(landmark1!=null)
                            {
                               // Toast.makeText(cameraActivity.this, "Searching", Toast.LENGTH_SHORT).show();
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Toast.makeText(cameraActivity.this, sdata, Toast.LENGTH_SHORT).show();
                                        monument_data data = snapshot.child(sdata).getValue(monument_data.class);
                                        Toast.makeText(cameraActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                                       // databaseReference.child(sdata).setValue(data);
                                            if(data!=null)
                                        {
                                           // Toast.makeText(cameraActivity.this, "okeeyt", Toast.LENGTH_SHORT).show();
                                          //  String mname1 = mname.get(position).replace(" ", "").toLowerCase();
                                            Intent i = new Intent(getApplicationContext(), Search_result.class);
                                            i.putExtra("mname", sdata);
                                            startActivity(i);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            for (FirebaseVisionCloudLandmark landmark: firebaseVisionCloudLandmarks) {


                                String landmarkName = landmark.getLandmark();


                                //Toast.makeText(cameraActivity.this, landmarkName, Toast.LENGTH_SHORT).show();



                                for (FirebaseVisionLatLng loc: landmark.getLocations()) {
                                    double latitude = loc.getLatitude();
                                    double longitude = loc.getLongitude();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                            Toast.makeText(cameraActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    void botnav()
        {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            navView.setSelectedItemId(R.id.navigation_scan);
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
        }

    public void openCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
           startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}